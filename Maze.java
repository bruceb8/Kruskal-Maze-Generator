/**
 * Maze.java
 *
 * TCSS342
 * Assignment 5
 */
import java.util.*;
/**
 * This is the maze generator
 * @author Bruce Baker
 * @version May 27, 2018
 */
public class Maze {
   /**
    * Boolean for debugging. adds printing for each step
    */
   boolean myDebug;
   /**
    *the array of nodes
    */
   MazeNode[][] myGraph;
   /**
    * the width of the maze
    */ 
   int myWidth;
   /**
    * the depth of the maze
    */
   int myDepth;
   /**
    * The amount of edges required for the maze to be full
    */
   int myEdgeGoal;
   /**
    * a random object we instantiate for the sake of it.
    */
   Random myRand = new Random();
   //IMPORTANT KRISPY KREAM WORKS.  THE FAIL STATE ON CONNECTION
   //IS FINDING A NODE WE VISITED AND TRYING TO CONNECT IT TO ANOTHER
   //NODE WE ALREADY VISITED. HAVE A BOOLEAN TO TRIGGER IF WE ARE LINKED TO THE ENTRANCE
   // AND CHECK THIS ON COMBINEING
   //Spanningtree has N - 1 edges in it where n is the amount of nodes
   /**
    *
    * @param width the width of the maze
    * @param depth the depth of the maze
    * @param debug the debugging boolean, just enable rints during runtime
    */
   Maze(int width, int depth, boolean debug) {
      myDebug = debug;
      myGraph = new MazeNode[width][depth];
      myWidth = depth;
      myDepth = width;
      myEdgeGoal = width * depth - 1;
      for( int i = 0; i < width; i++) {
         for(int j = 0; j < depth; j++) {
            myGraph[i][j] = new MazeNode();
         }
      }
      //System.out.println(toString());
      Kruskal();
      FindPath();
      
   }
   /** 
   * prints the maze to the console
   */
   void display() {
      System.out.println(toString());
   }
   /**
    * The tostring function that everything should have
    * @return the maze visiualized in text
    */
   public String toString() {
      StringBuilder tSt = new StringBuilder();
      tSt.append('X');
      tSt.append(' ');
      tSt.append(' ');
      
      for(int i = 1; i < myWidth; i++) {
         tSt.append(' ');
         tSt.append('X');
         tSt.append(' ');
         tSt.append('X');
      }
      tSt.append(' ');
      tSt.append('X');
      tSt.append('\n');
      
      for(int i = 0; i < myDepth; i++) {
        tSt.append('X');
        for(int j = 0; j < myWidth; j++) {
           MazeNode tNode = myGraph[i][j];
           tSt.append(' ');
           if(tNode.Enter == true) {
              tSt.append('V');
           } else {
              tSt.append(' ');
           }
           tSt.append(' ');
           if(tNode.checkEast()) {
              tSt.append('X');
           } else {
              tSt.append(' ');
           }
        }
        tSt.append('\n'); 
        tSt.append('X');
        for(int k = 0; k < myWidth; k++) {
            MazeNode tNode = myGraph[i][k];
            tSt.append(' ');
            if(tNode.checkSouth()) {
              tSt.append('X');
            } else {
              tSt.append(' ');
            }
            tSt.append(" X");
        }
        
        tSt.append('\n'); 
      
      }
      tSt.replace(tSt.length() - 4, tSt.length() - 1, "  X");
      /*
      for(int i = 1; i < myWidth; i++) {
         tSt.append('X');
         tSt.append(' ');
      }
      tSt.append(' ');
      tSt.append('\n');
      */
      return tSt.toString();
   }
   /**
    * Assigns a flag to all the nodes that are part of the 
    * maze path solution.
    */
   void FindPath() {
      //now we need to traverse the node. we can do some baby recursion or something
      myGraph[0][0].Visited = true;
      myGraph[myDepth - 1][myWidth - 1].Enter = true;
      ArrayList<MazeNode> myPath = new ArrayList<MazeNode>();
      myPath.add(myGraph[0][0]);
      //we flag visit whenever we visit a node so we can backtrack it.
      //we never visit a node that we already visted unless we are backtracking
      boolean stillLooking = true;
      while(stillLooking) {
            MazeNode tNode = myPath.get(myPath.size() - 1);
            if(tNode.Enter == true) {
               stillLooking = false;
               break;
            }
            if(tNode.checkEast() == false && tNode.EastNode.Visited == false){
               myPath.add(tNode.EastNode);
               tNode.EastNode.Visited = true;
            } else if(tNode.checkSouth() == false && tNode.SouthNode.Visited == false) {
               myPath.add(tNode.SouthNode);
               tNode.SouthNode.Visited = true;
            } else if(tNode.checkWest() == false && tNode.WestNode.Visited == false) {
               myPath.add(tNode.WestNode);
               tNode.WestNode.Visited = true;
            } else if(tNode.checkNorth() == false && tNode.NorthNode.Visited == false) {
               myPath.add(tNode.NorthNode);
               tNode.NorthNode.Visited = true;
            } else {
               myPath.remove(tNode);
            }
         }
         
         for(int i = 0; i < myPath.size(); i++) {
            MazeNode tNode = myPath.get(i);
            tNode.Enter = true;
         }
      }
   
   /**
    * Doing Kruskal was a mistake, the algoritm has so many
    * if statements to check the random directions to make sure they are legal
    * moves.  
    */
   public void Kruskal() {
      int madeEdges = 0;
      int tD;
      int tW;
      if(myDebug) {
            display();
         }
      while(madeEdges < myEdgeGoal) {
         
         //first we get two random numbers to represent our position
         
         tD = myRand.nextInt(myDepth);
         tW = myRand.nextInt(myWidth);
         MazeNode tNode = myGraph[tD][tW];
         //this huge logic block essentially determines if the node 
         //if fully connected or not depending on its position in the graph
         boolean canLink = tNode.openEdge();
         boolean NorthOK = true;
         boolean SouthOK = true;
         boolean EastOK = true;
         boolean WestOK = true;
         if(tD == 0 && tW == 0) {
            canLink = tNode.checkEast() || tNode.checkSouth();
            WestOK = false;
            NorthOK = false;
         }else if( tD == 0 && tW == myWidth - 1) {
            canLink = tNode.checkWest() || tNode.checkSouth();
            EastOK = false;
            NorthOK = false;
         } else if (tD == myDepth - 1 && tW == 0) {
            canLink = tNode.checkNorth() || tNode.checkEast();
            SouthOK = false;
            WestOK = false;
         } else if (tD == myDepth - 1 && tW == myWidth - 1) {
            canLink = tNode.checkNorth() || tNode.checkWest();
            SouthOK = false;
            EastOK = false;
         } else if(tD == 0) {
            canLink = (tNode.checkEast() || tNode.checkSouth() || tNode.checkWest());
            NorthOK = false;
         } else if(tD == myDepth - 1) {
            canLink = (tNode.checkEast() || tNode.checkNorth() || tNode.checkWest());
            SouthOK = false;
         } else if(tW == 0) {
            canLink = (tNode.checkNorth() || tNode.checkEast() || tNode.checkSouth());
            WestOK = false;
         } else if(tW == myWidth - 1) {
            canLink = (tNode.checkNorth() || tNode.checkWest() || tNode.checkSouth());
            EastOK = false;
         }
         
         //this is the part where we join nodes acccording to our sheet rules
         //set up a series of flags that give us the go ahead on what 
         //directions we can take
         if(canLink == true) {
            
            ArrayList<Integer> directions = randArray(4);
            /*
            System.out.println(directions);
            */
            if(NorthOK == false) {
               directions.remove(Integer.valueOf(0));
            }
            if(EastOK == false) {
               directions.remove(Integer.valueOf(1));
            }
            if(SouthOK == false) {
               directions.remove(Integer.valueOf(2));
            }
            if(WestOK == false) {
               directions.remove(Integer.valueOf(3));
            }
            /*
            System.out.println(directions);
            System.out.println("Width = " + tW);
            System.out.println("Depth = " + tD);
            */
            if(tNode.NodeID == -1) {
               tNode.NodeID = madeEdges;
               for(int i = 0; i < directions.size(); i++) {
                  int dir = directions.get(i);
                  if(dir == 0 && tNode.NorthNode == null) {
                     madeEdges++;
                     tNode.NorthNode = myGraph[tD - 1][tW];
                     tNode.NorthNode.SouthNode = myGraph[tD][tW];
                     tNode.SetID(tNode.NodeID);
                     //tNode.NorthNode.NodeID = tNode.NodeID;
                     break;
                  } 
                  if(dir == 1 && tNode.EastNode == null) {
                     madeEdges++;
                     tNode.EastNode = myGraph[tD][tW + 1];
                     tNode.EastNode.WestNode = myGraph[tD][tW];
                     tNode.SetID(tNode.NodeID);
                     break;
                  }
                  if(dir == 2 && tNode.SouthNode == null) {
                     madeEdges++;
                     tNode.SouthNode = myGraph[tD + 1][tW];
                     tNode.SouthNode.NorthNode = myGraph[tD][tW];
                     tNode.SetID(tNode.NodeID);
                     break;
                  }
                  if(dir == 3 && tNode.WestNode == null) {
                     madeEdges++;
                     tNode.WestNode = myGraph[tD][tW - 1];
                     tNode.WestNode.EastNode = myGraph[tD][tW];
                     tNode.SetID(tNode.NodeID);
                     break;
                  }  
               }
               
            } else {
               //this is the case where we visit a node that already has an ID
               //System.out.println(" My ID is: " + tNode.NodeID);
               for(int i = 0; i < directions.size(); i++) {
                  int dir = directions.get(i);
                  if(dir == 0 && (tNode.NorthNode == null && myGraph[tD - 1][tW].NodeID != tNode.NodeID)) {
                     madeEdges++;
                     tNode.NorthNode = myGraph[tD - 1][tW];
                     tNode.NorthNode.SouthNode = tNode;
                     tNode.NorthNode.SetID(tNode.NodeID);
                     //tNode.NorthNode.NodeID = tNode.NodeID;
                     break;
                  } 
                  if(dir == 1 && (tNode.EastNode == null && myGraph[tD][tW + 1].NodeID != tNode.NodeID)) {
                     madeEdges++;
                     tNode.EastNode = myGraph[tD][tW + 1];
                     tNode.EastNode.WestNode = tNode;
                     tNode.EastNode.SetID(tNode.NodeID);
                     break;
                  }
                  if(dir == 2 && (tNode.SouthNode == null && myGraph[tD + 1][tW].NodeID != tNode.NodeID)) {
                     madeEdges++;
                     tNode.SouthNode = myGraph[tD + 1][tW];
                     tNode.SouthNode.NorthNode = tNode;
                     tNode.SouthNode.SetID(tNode.NodeID);
                     break;
                  }
                  if(dir == 3 && (tNode.WestNode == null && myGraph[tD][tW - 1].NodeID != tNode.NodeID)) {
                     madeEdges++;
                     tNode.WestNode = myGraph[tD][tW - 1];
                     tNode.WestNode.EastNode = tNode;
                     tNode.WestNode.SetID(tNode.NodeID);
                     break;
                  }  
               }
            }
          if(myDebug) {
            display();
         }  
         }
            
      }
      
   }
   //this function returns 4 directions in a random order
   ////////////COMPASS ROSE/////////////////
   //             0N
   //           3W  1E
   //             2S 
   /**
    * The random array of subsequent integers
    * @param range the range of values to be contained within the array
    * @return the shuffled array of integers
    */
   ArrayList<Integer> randArray(int range) {
      ArrayList<Integer> direc = new ArrayList<Integer>();
      
      for(int i = 0; i < range; i++) {
      direc.add(i);
      }
      Collections.shuffle(direc);
      
      return direc;
   }
   /* this node structure might nor work lets think about something else
   Marriot might have been on to something*/
   /*marriot did this with strings, so we can logic hard core here
   even numbers on the position are 'nodes' odd numbers are edges. 
   any values on the edge is with position zero or position of arraysize -1. I could do array of char.
   I could use the nodes and just have them be connected?  I could have the 
   entire map by default "filled", but as we make links, we remove walls.
   
   we need to pick out an entrance and a exit.  a random node from the top
   and a node from the bottom.
   
   I should also make an edge class?
   nope each node will be come packaged with this shit
   eventually all the nodes combine into just one
   and we print it like we print a tree
   it will print recursivly most likely
   we will actually create everything linked together initially
   then break links between nodes
   */
   /**
    * Private MazeNode class.  does everything i need it too.
    */
   private class MazeNode {
      /**
       * The 4 fields here are just for the nodes in the cardinal directions
       */
      MazeNode NorthNode;
      /**
       * The 4 fields here are just for the nodes in the cardinal directions
       */
      MazeNode EastNode;
      /**
       * The 4 fields here are just for the nodes in the cardinal directions
       */
      MazeNode SouthNode;
      /**
       * The 4 fields here are just for the nodes in the cardinal directions
       */
      MazeNode WestNode;
      /**
       * unique ID assigned to the node
       */ 
      int NodeID;
      /**
       * If the node was visited during pathfnding or not
       */
      boolean Visited;
      /**
       * If the node is part of the critical path or not
       */
      boolean Enter;
      
      MazeNode () {
         NorthNode = null;
         EastNode = null;
         SouthNode = null;
         WestNode = null;
         Visited = false;
         Enter = false;
         NodeID = -1;
      }
      
      /**
       * IM NOT GOING TO COMMENT THE REST, SO LISTEN UP
       * The check[direction] function return true if the direction does not have a node and false otherwise
       * The openEdge just check is a single direction is alowed
       * the SetID spread the nodes ID to all of its companions like a plague
       * SetEntrance is useless and was made before the plan was realized
       */
      public boolean checkEast() {
         boolean temp = true;
         if(EastNode != null) {
            temp = false;
         }
         return temp;
      }
      public boolean checkSouth() {
         boolean temp = true;
         if(SouthNode != null) {
            temp = false;
         }
         return temp;
      }
      public boolean checkWest() {
         boolean temp = true;
         if(WestNode != null) {
            temp = false;
         }
         return temp;
      }
      public boolean checkNorth() {
         boolean temp = true;
         if(NorthNode != null) {
            temp = false;
         }
         return temp;
      }
      
      
      
      public boolean openEdge() {
         boolean temp = false;
         if(NorthNode == null) {
            temp = true;
         }
         else if(EastNode == null) {
            temp = true;
         }
         else if(SouthNode == null) {
            temp = true;
         }
         else if(WestNode == null) {
            temp = true;
         }
         return temp;
      }
      
      
      
      public void SetID(int ID) {
         NodeID = ID;
         if(NorthNode != null && NorthNode.NodeID != NodeID) {
            NorthNode.SetID(ID);
         }
         if(EastNode != null &&  EastNode.NodeID != NodeID) {
            EastNode.SetID(ID);
         }
         if(SouthNode != null && SouthNode.NodeID != NodeID) {
            SouthNode.SetID(ID);
         }
         if(WestNode != null && WestNode.NodeID != NodeID) {
            WestNode.SetID(ID);
         }
      }
      public void SetEntrance() {
         Enter = true;
         if(NorthNode != null && NorthNode.Enter != true) {
            NorthNode.SetEntrance();
         }
         if(EastNode != null && EastNode.Enter != true) {
            EastNode.SetEntrance();
         }
         if(SouthNode != null && SouthNode.Enter != true) {
            SouthNode.SetEntrance();
         }
         if(WestNode != null && WestNode.Enter != true) {
            WestNode.SetEntrance();
         }
      }
      
   }
      
   
}
