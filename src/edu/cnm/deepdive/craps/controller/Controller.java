package edu.cnm.deepdive.craps.controller;

import edu.cnm.deepdive.craps.model.Game;
import edu.cnm.deepdive.craps.model.Game.Roll;
import edu.cnm.deepdive.craps.model.Game.State;
import edu.cnm.deepdive.craps.view.RollCell;
import java.security.SecureRandom;
import java.util.List;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.css.Style;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.text.Text;
import javafx.util.Callback;

public class Controller {

  private Game game;
  private boolean running;
  private String tallyFormat;

  @FXML
  private ResourceBundle resources;//this must be named "resources", if you've got this then fxml will autowire it
  @FXML
  private MenuItem fast;
  @FXML
  private MenuItem pause;
  @FXML
  private MenuItem reset;
  @FXML
  private Text tally;
  @FXML
  private MenuItem next;
  @FXML
  private ListView<Roll> rolls;

  @FXML
  private void initialize() {
    tallyFormat = tally.getText();//store the format string
//    rolls.setCellFactory(new Callback<ListView<Roll>, ListCell<Roll>>() {  //anonymous class method, lambda method below
//      @Override
//      public ListCell<Roll> call(ListView<Roll> param) {
//        return new RollCell(resources);
//      }
//    });
    rolls.setCellFactory((lv) -> new RollCell(resources));
    rolls.getStyleClass().add("");
    reset(null);
    updateMenu();
  }

  @FXML
  private void next(ActionEvent actionEvent) {
    game.play();
    updateTally(game.getWins(), game.getLosses());
    updateRolls(game.getRolls());
  }

  @FXML
  private void fast(ActionEvent actionEvent) {
    running = true;
    updateMenu();
    new Runner().start();
  }

  @FXML
  private void pause(ActionEvent actionEvent) {
    running = false;

  }

  @FXML
  private void reset(ActionEvent actionEvent) {
    game = new Game(new SecureRandom());
    updateTally(game.getWins(), game.getLosses());
    updateRolls(game.getRolls());
  }

  private void updateTally(int wins, int losses) {
    int total = wins + losses;
    double percentage = (total != 0) ? 100.0 * wins / total : 0;
    tally.setText(String.format(tallyFormat, wins, total, percentage));
  }

  private void updateRolls(List<Roll> diceRolls) {
    List<String> styleClasses = rolls.getStyleClass();
    styleClasses.remove(styleClasses.size() - 1);
    rolls.getItems().clear();
    if (!diceRolls.isEmpty()) {
      State state = diceRolls.get(diceRolls.size() - 1).getState();
      if (state == State.WIN) {
        styleClasses.add("win");
      } else if (state == State.LOSS) {
        styleClasses.add("loss");
      }
      rolls.getItems().addAll(diceRolls);
    } else {
      styleClasses.add("");
    }
  }

  public void stop(){
    pause(null);
  }

  private void updateMenu() {
    next.setDisable(running);
    fast.setDisable(running);
    pause.setDisable(!running);
    reset.setDisable(running);
  }

  private class Runner extends Thread {

    private static final int TALLY_UPDATE_INTERVAL = 2000;
    private static final int ROLLS_UPDATE_INTERVAL = 10_000;

    @Override
    public void run() {
      int count = 0;
      while (running) {
        game.play();
        count++;
        if (count % TALLY_UPDATE_INTERVAL == 0) {
          int wins = game.getWins();
          int losses = game.getLosses();
          Platform.runLater(() -> updateTally(wins,
              losses));//this lambda dow not use a semicolon after update tally because there is just a single statement
        }
        if (count % ROLLS_UPDATE_INTERVAL == 0) {
          var rolls = game.getRolls();//var is ok to use for a local variable, in java10 and higher
          Platform.runLater(() -> updateRolls(rolls));
        }
      }
      var wins = game.getWins();
      var losses = game.getLosses();
      var rolls = game.getRolls();
      Platform.runLater(() -> {
        updateTally(wins, losses); //local variables used in lambdas must be final; local variables CANNOT shadow other local variables
        updateRolls(rolls);
        updateMenu();
      });


    }
  }

}
