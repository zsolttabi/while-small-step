package ast.statement.interfaces;


import app.SimpleASTNode;
import ast.State;
import utils.Element;
import utils.Pair;
import utils.Tree;

public interface Statement extends Element<Tree.Node<SimpleASTNode>> {

    Pair<Statement, State> step(State state);

}
