package viewmodel.interfaces;

import program.Configuration;
import program.Exception;
import program.Program;
import program.SyntaxError;
import program.expressions.BinOp;
import program.expressions.Identifier;
import program.expressions.UnOp;
import program.expressions.Value;
import program.expressions.literals.Literal;
import program.statements.*;

public interface INodeVisitor<T> {

    T visit(Program element);
    T visit(Configuration element);
    T visit(Skip element);
    T visit(Sequence element);
    T visit(Assignment element);
    T visit(If element);
    T visit(While element);
    T visit(BinOp element);
    T visit(UnOp element);
    T visit(Value<?> element);
    T visit(Identifier element);
    T visit(Abort element);
    T visit(Or element);
    T visit(Par element);
    T visit(SyntaxError element);
    T visit(Literal element);
    T visit(Exception element);
    T visit(Throw element);
    T visit(TryCatch element);

}
