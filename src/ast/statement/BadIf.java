package ast.statement;

import ast.State;
import ast.expression.interfaces.Expression;
import utils.Pair;

public class BadIf extends If implements BadStatement {

    public BadIf(Expression condition, Statement s1, Statement s2) {
        super(condition, s1, s2);
    }

    @Override
    public Pair<Statement, State> step(State state) {
        return stuckStep(state);
    }

}
