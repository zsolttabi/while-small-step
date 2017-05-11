package ast.statement.bad_statements;

import ast.State;
import ast.StmConfig;
import ast.expression.interfaces.Expression;
import ast.statement.If;
import ast.statement.interfaces.Statement;
import ast.statement.interfaces.StuckStatement;

public final class StuckIf extends If implements StuckStatement {

    public StuckIf(Expression condition, Statement s1, Statement s2) {
        super(condition, s1, s2);
    }

    @Override
    public StmConfig step(State state) {
        return StmConfig.of(this, state);
    }

}
