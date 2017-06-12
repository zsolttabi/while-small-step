package program.expressions.literals;

import program.IProgramElement;
import program.expressions.Value;

public class BooleanLiteral extends Literal {

    public BooleanLiteral(String value) {
        super(value);
    }

    @Override
    public IProgramElement copy() {
        return new BooleanLiteral(getValue());
    }

    @Override
    protected Value<?> convertToValue() {
        return new Value<>(Boolean.parseBoolean(getValue()));
    }
}
