package ast;

import java.util.*;

public class ExpressionProcessor {

    private List<Expression> exprList;
    public Map<String, Integer> symbolTable;

    public ExpressionProcessor(List<Expression> list) {
        this.exprList = list;
        symbolTable = new HashMap<>();
    }

    public List<String> getEvaluationResults() {
        List<String> evaluations = new ArrayList<>();

        for(Expression e: exprList){
            getEvaluationResult(e);
//            if(e instanceof VariableDeclaration) {
//                VariableDeclaration decl = (VariableDeclaration) e;
//                values.put(decl.id, decl.value);
//            }
//            else { // if instanceof Nis Number, Variable, Addition, Subtraction
//                String input = e.toString();
//                int result = getEvalResult(e);
//                evaluations.add(input + " is " + result);
//            }
        }
        return evaluations;
    }

    private String getEvaluationResult(Expression e){
//        System.out.println(e.getClass());
//        String resultLeft = "";
//        String resultRight = "";
        if(e instanceof FunctionDefinition){
            getEvaluationResult(((FunctionDefinition) e).exprBody);
            return "";
        }
        if(e instanceof CompoundStatement){
            getEvaluationResult(((CompoundStatement) e).exprMiddle);
            return "";
        }
        if(e instanceof BlockItemList){
            for(int i = 0; i < ((BlockItemList) e).expressions.size(); i++){
                getEvaluationResult(((BlockItemList) e).expressions.get(i));
            }
            return "";
        }
        if(e instanceof DeclaratorList){
            for(int i = 0; i < ((DeclaratorList) e).expressions.size(); i++){
                getEvaluationResult(((DeclaratorList) e).expressions.get(i));
            }
            return "";
        }
        if(e instanceof ExpressionStatement){
            getEvaluationResult(((ExpressionStatement) e).expr);
        }
        if(e instanceof SelectionStatement){
            boolean result = Boolean.parseBoolean(getEvaluationResult(((SelectionStatement) e).ifClauseExpr));
            if(result){
                getEvaluationResult(((SelectionStatement) e).ifExpr);
                System.out.println(symbolTable);
            }
            else {
                getEvaluationResult(((SelectionStatement) e).elseExpr);
                System.out.println(symbolTable);
            }
            return "";
        }
        if(e instanceof AssignmentExpression){
            if(((AssignmentExpression) e).left == null){
                return getEvaluationResult(((AssignmentExpression) e).right);
            }
            else {
                String resultLeft = getEvaluationResult(((AssignmentExpression) e).left);
                String resultRight = getEvaluationResult(((AssignmentExpression) e).right);

                if(symbolTable.containsKey(resultLeft)){
                    symbolTable.put(resultLeft, Integer.parseInt(resultRight));
                }
            }
        }
        if(e instanceof RelationalExpression){
            if(((RelationalExpression) e).leftSide == null){
                return getEvaluationResult(((RelationalExpression) e).rightSide);
            }
            else {
                String leftSide = getEvaluationResult(((RelationalExpression) e).leftSide);
                String rightSide = getEvaluationResult(((RelationalExpression) e).rightSide);
                String operator = ((RelationalExpression) e).operator;

                int valLeft = symbolTable.get(leftSide);
                int valRight = symbolTable.get(rightSide);
                switch(operator) {
                    case "<":
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
                    default:
//                        return "";
                }
            }
        }
        if(e instanceof AdditiveExpression){
            if(((AdditiveExpression) e).expressions.size() == 1){
                return getEvaluationResult(((AdditiveExpression) e).expressions.get(0));
            }
            else {
                ArrayList<String> vars = new ArrayList<>();
                for(int i = 0; i < ((AdditiveExpression) e).expressions.size(); i++){
                    vars.add(getEvaluationResult(((AdditiveExpression) e).expressions.get(i)));
                }

                Collections.reverse(vars);
                Stack<String> varsStack = new Stack<>();
                vars.forEach(varsStack::push);
//                System.out.println("STACK"+varsStack);
//                System.out.println("NUMS: " + vars + " OPS: " + ((AdditiveExpression) e).operators);

                int result = 0;
                for(String op: ((AdditiveExpression) e).operators){
                    String left = varsStack.pop();
                    String right = varsStack.pop();
//                    System.out.println("POP:" + left +" "+ right);
                    if (symbolTable.containsKey(left) && symbolTable.containsKey(right)) {
//                        System.out.println("INTM: " + left + " " + right);
                        result = getAdditiveResult(symbolTable.get(left), symbolTable.get(right), op);
                        varsStack.push(Integer.toString(result));
//                        System.out.println("INTMR: " + result);
                    } else if (symbolTable.containsKey(left) && !symbolTable.containsKey(right)) {
//                        System.out.println("INTM: " + left + " " + right);
                        result = getAdditiveResult(symbolTable.get(left), Integer.parseInt(right), op);
                        varsStack.push(Integer.toString(result));
//                        System.out.println("INTMR: " + result);
                    } else if (!symbolTable.containsKey(left) && symbolTable.containsKey(right)) {
//                        System.out.println("INTM: " + left + " " + right);
                        result = getAdditiveResult(Integer.parseInt(left), symbolTable.get(right), op);
                        varsStack.push(Integer.toString(result));
//                        System.out.println("INTMR: " + result);
                    } else {
//                        System.out.println("INTM: " + left + " " + right);
                        result = getAdditiveResult(Integer.parseInt(left), Integer.parseInt(right), op);
                        varsStack.push(Integer.toString(result));
//                        System.out.println("INTMR: " + result);
                    }
                }
//                System.out.println("RES: " + result);
                return Integer.toString(result);
            }
        }
        if(e instanceof PrimaryExpression){
            if(((PrimaryExpression) e).expr == null){
                return ((PrimaryExpression) e).value;
            }
            else {
                return getEvaluationResult(((PrimaryExpression) e).expr);
            }
        }
        if(e instanceof Declaration){
            if(((Declaration) e).right == null){
                getEvaluationResult(((Declaration) e).left);
            }
            else {
                getEvaluationResult(((Declaration) e).left);
                getEvaluationResult(((Declaration) e).right);
            }
            return "";
        }
        if(e instanceof DirectDeclarator){
            symbolTable.put(((DirectDeclarator) e).name, null);
            return ((DirectDeclarator) e).name;
        }
        if(e instanceof DeclarationSpecifiers){
            for(int i = 0; i < ((DeclarationSpecifiers) e).specifiers.size(); i++){
                System.out.println(((DeclarationSpecifiers) e).specifiers.get(i));
            }
            return "";
        }

        System.out.println(symbolTable);
        return "";
    }

    private int getAdditiveResult(int left, int right, String op){
        return switch (op) {
            case "+" -> left + right;
            case "-" -> left - right;
            default -> 0;
        };
    }
}


