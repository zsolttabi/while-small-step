package ast.expression.operations.bad_operations;

import ast.expression.interfaces.BadExpression;
import ast.expression.interfaces.Expression;
import ast.expression.operations.UnOp;

public class BadUnOp<T, R> extends UnOp<T, R> implements BadExpression {

    public BadUnOp(String operator, Expression operand) {
        super(operator, operand, null, null, null);
    }

}
