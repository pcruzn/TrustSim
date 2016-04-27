package cl.toeska.simulationobjects;

import java.util.Random;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import cl.toeska.neo4jdb.RelTypes;
import cl.toeska.services.GraphService;
import cl.toeska.sqlitedb.MainDB;

public class Item {
	private int itemId;

	public int getItemId() {
		return itemId;
	}

	public void setItemId(int itemId) {
		this.itemId = itemId;
	}

	public void insertItemDb () {
		Random randZ = new Random ();
		
		// append an I to the random generated itemId
		String itemUpdateString = 
				"INSERT INTO item VALUES ('I" + String.valueOf(randZ.nextInt()) + "')";
		MainDB.dbUpdate(itemUpdateString);
	}
	
	public void createItemNode (String nodeId, GraphDatabaseService graphDb) {
		
		GraphService.insertSimNode("nid", nodeId, "Item", graphDb);
		
	}
	
	public void createRatingRelationship (Node initNode, Node endNode, String value, GraphDatabaseService graphDb) {
		Relationship newRelationship;
		
		try (Transaction tx = graphDb.beginTx()) {

				newRelationship = initNode.createRelationshipTo(endNode, RelTypes.RATING);
				newRelationship.setProperty("value", value);
				
				tx.success();
				
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}
	}
		
}
