package ast.expression.abstract_operations;

import ast.expression.Expression;
import ast.expression.interfaces.IntegerValue;

import java.util.function.BiFunction;

public abstract class RelBinOp extends BinOp {

    public RelBinOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression evaluate(BiFunction<Integer, Integer, Boolean> evalFun) {
        return evaluate(IntegerValue.class, evalFun);
    }

    protected Expression step(BiFunction<Expression, Expression, BinOp> binOpCtor, BiFunction<Integer, Integer, Boolean> evalFun) {
        return step(IntegerValue.class, binOpCtor, evalFun);
    }


}
