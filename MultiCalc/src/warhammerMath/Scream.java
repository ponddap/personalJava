package warhammerMath;

import die.Die;
import die.Side;
import multiCalcV1.Roll;
import result.Result;

public class Scream {

	public static void main(String[] args) {
		int numScreams = 3;
		int ld = 9;
		
		Side[] s = {new Side("18", "1/216"), new Side("17", "3/216"), new Side("16", "6/216")
				,new Side("15", "10/216"), new Side("14", "15/216"), new Side("13", "21/216")
				,new Side("12", "25/216"), new Side("11", "27/216"), new Side("10", "27/216")
				,new Side("9", "25/216"), new Side("8", "21/216"), new Side("7", "15/216")
				,new Side("6", "10/216"), new Side("5", "6/216"), new Side("4", "3/216")
				,new Side("3", "1/216")};
		String[] allResultName = {"wounds"};
		String[] sideName = {"18", "17", "16", "15", "14", "13", "12", "11", "10", "9", "8"
				, "7", "6", "5", "4", "3"};
		int[] sideAmount = {1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1};
		String[] resultName = {"wounds", "wounds", "wounds", "wounds", "wounds", "wounds"
				, "wounds", "wounds", "wounds", "wounds", "wounds", "wounds", "wounds"
				, "wounds", "wounds", "wounds"};
		int[] resultAmount = {18, 17, 16, 15, 14, 13, 12, 11, 10, 9, 8, 7, 6, 5, 4, 3};
		//change the number of wounds to reflect the ld of the target
		for(int i=0; i<resultAmount.length; i++){
			if(resultAmount[i] - ld > 0) resultAmount[i] -= ld;
			else resultAmount[i] = 0;
		}
		Die scream = new Die("Scream", s, allResultName, sideName, sideAmount, resultName, resultAmount);
		
		Roll screamWounds = new Roll(numScreams, scream);
		Result wounds = screamWounds.genResult();
		wounds.printResult();

	}

}
