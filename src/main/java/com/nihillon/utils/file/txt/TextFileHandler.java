package com.nihillon.utils.file.txt;

import com.nihillon.models.SubCategory;
import com.nihillon.viewModel.CategoryView;
import com.nihillon.viewModel.SubCategoryView;
import com.nihillon.viewModel.WordView;
import javafx.collections.FXCollections;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Stream;

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

    public static List<WordView> ReadTxtFile(File file) throws IOException {

        Stream<String> linesFirst = Files.lines(Paths.get(String.valueOf(file)));
        Pattern pattern = Pattern.compile("((\\{(\\d+)\\}\\s+\\-\\s+){0,2}\\{(\\d+)\\})");

        Optional<String> format = linesFirst.findFirst();
        linesFirst.close();


        if(format.isPresent()&& pattern.matcher(format.get()).matches())
        {
            Stream<String> lines= Files.lines(Paths.get(String.valueOf(file)));
            List<WordView> wordViewList = new ArrayList<>();
            String patternFormated = format.get().replaceAll("\\D+","");
            char[] patternValues = patternFormated.toCharArray();

            lines.skip(1).forEach(s -> {
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
            lines.close();
            return wordViewList;
        }




        //read file into stream, try-with-resources
//        try (Stream<String> stream = Files.lines(Paths.get(file.getPath()))) {
//               stream.forEach(System.out::println);
//    }

        return null;
    }
}

