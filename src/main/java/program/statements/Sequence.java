package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import program.Configuration;
import program.Configuration.ConfigType;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Sequence implements Statement {

    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;

    @Override
    public StatementConfiguration step(State state) {

        StatementConfiguration nextS1Config = s1.step(state);

        return nextS1Config.getConfigType() == ConfigType.TERMINATED ?
                new StatementConfiguration(s2, nextS1Config.getState(), ConfigType.INTERMEDIATE) :
                new StatementConfiguration(new Sequence(nextS1Config.getNode(), s2), nextS1Config.getState(), nextS1Config.getConfigType());
    }

    @Override
    public Configuration next(State state) {
        return s1.next(state);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
