package ast.expression;


public interface Expression {

    Expression evaluate();

    Expression step();

}
