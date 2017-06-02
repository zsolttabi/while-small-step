package parser;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.TokenStream;
import program.statements.IStatement;
import syntax.while_parser.WhileLexer;
import syntax.while_parser.WhileParser;

public class WhileProgramParser extends WhileParser {

    public WhileProgramParser(TokenStream input) {
        super(input);
    }

    public static IStatement parse(String input) {
        WhileParser parser = new WhileParser(new CommonTokenStream(new WhileLexer(CharStreams.fromString(input))));
        return (IStatement) new WhileProgramVisitor().visit(parser.start());
    }

}
