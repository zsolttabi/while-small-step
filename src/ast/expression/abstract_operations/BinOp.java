package ast.expression.abstract_operations;

import app.SimpleASTNode;
import ast.State;
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
public abstract class BinOp implements Expression {

    @Getter
    private final Expression lhs;
    @Getter
    private final Expression rhs;

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

        return new BadBinOp(lhs, rhs);
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
