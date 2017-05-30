package program;

import viewmodel.interfaces.IVisitableNode;

import java.util.Random;
import java.util.Set;

public interface IProgramElement extends IVisitableNode {

    Random RANDOM = new Random();

    @SafeVarargs
    static <T> T choose(T... args) {
        return args[RANDOM.nextInt(args.length)];
    }

    Configuration step(State state);

    Set<Configuration> peek(State state);

}
