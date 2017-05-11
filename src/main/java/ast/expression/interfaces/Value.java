package ast.expression.interfaces;

import ast.ExprConfig;
import ast.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;
import viewmodel.interfaces.IASTVisitor;

public interface Value<T> extends Expression, IASTElement<Tree.Node<ASTNode>> {

    T getValue();

    @Override
    default ExprConfig step(State state) {
        return ExprConfig.of(this, state);
    }

    @Override
    default Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return  visitor.visit(this);
    }
}
