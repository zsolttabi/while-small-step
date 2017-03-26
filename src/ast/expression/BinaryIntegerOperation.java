package ast.expression;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
public abstract class BinaryIntegerOperation<T> extends Expression<T> {

    @Getter
    protected final IntegerExpression lhs;
    @Getter
    protected final IntegerExpression rhs;

    protected EvaluatedExpression<T> evaluate(BiFunction<Integer, Integer, T> evalFun) {
        val lVal = lhs.evaluate();
        val rVal = rhs.evaluate();
        if(lVal instanceof IntegerValue && rVal instanceof IntegerValue) {
            return new EvaluatedExpression<>(evalFun.apply(((IntegerValue) lVal).getValue(), ((IntegerValue) rVal).getValue()));
        }
        throw new EvaluationException(Stream.of(lVal, rVal).collect(Collectors.toList()));
    }

}
