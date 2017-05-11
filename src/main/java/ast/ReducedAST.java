package ast;

public class ReducedAST extends AST implements IAST {

    ReducedAST(StmConfig newConfig) {
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
