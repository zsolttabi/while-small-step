package ast.expression;

public class Plus extends BinaryIntegerExpressionOperation {
    protected Plus(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate(Integer::sum);
    }
}
