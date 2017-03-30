package ast.statement;

import ast.State;
import ast.expression.Expression;
import ast.expression.values.Var;
import utils.Pair;

public class BadAssignment extends Assignment implements BadStatement {

    public BadAssignment(Var<?> var, Expression value) {
        super(var, value);
    }

    @Override
    public Pair<Statement, State> step(State state) {
        return stuckStep(state);
    }

}
