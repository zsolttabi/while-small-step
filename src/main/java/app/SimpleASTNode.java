package app;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleASTNode {

    @Getter
    private final Class<?> elemClazz;
    @Getter
    private final String value;
    private final boolean bad;

    @Override
    public String toString() {
        return getValue() == null ? elemClazz.getSimpleName() :  getValue();
    }

    public boolean isBad() {
        return bad;
    }
}
