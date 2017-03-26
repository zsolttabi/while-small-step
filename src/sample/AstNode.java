package sample;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class AstNode<T> {

    @Getter
    private final AstType type;
    @Getter
    private final T value;

    public AstNode(AstType type) {
        this(type, null);
    }

    @Override
    public String toString() {
        return type.getName() + (value == null ? "" : "\nValue = " + value);
    }

    public enum AstType {

        Skip("Skip"),
        If("If"),
        Seq("Sequence"),
        IntBinOp("Integral binary operation"),
        BoolBinOp("Boolean binary operation"),
        BoolLit("Boolean literal");

        @Getter
        private String name;

        AstType(String name) {
            this.name = name;
        }

    }

}
