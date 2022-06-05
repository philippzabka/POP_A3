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
//                System.out.println("SIZE: "+((AdditiveExpression) e).expressions.size());
//                Stack<String> varStack = new Stack<>();
//                Stack<String> opStack = new Stack<>();
                ArrayList<String> vars = new ArrayList<>();
                for(int i = 0; i < ((AdditiveExpression) e).expressions.size(); i++){
                    vars.add(getEvaluationResult(((AdditiveExpression) e).expressions.get(i)));
                }

//                Collections.reverse(((AdditiveExpression) e).operators);
//                ((AdditiveExpression) e).operators.forEach(opStack::push);
                System.out.println("NUMS: " + vars + " OPS: " + ((AdditiveExpression) e).operators);

                int result = 0;
                for(String op: ((AdditiveExpression) e).operators){
                    for(int i = 0; i < vars.size() - 1; i++){
                        if(i == 0) {
                            if (symbolTable.containsKey(vars.get(i)) && symbolTable.containsKey(vars.get(i + 1))) {
                                int left = symbolTable.get(vars.get(i));
                                int right = symbolTable.get(vars.get(i + 1));
                                System.out.println("INTM: " + left + " " + right);
                                result += getAdditiveResult(left, right, op);
                                System.out.println("INTMR: " + result);
                            } else if (symbolTable.containsKey(vars.get(i)) && !symbolTable.containsKey(vars.get(i + 1))) {
                                int left = symbolTable.get(vars.get(i));
                                int right = Integer.parseInt(vars.get(i + 1));
                                System.out.println("INTM: " + left + " " + right);
                                result += getAdditiveResult(left, right, op);
                                System.out.println("INTMR: " + result);
                            } else if (!symbolTable.containsKey(vars.get(i)) && symbolTable.containsKey(vars.get(i + 1))) {
                                int left = Integer.parseInt(vars.get(i));
                                int right = symbolTable.get(vars.get(i + 1));
                                System.out.println("INTM: " + left + " " + right);
                                result += getAdditiveResult(left, right, op);
                                System.out.println("INTMR: " + result);
                            } else {
                                int left = Integer.parseInt(vars.get(i));
                                int right = Integer.parseInt(vars.get(i + 1));
                                System.out.println("INTM: " + left + " " + right);
                                result += getAdditiveResult(left, right, op);
                                System.out.println("INTMR: " + result);
                            }
                        }
                        else {
                            if(symbolTable.containsKey(vars.get(i+1))){
                                int right = symbolTable.get(vars.get(i+1));
                                result += getAdditiveResult(result, right, op);
                            }
                            else {
                                int right = Integer.parseInt(vars.get(i+1));
                                result += getAdditiveResult(result, right, op);
                            }
                        }
                    }
                }
                System.out.println("RES: " + result);
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


