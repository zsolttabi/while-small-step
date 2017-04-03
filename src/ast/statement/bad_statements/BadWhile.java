package ast.statement.bad_statements;

import ast.State;
import ast.expression.interfaces.Expression;
import ast.statement.While;
import ast.statement.interfaces.BadStatement;
import ast.statement.interfaces.Statement;
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
