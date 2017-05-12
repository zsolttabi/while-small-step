package program.expressions;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration.ConfigType;
import program.State;
import utils.Tree.Node;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;
import viewmodel.interfaces.IVisitableNode;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Value<T> implements IExpression, IVisitableNode<Node<ASTNode>> {

    @Getter
    private final T value;

    @Override
    public ExpressionConfiguration step(State state) {
        return new ExpressionConfiguration(this, state, ConfigType.TERMINATED);
    }

    @Override
    public ExpressionConfiguration next(State state) {
        return step(state);
    }

    @Override
    public Node<ASTNode> accept(INodeVisitor<Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
