import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import cl.toeska.services.RecommenderService;
import cl.toeska.simulationobjects.User;


public class UserTest {

	@Test
	public void userCreationTest () {
		User randomUser = new User ();
		
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		
		for (int i = 0; i <= 100; i++) {
			randomUser.create(graphDb);
		}
		
		RecommenderService.getTrustBasedRecommendation(graphDb);

				
		graphDb.shutdown();
		
	}

}
