package ast.statement;

import ast.State;
import ast.expression.EvaluatedBooleanExpression;
import ast.expression.EvaluationException;
import ast.expression.Expression;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.val;
import utils.Pair;

import java.util.Collections;

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

        val evalCond = condition.evaluate();
        if (evalCond instanceof EvaluatedBooleanExpression) {
            return Pair.of(((EvaluatedBooleanExpression) evalCond).getValue() ? s1 : s2, state);
        }

        throw new EvaluationException("Could not evaluate condition in If", Collections.singletonList(condition));
    }
}
