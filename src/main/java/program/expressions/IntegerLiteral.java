package program.expressions;

import java.math.BigInteger;

public class IntegerLiteral extends Literal {

    public IntegerLiteral(String value) {
        super(value);
    }

    @Override
    public IExpression copy() {
        return new IntegerLiteral(getValue());
    }

    @Override
    protected Value<?> convertToValue() {
        return new Value<>(new BigInteger(getValue()));
    }
}
