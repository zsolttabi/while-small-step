package ast.expression.interfaces;


import ast.State;
import utils.Tree;
import viewmodel.interfaces.IASTElement;
import viewmodel.ASTNode;

public interface Expression extends IASTElement<Tree.Node<ASTNode>> {

    Expression step(State state);

}
