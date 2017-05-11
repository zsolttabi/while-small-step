package ast.expression.operations.bad_operations;

import ast.ExprConfig;
import ast.State;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.StuckExpression;
import ast.expression.operations.UnOp;

public class StuckUnOp<T, R> extends UnOp<T, R> implements StuckExpression {

    public StuckUnOp(String operator, Expression operand) {
        super(operator, operand, null, null, null);
    }

    @Override
    public ExprConfig step(State state) {
        return ExprConfig.of(this, state);
    }

}
