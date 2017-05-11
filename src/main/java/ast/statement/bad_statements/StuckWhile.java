package ast.statement.bad_statements;

import ast.State;
import ast.StmConfig;
import ast.expression.interfaces.Expression;
import ast.statement.While;
import ast.statement.interfaces.Statement;
import ast.statement.interfaces.StuckStatement;

public final class StuckWhile extends While implements StuckStatement {

    public StuckWhile(Expression condition, Statement s) {
        super(condition, s);
    }

    @Override
    public StmConfig step(State state) {
        return StmConfig.of(this, state);
    }

}
