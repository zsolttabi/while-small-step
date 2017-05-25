package program;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;
import viewmodel.interfaces.IVisitableNode;

import java.util.Set;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public abstract class Configuration implements IVisitableNode<Tree.Node<ASTNode>> {

    public enum ConfigType {
        INTERMEDIATE, STUCK, TERMINATED
    }

    @Getter
    private final IProgramElement node;
    @Getter
    private final State state;
    @Getter
    private final ConfigType configType;

    public Configuration step() {
        return node.step(state);
    }

    public Set<Configuration> peek() {
        return node.peek(state);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
