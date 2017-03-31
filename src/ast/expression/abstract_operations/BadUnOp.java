package ast.expression.abstract_operations;

import ast.expression.Expression;

public class BadUnOp extends UnOp {

    BadUnOp(Expression operand) {
        super(operand);
    }

    @Override
    public Expression step() {
        return this;
    }

}
