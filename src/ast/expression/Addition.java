package ast.expression;

public class Addition extends BinaryIntegerExpressionOperation {

    public Addition(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate(Integer::sum);
    }

}
