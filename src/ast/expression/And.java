package ast.expression;

public class And extends BinaryBooleanExpressionOperation {
    public And(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate(Boolean::logicalAnd);
    }
}
