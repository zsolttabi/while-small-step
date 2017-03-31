package ast.expression;


import app.SimpleASTNode;
import utils.Element;
import utils.Tree;
import utils.Visitor;

public interface Expression extends Element<Tree.Node<SimpleASTNode>> {

    Expression step();

    @Override
    default Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
