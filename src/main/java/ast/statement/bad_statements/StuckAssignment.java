package ast.statement.bad_statements;

import ast.State;
import ast.StmConfig;
import ast.expression.interfaces.Expression;
import ast.statement.Assignment;
import ast.statement.interfaces.StuckStatement;

public final class StuckAssignment extends Assignment implements StuckStatement {

    public StuckAssignment(Expression identifier, Expression value) {
        super(identifier, value);
    }

    @Override
    public StmConfig step(State state) {
        return StmConfig.of(this, state);
    }

}
