package warhammerMath;

import java.math.BigDecimal;

import die.Die;
import die.Side;
import multiCalcV1.Roll;
import result.Result;

public class Wounds {

	public static void main(String[] args) {
		int numberOfAttacks = 4;
		
		int hitsOn = 3;
		boolean rerollHit = false;
		boolean rerollHitOne = false;
		int woundsOn = 2;
		boolean rerollWound = false;
		boolean rerollWoundOne = false;
		boolean rend = false;
		int ap = 2;
		int armorSave = 7;
		int invulSave = 7;
		int coverSave = 7;
		boolean ignoresCover = false;
		boolean rerollSave = false;
		int feelNoPain = 7;
		boolean rerollFnp = false;
		boolean rerollFnpOne = false;
		
		
		//hits
		double hitOdds;
		if(rerollHit){
			hitOdds = 1-((1-hitsOn)/6d)*((1-hitsOn)/6d);
		}else if(rerollHitOne && !rerollHit){
			hitOdds = (7-hitsOn)/6d;
			hitOdds += (1-hitOdds)*((1/6d)/(1-hitOdds))*(7-hitsOn)/6d;
		}
		else hitOdds = (7-hitsOn)/6d;
		
		//wounds
		double woundOdds;
		if(rerollWound){
			woundOdds = (1-((1-woundsOn)/6d)*((1-woundsOn)/6d));
		}else if(rerollWoundOne && !rerollWound){
			double w = (7-woundsOn)/6d;
			w += (1-w)*((1/6d)/(1-w))*(7-woundsOn)/6d;
			woundOdds = w;
		} else woundOdds = ((7-woundsOn)/6d);
		//saves
		
		double failSaveOdds;
		//armor
		if(rerollSave){
			if(ap>armorSave && armorSave<invulSave && armorSave<coverSave){
				if(rend){
					double rendOdds = ((1/6d)/((7-woundsOn)/6d));
					if(coverSave<7 && !ignoresCover && coverSave<invulSave){
						failSaveOdds = rendOdds*(1-reroll((7-coverSave)/6d));
						failSaveOdds += (1-rendOdds)*(1-reroll((7-armorSave)/6d));
					}
					else if(invulSave<7){
						failSaveOdds = rendOdds*(1-reroll((7-invulSave)/6d));
						failSaveOdds += (1-rendOdds)*(1-reroll((7-armorSave)/6d));
					}
					else{
						failSaveOdds = rendOdds+(1-rendOdds)*(1-reroll((7-armorSave)/6d));
					}
				}else failSaveOdds = 1-reroll((7-armorSave)/6d);
			}
			//cover
			else if(coverSave<7 && !ignoresCover && coverSave<invulSave) 
				failSaveOdds = 1-reroll((7-coverSave)/6d);
			//invul
			else if(invulSave<7) failSaveOdds = 1-reroll((7-invulSave)/6d);
			else failSaveOdds = 1;
		}else{
			if(ap>armorSave && armorSave<invulSave && armorSave<coverSave){
				if(rend){
					double rendOdds = ((1/6d)/((7-woundsOn)/6d));
					if(coverSave<7 && !ignoresCover && coverSave<invulSave){
						failSaveOdds = rendOdds*(1-(7-coverSave)/6d);
						failSaveOdds += (1-rendOdds)*(1-(7-armorSave)/6d);
					}
					else if(invulSave<7){
						failSaveOdds = rendOdds*(1-(7-invulSave)/6d);
						failSaveOdds += (1-rendOdds)*(1-(7-armorSave)/6d);
					}
					else{
						failSaveOdds = rendOdds+(1-rendOdds)*(1-(7-armorSave)/6d);;
					}
				}else failSaveOdds = (1-(7-armorSave)/6d);
			}
			//cover
			else if(coverSave<7 && !ignoresCover && coverSave<invulSave) 
				failSaveOdds = (1-(7-coverSave)/6d);
			//invul
			else if(invulSave<7) failSaveOdds = (1-(7-invulSave)/6d);
			else failSaveOdds = 1;
		}
		
		//fnp
		double failFnpOdds = 1;
		if(feelNoPain<7){ 
			if(rerollFnp){
				failFnpOdds = (1-(7-feelNoPain)/6d)*(1-(7-feelNoPain)/6d);
			}else if(rerollFnpOne && !rerollFnp){
				failFnpOdds = (1-(7-feelNoPain)/6d);
				failFnpOdds -= (failFnpOdds)*((1/6d)/(failFnpOdds))*((7-feelNoPain)/6d);
			}
			else failFnpOdds = (1-(7-feelNoPain)/6d);
		}
		double unsavedWound = hitOdds*woundOdds*failSaveOdds*failFnpOdds;
		
		System.out.println("Hit Odds " + hitOdds);
		System.out.println("Wound Odds " + woundOdds);
		System.out.println("Failed Save Odds " + failSaveOdds);
		System.out.println("Failed fnp Odds " + failFnpOdds);
		System.out.println("Unsaved Wound " + unsavedWound);
		System.out.println("Avg Wounds " + unsavedWound*numberOfAttacks);
		double stdDev = Math.sqrt((unsavedWound*numberOfAttacks*(1-unsavedWound)));
		System.out.println("Std Dev " + stdDev);
		
		BigDecimal usw = new BigDecimal(unsavedWound);
		BigDecimal sw = BigDecimal.ONE.subtract(usw);
		Side[] war = {new Side("wound", usw.toString()), new Side("noWound", sw.toString())};
		String[] allResultName = {"wound"};
		String[] sideName = {"wound"};
		int[] sideAmount = {1};
		String[] resultName = {"wound"};
		int[] resultAmount = {1};
		Die warDie = new Die("Warhammer", war, allResultName, sideName, sideAmount, resultName, resultAmount);
		
		Roll warhammer = new Roll(numberOfAttacks, warDie);
		Result warhammerResult = warhammer.genResult();
		warhammerResult.printResult();
		
		
	}
	//1 - odds of failing two rolls in a row 
	public static double reroll(double odds) {
		return (1-(1 - odds) * (1 - odds));
	}
}
