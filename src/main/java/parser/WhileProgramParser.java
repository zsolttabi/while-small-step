package parser;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.ATNConfigSet;
import org.antlr.v4.runtime.dfa.DFA;
import program.IProgramElement;
import syntax.while_parser.WhileLexer;
import syntax.while_parser.WhileParser;

import java.util.BitSet;

public class WhileProgramParser extends WhileParser {

    public WhileProgramParser(TokenStream input) {
        super(input);
    }

    public static IProgramElement parse(String input) {
        WhileParser parser = new WhileParser(new CommonTokenStream(new WhileLexer(CharStreams.fromString(input))));
        return new WhileProgramVisitor().visit(parser.start());
    }

}
