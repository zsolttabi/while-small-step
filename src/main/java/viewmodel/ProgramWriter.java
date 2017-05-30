package viewmodel;

import program.Configuration;
import program.Program;
import program.SyntaxError;
import program.expressions.BinOp;
import program.expressions.Identifier;
import program.expressions.UnOp;
import program.expressions.Value;
import program.statements.*;
import viewmodel.interfaces.INodeVisitor;

public class ProgramWriter implements INodeVisitor<String> {

    public static final String INDENT = "    ";

    @Override
    public String visit(Program element) {
        return element.current().accept(this);
    }

    @Override
    public String visit(Configuration element) {
        return element.getNode().accept(this);
    }

    @Override
    public String visit(Skip element) {
        return "skip";
    }

    @Override
    public String visit(Sequence element) {
        return element.getS1().accept(this) + ";\n" +
                element.getS2().accept(this);
    }

    @Override
    public String visit(Assignment element) {
        return element.getIdentifier().accept(this) + " := " + element.getValue().accept(this);
    }

    @Override
    public String visit(If element) {
        return "if " + element.getCondition().accept(this) + " then\n" +
                    INDENT + element.getS1().accept(this) + "\n" +
                "else\n" +
                    INDENT + element.getS2().accept(this) + "\n" +
                "fi";
    }

    @Override
    public String visit(While element) {
        return "while " + element.getCondition().accept(this) + " do\n" +
                    INDENT + element.getStm().accept(this) + "\n" +
                "od";
    }

    @Override
    public String visit(BinOp element) {
        return element.getLhs().accept(this) + " " + element.getOperator() + " " + element.getRhs().accept(this);
    }

    @Override
    public String visit(UnOp element) {
        return element.getOperator() + " " + element.getOperand().accept(this);
    }

    @Override
    public String visit(Value<?> element) {
        return element.getValue().toString();
    }

    @Override
    public String visit(Identifier element) {
        return element.getIdentifier();
    }

    @Override
    public String visit(Abort element) {
        return "abort";
    }

    @Override
    public String visit(Or element) {
        return element.getS1().accept(this) +
                "\n or \n" +
                element.getS2().accept(this);
    }

    @Override
    public String visit(Par element) {
        return element.getS1().accept(this) +
                "\n par \n" +
                element.getS2().accept(this);
    }

    @Override
    public String visit(SyntaxError element) {
        return element.getText();
    }

}
