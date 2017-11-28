import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class Purpose: Holds all of the names of 
 * actors that were read from the the 
 * 'tmdb_5000_credits.csv' file
 * 
 * @author Anthony Panisales
 */

public class Graph {
	
	class Vertex {
		
		String value; 
		LinkedList<Vertex> connections;
		
		public Vertex(String newVal) {
			value = newVal;
			connections = new LinkedList<Vertex>();
		}
		
		public void setValue(String newVal) {
			value = newVal;
		}
	}

	private Vertex[] vertices;
	
	public Graph(int n) {
		vertices = new Vertex[n];
		for (int i = 0; i < vertices.length; i++)
			vertices[i] = new Vertex(null);
	}
	
	public Vertex[] getVertices() {
		return vertices;
	}
	
	public void addEdge(int actorAVertex, int actorBVertex) {
		Vertex v1 = vertices[actorAVertex];
		Vertex v2 = vertices[actorBVertex];
		v1.connections.add(v2);
		v2.connections.add(v1);
	}
	
	/**
	 * Function Purpose: Used in the findPath() method.
	 * Returns the LinkedList of connections with other vertices
	 * that a vertex in 'vertices' has.
	 */
	private LinkedList<Vertex> getNeighbors(int index) {
		return vertices[index].connections;
	}
	
	/**
	 * Function Purpose: Finds the shortest path between two vertices
	 * and returns the path as an ArrayList.
	 */
	//IS IT OK FOR THIS TO BE RECURSIVE?
	public ArrayList<String> findPath(int indexA, int indexB, HashMap<String,Integer> map) {
		boolean[] visited = new boolean[vertices.length];
		return findPath(indexA, indexB, visited, map);
	}
	
	private ArrayList<String> findPath(int indexA, int indexB, boolean[] visited, HashMap<String,Integer> map) {
		ArrayQueue q = new ArrayQueue();
		q.enqueue(indexA);
		ArrayList<String> path = new ArrayList<String>();
		if (indexA == indexB) {
			path.add(vertices[indexA].value);
			return path;
		}
		while (!q.empty()) {
			int v = (Integer) q.dequeue();
			visited[v] = true;
			Iterator<Vertex> it = getNeighbors(v).iterator();
			while (it.hasNext()) {
				//HashMaps allow us to get the index of the vertex in 'vertices' quicker
				int u = map.get(it.next().value);
				if (!visited[u]) {
					q.enqueue(u);
					visited[u] = true;
					if (u == indexB) {
						path.add(vertices[u].value);
						path.addAll(findPath(indexA, v, map));
						return path;
					}
				}
			}
		}
		//A path could not be found
		return null;
	}
}
