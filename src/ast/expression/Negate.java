package ast.expression;

import ast.expression.abstract_operations.UnOp;
import ast.expression.interfaces.IntegerValue;

public class Negate extends UnOp {

    public Negate(Expression operand) {
        super(operand);
    }

    @Override
    public Expression evaluate() {
        return evaluate(IntegerValue.class, i -> i * -1);
    }

    @Override
    public Expression step() {
        return step(IntegerValue.class, Negate::new, i -> -1 * i);
    }

}
