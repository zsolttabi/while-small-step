package ast.expression;

import ast.State;
import ast.expression.abstract_operations.RelBinOp;

public class Equals extends RelBinOp {

    public Equals(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression step(State state) {
        return step(state, Equals::new, Object::equals);
    }

}
