package ast;

import ast.statement.BadStatement;
import ast.statement.Statement;
import lombok.Getter;
import utils.Pair;

public class AST implements IAST {

    @Getter
    private final State state;
    @Getter
    private final Statement stm;

    public AST(Statement stm) {
        this.stm = stm;
        state = new State();
    }

    AST(Pair<Statement, State> newConfig) {
        this.stm = newConfig.getFirst();
        this.state = newConfig.getSecond();
    }

    public IAST reduce() {

        IAST newAST = step();
        while (!(newAST instanceof ReducedAST) && !(newAST instanceof BadAST)) {
            newAST = newAST.step();
        }

        return newAST;
    }

    @Override
    public IAST step() {

        Pair<Statement, State> newConfig = stm.step(state);

        if (newConfig.getFirst() instanceof BadStatement) {
            return new BadAST(newConfig);
        }

        return newConfig.getFirst() == null ? new ReducedAST(newConfig) : new AST(newConfig);
    }

}

