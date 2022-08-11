package ru.viktoriavels.shift.mergesort.provider;

import org.apache.commons.io.LineIterator;
import ru.viktoriavels.shift.mergesort.item.FileItem;
import ru.viktoriavels.shift.mergesort.item.IntegerFileItem;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class IntegerFileItemProvider implements FileItemProvider {

    private final Path path;
    private final LineIterator lineIterator;

    public IntegerFileItemProvider(Path path) throws IOException {
        this.path = path;
        this.lineIterator = new LineIterator(Files.newBufferedReader(path));
    }

    @Override
    public FileItem next() {
        while (lineIterator.hasNext()) {
            String str = lineIterator.nextLine();
            if (str == null || str.isEmpty()) {
                System.err.println(path.toString() + ": skipped illegal integer with the value \"" + str + "\"");
                continue;
            }

            try {
                Integer integer = Integer.valueOf(str.trim());
                return new IntegerFileItem(integer);
            } catch (NumberFormatException e) {
                System.err.println(path.toString() + ": skipped illegal integer with the value \"" + str + "\"");
            }
        }
        return null;
    }

    @Override
    public void close() throws IOException {
        lineIterator.close();
    }

}
