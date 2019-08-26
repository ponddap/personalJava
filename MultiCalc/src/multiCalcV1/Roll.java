package multiCalcV1;

import java.math.BigDecimal;
import java.util.Arrays;
import die.Die;
import die.Side;
import result.Result;

public class Roll {
	private Die die;
	private int numberRolled;
	private int[][] multi;
	// Look into using log probability to make this easier (log_2(X*Y) = log_2(X) + log_2(Y))
	private BigDecimal[] prob;

	public Roll(int numberRolled, Die die) {
		this.numberRolled = numberRolled;
		this.die = die;

		int possNum = die.getNumPoss();
		int terms = calcTerms(numberRolled, possNum);

		this.multi = new int[terms][possNum];
		this.prob = new BigDecimal[terms];

		expandRoll(terms);
	}

	/**
	 * This method does most of the work setting up what every possible roll
	 * could look like, and the probability of it actually happening.
	 * 
	 * @param terms
	 *            -- how many possible rolls there could be
	 */
	private void expandRoll(int terms) {
		int fill[] = new int[die.getNumPoss()];
		Side[] outcomes = die.getSides();
		Arrays.fill(fill, 0);
		fill[0] = numberRolled;
		int index = 0;
		int curentPower = 0;
		BigDecimal coefNum = calcFact(numberRolled);
		BigDecimal coefDen = BigDecimal.ONE;
		BigDecimal coef = BigDecimal.ONE;
		BigDecimal totalProb = BigDecimal.ONE;
		boolean loop = false;

		for (int i = 0; i < terms; i++) {
			// store the values of the fill array in the multi
			// array
			for (int j = 0; j < outcomes.length; j++) {
				int value = fill[j];
				multi[i][j] = value;
			}

			// calculate the probability of each outcome
			coefDen = BigDecimal.ONE;
			totalProb = BigDecimal.ONE;
			for (int j = 0; j < outcomes.length; j++) {
				coefDen = coefDen.multiply(calcFact(fill[j]));
				totalProb = totalProb.multiply(outcomes[j].getChance().pow(fill[j]));
			}
			coef = coefNum.divide(coefDen);
			prob[i] = totalProb.multiply(coef);

			// calculate the power of each variable in the next term in the
			// multinomial
			if (fill[outcomes.length - 1] < numberRolled) {
				index = 0;
				do {
					if (fill[index] > 0) {
						loop = false;
					} else {
						index++;
						loop = true;
					}
				} while (loop);
			}
			fill[index + 1]++;
			fill[index] = 0;
			curentPower = 0;
			for (int j = 0; j < outcomes.length; j++) {
				curentPower += fill[j];
			}
			fill[0] = numberRolled - curentPower;
		}
	}

	private BigDecimal calcFact(int number) {
		BigDecimal value = BigDecimal.ONE;
		BigDecimal count = BigDecimal.ONE;

		for (int i = 1; i <= number; i++) {
			value = value.multiply(count);
			count = count.add(BigDecimal.ONE);
		}
		return value;
	}

	private int calcTerms(int power, int outcomes) {
		BigDecimal num = calcFact(power + outcomes - 1);
		BigDecimal den = calcFact(power).multiply(calcFact(outcomes - 1));
		BigDecimal terms = num.divide(den);
		return terms.intValue();
	}

	/**
	 * @param match
	 * @return the index of the int[] match in the multi[][] of the roll if it
	 *         is in the array, using a binary search algorithm
	 */
	private int getSingleRollIndex(int[] match) {
		int hi = multi.length - 1;
		int low = 0;
		while (low <= hi) {
			int mid = low + (hi - low) / 2;
			int comp = compareSingleRollIndex(match, multi[mid]);
			if (comp < 0)
				hi = mid - 1;
			if (comp > 0)
				low = mid + 1;
			if (comp == 0)
				return mid;
		}
		return -1;
	}

	private int compareSingleRollIndex(int[] roll1, int[] roll2) {
		for (int i = roll1.length-1; i > -1; i--) {
			int comp = roll1[i] - roll2[i];
			if(comp != 0) return comp;
		}
		return 0;
	}

	public void change(String from, String to) {
		change(numberRolled, from, to);
	}

	/**
	 * @param upto
	 *            -- change up to so many numbers of sides
	 * @param from
	 *            -- side being changed
	 * @param to
	 *            -- side that die are being changed to
	 */
	public void change(int upto, String from, String to) {
		int indexFrom = die.sideIndexFromString(from);
		int indexTo = die.sideIndexFromString(to);
		int[] multiClone;
		int numChanged;
		int newIndex;
		BigDecimal probHolder;
		BigDecimal[] newProb = new BigDecimal[prob.length];
		for (int i = 0; i < newProb.length; i++) {
			newProb[i] = BigDecimal.ZERO;
		}

		for (int i = 0; i < multi.length; i++) {
			if (multi[i][indexFrom] > 0 && !prob[i].equals(BigDecimal.ZERO)) {
				multiClone = multi[i].clone();
				numChanged = Math.min(multiClone[indexFrom], upto);
				multiClone[indexFrom] -= numChanged;
				multiClone[indexTo] += numChanged;
				newIndex = getSingleRollIndex(multiClone);
				probHolder = prob[i];
				prob[i] = BigDecimal.ZERO;
				newProb[newIndex] = newProb[newIndex].add(probHolder);
			}
		}
		for (int i = 0; i < prob.length; i++) {
			prob[i] = prob[i].add(newProb[i]);
		}
	}

	public void change(String[] from, String to) {
		change(numberRolled, from, to);
	}

	/**
	 * @param upto
	 *            -- change up to so many numbers of sides
	 * @param from
	 *            -- sides being changed
	 * @param to
	 *            -- side that die are being changed to
	 */
	public void change(int upto, String[] from, String to) {
		int[] indexFrom = new int[from.length];
		for (int i = 0; i < from.length; i++) {
			indexFrom[i] = die.sideIndexFromString(from[i]);
		}
		int indexTo = die.sideIndexFromString(to);
		int[] multiClone;
		int[] numChanged = new int[indexFrom.length];
		int changedSoFar;
		int newIndex;
		BigDecimal probHolder;
		BigDecimal[] newProb = new BigDecimal[prob.length];
		for (int i = 0; i < newProb.length; i++) {
			newProb[i] = BigDecimal.ZERO;
		}
		boolean somethingToChange;
		for (int i = 0; i < multi.length; i++) {
			somethingToChange = false;
			// check if there is something to change
			if (!prob[i].equals(BigDecimal.ZERO)) {
				for (int j = 0; j < indexFrom.length; j++) {
					if (multi[i][indexFrom[j]] > 0) {
						somethingToChange = true;
						break;
					}
				}
			}
			// only look at changing things if there is something to change
			if (somethingToChange) {
				changedSoFar = 0;
				multiClone = multi[i].clone();
				Arrays.fill(numChanged, 0);
				for (int j = 0; j < indexFrom.length; j++) {
					if (multiClone[indexFrom[j]] > 0 && changedSoFar < upto) {
						numChanged[j] = Math.min(upto - changedSoFar, multiClone[indexFrom[j]]);
						changedSoFar += numChanged[j];
					}
				}
				for (int j = 0; j < indexFrom.length; j++) {
					if (numChanged[j] > 0)
						multiClone[indexFrom[j]] -= numChanged[j];
				}
				multiClone[indexTo] += changedSoFar;
				newIndex = getSingleRollIndex(multiClone);
				probHolder = prob[i];
				prob[i] = BigDecimal.ZERO;
				newProb[newIndex] = newProb[newIndex].add(probHolder);
			}
		}
		for (int i = 0; i < prob.length; i++) {
			prob[i] = prob[i].add(newProb[i]);
		}
	}

	public void reroll(String[] rerolled) {
		reroll(numberRolled, rerolled);
	}

	/**
	 * @param upto
	 *            -- re-roll up to this many dice
	 * @param rerolled
	 *            -- names of sides to be re-rolled
	 */
	public void reroll(int upto, String[] rerolled) {
		int indexMulti;
		Roll reRoll;
		BigDecimal[] newProb = new BigDecimal[prob.length];
		for (int i = 0; i < newProb.length; i++) {
			newProb[i] = BigDecimal.ZERO;
		}
		int[] indexRerolled = new int[rerolled.length];
		int numRerolled;
		int changeUpto;
		int[] multiClone;
		int[] rerolledMulti = new int[die.getNumPoss()];
		for (int i = 0; i < rerolled.length; i++) {
			indexRerolled[i] = die.sideIndexFromString(rerolled[i]);
		}
		for (int i = 0; i < multi.length; i++) {
			numRerolled = 0;
			multiClone = multi[i].clone();
			for (int index : indexRerolled) {
				if (numRerolled < upto) {
					changeUpto = Math.min(upto, multiClone[index]);
					numRerolled += changeUpto;
					multiClone[index] -= changeUpto;
				}
			}
			if (numRerolled > 0) {
				// reDistribute probability based on reroll
				reRoll = new Roll(numRerolled, die);
				for (int j = 0; j < reRoll.multi.length; j++) {
					for (int k = 0; k < rerolledMulti.length; k++) {
						rerolledMulti[k] = multiClone[k] + reRoll.multi[j][k];
					}
					indexMulti = getSingleRollIndex(rerolledMulti);
					newProb[indexMulti] = newProb[indexMulti].add(prob[i].multiply(reRoll.prob[j]));
				}
				prob[i] = BigDecimal.ZERO;
			}
		}
		for (int i = 0; i < prob.length; i++) {
			prob[i] = prob[i].add(newProb[i]);
		}
	}
	
	public Result genResult(){
		return new Result(die, prob, multi);
	}
	
	//TODO for Ben numbers -- remove when done
	public double avgDam(int[] mult){
		double dam = 0;
		int damage = 0;
		for(int i=0; i<multi.length; i++){
			damage = 0;
			for(int j=0; j<mult.length; j++){
				damage += multi[i][j]*mult[j];
			}
			dam += damage*prob[i].doubleValue();
		}
		
		return dam;
	}

	// TODO for test -- remove when done
	public void printTable() {
		Side[] printList = die.getSides();
		BigDecimal totalProb = BigDecimal.ZERO;
		System.out.printf("%10s ", "Chance");
		for (int j = 0; j < printList.length; j++) {
			System.out.printf("%10s ", printList[j].getName());
		}
		System.out.println();
		for (int i = 0; i < multi.length; i++) {
			System.out.printf("%10f ", prob[i].doubleValue());
			totalProb = totalProb.add(prob[i]);
			for (int j = 0; j < multi[i].length; j++) {
				System.out.printf("%10d ", multi[i][j]);
			}
			System.out.println();
		}
		System.out.printf("Total Prob: %-20f%n", totalProb.doubleValue());
		System.out.println("Number of terms: " + multi.length);
	}
	

	public static void main(String[] args){
//		Side[] xRed = {new Side("hit", "1/2"), new Side("focus", "1/4"), new Side("blank", "1/4")};
//		String[] allResultName = {"hit"};
//		String[] sideName = {"hit"};
//		int[] sideAmount = {1};
//		String[] resultName = {"hit"};
//		int[] resultAmount = {1};
//		Die xWingRed = new Die("xWingRed", xRed, allResultName, sideName, sideAmount, resultName, resultAmount);
		
//		Roll threeRed = new Roll(3, xWingRed);
//		String[] test = {"blank", "focus"};
//		threeRed.reroll(test);
//		Result threeAtk = threeRed.genResult();
//		threeAtk.printResult();
		
//		Side[] d6 = {new Side("one", "1/6"), new Side("two", "1/6"), new Side("three", "1/6")
//				, new Side("four", "1/6"), new Side("five", "1/6"), new Side("six", "1/6")};
//		String[] allResultName0 = {"total"};
//		String[] sideName0 = {"one", "two", "three", "four", "five", "six"};
//		int[] sideAmount0 = {1,1,1,1,1,1};
//		String[] resultName0 = {"total", "total", "total", "total", "total", "total"};
//		int[] resultAmount0 = {1,2,3,4,5,6};
//		Die dsix = new Die("d6", d6, allResultName0, sideName0, sideAmount0, resultName0, resultAmount0);
//		
//		Side[] aRed = {new Side("doubleHit", "1/8"), new Side("hit", "1/4"), new Side("crit", "1/4")
//				, new Side("accuracy", "1/8"), new Side("blank", "1/4")};
//		String[] allResultName1 = {"damage", "accuracy", "critEffect"};
//		String[] sideName1 = {"doubleHit", "hit", "crit", "crit", "accuracy"};
//		int[] sideAmount1 = {1,1,1,1,1};
//		String[] resultName1 = {"damage", "damage", "damage", "critEffect", "accuracy"};
//		int[] resultAmount1 = {2, 1, 1, 1, 1};
//		Die armadaRed = new Die("Armada Red", aRed, allResultName1, sideName1, sideAmount1, resultName1, resultAmount1);
//		
//		long startTime = System.currentTimeMillis();
//		Roll testRoll = new Roll(2, armadaRed);
//		String[] from = {"blank", "hit"};
//		testRoll.change(1, from, "doubleHit");
//		Result testResult = testRoll.genResult();
//		long endTime = System.currentTimeMillis();
//		int[][] minMax = {{1, 1, 0}, {8, 4, 4}};
//		String[] resultNames = {"damage", "accuracy", "critEffect"};
//		Result subset = testResult.subset(resultNames, minMax);
//		subset.printResult();
////		testResult.printResult();
//		long time = endTime - startTime;
//		System.out.println("\n" + time);
		
//		Side[] aBlue = {new Side("hit", "1/2"), new Side("crit", "1/4"), new Side("accuracy", "1/4")};
//		String[] allResultName1 = {"damage", "accuracy", "critEffect"};
//		String[] sideName1 = {"hit", "crit", "crit", "accuracy"};
//		int[] sideAmount1 = {1,1,1,1};
//		String[] resultName1 = {"damage", "damage", "critEffect", "accuracy"};
//		int[] resultAmount1 = {1, 1, 1, 1};
//		Die armadaBlue = new Die("Armada Blue", aBlue, allResultName1, sideName1, sideAmount1, resultName1, resultAmount1);
//
//		Roll tenNumb = new Roll(2, armadaBlue);
//		String[] from = {"accuracy", "hit"};
//		tenNumb.reroll(1, from);
//		
//		Result oneAtk = tenNumb.genResult();
//		Result twoAtk = oneAtk.addResults(oneAtk);
//		Result threeAtk = twoAtk.addResults(oneAtk);
//		
//		oneAtk.printResult();
//		twoAtk.printResult();
//		threeAtk.printResult();
//		
//		System.out.println("\n\n\n");
//		
//		String[] resultNames = {"critEffect"};
//		int[][] minMax = {{0,0,0},{10,10,10}};
//		Result oneAtkCrit = oneAtk.subset(resultNames, minMax);
//		Result twoAtkCrit = twoAtk.subset(resultNames, minMax);
//		Result threeAtkCrit = threeAtk.subset(resultNames, minMax);
//		
//		oneAtkCrit.printResult();
//		twoAtkCrit.printResult();
//		threeAtkCrit.printResult();
		
		Side[] burst = {new Side("burst", "37/64"), new Side("nothing", "27/64")};
		String[] allResultName1 = {"burst"};
		String[] sideName1 = {"burst"};
		int[] sideAmount1 = {1};
		String[] resultName1 = {"burst"};
		int[] resultAmount1 = {1, 1, 1, 1};
		Die NumbBurst = new Die("TenNumb", burst, allResultName1, sideName1, sideAmount1, resultName1, resultAmount1);

		Roll burstRoll = new Roll(9, NumbBurst);
		Result burstResult = burstRoll.genResult();
		burstResult.printResult();
		
	}

}
