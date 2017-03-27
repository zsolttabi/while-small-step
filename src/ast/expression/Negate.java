package ast.expression;

import ast.expression.interfaces.IntegerValue;

public class Negate extends UnaryExpressionOperation {
    public Negate(Expression operand) {
        super(operand);
    }

    @Override
    public Expression evaluate() {
        return evaluate(IntegerValue.class, i -> i * -1);
    }
}
