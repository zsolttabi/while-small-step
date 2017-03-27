package ast.expression;

import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public abstract class BinaryExpressionOperation implements Expression {

    @Getter
    private final Expression lhs;
    @Getter
    private final Expression rhs;

    protected <T, V extends Value<T>, R> Expression evaluate(Class<V> operandClass, BiFunction<T, T, R> evalFun) {
        val lVal = lhs.evaluate();
        val rVal = rhs.evaluate();
        if (operandClass.isAssignableFrom(lVal.getClass()) && operandClass.isAssignableFrom(rVal.getClass())) {
            return new EvaluatedExpression<>(evalFun.apply(operandClass.cast(lVal).getValue(), operandClass.cast(rVal).getValue()));
        }
        return new BadBinaryExpressionOperation(lVal, rVal);
    }

}
