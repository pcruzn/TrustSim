package cl.toeska.services;

import java.util.Iterator;

import org.neo4j.graphalgo.GraphAlgoFactory;
import org.neo4j.graphalgo.PathFinder;
import org.neo4j.graphdb.Direction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.PathExpanders;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;

import cl.toeska.neo4jdb.RelTypes;
import cl.toeska.simulationobjects.User;


public class UserService {
		
	public static float getTrustOnUser (Node startNode, Node endNode, GraphDatabaseService graphDb) {
		// by default, all users are supposed to be part of label 'users'
		float trustOnUser = 0;

		try (Transaction tx = graphDb.beginTx()) {

			// set the algorithm, direction outgoing and relationship TRUSTS, max length = 5
			PathFinder<Path> finder = GraphAlgoFactory.allSimplePaths(PathExpanders.forTypeAndDirection(RelTypes.TRUSTS, Direction.OUTGOING ), 5);
			//PathFinder<Path> finder = GraphAlgoFactory.shortestPath(PathExpanders.forTypeAndDirection(RelTypes.TRUSTS, Direction.OUTGOING ), 10);
			
			// get all paths from startNode to endNode
			// only simple paths (i.e., no loops), as set by the algorithm
			Iterable<Path> paths = finder.findAllPaths(startNode, endNode);
			
			float averageTrustPerPath = 0;
			float nPaths = 0;
			for (Iterator<Path> pathIterator = paths.iterator(); pathIterator.hasNext(); ) {
				Path path = pathIterator.next();
				//System.out.println ("Path Length: " + path.length());

				// the sum of trust values in a path
				float pathTrustValues = 0;
				// the average of trust values in the chain of relationships
				float pathTrustAverage;
				// for counting the number of relationships in a path
				int nRelationships = 0;
				
				for (Iterator<Relationship> pathRelationships = path.relationships().iterator(); pathRelationships.hasNext(); ) {
					pathTrustValues += Float.parseFloat((String) pathRelationships.next().getProperty("value"));
					nRelationships ++;
				}

				// simple average: sum of trust values in each relationship divided
				// by the number of relationships
				pathTrustAverage = (float) pathTrustValues / nRelationships;
				
				
				nPaths++;

				//System.out.println ("Average trust per path: " + pathTrustAverage);

				averageTrustPerPath += pathTrustAverage;

			}
			
			if (nPaths == 0)
				return (float)-1;

			trustOnUser = (float) averageTrustPerPath / nPaths;
			
			tx.success();
		} catch (Exception txEx) {
			txEx.printStackTrace();
		}		
		
		return trustOnUser;
	}

	public static void createStartNode (GraphDatabaseService graphDb) {
		User node = new User ();
		
		node.createStartNode(graphDb);
	
	}
	
	public static void createEndLineNodes (int nNodes, GraphDatabaseService graphDb) {
		User node = new User ();
		
		for (int userCount = 0; userCount < nNodes; userCount++) {
			node.createEndLineNode("e" + (userCount + 1), graphDb);
		}
		
	}
	
	public static void createInBetweenNodes (int nNodes, GraphDatabaseService graphDb) {
		User node = new User();
		
		for (int userCount = 0; userCount < nNodes; userCount++) {
			node.createInBetweenNode("n" + (userCount + 1), graphDb);
		}
		
	}
	
	public static void createTrustRelationship (String initNodeId, String endNodeId, String value, String initNodeLabel, String endNodeLabel, GraphDatabaseService graphDb) {
		User node = new User();
		
		node.createRelationship(true, GraphService.getNode(initNodeId, initNodeLabel, graphDb), GraphService.getNode(endNodeId, endNodeLabel, graphDb), value, graphDb);
	}
	
	public static void createKnowledgeObsolescenceRelationship (String initNodeId, String endNodeId, String value, String initNodeLabel, String endNodeLabel, GraphDatabaseService graphDb) {
		User node = new User();
		
		node.createRelationship(false, GraphService.getNode(initNodeId, initNodeLabel, graphDb), GraphService.getNode(endNodeId, endNodeLabel, graphDb), value, graphDb);
		
	}
	
}
