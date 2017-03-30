package ast.expression;

import ast.expression.abstract_operations.ArithBinOp;

public class Addition extends ArithBinOp {

    public Addition(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression evaluate() {
        return evaluate(Integer::sum);
    }

    @Override
    public Expression step() {
        return step(Addition::new, Integer::sum);
    }

}
