package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration;
import program.State;
import program.expressions.ExpressionConfiguration;
import program.expressions.IExpression;
import program.expressions.Value;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

import static program.Configuration.ConfigType.INTERMEDIATE;
import static program.Configuration.ConfigType.STUCK;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class If implements IStatement {

    @Getter
    private final IExpression condition;
    @Getter
    private final IStatement s1;
    @Getter
    private final IStatement s2;

    @Override
    public StatementConfiguration step(State state) {

        if (!(condition instanceof Value)) {
            ExpressionConfiguration condConf = condition.step(state);
            return new StatementConfiguration(new If(condConf.getNode(), s1, s2),
                    condConf.getState(),
                    condConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
        }

        Object condValue = ((Value) condition).getValue();

        if (!(condValue instanceof Boolean)) {
            return new StatementConfiguration(this, state, STUCK);
        }

        return new StatementConfiguration((Boolean) condValue ? s1 : s2, state, INTERMEDIATE);
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
