package app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleASTNode {

    @Getter
    private final Class<?> elemClazz;
    @Getter
    private final String value;

    @Override
    public String toString() {
        return elemClazz.getSimpleName() + (getValue() == null? "" :  " \nValue = " + getValue());
    }

}
