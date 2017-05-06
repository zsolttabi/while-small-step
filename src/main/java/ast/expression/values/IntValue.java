package ast.expression.values;


import ast.State;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class IntValue implements Expression, Value<Integer> {

    @Getter
    private final Integer value;

    @Override
    public Expression step(State state) {
        return this;
    }

}
