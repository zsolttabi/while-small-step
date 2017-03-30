package ast.expression.abstract_operations;

import ast.expression.Expression;
import ast.expression.OpResult;
import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;

import java.util.function.BiFunction;

@RequiredArgsConstructor
public abstract class BinOp implements Expression {

    @Getter
    private final Expression lhs;
    @Getter
    private final Expression rhs;

    protected <T, V extends Value<T>, R> Expression evaluate(Class<V> operandClass, BiFunction<T, T, R> evalFun) {
        val lVal = lhs.evaluate();
        val rVal = rhs.evaluate();
        if (operandClass.isAssignableFrom(lVal.getClass()) && operandClass.isAssignableFrom(rVal.getClass())) {
            return new OpResult<>(evalFun.apply(operandClass.cast(lVal).getValue(), operandClass.cast(rVal).getValue()));
        }
        return new BadBinOp(lVal, rVal);
    }


    protected <T, V extends Value<T>, R> Expression step(Class<V> valueClass, BiFunction<Expression, Expression, BinOp> binOpCtor, BiFunction<T, T, R> evalFun) {
        if (!(lhs instanceof Value)) {
            return binOpCtor.apply(lhs.step(), rhs);
        }

        if (!(rhs instanceof Value)) {
            return binOpCtor.apply(lhs, rhs.step());
        }

        if (valueClass.isAssignableFrom(lhs.getClass()) && valueClass.isAssignableFrom(rhs.getClass())) {
            return new OpResult<>(evalFun.apply(valueClass.cast(lhs).getValue(), valueClass.cast(rhs).getValue()));
        }

        return new BadBinOp(lhs, rhs);
    }

}
