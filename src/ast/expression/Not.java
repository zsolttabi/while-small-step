package ast.expression;

import ast.expression.interfaces.BooleanValue;

public class Not extends UnaryExpressionOperation {
    public Not(Expression operand) {
        super(operand);
    }

    @Override
    public Expression evaluate() {
        return evaluate(BooleanValue.class, b -> !b);
    }

}
