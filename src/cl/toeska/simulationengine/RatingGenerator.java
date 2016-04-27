// A ratings generator; it uses the Inverse-Transform method
package cl.toeska.simulationengine;

public class RatingGenerator {
	
	public static int getGoodItemRating () {
	
		// ratings gathered from amazon.com!
		float[] ratingStarsFreq = 
			{(float) 0.027, (float) 0.031, (float) 0.075, (float) 0.186, (float) 0.681};
		

		// seed for random generator is currentTimeMillis()
		double unifRandom = Math.random();

		if (unifRandom - (ratingStarsFreq [0]) < 0)
			return 1;
		if (unifRandom - (ratingStarsFreq [0] + ratingStarsFreq [1]) < 0)
			return 2;
		if (unifRandom - (ratingStarsFreq [0] + ratingStarsFreq [1] + ratingStarsFreq [2]) < 0)
			return 3;
		if (unifRandom - (ratingStarsFreq [0] + ratingStarsFreq [1] + ratingStarsFreq [2] + ratingStarsFreq [3]) < 0)
			return 4;
		if (unifRandom - (ratingStarsFreq [0] + ratingStarsFreq [1] + ratingStarsFreq [2] + ratingStarsFreq [3] + ratingStarsFreq [4]) < 0)
			return 5;

		return 0;
			
	}
	

	public static int getRandomItemRating () {
		double unifRandom = Math.random();

		// this small method implements the inverse-transform procedure for generating
		// uniform discrete values (from 1 to 5)
		// constant values are left in 'raw' for informative purposes
		return 1 + (int) Math.floor((5-1+1)*unifRandom);
	}
}
