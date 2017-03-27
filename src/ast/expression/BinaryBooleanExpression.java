package ast.expression;

import ast.expression.interfaces.BooleanValue;

import java.util.function.BiFunction;

public abstract class BinaryBooleanExpression extends BinaryExpression {

    public BinaryBooleanExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression evaluate(BiFunction<Boolean, Boolean, Boolean> evalFun) {
        return evaluate(evalFun, BooleanValue.class);
    }

}
