package die;

import java.math.BigDecimal;

/**
 * @author David
 * 
 * Class meant to represent the side of a dice with both 
 * a name and a probability of the side being rolled being 
 * represented
 */
public class Side {
	private String name;
	private BigDecimal probability;
	
	public Side(String name, String chance){
		this.name = name;
		setChance(chance);
	}
	
	private void setChance(String stringChance){
		String[] frac = stringChance.split("/");
		if(frac.length == 2){
			BigDecimal num = new BigDecimal(frac[0]);
			BigDecimal den = new BigDecimal(frac[1]);
			probability = num.divide(den, 64, BigDecimal.ROUND_HALF_UP);
		}else{
			probability = new BigDecimal(stringChance);
		}
	}

	public String getName() {
		return name;
	}

	public BigDecimal getChance() {
		return probability;
	}
}