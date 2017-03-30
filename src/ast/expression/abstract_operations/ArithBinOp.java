package ast.expression.abstract_operations;

import ast.expression.Expression;
import ast.expression.interfaces.IntegerValue;

import java.util.function.BiFunction;

public abstract class ArithBinOp extends BinOp {

    protected ArithBinOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression evaluate(BiFunction<Integer, Integer, Integer> evalFun) {
        return evaluate(IntegerValue.class, evalFun);
    }

    protected Expression step(BiFunction<Expression, Expression, BinOp> binOpCtor, BiFunction<Integer, Integer, Integer> evalFun) {
        return step(IntegerValue.class, binOpCtor, evalFun);
    }

}
