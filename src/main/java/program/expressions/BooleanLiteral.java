package program.expressions;

public class BooleanLiteral extends Literal {
    public BooleanLiteral(String value) {
        super(value);
    }

    @Override
    public IExpression copy() {
        return new BooleanLiteral(getValue());
    }

    @Override
    protected Value<?> convertToValue() {
        return new Value<>(Boolean.parseBoolean(getValue()));
    }
}
