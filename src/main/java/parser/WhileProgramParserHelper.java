package parser;

import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.misc.Interval;
import program.IProgramElement;
import program.expressions.BinOp;
import program.expressions.ExpressionSyntaxError;
import program.expressions.IExpression;
import program.statements.IStatement;
import program.statements.StatementSyntaxError;
import syntax.while_parser.WhileParser;
import syntax.while_parser.WhileParser.ExprContext;
import syntax.while_parser.WhileParser.StmContext;
import utils.TriFunction;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class WhileProgramParserHelper {

    public static final Map<Integer, BinOp.Arithmetic> ARITHMETIC_BIN_OPS = new HashMap<>();
    public static final String MISSING = "missing";
    public static final String MISSING_ELEMENT = "<missing element>";
    public static final String MISSING_STATEMENT = "<missing statement>";

    static {
        ARITHMETIC_BIN_OPS.put(WhileParser.DIV, BinOp.Arithmetic.DIV);
        ARITHMETIC_BIN_OPS.put(WhileParser.MUL, BinOp.Arithmetic.MUL);
        ARITHMETIC_BIN_OPS.put(WhileParser.REM, BinOp.Arithmetic.REM);
        ARITHMETIC_BIN_OPS.put(WhileParser.PLUS, BinOp.Arithmetic.ADD);
        ARITHMETIC_BIN_OPS.put(WhileParser.MINUS, BinOp.Arithmetic.SUB);
    }

    public static final Map<Integer, BinOp.Relational> RELATIONAL_BIN_OPS = new HashMap<>();

    static {
        RELATIONAL_BIN_OPS.put(WhileParser.LT, BinOp.Relational.LT);
        RELATIONAL_BIN_OPS.put(WhileParser.LE, BinOp.Relational.LE);
        RELATIONAL_BIN_OPS.put(WhileParser.GT, BinOp.Relational.GT);
        RELATIONAL_BIN_OPS.put(WhileParser.GE, BinOp.Relational.GE);
        RELATIONAL_BIN_OPS.put(WhileParser.EQ, BinOp.Relational.EQ);
        RELATIONAL_BIN_OPS.put(WhileParser.NE, BinOp.Relational.NE);
    }

    public static final Map<Integer, BinOp.Logical> LOGICAL_BIN_OPS = new HashMap<>();

    static {
        LOGICAL_BIN_OPS.put(WhileParser.AND, BinOp.Logical.AND);
        LOGICAL_BIN_OPS.put(WhileParser.OR, BinOp.Logical.OR);
        LOGICAL_BIN_OPS.put(WhileParser.XOR, BinOp.Logical.XOR);
    }

    private final WhileProgramVisitor visitor;

    public WhileProgramParserHelper(WhileProgramVisitor visitor) {
        this.visitor = visitor;
    }

    @SuppressWarnings("unchecked")
    private static <T extends IProgramElement> T handleError(ParserRuleContext ctx, Supplier<T> stmSupp) {
        return ctx.getText().contains(MISSING) ? (T) new StatementSyntaxError(getErrorText(ctx)) : stmSupp.get();
    }

    private IStatement handleError(StmContext ctx) {
        IProgramElement element = visitor.visit(ctx);
        if (element == null || !(element instanceof IStatement)) {
            return new StatementSyntaxError(getErrorText(ctx));
        }
        return (IStatement) element;
    }

    private IExpression handleError(WhileParser.ExprContext ctx) {
        IProgramElement element = visitor.visit(ctx);
        if (element == null || !(element instanceof IExpression)) {
            return new ExpressionSyntaxError(getErrorText(ctx));
        }
        return (IExpression) element;
    }

    public static String getErrorText(ParserRuleContext ctx) {
        int startIndex = ctx.getStart().getStartIndex();
        int stopIndex = ctx.getStop().getStopIndex();
        return isErrorous(ctx) ?
                MISSING_ELEMENT :
                ctx.getStart().getInputStream().getText(new Interval(startIndex, stopIndex));
    }

    public static boolean isErrorous(ParserRuleContext ctx) {
        return ctx.getStart().getStartIndex() > ctx.getStop().getStopIndex();
    }

    public IStatement makeStm(StmContext ctx, BiFunction<IStatement, IStatement, IStatement> ctor, StmContext sc1, StmContext sc2) {
        return handleError(ctx, () -> ctor.apply(handleError(sc1), handleError(sc2)));
    }

    public IStatement makeStm(StmContext ctx, BiFunction<IExpression, IStatement, IStatement> ctor, ExprContext ec, StmContext sc) {
        return handleError(ctx, () -> ctor.apply(handleError(ec), handleError(sc)));
    }

    public IStatement makeStm(StmContext ctx, BiFunction<IExpression, IExpression, IStatement> ctor, ExprContext ec1, ExprContext ec2) {
        return handleError(ctx, () -> ctor.apply(handleError(ec1), handleError(ec2)));
    }

    public IStatement makeStm(StmContext ctx, TriFunction<IExpression, IStatement, IStatement, IStatement> ctor, ExprContext ec, StmContext sc1, StmContext sc2) {
        return handleError(ctx, () -> ctor.apply(handleError(ec), handleError(sc1), handleError(sc2)));
    }

    public IExpression makeExpr(ExprContext ctx, Function<IExpression, IExpression> ctor, ExprContext ec) {
        return handleError(ctx, () -> ctor.apply(handleError(ec)));
    }

    public IExpression makeExpr(ExprContext ctx, BiFunction<IExpression, IExpression, IExpression> ctor, ExprContext ec1, ExprContext ec2) {
        return handleError(ctx, () -> ctor.apply(handleError(ec1), handleError(ec2)));
    }

}
