package ast.statement;

import ast.State;
import ast.expression.Expression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;

@RequiredArgsConstructor
public class While implements Statement {

    @Getter
    private final Expression condition;
    @Getter
    private final Statement s;

    @Override
    public Pair<Statement, State> step(State state) {
        return Pair.of(new If(condition, new Sequence(s, this), new Skip()), state);
    }
}
