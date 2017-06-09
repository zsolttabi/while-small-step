package viewmodel;

import program.Configuration;
import program.Program;
import program.SyntaxError;
import program.expressions.BinOp;
import program.expressions.Identifier;
import program.expressions.UnOp;
import program.expressions.Value;
import program.expressions.literals.Literal;
import program.statements.*;
import viewmodel.interfaces.INodeVisitor;

import java.util.Arrays;

import static program.Configuration.ConfigType.TERMINATED;

public class CodeWriter implements INodeVisitor<String> {

    public static final String NEWLINE = System.lineSeparator();
    public static final String BASE_INDENT = "  ";

    private static class CodePrinter {

        private final StringBuilder builder = new StringBuilder();

        CodePrinter print(String str) {
            builder.append(str);
            return this;
        }

        CodePrinter printLn() {
            builder.append(NEWLINE);
            return this;
        }

        CodePrinter printLn(String str) {
            builder.append(str).append(NEWLINE);
            return this;
        }

        CodePrinter printLinesWithIndent(String lines) {
            Arrays.stream(lines.split(NEWLINE)).forEach(line -> builder.append(BASE_INDENT).append(line).append(NEWLINE));
            return this;
        }

        CodePrinter removeNewline() {
            if (builder.lastIndexOf(NEWLINE) + NEWLINE.length() == builder.length()) {
                builder.setLength(builder.length() - 1);
            }
            return this;
        }

        @Override
        public String toString() {
            return builder.toString();
        }

    }

    public String write(Program program) {
        return visit(program);
    }

    @Override
    public String visit(Program element) {
        return element.current().accept(this);
    }

    @Override
    public String visit(Configuration element) {
        return element.getConfigType() == TERMINATED ? "" : element.getElement().accept(this);
    }

    @Override
    public String visit(Skip element) {
        return new CodePrinter()
                .print("skip")
                .toString();
    }

    @Override
    public String visit(Sequence element) {
        return new CodePrinter()
                .print(element.getS1().accept(this))
                .removeNewline()
                .printLn(";")
                .print(element.getS2().accept(this))
                .toString();
    }

    @Override
    public String visit(Assignment element) {
        return new CodePrinter()
                .print(element.getIdentifier().accept(this))
                .print(" := ")
                .printLn(element.getValue().accept(this))
                .toString();
    }

    @Override
    public String visit(If element) {

        return new CodePrinter()
                .print("if ")
                .print(element.getCondition().accept(this))
                .print(" then")
                .printLn()
                .printLinesWithIndent(element.getS1().accept(this))
                .printLn("else")
                .printLinesWithIndent(element.getS2().accept(this))
                .printLn("fi")
                .toString();
    }

    @Override
    public String visit(While element) {

        return new CodePrinter()
                .print("while ")
                .print(element.getCondition().accept(this))
                .print(" do")
                .printLn()
                .printLinesWithIndent(element.getStm().accept(this))
                .printLn("od")
                .toString();
    }

    @Override
    public String visit(BinOp element) {
        return new CodePrinter()
                .print("(")
                .print(element.getLhs().accept(this))
                .print(" ")
                .print(element.getOperator())
                .print(" ")
                .print(element.getRhs().accept(this))
                .print(")")
                .toString();
    }

    @Override
    public String visit(UnOp element) {
        return new CodePrinter()
                .print("(")
                .print(element.getOperator())
                .print(" ")
                .print(element.getOperand().accept(this))
                .print(")")
                .toString();
    }

    @Override
    public String visit(Value<?> element) {
        return new CodePrinter()
                .print(element.getValue().toString())
                .toString();
    }

    @Override
    public String visit(Identifier element) {
        return new CodePrinter()
                .print(element.getIdentifier())
                .toString();
    }

    @Override
    public String visit(Abort element) {
        return new CodePrinter()
                .print("abort")
                .toString();
    }

    @Override
    public String visit(Or element) {
        return new CodePrinter()
                .printLinesWithIndent(element.getS1().accept(this))
                .printLn("or")
                .printLinesWithIndent(element.getS2().accept(this))
                .toString();
    }

    @Override
    public String visit(Par element) {
        return new CodePrinter()
                .printLinesWithIndent(element.getS1().accept(this))
                .printLn("par")
                .printLinesWithIndent(element.getS2().accept(this))
                .toString();
    }

    @Override
    public String visit(SyntaxError element) {
        return new CodePrinter()
                .print(element.getText())
                .toString();
    }

    @Override
    public String visit(Literal element) {
        return new CodePrinter()
                .print(element.getValue())
                .toString();
    }

}
