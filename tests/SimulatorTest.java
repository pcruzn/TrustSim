import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import cl.toeska.services.ItemService;
import cl.toeska.services.RecommenderService;
import cl.toeska.services.RelationshipService;
import cl.toeska.services.UserService;


public class SimulatorTest {

	@Test
	public void test() {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		
//		UserService.createEndLineNodes(3, graphDb);
//		UserService.createInBetweenNodes (3, graphDb);
//		UserService.createStartNode(graphDb);
//		ItemService.createItemsNodes(3, graphDb);
//
//		RelationshipService.simulateTrustRelationships(0.6, 0.4, 0.3, 0.4,  graphDb);
//		
//		
//		RelationshipService.simulateRatingRelationships(0.5, 0.8, graphDb);
				
		RecommenderService.getTrustBasedRecommendation(graphDb);
		
		graphDb.shutdown();

	}

}
