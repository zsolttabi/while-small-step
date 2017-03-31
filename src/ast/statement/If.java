package ast.statement;

import ast.State;
import ast.expression.Expression;
import ast.expression.interfaces.BoolValue;
import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;

@RequiredArgsConstructor
public class If implements Statement {

    @Getter
    private final Expression condition;
    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;

    @Override
    public Pair<Statement, State> step(State state) {

        if (!(condition instanceof Value)) {
            return Pair.of(new If(condition.step(), s1, s2), state);
        }

        if (condition instanceof BoolValue) {
            return Pair.of(((BoolValue) condition).getValue() ? s1 : s2, state);
        }

        return Pair.of(new BadIf(condition, s1, s1), state);
    }
}
