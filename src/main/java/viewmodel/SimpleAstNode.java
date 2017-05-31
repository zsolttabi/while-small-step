package viewmodel;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SimpleAstNode {

    public enum NodeType {
        NORMAL, NEXT, STUCK, TERMINATED, SYNTAX_ERROR
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
