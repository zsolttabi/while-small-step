package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.State;
import program.expressions.ExpressionConfiguration;
import program.expressions.IExpression;
import program.expressions.Identifier;
import program.expressions.Value;
import utils.Tree.Node;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

import static program.Configuration.ConfigType.*;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class Assignment implements IStatement {

    @Getter
    private final IExpression identifier;
    @Getter
    private final IExpression value;

    @Override
    public StatementConfiguration step(State state) {

        if (!(identifier instanceof Identifier)) {
            return new StatementConfiguration(this, state, STUCK);
        }

        if (!(value instanceof Value)) {
            ExpressionConfiguration valueConf = value.step(state);
            return new StatementConfiguration(new Assignment(identifier, valueConf.getNode()),
                    valueConf.getState(),
                    valueConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
        }

        Identifier id = (Identifier) identifier;
        Value val = (Value) this.value;

        Value currentValue = state.get(id);
        if (currentValue != null && currentValue.getValue().getClass() != val.getValue().getClass()) {
            return new StatementConfiguration(this, state, STUCK);
        }

        State newState = state.copy();
        newState.set(id, val);
        return new StatementConfiguration(this, newState, TERMINATED);
    }

    @Override
    public Configuration next(State state) {
        if (!(value instanceof Value)) {
            return value.next(state);
        }
        StatementConfiguration stepConfiguration = step(state);
        return new StatementConfiguration(this, stepConfiguration.getState(), stepConfiguration.getConfigType());
    }

    @Override
    public Node<ASTNode> accept(INodeVisitor<Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
