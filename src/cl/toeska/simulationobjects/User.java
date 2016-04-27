package cl.toeska.simulationobjects;

import java.util.Random;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;


import cl.toeska.neo4jdb.RelTypes;
import cl.toeska.services.GraphService;
import cl.toeska.sqlitedb.MainDB;


public class User {
	private int userId;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}
	
	public void create (GraphDatabaseService graphDb) {
		Random randZ = new Random ();
		// append an U to the random generated user
		String userId = "U" + String.valueOf(randZ.nextInt());
		
		// first, we insert user in sql db
		String userUpdateString = 
				"INSERT INTO user VALUES ('" + userId + "')";
		MainDB.dbUpdate(userUpdateString);
		
		// second, we create the node in the graph db
		//GraphService.insertSimNode("nid", userId, graphDb);
	}
	
	public void createEndLineNode (String nodeId, GraphDatabaseService graphDb) {
		
		GraphService.insertSimNode("nid", nodeId, "EndLine", graphDb);
		
	}
	
	// property "nid" has value nodeId (n
	public void createInBetweenNode (String nodeId, GraphDatabaseService graphDb) {
		
		GraphService.insertSimNode("nid", nodeId, "InBetween", graphDb);
		
	}
	
	// property "nid" has fixed value i1 for the only one initial node 1
	public void createStartNode (GraphDatabaseService graphDb) {
		
		GraphService.insertSimNode("nid", "i1", "Start",graphDb);
		
	}
	
	public void createRelationship (boolean relKind, Node initNode, Node endNode, String value, GraphDatabaseService graphDb) {
		// relKind defines what kind of relationship will be created
		// true for trust
		// false for knowledge obsolescence
		Relationship newRelationship;
		
		try (Transaction tx = graphDb.beginTx()) {
			if (relKind) {
				// relKind is true
				
				newRelationship = initNode.createRelationshipTo(endNode, RelTypes.TRUSTS);
				newRelationship.setProperty("value", value);
				
				tx.success();
				
			} else {
				// relKind is false
				
				//newRelationship = initNode.createRelationshipTo(endNode, RelTypes.KNOWOBS);
				//newRelationship.setProperty("value", value);
				
				//tx.success();
			
			}
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}
		
	}

	
}
