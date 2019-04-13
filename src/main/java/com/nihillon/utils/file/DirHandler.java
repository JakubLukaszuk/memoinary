package com.nihillon.utils.file;

import com.nihillon.utils.DialogUtils;
import org.springframework.stereotype.Component;

import java.io.File;


public class DirHandler {
    public void crateDirIfNotExist(String pathname)
    {
        File file = new File(pathname);

        if (!file.exists()) {
            if (file.mkdir()) {
                DialogUtils.confirmDialog( "just created: "+file.getAbsolutePath(),"Creating Directory");
            } else {
                DialogUtils.confirmDialog( "creating: "+pathname+"floder failed","Creating Directory");
            }
        }
    }
}
