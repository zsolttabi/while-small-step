package ast.expression.abstract_operations;

import ast.State;
import ast.expression.Expression;
import ast.expression.values.BoolValue;

import java.util.function.BiFunction;

public abstract class BoolBinOp extends BinOp {

    public BoolBinOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression step(State state, BiFunction<Expression, Expression, BinOp> binOpCtor, BiFunction<Boolean, Boolean, Boolean> evalFun) {
        return step(state, BoolValue.class, BoolValue::new, binOpCtor, evalFun);
    }

}
