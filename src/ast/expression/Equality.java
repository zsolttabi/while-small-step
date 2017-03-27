package ast.expression;

public class Equality extends BinaryRelationExpression {

    public Equality(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate(Object::equals);
    }

}
