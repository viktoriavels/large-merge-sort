package ru.viktoriavels.shift.mergesort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {

        List<String> strings = Arrays.asList(args);
        DataType dataType = fetchDataType(strings);
        SortDirection sortDirection = fetchSortDirection(strings);
        Path output = fetchOutputFile(strings);
        List<Path> input = fetchInputFiles(strings);
        Path tempDir = fetchTempDir(strings);

        new MergeSort(sortDirection, dataType, output, input, tempDir)
                .sort();
    }

    public static SortDirection fetchSortDirection(List<String> strings) {

        if (strings.contains("-d") && strings.contains("-a")) {
            throw new IllegalArgumentException("Must be only -d or -a args");
        }
        if (strings.contains("-d")) {
            return SortDirection.DESC;
        }
        return SortDirection.ASC;
    }

    public static DataType fetchDataType(List<String> strings) {

        if (strings.contains("-i") && !strings.contains("-s")) {
            return DataType.INTEGER;
        }
        if (strings.contains("-s") && !strings.contains("-i")) {
            return DataType.STRING;
        }
        throw new IllegalArgumentException("Must be only -i or -s args");
    }

    public static List<Path> fetchInputFiles(List<String> strings) {

        if (strings.contains("-in")) {
            int index = strings.indexOf("-in") + 1;
            if (strings.size() <= index) {
                throw new IllegalArgumentException("Must contains input file. For example: \"-in file1.txt,file2.txt\"");
            }
            String paths = strings.get(index);
            if (paths == null || paths.isEmpty()) {
                throw new IllegalArgumentException("Must contains input file. For example: \"-in file1.txt,file2.txt\"");
            }
            String[] pathArray = paths.split(",");
            List<Path> files = new ArrayList<>(pathArray.length);
            for (String rawPath : pathArray) {
                if (rawPath == null || rawPath.isEmpty()) {
                    System.out.println("Illegal filename was skipped");
                    continue;
                }
                Path path = Paths.get(rawPath);
                if (Files.notExists(path)) {
                    System.out.println("File is not exists: " + rawPath);
                    continue;
                }
                files.add(path);

            }
            return files;
        }
        throw new IllegalArgumentException("Must contains input file. For example: \"-in file1.txt,file2.txt\"");
    }

    public static Path fetchOutputFile(List<String> strings) {

        if (strings.contains("-out")) {
            int index = strings.indexOf("-out") + 1;
            if (strings.size() <= index) {
                throw new IllegalArgumentException("Must contains output file. For example: \"-out file.txt\"");
            }
            String path = strings.get(index);
            if (path == null || path.isEmpty()) {
                throw new IllegalArgumentException("Must contains output file. For example: \"-out file.txt\"");
            }
            return Paths.get(path);
        }
        throw new IllegalArgumentException("Must contains output file. For example: \"-out file.txt\"");
    }

    public static Path fetchTempDir(List<String> strings) {

        if (strings.contains("-temp")) {
            int index = strings.indexOf("-temp") + 1;
            if (strings.size() <= index) {
                return Paths.get("tmp");
            }
            String path = strings.get(index);
            if (path == null || path.isEmpty()) {
                return Paths.get("tmp");
            }
            return Paths.get(path);
        }
        return Paths.get("tmp");
    }
}
