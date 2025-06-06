package ru.ivanov.vinitro.util;

public class AnalysisNameKeeper {
    private String partOfName;

    public AnalysisNameKeeper() {
    }

    public AnalysisNameKeeper(String partOfName) {
        this.partOfName = partOfName;
    }

    public String getPartOfName() {
        return partOfName;
    }

    public void setPartOfName(String partOfName) {
        this.partOfName = partOfName;
    }

    @Override
    public String toString() {
        return "AnalysisNameKeeper{" +
                "partOfName='" + partOfName + '\'' +
                '}';
    }
}
