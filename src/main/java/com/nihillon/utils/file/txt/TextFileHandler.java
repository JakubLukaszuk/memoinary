package com.nihillon.utils.file.txt;

import com.nihillon.models.Word;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.List;
import java.util.stream.Stream;

public class TextFileHandler {

    public static void SaveTxtFile(List<Word> words, String formatPattern, Path path) throws IOException {


        try (
                BufferedWriter writer = Files.newBufferedWriter(path))
        {
            words.forEach(word -> {
                try {
                    writer.write(MessageFormat.format(formatPattern, word.getIssue(), word.getMean(), word.getCategory(), word.getSubCategory(), word.getKnowledgeStatus()));
                } catch (IOException e) {

                }

            });
        }
    }

    public void ReadTxtFile(String fileName) throws IOException {

        //read file into stream, try-with-resources
        try (Stream<String> stream = Files.lines(Paths.get(fileName))) {
            stream.forEach(System.out::println);

        }

    }
}

