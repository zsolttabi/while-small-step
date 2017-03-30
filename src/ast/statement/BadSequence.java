package ast.statement;

import ast.State;
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
