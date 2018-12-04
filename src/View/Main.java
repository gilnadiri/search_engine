package View;

import Modle.Model;
import ViewModel.View_Model;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Model model = new Model();
        View_Model viewModel = new View_Model(model);
        //--------------
        primaryStage.setTitle("Search_Engine");
        FXMLLoader fxmlLoader = new FXMLLoader();
        Parent root = fxmlLoader.load(getClass().getResource("View.fxml").openStream());
        Scene scene = new Scene(root, 720, 560);
        primaryStage.setScene(scene);
        //--------------
        Controller controller = fxmlLoader.getController();
        controller.setViewModel(viewModel);
        controller.setStage(primaryStage);
        //--------------
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
