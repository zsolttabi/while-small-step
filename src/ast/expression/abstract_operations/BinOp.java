package ast.expression.abstract_operations;

import app.SimpleASTNode;
import ast.State;
import ast.expression.And;
import ast.expression.Expression;
import ast.expression.Identifier;
import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import utils.Visitor;

import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
public class BinOp<T, V extends Value<T>, R> implements Expression {

    public static <T, V extends Value<T>, R> BinOp<T, V, R> of(String operator,
                                                                        Expression lhs,
                                                                        Expression rhs,
                                                                        Class<V> operandClass,
                                                                        Function<R, Value<R>> resultCtor,
                                                                        BiFunction<T, T, R> evalFun) {

        return lhs == null || rhs == null ? new BadBinOp<>(operator, lhs, rhs) : new BinOp<>(operator,
                lhs,
                rhs,
                operandClass,
                resultCtor,
                evalFun);
    }

    @Getter
    private final String operator;
    @Getter
    private final Expression lhs;
    @Getter
    private final Expression rhs;

    private final Class<V> operandClass;
    private final Function<R, Value<R>> resultCtor;
    private final BiFunction<T, T, R> evalFun;

    @Override
    public Expression step(State state) {

        if (!(lhs instanceof Value || lhs instanceof Identifier)) {
            return new BinOp<>(operator, lhs.step(state), rhs, operandClass, resultCtor, evalFun);
        }

        if (!(rhs instanceof Value || rhs instanceof Identifier)) {
            return new BinOp<>(operator, lhs, rhs.step(state), operandClass, resultCtor, evalFun);
        }

        Value lVal = lhs instanceof Identifier ? state.get((Identifier)lhs) : (Value) lhs;
        Value rVal = rhs instanceof Identifier ? state.get((Identifier)rhs) : (Value) rhs;

        if (lVal != null && rVal != null && operandClass.isAssignableFrom(lVal.getClass()) && operandClass.isAssignableFrom(rVal.getClass())) {
            return resultCtor.apply(evalFun.apply(operandClass.cast(lVal).getValue(), operandClass.cast(rVal).getValue()));
        }

        return new BadBinOp(operator, lhs, rhs);
    }

    protected <T, V extends Value<T>, R> Expression step(State state,
                                                         Class<V> valueClass,
                                                         Function<R, Value<R>> resultCtor,
                                                         BiFunction<Expression, Expression, BinOp> binOpCtor,
                                                         BiFunction<T, T, R> evalFun) {

        if (!(lhs instanceof Value || lhs instanceof Identifier)) {
            return binOpCtor.apply(lhs.step(state), rhs);
        }

        if (!(rhs instanceof Value || rhs instanceof Identifier)) {
            return binOpCtor.apply(lhs, rhs.step(state));
        }

        Value lVal = lhs instanceof Identifier ? state.get((Identifier)lhs) : (Value) lhs;
        Value rVal = rhs instanceof Identifier ? state.get((Identifier)rhs) : (Value) rhs;

        if (lVal != null && rVal != null && valueClass.isAssignableFrom(lVal.getClass()) && valueClass.isAssignableFrom(rVal.getClass())) {
            return resultCtor.apply(evalFun.apply(valueClass.cast(lVal).getValue(), valueClass.cast(rVal).getValue()));
        }

        return new BadBinOp(operator, lhs, rhs);
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
