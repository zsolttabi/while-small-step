package ast;

import ast.statement.interfaces.Statement;

public class StuckStmConfig extends StmConfig {

    public StuckStmConfig(Statement statement, State state) {
        super(statement, state);
    }

    @Override
    public boolean isEndConfiguration() {
        return false;
    }

    @Override
    public StuckStmConfig step() {
        return this;
    }

    public static StuckStmConfig of(Statement statement, State state) {
        return new StuckStmConfig(statement, state);
    }

}
