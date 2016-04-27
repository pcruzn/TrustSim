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
import org.neo4j.graphdb.index.Index;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.kernel.Traversal;
import org.neo4j.tooling.GlobalGraphOperations;

public class TestNeo4jPathsDirectDB {
	public static void main (String args[]) {
				
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		//registerShutdownHook( graphDb );
		
		Node firstNode;
		Node fourthNode;
	
		try ( Transaction tx = graphDb.beginTx() )
		{
			//Iterable<Node> nodes = GlobalGraphOperations.at(graphDb).getAllNodes();
			
						
			firstNode = graphDb.findNodesByLabelAndProperty(DynamicLabel.label("users"), "nid", "1").iterator().next();
			fourthNode = graphDb.findNodesByLabelAndProperty(DynamicLabel.label("users"), "nid", "4").iterator().next();		
			/*Iterator<Node> actualNode = nodes.iterator();
			firstNode = actualNode.next();
			System.out.println(firstNode.getProperty("nid"));
			actualNode.next();
			actualNode.next();
			fourthNode = actualNode.next();
			System.out.println(fourthNode.getProperty("nid"));*/
				
			/*
			Index<Node> idIndex = graphDb.index().forNodes("identifiers");
			firstNode = idIndex.get("nid", "1").getSingle();
						
			fourthNode = idIndex.get("nid", "4").getSingle();
			
			*/
						
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
