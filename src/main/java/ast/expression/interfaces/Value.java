package ast.expression.interfaces;

import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;
import viewmodel.interfaces.IASTVisitor;

public interface Value<T> extends Expression, IASTElement<Tree.Node<ASTNode>> {

    T getValue();

    @Override
    default Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return  visitor.visit(this);
    }
}
