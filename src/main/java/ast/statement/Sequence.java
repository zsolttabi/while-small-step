package ast.statement;

import app.SimpleASTNode;
import ast.State;
import ast.statement.bad_statements.BadSequence;
import ast.statement.interfaces.BadStatement;
import ast.statement.interfaces.Statement;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Element;
import utils.Pair;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
@EqualsAndHashCode
public class Sequence implements Statement, Element<Tree.Node<SimpleASTNode>> {

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
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
