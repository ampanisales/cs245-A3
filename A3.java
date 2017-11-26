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
import java.util.Iterator;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Class Purpose: 
 * 
 * @author Anthony Panisales
 */

public class A3 {

	public static void main(String args[]) throws ParseException {
		Scanner filescan;
		
		//Get file
		while (true) {
			try {
				if (args == null) {
					Scanner locationScan = new Scanner(System.in);
					System.out.print("Enter the file location: ");
					String fileLocation = locationScan.nextLine();
					filescan = new Scanner(new File(fileLocation));
					locationScan.close();
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
		
		//Graph g = new Graph(54200); //ASK PROFESSOR ABOUT HEAP SIZE
		Graph g = new Graph(10000);
	
		/*This keeps track of which actor each vertex in the graph
		corresponds to */
		ArrayList<String> actorVertices = new ArrayList<String>(); 
		
		int numOfMovies = 0;
		while (filescan.hasNextLine()) {
			numOfMovies++;
			
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
			ArrayList<String> actorsInMovie = new ArrayList<String>();
			while (it.hasNext()) {
				JSONObject jsOb = it.next();
				String actorName = (String) jsOb.get("name");
				
				//If the actor has not been accounted for yet
				if (!actorVertices.contains(actorName))
					actorVertices.add(actorName);	
				actorsInMovie.add(actorName);
			}
			
			//Connect all the actors in the movie with each other
//			for (String actorA : actorsInMovie) {
//				int actorAVertex = actorVertices.indexOf(actorA);
//				for (String actorB : actorsInMovie) {
//					if (!actorA.equals(actorB)) {
//						int actorBVertex = actorVertices.indexOf(actorB);
//						g.addEdge(actorAVertex, actorBVertex);
//					}
//				}
//			}
			filescan.nextLine();
			filescan.useDelimiter(Pattern.compile(","));
			//System.out.println("Pattern of filescan: " + filescan.delimiter());
		}
		filescan.close();
		System.out.println("Number of movies: " + numOfMovies);
		System.out.println("Number of actors: " + actorVertices.size());
		
		//Asks user for names of actors then finds a path between them
		Scanner inputScan = new Scanner(System.in);
		while (true) {
			System.out.print("Actor 1 name: ");
			String actor1 = inputScan.nextLine();
			if (!actorVertices.contains(actor1)) {
				System.out.println("No such actor.");
				continue;
			}
			System.out.print("Actor 2 name: ");
			String actor2 = inputScan.nextLine();
			if (!actorVertices.contains(actor2)) {
				System.out.println("No such actor.");
				continue;
			}
			ArrayList<Integer> path = g.findPath(actorVertices.indexOf(actor1), actorVertices.indexOf(actor2));
			Collections.reverse(path);
			if (path == null)
				System.out.println("No path was found");
			else {
				System.out.print("Path between " + actor1 + " and " + actor2 + ": ");
				for (int v : path) {
					if (!actorVertices.get(v).equals(actor2))
						System.out.print(actorVertices.get(v) + " --> ");
					else
						System.out.print(actorVertices.get(v));
				}
			}
			break;
		}
		inputScan.close();
	}
}
