package ast.expression;

import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EvaluatedExpression<T> implements Expression, Value<T> {

    @Getter
    private final T value;

    @Override
    public EvaluatedExpression evaluate() {
        return this;
    }

}
