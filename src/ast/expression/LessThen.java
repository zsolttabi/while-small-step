package ast.expression;

public class LessThen extends BinaryRelationExpressionOperation {
    public LessThen(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate((i1,i2) -> i1 < i2);
    }
}
