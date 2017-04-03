package ast.statement.bad_statements;

import ast.State;
import ast.statement.Sequence;
import ast.statement.interfaces.BadStatement;
import ast.statement.interfaces.Statement;
import utils.Pair;

public class BadSequence extends Sequence implements BadStatement {

    public BadSequence(Statement s1, Statement s2) {
        super(s1, s2);
    }

    @Override
    public Pair<Statement, State> step(State state) {
        return stuckStep(state);
    }

}
