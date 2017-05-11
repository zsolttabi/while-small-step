package ast;

import ast.expression.Identifier;
import ast.expression.StuckIdentifier;
import ast.expression.interfaces.Expression;
import ast.expression.operations.BinOp;
import ast.expression.operations.UnOp;
import ast.expression.operations.bad_operations.StuckBinOp;
import ast.expression.operations.bad_operations.StuckUnOp;
import ast.expression.values.BoolValue;
import ast.expression.values.IntValue;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTests {

    @Test
    public void testInitializedIdentifierYieldsValue() {

        Identifier underTest = new Identifier("x");
        IntValue value = new IntValue(1);
        State state = new State();
        state.set(underTest, value);

        ExprConfig result = underTest.step(state);

        Assert.assertEquals(result.getExpression(), value);
        Assert.assertEquals(result.getState(), state);

    }

    @Test
    public void testUninitializedIdentifierYieldsBadIdentifier() {

        Identifier underTest = new Identifier("x");
        State state = new State();
        ExprConfig result = underTest.step(state);

        Assert.assertEquals(result.getExpression(), new StuckIdentifier("x"));
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testBinOpYieldsResult() {

        Expression lhs = new IntValue(1);
        Expression rhs = new IntValue(1);
        Expression underTest = BinOp.add(lhs, rhs);

        ExprConfig result = underTest.step(new State());

        Assert.assertEquals(result.getExpression(), new IntValue(2));
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testBadOperandYieldsBadBinOp() {

        Expression lhs = new IntValue(1);
        Expression rhs = new Identifier("x");
        Expression underTest = BinOp.add(lhs, rhs);

        ExprConfig result = underTest.step(new State());

        Assert.assertEquals(result.getExpression(),  BinOp.add(lhs, rhs.step(new State()).getExpression()));

        result = result.step();

        Assert.assertEquals(result.getExpression(), new StuckBinOp<>("+", lhs, rhs.step(new State()).getExpression()));
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testMismatchedTypesYieldsBadBinOp() {

        Expression lhs = new BoolValue(true);
        Expression rhs = new IntValue(1);
        Expression underTest = BinOp.add(lhs, rhs);

        ExprConfig result = underTest.step(new State());

        Assert.assertEquals(result.getExpression(), new StuckBinOp<>("+", lhs, rhs));
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testBadOperandTypesYieldsBadBinOp() {

        Expression lhs = new IntValue(1);
        Expression rhs = new IntValue(1);
        Expression underTest = BinOp.and(lhs, rhs);

        ExprConfig result = underTest.step(new State());

        Assert.assertEquals(result.getExpression(), new StuckBinOp<>("and", lhs, rhs));
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testUnOpYieldsResult() {

        Expression operand = new IntValue(1);
        Expression underTest = UnOp.neg(operand);

        ExprConfig result = underTest.step(new State());

        Assert.assertEquals(result.getExpression(), new IntValue(-1));
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testBadOperandYieldsBadUnOp() {

        Expression operand = new Identifier("x");
        Expression underTest = UnOp.neg(operand);

        ExprConfig result = underTest.step(new State());

        Assert.assertEquals(result.getExpression(), UnOp.neg(operand.step(new State()).getExpression()));

        result = result.step();

        Assert.assertEquals(result.getExpression(), new StuckUnOp<>( "-", operand.step(new State()).getExpression()));
        Assert.assertEquals(result.getState(), new State());

    }

    @Test
    public void testBadOperandTypeYieldsBadUnOp() {

        Expression operand = new IntValue(1);
        Expression underTest = UnOp.not(operand);

        ExprConfig result = underTest.step(new State());

        Assert.assertEquals(result.getExpression(), new StuckUnOp<>("not", operand));
        Assert.assertEquals(result.getState(), new State());

    }

}
