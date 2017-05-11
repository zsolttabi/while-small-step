package ast.expression.operations.bad_operations;

import ast.ExprConfig;
import ast.State;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.StuckExpression;
import ast.expression.operations.BinOp;

public class StuckBinOp<T, R> extends BinOp<T, R> implements StuckExpression {


    public StuckBinOp(String operator, Expression lhs, Expression rhs) {
        super(operator, lhs, rhs, null, null, null);
    }

    @Override
    public ExprConfig step(State state) {
        return ExprConfig.of(this, state);
    }

}
