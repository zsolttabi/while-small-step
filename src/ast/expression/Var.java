package ast.expression;

import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.Setter;

public abstract class Var<T> implements Expression, Value<T> {

    @Getter
    private final String identifier;

    @Getter
    @Setter
    private T value;

    protected Var(String identifier, T value) {
        this.identifier = identifier;
        this.value = value;
    }

}
