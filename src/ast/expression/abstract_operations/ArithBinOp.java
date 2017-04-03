package ast.expression.abstract_operations;

import ast.State;
import ast.expression.Expression;
import ast.expression.values.IntValue;

import java.util.function.BiFunction;

public abstract class ArithBinOp extends BinOp {

    protected ArithBinOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    protected Expression step(State state, BiFunction<Expression, Expression, BinOp> binOpCtor, BiFunction<Integer, Integer, Integer> evalFun) {
        return step(state, IntValue.class, IntValue::new, binOpCtor, evalFun);
    }

}
