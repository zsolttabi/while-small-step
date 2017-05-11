package ast.statement.interfaces;


import ast.State;
import ast.StmConfig;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;

public interface Statement extends IASTElement<Tree.Node<ASTNode>> {

    StmConfig step(State state);

}
