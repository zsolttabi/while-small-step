package ast.statement.bad_statements;

import ast.State;
import ast.StmConfig;
import ast.statement.Sequence;
import ast.statement.interfaces.Statement;
import ast.statement.interfaces.StuckStatement;

public final class StuckSequence extends Sequence implements StuckStatement {

    public StuckSequence(Statement s1, Statement s2) {
        super(s1, s2);
    }

    @Override
    public StmConfig step(State state) {
        return StmConfig.of(this, state);
    }

}
