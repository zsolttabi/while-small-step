package program.statements;

import lombok.EqualsAndHashCode;
import lombok.ToString;
import program.Configuration;
import program.Configuration.ConfigType;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

import java.util.Collections;
import java.util.Set;

@EqualsAndHashCode
@ToString
public class Abort implements IStatement {

    @Override
    public StatementConfiguration step(State state) {
        return new StatementConfiguration(this, state, ConfigType.STUCK);
    }

    @Override
    public Set<Configuration> peek(State state) {
        return Collections.singleton(step(state));
    }

    @Override
    public IStatement copy() {
        return new Abort();
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
