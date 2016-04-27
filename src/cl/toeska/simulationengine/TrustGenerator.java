package cl.toeska.simulationengine;

public class TrustGenerator {
//	public static int getGoodTrustValue () {
//		
//		float[] trustFreq = 
//			{(float) 0.013, (float) 0.013, (float) 0.026, (float) 0.051, (float) 0.128, (float) 0.128, (float) 0.154, (float) 0.256, (float) 0.154, (float) 0.077};
//		
//
//		double unifRandom = Math.random();
//
//		if (unifRandom - trustFreq [0] < 0)
//			return 1;
//		if (unifRandom - (trustFreq [0] + trustFreq [1]) < 0)
//			return 2;
//		if (unifRandom - (trustFreq [0] + trustFreq [1] + trustFreq [2]) < 0)
//			return 3;
//		if (unifRandom - (trustFreq [0] + trustFreq [1] + trustFreq [2] + trustFreq [3]) < 0)
//			return 4;
//		if (unifRandom - (trustFreq [0] + trustFreq [1] + trustFreq [2] + trustFreq [3] + trustFreq [4]) < 0)
//			return 5;
//		if (unifRandom - (trustFreq [0] + trustFreq [1] + trustFreq [2] + trustFreq [3] + trustFreq [4] + trustFreq [5]) < 0)
//			return 6;
//		if (unifRandom - (trustFreq [0] + trustFreq [1] + trustFreq [2] + trustFreq [3] + trustFreq [4] + trustFreq [5] + trustFreq [6]) < 0)
//			return 7;
//		if (unifRandom - (trustFreq [0] + trustFreq [1] + trustFreq [2] + trustFreq [3] + trustFreq [4] + trustFreq [5] + trustFreq [6] + trustFreq [7]) < 0)
//			return 8;
//		if (unifRandom - (trustFreq [0] + trustFreq [1] + trustFreq [2] + trustFreq [3] + trustFreq [4] + trustFreq [5] + trustFreq [6] + trustFreq [7] + trustFreq [8]) < 0)
//			return 9;
//		if (unifRandom - (trustFreq [0] + trustFreq [1] + trustFreq [2] + trustFreq [3] + trustFreq [4] + trustFreq [5] + trustFreq [6] + trustFreq [7] + trustFreq [8] + trustFreq [9]) < 0)
//			return 10;
//
//		return 0;
//			
//	}
	
	public static int getGoodTrustValue () {
			double unifRandom = Math.random();

			// this small method implements the inverse-transform procedure for generating
			// uniform discrete values (from 1 to 5)
			// constant values are left in 'raw' for informative purposes
			return 1 + (int) Math.floor((10-1+1)*unifRandom);
	}
	
	public static int getLinearTrustValueFromKnowObs() {
		double unifRandom = Math.random();
		int knowObs =  1 + (int) Math.floor((10-1+1)*unifRandom);
		
		// this will return the 'linearly determined' value of trust for a particular value of
		// knowledge obsolescence
		//return (-1*knowObs + 11);
		
		// for now, returning an example of linear (cubic) regression
		return (int) Math.round(-0.018*Math.pow(knowObs, 3) + 0.2418*Math.pow(knowObs, 2) - 1.5638*knowObs + 10.403);
	}
}
