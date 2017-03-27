package ast.expression;

import ast.expression.interfaces.IntegerValue;

import java.util.function.BiFunction;

public abstract class BinaryRelationExpressionOperation extends BinaryExpressionOperation {

    public BinaryRelationExpressionOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression evaluate(BiFunction<Integer, Integer, Boolean> evalFun) {
        return evaluate(IntegerValue.class, evalFun);
    }

}
