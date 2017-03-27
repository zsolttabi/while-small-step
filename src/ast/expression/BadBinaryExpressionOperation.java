package ast.expression;

public class BadBinaryExpressionOperation extends BinaryExpressionOperation {

    public BadBinaryExpressionOperation(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return this;
    }

}
