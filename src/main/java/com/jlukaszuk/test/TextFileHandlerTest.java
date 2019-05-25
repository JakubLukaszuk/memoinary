package com.jlukaszuk.test;

import com.jlukaszuk.utils.file.txt.TextFileHandler;
import com.jlukaszuk.viewModel.WordView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


class TextFileHandlerTest {


    @Test
    void readShouldThrownNullPointerException() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            TextFileHandler.ReadTxtFile(null);
        });

    }

    @Test
    void readShouldThrownFileNotFoundException() {
        Assertions.assertThrows(FileNotFoundException.class,() -> {
            TextFileHandler.ReadTxtFile(new File(""));
        });
    }

    @Test
    void readFileGoodFormat() {
        try {
            Assertions.assertNull(
                TextFileHandler.ReadTxtFile(new File("F:\\MojeProgramy\\memoinary\\src\\main\\resources\\test\\testReadandWrite.txt")));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Test
    void writeShouldThrownNullPointerException() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            TextFileHandler.SaveTxtFile(null, null, null);
        });
    }

    @Test
    void writeShouldThrownIllegalArgumentException() {
        System.out.println(new File("F:\\MojeProgramy\\memoinary\\src\\main\\resources\\test\\testReadandWrite.txt").exists());
        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            TextFileHandler.SaveTxtFile(new LinkedList<WordView>(), "pattern", new File("F:\\MojeProgramy\\memoinary\\src\\main\\resources\\test\\testReadandWrite.txt"));
        });

    }

    @Test
    void saveTxtFileShouldThrownIllegalArgumentExceptionWithWellPatternAndList() {
        List<WordView> list = new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            TextFileHandler.SaveTxtFile(list, "{0} - {1}", new File("path"));
        });
    }

    @Test
    void saveTxtFileShouldThrownNullExceptionForNullList() {

        Assertions.assertThrows(NullPointerException.class,() -> {
            TextFileHandler.SaveTxtFile(null, "{0} - {1}", new File("F:\\MojeProgramy\\memoinary\\src\\main\\resources\\test\\testReadandWrite.txt"));
        });
    }

    @Test
    void saveTxtFileShouldThrownNullExceptionForNullPattern() {
        List<WordView> list = new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(NullPointerException.class,() -> {
            TextFileHandler.SaveTxtFile(list, null, new File("F:\\MojeProgramy\\memoinary\\src\\main\\resources\\test\\testReadandWrite.txt"));
        });
    }

    @Test
    void saveTxtFileShouldThrownNullExceptionForNullPath() {
        List<WordView> list = new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(NullPointerException.class,() -> {
            TextFileHandler.SaveTxtFile(list, "{0} - {1}", null);
        });
    }
}