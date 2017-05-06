package ast.statement;

import ast.State;
import ast.statement.bad_statements.BadSequence;
import ast.statement.interfaces.BadStatement;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;
import utils.Tree;
import viewmodel.interfaces.IASTElement;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTVisitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Sequence implements Statement, IASTElement<Tree.Node<ASTNode>> {

    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;

    public static Sequence of(Statement s1, Statement s2) {
        return s1 == null || s2 == null ? new BadSequence(s1, s2) : new Sequence(s1, s2);
    }

    @Override
    public Pair<Statement, State> step(State state) {

        Pair<Statement, State> s1NewConfig = s1.step(state);

        if (s1NewConfig.getFirst() instanceof BadStatement) {
            return Pair.of(new BadSequence(s1NewConfig.getFirst(), s2), state);
        }

        return s1NewConfig.getFirst() == null ?
                Pair.of(s2, state) : Pair.of(new Sequence(s1NewConfig.getFirst(), s2),
                s1NewConfig.getSecond());
    }

    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
