package program.expressions;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration.ConfigType;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;
import viewmodel.interfaces.IVisitableNode;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Identifier implements IExpression, IVisitableNode<Tree.Node<ASTNode>> {

    @Getter
    private final String identifier;

    @Override
    public ExpressionConfiguration step(State state) {
        Value<?> value = state.get(this);
        if (value == null) {
            return new ExpressionConfiguration(this, state, ConfigType.STUCK);
        } else {
            return new ExpressionConfiguration(value, state, ConfigType.TERMINATED);
        }
    }

    @Override
    public ExpressionConfiguration next(State state) {
        return new ExpressionConfiguration(this, state, ConfigType.INTERMEDIATE);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
