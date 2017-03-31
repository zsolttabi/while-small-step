package ast.expression.abstract_operations;

import ast.expression.Expression;
import ast.expression.interfaces.IntValue;

import java.util.function.BiFunction;

public abstract class RelBinOp extends BinOp {

    public RelBinOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression step(BiFunction<Expression, Expression, BinOp> binOpCtor, BiFunction<Integer, Integer, Boolean> evalFun) {
        return step(IntValue.class, binOpCtor, evalFun);
    }


}
