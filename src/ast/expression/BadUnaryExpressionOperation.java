package ast.expression;

public class BadUnaryExpressionOperation extends UnaryExpressionOperation {

    public BadUnaryExpressionOperation(Expression operand) {
        super(operand);
    }

    @Override
    public Expression evaluate() {
            return this;
    }
}
