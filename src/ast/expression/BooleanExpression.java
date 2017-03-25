package ast.expression;

public abstract class BooleanExpression extends Expression<Boolean> {

    @Override
    public abstract BooleanExpression evaluate();

}
