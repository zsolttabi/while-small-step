package ast.statement;

import ast.State;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;

@RequiredArgsConstructor
public class Sequence implements Statement {

    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;

    @Override
    public Pair<Statement, State> step(State state) {

        Pair<Statement, State> newConfig = s1.step(state);

        if (newConfig.getFirst() instanceof BadStatement) {
            return Pair.of(new BadSequence(newConfig.getFirst(), s2), state);
        }

        return newConfig.getFirst() != null ? Pair.of(s2, state) : newConfig;
    }

}
