package cl.toeska.main;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import cl.toeska.services.ItemService;
import cl.toeska.services.RecommenderService;
import cl.toeska.services.RelationshipService;
import cl.toeska.services.UserService;

public class Starter {
	
	public static void writeSimTimeToFile (long simTime) {
		try {
			 
			String content = simTime + "\n";

			File reportFile = new File("results/file.txt");

			
			if (!reportFile.exists()) {
				reportFile.createNewFile();
			}

			FileWriter fw = new FileWriter(reportFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Simulation Time Written!");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main (String args[]) {
	
		long startTime = System.currentTimeMillis();
		for (int iteration = 0; iteration < 40; iteration++) {
			
			GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("sim/GRAPHDB" + iteration + ".db");
			
			UserService.createEndLineNodes(10, graphDb);
			UserService.createInBetweenNodes (20, graphDb);
			UserService.createStartNode(graphDb);
			ItemService.createItemsNodes(50, graphDb);

			RelationshipService.simulateTrustRelationships(0.5, 0.5, 0.5, 0.5, 0.3, graphDb);

			RelationshipService.simulateRatingRelationships(0.3, 0.8, graphDb);

			RecommenderService.getTrustBasedRecommendation(graphDb, iteration);
			
			graphDb.shutdown();	
						
		}
		long endTime = System.currentTimeMillis();
		
		// write approximate simulation run time in milliseconds
		writeSimTimeToFile ((endTime - startTime));
			
	}

}
