import static org.junit.Assert.*;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import cl.toeska.services.GraphService;


public class GetProperties {

	@Test
	public void test() {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		try (Transaction tx = graphDb.beginTx()) {
		Node node = GraphService.getAllItemNodes(graphDb).iterator().next();
		
		
		
		while(node.getPropertyKeys().iterator().hasNext()) {
		
			
			System.out.println (node.getPropertyKeys().iterator().next().toString());
			
		}
		} catch (Exception a) {
			a.printStackTrace();
		}
		
	}

}
