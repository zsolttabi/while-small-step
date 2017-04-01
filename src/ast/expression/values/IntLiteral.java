package ast.expression.values;


import app.SimpleASTNode;
import ast.expression.Expression;
import ast.expression.interfaces.IntValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
public class IntLiteral implements Expression, IntValue {

    @Getter
    private final Integer value;

    @Override
    public Expression step() {
        return this;
    }

}
