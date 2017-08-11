import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.shape.Path;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.xwpf.converter.pdf.PdfConverter;
import org.apache.poi.xwpf.converter.pdf.PdfOptions;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RunApp extends Application {

    BorderPane root = new BorderPane();
    HBox hbox = new HBox();

    Button btn = new Button("select");
    Button btnStartPrinting = new Button("Start");
    FileChooser fc = new FileChooser();
    Label l = new Label("Hallo world!!");


    List<String> filePaths = new ArrayList<String>();
    List<File> file = new ArrayList<File>();


    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage primaryStage) throws Exception{
        Scene scene = new Scene(root, 1000, 500);
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(scene);
        primaryStage.show();
        erstelleKomponenten();
    }

    public void erstelleKomponenten() throws IOException, PrintException {
        //l.setPadding(new Insets((scene.getHeight() / 2) - 30,scene.getWidth()/2,scene.getHeight()/2,scene.getWidth()/2-30));
        l.setFont(new Font(15));

        btn.setPrefSize(100,20);
        btnStartPrinting.setPrefSize(100,20);

        hbox.getChildren().addAll(btn,btnStartPrinting);
        hbox.setPadding(new Insets(100,100,100,100));
        hbox.setSpacing(100);
        hbox.setAlignment(Pos.CENTER);

        root.setTop(hbox);
        root.setCenter(l);

        getFilePath();

        btnStartPrinting.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
                    printTheFiles();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (PrintException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    public void getFilePath() throws PrintException, IOException {

        btn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                file = fc.showOpenMultipleDialog(null);
                int size =file.size();
                String lable = "";
                for(int i = 0;i<size;i++){
                    if(file.get(i).getPath().contains(".pdf")){
                        filePaths.add(file.get(i).getPath());
                        lable +=file.get(i).getPath() + "\n";
                        System.out.println("pdf");
                    }
                    else{
                        String path =setNewPDFPaths(file.get(i).getPath());
                        pdfConverterr(file.get(i).getPath(),path);
                        filePaths.add(path);
                        lable+= path + "\n";
                        System.out.println("docx");

                    }
                }
                l.setText(lable);
            }
        });
    }

    public void printTheFiles() throws IOException, PrintException, InterruptedException {
        try {
            PrintRequestAttributeSet pras = new HashPrintRequestAttributeSet();
            pras.add(new Copies(1));

            PrintService pss[] = (PrintService[]) PrintServiceLookup.lookupPrintServices(DocFlavor.INPUT_STREAM.GIF, pras);

            if (pss.length == 0)
                throw new RuntimeException("No printer services available.");

            PrintService ps = pss[2];
            System.out.println("Printing to " + ps);

            int size = filePaths.size();
            for(int i = 0;i<size;i++){

                DocPrintJob job = ps.createPrintJob();
                FileInputStream fin = new FileInputStream(filePaths.get(i));
                Doc doc = new SimpleDoc(fin, DocFlavor.INPUT_STREAM.PDF, null);
                job.print(doc, pras);
                Thread.sleep(1000);
                fin.close();

            }
        } catch (IOException ie) {
            ie.printStackTrace();
        } catch (PrintException pe) {
            pe.printStackTrace();
        }

    }
    public String setNewPDFPaths(String oldPath){

            int nrOfSlash = StringUtils.countMatches(oldPath, "\\");
            String fileName = oldPath.split("\\\\")[nrOfSlash];
            String pathWithoutTheFile = fileName;
            pathWithoutTheFile = oldPath.replace(pathWithoutTheFile,"");
            String newPath = pathWithoutTheFile+ "testFolder " + new Random().nextInt(100);
            new File(newPath).mkdir();
            newPath += "\\";
            System.out.println(pathWithoutTheFile);
            System.out.println(newPath);
            return newPath + fileName.split(".docx")[0] + ".pdf";


    }
    public static void pdfConverterr(String docxPath, String pdfPath){
        try{

            InputStream doc = new FileInputStream(new File(docxPath));
            OutputStream out = new FileOutputStream(new File(pdfPath));

            XWPFDocument d = new XWPFDocument(doc);
            PdfConverter.getInstance().convert(d, out, PdfOptions.create());
        }
        catch (Exception e){ e.printStackTrace();}

    }
}

