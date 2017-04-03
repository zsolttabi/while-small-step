package ast.expression.abstract_operations;

import ast.State;
import ast.expression.Expression;
import ast.expression.values.BoolValue;
import ast.expression.values.IntValue;

import java.util.function.BiFunction;

public abstract class RelBinOp extends BinOp {

    public RelBinOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression step(State state, BiFunction<Expression, Expression, BinOp> binOpCtor, BiFunction<Integer, Integer, Boolean> evalFun) {
        return step(state, IntValue.class, BoolValue::new, binOpCtor, evalFun);
    }


}
