package ast.statement;

import ast.State;
import ast.expression.interfaces.Expression;
import utils.Pair;

public class BadWhile extends While implements BadStatement {

    public BadWhile(Expression condition, Statement s) {
        super(condition, s);
    }

    @Override
    public Pair<Statement, State> step(State state) {
        return stuckStep(state);
    }
}
