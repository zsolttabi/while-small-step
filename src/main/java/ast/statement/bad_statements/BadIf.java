package ast.statement.bad_statements;

import ast.State;
import ast.expression.interfaces.Expression;
import ast.statement.If;
import ast.statement.interfaces.BadStatement;
import ast.statement.interfaces.Statement;
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
