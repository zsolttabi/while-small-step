package ast.expression;

import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.Function;

@RequiredArgsConstructor
public abstract class UnaryExpressionOperation implements Expression {

    @Getter
    private final Expression operand;

    protected <T, V extends Value<T>, R> Expression evaluate(Class<V> operandClass, Function<T, R> evalFun) {
        val evaluated = operand.evaluate();
        if (operandClass.isAssignableFrom(evaluated.getClass())) {
            return new EvaluatedExpression<>(evalFun.apply(operandClass.cast(evaluated).getValue()));
        }
        return new BadUnaryExpressionOperation(operand);
    }
}
