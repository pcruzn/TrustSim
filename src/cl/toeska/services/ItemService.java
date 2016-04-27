package cl.toeska.services;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;

import cl.toeska.simulationobjects.Item;

public class ItemService {
	
	public static void createItemsNodes (int nItems, GraphDatabaseService graphDb) {
		
		Item item = new Item();
		
		for (int itemCount = 0; itemCount < nItems; itemCount++) {
			item.createItemNode("it" + (itemCount + 1), graphDb);
		}
	
	}
	
	// similar as the other ones, but in this case our endNode is not an 'user', but an
	// item
	public static void createRatingRelationship (String initNodeId, String endNodeId, String value, String initNodeLabel, String endNodeLabel, GraphDatabaseService graphDb) {
		Item node = new Item();
		
		node.createRatingRelationship(GraphService.getNode(initNodeId, initNodeLabel, graphDb), GraphService.getNode(endNodeId, endNodeLabel, graphDb), value, graphDb);
		
	}
	
	public static int getRatingOnItem (Node raterNode, Node itemNode, GraphDatabaseService graphDb) {
		int rating = 0;
		
		ExecutionEngine engine = new ExecutionEngine(graphDb);

		ExecutionResult result;
		String raterNidValue = raterNode.getProperty("nid").toString();
		String itemNidValue = itemNode.getProperty("nid").toString();
		
		// the query matches an end-line node with specified nid and a RATING relationship to
		// an item node with specified nid
		// then, the value of the relationship (i.e., the value of the rating) is returned
		result = engine.execute ("MATCH (n:EndLine {nid:'" + raterNidValue + "'})-[r:RATING]->(m:Item {nid:'" + itemNidValue + "'}) RETURN r.value");
		
		// as the value returned by neo4j is a 'general object', a cast to string is required
		// before converting (parseInt) the value to Integer
		rating = Integer.parseInt((String) result.columnAs("r.value").next());
		
		return rating;
	}
	

}
