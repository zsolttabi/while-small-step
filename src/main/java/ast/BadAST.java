package ast;

public class BadAST extends AST implements IAST {

    BadAST(StmConfig newConfig) {
        super(newConfig);
    }

    @Override
    public BadAST step() {
        return this;
    }

    @Override
    public BadAST reduce() {
        return this;
    }

}
