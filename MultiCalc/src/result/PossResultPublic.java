package result;

import java.math.BigDecimal;

/**
 * @author David
 * 
 * A possible result used in an array list by the Result class.
 *
 */
public class PossResultPublic  implements Comparable<PossResultPublic>{
	private BigDecimal prob;
	private int[] amount;
	
	private PossResultPublic(BigDecimal prob, int[] amount){
		this.prob = prob;
		this.amount = amount.clone();
	}

	public BigDecimal getProb() {
		return prob;
	}

	public void setProb(BigDecimal prob) {
		this.prob = prob;
	}

	public int[] getAmount() {
		return amount;
	}

	@Override
	public int compareTo(PossResultPublic res) {
		if ((this.amount.length - res.amount.length) != 0) 
			throw new IllegalArgumentException();
		
		int difference;
		for(int i=0; i<this.amount.length; i++){
			difference = res.amount[i] - this.amount[i];
			if(difference != 0) return difference;
		}
		return 0;
	}
}