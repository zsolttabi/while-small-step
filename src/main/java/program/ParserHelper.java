package program;

import program.expressions.ExpressionSyntaxError;
import program.expressions.IExpression;
import program.statements.IStatement;
import program.statements.StatementSyntaxError;
import syntax.while_parser.WhileParser.ExpressionContext;
import syntax.while_parser.WhileParser.StatementContext;
import utils.TriFunction;

import java.util.function.BiFunction;
import java.util.function.Function;

public class ParserHelper {

    public static IStatement convertError(StatementContext sc) {
        return sc.value == null ? new StatementSyntaxError(sc.getText()) : sc.value;
    }

    public static IExpression convertError(ExpressionContext ec) {
        return ec.value == null ? new ExpressionSyntaxError(ec.getText()) : ec.value;
    }

    public static IStatement makeStm(BiFunction<IStatement, IStatement, IStatement> ctor, StatementContext sc1, StatementContext sc2) {
        return ctor.apply(convertError(sc1), convertError(sc2));
    }

    public static IStatement makeStm(BiFunction<IExpression, IStatement, IStatement> ctor, ExpressionContext ec, StatementContext sc) {
        return ctor.apply(convertError(ec), convertError(sc));
    }

    public static IStatement makeStm(BiFunction<IExpression, IExpression, IStatement> ctor, ExpressionContext ec1, ExpressionContext ec2) {
        return ctor.apply(convertError(ec1), convertError(ec2));
    }

    public static IStatement makeStm(TriFunction<IExpression, IStatement, IStatement, IStatement> ctor, ExpressionContext ec, StatementContext sc1, StatementContext sc2) {
        return ctor.apply(convertError(ec), convertError(sc1), convertError(sc2));
    }

    public static IExpression makeExpr(Function<IExpression, IExpression> ctor, ExpressionContext ec) {
        return ctor.apply(convertError(ec));
    }

    public static IExpression makeExpr(BiFunction<IExpression, IExpression, IExpression> ctor, ExpressionContext ec1, ExpressionContext ec2) {
        return ctor.apply(convertError(ec1), convertError(ec2));
    }

}
