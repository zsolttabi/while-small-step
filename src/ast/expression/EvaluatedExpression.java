package ast.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EvaluatedExpression<T> extends Expression<T> implements Value<T> {

    @Getter
    private final T value;

    @Override
    public EvaluatedExpression<T> evaluate() {
        return this;
    }

}
