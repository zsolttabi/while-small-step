package ast.expression;

import ast.ExprConfig;
import ast.State;
import ast.expression.interfaces.StuckExpression;


public final class StuckIdentifier extends Identifier implements StuckExpression {

    public StuckIdentifier(String identifier) {
        super(identifier);
    }

    @Override
    public ExprConfig step(State state) {
        return ExprConfig.of(this, state);
    }

}
