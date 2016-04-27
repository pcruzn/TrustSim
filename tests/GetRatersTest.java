import java.util.Iterator;

import org.junit.Test;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import cl.toeska.services.GraphService;
import cl.toeska.services.ItemService;


public class GetRatersTest {

	@Test
	public void test() {
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		
		try (Transaction tx = graphDb.beginTx()) {
			Iterable<Node> items = GraphService.getAllItemNodes(graphDb);
			Node temporal = items.iterator().next();
			System.out.println(temporal.getProperty("nid").toString());
			Iterator<Node> endLineNodesRaters = GraphService.getAllRaters(temporal, graphDb); 
			while (endLineNodesRaters.hasNext()) {
				Node temporalEndLine = endLineNodesRaters.next(); 
				System.out.println(temporalEndLine.getProperty("nid"));
				System.out.println(ItemService.getRatingOnItem(temporalEndLine, temporal, graphDb));
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
