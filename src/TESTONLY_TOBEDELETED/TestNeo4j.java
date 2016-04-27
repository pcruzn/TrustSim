package TESTONLY_TOBEDELETED;

import java.util.Iterator;
import org.neo4j.cypher.javacompat.ExecutionEngine;
import org.neo4j.cypher.javacompat.ExecutionResult;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

public class TestNeo4j {
	public static void main (String args[]) {
				
		GraphDatabaseService graphDb = new GraphDatabaseFactory().newEmbeddedDatabase("GRAPHDB.db");
		//registerShutdownHook( graphDb );
		
		Node firstNode;
		Node secondNode;
		Relationship relationship;
		
		/*try ( Transaction tx = graphDb.beginTx() )
		{
			// prueba mia
		
			for (int i = 0; i<=200; i++ ) {
				firstNode = graphDb.createNode();
				firstNode.setProperty("nombre", "Pablo Cruz Navea");
					
				secondNode = graphDb.createNode();
				secondNode.setProperty("nombre", "BWV" + i);
			
				relationship = firstNode.createRelationshipTo(secondNode, RelTypes.TRUSTS);
				relationship.setProperty("trustValue", 8);
			
				tx.success();
			}
			
			// Database operations go here
			/*firstNode = graphDb.createNode();
			firstNode.setProperty( "message", "Hello, " );
			secondNode = graphDb.createNode();
			secondNode.setProperty( "message", "World!" ); */

			/*relationship = firstNode.createRelationshipTo( secondNode, RelTypes.KNOWS );
			relationship.setProperty( "message", "brave Neo4j " );
			*/
			
			//System.out.print(firstNode.getProperty("message"));
			//System.out.print(relationship.getProperty("message"));
			//System.out.print(secondNode.getProperty("message"));
			
		    //tx.success();
		//}
		
		ExecutionEngine engine = new ExecutionEngine(graphDb);

		ExecutionResult result;
		try (Transaction ignored = graphDb.beginTx())
		{
		    //result = engine.execute( "match (n {nombre: 'BWV100'}) return n, n.nombre" );
			result = engine.execute("MATCH (n) RETURN n");
		
			
			for (Iterator<Node> n_column = result.columnAs("n"); n_column.hasNext();) {
				
				System.out.println(n_column.next().getProperty("id"));
				
			}
			
			/*
			for (Node node : IteratorUtil.asIterable(n_column))
			{
			    // note: we're grabbing the name property from the node,
			    // not from ls
			     * the n.name in this case.
			    String nodeResult = "Nombre del nodo: " + node.getProperty("id");
			   			    
			    System.out.println (nodeResult);
			}*/
		}
		graphDb.shutdown();	
	}
}
