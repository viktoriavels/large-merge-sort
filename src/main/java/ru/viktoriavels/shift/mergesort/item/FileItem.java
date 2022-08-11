package ru.viktoriavels.shift.mergesort.item;

public interface FileItem extends Comparable<FileItem> {
    String toWritableFormat();
}
