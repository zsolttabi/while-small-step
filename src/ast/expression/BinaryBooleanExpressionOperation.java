package ast.expression;

import ast.expression.interfaces.BooleanValue;

import java.util.function.BiFunction;

public abstract class BinaryBooleanExpressionOperation extends BinaryExpressionOperation {

    public BinaryBooleanExpressionOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression evaluate(BiFunction<Boolean, Boolean, Boolean> evalFun) {
        return evaluate(BooleanValue.class, evalFun);
    }

}
