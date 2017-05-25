package program.expressions;

import program.State;
import program.SyntaxError;

import static program.Configuration.ConfigType.STUCK;

public class ExpressionSyntaxError extends SyntaxError implements IExpression {
    public ExpressionSyntaxError(String text) {
        super(text);
    }

    @Override
    public ExpressionConfiguration step(State state) {
        return new ExpressionConfiguration(copy(), state, STUCK);
    }

    @Override
    public IExpression copy() {
        return new ExpressionSyntaxError(getText());
    }

}
