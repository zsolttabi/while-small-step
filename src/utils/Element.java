package utils;

public interface Element<T> {

    T accept(Visitor<T> visitor);
}
