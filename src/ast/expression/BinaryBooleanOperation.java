package ast.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class BinaryBooleanOperation<T> extends Expression<T> {

    @Getter
    private final BooleanExpression lhs;
    @Getter
    private final BooleanExpression rhs;

    protected EvaluatedExpression<Boolean> evaluate(BiFunction<Boolean, Boolean, Boolean> evalFun) {
        val lVal = lhs.evaluate();
        val rVal = rhs.evaluate();
        if(lVal instanceof BooleanValue && rVal instanceof BooleanValue) {
            return new EvaluatedExpression<>(evalFun.apply(((BooleanValue) lVal).getValue(), ((BooleanValue) rVal).getValue()));
        }
        throw new EvaluationException(Stream.of(lVal, rVal).collect(Collectors.toList()));
    }

}
