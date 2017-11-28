import java.util.ArrayList;
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
	}

	Vertex[] vertices;
	
	public Graph(int n) {
		vertices = new Vertex[n];
		for (int i = 0; i < vertices.length; i++)
			vertices[i] = new Vertex(null);
	}
	
	/**
	 * Function Purpose: Searches for the vertex in 
	 * 'vertices' with a specific actor's name. Returns
	 * the index of that vertex in 'vertices' if located.
	 * Returns -1 otherwise.
	 */
	public int search(Vertex[] arr, String target) {
		for (int i = 0; i < arr.length; i++) {
			if (arr[i].value == null)
				return -1;
			else if (arr[i].value.equals(target))
				return i;
		}
		return -1;
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
	
//	public void bfs() {
//		boolean[] visited = new boolean[edges.length];
//		bfs(0, visited);
//		for (int i = 0; i < visited.length; i++) {
//			if (!visited[i])
//				bfs(i, visited);
//		}
//	}
//	
//	private void bfs(int vertex, boolean[] visited) {
//		ArrayQueue q = new ArrayQueue();
//		q.enqueue(vertex);
//		while (!q.empty()) {
//			int v = (Integer) q.dequeue();
//			System.out.println(v + " ");
//			visited[v] = true;
//			Iterator<Integer> it = getNeighbors(v).iterator();
//			while (it.hasNext()) {
//				int u = it.next();
//				if (!visited[u]) {
//					q.enqueue(u);
//					visited[u] = true;
//				}
//			}
//		}
//	}
	
	/**
	 * Function Purpose: Finds the shortest path between two vertices
	 * and returns the path as an ArrayList.
	 */
	//IS IT OK FOR THIS TO BE RECURSIVE?
	public ArrayList<String> findPath(int indexA, int indexB) {
		boolean[] visited = new boolean[vertices.length];
		return findPath(indexA, indexB, visited);
	}
	
	private ArrayList<String> findPath(int indexA, int indexB, boolean[] visited) {
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
				int u = search(vertices, it.next().value);
				if (!visited[u]) {
					q.enqueue(u);
					visited[u] = true;
					if (u == indexB) {
						path.add(vertices[u].value);
						path.addAll(findPath(indexA, v));
						return path;
					}
				}
			}
		}
		//A path could not be found
		return null;
	}
}
