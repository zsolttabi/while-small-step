package ast.expression.values;

import ast.expression.interfaces.BooleanValue;

public class BooleanVar extends Var<Boolean> implements BooleanValue {

    public BooleanVar(String identifier, Boolean value) {
        super(identifier, value);
    }

    @Override
    public BooleanVar evaluate() {
        return this;
    }

}
