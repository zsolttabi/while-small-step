package ast.expression.values;


import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public final class BoolValue implements Expression, Value<Boolean> {

    @Getter
    private final Boolean value;

    public static BoolValue of(Boolean value) {
        return new BoolValue(value);
    }

}
