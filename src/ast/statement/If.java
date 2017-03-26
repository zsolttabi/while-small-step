package ast.statement;

import ast.State;
import ast.expression.Expression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Pair;

@RequiredArgsConstructor
public class If extends Statement {

    @Getter
    private final Expression<Boolean> condition;
    @Getter
    private final Statement s1;
    @Getter
    private final Statement s2;


    @Override
    public Pair<Statement, State> run(State state) {
        return null;
    }
}
