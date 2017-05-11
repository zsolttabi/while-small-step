package ast;

import ast.statement.interfaces.Statement;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class StmConfig implements IConfiguration {

    @Getter
    private final Statement statement;
    @Getter
    private final State state;

    @Override
    public boolean isEndConfiguration() {
        return statement == null;
    }

    @Override
    public StmConfig step() {
        return statement.step(state);
    }

    public static StmConfig of(Statement statement, State state) {
        return new StmConfig(statement, state);
    }

    public static StmConfig endConfig(State state) {
        return of(null, state);
    }

}
