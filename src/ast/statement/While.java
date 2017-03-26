package ast.statement;

import ast.State;
import ast.expression.BooleanExpression;
import ast.expression.Expression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;

@RequiredArgsConstructor
public class While extends Statement {

    @Getter
    private final Expression<Boolean> condition;
    @Getter
    private final Statement s;

    @Override
    public Pair<Statement, State> run(State state) {
        return Pair.of(new If(condition, new Sequence(s, this), new Skip()), state);
    }
}
