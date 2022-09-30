import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import javax.swing.JOptionPane;


public class Main {

    final static int rowL = 5;//number of rows for chess board
    final static int colL = 5;//number of cols for chess board
    static Stack<Location> stack = new Stack<Location>(); //store moves in order (backtrack capability)

    //list of exhausted locations for each location.  Must use method convertLocToIndex to find a Locations proper index
    static ArrayList<ArrayList<Location>> exhausted = new ArrayList<>(rowL * colL);
    static int board[][] = new int[rowL][colL];//2d array used to store the order of moves
    static boolean visited[][] = new boolean[rowL][colL];//2d array used to store what locations have been used
    static Location startLoc;

    public static void main(String[] args) {

        System.out.println("START");
        initExhausted();
        ArrayList<Location> currentPossible;
        obtainStartLoc();
        /*
        System.out.println("Start Loc is " + startLoc);
        visited[startLoc.getRow()][startLoc.getCol()] = true;
        printBoard();
        addToExhausted(new Location(1,1), new Location(1,2));
        System.out.println("cleared 1, 1");
        clearExhausted(new Location(1,1));
        printExhausedList(new Location(1,1));
        printPossibleMoveLocations(new Location(1,2));
        System.out.println(getNextMove(new Location(1,1),getPossibleMoves(new Location(1,1))));
        printVisited();
        printPossibleMoveLocations(new Location(4,4));

         */

        while(stack.size() != rowL * colL && stack.size() != 0){
            Location top =stack.peek();
            Location temp =getNextMove(top,getPossibleMoves(top));
            if(temp!=null){
                exhausted.get(convertLocToIndex(top)).add(temp);
                stack.push(temp);
                board[temp.getRow()][temp.getCol()]=stack.size();
                printBoard();
                printPossibleMoveLocations(top);
            }else{
                board[top.getRow()][top.getCol()]=0;
                clearExhausted(stack.pop());
            }
        }
        if(stack.size()==0){
            System.out.println("No solution");
        }else{
            printBoard();
        }


    }

    /*
     * Printed out the exhausted list for a given Location
     */
    public static void printExhausedList(Location loc) {
        for(int i=0; i<exhausted.get(convertLocToIndex(loc)).size();i++){
            System.out.print(exhausted.get(convertLocToIndex(loc)).get(i));
        }
        System.out.println();
    }

    /*
     * Prints out the possible move locations for a given Location
     */
    public static void printPossibleMoveLocations(Location loc) {
        ArrayList list = getPossibleMoves(loc);
        for(int i=0; i<list.size();i++){
            System.out.print(list.get(i)+" ");
        }
        System.out.println();
    }

    /*
     * prints out the board (numbers correspond to moves)
     */
    public static void printBoard() {
        for(int i=0; i<colL; i++){
            for(int j=0; j<rowL; j++){
                System.out.print(board[i][j]+" ");
            }
            System.out.println();
        }
        System.out.println();
    }

    /*
     * prints out true/false for what spaces have been visited
     */
    public static void printVisited(){
        for (int i=0;i<stack.size();i++){
            System.out.println(stack.get(i)+" ");
        }
    }

    /*
     * clear out the exhausted list for a given Location
     * This needs to be done everytime a Location is removed from the Stack
     */
    public static void clearExhausted(Location loc) {
        for(int i=0; i<exhausted.get(convertLocToIndex(loc)).size();i++){
            exhausted.get(convertLocToIndex(loc)).remove(0);
        }
    }

    /*
     * set up the exhausted list with empty exhuasted lists.
     */
    public static void initExhausted() {
        for(int i=0;i<colL*rowL;i++)
            exhausted.add(new ArrayList<>());
    }
    /*
     * is this dest Location exhausted from the source Location
     */
    public static boolean inExhausted(Location source, Location dest) {
        boolean isExhausted = false;
        for (int i =0; i<exhausted.get(convertLocToIndex(source)).size()&&!isExhausted;i++){
            if(exhausted.get(convertLocToIndex(source)).get(i).getCol()==dest.getCol()&&exhausted.get(convertLocToIndex(source)).get(i).getRow()==dest.getRow()){
                isExhausted =true;
            }
        }
        return isExhausted;
    }
    /*
     * returns the next valid move for a given Location on a given ArrayList of possible moves
     */
    public static Location getNextMove(Location loc, ArrayList<Location> list) {
        Location sending=null;
        int inter=0;
        for(int i=0; i<list.size();i++){
            boolean send=true;
            boolean blue;
            if(!inExhausted(loc, list.get(i))){
                for (int j=0; j<stack.size();j++) {
                    Location listener=list.get(i);
                    send = !(stack.get(j).getCol() == list.get(i).getCol() && (stack.get(j)).getRow() == list.get(i).getRow());
                    blue = (board[(list.get(i).getRow())][(list.get(i).getCol())] == 0);
                    if (send && blue) {
                        inter = i;
                        break;
                    }
                }
                if(send){
                    sending=list.get(inter);
                    break;
                }
            }
        }
        return sending;
    }

    /*
     * converts a (row,col) to an array index for use in the exhausted list
     */
    public static int convertLocToIndex(Location loc) {
        return (loc.getRow()*rowL) + loc.getCol();
    }

    /*
     * adds a dest Location in the exhausted list for the source Location
     */
    public static void addToExhausted(Location source, Location dest) {
        exhausted.get(convertLocToIndex(source)).add(dest);
    }

    /*
     * is this Location a valid one
     */
    public static boolean isValid(Location loc) {
        return loc.getCol() < colL && loc.getRow() < rowL && loc.getRow() >= 0 && loc.getCol() >= 0;
    }

    /*
     * returns another Location for the knight to move in.  If no more possible move
     * locations exist from Location loc, then return null
     */
    public static ArrayList<Location> getPossibleMoves(Location loc) {
        ArrayList<Location> list = new ArrayList<>();
        list.add(new Location(loc.getRow()+1,loc.getCol()+2));
        list.add(new Location(loc.getRow()-1,loc.getCol()+2));
        list.add(new Location(loc.getRow()+1,loc.getCol()-2));
        list.add(new Location(loc.getRow()-1,loc.getCol()-2));
        list.add(new Location(loc.getRow()+2,loc.getCol()+1));
        list.add(new Location(loc.getRow()+2,loc.getCol()-1));
        list.add(new Location(loc.getRow()-2,loc.getCol()+1));
        list.add(new Location(loc.getRow()-2,loc.getCol()-1));
        for(int i=7; i>=0;i--){
            if(!isValid(list.get(i))){
                list.remove(i);
            }
        }
        return list;
    }


    /*
     * prompt for input and read in the start Location
     */
    public static void obtainStartLoc() {
        startLoc=new Location(0,0);
        board[startLoc.getCol()][startLoc.getRow()]=1;
        stack.push(startLoc);
    }

}