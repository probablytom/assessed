import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import org.chocosolver.solver.*;
import org.chocosolver.solver.variables.*;
import org.chocosolver.solver.search.loop.monitors.SearchMonitorFactory;
import org.chocosolver.solver.search.strategy.*;
import org.chocosolver.solver.search.strategy.selectors.variables.DomOverWDeg;
import org.chocosolver.solver.constraints.*;
import org.chocosolver.solver.search.strategy.selectors.*;



public class Optimize {

	Solver solver;
	IntVar[] meetingTimes;
	int nAgents;
	int mMeetings;
	int timeslots;
	int[][] distanceMatrix;
	int[][] attendanceMatrix;
	int[][] constrainedMeetings;
	IntVar meetingLimitUpperBound;

	public Optimize(String filename) throws IOException {

		solver = new Solver("solver");


		try (Scanner sc = new Scanner(new File(filename))) {

			mMeetings = sc.nextInt();
			nAgents = sc.nextInt();
			timeslots = sc.nextInt();

			// Make the variables we'll be using...
			meetingTimes = VF.enumeratedArray("timeslots", mMeetings, 0, timeslots - 1, solver); // Should this be -1?
			attendanceMatrix = new int[nAgents][mMeetings];
			distanceMatrix = new int[mMeetings][mMeetings];
			constrainedMeetings = new int[mMeetings][mMeetings];

			String temp; // For the labels at the beginning of lines

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

						// Create the constraint if we're attending both meetings and there hasn't been a constraint made between these two meetings already.
						if (attendanceMatrix[agentIndex][meeting1] == 1 && 
								attendanceMatrix[agentIndex][meeting2] == 1 &&
								constrainedMeetings[meeting1][meeting2] != 1) {

							constrainedMeetings[meeting1][meeting2] = 1;  // Make sure we don't post this contraint twice
							Constraint travelTime = ICF.distance(meetingTimes[meeting1], meetingTimes[meeting2], ">", distanceMatrix[meeting1][meeting2]);
							solver.post(travelTime);

						}

					}
				}
			}
		}
		//postHeuristic();
		postOptimizationConstraints();
	}


	public void postHeuristic() {
		IntValueSelector valueSelector = IntStrategyFactory.min_value_selector();
		solver.set(new DomOverWDeg(meetingTimes, 0, valueSelector));
	}

	// Find an optimal solution and tell us whether a solution was found. 
		public boolean findSolution() {
			solver.findOptimalSolution(ResolutionPolicy.MINIMIZE, meetingLimitUpperBound);
			return solver.getMeasures().getSolutionCount() != 0;
		}
		
		// Post a constraint so that the times chosen must be lower than some bound. 
		public void postOptimizationConstraints() {
			meetingLimitUpperBound = VF.integer("Upper bound for meeting times", 1, timeslots, solver); // domain shifted by 1 because we're using '<' to constrain
			for (int meetingIndex = 0; meetingIndex < mMeetings; meetingIndex++) {
				solver.post(ICF.arithm(meetingTimes[meetingIndex], "<", meetingLimitUpperBound));
			}
		}

	public void printSolution() {
		for (int i = 0; i < mMeetings; i++) {
			System.out.println(Integer.toString(i) + " " + meetingTimes[i].getValue());
		}

		System.out.println();

		System.out.println("Nodes:\t\t" + solver.getMeasures().getNodeCount());
		System.out.println("Time:\t\t" + solver.getMeasures().getTimeCount());

	}

	public static void main(String[] args) {



		if (args.length == 1) {
			try {
				Optimize solution = new Optimize(args[0]);

				boolean resultFound = solution.findSolution();


				solution.printSolution();
				System.out.println("Solution:\t" + resultFound);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (args.length == 2) {

			try {
				Optimize solution = new Optimize(args[0]);

				SearchMonitorFactory.limitTime(solution.solver, Integer.parseInt(args[1])); // Put a time limit on the solution

				boolean resultFound = solution.findSolution();

				solution.printSolution();
				System.out.println("Solution:\t" + resultFound);

			} catch (Exception e) {
				e.printStackTrace();
			}


		}


	}
}