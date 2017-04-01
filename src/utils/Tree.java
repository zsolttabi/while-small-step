package utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Tree<T> {

    @Getter
    private Node<T> root;

    public Tree(Node<T> root) {
        this.root = root;
    }

    public int depth() {
        return root.depth();
    }

    public static class Node<T> {

        @Setter
        @Getter
        private T data;
        @Getter
        private Node<T> parent;
        @Getter
        private final List<Node<T>> children = new ArrayList<>();

        public Node(T data, Node<T> parent) {
            this.data = data;
            this.parent = parent;
        }

        public static <T> Node<T> of (T data, Node<T> parent) {
            return new Node<>(data, parent);
        }

        public Node() {

        }

        public void addChild(Node<T> child) {
            addChild(child, children.size());
        }

        public void addChild(Node<T> child, int index) {
            child.parent = this;
            this.children.add(index, child);
        }

        public Node<T> getChild (int index){
            return children.get(index);
        }

        public int size() {
            return children.size();
        }

        public boolean isLeaf() {
            return children.isEmpty();
        }

        public int depth() {
            return isLeaf() ? 0 : 1 + children.stream().mapToInt(Node::depth).max().orElse(0);
        }

    }
}
