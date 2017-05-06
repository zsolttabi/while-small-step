package viewmodel.interfaces;

public interface IASTElement<T> {

    T accept(IASTVisitor<T> visitor);
}
