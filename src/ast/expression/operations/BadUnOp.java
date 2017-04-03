package ast.expression.operations;

import ast.State;
import ast.expression.interfaces.Expression;

public class BadUnOp<T, R> extends UnOp<T, R> {

    BadUnOp(String operator, Expression operand) {
        super(operator, operand, null, null, null);
    }

    @Override
    public Expression step(State state) {
        return this;
    }

}
