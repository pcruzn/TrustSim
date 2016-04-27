package cl.toeska.neo4jdb;

import org.neo4j.graphdb.RelationshipType;

public enum RelTypes implements RelationshipType{
	TRUSTS, RATING;
}