package io.github.benas.jpopulator.randomizers;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;

public class NumericStringRandomizerTest {
	private NumericStringRandomizer numericStringRandomizer;

    @Before
    public void setUp() throws Exception {
        numericStringRandomizer = new NumericStringRandomizer(1,3);
    }

    @org.junit.Test
    public void generatedStringShouldBeNumber() throws Exception {
        try {
        	Integer.parseInt(numericStringRandomizer.getRandomValue());
        }
        catch (NumberFormatException e){
        	Assert.fail("Numeric string randomizer should generate number");
        }
    }
    
    @org.junit.Test
    public void generatedNumericStringShouldWithinRange() throws Exception {
    	String generatedNumericString = numericStringRandomizer.getRandomValue();
    	Assert.assertTrue(generatedNumericString.equals("1") || generatedNumericString.equals("2") || generatedNumericString.equals("3"));
    }

    @After
    public void tearDown() throws Exception {
    	numericStringRandomizer = null;
        System.gc();
    }
}
