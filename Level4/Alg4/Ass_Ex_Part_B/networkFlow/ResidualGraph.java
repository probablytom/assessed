package networkFlow;
import java.util.*;

/**
 * The Class ResidualGraph.
 * Represents the residual graph corresponding to a given network.
 */
public class ResidualGraph extends Network {
	/**
	 * Instantiates a new ResidualGraph object.
	 * Builds the residual graph corresponding to the given network net.
	 * Residual graph has the same number of vertices as net.
	 * @param net the network
	 */
	public ResidualGraph (Network net) {
		super(net.numVertices);
		this.adjLists = net.adjLists;
		this.adjMatrix = net.adjMatrix;
		this.sink = net.sink;
		this.vertices = net.vertices;
	}

	/**
	 * Find an augmenting path if one exists.
	 * Determines whether there is a directed path from the source to the sink in the residual
	 * graph -- if so, return a linked list containing the edges in the augmenting path in the
     * form (s,v_1), (v_1,v_2), ..., (v_{k-1},v_k), (v_k,t); if not, return an empty linked list.
	 * @return the linked list
	 */
	public LinkedList<Edge> findAugmentingPath () {
		
		// Make an arraylist of edges to process and add all of the edges from the source to other vertices
		ArrayList<Edge> edges = new ArrayList<Edge>();
		for (Vertex vertex: adjLists.get(source.getLabel())) {
			edges.add(adjMatrix[source.getLabel()][vertex.getLabel()]);
		}
		// Also keep track of vertices we've seen
		ArrayList<Integer> verticesSeen = new ArrayList<Integer>();
		
		// Make a linkedList to store our residual graph
		Edge[][] resGraph = new Edge[numVertices][numVertices];
		
		// Now that edges contains all of the edges from the source, we follow those edges from the vertices. 
		// If they're valid (0<f<c), we add all of the edges where this edge's endpoint is the source.
		// If there's already an entry for the vertice in verticeInOut, we don't add the edges to the source, as we must have visited this source earlier
		//    ...(inflows must always be processed before outputs, so an entry means we've already added the edges for that vertice)
		while (!edges.isEmpty()) {
			ArrayList<Edge> originalEdges = (ArrayList<Edge>) edges.clone();
			for (Edge edge : originalEdges) {

				// If we haven't visited this edge's target edge before, we need to add that target's adjacent edges to the set of edges to process
				int origin = edge.getSourceVertex().getLabel();
				int target = edge.getTargetVertex().getLabel();
				if (!verticesSeen.contains(target) && target != sink.getLabel()) {
					verticesSeen.add(target);  // Make sure we can record flow to/from the target, which we'll do in a mo...
					// Add all of the edges from this vertice to edges.
					for (Vertex vertexToAdd : adjLists.get(target)) {
						edges.add(adjMatrix[target][vertexToAdd.getLabel()]);
					}
				}

				// We need to know which edges can be added to resGraph, which means they must satisfy one of two conditions:
				// 1. They are in the original graph (they have the original graph's direction) and they haven't met capacity
				// 2. They are the reverse of the original graph and the flow in the original direction is not at capacity
				if (edge.getFlow() < edge.getCap()) {
					resGraph[origin][target] = new Edge(edge.getSourceVertex(), edge.getTargetVertex(), edge.getCap()-edge.getFlow());
				} 
				// We remove backwards edges
				/*if (edge.getFlow() > 0) {
					resGraph[target][origin] = new Edge(edge.getTargetVertex(), edge.getSourceVertex(), edge.getFlow());
				}*/

			}
			edges.removeAll(originalEdges);
		}

		// Find the augmenting path in the residual graph if it exists!
		
		// Populate our initial set of paths to check with a list of paths consisting only of edges from the source in resGraph
		ArrayList<LinkedList<Edge>> possiblePaths = new ArrayList<LinkedList<Edge>>();
		for (int i = 0; i < numVertices; i++) {
			if (resGraph[source.getLabel()][i] != null) {
				LinkedList<Edge> currentStartingPoint = new LinkedList<Edge>();
				currentStartingPoint.add(resGraph[source.getLabel()][i]);
				possiblePaths.add(currentStartingPoint);
			}
		}
		
		// Build paths using breadth-first search and try to hit the sink. Currently possiblePaths contains LLs with only one edge from the source.
		while (!possiblePaths.isEmpty()) {

			// TODO: remember that we need to push onto the linked list i.e. newer edges go onto the front of the list
			ArrayList<LinkedList<Edge>> newPaths = new ArrayList<LinkedList<Edge>>();
			ArrayList<LinkedList<Edge>> originalPaths = (ArrayList<LinkedList<Edge>>) possiblePaths.clone();
			
			for (LinkedList<Edge> path : possiblePaths) {
				int targetLabel = path.peek().getTargetVertex().getLabel();
				for (int i = 0; i < numVertices; i++) {
					Edge residualEdge = resGraph[targetLabel][i];
					if (residualEdge != null && !path.contains(residualEdge)) { // If the edge exists in the residual graph and we haven't already visited it in this path:
						LinkedList<Edge> newPath = (LinkedList<Edge>) path.clone();
						newPath.push(residualEdge); // We push so it goes on the front of the list and we see this edge when we peek i.e. the list is in reverse order.
						newPaths.add(newPath);
						
						if (residualEdge.getTargetVertex().getLabel() == this.sinkLabel) {
							// We need to reverse the path so it's in the right order for returning, because we push earlier.
							LinkedList<Edge> augmentingPath = new LinkedList<Edge>();
							while (!newPath.isEmpty()) {
								// first element of newPath should become first element of Augmenting Path, so we move backwards through the new path and adding an element pushes earlier elements forwards
								augmentingPath.push(newPath.removeFirst()); 
							}
							return augmentingPath;
						}
					}
				}
				
			}
			possiblePaths.addAll(newPaths);
			possiblePaths.removeAll(originalPaths);
		}
		
		// We didn't find the path in the residual graph, as it hasn't been returned yet, so we return an empty linked list as per spec
		return new LinkedList<Edge>();
	}
}
