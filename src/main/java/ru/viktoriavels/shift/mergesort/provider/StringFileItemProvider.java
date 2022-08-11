package ru.viktoriavels.shift.mergesort.provider;

import org.apache.commons.io.LineIterator;
import ru.viktoriavels.shift.mergesort.item.FileItem;
import ru.viktoriavels.shift.mergesort.item.StringFileItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class StringFileItemProvider implements FileItemProvider {

    private final Path path;
    private final LineIterator lineIterator;

    public StringFileItemProvider(Path path) throws IOException {
        this.path = path;
        this.lineIterator = new LineIterator(Files.newBufferedReader(path));
    }

    @Override
    public FileItem next() {
        while (lineIterator.hasNext()) {
            String str = lineIterator.nextLine();
            if (str == null || str.isEmpty() || str.contains(" ")) {
                System.err.println(path.toString() + ": skipped illegal string with the value \"" + str + "\"");
                continue;
            }
            return new StringFileItem(str);
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        lineIterator.close();
    }
}
