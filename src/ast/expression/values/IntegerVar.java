package ast.expression.values;

import ast.expression.interfaces.IntegerValue;

public class IntegerVar extends Var<Integer> implements IntegerValue {

    public IntegerVar(String identifier, Integer value) {
        super(identifier, value);
    }

    @Override
    public IntegerVar evaluate() {
        return this;
    }

}
