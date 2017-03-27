package ast.expression;

import ast.expression.interfaces.BooleanValue;

public class EvaluatedBooleanExpression extends EvaluatedExpression<Boolean> implements BooleanValue {

    public EvaluatedBooleanExpression(Boolean value) {
        super(value);
    }

    @Override
    public EvaluatedBooleanExpression evaluate() {
        return this;
    }

}
