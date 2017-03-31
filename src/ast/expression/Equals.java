package ast.expression;

import ast.expression.abstract_operations.RelBinOp;

public class Equals extends RelBinOp {

    public Equals(Expression lhs, Expression rhs) {
        super(lhs, rhs);
    }

    @Override
    public Expression step() {
        return step(Equals::new, Object::equals);
    }

}
