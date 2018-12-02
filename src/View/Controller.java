package View;

import Modle.Term;
import ViewModel.View_Model;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.Element;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.List;
import java.util.Observable;

public class Controller {
    public View_Model viewModel;
    private Stage primaryStage;

    public javafx.scene.control.TextArea corpus_path;
    public javafx.scene.control.TextArea posting_destanation_path;
    public javafx.scene.control.CheckBox stemming;
    public javafx.scene.control.ChoiceBox language;
    public ObservableList<String> list= FXCollections.observableArrayList("English","Spanish","Hebrew","France");
    public javafx.scene.control.Button start;
    public javafx.scene.control.Button reset;
    public javafx.scene.control.Button showDic;
    public javafx.scene.control.Button loadDic;
    public javafx.scene.control.Button browse_path;
    public javafx.scene.control.Button browse_dest;
    public File file;
    public FileChooser fileChooser;
    public ListView listView;

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();

    }

    public void Initialize(){
        language.setItems(list);
    }

    public void setViewModel(View_Model v){this.viewModel=v;}

    public void start(){
        long startTime = System.nanoTime();
        boolean wantToStem=this.stemming.isSelected();
        String corpus_path=this.corpus_path.getText();
        String destination=this.posting_destanation_path.getText();
        if(corpus_path.equals("")|| destination.equals(""))
                 showAlert("please enter corpus path and Posting & Dictionary Destanation");
        else {
            viewModel.start(corpus_path, destination, wantToStem);
            showAlert("The dictionary build");
        }
        long finishTime 	= System.nanoTime();
        System.out.println("Time for test:  " + (finishTime - startTime)/1000000.0 + " ms");
    }

    public void showDic() {
        Stage stage=new Stage();
        List<Term> list = viewModel.showDic();
        listView = new ListView();
        for (Term t : list) {
            listView.getItems().add(t.getTerm()+"-----> The DF:    "+t.getDf());
        }
        Scene scene=new Scene(new Group());
        stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
        final VBox vBox=new VBox();
        vBox.setSpacing(5);
        vBox.setPadding(new Insets(10,0,0,10));

        vBox.getChildren().addAll(listView);
        vBox.setAlignment(Pos.CENTER);

        Group group=((Group) scene.getRoot());
        group.getChildren().addAll(vBox);
        stage.setScene(scene);
        stage.show();
    }


    public void browsePath(){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

            File dir = directoryChooser.showDialog(primaryStage);
            if (dir != null) {
                    corpus_path.setText(dir.getAbsolutePath());
            } else {
                corpus_path.setText("");
             }
        }

    public void browseDest(){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        File dir = directoryChooser.showDialog(primaryStage);
        if (dir != null) {
            posting_destanation_path.setText(dir.getAbsolutePath());
        } else {
            posting_destanation_path.setText("");
        }

    }

    public void reset(){
        String destination=this.posting_destanation_path.getText();
        viewModel.reset(destination);
    }

    public void loadDic(){
        boolean wantToStem=this.stemming.isSelected();
        String destination=this.posting_destanation_path.getText();
        viewModel.loadDic(wantToStem,destination);
    }


    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }
}
