package ast;

import ast.statement.Statement;
import utils.Pair;

public class ReducedAST extends AST implements IAST {

    ReducedAST(Pair<Statement, State> newConfig) {
        super(newConfig);
    }

    @Override
    public IAST step() {
        return this;
    }

    @Override
    public IAST reduce() {
        return this;
    }
}
