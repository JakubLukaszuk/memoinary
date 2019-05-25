package com.jlukaszuk.test;

import com.itextpdf.text.Rectangle;
import com.jlukaszuk.utils.file.pdf.PdfFileCreator;
import com.jlukaszuk.viewModel.WordView;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class PdfFileCreatorTest {

    @Test
    void shouldThrownIllegalArgumentExceptionForBadPattern() {
        List<WordView> list =new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(IllegalArgumentException.class,() -> {
            PdfFileCreator.saveToPdf(list, "dasd", new File(""), new Rectangle(1,1), ResourceBundle.getBundle("bundles.messagesData"), 10);
        });
    }

    @Test
    void shouldThrownIllegalArgumentExceptionForEmptyList() {
        List<WordView> list =new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(IllegalArgumentException.class,() -> {

            PdfFileCreator.saveToPdf(list, "dasd", new File(""), new Rectangle(1,1), ResourceBundle.getBundle("bundles.messagesData"), 10);
        });
    }

    @Test
    void shouldThrownMissingResourceExceptionnForNotExistingBundle() {
        List<WordView> list =new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(MissingResourceException.class,() -> {
            PdfFileCreator.saveToPdf(list, "{0} - {1}", new File(""), new Rectangle(1,1), ResourceBundle.getBundle(""), 10);
        });
    }

    @Test
    void shouldThrownMissingResourceExceptionnForEmptyList() {
        Assertions.assertThrows(MissingResourceException.class,() -> {
            PdfFileCreator.saveToPdf(new LinkedList<>(), "{0} - {1}", new File(""), new Rectangle(1,1), ResourceBundle.getBundle("bundles.messagesData"), 10);
        });
    }

    @Test
    void shouldThrownNullPointerExceptionForNullForNullList() {
        Assertions.assertThrows(NullPointerException.class,() -> {
            PdfFileCreator.saveToPdf(null, "{0} - {1}", new File(""), new Rectangle(1,1), ResourceBundle.getBundle("bundles.messagesData"), 10);
        });
    }

    @Test
    void shouldThrownNullPointerExceptionForNullForPattern() {
        List<WordView> list =new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(NullPointerException.class,() -> {
            PdfFileCreator.saveToPdf(list, null, new File(""), new Rectangle(1,1), ResourceBundle.getBundle("bundles.messagesData"), 10);
        });
    }

    @Test
    void shouldThrownNullPointerExceptionForNullForFile() {
        List<WordView> list =new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(NullPointerException.class,() -> {
            PdfFileCreator.saveToPdf(list, "{0} - {1}", null, new Rectangle(1,1), ResourceBundle.getBundle("bundles.messagesData"), 10);
        });
    }

    @Test
    void shouldThrownNullPointerExceptionForNullRectangle() {
        List<WordView> list =new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(NullPointerException.class,() -> {
            PdfFileCreator.saveToPdf(list, "{0} - {1}", new File(""), null, ResourceBundle.getBundle("bundles.messagesData"), 10);
        });
    }

    @Test
    void shouldThrownNullPointerExceptionForZeroFontSize() {
        List<WordView> list =new LinkedList<>();
        list.add(new WordView());

        Assertions.assertThrows(NullPointerException.class,() -> {
            PdfFileCreator.saveToPdf(list, "{0} - {1}", new File(""), new Rectangle(1,1), ResourceBundle.getBundle("bundles.messagesData"), 0);
        });
    }

}