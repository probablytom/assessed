package networkFlow;
import java.util.*;

/**
 * The Class Network.
 * Represents a network - inherits from DirectedGraph class.
 */
public class Network extends DirectedGraph {

	/** The source vertex of the network. */
	protected Vertex source;

	/** The label of the source vertex. */
	protected int sourceLabel;

	/** The sink vertex of the network. */
	protected Vertex sink;

	/** The label of the sink vertex. */
	protected int sinkLabel;

	protected int numStudents;
	protected int numProjects;
	protected int numLecturers;

	/**
	 * Instantiates a new network.
	 * @param n the number of vertices
	 */
	public Network (int n) {
		super(n);
		constructorSetup();
	}

	public Network (int numStudents, int numProjects, int numLecturers) {
		super(numLecturers + numProjects + numStudents + 2);  // +2 for sink and source
		this.numLecturers = numLecturers;
		this.numProjects = numProjects;
		this.numStudents = numStudents;
		constructorSetup();
	}

	private void constructorSetup() {
		// add the source vertex - assumed to have label 0
		sourceLabel = 0;
		source = addVertex(sourceLabel);
		// add the sink vertex - assumed to have label numVertices - 1
		sinkLabel = numVertices - 1;
		sink = addVertex(sinkLabel);

		// add the remaining vertices
		for (int i = 1 ; i <=numVertices-2 ; i++)
			addVertex(i);
	}

	/**
	 * Gets the source vertex.
	 * @return the source vertex
	 */
	public Vertex getSource() {
		return source;
	}

	/**
	 * Gets the sink vertex.
	 * @return the sink vertex
	 */
	public Vertex getSink() {
		return sink;
	}

	/**
	 * Adds the edge with specified source and target vertices and capacity.
	 * @param sourceEndpoint the source endpoint vertex
	 * @param targetEndpoint the target endpoint vertex
	 * @param capacity the capacity of the edge
	 */
	public void addEdge(Vertex sourceEndpoint, Vertex targetEndpoint, int capacity) { 
		Edge e = new Edge(sourceEndpoint, targetEndpoint, capacity);
		adjLists.get(sourceEndpoint.getLabel()).addLast(targetEndpoint);
		adjMatrix[sourceEndpoint.getLabel()][targetEndpoint.getLabel()]=e;
	}

	/**
	 * Returns true if and only if the assignment of integers to the flow fields of 
	 * each edge in the network is a valid flow.
	 * @return true, if the assignment is a valid flow
	 */
	public boolean isFlow() {
		// Initialise a hashmap to contain all of our inflows and outflows for our flow conservation constraint
		HashMap<Integer, Integer> verticeInOut = new HashMap<Integer, Integer>();
		verticeInOut.put(source.getLabel(), 0); // We don't care about the source
		verticeInOut.put(sink.getLabel(), 0);  // We don't care about the sink

		// Make an arraylist of edges to process and add all of the edges from the source to other vertices
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Vertex vertex: adjLists.get(source.getLabel())) {
			edges.add(adjMatrix[source.getLabel()][vertex.getLabel()]);
		}

		// Now that edges contains all of the edges from the source, we follow those edges from the vertices. 
		// If they're valid (0<f<c), we add all of the edges where this edge's endpoint is the source.
		// If there's already an entry for the vertice in verticeInOut, we don't add the edges to the source, as we must have visited this source earlier
		//    ...(inflows must always be processed before outputs, so an entry means we've already added the edges for that vertice)
		while (!edges.isEmpty()) {
			ArrayList<Edge> originalEdges = (ArrayList<Edge>) edges.clone();
			for (Edge edge : originalEdges) {

				// Make sure we obey the capacity constraint
				if (edge.getFlow() < 0 || edge.getFlow() > edge.getCap()) return false;

				// If there isn't already an entry for this edge's endpoint in verticeInOut, we need to add that target's adjacent edges to the set of edges to process
				int origin = edge.getSourceVertex().getLabel();
				int target = edge.getTargetVertex().getLabel();
				if (!verticeInOut.containsKey(target) && target != sink.getLabel()) {
					verticeInOut.put(target, 0);  // Make sure we can record flow to/from the target, which we'll do in a mo...
					// Add all of the edges from this vertice to edges.
					for (Vertex vertexToAdd : adjLists.get(target)) {
						edges.add(adjMatrix[target][vertexToAdd.getLabel()]);
					}
				}


				// Make sure we obey the flow conservation constraint
				if (origin != source.getLabel()) {
					verticeInOut.put(origin, verticeInOut.get(origin) - edge.getFlow()); // Update the flow from the origin
				}
				if (target != sink.getLabel()) {
					verticeInOut.put(target, verticeInOut.get(target) + edge.getFlow());  // Update the flow to the target
				}

				// Now that we've processed this edge, remove it from the list of edges to process
				edges.remove(edge);

			}
		}
		System.out.println(verticeInOut.values());
		// Confirm that all of the entries in verticeInOut are 0, i.e. all of the inflows to vertices are equal to their outflows.
		for (Integer vertexValue : verticeInOut.values()) {
			if (vertexValue != 0) return false;
		}

		// We satisfy all necessary constraints for a valid flow!
		return true;

	}

	/**
	 * Gets the value of the flow.
	 * @return the value of the flow
	 */
	public int getValue() {
		int output = 0;
		
		// Make an arraylist of edges to process and add all of the edges from the source to other vertices
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Vertex vertex: adjLists.get(source.getLabel())) {
			edges.add(adjMatrix[source.getLabel()][vertex.getLabel()]);
		}
		
		for (Edge edge : edges) {
			output += edge.getFlow();
		}
		
		return output;
	}

	/**
	 * Prints the flow.
	 * Display the flow through the network in the following format:
	 * (u,v) c(u,v)/f(u,v)
	 * where (u,v) is an edge, c(u,v) is the capacity of that edge and f(u,v) 
	 * is the flow through that edge - one line for each edge in the network
	 */
	public void printFlow() {
		StringBuilder output = new StringBuilder();
		int[] flows = new int[numLecturers + numProjects + numStudents];
		for (int student = 0; student < numStudents; student++) {
			flows[student] = -1;  // Default incase student is unassigned
			Vertex studentVertex = this.getVertexByIndex(student+1); // +1 to avoid source
			LinkedList<Vertex> studentAdj = this.getAdjList(studentVertex);
			// If student is assigned, they'll have 2 adjacencies
			for (Vertex adj : studentAdj) {
				if (!this.source.equals(adj)) {
					if (!adj.equals(this.getSource())) {
						Edge e = getAdjMatrixEntry(studentVertex, adj);
						if (e.getFlow() > 0) {
							flows[student] = adj.getLabel() - numStudents;
						}
					}
				}
			}

			output.append("Student " + Integer.toString(student + 1) + " is ");
			String endPart = flows[student] == -1 ? "unassigned\n" : "assigned to project " + Integer.toString(flows[student]) + "\n";
			output.append(endPart);
		}
		output.append("\n");
		for (int project = 0; project < numProjects; project++) {
			int projectIndex = project + numStudents;
			flows[projectIndex] = 0;
			Vertex projectVertex = this.getVertexByIndex(projectIndex + 1);
			LinkedList<Vertex> projectAdj = this.getAdjList(projectVertex);
			int capacity = 0;
			for (Vertex adj : projectAdj) {
				Edge e = getAdjMatrixEntry(projectVertex, adj);
				// if e==null we have the edge between the project and the lecturer
				if (e != null) {
					flows[projectIndex] += e.getFlow();
				}
			}
			boolean foundLecturer = false;
			for (int i = 0; i < numLecturers && !foundLecturer; i++) {
				Vertex lecturer = getVertexByIndex(i+numStudents+numProjects+1);
				if (getAdjMatrixEntry(projectVertex, lecturer) != null) {
					foundLecturer = true;
					capacity += getAdjMatrixEntry(projectVertex, lecturer).getCap();
				}
			}
			output.append("Project " + Integer.toString(project+1) + " with capacity " + Integer.toString(capacity) + " is assigned " + Integer.toString(flows[projectIndex]) + " student");
			String endPart = flows[projectIndex] == 1 ? "\n" : "s\n";
			output.append(endPart);
		}
		output.append("\n");
		for (int lecturer = 0; lecturer < numLecturers; lecturer++) {
			int lecturerIndex = lecturer + numStudents + numProjects;
			Vertex lecturerVertex = this.getVertexByIndex(lecturerIndex + 1);
			flows[lecturerIndex] = 0;
			int capacity = 0;
            Edge edge = getAdjMatrixEntry(lecturerVertex, getSink());
            flows[lecturerIndex] = edge.getFlow();
			capacity += getAdjMatrixEntry(lecturerVertex, getSink()).getCap();
			output.append("Lecturer " + Integer.toString(lecturer + 1) + " with capacity " + Integer.toString(capacity) + " is assigned "+ flows[lecturerIndex] + " student");
			String endPart = flows[lecturerIndex] == 1 ? "\n" : "s\n";
			output.append(endPart);
		}
		System.out.println(output.toString());
	}
}
