package ast.expression;

public class BadBinaryExpression extends BinaryExpression {

    public BadBinaryExpression(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return this;
    }

}
