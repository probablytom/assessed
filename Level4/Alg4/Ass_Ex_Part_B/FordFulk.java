import java.util.*;
import java.io.*;
import networkFlow.*;

/**
 * The Class FordFulk.
 * Contains main part of the Ford-Fulkerson implementation and code for file input
 */
public class FordFulk {

	/** The name of the file that encodes the given network. */
	private String filename;
	
	/** The network on which the Ford-Fulkerson algorithm is to be run. */
	private Network net;

	/**
	 * Instantiates a new FordFulk object.
	 * @param s the name of the input file
	 */
	public FordFulk(String s) {
		filename = s; // store name of input file
	}

	/**
	 * Read in network from file.
	 * See assessed exercise specification for the file format.
	 */
	public void readNetworkFromFile() {
		FileReader fr = null;
		// open file with name given by filename
		try {
			try {
				fr = new FileReader (filename);
				Scanner in = new Scanner (fr);
				ArrayList<String[]> lines = new ArrayList<>();
				while (in.hasNextLine()) {
					String line = in.nextLine();
					lines.add(line.split(" "));
				}
				
				

				// get number of vertices
				int numStudents = Integer.parseInt(lines.get(0)[0]);
				int numProjects = Integer.parseInt(lines.get(1)[0]);
				int numLecturers = Integer.parseInt(lines.get(2)[0]);

                lines.remove(0);
                lines.remove(0);
                lines.remove(0);

				// create new network with desired number of vertices
				net = new networkFlow.Network(numStudents, numProjects, numLecturers);

				// For every student, add an edge from the source and another edge for every course they're allowed to take and want to
				for(int student = 0; student < numStudents; student++) {
					Vertex studentVertex = net.getVertexByIndex(student + 1);

					// Make an edge to the student from source
					net.addEdge(net.getSource(), studentVertex, 1);

					// Add an edge to each project the student is interested in
					for (int pos = 2; pos < lines.get(student).length; pos++) {
						int projectNumber = Integer.parseInt(lines.get(student)[pos])-1;
						Vertex projectVertex = net.getVertexByIndex(projectNumber + numStudents + 1);
						// Only add edge if student is permitted to take course
						if (lines.get(student)[1].equals("N") || lines.get(student)[1].equals(lines.get(projectNumber + numStudents)[1])) {
							net.addEdge(studentVertex, projectVertex, 1);
						}
					}
				}

				// For every project, add an edge to its lecturer with appropriate capacity
				for (int project = 0; project < numProjects; project++) {
					Vertex projectVertex = net.getVertexByIndex(project + numStudents + 1);
					Vertex lecturerVertex = net.getVertexByIndex(Integer.parseInt(lines.get(project + numStudents)[2]) + numStudents + numProjects);
					net.addEdge(projectVertex, lecturerVertex, Integer.parseInt(lines.get(project + numStudents)[3]));
				}

				// For every lecturer, add an edge with appropriate capacity to the terminal
				for (int lecturer = 0; lecturer < numLecturers; lecturer++) {
					Vertex lecturerVertex = net.getVertexByIndex(lecturer + numStudents + numProjects + 1);
					net.addEdge(lecturerVertex, net.getSink(), Integer.parseInt(lines.get(lecturer + numStudents + numProjects)[1]));
				}
			}
			finally { 
				if (fr!=null) fr.close();
			}
		}
		catch (IOException e) {
			System.err.println("IO error:");
			System.err.println(e);
			System.exit(1);
		}
	}

	/**
	 * Executes Ford-Fulkerson algorithm on the constructed network net.
	 */
	public void fordFulkerson() {


        while (true) {
            ResidualGraph resGraph = new ResidualGraph(net);
            LinkedList<Edge> augmentingPath = resGraph.findAugmentingPath();
            if (augmentingPath.isEmpty()) {
                break;  // found the max flow
            } else {
                // Find the minimum capacity
                int minCap = Integer.MAX_VALUE;
                for (Edge e : augmentingPath) {
                    if (e.getCap() < minCap) minCap = e.getCap();
                }
                // Edit the path according to what we've found in the augmenting path
                for (Edge e : augmentingPath) {
                    Vertex source = e.getSourceVertex();
                    Vertex target = e.getTargetVertex();
                    Edge originalEdge = net.getAdjMatrixEntry(source, target);

					// if e is a forward edge:
                    if (net.getAdjList(source).contains(target) && originalEdge.getFlow() + minCap <= originalEdge.getCap()) {
                        originalEdge.setFlow(originalEdge.getFlow() + minCap);
                    } else {
						// We remove backwards edges
                        //originalEdge = net.getAdjMatrixEntry(target, source);
						//originalEdge.setFlow(net.getAdjMatrixEntry(target, source).getFlow() - minCap);
                    }
                }
            }
        }
    }

	/**
	 * Print the results of the execution of the Ford-Fulkerson algorithm.
	 */
	public void printResults() {
		if (net.isFlow()) {
			System.out.println("The assignment is a valid flow");
			System.out.println("A maximum flow has value: "+net.getValue());
			System.out.println("The flows along the edges are as follows:");
			net.printFlow();
		}
		else
			System.out.println("The assignment is not a valid flow");
	}
}