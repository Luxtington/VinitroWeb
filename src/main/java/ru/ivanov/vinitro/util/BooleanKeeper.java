package ru.ivanov.vinitro.util;

public class BooleanKeeper {
    private final boolean field;

    public BooleanKeeper(boolean field) {
        this.field = field;
    }

    public boolean isField() {
        return field;
    }

    @Override
    public String toString() {
        return "BooleanKeeper{" +
                "field=" + field +
                '}';
    }
}
