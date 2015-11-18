import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.chocosolver.solver.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.search.strategy.*;
import org.chocosolver.solver.constraints.*;



public class Solve {

	Solver solver;
	IntVar[] meetingTimes;
	int nAgents;
	int mMeetings;
	int timeslots;
	int[][] distanceMatrix;
	int[][] attendanceMatrix;

	public Solve(String filename) throws IOException {

		solver = new Solver("solver");


		try (Scanner sc = new Scanner(new File(filename))) {

			mMeetings = sc.nextInt();
			nAgents = sc.nextInt();
			timeslots = sc.nextInt();

			// Make the variables we'll be using...
			meetingTimes = VF.enumeratedArray("timeslots", mMeetings, 0, mMeetings - 1, solver); // Should this be -1?
			attendanceMatrix = new int[nAgents][mMeetings];
			distanceMatrix = new int[mMeetings][mMeetings];

			String temp;

			// Let's read in the data and populate our matrices. 
			for (int agentIndex = 0; agentIndex < nAgents; agentIndex++) {

				// Skip the label at the beginning of the line
				temp = sc.next();

				for (int meetingIndex = 0; meetingIndex < mMeetings; meetingIndex++) {
					attendanceMatrix[agentIndex][meetingIndex] = sc.nextInt();
				}
			}

			for (int meeting1 = 0; meeting1 < mMeetings; meeting1++){

				// Skip the label at the beginning of the line
				temp = sc.next();

				for (int meeting2 = 0; meeting2 < mMeetings; meeting2++) {
					distanceMatrix[meeting1][meeting2] = sc.nextInt();
				}
			}

			// We've read in all of the data, so now, we can make our Choco constraints. 
			// If we find an agent who's going to two meetings, those two meetings must have timeslots of their distance + 1 or greater.

			for (int agentIndex = 0; agentIndex < nAgents; agentIndex++){
				for (int meeting1 = 0; meeting1 < mMeetings-1; meeting1++){
					for (int meeting2 = meeting1 + 1; meeting2 < mMeetings; meeting2++){

						// Create the constraint if we're attending both meetings
						if (attendanceMatrix[agentIndex][meeting1] == 1 && 
								attendanceMatrix[agentIndex][meeting2] == 1) {
							solver.post(ICF.distance(meetingTimes[meeting1], meetingTimes[meeting2], ">", distanceMatrix[meeting1][meeting2]));

						}

					}
				}
			}

		}

	}

	public boolean findSolution() {
		return solver.findSolution();
	}

	public void printSolution() {
		for (int i = 0; i < mMeetings; i++) {
			System.out.println(Integer.toString(i) + " " + meetingTimes[i].getValue());
		}
	}

	public static void main(String[] args) {

		/*for (String arg : args) {
			System.out.println(arg);
		}*/


		if (args.length == 1) {
			try {
				Solve solution = new Solve(args[0]);
				boolean resultFound = solution.findSolution();
				if (resultFound) {

					solution.printSolution();

				}
				// process output of solver
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (args.length == 2) {
			// The same, but with a timer
			// TODO: Implement timer


			try {
				Solve solution = new Solve(args[0]);
				boolean resultFound = solution.findSolution();
				if (resultFound) {

					solution.printSolution();

				}
				// process output of solver
			} catch (Exception e) {
				e.printStackTrace();
			}


		}

	}
}