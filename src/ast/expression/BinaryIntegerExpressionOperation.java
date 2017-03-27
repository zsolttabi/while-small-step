package ast.expression;

import ast.expression.interfaces.IntegerValue;

import java.util.function.BiFunction;

public abstract class BinaryIntegerExpressionOperation extends BinaryExpressionOperation {

    protected BinaryIntegerExpressionOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression evaluate(BiFunction<Integer, Integer, Integer> evalFun) {
        return evaluate(IntegerValue.class, evalFun);
    }

}
