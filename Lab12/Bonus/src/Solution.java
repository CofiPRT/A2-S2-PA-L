import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

class State implements Comparable<State> {
	// acestea doua reprezinta numerele de pe malul ESTIC
	int missionaries;
	int cannibals;
	// locatia barcii
	boolean boatIsEast;
	
	State parent;
	
	// valorile functiilor aferente din algoritm (pentru euristica)
	int g;
	int h; 
	
	State(int missionaries, int cannibals, boolean boatIsRight) {
		this.missionaries = missionaries;
		this.cannibals = cannibals;
		this.boatIsEast = boatIsRight;
		
		// h se poate calcula aici, in functie de euristica
		h = calcH();
		g = 0; // va fi atribuit in cod
	}
	
	// euristica
	private int calcH() {
		return	missionaries * missionaries +
				cannibals * cannibals +
				2 * missionaries * cannibals;
	}
	
	int getF() {
		return g + h;
	}
	
	List<State> getMoves() {
		List<State> moves = new ArrayList<>();
		
		for (int capacity = 1; capacity <= Solution.boatCap; capacity++) {
			// incercam toate combinatiile de 'capacity' locuri
			
			// am prefera sa caram cat mai multi misionari in limitele
			// regulilor, deci incepem cu aceasta varianta
			int carryMis = capacity;
			int carryCan = 0;
			
			while (carryMis >= 0) {
				if (boatIsEast) {
					// imbarcam pasageri de pe malul drept (estic)
					int newMis = missionaries + carryMis;
					int newCan = cannibals + carryCan;
					
					if (isValid(newMis, newCan)) {
						// putem adauga aceasta actiune
						
						// acum barca e pe malul vestic
						moves.add(new State(newMis, newCan, false));
					}
				} else {
					// debarcam pasageri pe malul stang (vestic)
					int newMis = missionaries - carryMis;
					int newCan = cannibals - carryCan;
					
					if (isValid(newMis, newCan)) {
						// putem adauga aceasta actiune
						
						// acum barca este pe malul estic
						moves.add(new State(newMis, newCan, true));
					}
				}
				
				// incercam cu mai putini misionari si mai multi canibali
				// (population control lol)
				carryMis--;
				carryCan++;
			}
		}
		
		return moves;
	}
	
	// trebuie sa respectam regulile puzzle-ului
	boolean isValid(int newMis, int newCan) {
		int diffMis = Solution.maxMis - newMis;
		int diffCan = Solution.maxCan - newCan;
		
		if (boatIsEast) {
			return 	newMis <= Solution.maxMis &&
					newCan <= Solution.maxCan &&
					(newMis == 0 || newMis >= newCan) &&
					(diffMis == 0 || diffMis >= diffCan);
		} else {
			return 	newMis >= 0 &&
					newCan >= 0 &&
					(newMis == 0 || newMis >= newCan) &&
					(diffMis == 0 || diffMis >= diffCan);
		}
	}
	
	boolean ended() {
		return 	missionaries == 0 &&
				cannibals == 0 &&
				boatIsEast;
	}
	
	public String toString() {		
		return 	"EAST: " +
				"M = " + missionaries + ", " +
				"C = " + cannibals + " | " +
				"WEST: " +
				"M = " + (Solution.maxMis - missionaries) + ", " +
				"M = " + (Solution.maxCan - cannibals) + " | " +
				"BOAT: " + (boatIsEast ? "WEST" : "EAST");
	}
	
	public String actionString(State prev) {
		return 	"BOAT GOING: " + (boatIsEast ? "WEST" : "EAST") + " | " +
				"CARRYING: " +
				"M = " + Math.abs(missionaries - prev.missionaries) + ", " +
				"C = " + Math.abs(cannibals - prev.cannibals);
				
	}

	@Override
	public int compareTo(State other) {
		if (this.getF() > other.getF()) {
			return 1;
		} else if (this.getF() < other.getF()) {
			return -1;
		}
		
		return 0;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof State)) return false;
		
		State other = (State) o;
		
		return	this.missionaries == other.missionaries &&
				this.cannibals == other.cannibals &&
				this.boatIsEast == other.boatIsEast;
	}
}

public class Solution {
	Queue<State> open;
	
	// nu vrem sa trecem prin aceeasi stare de doua ori
	// vom redefini equals() pentru State
	HashSet<State> closed;
	Queue<State> nodes;
	
	static final int maxMis = 3;	// nr de misionari in puzzle
	static final int maxCan = 3;	// nr de canibali in puzzle
	static final int boatCap = 2;	// capacitatea barcii
	
	State endState = null;
	int steps;
	
	Solution() {
		nodes = new LinkedList<>();
	}
	
	void astar() {
		steps = 0;
		
		open = new PriorityQueue<>();
		closed = new HashSet<>();
		
		// prima imbarcare se face cand barca este pe malul estic
		// aceasta stare are barca pe malul vestic
		State initialState = new State(maxMis, maxCan, false);
		initialState.g = 0; // bineinteles, in starea de start
		
		open.add(initialState);
		
		State currState = null;
		
		while (!open.isEmpty()) {
			steps++; // o noua actiune

			currState = open.poll();
						
			closed.add(currState); // prelucram aceasta stare
			
			List<State> moves = currState.getMoves();
			
			for (State move : moves) {
				boolean exists = false; // daca o stare exista deja
				
				for (State closedState : closed) {
					if (closedState.equals(move)) {
						// am trecut deja prin aceasta stare
						exists = true;
						
						if (closedState.g > currState.g + 1) {
							// se verifica daca starea curenta produce o
							// cale mai scurta de la start
							closedState.g = currState.g + 1;
							closedState.parent = currState;
							
							reevaluateParents(closedState);
						}
						
						break;
					}
				}
				
				if (exists) {
					// propagam aceasta conditie
					continue;
				}
				
				// procedam la fel si pentru open
				exists = false;
				
				for (State openedState : open) {
					if (openedState.equals(move)) {
						// am trecut deja prin aceasta stare
						exists = true;
						
						if (openedState.g > currState.g + 1) {
							// se verifica daca starea curenta produce o
							// cale mai scurta de la start
							openedState.g = currState.g + 1;
							openedState.parent = currState;
						}
						
						break;
					}
				}
				
				if (exists) {
					// propagam aceasta conditie
					continue;
				}
				
				// actualizam distanta pentru aceasta cale (stare)
				move.g = currState.g + 1;
				move.parent = currState;
				open.add(move);
			}
			
			if (currState.ended()) {
				// s-a gasit solutia
				endState = currState;
				break;
			}
		}
	}
	
	public void reevaluateParents(State currState) {
		List<State> moves = currState.getMoves();
		
		for (State move : moves) {
			for (State closedState : closed) {
				if (closedState.equals(move)) {
					if (closedState.g > currState.g + 1) {
						// se verifica daca starea curenta produce o
						// cale mai scurta de la start
						closedState.g = currState.g + 1;
						closedState.parent = currState;
						
						reevaluateParents(closedState);
					}
					
					break;
				}
			}
		}
	}
	
	public void printEndState() {
		System.out.println("Total steps: " + steps);
		System.out.println("Solution: ");
		
		Stack<State> stack = new Stack<>();
		
		State currState = endState;
		
		while (currState != null) {
			stack.push(currState);
			
			currState = currState.parent;
		}
		
		State prevState = null;
		
		while (!stack.isEmpty()) {
			currState = stack.pop();
			
			if (prevState != null) {
				System.out.println("        " + currState.actionString(prevState));
			}
			
			System.out.println("    " + currState.toString());
			System.out.println();
			
			prevState = currState;
		}
	}

	public static void main(String[] args) {
		Solution sol = new Solution();
		sol.astar();
		sol.printEndState();
	}

}
