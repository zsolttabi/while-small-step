package ast.expression.abstract_operations;

import ast.State;
import ast.expression.Expression;
import ast.expression.interfaces.Value;

import java.util.function.BiFunction;
import java.util.function.Function;

public class BadBinOp<T, V extends Value<T>, R> extends BinOp<T, V, R> {


    public BadBinOp(String operator, Expression lhs, Expression rhs) {
        super(operator, lhs, rhs, null, null, null);
    }

    @Override
    public Expression step(State state) {
        return this;
    }

}
