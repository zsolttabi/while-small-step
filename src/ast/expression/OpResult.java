package ast.expression;

import app.SimpleASTNode;
import ast.expression.interfaces.Value;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import utils.Visitor;

@RequiredArgsConstructor
public class OpResult<T> implements Expression, Value<T> {

    @Getter
    private final T value;

    @Override
    public Expression step() {
        return this;
    }

}
