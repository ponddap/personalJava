package warhammerMath;

import die.Die;
import die.Side;
import multiCalcV1.Roll;
import result.Result;

public class Perils {

	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Side[] p2 = {new Side("perils", "1/36"), new Side("noPerils", "35/36")};
		String[] allResultName = {"perils"};
		String[] sideName = {"perils"};
		int[] sideAmount = {1};
		String[] resultName = {"perils"};
		int[] resultAmount = {1};
		Die peril2 = new Die("Perils2", p2, allResultName, sideName, sideAmount
				, resultName, resultAmount);
		
		Side[] p3 = {new Side("perils", "2/27"), new Side("noPeril", "25/27")};
		Die peril3 = new Die("Perils2", p3, allResultName, sideName, sideAmount
				, resultName, resultAmount);
		
		Side[] p4 = {new Side("perils", "19/144"), new Side("noPeril", "125/144")};
		Die peril4 = new Die("Perils2", p4, allResultName, sideName, sideAmount
				, resultName, resultAmount);
		
		Side[] p5 = {new Side("perils", "763/3888"), new Side("noPeril", "3125/3888")};
		Die peril5 = new Die("Perils2", p5, allResultName, sideName, sideAmount
				, resultName, resultAmount);
		
		Side[] p6 = {new Side("perils", "12281/46656"), new Side("noPeril", "34375/46656")};
		Die peril6 = new Die("Perils2", p6, allResultName, sideName, sideAmount
				, resultName, resultAmount);
		
		Side[] p7 = {new Side("perils", "7703/23328"), new Side("noPeril", "15625/23328")};
		Die peril7 = new Die("Perils2", p7, allResultName, sideName, sideAmount
				, resultName, resultAmount);
		
		Side[] p8 = {new Side("perils", "663991/1679616"), new Side("noPeril", "1015625/1679616")};
		Die peril8 = new Die("Perils2", p8, allResultName, sideName, sideAmount
				, resultName, resultAmount);
		
		Side[] p9 = {new Side("perils", "2304473/5038848"), new Side("noPeril", "2734375/5038848")};
		Die peril9 = new Die("Perils2", p9, allResultName, sideName, sideAmount
				, resultName, resultAmount);
		
		Roll perils2Roll = new Roll(12, peril6);
		Result perils2 = perils2Roll.genResult();
		perils2.printResult();
		
//		Roll test = new Roll(0, peril2);
//		Result testResult = test.genResult();
//		testResult = testResult.addResults(testResult);
//		testResult.printResult();

	}

}
