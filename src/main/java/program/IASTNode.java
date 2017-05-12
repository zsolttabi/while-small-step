package program;

import utils.Tree.Node;
import viewmodel.ASTNode;
import viewmodel.interfaces.IVisitableNode;

public interface IASTNode extends IVisitableNode<Node<ASTNode>> {

    Configuration step(State state);

    Configuration next(State state);

}
