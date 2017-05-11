package ast.expression.operations;

import ast.ExprConfig;
import ast.State;
import ast.expression.interfaces.Expression;
import ast.expression.interfaces.StuckExpression;
import ast.expression.interfaces.Value;
import ast.expression.operations.bad_operations.StuckUnOp;
import ast.expression.values.BoolValue;
import ast.expression.values.IntValue;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import utils.Tree;
import viewmodel.ASTNode;
import viewmodel.interfaces.IASTVisitor;

import java.util.function.Function;

@RequiredArgsConstructor
@EqualsAndHashCode
public class UnOp<T, R> implements Expression {

    public static <T, R> UnOp<T, R> of(String operator,
                                        Expression operand,
                                        Class<? extends Value<T>> operandClass,
                                        Function<R, Value<R>> resultCtor,
                                        Function<T, R> evalFun) {

        return operand == null ? new StuckUnOp<>(operator, operand) : new UnOp<>(operator,
                operand,
                operandClass,
                resultCtor,
                evalFun);
    }

    public static UnOp<Integer, Integer> intOp(String operator, Expression operand, Function<Integer, Integer> evalFun) {
        return UnOp.of(operator, operand, IntValue.class, IntValue::of, evalFun);
    }

    public static UnOp<Integer, Integer> neg(Expression operand) {
        return UnOp.intOp("-", operand, i -> 0 - i);
    }

    public static UnOp<Boolean, Boolean> boolOp(String operator, Expression operand, Function<Boolean, Boolean> evalFun) {
        return UnOp.of(operator, operand, BoolValue.class, BoolValue::of, evalFun);
    }

    public static UnOp<Boolean, Boolean> not(Expression operand) {
        return UnOp.boolOp("not", operand, b -> !b);
    }

    @Getter
    private final String operator;
    @Getter
    private final Expression operand;

    private final Class<? extends Value<T>> operandClass;
    private final Function<R, Value<R>> resultCtor;
    private final Function<T, R> evalFun;

    @Override
    public ExprConfig step(State state) {

        if (operand instanceof StuckExpression) {
            return ExprConfig.of(new StuckUnOp<>(operator, operand), state);
        }

        if (!(operand instanceof Value)) {
            return ExprConfig.of(new UnOp<>(operator, operand.step(state).getExpression(), operandClass, resultCtor, evalFun),
                    state);
        }

        Value val = (Value) operand;

        if ((operandClass.isAssignableFrom(val.getClass()))) {
            return ExprConfig.of(resultCtor.apply(evalFun.apply(operandClass.cast(val).getValue())), state);
        }

        return ExprConfig.of(new StuckUnOp(operator, operand), state);
    }


    @Override
    public Tree.Node<ASTNode> accept(IASTVisitor<Tree.Node<ASTNode>> visitor) {
        return visitor.visit(this);
    }

}
