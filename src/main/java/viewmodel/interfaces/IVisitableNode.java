package viewmodel.interfaces;

public interface IVisitableNode<T> {

    T accept(INodeVisitor<T> visitor);
}
