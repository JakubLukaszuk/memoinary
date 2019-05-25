package com.jlukaszuk.utils.file.txt;

import com.jlukaszuk.viewModel.WordView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;



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

        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            TextFileHandler.SaveTxtFile(new LinkedList<WordView>(), "[0]-[1]", new File("F:\\MojeProgramy\\memoinary\\src\\main\\resources\\test\\testReadandWrite.txt"));
        });
    }


}