package ast.expression;

import ast.expression.abstract_operations.UnOp;
import ast.expression.interfaces.IntValue;

public class Negate extends UnOp {

    public Negate(Expression operand) {
        super(operand);
    }

    @Override
    public Expression step() {
        return step(IntValue.class, Negate::new, i -> -1 * i);
    }

}
