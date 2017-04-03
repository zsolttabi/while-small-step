package ast.expression.interfaces;

import app.SimpleASTNode;
import ast.expression.Expression;
import utils.Element;
import utils.Tree;
import utils.Visitor;

public interface Value<T> extends Expression, Element<Tree.Node<SimpleASTNode>> {

    T getValue();

    @Override
    default Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return  visitor.visit(this);
    }
}
