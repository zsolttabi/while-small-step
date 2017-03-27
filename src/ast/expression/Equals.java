package ast.expression;

public class Equals extends BinaryRelationExpressionOperation {

    public Equals(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate(Object::equals);
    }

}
