package com.jlukaszuk.utils.file.pdf;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import com.jlukaszuk.viewModel.WordView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.nio.BufferUnderflowException;
import java.text.MessageFormat;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class PdfFileCreator {

    public static void saveToPdf(List<WordView> wordViewList, String pattern, File file, Rectangle pageSize, ResourceBundle resourceBundle, int fontSize) throws DocumentException, FileNotFoundException
    {
        Pattern patternCheck = Pattern.compile("((\\{(\\d+)\\}\\s+\\-\\s+){0,2}\\{(\\d+)\\})");

        if (resourceBundle.getBaseBundleName()==null)
            throw new BufferUnderflowException();

        if (fontSize <= 0)
            throw  new NullPointerException();

        if (wordViewList.size() < 1 || patternCheck.matcher(pattern).matches()){
            throw  new IllegalArgumentException();
        }


       Font basicFont = new Font(Font.FontFamily.TIMES_ROMAN, fontSize,
                Font.NORMAL, BaseColor.BLACK);

       Font titleFont = new Font(Font.FontFamily.TIMES_ROMAN, fontSize+2,
                Font.BOLD, BaseColor.BLACK);

        Document document = new Document(pageSize, 5.0F, 5.0F, 5.0F, 5.0F);
        PdfWriter.getInstance(document, new FileOutputStream(file.getPath() + ".pdf"));
        document.open();

        Paragraph title = new Paragraph(resourceBundle.getString("application.title")+"\n\n", titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        title.setIndentationRight(50);
        document.add(title);

        for (WordView word : wordViewList) {
            Paragraph paragraph = new Paragraph(MessageFormat.format(pattern, word.getIssue(), word.getMean(), word.getCategory(), word.getSubCategory(), word.isKnowledgeStatus()), basicFont);
            paragraph.setAlignment(Element.ALIGN_LEFT);
            document.add(paragraph);
        }
        document.close();
    }
}

