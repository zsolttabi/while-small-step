package ast.expression;

import ast.expression.interfaces.IntegerValue;

import java.util.function.BiFunction;

public abstract class BinaryIntegerExpression extends BinaryExpression {

    protected BinaryIntegerExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression evaluate(BiFunction<Integer, Integer, Integer> evalFun) {
        return evaluate(evalFun, IntegerValue.class);
    }

}