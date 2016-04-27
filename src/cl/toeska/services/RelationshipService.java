package cl.toeska.services;

import java.util.Iterator;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

import cl.toeska.simulationengine.RatingGenerator;
import cl.toeska.simulationengine.TrustGenerator;

public class RelationshipService {
	
	/**
	 * 
	 * @param startInBetweenConnectivityFactor
	 * @param startEndLineConnectivityFactor
	 * @param inBetweenInBetweenConnectivityFactor
	 * @param inBetweenEndLineConnectivityFactor
	 * @param graphDb
	 */
	public static void simulateTrustRelationships (
			double startInBetweenConnectivityFactor, 
			double startEndLineConnectivityFactor,
			double inBetweenInBetweenConnectivityFactor,
			double inBetweenEndLineConnectivityFactor,
			double knowledgeObsolescenceRelationProbability,
			GraphDatabaseService graphDb) {
		Node startNode = null;
		Node actualTempNode = null;
		Iterable<Node> allInBetweenNodes = null;
		Iterable<Node> allEndLineNodes = null;
		// get all users
		// take one user and check
		
		
		try (Transaction tx = graphDb.beginTx()) {
			startNode = GraphService.getNode("i1", "Start", graphDb);
			
			allInBetweenNodes = GraphService.getAllInBetweenNodes(graphDb);
			
			// first, we define relationships from start user to other ones in the
			// whole network (including end-line nodes)
			for (Iterator<Node> nodeIterator = allInBetweenNodes.iterator(); nodeIterator.hasNext(); ) {
				actualTempNode = nodeIterator.next();
				if (Math.random() - startInBetweenConnectivityFactor <= 0.0) {
					UserService.createTrustRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(TrustGenerator.getGoodTrustValue()), "Start", "InBetween", graphDb);
					tx.success();
				} 
				// trust relationship not established directly, thus we try to establish a knowledge obsolescence
				// relationship
				else {
					
					if (Math.random() - knowledgeObsolescenceRelationProbability <= 0.0) {
						// we create a trust relationship, but using knowledge obsolescence!
						UserService.createTrustRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(TrustGenerator.getLinearTrustValueFromKnowObs()), "Start", "InBetween", graphDb);
						tx.success();
					}
					
				}
					
			}
			
	
			// second, we define relationships from start node to end line nodes
			allEndLineNodes = GraphService.getAllEndLineNodes(graphDb);
					
			for (Iterator<Node> nodeIterator = allEndLineNodes.iterator(); nodeIterator.hasNext(); ) {
				actualTempNode = nodeIterator.next();
				if (Math.random() - startEndLineConnectivityFactor <= 0.0) {	
					UserService.createTrustRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(TrustGenerator.getGoodTrustValue()), "Start", "EndLine", graphDb);
					tx.success();
				}
				// trust relationship not established directly, thus we try to establish a knowledge obsolescence
				// relationship
				else {
					if (Math.random() - knowledgeObsolescenceRelationProbability <= 0.0) {
						// we create a trust relationship, but using knowledge obsolescence!
						UserService.createTrustRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(TrustGenerator.getLinearTrustValueFromKnowObs()), "Start", "EndLine", graphDb);
						tx.success();
					}	
				}
			}

			// third, most time-complex part :(, we define relationships between all others pairs of nodes
			Iterable<Node> allInBetweenNodesBASE = allInBetweenNodes; 
					
			for (Iterator<Node> nodeIteratorBASE = allInBetweenNodesBASE.iterator(); nodeIteratorBASE.hasNext(); ) {
				startNode = nodeIteratorBASE.next();
				// between the base (in between) node and other in-between node
				for (Iterator<Node> nodeIteratorRUNNER = allInBetweenNodes.iterator(); nodeIteratorRUNNER.hasNext(); ) {
					actualTempNode = nodeIteratorRUNNER.next();
					if (Math.random() - inBetweenInBetweenConnectivityFactor <= 0.0) {
						UserService.createTrustRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(TrustGenerator.getGoodTrustValue()), "InBetween", "InBetween", graphDb);
						tx.success();
					}
					// trust relationship not established directly, thus we try to establish a knowledge obsolescence
					// relationship
					else {
						if (Math.random() - knowledgeObsolescenceRelationProbability <= 0.0) {
							// we create a trust relationship, but using knowledge obsolescence!
							UserService.createTrustRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(TrustGenerator.getLinearTrustValueFromKnowObs()), "InBetween", "InBetween", graphDb);
							tx.success();
						}
					}
					
				}
				// between the base node and end line node
				for (Iterator<Node> nodeIteratorRUNNER = allEndLineNodes.iterator(); nodeIteratorRUNNER.hasNext(); ) {
					actualTempNode = nodeIteratorRUNNER.next();
					if (Math.random() - inBetweenEndLineConnectivityFactor <= 0.0) {	
						UserService.createTrustRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(TrustGenerator.getGoodTrustValue()), "InBetween", "EndLine", graphDb);
						tx.success();
					}
					// trust relationship not established directly, thus we try to establish a knowledge obsolescence
					// relationship
					else {
						if (Math.random() - knowledgeObsolescenceRelationProbability <= 0.0) {
							// we create a trust relationship, but using knowledge obsolescence!
							UserService.createTrustRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(TrustGenerator.getLinearTrustValueFromKnowObs()), "InBetween", "EndLine", graphDb);
							tx.success();
						}
					}
				}
				
			}
						
			
		}  catch (Exception txEx) {
			txEx.printStackTrace();
		}
	
	}
	
	public static void simulateRatingRelationships (double connectivityFactor, double goodItemRatio, GraphDatabaseService graphDb) {
		// here, we create the rating relationships between end line nodes and the item nodes
		// i.e., we simulate here a user rating and item
		Node startNode = null;
		Node actualTempNode = null;
		Iterable<Node> allItemNodes = null;
		double originalConnectivityFactor = connectivityFactor;
		// get all users
		// take one user and check
				
		try (Transaction tx = graphDb.beginTx()) {
			Iterable<Node> allEndLineNodesBASE =  GraphService.getAllEndLineNodes(graphDb); 
			
			allItemNodes = GraphService.getAllItemNodes(graphDb);
			
			for (Iterator<Node> nodeIteratorBASE = allItemNodes.iterator(); nodeIteratorBASE.hasNext(); ) {
				actualTempNode = nodeIteratorBASE.next();
				connectivityFactor = originalConnectivityFactor;
				// rating between the base node and the item node
				for (Iterator<Node> nodeIteratorRUNNER = allEndLineNodesBASE.iterator(); nodeIteratorRUNNER.hasNext(); ) {
					startNode = nodeIteratorRUNNER.next();
					if (Math.random() - connectivityFactor <= 0.0) {
						if (Math.random() - goodItemRatio <= 0)
							ItemService.createRatingRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(RatingGenerator.getGoodItemRating()), "EndLine", "Item", graphDb);
						else 
							ItemService.createRatingRelationship(startNode.getProperty("nid").toString(), actualTempNode.getProperty("nid").toString(), String.valueOf(RatingGenerator.getRandomItemRating()), "EndLine", "Item", graphDb);
					}
					
					if (connectivityFactor >= 0.0)
						connectivityFactor -= 0.1;
					tx.success();
				}
				
			}
						
		}  catch (Exception txEx) {
			txEx.printStackTrace();
		}
	
	}
	
	
	/// FOR NOW; THIS METHOD WILL PROBABLY BE DEPRECATED
	/**
	 * 
	 * @param startInBetweenConnectivityFactor
	 * @param startEndLineConnectivityFactor
	 * @param inBetweenInBetweenConnectivityFactor
	 * @param inBetweenEndLineConnectivityFactor
	 * @param graphDb
	 */
	public static void simulateKnowledgeObsolescenceRelationship (
			double startInBetweenConnectivityFactor, 
			double startEndLineConnectivityFactor,
			double inBetweenInBetweenConnectivityFactor,
			double inBetweenEndLineConnectivityFactor,
			double endLineEndLineConnectivityFactor,
			GraphDatabaseService graphDb) {
		// in each step, we make use of cypher to determine 'labeled' pairs of nodes
		// with no TRUST relationships between them
		// then, for each 'labeled class of nodes' we simulate the knowledge obsolescence relationship
		// which is actually translated to trust by using the linear regression model
		Iterator<String> sourceNodes;
		Iterator<String> sinkNodes;
		
		try (Transaction tx = graphDb.beginTx()) {
			ExecutionEngine engine = new ExecutionEngine(graphDb);

			ExecutionResult result;

			// first, get pairs with no relationship between Start node an InBetween nodes
			result = engine.execute ("MATCH (n:Start),(m:InBetween) WHERE NOT (n)-[:TRUSTS]->(m) AND n.nid <> m.nid RETURN n.nid");
			sourceNodes = result.columnAs("n.nid");

			result = engine.execute ("MATCH (n:Start),(m:InBetween) WHERE NOT (n)-[:TRUSTS]->(m) AND n.nid <> m.nid RETURN m.nid");
			sinkNodes = result.columnAs("m.nid");

			while (sourceNodes.hasNext() && sinkNodes.hasNext()) { 
				if (Math.random() - startInBetweenConnectivityFactor <= 0.0) {	
					UserService.createTrustRelationship(
							sourceNodes.next().toString(),
							sinkNodes.next().toString(),
							String.valueOf(TrustGenerator.getLinearTrustValueFromKnowObs()),
							"Start",
							"InBetween",
							graphDb);
					tx.success();
				}
			}
			
			// second, get pairs with no relationships between Start node and EndLine nodes
			result = engine.execute ("MATCH (n:Start),(m:EndLine) WHERE NOT (n)-[:TRUSTS]->(m) AND n.nid <> m.nid RETURN n.nid");
			sourceNodes = result.columnAs("n.nid");

			result = engine.execute ("MATCH (n:Start),(m:EndLine) WHERE NOT (n)-[:TRUSTS]->(m) AND n.nid <> m.nid RETURN m.nid");
			sinkNodes = result.columnAs("m.nid");

			while (sourceNodes.hasNext() && sinkNodes.hasNext()) { 
				if (Math.random() - startEndLineConnectivityFactor <= 0.0) {	
					UserService.createTrustRelationship(sourceNodes.next().toString(), sinkNodes.next().toString(), String.valueOf(TrustGenerator.getLinearTrustValueFromKnowObs()), "Start", "EndLine", graphDb);
					tx.success();
				}
			}
			
			
			// third, get pairs with no relationships between EndLine nodes
			result = engine.execute ("MATCH (n:EndLine),(m:EndLine) WHERE NOT (n)-[:TRUSTS]->(m) AND n.nid <> m.nid RETURN n.nid");
			sourceNodes = result.columnAs("n.nid");

			result = engine.execute ("MATCH (n:EndLine),(m:EndLine) WHERE NOT (n)-[:TRUSTS]->(m) AND n.nid <> m.nid RETURN m.nid");
			sinkNodes = result.columnAs("m.nid");

			while (sourceNodes.hasNext() && sinkNodes.hasNext()) { 
				if (Math.random() - endLineEndLineConnectivityFactor <= 0.0) {	
					UserService.createTrustRelationship(sourceNodes.next().toString(), sinkNodes.next().toString(), String.valueOf(TrustGenerator.getLinearTrustValueFromKnowObs()), "Start", "EndLine", graphDb);
					tx.success();
				}
			}
			
			// fourth, get pairs with no relationships between InBetween nodes
			result = engine.execute ("MATCH (n:InBetween),(m:InBetween) WHERE NOT (n)-[:TRUSTS]->(m) AND n.nid <> m.nid RETURN n.nid");
			sourceNodes = result.columnAs("n.nid");

			result = engine.execute ("MATCH (n:InBetween),(m:InBetween) WHERE NOT (n)-[:TRUSTS]->(m) AND n.nid <> m.nid RETURN m.nid");
			sinkNodes = result.columnAs("m.nid");

			while (sourceNodes.hasNext() && sinkNodes.hasNext()) { 
				if (Math.random() - inBetweenInBetweenConnectivityFactor <= 0.0) {	
					UserService.createTrustRelationship(sourceNodes.next().toString(), sinkNodes.next().toString(), String.valueOf(TrustGenerator.getLinearTrustValueFromKnowObs()), "InBetween", "InBetween", graphDb);
					tx.success();
				}
			}
			

			tx.success();
		}  catch (Exception txEx) {
			txEx.printStackTrace();
		}



	}
}
