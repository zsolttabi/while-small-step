package program.statements;

import lombok.EqualsAndHashCode;
import program.Configuration.ConfigType;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

@EqualsAndHashCode
public class Skip implements Statement {

    @Override
    public StatementConfiguration step(State state) {
        return new StatementConfiguration(null, state, ConfigType.TERMINATED);
    }

    @Override
    public StatementConfiguration next(State state) {
        return step(state);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
