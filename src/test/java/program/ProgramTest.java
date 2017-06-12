package program;

import com.tngtech.java.junit.dataprovider.DataProvider;
import com.tngtech.java.junit.dataprovider.DataProviderRunner;
import com.tngtech.java.junit.dataprovider.UseDataProvider;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import parser.WhileProgramParser;
import program.Configuration.ConfigType;
import program.expressions.Identifier;
import program.expressions.literals.BooleanLiteral;
import program.expressions.literals.IntegerLiteral;
import program.statements.*;

import java.util.logging.Logger;

import static program.Configuration.ConfigType.STUCK;
import static program.Configuration.ConfigType.TERMINATED;
import static program.expressions.BinOp.Arithmetic.ADD;
import static program.expressions.BinOp.Relational.LT;
import static program.expressions.UnOp.Arithmetic.NEG;
import static program.expressions.UnOp.Logical.NOT;

@RunWith(DataProviderRunner.class)
public class ProgramTest {


    private static final int MAX_PREFIX = 100;
    private Logger logger = Logger.getLogger("ProgramTest");

    @DataProvider
    public static Object[][] correctCodeProvider() {
        return new Object[][]{

                {"skip", new Skip(), TERMINATED},
                {"skip; skip", new Sequence(new Skip(), new Skip()), TERMINATED},
                {"x := 1", new Assignment(new Identifier("x"), new IntegerLiteral("1")), TERMINATED},
                {"b := true", new Assignment(new Identifier("b"), new BooleanLiteral("true")), TERMINATED},
                {"if true then skip else skip fi", new If(new BooleanLiteral("true"), new Skip(), new Skip()), TERMINATED},
                {"b := true; if b then x := 1 else skip fi; z := x",
                        new Sequence(new Sequence(new Assignment(new Identifier("b"), new BooleanLiteral("true")),
                                new If(new Identifier("b"),
                                        new Assignment(new Identifier("x"), new IntegerLiteral("1")),
                                        new Skip())), new Assignment(new Identifier("z"), new Identifier("x"))), TERMINATED},
                {"x := 1; while x < 10 do x := x + 1 od", new Sequence(new Assignment(new Identifier("x"),
                        new IntegerLiteral("1")), new While(
                        LT.of(new Identifier("x"), new IntegerLiteral("10")),
                        new Assignment(new Identifier("x"), ADD.of(new Identifier("x"), new IntegerLiteral("1"))))), TERMINATED},
                {"skip or skip", new Or(new Skip(), new Skip()), TERMINATED},
                {"x := 1 par x := 2", new Par(new Assignment(new Identifier("x"), new IntegerLiteral("1")), new Assignment(new Identifier("x"), new IntegerLiteral("2"))), TERMINATED},

                {"x := 1; x := !x", new Sequence(new Assignment(new Identifier("x"), new IntegerLiteral("1")),
                        new Assignment(new Identifier("x"), NOT.of(new Identifier("x")))), STUCK},
                {"x := true; x := -x", new Sequence(new Assignment(new Identifier("x"), new BooleanLiteral("true")),
                        new Assignment(new Identifier("x"), NEG.of(new Identifier("x")))), STUCK},
                {"x := 1; y := false; z := x + y", new Sequence(new Sequence(new Assignment(new Identifier("x"),
                        new IntegerLiteral("1")),
                        new Assignment(new Identifier("y"), new BooleanLiteral("false"))),
                        new Assignment(new Identifier("z"),
                                ADD.of(new Identifier("x"), new Identifier("y")))), STUCK},
                {"x := 1; x := true; x := 2", new Sequence(new Sequence(new Assignment(new Identifier("x"),
                        new IntegerLiteral("1")), new Assignment(new Identifier("x"), new BooleanLiteral("true"))),
                        new Assignment(new Identifier("x"), new IntegerLiteral("2"))), STUCK},
                {"x := y", new Assignment(new Identifier("x"), new Identifier("y")), STUCK},
                {"x := 1; if x then skip else skip fi", new Sequence(new Assignment(new Identifier("x"),
                        new IntegerLiteral("1")), new If(new Identifier("x"), new Skip(), new Skip())), STUCK},
                {"if z then skip else skip fi", new If(new Identifier("z"), new Skip(), new Skip()), STUCK},
                {"x := true; if x then x := 1 else skip fi", new Sequence(new Assignment(new Identifier("x"),
                        new BooleanLiteral("true")),
                        new If(new Identifier("x"), new Assignment(new Identifier("x"), new IntegerLiteral("1")), new Skip())), STUCK},
                {"x := false; if x then skip else x := 1 fi", new Sequence(new Assignment(new Identifier("x"),
                        new BooleanLiteral("false")),
                        new If(new Identifier("x"), new Skip(), new Assignment(new Identifier("x"), new IntegerLiteral("1")))), STUCK},
                {"while 2 do skip od", new While(new IntegerLiteral("2"), new Skip()), STUCK},
                {"while z do skip od", new While(new Identifier("z"), new Skip()), STUCK},
                {"x := true; while x do x := x + 1 od", new Sequence(new Assignment(new Identifier("x"),
                        new BooleanLiteral("true")),
                        new While(new Identifier("x"),
                                new Assignment(new Identifier("x"), ADD.of(new Identifier("x"), new IntegerLiteral("1"))))), STUCK},
                {"abort", new Abort(), STUCK},
                {"abort or abort", new Or(new Abort(), new Abort()), STUCK},
                {"abort par skip", new Par(new Abort(), new Skip()), STUCK}
        };
    }

    @Test
    @UseDataProvider("correctCodeProvider")
    public void Given_SyntacticallyCorrectCode_When_CodeIsParsed_Then_ParsedProgramMatchesExpectedProgram(String code, IProgramElement expectedProgram, ConfigType expectedOutcome) {

        Program parsed = new Program(WhileProgramParser.parse(code), MAX_PREFIX);
        Assert.assertEquals(expectedProgram, parsed.current().getElement());
    }

    @Test
    @UseDataProvider("correctCodeProvider")
    public void Given_SyntacticallyCorrectCode_When_CodeIsRun_Then_ConfigTypeMatchesExpectedOutcome(String code, IProgramElement expectedProgram, ConfigType expectedOutcome) {

        Program parsed = new Program(WhileProgramParser.parse(code), MAX_PREFIX);
        parsed.last();
        logger.info("Steps: " + parsed.getReductionChain().size()); // TODO: assert reduction steps
        Assert.assertEquals(expectedOutcome, parsed.current().getConfigType());
    }

}
