package utils;

import ast.AST;
import ast.expression.Identifier;
import ast.expression.abstract_operations.BinOp;
import ast.expression.abstract_operations.UnOp;
import ast.expression.interfaces.Value;
import ast.statement.*;

public interface Visitor<T> {

    T visit(AST element);
    T visit(Skip element);
    T visit(Sequence element);
    T visit(Assignment element);
    T visit(If element);
    T visit(While element);
    T visit(BinOp element);
    T visit(UnOp element);
    T visit(Value<?> element);
    T visit(Identifier element);
}
