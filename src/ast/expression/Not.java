package ast.expression;

import ast.State;
import ast.expression.abstract_operations.UnOp;
import ast.expression.values.BoolValue;

public class Not extends UnOp {

    public Not(Expression operand) {
        super(operand);
    }

    @Override
    public Expression step(State state) {
        return step(state, BoolValue.class, BoolValue::new, Not::new, b -> !b);
    }

}
