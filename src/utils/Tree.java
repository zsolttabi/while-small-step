package utils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

public class Tree<T> {

    @Getter
    private Node<T> root;

    public Tree(Node<T> root) {
        this.root = root;
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

    }
}