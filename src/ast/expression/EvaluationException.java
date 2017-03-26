package ast.expression;

import lombok.Getter;

import java.util.List;

public class EvaluationException extends RuntimeException {

    @Getter
    private final List<Expression<?>> expressions;

    public EvaluationException(List<Expression<?>> expressions) {
        this("Cannot evaluate expression", expressions);
    }

    public EvaluationException(String message, List<Expression<?>> expressions) {
        super(message);
        this.expressions = expressions;
    }

    public EvaluationException() {
        this(null);
    }

}
