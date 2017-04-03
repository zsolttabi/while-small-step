package ast.expression;

import ast.State;
import ast.expression.abstract_operations.BoolBinOp;

public class And extends BoolBinOp {

    public And(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression step(State state) {
        return step(state, And::new, Boolean::logicalAnd);
    }
}
