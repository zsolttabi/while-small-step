package viewmodel.interfaces;

import ast.AST;
import ast.expression.Identifier;
import ast.expression.interfaces.Value;
import ast.expression.operations.BinOp;
import ast.expression.operations.UnOp;
import ast.statement.*;

public interface IASTVisitor<T> {

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
