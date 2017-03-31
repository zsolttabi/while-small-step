package ast.statement;

import ast.State;
import ast.expression.Expression;
import utils.Pair;

public class BadAssignment extends Assignment implements BadStatement {

    BadAssignment(String identifier, Expression value) {
        super(identifier, value);
    }

    @Override
    public Pair<Statement, State> step(State state) {
        return stuckStep(state);
    }

}
