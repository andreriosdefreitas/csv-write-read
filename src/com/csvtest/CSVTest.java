package com.csvtest;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.LongStream;
import java.util.stream.Stream;

public class CSVTest {

    public static void main(String[] args) throws IOException {
        CSVTest.createCSV();
    }

    public static void createCSV() throws IOException {
        List<String> values = new ArrayList<>();

        Integer numberOfColumns = 1000;

        IntStream.range(0, numberOfColumns).forEach(value -> values.add(""+value));

        String line = String.join(",", values);
        Long numberOfLines = 1000000l;
        System.out.println("Start write csv file with: " + numberOfLines + " lines and columns: " + numberOfColumns);
        LocalDateTime oldDate = LocalDateTime.now();

        File outputFile = Path.of( "c:/csvtest/test.csv").toFile();
        try (PrintWriter printWriter = new PrintWriter(outputFile)) {
            LongStream.range(0, numberOfLines).forEach(value -> printWriter.println(line));
        }

        LocalDateTime newDate = LocalDateTime.now();
        Duration duration = Duration.between(oldDate, newDate);

        long writeTime = duration.toMillis();
        System.out.println("Done... milliseconds: " + writeTime);

        System.out.println("Start read csv file: ");
        oldDate = LocalDateTime.now();
        try (Stream<String> stream = Files.lines(outputFile.toPath())) {
            AtomicInteger counter = new AtomicInteger();

            stream.collect(Collectors.groupingBy(x->counter.getAndIncrement()/100))
                    .values()
                    .forEach(strings -> System.out.println(counter));
        }

        newDate = LocalDateTime.now();
        duration = Duration.between(oldDate, newDate);
        long readTime = duration.toMillis();
        System.out.println("Done... milliseconds: " + readTime);
        System.out.println("Total time: " + (writeTime + readTime));

    }
}
