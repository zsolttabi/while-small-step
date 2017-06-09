package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import program.IProgramElement;
import program.expressions.*;
import program.statements.*;
import syntax.while_parser.WhileBaseVisitor;
import syntax.while_parser.WhileParser;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;


public class WhileProgramVisitor extends WhileBaseVisitor<IProgramElement> {

    private static final String MISSING = "missing";
    private static final String MISSING_STATEMENT = "<missing statement>";
    private static final String MISSING_EXPRESSION = "<missing expression>";

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
        return ctx.exception != null || ctx.getText().contains(MISSING) || ctx.getText().contains("extraneous input");
    }

    private static String getErrorText(ParserRuleContext ctx) {
        return ctx.getStart().getInputStream().getText(new Interval(ctx.getStart().getStartIndex(), ctx.getStop().getStopIndex()));
    }

    private static <T extends IProgramElement> T handleVisit(ParserRuleContext ctx, Supplier<T> supplier, Function<String, T> errorCtor, String missingMsg) {
        if (isUnrecoverable(ctx)) {
            return errorCtor.apply(missingMsg);
        }
        if (containsPartialError(ctx)) {
            return errorCtor.apply(getErrorText(ctx));
        }
        return supplier.get();
    }

    private static IStatement handleStmVisit(ParserRuleContext ctx, Supplier<IStatement> supplier) {
        return handleVisit(ctx, supplier, StatementSyntaxError::new, MISSING_STATEMENT);
    }

    private static IExpression handleExprVisit(ParserRuleContext ctx, Supplier<IExpression> supplier) {
        return handleVisit(ctx, supplier, ExpressionSyntaxError::new, MISSING_EXPRESSION);
    }

    @Override
    public IProgramElement visitAssignment(WhileParser.AssignmentContext ctx) {
        return handleStmVisit(ctx, () -> new Assignment((IExpression) visit(ctx.expr(0)), (IExpression) visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitPar(WhileParser.ParContext ctx) {
        return handleStmVisit(ctx, () -> new Par((IStatement) visit(ctx.stm(0)), (IStatement) visit(ctx.stm(1))));
    }

    @Override
    public IProgramElement visitAbort(WhileParser.AbortContext ctx) {
        return handleStmVisit(ctx, Abort::new);
    }

    @Override
    public IProgramElement visitOr(WhileParser.OrContext ctx) {
        return handleStmVisit(ctx, () -> new Or((IStatement) visit(ctx.stm(0)), (IStatement) visit(ctx.stm(1))));
    }

    @Override
    public IProgramElement visitSkip(WhileParser.SkipContext ctx) {
        return handleStmVisit(ctx, Skip::new);
    }

    @Override
    public IProgramElement visitWhile(WhileParser.WhileContext ctx) {
        return handleStmVisit(ctx, () -> new While((IExpression) visit(ctx.expr()), (IStatement) visit(ctx.stm())));
    }

    @Override
    public IProgramElement visitSequence(WhileParser.SequenceContext ctx) {
        return handleStmVisit(ctx, () -> new Sequence((IStatement) visit(ctx.stm(0)), (IStatement) visit(ctx.stm(1))));
    }

    @Override
    public IProgramElement visitIf(WhileParser.IfContext ctx) {
        return handleStmVisit(ctx, () -> new If((IExpression) visit(ctx.expr()), (IStatement) visit(ctx.stm(0)), (IStatement) visit(ctx.stm(1))));
    }

    @Override
    public IProgramElement visitNot(WhileParser.NotContext ctx) {
        return handleExprVisit(ctx, () -> UnOp.Logical.NOT.of((IExpression) visit(ctx.expr())));
    }

    @Override
    public IProgramElement visitAddSub(WhileParser.AddSubContext ctx) {
        return handleExprVisit(ctx, () -> ARITHMETIC_BIN_OPS.get(ctx.op.getType()).of((IExpression) visit(ctx.expr(0)), (IExpression) visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitAnd(WhileParser.AndContext ctx) {
        return handleExprVisit(ctx, () -> BinOp.Logical.AND.of((IExpression) visit(ctx.expr(0)), (IExpression) visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitOrXor(WhileParser.OrXorContext ctx) {
        return handleExprVisit(ctx, () -> LOGICAL_BIN_OPS.get(ctx.op.getType()).of((IExpression) visit(ctx.expr(0)), (IExpression) visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitMulDivRem(WhileParser.MulDivRemContext ctx) {
        return handleExprVisit(ctx, () -> ARITHMETIC_BIN_OPS.get(ctx.op.getType()).of((IExpression) visit(ctx.expr(0)), (IExpression) visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitMinus(WhileParser.MinusContext ctx) {
        return handleExprVisit(ctx, () -> UnOp.Arithmetic.NEG.of((IExpression) visit(ctx.expr())));
    }

    @Override
    public IProgramElement visitRel1(WhileParser.Rel1Context ctx) {
        return handleExprVisit(ctx, () -> RELATIONAL_BIN_OPS.get(ctx.op.getType()).of((IExpression) visit(ctx.expr(0)), (IExpression) visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitRel2(WhileParser.Rel2Context ctx) {
        return handleExprVisit(ctx, () -> RELATIONAL_BIN_OPS.get(ctx.op.getType()).of((IExpression) visit(ctx.expr(0)), (IExpression) visit(ctx.expr(1))));
    }

    @Override
    public IProgramElement visitParenthesis(WhileParser.ParenthesisContext ctx) {
        return visit(ctx.expr());
    }


    @Override
    public IProgramElement visitIntegerLiteral(WhileParser.IntegerLiteralContext ctx) {
        return handleExprVisit(ctx, () -> new IntegerLiteral(ctx.getText()));
    }

    @Override
    public IProgramElement visitBooleanLiteral(WhileParser.BooleanLiteralContext ctx) {
        return handleExprVisit(ctx, () -> new BooleanLiteral(ctx.getText()));
    }

    @Override
    public IProgramElement visitIdentifier(WhileParser.IdentifierContext ctx) {
        return handleExprVisit(ctx, () -> new Identifier(ctx.getText()));
    }

    @Override
    public IProgramElement visitStart(WhileParser.StartContext ctx) {
        return handleStmVisit(ctx, () -> (IStatement) visit(ctx.s));
    }

    @Override
    public IProgramElement visitAtomExpr(WhileParser.AtomExprContext ctx) {
        return handleExprVisit(ctx, () -> (IExpression) visit(ctx.atom()));
    }

    @Override
    public IProgramElement visitTerminal(TerminalNode terminalNode) {
        return null;
    }

    @Override
    public IProgramElement visitErrorNode(ErrorNode errorNode) {
        return null;
    }
}
