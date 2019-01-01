package View;

import Modle.Documentt;
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
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;

import javax.xml.bind.Element;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.*;
import java.util.List;

public class Controller {
    public View_Model viewModel;
    private Stage primaryStage;
    public javafx.scene.control.TextArea corpus_path;
    public javafx.scene.control.TextArea singleQuery;
    public javafx.scene.control.TextArea queriesFile;
    public javafx.scene.control.TextArea posting_destanation_path;
    public javafx.scene.control.TextArea resultPath;
    public javafx.scene.control.CheckBox stemming;
    public javafx.scene.control.CheckBox semantic;
    public javafx.scene.control.ChoiceBox language;
    public ObservableList<String> list= FXCollections.observableArrayList();
    public ObservableList<String> listCity= FXCollections.observableArrayList();
    public javafx.scene.control.Button start;
    public javafx.scene.control.Button reset;
    public javafx.scene.control.Button showDic;
    public javafx.scene.control.Button loadDic;
    public javafx.scene.control.Button browse_path;
    public javafx.scene.control.Button folderDialog;
    public javafx.scene.control.Button browse_dest;
    public javafx.scene.control.Button browse_QueriesFile;

    public javafx.scene.control.Button search;
    public File file;
    public FileChooser fileChooser;
    public ListView listView;
    public boolean loadFirst=true;
    public ArrayList<String> citiesWhomFilterd;
    public CheckComboBox cities;


    public void initializ(){
        showDic.setDisable(true);
        reset.setDisable(true);
        search.setDisable(true);

    }

    private void showAlert(String alertMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(alertMessage);
        alert.show();
    }

    public void setViewModel(View_Model v){this.viewModel=v;}

    public void start(){
        long startTime = System.nanoTime();
        boolean wantToStem=this.stemming.isSelected();
        String corpus_path=this.corpus_path.getText();
        String destination=this.posting_destanation_path.getText();
        if(corpus_path.equals("")|| destination.equals(""))
                 showAlert("please enter corpus path and Posting & Dictionary Destination");
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Just a few minutes, We are working on it....");
            alert.show();
            viewModel.start(corpus_path, destination, wantToStem);
            alert.close();
            HashSet<String> leng= viewModel.getLanguages();
            Iterator<String> itr = leng.iterator();
            while(itr.hasNext()){
                String toAdd=itr.next();
                if(!hasDigit(toAdd))
                    list.add(toAdd);
            }
            list.sort(String::compareToIgnoreCase);
            language.setItems(list);
            long finishTime 	= System.nanoTime();
            int N=viewModel.numOfDocs();
            int Terms=viewModel.numOfTerms();
            int cities=viewModel.numOfCities();
            double time=(finishTime - startTime)/1000000.0/1000;
            showAlert("The dictionary build successfully!\n It took: "+ time +"sec.\n" +"Number of terms in dictionary: "+ Terms+"\n"+"Number of documents indexed: "+N+"\nNumber of cities in cities index: "+ cities);
            showDic.setDisable(false);
            reset.setDisable(false);
            search.setDisable(false);

        }

    }

    public void Search(){
        String single=singleQuery.getText();
        String queryFile=queriesFile.getText();
        String destQuery=resultPath.getText();
        if(destQuery.equals("")) {
            showAlert("Please choose directory where the results will save.");
            return;
        }
        if((!single.equals(""))&&(!queryFile.equals(""))) {
            showAlert("Please make sure only one query window is full.");
            return;
        }
        String corpus=corpus_path.getText();
        if(corpus.equals("")){
            showAlert("Please enter the corpus path (with the stop words file).");
            return;
        }

        boolean wantToStem=this.stemming.isSelected();
        boolean semantic=this.semantic.isSelected();
        ArrayList<String> cities_limitation=new ArrayList<>();
        if(cities.getCheckModel().getCheckedItems()!=null){
            cities_limitation.addAll(cities.getCheckModel().getCheckedItems());
        }

        if(!single.equals("")){
            ArrayList<Map.Entry<Documentt,Double>> ansSingle=viewModel.Search_single_query(single,wantToStem,semantic,cities_limitation,corpus);
            openResultsForSingle(ansSingle);
        }
        else if(!queryFile.equals("")){
            LinkedHashMap<String, ArrayList<Map.Entry<Documentt,Double>> > ansFile=viewModel.Search_files_quries(queryFile,wantToStem,semantic,cities_limitation,corpus,destQuery);
            }
    }

    private void openResultsForSingle(ArrayList<Map.Entry<Documentt, Double>> ansSingle) {

        TableView<resToShow> table = new TableView<resToShow>();
        final ObservableList<resToShow> data =FXCollections.observableArrayList();

        for(Map.Entry<Documentt, Double> line:ansSingle) {
            Documentt d=line.getKey();
            String entities="";
            String adding="";
            for(int i=0;i<d.getYeshooyot().size();i++){
                String toAdd=d.getYeshooyot().get(i);
                String [] splited=toAdd.split("[+]");
                adding="The entity: "+splited[0]+", The score: "+splited[1]+"\n";
                entities+=adding;
            }
            data.add(new resToShow(line.getKey().Doc_Name, "1",entities));
        }


        Stage stage=new Stage();
        Scene scene = new Scene(new Group());
        stage.setTitle("Results");
        stage.setWidth(500);
        stage.setHeight(500);

        final javafx.scene.control.Label label = new Label("Results");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn firstNameCol = new TableColumn("Query Id");
        firstNameCol.setMinWidth(100);
        firstNameCol.setCellValueFactory(
                new PropertyValueFactory<resToShow, String>("queryId"));

        TableColumn secondNameCol = new TableColumn("Doc Id");
        secondNameCol.setMinWidth(100);
        secondNameCol.setCellValueFactory(
                new PropertyValueFactory<resToShow, String>("docNo"));

        TableColumn actionCol = new TableColumn("Entities");
        actionCol.setMinWidth(100);
        actionCol.setCellValueFactory(new PropertyValueFactory<>("DUMMY"));


        Callback<TableColumn<resToShow, String>, TableCell<resToShow, String>> cellFactory
                = //
                new Callback<TableColumn<resToShow, String>, TableCell<resToShow, String>>() {
                    @Override
                    public TableCell call(final TableColumn<resToShow, String> param) {
                        final TableCell<resToShow, String> cell = new TableCell<resToShow, String>() {

                            final Button btn = new Button("Entities");

                            @Override
                            public void updateItem(String item, boolean empty) {
                                super.updateItem(item, empty);
                                if (empty) {
                                    setGraphic(null);
                                    setText(null);
                                } else {
                                    btn.setOnAction(event -> {
                                        resToShow res = getTableView().getItems().get(getIndex());
                                        showAlert(res.getEntities());

                                    });
                                    setGraphic(btn);
                                    setText(null);
                                }
                            }
                        };
                        return cell;
                    }
                };
        actionCol.setCellFactory(cellFactory);

        table.setItems(data);
        table.getColumns().addAll(firstNameCol,secondNameCol,actionCol);

        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table);

        ((Group) scene.getRoot()).getChildren().addAll(vbox);

        stage.setScene(scene);
        stage.show();

    }

   private boolean hasDigit(String toAdd) {
        for(int i=0;i<toAdd.length();i++){
            if(Character.isDigit(toAdd.charAt(i)))
                return true;
        }
        return false;
    }

    public void showDic() {
        Stage stage=new Stage();
        List<Term> list = viewModel.showDic();
        if(list==null)
            showAlert("You must first start the program or load exist dictionary!");
        else {
            listView = new ListView();
            for (Term t : list) {
                listView.getItems().add(t.getTerm() + "-----> The TF:    " + t.getTotal_frequncy_in_the_corpus());
            }
            Scene scene = new Scene(new Group());
            stage.initModality(Modality.APPLICATION_MODAL); //Lock the window until it closes
            final VBox vBox = new VBox();
            vBox.setSpacing(5);
            vBox.setPadding(new Insets(10, 0, 0, 10));

            vBox.getChildren().addAll(listView);
            vBox.setAlignment(Pos.CENTER);

            Group group = ((Group) scene.getRoot());
            group.getChildren().addAll(vBox);
            stage.setScene(scene);
            stage.show();
        }
    }

    public void browseQuery(){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        File dir = directoryChooser.showDialog(primaryStage);
        if (dir != null) {
            queriesFile.setText(dir.getAbsolutePath());
        } else {
            queriesFile.setText("");
        }
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

    public void folderDialog(){
        final DirectoryChooser directoryChooser = new DirectoryChooser();

        File dir = directoryChooser.showDialog(primaryStage);
        if (dir != null) {
            resultPath.setText(dir.getAbsolutePath());
        } else {
            resultPath.setText("");
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
        showAlert("Reset done successfully!");
    }

    public void loadDic(){
        if(loadFirst==false) {
            showAlert("The previous dictionary is override");
        }
        else loadFirst=false;

        boolean wantToStem=this.stemming.isSelected();
        String destination=this.posting_destanation_path.getText();
        if(destination.equals(""))
            showAlert("Pleas enter the path where you saved your dictionary at last launch");
        else {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Just a few minutes, We are working on it....");
            alert.show();
            boolean ans=viewModel.loadDic(wantToStem, destination);
            if(!ans) {
                alert.close();
                showAlert("No items to load");
            }
            else {
                alert.close();
                showAlert("Dictionary loaded successfully");
                showDic.setDisable(false);
                reset.setDisable(false);
                search.setDisable(false);
                HashSet<String> leng= viewModel.getLen(destination);
                Iterator<String> itr = leng.iterator();
                while(itr.hasNext()){
                    String toAdd=itr.next();
                    if(!hasDigit(toAdd))
                        list.add(toAdd);
                }
                list.sort(String::compareToIgnoreCase);
                language.setItems(list);

                ArrayList<String> citiesA=viewModel.getCitie();
                for(String c:citiesA){
                    if(c.contains("'")||c.contains("(")||c.contains("[")||c.contains(")")||c.contains("-")||hasDigit(c))
                        continue;
                    listCity.add(c);
                }
                listCity.sort(String::compareToIgnoreCase);
                cities.getItems().addAll(listCity);
            }

        }
    }

    public void setStage(Stage stage) {
        this.primaryStage = stage;
    }


}
