package ast.expression.operations;

import ast.State;
import ast.expression.interfaces.BadExpression;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.Value;
import ast.expression.operations.bad_operations.BadBinOp;
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

        return lhs == null || rhs == null ? new BadBinOp<>(operator, lhs, rhs) : new BinOp<>(operator,
                lhs,
                rhs,
                operandClass,
                resultCtor,
                evalFun);
    }

    public static BinOp<Integer, Integer> arithOp(String operator, Expression lhs, Expression rhs, BiFunction<Integer, Integer, Integer> evalFun) {
        return BinOp.of(operator, lhs, rhs, IntValue.class, IntValue::new, evalFun);
    }

    public static BinOp<Integer, Integer> add(Expression lhs, Expression rhs) {
        return BinOp.arithOp("+", lhs, rhs, Integer::sum);
    }

    public static BinOp<Integer, Integer> subtract(Expression lhs, Expression rhs) {
        return BinOp.arithOp("-", lhs, rhs, (i1, i2) -> i1 - i2);
    }

    public static BinOp<Boolean, Boolean> boolOp(String operator, Expression lhs, Expression rhs, BiFunction<Boolean, Boolean, Boolean> evalFun) {
        return BinOp.of(operator, lhs, rhs, BoolValue.class, BoolValue::new, evalFun);
    }

    public static BinOp<Boolean, Boolean> and(Expression lhs, Expression rhs) {
        return BinOp.boolOp("and", lhs, rhs, Boolean::logicalAnd);
    }

    public static BinOp<Integer, Boolean> intRelOp(String operator, Expression lhs, Expression rhs, BiFunction<Integer, Integer, Boolean> evalFun) {
        return BinOp.of(operator, lhs, rhs, IntValue.class, BoolValue::new, evalFun);
    }

    public static BinOp<Integer, Boolean> equals(Expression lhs, Expression rhs) {
        return BinOp.intRelOp("=", lhs, rhs, Object::equals);
    }

    public static BinOp<Integer, Boolean> lessThen(Expression lhs, Expression rhs) {
        return BinOp.intRelOp("<=", lhs, rhs, (i1, i2) -> i1 < i2);
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
    public Expression step(State state) {

        if(lhs instanceof BadExpression) {
            return new BadBinOp<>(operator, rhs, lhs);
        }

        if (!(lhs instanceof Value)) {
            return new BinOp<>(operator, lhs.step(state), rhs, operandClass, resultCtor, evalFun);
        }

        if(rhs instanceof BadExpression) {
            return new BadBinOp<>(operator, rhs, lhs);
        }

        if (!(rhs instanceof Value)) {
            return new BinOp<>(operator, lhs, rhs.step(state), operandClass, resultCtor, evalFun);
        }

        Value lVal = (Value) lhs;
        Value rVal = (Value) rhs;

        if (operandClass.isAssignableFrom(lVal.getClass()) && operandClass.isAssignableFrom(rVal.getClass())) {
            return resultCtor.apply(evalFun.apply(operandClass.cast(lVal).getValue(), operandClass.cast(rVal).getValue()));
        }

        return new BadBinOp(operator, lhs, rhs);
    }


    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}