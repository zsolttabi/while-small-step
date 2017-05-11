package ast.expression.values;


import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public final class IntValue implements Expression, Value<Integer> {

    @Getter
    private final Integer value;

    public static IntValue of(Integer value) {
        return new IntValue(value);
    }

}
