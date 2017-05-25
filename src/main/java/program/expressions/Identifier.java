package program.expressions;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.Configuration.ConfigType;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;
import viewmodel.interfaces.IVisitableNode;

import java.util.Collections;
import java.util.Set;

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
    public Set<Configuration> peek(State state) {
        return Collections.singleton(new ExpressionConfiguration(this, state, ConfigType.INTERMEDIATE));
    }

    @Override
    public IExpression copy() {
        return new Identifier(identifier);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
