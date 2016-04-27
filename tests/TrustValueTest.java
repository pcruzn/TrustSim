import org.junit.Test;
import cl.toeska.simulationengine.RatingGenerator;
import cl.toeska.simulationengine.TrustGenerator;


public class TrustValueTest {

	@Test
	public void test() {
		
		for (int i = 1; i<= 4000; i++)
			System.out.print(TrustGenerator.getGoodTrustValue() + "\n");
	}

}
