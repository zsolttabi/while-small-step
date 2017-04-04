package ast.expression.interfaces;


import ast.State;

public interface BadExpression extends Expression {

    @Override
    default Expression step(State state) {
        return this;
    }

}
