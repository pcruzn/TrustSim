import static org.junit.Assert.*;

import java.util.Iterator;

import org.junit.Test;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.tooling.GlobalGraphOperations;

import cl.toeska.neo4jdb.RelTypes;
import cl.toeska.services.ItemService;
import cl.toeska.services.RecommenderService;
import cl.toeska.services.RelationshipService;
import cl.toeska.services.UserService;


public class GetUsersTest {

	@Test
	public void test() {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		
		UserService.createEndLineNodes(2, graphDb);
		UserService.createInBetweenNodes(2, graphDb);
		UserService.createStartNode(graphDb);
		ItemService.createItemsNodes(3, graphDb);
		
		//RelationshipService.simulateRelationships(0, 0, graphDb);
		
		//RatingRelationshipService.simulateRelationships(0.4, 0.7, graphDb);
				
		ExecutionEngine engine = new ExecutionEngine(graphDb);

		ExecutionResult result;
		try (Transaction ignored = graphDb.beginTx())
		{
			result = engine.execute("MATCH (n) RETURN n");

//			for (Iterator<Node> n_column = result.columnAs("n"); n_column.hasNext();) {
//				
//				System.out.println(n_column.next().getProperty("nid"));
//				
//			}
//			for (Iterator<Relationship> it = GlobalGraphOperations.at(graphDb).getAllRelationships().iterator(); it.hasNext(); ) {
//
//				Relationship rel = it.next();
//				if (rel.isType(RelTypes.TRUSTS))
//					System.out.println ("trust: " + rel.getProperty("value").toString());
//				else if (rel.isType(RelTypes.RATING))
//					System.out.println ("rating: " + rel.getProperty("value").toString());
//			}
		} catch (Exception e) {
			e.printStackTrace();
			fail ("Failed!");
		}
		
		RecommenderService.getTrustBasedRecommendation(graphDb);
		
		graphDb.shutdown();
	}

}
