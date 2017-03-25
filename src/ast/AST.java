package ast;

import ast.statement.Statement;
import lombok.Getter;
import utils.Pair;

public class AST {

    @Getter
    private final State state = new State();
    @Getter
    private final Statement stm;

    public AST(Statement stm) {
        this.stm = stm;
    }

    public void runConfiguration() {

        Pair<Statement, State> newConfig = stm.run(state);
        while (newConfig.getFirst() != null) {
            newConfig = newConfig.getFirst().run(newConfig.getSecond());
        }

    }

}

