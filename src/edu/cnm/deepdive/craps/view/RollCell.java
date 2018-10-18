package edu.cnm.deepdive.craps.view;

import edu.cnm.deepdive.craps.model.Game.Roll;
import java.io.IOException;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

public class RollCell extends ListCell<Roll> {

  public static final String RES_ROLL_FXML = "res/roll.fxml";
  public static final String RES_FACE_D_PNG = "res/face_%d.png";
  public static final int NUMBER_FACES = 6;
  private ResourceBundle bundle;

  public RollCell(ResourceBundle bundle){
    this.bundle = bundle;
  }

  @Override
  protected void updateItem(Roll item, boolean empty) {
    super.updateItem(item, empty);
    setText(null);
    if(empty){
      setGraphic(null);
    }else{
      try {
        ClassLoader classLoader = getClass().getClassLoader();
        FXMLLoader fxmlLoader = new FXMLLoader(classLoader.getResource(RES_ROLL_FXML), bundle);
        Controller controller = new Controller();
        fxmlLoader.setController(controller);
        Parent root = fxmlLoader.load();
        controller.setRoll(item);
        setGraphic(root);//setGraphic actually sets a node, it is a badly named method
      } catch (IOException e) {
        throw new RuntimeException(e);
      }
    }
  }

  private static class Controller {
    private static final String DIE_FACE_FORMAT = RES_FACE_D_PNG;
    private static Image[] faces;
    static{
      ClassLoader loader = Controller.class.getClassLoader();
      faces = new Image[NUMBER_FACES];
      for (int i = 0; i < faces.length; i++) {
        faces[i] = new Image(loader.getResourceAsStream(String.format(DIE_FACE_FORMAT, i+1)));
      }
    }

    private String totalFormat;
    @FXML
    private ImageView die0;
    @FXML
    private ImageView die1;
    @FXML
    private Text total;

    @FXML
    private void initialize(){
      totalFormat = total.getText();
    }

    private void setRoll(Roll roll){
      int[] dice = roll.getDice();
      die0.setImage(faces[dice[0]-1]);
      die1.setImage(faces[dice[1]-1]);
      total.setText(String.format(totalFormat, dice[0]+dice[1]));
    }


  }


}
