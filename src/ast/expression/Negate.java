package ast.expression;

import ast.State;
import ast.expression.abstract_operations.UnOp;
import ast.expression.values.IntValue;

public class Negate extends UnOp {

    public Negate(Expression operand) {
        super(operand);
    }

    @Override
    public Expression step(State state) {
        return step(state, IntValue.class, IntValue::new, Negate::new, i -> -1 * i);
    }

}
