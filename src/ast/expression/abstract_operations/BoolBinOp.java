package ast.expression.abstract_operations;

import ast.expression.Expression;
import ast.expression.interfaces.BoolValue;

import java.util.function.BiFunction;

public abstract class BoolBinOp extends BinOp {

    public BoolBinOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression step(BiFunction<Expression, Expression, BinOp> binOpCtor, BiFunction<Boolean, Boolean, Boolean> evalFun) {
        return step(BoolValue.class, binOpCtor, evalFun);
    }

}
