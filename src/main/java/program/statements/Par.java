package program.statements;

import lombok.Getter;
import program.Configuration;
import program.State;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

import java.util.HashSet;
import java.util.Set;

import static program.Configuration.ConfigType.INTERMEDIATE;
import static program.Configuration.ConfigType.STUCK;
import static program.IProgramElement.choose;


public class Par implements IStatement {

    @Getter
    private final IStatement s1;
    @Getter
    private final IStatement s2;
    private final IStatement stuck;

    public Par(IStatement s1, IStatement s2) {
        this.s1 = s1;
        this.s2 = s2;
        this.stuck = null;
    }

    private Par(IStatement s1, IStatement s2, IStatement stuck) {
        this.s1 = s1;
        this.s2 = s2;
        this.stuck = stuck;
    }

    private static IStatement getS(IStatement chosen, StatementConfiguration chosenConf, IStatement s) {
        return s == chosen ? chosenConf.getNode() : s;
    }

    @Override
    public StatementConfiguration step(State state) {

        IStatement chosen = stuck != null ? (stuck == s1 ? s2 : s1) : choose(s1, s2);

        StatementConfiguration chosenConf = chosen.step(state);

        if (chosenConf.getConfigType() == Configuration.ConfigType.TERMINATED) {
            return new StatementConfiguration(s1 == chosen ? s2 : s1, chosenConf.getState(), INTERMEDIATE);
        }

        return new StatementConfiguration(
                new Par(getS(chosen, chosenConf, s1), getS(chosen, chosenConf, s2),
                        chosenConf.getConfigType() == STUCK ? chosen : null),
                chosenConf.getState(),
                stuck != null && chosenConf.getConfigType() == STUCK ? STUCK : INTERMEDIATE);
    }

    @Override
    public Set<Configuration> peek(State state) {
        Set<Configuration> next = new HashSet<>();
        next.addAll(s1.peek(state));
        next.addAll(s2.peek(state));
        return next;
    }

    @Override
    public IStatement copy() {
        return new Par(s1.copy(), s2.copy(), stuck == null ? null : stuck.copy());
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
