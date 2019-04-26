package com.nihillon.utils.file.txt;


import com.nihillon.viewModel.WordView;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.text.MessageFormat;
import java.util.*;
import java.util.regex.Pattern;

public class TextFileHandler {

    public static void SaveTxtFile(List<WordView> words, String formatPattern, File file) throws IOException {

        StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(formatPattern).append(System.getProperty("line.separator"));
            words.forEach(word -> {
                stringBuilder.append(MessageFormat.format(formatPattern, word.getIssue(), word.getMean(), word.isKnowledgeStatus())).append(System.getProperty("line.separator"));;
            });

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(stringBuilder.toString());
        }

    }

    public static List<WordView> ReadTxtFile(File file) throws IOException{

        BufferedReader linesFirst = new BufferedReader(new InputStreamReader(
                        new FileInputStream(file), StandardCharsets.UTF_8));

        Pattern pattern = Pattern.compile("((\\{(\\d+)\\}\\s+\\-\\s+){0,2}\\{(\\d+)\\})");

        Optional<String> format = linesFirst.lines().findFirst();
        linesFirst.close();


        if(format.isPresent()&& pattern.matcher(format.get()).matches())
        {
            BufferedReader data = new BufferedReader(new InputStreamReader(
                    new FileInputStream(file), StandardCharsets.UTF_8));
            List<WordView> wordViewList = new ArrayList<>();
            String patternFormated = format.get().replaceAll("\\D+","");
            char[] patternValues = patternFormated.toCharArray();

            data.lines().skip(1).forEach(s -> {
                System.out.println(s);
                String[] wordArray = new String[1];
                 if (patternValues.length>1)
                     wordArray = s.split("-");
                 else
                     wordArray[0] = s;

                WordView wordView = new WordView();
              for (int i=0; i < wordArray.length; i++){
                  switch ( patternValues[i]){
                      case '0':
                          System.out.println(wordArray[i]+"  F");
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

        return null;
    }
}

