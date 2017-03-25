package ast.expression;

import lombok.Getter;

import java.util.List;

public class EvaluationException extends RuntimeException {

    @Getter
    private final List<Expression<?>> expressions;

    public EvaluationException(List<Expression<?>> expressions) {
        super("Cannot evaluate expression");
        this.expressions = expressions;
    }

    public EvaluationException() {
        this(null);
    }

}
