package ast;

import ast.expression.Identifier;
import ast.expression.interfaces.Value;
import ast.expression.values.BoolValue;
import ast.expression.values.IntValue;
import ast.statement.Assignment;
import ast.statement.If;
import ast.statement.Sequence;
import ast.statement.Skip;
import ast.statement.bad_statements.BadAssignment;
import ast.statement.bad_statements.BadSequence;
import ast.statement.interfaces.Statement;
import org.junit.Assert;
import org.junit.Test;
import utils.Pair;

public class StatementTests {

    @Test
    public void testSkipSkips() {

        Statement underTest = new Skip();
        State state = new State();

        Pair<Statement, State> result = underTest.step(state);

        Assert.assertNull(result.getFirst());
        Assert.assertEquals(result.getSecond(), state);

    }

    @Test
    public void testAssignmentAssignsValueToNewVariable() {

        Identifier identifier = new Identifier("x");
        Value<?> value = new IntValue(5);

        State state = new State();
        State expectedState = new State();
        expectedState.set(identifier, value);
        Statement underTest = new Assignment(identifier, value);

        Pair<Statement, State> result = underTest.step(state);

        Assert.assertNull(result.getFirst());
        Assert.assertEquals(result.getSecond(), expectedState);

    }


    @Test
    public void testAssignmentAssignsNewValueToExistingVariable() {

        Identifier identifier = new Identifier("x");
        Value<?> value = new IntValue(5);

        State state = new State();
        state.set(identifier, new IntValue(1));
        State expectedState = new State();
        expectedState.set(identifier, value);

        Statement underTest = new Assignment(identifier, value);

        Pair<Statement, State> result = underTest.step(state);

        Assert.assertNull(result.getFirst());
        Assert.assertEquals(result.getSecond(), expectedState);

    }

    @Test
    public void testAssignNonExistingVariable() {

        Identifier x = new Identifier("x");
        Identifier y = new Identifier("y");

        Statement underTest = new Assignment(x, y);

        Pair<Statement, State> result = underTest.step(new State());

        Assert.assertEquals(result.getSecond(), new State());
        Assert.assertTrue(result.getFirst() instanceof Assignment);

        result = result.getFirst().step(result.getSecond());

        Assert.assertEquals(result.getSecond(), new State());
        Assert.assertTrue(result.getFirst() instanceof BadAssignment);

    }

    @Test
    public void testAssignToBadIdentifier() {

    }


    @Test
    public void testAssignBadValue() {

    }


    @Test
    public void testSequence() {

        Statement s1 = new Skip();
        Statement s2 = new If(null, null, null);
        State state = new State();
        Statement underTest = new Sequence(s1, s2);

        Pair<Statement, State> result = underTest.step(state);

        Assert.assertEquals(result.getFirst(), s2);
        Assert.assertEquals(result.getSecond(), new State());

    }

    @Test
    public void testBadSequence() {

        Statement s1 = new BadAssignment(new Identifier("x"), new Identifier("y"));
        Statement s2 = new Skip();
        State state = new State();
        Statement underTest = new Sequence(s1, s2);

        Pair<Statement, State> result = underTest.step(state);

        Assert.assertEquals(result.getFirst().getClass(), BadSequence.class);
        Assert.assertEquals(result.getSecond(), new State());

    }


    @Test
    public void testIfTrueBranch() {

        Statement s1 = new If(null, null, null);
        Statement s2 = new Sequence(null, null);
        State state = new State();
        Statement underTest = new If(new BoolValue(true), s1, s2);

        Pair<Statement, State> result = underTest.step(state);

        Assert.assertEquals(result.getFirst(), s1);
        Assert.assertEquals(result.getSecond(), new State());

    }

    @Test
    public void testIfFalseBranch() {

    }


    @Test
    public void testWhile() {

    }


}