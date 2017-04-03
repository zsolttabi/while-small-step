package ast.expression;

import ast.State;
import ast.expression.abstract_operations.ArithBinOp;

public class Minus extends ArithBinOp {

    public Minus(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression step(State state) {
        return step(state, Minus::new, (i1, i2) -> i1 - i2);
    }
}
