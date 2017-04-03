package ast.expression.operations;

import app.SimpleASTNode;
import ast.State;
import ast.expression.interfaces.BadExpression;
import ast.expression.interfaces.Expression;
import ast.expression.Identifier;
import ast.expression.interfaces.Value;
import ast.expression.operations.bad_operations.BadBinOp;
import ast.expression.values.BoolValue;
import ast.expression.values.IntValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import utils.Visitor;

import java.util.function.BiFunction;
import java.util.function.Function;

@RequiredArgsConstructor
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

    public static BinOp<Boolean, Boolean> boolOp(String operator, Expression lhs, Expression rhs, BiFunction<Boolean, Boolean, Boolean> evalFun) {
        return BinOp.of(operator, lhs, rhs, BoolValue.class, BoolValue::new, evalFun);
    }

    public static BinOp<Integer, Boolean> intRelOp(String operator, Expression lhs, Expression rhs, BiFunction<Integer, Integer, Boolean> evalFun) {
        return BinOp.of(operator, lhs, rhs, IntValue.class, BoolValue::new, evalFun);
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

        if(lhs instanceof BadExpression || rhs instanceof BadExpression) {
            return new BadBinOp<>(operator, rhs, lhs);
        }

        if (!(lhs instanceof Value || lhs instanceof Identifier)) {
            return new BinOp<>(operator, lhs.step(state), rhs, operandClass, resultCtor, evalFun);
        }

        if (!(rhs instanceof Value || rhs instanceof Identifier)) {
            return new BinOp<>(operator, lhs, rhs.step(state), operandClass, resultCtor, evalFun);
        }

        Value lVal = lhs instanceof Identifier ? state.get((Identifier)lhs) : (Value) lhs;
        Value rVal = rhs instanceof Identifier ? state.get((Identifier)rhs) : (Value) rhs;

        if (lVal != null && rVal != null && operandClass.isAssignableFrom(lVal.getClass()) && operandClass.isAssignableFrom(rVal.getClass())) {
            return resultCtor.apply(evalFun.apply(operandClass.cast(lVal).getValue(), operandClass.cast(rVal).getValue()));
        }

        return new BadBinOp(operator, lhs, rhs);
    }


    @Override
    public Tree.Node<SimpleASTNode> accept(Visitor<Tree.Node<SimpleASTNode>> visitor) {
        return visitor.visit(this);
    }

}
