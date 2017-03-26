package ast.statement;

import ast.State;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;

@RequiredArgsConstructor
public class Sequence extends Statement {

    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;

    @Override
    public Pair<Statement, State> run(State state) {

        Pair<Statement, State> newConfig = s1.run(state);
        while (newConfig.getFirst() != null) {
            newConfig = newConfig.getFirst().run(newConfig.getSecond());
        }

        return Pair.of(s2, state);
    }

}
