package ast.process;

import ast.classes.*;

import java.util.*;

public class ExpressionProcessorToRiscV {

    private final List<Expression> exprList;
    public Map<String, Integer> symbolTable;
    public Map<String, String> registerTable;
    public Stack<String> registerStack;
    public List<String> instructions;
    private int instruction_counter;
    private int branch_instruction_counter;
    public ExpressionProcessorToRiscV(List<Expression> list) {
        this.exprList = list;
        symbolTable = new HashMap<>();
        registerTable = new HashMap<>();
        registerStack = new Stack<>();
        instructions = new ArrayList<>();
        instruction_counter = 0;
        branch_instruction_counter = 0;

        String[] registers = {"x1", "x2", "x3", "x4", "x5", "x6", "x7", "x8", "x9", "x10", "x11", "x12", "x13", "x14",
                              "x15", "x16", "x17", "x18", "x19", "x20", "x21", "x22"};
        for(String register:registers)
            registerTable.put(register, null);
    }

    public void initEvaluation() {
        for(Expression e: exprList){
            getEvaluationResult(e, "");
        }

        System.out.println("RegisterTable");
        for(Map.Entry<String,String> entry : registerTable.entrySet())
            System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());

        System.out.println("RegisterStack");
        for (String s : registerStack) System.out.println(s);

        System.out.println("Instructions");
        for(String instruction:instructions)
            System.out.println(instruction);
    }

    private String getEvaluationResult(Expression e, String token){
        if(e instanceof FunctionDefinition){
            return getEvaluationResult(((FunctionDefinition) e).exprBody, token);
        }
        if(e instanceof CompoundStatement){
            return getEvaluationResult(((CompoundStatement) e).exprMiddle, token);
        }
        if(e instanceof BlockItemList){
            for(int i = 0; i < ((BlockItemList) e).expressions.size(); i++){
                getEvaluationResult(((BlockItemList) e).expressions.get(i), token);
            }
        }
        if(e instanceof DeclaratorList){
            for(int i = 0; i < ((DeclaratorList) e).expressions.size(); i++){
                getEvaluationResult(((DeclaratorList) e).expressions.get(i), token);
            }
        }
        if(e instanceof ExpressionStatement){
            return getEvaluationResult(((ExpressionStatement) e).expr, token);
        }
        if(e instanceof SelectionStatement){
            getEvaluationResult(((SelectionStatement) e).ifClauseExpr, token);
            branch_instruction_counter = instruction_counter;
            getEvaluationResult(((SelectionStatement) e).ifExpr, token);
            getEvaluationResult(((SelectionStatement) e).elseExpr, "else");

            // CODEGEN BEGIN
            instructions.add(branch_instruction_counter++, "JAL x0,done");
            instructions.add(branch_instruction_counter++, "expr:");

            instructions.remove(instructions.size()-1);
            instructions.add("done:");
            instruction_counter++;
            instructions.add("ret");
            instruction_counter++;
            // CODEGEN END
        }
        if(e instanceof IterationStatement){
            symbolTable.put("start", 0);
            boolean result = Boolean.parseBoolean(getEvaluationResult(((IterationStatement) e).forConditionExpr, token));
            symbolTable.put("start", 1);
            while(result){
                getEvaluationResult(((IterationStatement) e).statementExpr, token);
                result = Boolean.parseBoolean(getEvaluationResult(((IterationStatement) e).forConditionExpr, token));
            }
        }
        if(e instanceof ForCondition){
            if(symbolTable.get("start") == 0) getEvaluationResult(((ForCondition) e).left, token);
            if(symbolTable.get("start") == 1) getEvaluationResult(((ForCondition) e).right, token);
            return getEvaluationResult(((ForCondition) e).middle, token);
        }
        if(e instanceof AssignmentExpression){
            if(((AssignmentExpression) e).left == null){
                return getEvaluationResult(((AssignmentExpression) e).right, token);
            }
            else {
                String resultLeft = getEvaluationResult(((AssignmentExpression) e).left, token);
                String operator = ((AssignmentExpression) e).operator;
                String resultRight = getEvaluationResult(((AssignmentExpression) e).right, token);
                if(symbolTable.containsKey(resultLeft)){
                    switch (operator){
                        case "=":
                            if(symbolTable.containsKey(resultRight)){
                                int result = symbolTable.get(resultRight);
                                symbolTable.put(resultLeft, result);
                            }
                            else {
                                symbolTable.put(resultLeft, Integer.parseInt(resultRight));
                            }
                            // CODEGEN BEGIN
                            // Get next free register and save reference to symbol table
                            for(Map.Entry<String,String> entry : registerTable.entrySet()){
                                if(entry.getValue() == null){
                                    registerTable.put(entry.getKey(), resultLeft);
                                    registerStack.push(entry.getKey());
                                    // Create risc instruction
                                    if(token.equals("")){
                                        instructions.add("ADDI " + entry.getKey() + ",x0," + symbolTable.get(resultLeft));
                                        instruction_counter++;
                                    }
                                    else{
                                        instructions.add(branch_instruction_counter, "ADDI " + entry.getKey() + ",x0," + symbolTable.get(resultLeft));
                                        branch_instruction_counter++;
                                    }
                                    break;
                                }
                            }
                            // CODEGEN END

                            break;
                        case "+=": {
                            int result = symbolTable.get(resultLeft);
                            if (symbolTable.containsKey(resultRight)) {
                                int result2 = symbolTable.get(resultRight);
                                result = result + result2;
                            } else {
                                result = result + Integer.parseInt(resultRight);
                            }
                            symbolTable.put(resultLeft, result);
                            break;
                        }
                        case "-=": {
                            int result = symbolTable.get(resultLeft);
                            if (symbolTable.containsKey(resultRight)) {
                                int result2 = symbolTable.get(resultRight);
                                result = result - result2;
                            } else {
                                result = result - Integer.parseInt(resultRight);
                            }
                            symbolTable.put(resultLeft, result);
                            break;
                        }
                    }
                }
            }
        }
        if(e instanceof RelationalExpression){
            if(((RelationalExpression) e).leftSide == null){
                return getEvaluationResult(((RelationalExpression) e).rightSide, token);
            }
            else {
                String leftSide = getEvaluationResult(((RelationalExpression) e).leftSide, token);
                String rightSide = getEvaluationResult(((RelationalExpression) e).rightSide, token);
                String operator = ((RelationalExpression) e).operator;

                int valLeft = symbolTable.get(leftSide);
                int valRight = symbolTable.get(rightSide);

                switch(operator) {
                    case "<":
                        // CODEGEN BEGIN
                        String register1 = getRegisterByValue(leftSide);
                        String register2 = getRegisterByValue(rightSide);
                        instructions.add("BLT " + register1 + "," + register2 + ",expr");
                        instruction_counter++;
                        // CODEGEN END

                        if(valLeft < valRight) return "true";
                        else return "false";
                    case ">":
                        if(valLeft > valRight) return "true";
                        else return "false";
                    case "<=":
                        if(valLeft <= valRight) return "true";
                        else return "false";
                    case ">=":
                        if(valLeft >= valRight) return "true";
                        else return "false";
                    case "==":
                        if(valLeft == valRight) return "true";
                        else return "false";
                    case "!=":
                        if(valLeft != valRight) return "true";
                        else return "false";
                }
            }
        }
        if(e instanceof AdditiveExpression){
            if(((AdditiveExpression) e).expressions.size() == 1){
                return getEvaluationResult(((AdditiveExpression) e).expressions.get(0), token);
            }
            else {
                ArrayList<String> vars = new ArrayList<>();
                for(int i = 0; i < ((AdditiveExpression) e).expressions.size(); i++){
                    vars.add(getEvaluationResult(((AdditiveExpression) e).expressions.get(i), token));
                }

                Collections.reverse(vars);
                Stack<String> varsStack = new Stack<>();
                vars.forEach(varsStack::push);

                int result = 0;
                for(String op: ((AdditiveExpression) e).operators){
                    String left = varsStack.pop();
                    String right = varsStack.pop();
                    if (symbolTable.containsKey(left) && symbolTable.containsKey(right)) {
                        result = getAdditiveResult(symbolTable.get(left), symbolTable.get(right), op);
                        varsStack.push(Integer.toString(result));
                        symbolTable.put(left+op+right, result);
                        // CODEGEN BEGIN
                        String register1 = getRegisterByValue(left);
                        String register2 = getRegisterByValue(right);
                        String register3 = getFreeRegister(left+op+right);
                        if(op.equals("+")) instructions.add("ADD "+register3+","+register1+","+register2);
                        if(op.equals("-")) instructions.add("SUB "+register3+","+register1+","+register2);
                        instruction_counter++;
                        // CODEGEN END

                    } else if (symbolTable.containsKey(left) && !symbolTable.containsKey(right)) {
                        result = getAdditiveResult(symbolTable.get(left), Integer.parseInt(right), op);
                        varsStack.push(Integer.toString(result));

                        // CODEGEN BEGIN
                        String register1 = getRegisterByValue(left);
                        String register2 = getFreeRegister(left+op+right);
                        instructions.add("ADDI "+register2+","+register1+","+right);
                        instruction_counter++;
                        // CODEGEN END

                    } else if (!symbolTable.containsKey(left) && symbolTable.containsKey(right)) {
                        result = getAdditiveResult(Integer.parseInt(left), symbolTable.get(right), op);
                        varsStack.push(Integer.toString(result));

                        // CODEGEN BEGIN
                        String register1 = getRegisterByValue(right);
                        String register2 = getFreeRegister(left+op+right);
                        instructions.add("ADDI "+register2+","+register1+","+left);
                        instruction_counter++;
                        // CODEGEN END

                    } else {
                        result = getAdditiveResult(Integer.parseInt(left), Integer.parseInt(right), op);
                        varsStack.push(Integer.toString(result));

                        // CODEGEN BEGIN
                        String register2;
                        if(Integer.parseInt(left) == 4 && Integer.parseInt(right) == 1) {
                            register2 = getFreeRegister(right);
                            registerStack.pop();
                            instructions.add("ADDI "+register2+",x0"+","+right);
                            instruction_counter++;
                        }
                        else register2 = registerStack.pop();
                        String register1 = registerStack.pop();
                        String register3 = getFreeRegister(left+op+right);
                        if(op.equals("+")) instructions.add("ADD "+register3+","+register1+","+register2);
                        if(op.equals("-")) instructions.add("SUB "+register3+","+register1+","+register2);
                        instruction_counter++;
                        // CODEGEN END
                    }
                }
                return Integer.toString(result);
            }
        }
        if(e instanceof PrimaryExpression){
            if(((PrimaryExpression) e).expr == null){
                return ((PrimaryExpression) e).value;
            }
            else {
                return getEvaluationResult(((PrimaryExpression) e).expr, token);
            }
        }
        if(e instanceof Declaration){
            if(((Declaration) e).right == null){
                return getEvaluationResult(((Declaration) e).left, token);
            }
            else {
                getEvaluationResult(((Declaration) e).left, token);
                getEvaluationResult(((Declaration) e).right, token);
            }
        }
        if(e instanceof DirectDeclarator){
            symbolTable.put(((DirectDeclarator) e).name, null);
            return ((DirectDeclarator) e).name;
        }
//        if(e instanceof DeclarationSpecifiers){
//            for(int i = 0; i < ((DeclarationSpecifiers) e).specifiers.size(); i++){
//                System.out.println(((DeclarationSpecifiers) e).specifiers.get(i));
//            }
//        }
        return "";
    }

    private int getAdditiveResult(int left, int right, String op){
        return switch (op) {
            case "+" -> left + right;
            case "-" -> left - right;
            default -> 0;
        };
    }

    private String getRegisterByValue(String value){
        for(Map.Entry<String, String> entry: registerTable.entrySet()) {
            if (Objects.equals(entry.getValue(), value)) {
                return entry.getKey();
            }
        }
        return "";
    }

    private String getFreeRegister(String value){
        for(Map.Entry<String,String> entry : registerTable.entrySet()){
            if(entry.getValue() == null){
                registerTable.put(entry.getKey(), value);
                registerStack.push(entry.getKey());
                return entry.getKey();
            }
        }
        return "";
    }
}


