package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import program.Configuration;
import program.Configuration.ConfigType;
import program.State;
import program.expressions.BoolValue;
import program.expressions.Expression;
import program.expressions.ExpressionConfiguration;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class If implements Statement {

    @Getter
    private final Expression condition;
    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;

    @Override
    public StatementConfiguration step(State state) {

        ExpressionConfiguration nextCondConfig = condition.step(state);

        if (nextCondConfig.getConfigType() == ConfigType.TERMINATED) {
            if (nextCondConfig.getNode() instanceof BoolValue) {
                return new StatementConfiguration(((BoolValue) nextCondConfig.getNode()).getValue() ? s1 : s2,
                        nextCondConfig.getState(),
                        ConfigType.INTERMEDIATE);
            }
            return new StatementConfiguration(new If(nextCondConfig.getNode(), s1, s2),
                    nextCondConfig.getState(),
                    ConfigType.STUCK);
        }

        return new StatementConfiguration(new If(nextCondConfig.getNode(), s1, s2),
                nextCondConfig.getState(),
                nextCondConfig.getConfigType());
    }

    @Override
    public Configuration next(State state) {
        return condition.next(state);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
