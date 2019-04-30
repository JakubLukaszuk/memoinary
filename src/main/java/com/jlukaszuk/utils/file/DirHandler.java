package com.jlukaszuk.utils.file;

import com.jlukaszuk.utils.DialogUtils;

import java.io.File;


public class DirHandler {
    public void crateDirIfNotExist(String pathname)
    {
        File file = new File(pathname);

        if (!file.exists()) {
            if (file.mkdir()) {
                DialogUtils.informDialog( "just created: "+file.getAbsolutePath(),"Creating Directory");
            } else {
                DialogUtils.informDialog( "creating: "+pathname+"floder failed","Creating Directory");
            }
        }
    }
}
