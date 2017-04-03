package ast;

import ast.statement.interfaces.Statement;
import utils.Pair;

public class ReducedAST extends AST implements IAST {

    ReducedAST(Pair<Statement, State> newConfig) {
        super(newConfig);
    }

    @Override
    public ReducedAST step() {
        return this;
    }

    @Override
    public ReducedAST reduce() {
        return this;
    }
}
