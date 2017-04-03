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

import java.util.function.Function;

@RequiredArgsConstructor
public abstract class UnOp implements Expression {

    @Getter
    private final Expression operand;

    protected <T, V extends Value<T>, R> Expression step(State state,
                                                         Class<V> valueClass,
                                                         Function<R, Value<R>> resultCtor,
                                                         Function<Expression, UnOp> unOpCtor,
                                                         Function<T, R> evalFun) {

        if (!(operand instanceof Value || operand instanceof Identifier)) {
            return unOpCtor.apply(operand.step(state));
        }

        Value operVal = operand instanceof Identifier ? state.get((Identifier)operand) : (Value) operand;

        if ((operVal.getValue() != null && valueClass.isAssignableFrom(operVal.getClass()))) {
            return resultCtor.apply(evalFun.apply(valueClass.cast(operand).getValue()));
        }

        return new BadUnOp(operand);
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
