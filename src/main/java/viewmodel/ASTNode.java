package viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ASTNode {

    @Getter
    private final String label;
    private final boolean stuck;

    @Override
    public String toString() {
        return getLabel();
    }

    public boolean isStuck() {
        return stuck;
    }

}
