package ast.expression.values;


import ast.State;
import ast.expression.Expression;
import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class IntValue implements Expression, Value<Integer> {

    @Getter
    private final Integer value;

    @Override
    public Expression step(State state) {
        return this;
    }

}
