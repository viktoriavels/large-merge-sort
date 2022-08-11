package ru.viktoriavels.shift.mergesort.item;

public class StringFileItem implements FileItem {

    private final String value;

    public StringFileItem(String value) {
        this.value = value;
    }

    @Override
    public int compareTo(FileItem o) {
        return this.value.compareTo(((StringFileItem) o).value);
    }

    @Override
    public String toWritableFormat() {
        return value;
    }
}
