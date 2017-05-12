package program;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;
import viewmodel.interfaces.IVisitableNode;

@RequiredArgsConstructor
public abstract class Configuration implements IVisitableNode<Tree.Node<ASTNode>> {

    public enum ConfigType {
        INTERMEDIATE, STUCK, TERMINATED
    }

    @Getter
    private final IASTNode node;
    @Getter
    private final State state;
    @Getter
    private final ConfigType configType;

    public Configuration step() {
        return node.step(state);
    }

    public Configuration next() {
        return node.next(state);
    }


    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
