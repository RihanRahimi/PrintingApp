
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.print.*;
import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.standard.Copies;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
                    filePaths.add(file.get(i).getPath());
                    lable +=file.get(i).getPath() + "\n";
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

            PrintService ps = pss[0];
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
}

