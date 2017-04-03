package ast.expression;

import ast.State;
import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class OpResult<T> implements Expression, Value<T> {

    @Getter
    private final T value;

    @Override
    public Expression step(State state) {
        return this;
    }

}
