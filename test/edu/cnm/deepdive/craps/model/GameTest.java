package edu.cnm.deepdive.craps.model;

import static org.junit.jupiter.api.Assertions.*;

import edu.cnm.deepdive.craps.model.Game.State;
import java.security.SecureRandom;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GameTest {


  @Test
  void testComeOut(){
    for (int roll: new int[] {2,3,12}) {
      assertSame(State.LOSS, State.COME_OUT.roll(roll, 0));
    }
    for(int roll: new int[] {4,5,6,8,9,10}){
      assertSame(State.POINT, State.COME_OUT.roll(roll, 0));
    }
    for(int roll: new int[] {7,11}){
      assertSame(State.WIN, State.COME_OUT.roll(roll, 0));
    }
  }

  @Test
  void testPointPoint(){
    for(int point: new int[]{4,5,6,8,9,10}){
      for(int roll = 2; roll<=12; roll++){
        if(roll!=7 && roll!=point){
          assertSame(State.POINT, State.POINT.roll(roll, point));
        }
      }
    }
  }

  @Test
  void testPointWin(){
    for(int point: new int[]{4,5,6,8,9,10}){
      for(int roll = 2; roll<=12; roll++){
        if(roll==point){
          assertSame(State.WIN, State.POINT.roll(roll, point));
        }
      }
    }
  }

  @Test
  void testPointLoss(){
    for(int point: new int[]{4,5,6,8,9,10}){
      for(int roll = 2; roll<=12; roll++){
        if(roll==7){
          assertSame(State.LOSS, State.POINT.roll(roll, point));
        }
      }
    }
  }

  @Test
  void testReset(){
    Game game = new Game(new SecureRandom());
    game.reset();
    assertSame(State.COME_OUT, game.getState());
    game.play();
    game.play();
    game.reset();
    assertSame(State.COME_OUT, game.getState());
  }

  @Test
  void testPlay(){
    Game game = new Game(new SecureRandom());
    game.play();
    int total = game.getLosses() + game.getWins();
    assertSame(1, total);
  }

}