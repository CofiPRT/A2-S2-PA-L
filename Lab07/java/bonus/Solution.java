import java.io.*;
import java.util.*;
import java.text.*;
import java.math.*;
import java.util.regex.*;

class Coord {
    public int row;
    public int col;
    
    public Coord parent;
    
    public Coord(int row, int col, Coord parent) {
        this.row = row;
        this.col = col;
        this.parent = parent;
    }
}

public class Solution {
    public static int n;
    public static int[][] matrix;
    
    public static Coord start;
    
    public static boolean inBounds(Coord coord) {
        if (coord.row < 1 || coord.row > n || coord.col < 1 || coord.col > n ||
           matrix[coord.row][coord.col] == 1) {
            return false;
        }
        
        return true;
    }

    public static void main(String[] args) {
        /* Enter your code here. Read input from STDIN. Print output to STDOUT. Your class should be named Solution. */
        Scanner input = new Scanner(System.in);
        
        n = input.nextInt();
        
        // clutter
        input.nextLine();
        
        matrix = new int[n + 1][n + 1];
        
        for (int i = 1; i <= n; i++) {
            String line = input.nextLine();
            
            // apparently the tests have different input formats...
            if (line.length() <= n) {
                // this one is like this "1012"
                for (int j = 1; j <= n; j++) {
                    matrix[i][j] = Integer.parseInt(String.valueOf(line.charAt(j - 1)));
                }
            } else {
                // this one is like this "1 0 1 2"
                Scanner lineInput = new Scanner(line);
                for (int j = 1; j <= n; j++) {
                    matrix[i][j] = lineInput.nextInt();
                }
            }
            
            
        }
        
        start = new Coord(input.nextInt(), input.nextInt(), null);
        
        input.close();
        
        // BFS
        Queue<Coord> queue = new LinkedList<>();
        queue.add(start); // start... at the start
        
        Coord currCoord = null; // we want it outside the loop for future use
        boolean solFound = false;
        
        while (!queue.isEmpty()) {
            int originalSize = queue.size();
                        
            // empty the queue for this step
            for (int i = 0; i < originalSize; i++) {
                currCoord = queue.poll();
                
                if (!inBounds(currCoord)) {
                    // we don't care about it
                    continue;
                }
                
                if (matrix[currCoord.row][currCoord.col] == 2) {
                    // solution found
                    solFound = true;
                    break;
                }
                
                // Gigel can move in 4 directions
                queue.add(new Coord(currCoord.row - 1, currCoord.col, currCoord)); // UP
                queue.add(new Coord(currCoord.row, currCoord.col + 1, currCoord)); // RIGHT
                queue.add(new Coord(currCoord.row + 1, currCoord.col, currCoord)); // DOWN
                queue.add(new Coord(currCoord.row, currCoord.col - 1, currCoord)); // LEFT
                
                // notice how we keep track of where we came from with the 'parent' field in Coord
                
                // convert this cell into a blocked one (so we don't visit it again)
                matrix[currCoord.row][currCoord.col] = 1;
            }
            
            if (solFound) break;
        }
        
        if (solFound) {
            // move through parents back to the start
            Stack<Coord> path = new Stack<>();

            while (currCoord != start) {
                path.push(currCoord);

                currCoord = currCoord.parent;
            }

            int pathSize = path.size();

            System.out.println(pathSize);

            for (int i = 0; i < pathSize; i++) {
                currCoord = path.pop();

                System.out.println(currCoord.row + " " + currCoord.col);
            }
        } else {
            System.out.println(-1);
        }
    }
}