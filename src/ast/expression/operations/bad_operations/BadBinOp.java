package ast.expression.operations.bad_operations;

import ast.expression.interfaces.BadExpression;
import ast.expression.interfaces.Expression;
import ast.expression.operations.BinOp;

public class BadBinOp<T, R> extends BinOp<T, R> implements BadExpression {


    public BadBinOp(String operator, Expression lhs, Expression rhs) {
        super(operator, lhs, rhs, null, null, null);
    }

}
