package ast.expression.operations;

import ast.State;
import ast.expression.interfaces.Expression;

public class BadBinOp<T, R> extends BinOp<T, R> {


    public BadBinOp(String operator, Expression lhs, Expression rhs) {
        super(operator, lhs, rhs, null, null, null);
    }

    @Override
    public Expression step(State state) {
        return this;
    }

}
