package ast.statement;


import app.SimpleASTNode;
import ast.State;
import utils.Element;
import utils.Pair;
import utils.Tree;
import utils.Tree.Node;
import utils.Visitor;

public interface Statement extends Element<Node<SimpleASTNode>> {

    Pair<Statement, State> step(State state);

    @Override
    default Node<SimpleASTNode> accept(Visitor<Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
