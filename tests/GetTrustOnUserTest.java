import org.junit.Test;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import cl.toeska.services.UserService;


public class GetTrustOnUserTest {

	@Test
	public void test() {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		
		try (Transaction tx = graphDb.beginTx()) {
			Node startNode = 
					graphDb.findNodesByLabelAndProperty(
							DynamicLabel.label("Start"),
							"nid", "i1").iterator().next();
			Node endNode = 
			graphDb.findNodesByLabelAndProperty(
					DynamicLabel.label("EndLine"),
					"nid", "e2").iterator().next();
			System.out.println(UserService.getTrustOnUser(startNode, endNode, graphDb));
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}
	}

}
