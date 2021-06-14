package com.android.kitob.service;



import com.android.kitob.payload.ApiResponse;
import com.android.kitob.utils.Converter;
import com.lowagie.text.DocumentException;
import fr.opensagres.poi.xwpf.converter.pdf.PdfConverter;
import fr.opensagres.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;


@Service
public class WordToPdf {

//    public static void main(String[] args) {
//        //this should be same as class name
//        //create object of class
//        WordToPdf cwoWord = new WordToPdf();
//        //you can specify your own path on the basis of file located
//        cwoWord.wortToPdf("/home/kali/Documents/diplom/Soliyev/kitob/2021/6/13/NP4g8v.docx", "/home/kali/Documents/diplom/Soliyev/kitob/2021/6/13/NP4g8v.pdf");
//    }

    @Autowired
    private Converter converter;


    public void wortToPdf(String docPath, String pdfPath){
        try {
            InputStream doc = new FileInputStream(new File(docPath));
            System.out.println(doc+" stream");

            XWPFDocument document = new XWPFDocument(doc);

            PdfOptions options = PdfOptions.create();
            OutputStream out = new FileOutputStream(new File(pdfPath));
            PdfConverter.getInstance().convert(document, out, options);
            document.close();
            out.close();

            converter.apiSuccess(pdfPath);

//            }

        } catch (IOException ex) {
            System.out.println(ex.getMessage());
        }
    }

}
