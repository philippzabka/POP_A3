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
//                getEvaluationResult(((SelectionStatement) e).elseExpr);
//                System.out.println(symbolTable);
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
//            if(((AdditiveExpression) e).left == null || ((AdditiveExpression) e).right == null){
//                return getEvaluationResult(((AdditiveExpression) e).straight);
//            }
//            else {
//                getEvaluationResult(((AdditiveExpression) e).left);
//                getEvaluationResult(((AdditiveExpression) e).right);
//            }

            if(((AdditiveExpression) e).expressions.size() == 1){
                return getEvaluationResult(((AdditiveExpression) e).expressions.get(0));
            }
            else {
                System.out.println(((AdditiveExpression) e).expressions.size());
                Stack<String> varStack = new Stack<>();
                Stack<String> opStack = new Stack<>();
                for(int i = 0; i < ((AdditiveExpression) e).expressions.size(); i++){
                    varStack.push(getEvaluationResult(((AdditiveExpression) e).expressions.get(i)));
                }

                ((AdditiveExpression) e).operators.forEach(opStack::push);
                System.out.println("NUMS: " + varStack + " OPS: " + ((AdditiveExpression) e).operators);

                int left = 0;
                int right = 0;
//                if()
//                int left = symbolTable.get(numS)
//                int result = getAdditiveResult(numStack.pop(), numStack.pop(), opStack.pop())


            }
        }
        if(e instanceof PrimaryExpression){
            if(((PrimaryExpression) e).expr == null){
                return ((PrimaryExpression) e).value;
            }
            else {
                getEvaluationResult(((PrimaryExpression) e).expr);
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


