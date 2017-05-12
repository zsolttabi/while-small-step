package program.expressions;

public final class IntValue extends Value<Integer> implements IExpression {

    public IntValue(Integer value) {
        super(value);
    }

    public static IntValue of(Integer value) {
        return new IntValue(value);
    }

}
