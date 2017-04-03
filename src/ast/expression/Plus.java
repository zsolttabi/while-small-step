package ast.expression;

import ast.State;
import ast.expression.abstract_operations.ArithBinOp;

public class Plus extends ArithBinOp {

    public Plus(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression step(State state) {
        return step(state, Plus::new, Integer::sum);
    }

}
