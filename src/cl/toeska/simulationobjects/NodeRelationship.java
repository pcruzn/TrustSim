package cl.toeska.simulationobjects;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import cl.toeska.neo4jdb.RelTypes;

public class NodeRelationship {
	
	public static void createTrustRelationship (Node firstNode, Node secondNode, int trustValue, GraphDatabaseService graphDb) {
		
		Relationship relationship;
		
		try (Transaction tx = graphDb.beginTx()) {
			
			relationship = firstNode.createRelationshipTo(secondNode, RelTypes.TRUSTS);
			relationship.setProperty("trustValue", trustValue);
			
			tx.success();
		}
		
	}

}
