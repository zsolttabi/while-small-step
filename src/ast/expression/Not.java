package ast.expression;

import ast.expression.abstract_operations.UnOp;
import ast.expression.interfaces.BooleanValue;

public class Not extends UnOp {
    public Not(Expression operand) {
        super(operand);
    }

    @Override
    public Expression evaluate() {
        return evaluate(BooleanValue.class, b -> !b);
    }

    @Override
    public Expression step() {
        return step(BooleanValue.class, Not::new, b -> !b);
    }

}
