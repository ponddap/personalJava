package die;

import java.math.BigDecimal;

/**
 * @author David
 *
 *An array of sides along with name that represents a die. 
 *
 *Translate a roll into a result
 */

public class Die {
	private String name;
	private Side[] sides;
	
	private String[] allResultNames;
	private String[] allSideNames;
	private String[] sideName;
	private int[] sideAmount;
	private String[] resultName;
	private int[] resultAmount;
	
	

	/**
	 * @param name
	 * @param die
	 * 
	 * Sets up the die, makes sure that the probability of all
	 * the sides adds up to 1 so all the math works.
	 */
	public Die(String name, Side[] die, String[] allResultNames, String[] sideName
			, int[] sideAmount, String[] resultName, int[] resultAmount){
		BigDecimal prob = BigDecimal.ZERO;
		for(Side p: die){
			prob = prob.add(p.getChance());
		}
		if(prob.doubleValue() != 1.0){
			throw new IllegalArgumentException("Chance of all possibilities must add up to one");
		}
		
//		if(sideName.length != sideAmount.length || sideName.length != resultName.length 
//				|| sideName.length != resultAmount.length){
//			throw new IllegalArgumentException("All translation arrays must have the same length");
//		}
		this.name = name;
		this.sides = die;
		this.allResultNames = allResultNames;
		this.sideName = sideName;
		this.sideAmount = sideAmount;
		this.resultName = resultName;
		this.resultAmount = resultAmount;
		
		allSideNames = new String[sides.length];
		for(int i=0; i <sides.length; i++){
			allSideNames[i] = sides[i].getName();
		}
	}
	
	public Side[] getSides(){
		return sides.clone();
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumPoss(){
		return sides.length;
	}
	
	public String[] getAllResultNames(){
		return allResultNames.clone();
	}
	
	
	/**
	 * @param singleRoll -- int[] representing one possible roll of an undetermined amount of dice
	 * @return singleResult -- int[] representing how that roll would be translated into a result
	 */
	public int[] translate(int[] singleRoll){
		if(singleRoll.length != sides.length) throw new IllegalArgumentException();
		int[] singleResult = new int[allResultNames.length]; 
		
		for(int i=0; i< sideName.length; i++){
			int sideIndex = sideIndexFromString(sideName[i]);
			int resultIndex = resultIndexFromString(resultName[i]);
			if(singleRoll[sideIndex] != 0){
				singleResult[resultIndex] += 
						singleRoll[sideIndex]/sideAmount[i]*resultAmount[i];
			}
			
		}
		return singleResult;
	}
	
	public int sideIndexFromString(String sideName){
		for(int i=0; i<allSideNames.length; i++){
			if(sideName.equalsIgnoreCase(allSideNames[i])) return i;
		}
		return -1;
	}
	
	public int resultIndexFromString(String resultName){
		for(int i=0; i<allResultNames.length; i++){
			if(resultName.equalsIgnoreCase(allResultNames[i])) return i;
		}
		return -1;
	}
}
