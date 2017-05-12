package viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ASTNode {

    public enum NodeType {
        NORMAL, NEXT, STUCK, TERMINATED
    }

    @Getter
    private final String label;
    @Getter
    private final NodeType nodeType;

    @Override
    public String toString() {
        return getLabel();
    }

}
