import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileNotFoundException;
//import java.io.FileReader;
//import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class Purpose: Reads and retrieves the names of 
 * actors from the 'tmdb_5000_credits.csv' file, then 
 * finds the shortest path between two actors
 * 
 * @author Anthony Panisales
 */

public class A3 {

	public static void main(String args[]) throws ParseException {
		Scanner filescan;
		Scanner inputScan = new Scanner(System.in);
		
		//Get file
		while (true) {
			try {
				if (args.length == 0) {
					System.out.print("Enter the file location: ");
					String fileLocation = inputScan.nextLine();
					filescan = new Scanner(new File(fileLocation));
				} else {
					filescan = new Scanner(new File(args[0]));
				}
			} catch (FileNotFoundException e) {
				System.out.println("File not found");
				continue;
			}
			break;
		}
		
		//Read file (We only care about 'name' in cast)
		filescan.nextLine(); //So first line isn't used
		filescan.useDelimiter(Pattern.compile(","));
		
		Graph g = new Graph(54200);
		int i = 0;
	
		/*This keeps track of which actor each vertex in the graph
		corresponds to */
		HashMap<String,Integer> actorVertices = new HashMap<String,Integer>();
		
		int numOfMovies = 0;
		System.out.println("Getting actor names from file...");
		while (filescan.hasNextLine()) {
			//numOfMovies++;
			System.out.println("Number of movies: " + numOfMovies++);
			
			//So movieID and title are skipped
			filescan.next();
			filescan.next();
			
			//So innerFileScan reads whole JSONArray
			filescan.useDelimiter(Pattern.compile("\\["));
			filescan.next();
			filescan.useDelimiter(Pattern.compile("\\]"));
			String data = filescan.next();
			
			JSONParser parser = new JSONParser();
			String arr = data.replaceAll("\"\"", "\"");
			JSONArray cast;
			
			try {
				cast = (JSONArray) parser.parse(arr + "]");
				//System.out.println(arr);
			} catch (Exception e) {
				String data2 = "";
				while (true) {
					data2 = filescan.next();
					String arr2 = "]" + data2.replaceAll("\"\"", "\"");
					arr += arr2;
					data2 = filescan.next();
					if (data2.charAt(1) == ',')
						break;
				}
				cast = (JSONArray) parser.parse(arr + "]");
				//System.out.println(arr);
			}
			
			//Get actor's names
			Iterator<JSONObject> it = cast.iterator();
			HashMap<String,Integer> actorsInMovie = new HashMap<String,Integer>();
			int j = 0;
			while (it.hasNext()) {
				JSONObject jsOb = it.next();
				String actorName = ((String) jsOb.get("name"));
				if (!actorVertices.containsKey(actorName)) {
					
					//You might have to have getters and setters for this
					g.vertices[i].value = actorName;
					
					actorVertices.put(actorName, i++);
				}
				actorsInMovie.put(actorName, j++);
			}
			
			//Connect all the actors in the movie with each other
			for (String actorA : actorsInMovie.keySet()) {
				int actorAVertex = actorVertices.get(actorA);
				for (String actorB : actorsInMovie.keySet()) {
					if (!actorA.equals(actorB)) {
						int actorBVertex = actorVertices.get(actorB);
						g.addEdge(actorAVertex, actorBVertex);
					}
				}
			}
			filescan.nextLine();
			filescan.useDelimiter(Pattern.compile(","));
			//System.out.println("Pattern of filescan: " + filescan.delimiter());
		}
		filescan.close();
		System.out.println("Number of movies: " + numOfMovies);
		System.out.println("Number of actors: " + actorVertices.size());
		
		//Asks user for names of actors then finds a path between them
		while (true) {
			System.out.print("Actor 1 name: ");
			String actor1 = inputScan.nextLine();
			if (!actorVertices.containsKey(actor1)) {
				System.out.println("No such actor.");
				continue;
			}
			System.out.print("Actor 2 name: ");
			String actor2 = inputScan.nextLine();
			if (!actorVertices.containsKey(actor2)) {
				System.out.println("No such actor.");
				continue;
			}
			ArrayList<String> path = g.findPath(actorVertices.get(actor1), 
					actorVertices.get(actor2));
			Collections.reverse(path);
			if (path == null)
				System.out.println("No path was found");
			else {
				System.out.print("Path between " + actor1 + " and " + actor2 + ": ");
				for (String actor : path) {
					if (!actor.equals(actor2))
						System.out.print(actor + " --> ");
					else
						System.out.print(actor);
				}
			}
			System.out.println();
			break;
		}
		inputScan.close();
	}
}
