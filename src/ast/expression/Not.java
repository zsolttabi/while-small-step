package ast.expression;

import ast.expression.abstract_operations.UnOp;
import ast.expression.interfaces.BoolValue;

public class Not extends UnOp {

    public Not(Expression operand) {
        super(operand);
    }

    @Override
    public Expression evaluate() {
        return evaluate(BoolValue.class, b -> !b);
    }

    @Override
    public Expression step() {
        return step(BoolValue.class, Not::new, b -> !b);
    }

}
