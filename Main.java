/**
 * Main.java
 *
 * TCSS342
 * Assignment 5
 */
import java.util.*;
/**
 * This is the driver for the maze generator
 * @author Bruce Baker
 * @version May 27, 2018
 */

public class Main {
   /**
    * The driver for the program.  Since testing it is the same as 
    * @param args the command line arguments
    */
   public static void main(String args[]) {
      //testMaze();
      Maze myMaze = new Maze(5,5,true);
      myMaze.display();
      Maze bigMaze = new Maze(16,16,false);
      bigMaze.display();
      
   }
   
   public static void testMaze() {
      Maze tempMaze = new Maze(4,5,true);
      tempMaze = new Maze(5,3,true);
      tempMaze = new Maze(4,8,true);
      
   }
   
   
}