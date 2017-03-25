package ast.expression;

public class BooleanVar extends Var<Boolean> implements BooleanValue {

    public BooleanVar(String identifier, Boolean value) {
        super(identifier, value);
    }

    @Override
    public BooleanVar evaluate() {
        return this;
    }
}
