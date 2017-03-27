package ast.expression;

import ast.expression.interfaces.IntegerValue;

import java.util.function.BiFunction;

public abstract class BinaryRelationExpression extends BinaryExpression {

    public BinaryRelationExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression evaluate(BiFunction<Integer, Integer, Boolean> evalFun) {
        return evaluate(evalFun, IntegerValue.class);
    }

}
