package edu.cnm.deepdive.craps.model;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

public class Game {


  public static final String DICE_STATE = "%s %s%n";
  public static final int NUMBER_OF_DICE = 2;
  public static final int NUMBER_OF_FACES = 6;
  public static final int CRAPS_NUMBER = 7;
  private State state = State.COME_OUT;
  private int point;
  private Random rng;
  private List<Roll> rolls;
  private int wins;
  private int losses;
  private final Object lock = new Object();

  public Game(Random rng) {
    this.rng = rng;
    rolls = new LinkedList<>();
    wins = 0;
    losses = 0;
  }

  public static class Roll {

    private final int[] dice;
    private final State state;

    public Roll(int[] dice, State state) {
      this.dice = Arrays.copyOf(dice, NUMBER_OF_DICE);
      this.state = state;
    }

    /**
     * A getter for the array of dice
     *
     * @return int[] of the dice
     */
    public int[] getDice() {
      return Arrays.copyOf(dice, NUMBER_OF_DICE);
    }

    /**
     * A getter for the state of the game
     * @return state
     */
    public State getState() {
      return state;
    }

    /**
     * A method to convert the game into a representative String
     * @return String
     */
    @Override
    public String toString() {
      return String.format(DICE_STATE, Arrays.toString(dice), state);
    }
  }

  private State rollDice() {
    int[] dice = {
        rng.nextInt(NUMBER_OF_FACES) + 1,
        rng.nextInt(NUMBER_OF_FACES) + 1
    };
    int total = dice[0] + dice[1];
    State state = this.state.roll(total, point);
    if (this.state == State.COME_OUT && state == State.POINT) {
      point = total;
    }
    this.state = state;
    synchronized (lock) {
      rolls.add(new Roll(dice, state));
    }
    return state;
  }

  /**
   * A method to reset the state of a game.
   */
  public void reset(){
    state = State.COME_OUT;
    synchronized (lock) {
      rolls.clear();
    }
    point = 0;
  }

  /**
   * Simulates a single game
   * @return State
   */
  public State play() {
    reset();
    while (state != State.WIN && state != State.LOSS) {
      rollDice();
    }
    if(state == State.WIN){
      wins++;
    }else{
      losses++;
    }
    return state;
  }

  /**
   * Getter method for game State
   * @return State
   */
  public State getState() {
    return state;
  }

  /**
   * Getter for game Rolls
   * @return List<Roll>
   */
  public List<Roll> getRolls() {
    synchronized (lock) {
      return new LinkedList<>(rolls);
    }
  }

  public enum State {//enums nested in another class are always static
    COME_OUT {
      @Override
      public State roll(int total, int point) {
        switch (total) {
          case 2:
          case 3:
          case 12:
            return LOSS;
          case 7:
          case 11:
            return WIN;
          default:
            return POINT;
        }
      }
    },
    WIN,
    LOSS,
    POINT {
      @Override
      public State roll(int total, int point) {
        if (total == point) {
          return WIN;
        } else if (total == CRAPS_NUMBER) {
          return LOSS;
        } else {
          return this;
        }
      }
    };

    /**
     * A method that will roll one time.
     * @param total
     * @param point
     * @return State
     */
    public State roll(int total, int point) {
      return this;
    }
  }

  /**
   * Getter for number of wins.
   * @return int
   */
  public int getWins() {
    return wins;
  }

  /**
   * Gett for number of losses.
   * @return int
   */
  public int getLosses() {
    return losses;
  }

}
