package ast.statement.interfaces;


import ast.State;
import utils.Pair;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;

public interface Statement extends IASTElement<Tree.Node<ASTNode>> {

    Pair<Statement, State> step(State state);

}
