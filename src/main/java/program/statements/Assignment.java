package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import program.Configuration;
import program.Configuration.ConfigType;
import program.State;
import program.expressions.Expression;
import program.expressions.ExpressionConfiguration;
import program.expressions.Identifier;
import program.expressions.Value;
import utils.Tree.Node;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

@EqualsAndHashCode
@RequiredArgsConstructor
public class Assignment implements Statement {

    @Getter
    private final Expression identifier;
    @Getter
    private final Expression value;

    @Override
    public StatementConfiguration step(State state) {

        if (!(identifier instanceof Identifier)) {
            return new StatementConfiguration(this, state, ConfigType.STUCK);
        }

        Identifier id = (Identifier) identifier;
        Value currentValue = state.get(id);

        ExpressionConfiguration nextValueConfig = value.step(state);
        if (nextValueConfig.getConfigType() == ConfigType.TERMINATED) {
            State newState = state.copy();
            if (currentValue == null || value.getClass() == currentValue.getClass()) {
                newState.set(id, (Value) value);
                return new StatementConfiguration(null, newState, ConfigType.TERMINATED);
            }
            return new StatementConfiguration(this, state, ConfigType.STUCK);
        }

        return new StatementConfiguration(new Assignment(identifier, nextValueConfig.getNode()),
                nextValueConfig.getState(),
                nextValueConfig.getConfigType());
    }

    @Override
    public Configuration next(State state) {
        if (!(value instanceof Value)) {
            return value.next(state);
        }
        return step(state);
    }

    @Override
    public Node<ASTNode> accept(INodeVisitor<Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
