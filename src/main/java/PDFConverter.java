import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;

public class PDFConverter {
    public static void main(String... args){
        try{

            InputStream doc = new FileInputStream(new File("/Users/RihanRahimi/Desktop/dummyData/testDocX.docx"));
            OutputStream out = new FileOutputStream(new File("/Users/RihanRahimi/Desktop/dummyData/test.pdf"));

            XWPFDocument d = new XWPFDocument(doc);
            PdfConverter.getInstance().convert(d, out, PdfOptions.create());
        }
        catch (Exception e){ e.printStackTrace();}

    }
}
