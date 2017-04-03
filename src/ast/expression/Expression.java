package ast.expression;


import app.SimpleASTNode;
import ast.State;
import utils.Element;
import utils.Tree;

public interface Expression extends Element<Tree.Node<SimpleASTNode>> {

    Expression step(State state);

}
