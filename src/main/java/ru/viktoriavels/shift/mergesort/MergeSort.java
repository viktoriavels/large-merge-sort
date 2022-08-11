package ru.viktoriavels.shift.mergesort;

import ru.viktoriavels.shift.mergesort.item.FileItem;
import ru.viktoriavels.shift.mergesort.provider.FileItemProvider;
import ru.viktoriavels.shift.mergesort.provider.IntegerFileItemProvider;
import ru.viktoriavels.shift.mergesort.provider.StringFileItemProvider;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class MergeSort {

    private final SortDirection sortDirection;
    private final DataType dataType;
    private final Path output;
    private final List<Path> input;
    private final Path tempDir;
    private final List<Path> tempFiles;

    public MergeSort(SortDirection sortDirection, DataType dataType, Path output, List<Path> input, Path tempDir) {
        assert sortDirection != null;
        assert dataType != null;
        assert output != null;
        assert input != null && !input.isEmpty();
        assert tempDir != null;

        this.sortDirection = sortDirection;
        this.dataType = dataType;
        this.output = output;
        this.input = input;
        this.tempDir = tempDir;
        this.tempFiles = new ArrayList<>(input.size());
    }

    public void sort() throws IOException {

        initDirectories();
        try {
            if (input.size() == 1) {
                Files.copy(input.get(0), output);
                return;
            }

            sortImpl();
        } finally {
            for (Path tempFile : tempFiles) {
                try {
                    Files.deleteIfExists(tempFile);
                } catch (IOException e) {
                    System.err.println("Can not to delete file " + tempFile);
                    e.printStackTrace();
                }
            }
        }
    }

    private void initDirectories() throws IOException {
        Files.createDirectories(tempDir.toAbsolutePath());
        Files.createDirectories(output.toAbsolutePath().getParent());
    }

    private void sortImpl() throws IOException {

        Comparator<FileItem> comparator = composeComparator(sortDirection);

        Path masterFile = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            Path tempFilePath = Paths.get(tempDir.toString(), UUID.randomUUID() + ".tmp");
            tempFiles.add(tempFilePath);

            Path nextFile = input.get(i);
            sortTwoFilesDataToTempFile(comparator, masterFile, nextFile, tempFilePath);
            masterFile = tempFilePath;
        }
        Files.copy(masterFile, output, StandardCopyOption.REPLACE_EXISTING);
    }

    private void sortTwoFilesDataToTempFile(Comparator<FileItem> comparator, Path firstFile, Path secondFile,
                                            Path tempFilePath) throws IOException {

        try (BufferedWriter tempFileWriter = Files.newBufferedWriter(tempFilePath,
                StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.WRITE);
             FileItemProvider firstFileItemProvider = composeFileItemProvider(dataType, firstFile);
             FileItemProvider secondFileItemProvider = composeFileItemProvider(dataType, secondFile)) {

            sortTwoFilesDataToTempFile(comparator, tempFileWriter, firstFileItemProvider, secondFileItemProvider);
        }
    }

    private void sortTwoFilesDataToTempFile(Comparator<FileItem> comparator,
                                            BufferedWriter tempFileWriter,
                                            FileItemProvider masterFileItemProvider,
                                            FileItemProvider inputFileItemProvider) throws IOException {

        FileItem masterFileItem = masterFileItemProvider.next();
        FileItem inputFileItem = inputFileItemProvider.next();
        while (masterFileItem != null || inputFileItem != null) {
            if (masterFileItem == null) {
                tempFileWriter.append(inputFileItem.toWritableFormat());
                tempFileWriter.append("\r\n");
                inputFileItem = inputFileItemProvider.next();
            } else if (inputFileItem == null) {
                tempFileWriter.append(masterFileItem.toWritableFormat());
                tempFileWriter.append("\r\n");
                masterFileItem = masterFileItemProvider.next();
            } else if (comparator.compare(masterFileItem, inputFileItem) >= 0) {
                tempFileWriter.append(inputFileItem.toWritableFormat());
                tempFileWriter.append("\r\n");
                inputFileItem = inputFileItemProvider.next();
            } else {
                tempFileWriter.append(masterFileItem.toWritableFormat());
                tempFileWriter.append("\r\n");
                masterFileItem = masterFileItemProvider.next();
            }
        }
    }

    private Comparator<FileItem> composeComparator(SortDirection sortDirection) {

        Comparator<FileItem> comparator;
        if (sortDirection == SortDirection.ASC) {
            comparator = Comparator.naturalOrder();
        } else {
            comparator = Comparator.reverseOrder();
        }
        return comparator;
    }

    private FileItemProvider composeFileItemProvider(DataType dataType, Path path) throws IOException {

        if (dataType == DataType.INTEGER) {
            return new IntegerFileItemProvider(path);
        } else {
            return new StringFileItemProvider(path);
        }
    }

}
