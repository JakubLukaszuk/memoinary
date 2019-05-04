package com.jlukaszuk.utils.file;

import com.jlukaszuk.utils.DialogUtils;

import java.io.File;


public class DirHandler {
    public void crateDirIfNotExist(String pathname)
    {
        File file = new File(pathname);

        if (!file.exists()) {
            if (!file.mkdir()) {
                DialogUtils.informDialog( "creating: "+pathname+"floder failed","Creating Directory");
            }
        }
    }
}
