import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import cl.toeska.services.RecommenderService;


public class RecommenderServiceTest {

	@Test
	public void test() {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		
		try (Transaction tx = graphDb.beginTx()) {
			RecommenderService.getTrustBasedRecommendation(graphDb);
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}
	}
}
