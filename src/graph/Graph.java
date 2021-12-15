package graph;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.util.HashMap;
import java.util.ArrayList;

/**
 * This class represents a graph data structure.
 * @author Tyler Weir 
 * 
 */
public class Graph<V> implements GraphIfc<V> {

	private Map<V, HashSet<V>> vertices; 

	/**
	 * Constructor for the graph class.
	 */
	public Graph() {
		this.vertices = new HashMap<V, HashSet<V>>();
	}
			
	/**
	 * Returns the number of vertices in the graph
	 * @return The number of vertices in the graph
	 */
	public int numVertices() {
		return this.vertices.size();
	}
		
	/**
	 * Returns the number of edges in the graph
	 * @return The number of edges in the graph
	 */
	public int numEdges() {
		// get the vertices
		ArrayList<V> verts = new ArrayList<V>(this.getVertices());

		// Iterate over each vertex and sum neighbors
		int numEdges = 0;
		for (int i = 0; i < verts.size(); i++) {
			numEdges += this.vertices.get(verts.get(i)).size();
		}
		return numEdges;
	}	
	

	
	/**
	 * Removes all vertices from the graph
	 */
	public void clear() {
		this.vertices.clear();
	}	
		
	/** 
	 * Adds a vertex to the graph. This method has no effect if the vertex already exists in the graph. 
	 * @param v The vertex to be added
	 */
	public void addVertex(V v) {
		this.vertices.put(v, new HashSet<V>());
	}	
	
	/**
	 * Adds an edge between vertices u and v in the graph. 
	 *
	 * @param u A vertex in the graph
	 * @param v A vertex in the graph
	 * @throws IllegalArgumentException if either vertex does not occur in the graph.
	 */
	public void addEdge(V u, V v) {
		// Check vertexs for existance
		boolean containsVertices = this.containsVertex(u) & this.containsVertex(v);
		if (!containsVertices) {
			throw new IllegalStateException("One or both vertices do not occur in the graph.");
		}

		// Add the edge to u's edge list.
		HashSet<V> edges = this.vertices.get(u);
		edges.add(v);
	}	

	/**
	 * Returns the set of all vertices in the graph.
	 * @return A set containing all vertices in the graph
	 */
	public Set<V> getVertices() {
		return this.vertices.keySet();
	}
	
	/**
	 * Returns the neighbors of v in the graph. A neighbor is a vertex that is connected to
	 * v by an edge. If the graph is directed, this returns the vertices u for which an 
	 * edge (v, u) exists.
	 *  
	 * @param v An existing node in the graph
	 * @return All neighbors of v in the graph.
	 * @throws IllegalArgumentException if the vertex does not occur in the graph
	 */
	public List<V> getNeighbors(V v) {
		if(!this.containsVertex(v)) {
			throw new IllegalStateException("The vertex does not occur in the graph.");
		}

		return new ArrayList<V>(this.vertices.get(v));
	}	

	/**
	 * Determines whether the given vertex is already contained in the graph. The comparison
	 * is based on the <code>equals()</code> method in the class V. 
	 * 
	 * @param v The vertex to be tested.
	 * @return True if v exists in the graph, false otherwise.
	 */
	public boolean containsVertex(V v) {
		return this.vertices.containsKey(v);
	}
	
	/**
	 * Determines whether an edge exists between two vertices. In a directed graph,
	 * this returns true only if the edge starts at v and ends at u. 
	 * @param v A node in the graph
	 * @param u A node in the graph
	 * @return True if an edge exists between the two vertices
	 * @throws IllegalArgumentException if either vertex does not occur in the graph
	 */
	@SuppressWarnings("rawtypes")
	public boolean edgeExists(V v, V u) {
		// Check vertexs for existance
		boolean containsVertices = this.containsVertex(u) & this.containsVertex(v);
		if (!containsVertices) {
			throw new IllegalStateException("One or both vertices do not occur in the graph.");
		}

		HashSet edges = this.vertices.get(v);
		return edges.contains(u);
	}

	/**
	 * Returns the degree of the vertex. In a directed graph, this returns the outdegree of the
	 * vertex. 
	 * @param v A vertex in the graph
	 * @return The degree of the vertex
	 * @throws IllegalArgumentException if the vertex does not occur in the graph
	 */
	public int degree(V v) {
		if(!this.containsVertex(v)) {
			throw new IllegalStateException("The vertex does not occur in the graph.");
		}

		return this.vertices.get(v).size();
	}
	
	/**
	 * Returns a string representation of the graph. The string representation shows all
	 * vertices and edges in the graph. 
	 * @return A string representation of the graph
	 */
	public String toString() {
		return "" + this.vertices;
	}

}
