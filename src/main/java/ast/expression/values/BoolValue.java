package ast.expression.values;


import ast.State;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class BoolValue implements Expression, Value<Boolean> {

    @Getter
    private final Boolean value;

    @Override
    public Expression step(State state) {
        return this;
    }

}
