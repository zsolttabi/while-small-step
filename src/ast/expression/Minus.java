package ast.expression;

public class Minus extends BinaryIntegerExpressionOperation {
    protected Minus(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate((i1, i2) -> i1 - i2);
    }
}
