package cl.toeska.services;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Iterator;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

public class RecommenderService {
	
	public static void writeResultsToFile (int totalItems, String meanWRatings, String fileName) {
		
		try {
			 
			String content = totalItems + " - " + meanWRatings + "\n";

			File reportFile = new File("results/" + fileName);

			
			if (!reportFile.exists()) {
				reportFile.createNewFile();
			}

			FileWriter fw = new FileWriter(reportFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

			System.out.println("Summarized Data Written!");

		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void writeAllItemsToFile (String ratingForItem, int nRaters, String fileName) {
		try {
			 
			String content = ratingForItem + " - " + nRaters + "\n";

			File reportFile = new File("results/" + fileName);

			
			if (!reportFile.exists()) {
				reportFile.createNewFile();
			}

			FileWriter fw = new FileWriter(reportFile.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.close();

		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
	
	public static void getTrustBasedRecommendation (GraphDatabaseService graphDb, int iteration) {
		// these two flags mean:
		// noTrustOnRater = true: there's no path between the source node (Start) and the rater node,
		// thus no trust relationship exists
		// noInvitedRater = ture: the source node (Start) trusts to this rater node, however the trust
		// level is below the threshold. therefore, this rater is not invited to join the recommendation session
		// IMPORTANT: as for now, both attributes work only as "we have at least one trusted rater and at least
		// one invited rater)
		boolean noTrustOnRater = true;
		boolean noInvitedRater = true;
		float totalRating = 0;
		int totalItems = 0;
		
		try (Transaction tx = graphDb.beginTx()) {
			Iterable<Node> allItems = GraphService.getAllItemNodes(graphDb);
			
			// actual rating (considering only invited raters)
			float actualRating;
			
			
			// to account for how many raters are considered in the recommendation session
			// this attribute is equivalent to say "how many raters with trust over a define threshold are"
			// but much more simple
			int nRaters;
			
			
			
			// for each item, get all raters, and work with ratings from raters
			for (Iterator<Node> itemIterator = allItems.iterator(); itemIterator.hasNext(); ) {
				actualRating = 0;
				nRaters = 0;
				
				Node actualItem = itemIterator.next();
				
				Iterator<Node> ratersIterator = GraphService.getAllRaters(actualItem, graphDb);
				
				// to ensure we do not advance the iterator, this copy of the ratersIterator attribute
				// is used to check if there is at least one rater (if not, continue is used)
				// this attribute may be ommited if after intensive testing results are consistent
				Iterator<Node> ratersIteratorForChecking = ratersIterator;
				
				
				// if no raters for the item, then skip to the next iteration
				if (!ratersIteratorForChecking.hasNext())
					continue;
				
				for (; ratersIterator.hasNext(); ) {
					Node actualRaterNode = ratersIterator.next();
					
					// get trust on rater from Start node (i1) (i.e., source node is the Start node
					// and the sink node is the actualRaterNode which is in this iteration)
					float trustOnRater = UserService.getTrustOnUser(GraphService.getNode("i1", "Start", graphDb), actualRaterNode, graphDb);
					//System.out.println(actualRaterNode.getProperty("nid") +" "+ trustOnRater);
					//if (trustOnRater + 1.0 > 0) {
					if (trustOnRater != (float)-1) {
						// trust on rater exists!
						noTrustOnRater = false;
						
						if (trustOnRater - 5.0 >= 0) {
							// rater has been invited to the recommendation session!
							noInvitedRater = false;
							
							float ratingOnItem = (float)ItemService.getRatingOnItem(actualRaterNode, actualItem, graphDb);
							//System.out.println ("Trust on user: " + actualRaterNode.getProperty("nid") + " " + trustOnRater);
							actualRating += ratingOnItem;
							
							nRaters++;
							//System.out.println(nRaters);
						}
					} 
					
					
				
				}
				
				// if there are at leat one trusted rater and at least one invited rater, we print the following:
				// weighted rating: simple mean of rating by each raters over the number of raters
				if (!noTrustOnRater && !noInvitedRater) {
					float wRating = actualRating/((float)nRaters);
					//System.out.println (/*"WRATING: " + */new DecimalFormat("#.##").format(wRating));
					writeAllItemsToFile (new DecimalFormat("#.##").format(wRating), nRaters, "file" + iteration + ".txt");
					totalRating += wRating;
					totalItems++;
				}
				
				// the two flags are re-seted for a new iteration
				noTrustOnRater = true;
				noInvitedRater = true;
							
				
				// for each rater
				// get the trust on rater (where the source node is the start node)
				// get the rating the rater has done to the item
				// then get the weighted trust x 
			}			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// at the end, we print the mean for all the weighted ratings
		//System.out.println (new DecimalFormat("#.##").format(totalRating/((float)totalItems)));

		if (totalItems > 0)
			writeResultsToFile (totalItems, new DecimalFormat("#.##").format(totalRating/((float)totalItems)), "file.txt");
		
		// getRaters (item);
		
		// getTrustOn (rater); // infer with KO when needed
		
		// compute weightedRating
		
		/* for user in Users
		 * 		trustbasedrating 
		 */
		
	
		
		//== (sumTrust x Ratings) / sumTrusts
		

		
		//return null;
		

		
	}
	
	// overloading getTrustBasedRecommendations is used to maintain compatibility with
	// old declaration (specially in case of unit tests)
	public static void getTrustBasedRecommendation (GraphDatabaseService graphDb) {
		getTrustBasedRecommendation(graphDb, 0);
	}
}
