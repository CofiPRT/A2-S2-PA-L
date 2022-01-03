import java.util.*;

class Coord implements Cloneable {
    int row;
    int col;
    
    public Coord(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    public Coord add(Coord other) {
        return new Coord(row + other.row, col + other.col);
    }
    
    public boolean equalTo(Coord other) {
        return row == other.row && col == other.col;
    }
    
    public Object clone() {
        return new Coord(row, col);
    }
    
    public String toString() {
        return row + " " + col;
    }
}

class State {
    Coord currCoord;
    List<Coord> path;
    int score;
    
    public State(Coord currCoord, List<Coord> path, int score) {
        this.currCoord = (Coord) currCoord.clone();
        this.path = (List<Coord>) clonePath(path);
        this.score = score;
    }
    
    public static List<Coord> clonePath(List<Coord> path) {
        List<Coord> newPath = new ArrayList<>();
        
        for (Coord coord : path) {
            newPath.add((Coord) coord.clone());
        }
        
        return newPath;
    }
}

class Move {
    Coord nextCoord;
    int score;
    
    public Move(Coord nextCoord, int score) {
        this.nextCoord = nextCoord;
        this.score = score;
    }
    
    public int getScore() {
        return score;
    }
}

public class Solution {
    static int numRows;
    static int numCols;
    
    static List<List<Character>> M;
    static Coord pacman;
    static Coord food;
    
    public static void printPath(List<Coord> path) {
        System.out.println(path.size() - 1);
        
        for (Coord coord : path) {
            System.out.println(coord.toString());
        }
    }
    
    public static boolean inBounds(Coord coord) {
        int row = coord.row;
        int col = coord.col;
        
        return row >= 0 && row < numRows && col >= 0 && col < numCols && M.get(row).get(col) != '%';
    }
    
    public static char charAt(Coord coord) {
        if (!inBounds(coord)) return '%';
        
        int row = coord.row;
        int col = coord.col;
        return M.get(row).get(col);
    }
    
    public static int manhattanDist(Coord coord1, Coord coord2) {
        return Math.abs(coord1.row - coord2.row) + Math.abs(coord1.col - coord2.col);
    }

    public static List<Coord> astar() {
        List<Coord> path = new ArrayList<>();

        // TODO: Implement A*.
        Queue<State> queue = new LinkedList<>();
        
        // pentru generarea mutarilor
        List<Coord> dirs = new ArrayList<>();
        dirs.add(new Coord(-1, 0)); // UP
        dirs.add(new Coord(0, -1)); // LEFT
        dirs.add(new Coord(0, 1)); // RIGHT
        dirs.add(new Coord(1, 0)); // DOWN
        
        State initialState = new State(pacman, path, 0);
        queue.add(initialState);
        
        while (!queue.isEmpty()) {
            State currState = queue.poll();
            
            Coord currCoord = currState.currCoord;
            List<Coord> currPath = currState.path;
            int currScore = currState.score;
            
            currPath.add(currCoord); // avansam la aceasta pozitie
            
            if (currCoord.equalTo(food)) {
                // am ajuns la destinatie
                return currPath;
            }
            
            List<Move> moves = new ArrayList<>(); // generam mutarile posibile
            
            for (Coord dir : dirs) {
                Coord nextCoord = currCoord.add(dir); // testam aceasta coordonata
                
                if (!inBounds(nextCoord)) {
                    // mutarea nu este valida
                    continue;
                }
                
                char target = charAt(nextCoord);
                if (target != '=') {
                    // '=' marcheaza o coordonata vizitata
                    M.get(nextCoord.row).set(nextCoord.col, '='); // marcam ca vizitata
                    
                    moves.add(new Move(nextCoord, currScore + manhattanDist(nextCoord, food)));
                }
            }
            
            // sortam mutarile dupa scor
            Collections.sort(moves, Comparator.comparing(Move::getScore));
            
            // adaugam starile in ordinea scorurilor mutarilor
            for (Move move : moves) {
                queue.add(new State(move.nextCoord, currPath, move.score));
            }
        }

        return path;
    }

    public static void main(String[] args) {
        M = new ArrayList<>();
        List<Coord> path;

        Scanner s = new Scanner(System.in);
        
        pacman = new Coord(s.nextInt(), s.nextInt());
        food = new Coord(s.nextInt(), s.nextInt());
        numRows = s.nextInt();
        numCols = s.nextInt();

        M = new ArrayList<>();

        for (int i = 0; i < numRows; ++i) {
            List<Character> currentRow = new ArrayList<>();
            
            String nextRow = s.next();
            
            for (int j = 0; j < numCols; ++j) {
                currentRow.add(nextRow.charAt(j));
            }
            
            M.add(currentRow);
        }

        s.close();

        path = astar();
        
        printPath(path);
    }
}