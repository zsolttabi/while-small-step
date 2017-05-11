package viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ASTNode {

    @Getter
    private final String label;
    private final boolean bad;

    @Override
    public String toString() {
        return getLabel();
    }

    public boolean isBad() {
        return bad;
    }

}
