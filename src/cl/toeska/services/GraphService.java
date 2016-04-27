package cl.toeska.services;

import java.util.Iterator;

import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.tooling.GlobalGraphOperations;

public class GraphService {
	
	public static Iterator<Node> getAllUsers (GraphDatabaseService graphDb) {
		ExecutionEngine engine = new ExecutionEngine(graphDb);

		ExecutionResult result;

		// select all users!
		result = engine.execute("MATCH (n) RETURN n");

		// returns an iterator object with all users
		return result.columnAs("n");

		
	}
	
	public static void insertSimNode (String propertyKey, String propertyValue, String label, GraphDatabaseService graphDb) {
		Node userNode;
		
		// simple transaction: create the simulated node
		try (Transaction tx = graphDb.beginTx()) {
			
			userNode = graphDb.createNode();
			userNode.addLabel(DynamicLabel.label(label));
			userNode.setProperty(propertyKey, propertyValue);			
			
			tx.success();
						
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}
		
	}
	
	public static Node getNode (String keyValue, String label, GraphDatabaseService graphDb) {
		Node node = null;
		
		try (Transaction tx = graphDb.beginTx()) {
			// as no nodes can have the same keyValue, the node to be returnes is the
			// first one in the Iterable object
			node = graphDb.findNodesByLabelAndProperty(DynamicLabel.label(label), "nid", keyValue).iterator().next();
		
			tx.success();
			// if the node is not found, null is returned
			return node;
		} catch (Exception txEx) {
			txEx.printStackTrace();
			return null;
		}
	}
	
	public static Iterable<Node> getAllInBetweenNodes (GraphDatabaseService graphDb) {
		Iterable<Node> inBetweenNodes = null;
		
		try (Transaction tx = graphDb.beginTx()) {
			
			inBetweenNodes = GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(DynamicLabel.label("InBetween"));
			tx.success();
			
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}
		
		
		return inBetweenNodes;
	}
	
	public static Iterable<Node> getAllEndLineNodes (GraphDatabaseService graphDb) {
		Iterable<Node> endLineNodes = null;
		
		try (Transaction tx = graphDb.beginTx()) {
			
			endLineNodes = GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(DynamicLabel.label("EndLine"));
			tx.success();
			
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}
		
		
		return endLineNodes;
	}
	
	public static Iterable<Node> getAllNodes (GraphDatabaseService graphDb) {
		Iterable<Node> allNodes = null;
		
		try (Transaction ignored = graphDb.beginTx()) {
			
			allNodes = GlobalGraphOperations.at(graphDb).getAllNodes();
			
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}
		
		
		return allNodes;
	}
	
	public static Iterable<Node> getAllItemNodes (GraphDatabaseService graphDb) {
		Iterable<Node> allItemNodes = null;
		
		try (Transaction tx = graphDb.beginTx()) {
			
			allItemNodes = GlobalGraphOperations.at(graphDb).getAllNodesWithLabel(DynamicLabel.label("Item"));
			tx.success();
			
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}
		
		
		return allItemNodes;
	}
	
	
	public static Iterator<Node> getAllRaters (Node itemNode, GraphDatabaseService graphDb) {
		ExecutionEngine engine = new ExecutionEngine(graphDb);

		ExecutionResult result;
		String nidValue = itemNode.getProperty("nid").toString();
		
		result = engine.execute ("MATCH (n:EndLine)-[:RATING]->(m:Item {nid:'" + nidValue + "'}) RETURN n");
		
		// returns an iterator object with all users
		return result.columnAs("n");
	}
	
}
