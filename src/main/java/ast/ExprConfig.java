package ast;

import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor(staticName = "of")
public class ExprConfig implements IConfiguration {

    @Getter
    private final Expression expression;
    @Getter
    private final State state;

    @Override
    public boolean isEndConfiguration() {
        return expression instanceof Value;
    }

    @Override
    public ExprConfig step() {
        return expression.step(state);
    }

}
