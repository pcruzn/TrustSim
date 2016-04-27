package TESTONLY_TOBEDELETED;

import java.util.Iterator;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.DynamicLabel;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class TestNeo4jPaths {
	public static void main (String args[]) {
				
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		//registerShutdownHook( graphDb );
		
		Node firstNode;
		Node secondNode;
		Node thirdNode;
		Node fourthNode;
		Relationship relationship;
		
		try ( Transaction tx = graphDb.beginTx() )
		{
			Label nodesLabel = DynamicLabel.label("users");
			// prueba mia
			firstNode = graphDb.createNode();
			firstNode.setProperty("nid", "1");
			firstNode.addLabel(nodesLabel);
			
			secondNode = graphDb.createNode();
			secondNode.setProperty("nid", "2");
			secondNode.addLabel(nodesLabel);
			
			thirdNode = graphDb.createNode();
			thirdNode.setProperty("nid", "3");
			thirdNode.addLabel(nodesLabel);
			
			fourthNode = graphDb.createNode();
			fourthNode.setProperty("nid", "4");
			fourthNode.addLabel(nodesLabel);
		
			relationship = firstNode.createRelationshipTo(secondNode, RelTypes.TRUSTS);
			relationship.setProperty("trustValue", 6);
			
			relationship = secondNode.createRelationshipTo(thirdNode, RelTypes.TRUSTS);
			relationship.setProperty("trustValue", 5);
			
			relationship = firstNode.createRelationshipTo(thirdNode, RelTypes.TRUSTS);
			relationship.setProperty("trustValue", 8);
			
			relationship = firstNode.createRelationshipTo(fourthNode, RelTypes.TRUSTS);
			relationship.setProperty("trustValue", 7);
			
			relationship = thirdNode.createRelationshipTo(fourthNode, RelTypes.TRUSTS);
			relationship.setProperty("trustValue",  1);
			
			
			
			PathFinder<Path> finder = GraphAlgoFactory.allSimplePaths(PathExpanders.forTypeAndDirection(RelTypes.TRUSTS, Direction.OUTGOING ), 4);
		
			Iterable<Path> paths = finder.findAllPaths(firstNode, fourthNode);
			
			float averageTrust;
			float averageTrustPerPath = 0;
			float nPaths = 0;
			for (Iterator<Path> pathIterator = paths.iterator(); pathIterator.hasNext(); ) {
				Path path = pathIterator.next();
				System.out.println ("Path Length: " + path.length());
				
				float pathTrustValues = 0;
				float pathTrustAverage;
				int nRelationships = 0;
				for (Iterator<Relationship> pathRelationships = path.relationships().iterator(); pathRelationships.hasNext(); ) {
					pathTrustValues += (int) pathRelationships.next().getProperty("trustValue");
					nRelationships ++;
				}
				
				pathTrustAverage = (float) pathTrustValues / nRelationships;
				nPaths++;
				
				System.out.println ("Average trust per path: " + pathTrustAverage);
				
				averageTrustPerPath += pathTrustAverage;
				
			}
			
			averageTrust = (float) averageTrustPerPath / nPaths;
			
			System.out.println ("Average Trust: " + averageTrust);
			
			tx.success();
		
		}
		

		graphDb.shutdown();	
	}
}
