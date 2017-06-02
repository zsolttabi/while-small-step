package program;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import parser.WhileProgramParser;
import program.expressions.Identifier;
import program.expressions.Value;
import program.statements.*;

import java.math.BigInteger;
import java.util.logging.Logger;

import static program.Configuration.ConfigType.STUCK;
import static program.Configuration.ConfigType.TERMINATED;
import static program.expressions.BinOp.Arithmetic.ADD;
import static program.expressions.BinOp.Relational.LT;
import static program.expressions.UnOp.Arithmetic.NEG;
import static program.expressions.UnOp.Logical.NOT;

@RunWith(DataProviderRunner.class)
public class ProgramTest {

    private Logger logger = Logger.getLogger("ProgramTest");

    @DataProvider
    public static Object[][] terminatingCodeProvider() {
        return new Object[][]{
                {"skip", new Skip()},
                {"skip; skip", new Sequence(new Skip(), new Skip())},
                {"x := 1", new Assignment(new Identifier("x"), new Value<>(new BigInteger("1")))},
                {"b := true", new Assignment(new Identifier("b"), new Value<>(true))},
                {"if true then skip else skip fi", new If(new Value<>(true), new Skip(), new Skip())},
                {"b := true; if b then x := 1 else skip fi; z := x",
                        new Sequence(new Sequence(new Assignment(new Identifier("b"), new Value<>(true)),
                                new If(new Identifier("b"),
                                        new Assignment(new Identifier("x"), new Value<>(new BigInteger("1"))),
                                        new Skip())), new Assignment(new Identifier("z"), new Identifier("x")))},
                {"x := 1; while x < 10 do x := x + 1 od", new Sequence(new Assignment(new Identifier("x"),
                        new Value<>(new BigInteger("1"))), new While(
                        LT.of(new Identifier("x"), new Value<>(new BigInteger("10"))),
                        new Assignment(new Identifier("x"), ADD.of(new Identifier("x"), new Value<>(new BigInteger("1"))))))},
        };
    }

    @DataProvider
    public static Object[][] stuckCodeProvider() {
        return new Object[][]{
                {"x := 1; x := !x", new Sequence(new Assignment(new Identifier("x"), new Value<>(new BigInteger("1"))),
                        new Assignment(new Identifier("x"), NOT.of(new Identifier("x"))))},
                {"x := true; x := -x", new Sequence(new Assignment(new Identifier("x"), new Value<>(true)),
                        new Assignment(new Identifier("x"), NEG.of(new Identifier("x"))))},
                {"x := 1; y := false; z := x + y", new Sequence(new Sequence(new Assignment(new Identifier("x"),
                        new Value<>(new BigInteger("1"))),
                        new Assignment(new Identifier("y"), new Value<>(false))),
                        new Assignment(new Identifier("z"),
                                ADD.of(new Identifier("x"), new Identifier("y"))))},
                {"x := 1; x := true; x := 2", new Sequence(new Sequence(new Assignment(new Identifier("x"),
                        new Value<>(new BigInteger("1"))), new Assignment(new Identifier("x"), new Value<>(true))),
                        new Assignment(new Identifier("x"), new Value<>(new BigInteger("2"))))},
                {"x := y", new Assignment(new Identifier("x"), new Identifier("y"))},
                {"x := 1; if x then skip else skip fi", new Sequence(new Assignment(new Identifier("x"),
                        new Value<>(new BigInteger("1"))), new If(new Identifier("x"), new Skip(), new Skip()))},
                {"if z then skip else skip fi", new If(new Identifier("z"), new Skip(), new Skip())},
                {"x := true; if x then x := 1 else skip fi", new Sequence(new Assignment(new Identifier("x"),
                        new Value<>(true)),
                        new If(new Identifier("x"), new Assignment(new Identifier("x"), new Value<>(new BigInteger("1"))), new Skip()))},
                {"x := false; if x then skip else x := 1 fi", new Sequence(new Assignment(new Identifier("x"),
                        new Value<>(false)),
                        new If(new Identifier("x"), new Skip(), new Assignment(new Identifier("x"), new Value<>(new BigInteger("1")))))},
                {"while 2 do skip od", new While(new Value<>(new BigInteger("2")), new Skip())},
                {"while z do skip od", new While(new Identifier("z"), new Skip())},
                {"x := true; while x do x := x + 1 od", new Sequence(new Assignment(new Identifier("x"),
                        new Value<>(true)),
                        new While(new Identifier("x"),
                                new Assignment(new Identifier("x"), ADD.of(new Identifier("x"), new Value<>(new BigInteger("1"))))))},
        };
    }

    @Test
    @UseDataProvider("terminatingCodeProvider")
    public void testTerminatingProgram(String code, IStatement statement) {

        Program okProgram = new Program(WhileProgramParser.parse(code), 100);
        Assert.assertEquals(statement, okProgram.current().getNode());

        okProgram.last();
        logger.info("Steps: " + okProgram.getReductionChain().size()); // TODO: assert reduction steps
        Assert.assertEquals(TERMINATED, okProgram.current().getConfigType());
    }

    @Test
    @UseDataProvider("stuckCodeProvider")
    public void testStuckProgram(String code, IStatement statement) {

        Program stuckProgram = new Program(WhileProgramParser.parse(code), 100);
        Assert.assertEquals(statement, stuckProgram.current().getNode());

        stuckProgram.last();
        logger.info("Steps: " + stuckProgram.getReductionChain().size()); // TODO: assert reduction steps
        Assert.assertEquals(STUCK, stuckProgram.current().getConfigType());
    }

}
