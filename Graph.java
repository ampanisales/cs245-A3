import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Class Purpose: 
 * 
 * @author Anthony Panisales
 */

public class Graph {
	
	int[][] edges;
	
	public Graph(int vertices) {
		edges = new int[vertices][vertices];
	}

	public void addEdge(int v1, int v2) {
		edges[v1][v2] = 1;
		edges[v2][v1] = 1;
	}
	
	private LinkedList<Integer> getNeighbors(int vertex) {
		LinkedList<Integer> list = new LinkedList<Integer>();
		for (int i = 0; i < edges.length; i++) {
			if (edges[vertex][i] == 1)
				list.add(new Integer(i));
		}
		return list;
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
	
	public ArrayList<Integer> findPath(int vertexA, int vertexB) {
		boolean[] visited = new boolean[edges.length];
		return findPath(vertexA, vertexB, visited);
	}
	
	private ArrayList<Integer> findPath(int vertexA, int vertexB, boolean[] visited) {
		ArrayQueue q = new ArrayQueue();
		q.enqueue(vertexA);
		ArrayList<Integer> path = new ArrayList<Integer>();
		while (!q.empty()) {
			int v = (Integer) q.dequeue();
			
			visited[v] = true;
			Iterator<Integer> it = getNeighbors(v).iterator();
			while (it.hasNext()) {
				int u = it.next();
				if (!visited[u]) {
					q.enqueue(u);
					visited[u] = true;
				}
			}
		}
		if (!visited[vertexB])
			return null;
		else
			return path;
	}
}
