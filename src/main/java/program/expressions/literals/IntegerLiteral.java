package program.expressions.literals;

import program.IProgramElement;
import program.expressions.Value;

import java.math.BigInteger;

public class IntegerLiteral extends Literal {

    public IntegerLiteral(String value) {
        super(value);
    }

    @Override
    public IProgramElement copy() {
        return new IntegerLiteral(getValue());
    }

    @Override
    protected Value<?> convertToValue() {
        return new Value<>(new BigInteger(getValue()));
    }
}
