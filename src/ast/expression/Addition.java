package ast.expression;

public class Addition extends BinaryIntegerExpression {

    public Addition(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate(Integer::sum);
    }

}
