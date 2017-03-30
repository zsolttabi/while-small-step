package ast.statement;

import ast.State;
import utils.Pair;

public class Skip implements Statement {

    @Override
    public Pair<Statement, State> step(State state) {
        return Pair.of(null, state);
    }

}
