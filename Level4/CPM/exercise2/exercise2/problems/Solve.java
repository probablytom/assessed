import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.chocosolver.solver.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.search.strategy.*;
import org.chocosolver.solver.constraints.*;



public class Solve {
	
	Solver solver;
	IntVar[] timeslot;
	int nAgents;
	int mMeetings;
	int timeslots;
	
	Solve(String filename) throws IOException {

		solver = new Solver("solver");
		
		
		try (Scanner sc = new Scanner(new File(filename))) {
			
			mMeetings = sc.nextInt();
			nAgents = sc.nextInt();
			timeslots = sc.nextInt();
			
			// Make the choco variables we'll be using
			timeslot = VF.enumeratedArray("timeslots", timeslots, 0, mMeetings, solver);
			
			
			
		}
		
	}

	public static void main(String[] args) {
		
		
		if (args.length == 1) {
			// No arguments, nothing to process
		} else if (args.length == 2) {
			// We're passed a file to process, but no time to return in.
		} else {
			// Some edge case we should take care of
		}

	}

}
