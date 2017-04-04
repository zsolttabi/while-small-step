package utils;

import java.util.Optional;

public final class Either<L, R> {

    public static <L, R> Either<L, R> left(L value) {
        return new Either<>(Optional.of(value), Optional.empty());
    }

    public static <L, R> Either<L, R> right(R value) {
        return new Either<>(Optional.empty(), Optional.of(value));
    }

    private Optional<L> left;
    private Optional<R> right;

    private Either(Optional<L> l, Optional<R> r) {
        left = l;
        right = r;
    }

    public Optional<L> getLeft() {
        return left;
    }

    public Optional<R> getRight() {
        return right;
    }

    public void setLeft(Optional<L> left) {
        this.left = left;
    }

    public void setRight(Optional<R> right) {
        this.right = right;
    }
}