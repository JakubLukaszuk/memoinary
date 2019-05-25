package com.jlukaszuk.test;

import com.jlukaszuk.utils.file.DirHandler;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import java.util.IllegalFormatException;

class DirHandlerTest {
    @Test
    void shouldThrownFileFormatExceptionForEmptyPath() {
        DirHandler dirHandler = new DirHandler();
        Assertions.assertThrows(IllegalFormatException.class,() -> {
            dirHandler.crateDirIfNotExist("");
        });
    }

    @Test
    void shouldThrownNullPointerExceptionForNull() {
        DirHandler dirHandler = new DirHandler();
        Assertions.assertThrows(NullPointerException.class,() -> {
            dirHandler.crateDirIfNotExist(null);
        });
    }
}