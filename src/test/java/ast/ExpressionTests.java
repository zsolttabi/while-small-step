package ast;

import ast.expression.BadIdentifier;
import ast.expression.Identifier;
import ast.expression.interfaces.Expression;
import ast.expression.operations.BinOp;
import ast.expression.operations.UnOp;
import ast.expression.operations.bad_operations.BadBinOp;
import ast.expression.operations.bad_operations.BadUnOp;
import ast.expression.values.BoolValue;
import ast.expression.values.IntValue;
import org.junit.Assert;
import org.junit.Test;

public class ExpressionTests {

    @Test
    public void testInitializedIdentifierYieldsValue() {

        Identifier underTest = new Identifier("x");
        State state = new State();
        IntValue value = new IntValue(1);
        state.set(underTest, value);

        Expression result = underTest.step(state);

        Assert.assertEquals(result, value);

    }

    @Test
    public void testUninitializedIdentifierYieldsBadIdentifier() {

        Identifier underTest = new Identifier("x");
        State state = new State();
        Expression result = underTest.step(state);

        Assert.assertEquals(result, new BadIdentifier("x"));

    }

    @Test
    public void testBinOpYieldsResult() {

        Expression lhs = new IntValue(1);
        Expression rhs = new IntValue(1);
        Expression underTest = BinOp.add(lhs, rhs);

        Expression result = underTest.step(new State());

        Assert.assertEquals(result, new IntValue(2));

    }

    @Test
    public void testBadOperandYieldsBadBinOp() {

        Expression lhs = new IntValue(1);
        Expression rhs = new Identifier("x");
        Expression underTest = BinOp.add(lhs, rhs);

        Expression result = underTest.step(new State());

        Assert.assertEquals(result,  BinOp.add(lhs, rhs.step(new State())));

        result = result.step(new State());

        Assert.assertEquals(result, new BadBinOp<>("+", lhs, rhs.step(new State())));

    }

    @Test
    public void testMismatchedTypesYieldsBadBinOp() {

        Expression lhs = new BoolValue(true);
        Expression rhs = new IntValue(1);
        Expression underTest = BinOp.add(lhs, rhs);

        Expression result = underTest.step(new State());

        Assert.assertEquals(result, new BadBinOp<>("+", lhs, rhs));

    }

    @Test
    public void testBadOperandTypesYieldsBadBinOp() {

        Expression lhs = new IntValue(1);
        Expression rhs = new IntValue(1);
        Expression underTest = BinOp.and(lhs, rhs);

        Expression result = underTest.step(new State());

        Assert.assertEquals(result, new BadBinOp<>("and", lhs, rhs));

    }

    @Test
    public void testUnOpYieldsResult() {

        Expression operand = new IntValue(1);
        Expression underTest = UnOp.neg(operand);

        Expression result = underTest.step(new State());

        Assert.assertEquals(result, new IntValue(-1));

    }

    @Test
    public void testBadOperandYieldsBadUnOp() {

        Expression operand = new Identifier("x");
        Expression underTest = UnOp.neg(operand);

        Expression result = underTest.step(new State());

        Assert.assertEquals(result, UnOp.neg(operand.step(new State())));

        result = result.step(new State());

        Assert.assertEquals(result, new BadUnOp<>( "-", operand.step(new State())));

    }

    @Test
    public void testBadOperandTypeYieldsBadUnOp() {

        Expression operand = new IntValue(1);
        Expression underTest = UnOp.not(operand);

        Expression result = underTest.step(new State());

        Assert.assertEquals(result, new BadUnOp<>("not", operand));

    }

}
