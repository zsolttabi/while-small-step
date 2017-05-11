package ast.expression.interfaces;


import ast.ExprConfig;
import ast.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTElement;

public interface Expression extends IASTElement<Tree.Node<ASTNode>> {

    ExprConfig step(State state);

}
