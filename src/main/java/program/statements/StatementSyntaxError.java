package program.statements;

import program.State;
import program.SyntaxError;

import static program.Configuration.ConfigType.STUCK;

public class StatementSyntaxError extends SyntaxError implements IStatement {
    public StatementSyntaxError(String text) {
        super(text);
    }

    @Override
    public StatementConfiguration step(State state) {
        return new StatementConfiguration(this, state, STUCK);
    }

    @Override
    public IStatement copy() {
        return new StatementSyntaxError(getText());
    }

}
