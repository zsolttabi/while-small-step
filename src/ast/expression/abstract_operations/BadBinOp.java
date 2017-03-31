package ast.expression.abstract_operations;

import ast.expression.Expression;

public class BadBinOp extends BinOp {

    BadBinOp(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression step() {
        return this;
    }

}
