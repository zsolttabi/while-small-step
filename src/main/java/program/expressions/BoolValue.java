package program.expressions;

public final class BoolValue extends Value<Boolean> implements IExpression {

    public BoolValue(Boolean value) {
        super(value);
    }

    public static BoolValue of(Boolean value) {
        return new BoolValue(value);
    }

}
