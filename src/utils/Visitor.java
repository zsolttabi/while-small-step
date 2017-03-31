package utils;

public interface Visitor<T> {

    T visit(Element<T> element);
}
