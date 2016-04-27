import static org.junit.Assert.fail;

import org.junit.Test;

import cl.toeska.simulationengine.TrustGenerator;


public class KnowObsTrustTest {

	@Test
	public void testGetLinearTrustValueFromKnowObs() {
		int value;
		for (int i = 1; i <= 1000; i++) {
			value = TrustGenerator.getLinearTrustValueFromKnowObs();
			System.out.println ();
			if (value > 10 || value < 1)
				fail ("not a valid trust value!");
		}
	}

}
