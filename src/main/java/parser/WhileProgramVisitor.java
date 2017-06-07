package parser;

import org.antlr.v4.runtime.tree.ErrorNode;
import org.antlr.v4.runtime.tree.TerminalNode;
import program.IProgramElement;
import program.expressions.BinOp;
import program.expressions.Identifier;
import program.expressions.UnOp;
import program.expressions.Value;
import program.statements.*;
import syntax.while_parser.WhileBaseVisitor;
import syntax.while_parser.WhileParser;

import java.math.BigInteger;

import static parser.WhileProgramParserHelper.*;


public class WhileProgramVisitor extends WhileBaseVisitor<IProgramElement> {

    private WhileProgramParserHelper helper = new WhileProgramParserHelper(this);

    @Override
    public IProgramElement visitAssignment(WhileParser.AssignmentContext ctx) {
        return helper.makeStm(ctx, Assignment::new, ctx.expr(0), ctx.expr(1));
    }

    @Override
    public IProgramElement visitPar(WhileParser.ParContext ctx) {
        return helper.makeStm(ctx, Par::new, ctx.stm(0), ctx.stm(1));
    }

    @Override
    public IProgramElement visitAbort(WhileParser.AbortContext ctx) {
        return new Abort();
    }

    @Override
    public IProgramElement visitOr(WhileParser.OrContext ctx) {
        return helper.makeStm(ctx, Or::new, ctx.stm(0), ctx.stm(1));
    }

    @Override
    public IProgramElement visitSkip(WhileParser.SkipContext ctx) {
        return new Skip();
    }

    @Override
    public IProgramElement visitWhile(WhileParser.WhileContext ctx) {
        return helper.makeStm(ctx, While::new, ctx.expr(), ctx.stm());
    }

    @Override
    public IProgramElement visitSequence(WhileParser.SequenceContext ctx) {
        return helper.makeStm(ctx, Sequence::new, ctx.stm(0), ctx.stm(1));
    }

    @Override
    public IProgramElement visitIf(WhileParser.IfContext ctx) {
        return helper.makeStm(ctx, If::new, ctx.expr(), ctx.stm(0), ctx.stm(1));
    }

    @Override
    public IProgramElement visitNot(WhileParser.NotContext ctx) {
        return helper.makeExpr(ctx, UnOp.Logical.NOT::of, ctx.expr());
    }

    @Override
    public IProgramElement visitAddSub(WhileParser.AddSubContext ctx) {
        return helper.makeExpr(ctx, ARITHMETIC_BIN_OPS.get(ctx.op.getType())::of, ctx.expr(0), ctx.expr(1));
    }

    @Override
    public IProgramElement visitAnd(WhileParser.AndContext ctx) {
        return helper.makeExpr(ctx, BinOp.Logical.AND::of, ctx.expr(0), ctx.expr(1));
    }

    @Override
    public IProgramElement visitOrXor(WhileParser.OrXorContext ctx) {
        return helper.makeExpr(ctx, LOGICAL_BIN_OPS.get(ctx.op.getType())::of, ctx.expr(0), ctx.expr(1));
    }

    @Override
    public IProgramElement visitMulDivRem(WhileParser.MulDivRemContext ctx) {
        return helper.makeExpr(ctx, ARITHMETIC_BIN_OPS.get(ctx.op.getType())::of, ctx.expr(0), ctx.expr(1));
    }

    @Override
    public IProgramElement visitMinus(WhileParser.MinusContext ctx) {
        return helper.makeExpr(ctx, UnOp.Arithmetic.NEG::of, ctx.expr());
    }

    @Override
    public IProgramElement visitRel1(WhileParser.Rel1Context ctx) {
        return helper.makeExpr(ctx, RELATIONAL_BIN_OPS.get(ctx.op.getType())::of, ctx.expr(0), ctx.expr(1));
    }

    @Override
    public IProgramElement visitRel2(WhileParser.Rel2Context ctx) {
        return helper.makeExpr(ctx, RELATIONAL_BIN_OPS.get(ctx.op.getType())::of, ctx.expr(0), ctx.expr(1));
    }

    @Override
    public IProgramElement visitParenthesis(WhileParser.ParenthesisContext ctx) {
        return visit(ctx.expr());
    }

    @Override
    public IProgramElement visitInteger(WhileParser.IntegerContext ctx) {
        return new Value<>(new BigInteger(ctx.getText()));
    }

    @Override
    public IProgramElement visitBool(WhileParser.BoolContext ctx) {
        return new Value<>(ctx.tf.getType() == WhileParser.TRUE);
    }

    @Override
    public IProgramElement visitIdentifier(WhileParser.IdentifierContext ctx) {
        return new Identifier(ctx.getText());
    }

    @Override
    public IProgramElement visitStart(WhileParser.StartContext ctx) {

        if (isErrorous(ctx)) {
            return new StatementSyntaxError(MISSING_STATEMENT);
        }
        return visit(ctx.s);
    }

    @Override
    public IProgramElement visitAtomExpr(WhileParser.AtomExprContext ctx) {
        return visit(ctx.atom());
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
