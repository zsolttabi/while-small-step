package program.statements;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import program.Configuration.ConfigType;
import program.State;
import program.expressions.IExpression;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.INodeVisitor;

@RequiredArgsConstructor
@EqualsAndHashCode
@ToString
public class While implements IStatement {

    @Getter
    private final IExpression condition;
    @Getter
    private final IStatement stm;

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
