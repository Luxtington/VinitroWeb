package ru.ivanov.vinitro.util;

public class TagKeeper {
    private int tag;

    public TagKeeper() {
    }

    public TagKeeper(int tag) {
        this.tag = tag;
    }

    public int getTag() {
        return tag;
    }

    public void setTag(int tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "TagKeeper{" +
                "tag=" + tag +
                '}';
    }
}
