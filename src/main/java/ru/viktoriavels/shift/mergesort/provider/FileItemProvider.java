package ru.viktoriavels.shift.mergesort.provider;

import ru.viktoriavels.shift.mergesort.item.FileItem;

import java.io.Closeable;

public interface FileItemProvider extends AutoCloseable, Closeable {

    FileItem next();

}
