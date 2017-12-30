package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import die.Die;
import die.Side;

public class DieTest {
	Die mistborn;

	@Before
	public void setUp() throws Exception {
		Side[] d6 = {new Side("one", "1/6"), new Side("two", "1/6"), new Side("three", "1/6")
				, new Side("four", "1/6"), new Side("five", "1/6"), new Side("six", "1/6")};
		String[] allResultName = {"PairOfOnes", "PairOfTwos", "PairOfThrees", "PairOfFours"
				, "PairOfFives", "Nudge"};
		String[] sideName = {"two", "three", "four", "five", "six", "one"};
		int[] sideAmount = {2, 2, 2, 2, 1, 2};
		String[] resultName = {"PairOfTwos", "PairOfThrees", "PairOfFours"
				, "PairOfFives", "Nudge", "PairOfOnes"};
		int[] resultAmount = {1, 1, 1, 1, 1, 1};
		mistborn = new Die("mistborn", d6, allResultName, sideName, sideAmount, resultName, resultAmount);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testTranslate() {
		int[] singleRoll = {2, 0, 3, 0, 2, 3};
		int[] singleResultTest = mistborn.translate(singleRoll);
		int[] singleResult = {1, 0, 1, 0, 1, 3};
		for(int i=0; i<singleResult.length; i++){
			assertEquals(singleResult[i], singleResultTest[i]);
		}
	}

}
