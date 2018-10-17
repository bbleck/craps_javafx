package edu.cnm.deepdive.craps;

import java.io.IOException;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

  private ClassLoader classLoader;
  private ResourceBundle bundle;
  private FXMLLoader fxmlLoader;


  public static void main(String[] args) {
    launch(args);
  }

  @Override
  public void start(Stage stage) throws Exception {
    setupLoaders();
    //todo: setup stage with results of load layout
  }

  private void setupLoaders(){
    classLoader = getClass().getClassLoader();
    bundle = ResourceBundle.getBundle("res/ui.properties");
    fxmlLoader = new FXMLLoader(classLoader.getResource("res/main.fxml"), bundle);

  }

  private Parent loadLayout() throws IOException {
     Parent root = fxmlLoader.load();
     //todo: do something more?
    return root;
  }

  private void setupStage(Stage stage, Parent root){
    Scene scene = new Scene(root);
    //todo: set title, icon, etc.
    stage.setResizable(false);
    stage.setScene(scene);
    stage.show();
  }

}
