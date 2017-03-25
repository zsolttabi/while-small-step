package ast.statement;

import ast.State;
import utils.Pair;

public class Skip extends Statement {

    @Override
    public Pair<Statement, State> run(State state) {
        return Pair.of(null, state);
    }

}
