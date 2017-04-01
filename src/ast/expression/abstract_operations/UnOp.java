package ast.expression.abstract_operations;

import app.SimpleASTNode;
import ast.expression.Expression;
import ast.expression.OpResult;
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

    protected <T, V extends Value<T>, R> Expression step(Class<V> valueClass, Function<Expression, UnOp> unOpCtor, Function<T, R> evalFun) {

        if (!(operand instanceof Value)) {
            return unOpCtor.apply(operand.step());
        }

        if (valueClass.isAssignableFrom(operand.getClass())) {
            return new OpResult<>(evalFun.apply(valueClass.cast(operand).getValue()));
        }

        return new BadUnOp(operand);
    }

    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
