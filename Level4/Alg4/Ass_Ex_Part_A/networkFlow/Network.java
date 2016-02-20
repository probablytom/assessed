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

	/**
	 * Instantiates a new network.
	 * @param n the number of vertices
	 */
	public Network (int n) {
		super(n);

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
			ArrayList<Edge> originalEdges = edges;
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
		ArrayList<Edge> edgesSeen = new ArrayList<Edge>();
		for (int vertexNumber = 0; vertexNumber < adjLists.size(); vertexNumber++) {
			for (Vertex adjVertex : adjLists.get(vertexNumber)) {
				Edge currentEdge = adjMatrix[vertexNumber][adjVertex.getLabel()];
				if (!edgesSeen.contains(currentEdge)) {
					edgesSeen.add(currentEdge);
					output.append("(" + Integer.toString(vertexNumber) + "," + Integer.toString(adjVertex.getLabel()) + ") " + Integer.toString(currentEdge.getCap()) + "/" + Integer.toString(currentEdge.getFlow()) + "\n");
				}
			}
		}
		System.out.println(output.toString());
	}
}
