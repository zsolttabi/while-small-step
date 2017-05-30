package viewmodel.interfaces;

public interface IVisitableNode {

    <V> V accept(INodeVisitor<V> visitor);
}
