package ru.viktoriavels.shift.mergesort.item;

public class IntegerFileItem implements FileItem {

    private final Integer value;

    public IntegerFileItem(Integer value) {
        this.value = value;
    }

    @Override
    public int compareTo(FileItem o) {
        return this.value.compareTo(((IntegerFileItem) o).value);
    }

    @Override
    public String toWritableFormat() {
        return value.toString();
    }
}
