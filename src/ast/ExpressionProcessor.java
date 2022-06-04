package ast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    private int getEvaluationResult(Expression e){
        System.out.println(e.getClass());
        if(e instanceof FunctionDefinition){
            getEvaluationResult(((FunctionDefinition) e).exprBody);
            return 0;
        }
        if(e instanceof CompoundStatement){
            getEvaluationResult(((CompoundStatement) e).exprMiddle);
            return 0;
        }
        if(e instanceof BlockItemList){
            for(int i = 0; i < ((BlockItemList) e).expressions.size(); i++){
                getEvaluationResult(((BlockItemList) e).expressions.get(i));
            }
        }
        if(e instanceof SelectionStatement){
            getEvaluationResult(((SelectionStatement) e).ifClauseExpr);
        }
        if(e instanceof AssignmentExpression){
//            getEvaluationResult(((AssignmentExpression) e).left);
            getEvaluationResult(((AssignmentExpression) e).right);
//            System.out.println(((AssignmentExpression) e).operator);
        }
        if(e instanceof RelationalExpression){
            getEvaluationResult(((RelationalExpression) e).leftSide);
            getEvaluationResult(((RelationalExpression) e).rightSide);
            System.out.println(((RelationalExpression) e).operator);
        }
        if(e instanceof AdditiveExpression){
            System.out.println(((AdditiveExpression) e).expressions.size());
            getEvaluationResult(((AdditiveExpression) e).expressions.get(0));
        }
        if(e instanceof PrimaryExpression){
            System.out.println(((PrimaryExpression) e).value);
        }
        return 0;
    }
}
