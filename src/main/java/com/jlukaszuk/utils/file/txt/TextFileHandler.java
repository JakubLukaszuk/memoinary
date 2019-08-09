package com.jlukaszuk.utils.file.txt;


import com.jlukaszuk.viewModel.WordView;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

public class TextFileHandler {

    private static Pattern _pattern = Pattern.compile("((\\{(\\d+)\\}\\s+\\-\\s+){0,2}\\{(\\d+)\\})" , Pattern.UNICODE_CHARACTER_CLASS);

    public static void SaveTxtFile(List<WordView> words, String formatPattern, File file) throws IOException {

        if (!_pattern.matcher(formatPattern).matches())
            throw new IllegalArgumentException();

        StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(formatPattern).append(System.getProperty("line.separator"));

            words.forEach(word -> {
                stringBuilder.append(MessageFormat.format(formatPattern, word.getIssue(), word.getMean(), word.isKnowledgeStatus())).append(System.getProperty("line.separator"));
            });

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(stringBuilder.toString());
        }

    }

    public static List<WordView> ReadTxtFile(File file) throws IOException{

        if (file != null) {
            BufferedReader linesFirst = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8));

            Optional<String> format = linesFirst.lines().findFirst();

            linesFirst.close();

            if (_pattern.matcher(format.get()).matches() || _pattern.matcher(format.get().substring(1)).matches()) {
                //second check is for remove empty char from file that use UTF 8 format.
                BufferedReader data = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8));
                List<WordView> wordViewList = new ArrayList<>();
                String patternFormated = format.get().replaceAll("\\D+", "");
                char[] patternValues = patternFormated.toCharArray();

                data.lines().skip(1).forEach(s -> {
                    String[] wordArray = new String[1];
                    if (patternValues.length > 1)
                        wordArray = s.split("-");
                    else
                        wordArray[0] = s;

                    WordView wordView = new WordView();
                    for (int i = 0; i < wordArray.length; i++) {
                        switch (patternValues[i]) {
                            case '0':
                                wordView.setIssue(wordArray[i]);
                                break;
                            case '1':
                                wordView.setMean(wordArray[i]);
                                break;
                            case '2':
                                wordView.setKnowledgeStatus(Boolean.parseBoolean(wordArray[i]));
                                break;
                        }
                    }
                    wordView.setModifed(true);
                    wordViewList.add(wordView);

                });
                data.close();
                return wordViewList;
            }
        }
        else throw new NullPointerException();
        return null;
    }
}

