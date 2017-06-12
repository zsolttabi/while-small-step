package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import program.Exception;
import program.IProgramElement;
import program.SyntaxError;
import program.expressions.BinOp;
import program.expressions.Identifier;
import program.expressions.UnOp;
import program.expressions.literals.BooleanLiteral;
import program.expressions.literals.IntegerLiteral;
import program.statements.*;
import syntax.while_parser.WhileBaseVisitor;
import syntax.while_parser.WhileParser;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


public class WhileProgramVisitor extends WhileBaseVisitor<IProgramElement> {

    private static final String MISSING = "missing";
    private static final String MISMATCHED = "mismatched";

    private static final String MISSING_STATEMENT = "<missing statement>";
    private static final String MISSING_EXPRESSION = "<missing expression>";
    private static final String MISSING_ELEMENT = "<missing element>";

    private static final Map<Integer, BinOp.Arithmetic> ARITHMETIC_BIN_OPS = new HashMap<>();

    static {
        ARITHMETIC_BIN_OPS.put(WhileParser.DIV, BinOp.Arithmetic.DIV);
        ARITHMETIC_BIN_OPS.put(WhileParser.MUL, BinOp.Arithmetic.MUL);
        ARITHMETIC_BIN_OPS.put(WhileParser.REM, BinOp.Arithmetic.REM);
        ARITHMETIC_BIN_OPS.put(WhileParser.PLUS, BinOp.Arithmetic.ADD);
        ARITHMETIC_BIN_OPS.put(WhileParser.MINUS, BinOp.Arithmetic.SUB);
    }

    private static final Map<Integer, BinOp.Relational> RELATIONAL_BIN_OPS = new HashMap<>();

    static {
        RELATIONAL_BIN_OPS.put(WhileParser.LT, BinOp.Relational.LT);
        RELATIONAL_BIN_OPS.put(WhileParser.LE, BinOp.Relational.LE);
        RELATIONAL_BIN_OPS.put(WhileParser.GT, BinOp.Relational.GT);
        RELATIONAL_BIN_OPS.put(WhileParser.GE, BinOp.Relational.GE);
        RELATIONAL_BIN_OPS.put(WhileParser.EQ, BinOp.Relational.EQ);
        RELATIONAL_BIN_OPS.put(WhileParser.NE, BinOp.Relational.NE);
    }

    private static final Map<Integer, BinOp.Logical> LOGICAL_BIN_OPS = new HashMap<>();

    static {
        LOGICAL_BIN_OPS.put(WhileParser.AND, BinOp.Logical.AND);
        LOGICAL_BIN_OPS.put(WhileParser.OR, BinOp.Logical.OR);
        LOGICAL_BIN_OPS.put(WhileParser.XOR, BinOp.Logical.XOR);
    }

    private static boolean isUnrecoverable(ParserRuleContext ctx) {
        return ctx == null || ctx.getStart().getStartIndex() > ctx.getStop().getStopIndex();
    }

    private static boolean containsPartialError(ParserRuleContext ctx) {
        return ctx.exception != null || ctx.getText().contains(MISSING) || ctx.getText().contains(MISMATCHED);
    }

    private static String getErrorText(ParserRuleContext ctx) {
        return ctx.getStart().getInputStream().getText(new Interval(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex()));
    }

    private static IProgramElement handleVisit(ParserRuleContext ctx, Supplier<IProgramElement> supplier) {
        if (isUnrecoverable(ctx)) {
            return new SyntaxError(ctx instanceof WhileParser.StmContext ? MISSING_STATEMENT : MISSING_EXPRESSION);
        }
        if (containsPartialError(ctx)) {
            return new SyntaxError(getErrorText(ctx));
        }
        return supplier.get();
    }

    @Override
    public IProgramElement visitAssignment(WhileParser.AssignmentContext ctx) {
        return handleVisit(ctx, () -> new Assignment(visit(ctx.expr(0)), visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitPar(WhileParser.ParContext ctx) {
        return handleVisit(ctx, () -> new Par(visit(ctx.stm(0)), visit(ctx.stm(1))));
    }

    @Override
    public IProgramElement visitAbort(WhileParser.AbortContext ctx) {
        return handleVisit(ctx, Abort::new);
    }

    @Override
    public IProgramElement visitOr(WhileParser.OrContext ctx) {
        return handleVisit(ctx, () -> new Or(visit(ctx.stm(0)), visit(ctx.stm(1))));
    }

    @Override
    public IProgramElement visitSkip(WhileParser.SkipContext ctx) {
        return handleVisit(ctx, Skip::new);
    }

    @Override
    public IProgramElement visitWhile(WhileParser.WhileContext ctx) {
        return handleVisit(ctx, () -> new While(visit(ctx.expr()), visit(ctx.stm())));
    }

    @Override
    public IProgramElement visitSequence(WhileParser.SequenceContext ctx) {
        return handleVisit(ctx, () -> new Sequence(visit(ctx.stm(0)), visit(ctx.stm(1))));
    }

    @Override
    public IProgramElement visitIf(WhileParser.IfContext ctx) {
        return handleVisit(ctx, () -> new If(visit(ctx.expr()), visit(ctx.stm(0)), visit(ctx.stm(1))));
    }

    @Override
    public IProgramElement visitNot(WhileParser.NotContext ctx) {
        return handleVisit(ctx, () -> UnOp.Logical.NOT.of(visit(ctx.expr())));
    }

    @Override
    public IProgramElement visitAddSub(WhileParser.AddSubContext ctx) {
        return handleVisit(ctx, () -> ARITHMETIC_BIN_OPS.get(ctx.op.getType()).of(visit(ctx.expr(0)), visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitAnd(WhileParser.AndContext ctx) {
        return handleVisit(ctx, () -> BinOp.Logical.AND.of(visit(ctx.expr(0)), visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitOrXor(WhileParser.OrXorContext ctx) {
        return handleVisit(ctx, () -> LOGICAL_BIN_OPS.get(ctx.op.getType()).of(visit(ctx.expr(0)), visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitMulDivRem(WhileParser.MulDivRemContext ctx) {
        return handleVisit(ctx, () -> ARITHMETIC_BIN_OPS.get(ctx.op.getType()).of(visit(ctx.expr(0)), visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitMinus(WhileParser.MinusContext ctx) {
        return handleVisit(ctx, () -> UnOp.Arithmetic.NEG.of(visit(ctx.expr())));
    }

    @Override
    public IProgramElement visitRel1(WhileParser.Rel1Context ctx) {
        return handleVisit(ctx, () -> RELATIONAL_BIN_OPS.get(ctx.op.getType()).of(visit(ctx.expr(0)), visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitRel2(WhileParser.Rel2Context ctx) {
        return handleVisit(ctx, () -> RELATIONAL_BIN_OPS.get(ctx.op.getType()).of(visit(ctx.expr(0)), visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitParenthesis(WhileParser.ParenthesisContext ctx) {
        return visit(ctx.expr());
    }


    @Override
    public IProgramElement visitIntegerLiteral(WhileParser.IntegerLiteralContext ctx) {
        return handleVisit(ctx, () -> new IntegerLiteral(ctx.getText()));
    }

    @Override
    public IProgramElement visitBooleanLiteral(WhileParser.BooleanLiteralContext ctx) {
        return handleVisit(ctx, () -> new BooleanLiteral(ctx.getText()));
    }

    @Override
    public IProgramElement visitIdentifier(WhileParser.IdentifierContext ctx) {
        return handleVisit(ctx, () -> new Identifier(ctx.getText()));
    }

    @Override
    public IProgramElement visitStart(WhileParser.StartContext ctx) {
        return handleVisit(ctx, () -> visit(ctx.s));
    }

    @Override
    public IProgramElement visitAtomExpr(WhileParser.AtomExprContext ctx) {
        return handleVisit(ctx, () -> visit(ctx.atom()));
    }

    @Override
    public IProgramElement visitTryCatch(WhileParser.TryCatchContext ctx) {
        return handleVisit(ctx, () -> new TryCatch(visit(ctx.stm(0)), visit(ctx.stm(1)), (program.Exception) visit(ctx.exception())));
    }

    @Override
    public IProgramElement visitThrow(WhileParser.ThrowContext ctx) {
        return handleVisit(ctx, () -> new Throw((Exception) visit(ctx.exception())));
    }

    @Override
    public IProgramElement visitException(WhileParser.ExceptionContext ctx) {
        return handleVisit(ctx, () -> new Exception(ctx.getText()));
    }

    @Override
    protected IProgramElement defaultResult() {
        return new SyntaxError(MISSING_ELEMENT);
    }
}
