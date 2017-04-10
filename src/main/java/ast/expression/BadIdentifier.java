package ast.expression;

import ast.expression.interfaces.BadExpression;

public class BadIdentifier extends Identifier implements BadExpression {

    public BadIdentifier(String identifier) {
        super(identifier);
    }
}
