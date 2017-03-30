package ast.statement;

import ast.State;
import utils.Pair;


public interface BadStatement extends Statement {

    default Pair<Statement, State> stuckStep(State state) {
        return Pair.of(this, state);
    }

}
