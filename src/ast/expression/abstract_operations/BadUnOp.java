package ast.expression.abstract_operations;

import ast.State;
import ast.expression.Expression;

public class BadUnOp extends UnOp {

    BadUnOp(Expression operand) {
        super(operand);
    }

    @Override
    public Expression step(State state) {
        return this;
    }

}
