package program.expressions;

import program.Configuration.ConfigType;
import program.State;
import utils.Tree.Node;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;
import viewmodel.interfaces.IVisitableNode;

public interface Value<T> extends Expression, IVisitableNode<Node<ASTNode>> {

    T getValue();

    @Override
    default ExpressionConfiguration step(State state) {
        return new ExpressionConfiguration(this, state, ConfigType.TERMINATED);
    }

    @Override
    default ExpressionConfiguration next(State state) {
        return step(state);
    }

    @Override
    default Node<ASTNode> accept(INodeVisitor<Node<ASTNode>> visitor) {
        return  visitor.visit(this);
    }

}
