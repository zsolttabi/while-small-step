package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import program.Configuration.ConfigType;
import program.State;
import program.expressions.Expression;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class While implements Statement {

    @Getter
    private final Expression condition;
    @Getter
    private final Statement stm;

    @Override
    public StatementConfiguration step(State state) {
        return new StatementConfiguration(new If(condition, new Sequence(stm, this), new Skip()), state, ConfigType.INTERMEDIATE);
    }

    @Override
    public StatementConfiguration next(State state) {
        return new StatementConfiguration(this, state, ConfigType.INTERMEDIATE);
    }

    @Override
    public Tree.Node<ASTNode> accept(INodeVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
