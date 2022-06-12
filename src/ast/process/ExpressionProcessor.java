package ast.process;

import ast.classes.*;
import java.util.*;
public class ExpressionProcessor {
    private final List<Expression> exprList;
    public Map<String, Integer> symbolTable;
    public ExpressionProcessor(List<Expression> list) {
        this.exprList = list;
        symbolTable = new HashMap<>();
    }

    public void initEvaluation() {
        for(Expression e: exprList){
            getEvaluationResult(e);
        }
    }

    private String getEvaluationResult(Expression e){
        if(e instanceof FunctionDefinition){
            return getEvaluationResult(((FunctionDefinition) e).exprBody);
        }
        if(e instanceof CompoundStatement){
            return getEvaluationResult(((CompoundStatement) e).exprMiddle);
        }
        if(e instanceof BlockItemList){
            for(int i = 0; i < ((BlockItemList) e).expressions.size(); i++){
                getEvaluationResult(((BlockItemList) e).expressions.get(i));
            }
        }
        if(e instanceof DeclaratorList){
            for(int i = 0; i < ((DeclaratorList) e).expressions.size(); i++){
                getEvaluationResult(((DeclaratorList) e).expressions.get(i));
            }
        }
        if(e instanceof ExpressionStatement){
            return getEvaluationResult(((ExpressionStatement) e).expr);
        }
        if(e instanceof SelectionStatement){
            boolean result = Boolean.parseBoolean(getEvaluationResult(((SelectionStatement) e).ifClauseExpr));
            if(result) getEvaluationResult(((SelectionStatement) e).ifExpr);
            else getEvaluationResult(((SelectionStatement) e).elseExpr);
        }
        if(e instanceof IterationStatement){
            symbolTable.put("start", 0);
            boolean result = Boolean.parseBoolean(getEvaluationResult(((IterationStatement) e).forConditionExpr));
            symbolTable.put("start", 1);
            while(result){
                getEvaluationResult(((IterationStatement) e).statementExpr);
                result = Boolean.parseBoolean(getEvaluationResult(((IterationStatement) e).forConditionExpr));
            }
        }
        if(e instanceof ForCondition){
            if(symbolTable.get("start") == 0) getEvaluationResult(((ForCondition) e).left);
            if(symbolTable.get("start") == 1) getEvaluationResult(((ForCondition) e).right);
            return getEvaluationResult(((ForCondition) e).middle);
        }
        if(e instanceof AssignmentExpression){
            if(((AssignmentExpression) e).left == null){
                return getEvaluationResult(((AssignmentExpression) e).right);
            }
            else {
                String resultLeft = getEvaluationResult(((AssignmentExpression) e).left);
                String operator = ((AssignmentExpression) e).operator;
                String resultRight = getEvaluationResult(((AssignmentExpression) e).right);

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

                int result = 0;
                for(String op: ((AdditiveExpression) e).operators){
                    String left = varsStack.pop();
                    String right = varsStack.pop();
                    if (symbolTable.containsKey(left) && symbolTable.containsKey(right)) {
                        result = getAdditiveResult(symbolTable.get(left), symbolTable.get(right), op);
                        varsStack.push(Integer.toString(result));
                    } else if (symbolTable.containsKey(left) && !symbolTable.containsKey(right)) {
                        result = getAdditiveResult(symbolTable.get(left), Integer.parseInt(right), op);
                        varsStack.push(Integer.toString(result));
                    } else if (!symbolTable.containsKey(left) && symbolTable.containsKey(right)) {
                        result = getAdditiveResult(Integer.parseInt(left), symbolTable.get(right), op);
                        varsStack.push(Integer.toString(result));
                    } else {
                        result = getAdditiveResult(Integer.parseInt(left), Integer.parseInt(right), op);
                        varsStack.push(Integer.toString(result));
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
                return getEvaluationResult(((PrimaryExpression) e).expr);
            }
        }
        if(e instanceof Declaration){
            if(((Declaration) e).right == null){
                return getEvaluationResult(((Declaration) e).left);
            }
            else {
                getEvaluationResult(((Declaration) e).left);
                getEvaluationResult(((Declaration) e).right);
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
}


