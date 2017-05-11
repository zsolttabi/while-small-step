package ast.expression.operations;

import ast.ExprConfig;
import ast.State;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.StuckExpression;
import ast.expression.interfaces.Value;
import ast.expression.operations.bad_operations.StuckBinOp;
import ast.expression.values.BoolValue;
import ast.expression.values.IntValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTVisitor;

import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
@EqualsAndHashCode
public class BinOp<T, R> implements Expression {

    public static <T, R> BinOp<T, R> of(String operator,
                                                                        Expression lhs,
                                                                        Expression rhs,
                                                                        Class<? extends Value<T>> operandClass,
                                                                        Function<R, Value<R>> resultCtor,
                                                                        BiFunction<T, T, R> evalFun) {

        return lhs == null || rhs == null ? new StuckBinOp<>(operator, lhs, rhs) : new BinOp<>(operator,
                lhs,
                rhs,
                operandClass,
                resultCtor,
                evalFun);
    }

    public static BinOp<Integer, Integer> arithOp(String operator, Expression lhs, Expression rhs, BiFunction<Integer, Integer, Integer> evalFun) {
        return BinOp.of(operator, lhs, rhs, IntValue.class, IntValue::of, evalFun);
    }

    public static BinOp<Integer, Integer> add(Expression lhs, Expression rhs) {
        return BinOp.arithOp("+", lhs, rhs, Integer::sum);
    }

    public static BinOp<Integer, Integer> subtract(Expression lhs, Expression rhs) {
        return BinOp.arithOp("-", lhs, rhs, (i1, i2) -> i1 - i2);
    }

    public static BinOp<Boolean, Boolean> boolOp(String operator, Expression lhs, Expression rhs, BiFunction<Boolean, Boolean, Boolean> evalFun) {
        return BinOp.of(operator, lhs, rhs, BoolValue.class, BoolValue::of, evalFun);
    }

    public static BinOp<Boolean, Boolean> and(Expression lhs, Expression rhs) {
        return BinOp.boolOp("and", lhs, rhs, Boolean::logicalAnd);
    }

    public static BinOp<Integer, Boolean> intRelOp(String operator, Expression lhs, Expression rhs, BiFunction<Integer, Integer, Boolean> evalFun) {
        return BinOp.of(operator, lhs, rhs, IntValue.class, BoolValue::of, evalFun);
    }

    public static BinOp<Integer, Boolean> areEquals(Expression lhs, Expression rhs) {
        return BinOp.intRelOp("=", lhs, rhs, Object::equals);
    }

    public static BinOp<Integer, Boolean> lessThen(Expression lhs, Expression rhs) {
        return BinOp.intRelOp("<", lhs, rhs, (i1, i2) -> i1 < i2);
    }

    @Getter
    private final String operator;
    @Getter
    private final Expression lhs;
    @Getter
    private final Expression rhs;

    private final Class<? extends Value<T>> operandClass;
    private final Function<R, Value<R>> resultCtor;
    private final BiFunction<T, T, R> evalFun;

    @Override
    public ExprConfig step(State state) {

        if(lhs instanceof StuckExpression) {
            return ExprConfig.of(new StuckBinOp<>(operator, rhs, lhs), state);
        }

        if (!(lhs instanceof Value)) {
            return ExprConfig.of(new BinOp<>(operator, lhs.step(state).getExpression(), rhs, operandClass, resultCtor, evalFun),
                    state);
        }

        if(rhs instanceof StuckExpression) {
            return ExprConfig.of(new StuckBinOp<>(operator, lhs, rhs), state);
        }

        if (!(rhs instanceof Value)) {
            return ExprConfig.of(new BinOp<>(operator, lhs, rhs.step(state).getExpression(), operandClass, resultCtor, evalFun),
                    state);
        }

        Value lVal = (Value) lhs;
        Value rVal = (Value) rhs;

        if (operandClass.isAssignableFrom(lVal.getClass()) && operandClass.isAssignableFrom(rVal.getClass())) {
            return ExprConfig.of(resultCtor.apply(evalFun.apply(operandClass.cast(lVal).getValue(), operandClass.cast(rVal).getValue())),
                    state);
        }

        return ExprConfig.of(new StuckBinOp(operator, lhs, rhs), state);
    }


    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
