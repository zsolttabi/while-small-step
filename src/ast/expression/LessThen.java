package ast.expression;

import ast.State;
import ast.expression.abstract_operations.RelBinOp;

public class LessThen extends RelBinOp {

    public LessThen(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression step(State state) {
        return step(state, LessThen::new, (i1,i2) -> i1 < i2);
    }
}
