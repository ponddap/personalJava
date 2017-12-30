package result;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import die.Die;


public class Result {
	private List<PossResult> results = new ArrayList<>();
	private String[] resultNames;
	
	public Result(Die die, BigDecimal[] prob, int[][] multi){
		resultNames = die.getAllResultNames();
		int[] resultIndex;
		int[] amount;
		for(int i=0; i<multi.length; i++){
			amount = die.translate(multi[i]);
			resultIndex = findIndex(amount);
			if(results.isEmpty()){
				results.add(new PossResult(prob[i], amount));
			}else if(resultIndex[0] == 0){
				if(compArrays(amount, results.get(results.size()-1).amount) > 0){
					results.add(resultIndex[1]+1, new PossResult(prob[i], amount));
				}else{
					results.add(resultIndex[1], new PossResult(prob[i], amount));
				}
			}else{
				results.get(resultIndex[1]).prob = 
						results.get(resultIndex[1]).prob.add(prob[i]);
			}
		}
	}
	
	public Result(String[] names, List<PossResult> results){
		this.resultNames = names;
		this.results = results;
		Collections.sort(this.results);
	}
	
	private Result(String[] resultNames, int[][] minMax, Result initialResult){
		this.resultNames = resultNames;
		int[] amount = new int[resultNames.length];
		int[] resultIndex;
		int initialResultIndex;
		boolean subset;
		BigDecimal prob;
		
		for(int i=0; i<initialResult.results.size(); i++){
			subset = true;
			for(int j=0; j<minMax[0].length; j++){
				int resultValue = initialResult.results.get(i).amount[j];
				if(resultValue < minMax[0][j] || resultValue > minMax[1][j]){
					subset = false;
					break;
				}
			}
			if(subset){
				for(int j=0; j<amount.length; j++){
					initialResultIndex = initialResult.resultNameIndexFromString(resultNames[j]);
					amount[j] = initialResult.results.get(i).amount[initialResultIndex];
				}
				prob = initialResult.results.get(i).prob.add(BigDecimal.ZERO);
				resultIndex = findIndex(amount);
				if(results.isEmpty()){
					results.add(new PossResult(prob, amount));
				}else if(resultIndex[0] == 0){
					if(compArrays(amount, results.get(results.size()-1).amount) > 0){
						results.add(resultIndex[1]+1, new PossResult(prob, amount));
					}else{
						results.add(resultIndex[1], new PossResult(prob, amount));
					}
				}else{
					results.get(resultIndex[1]).prob = 
							results.get(resultIndex[1]).prob.add(prob);
				}
			}
		}
		
	}
	
	/**
	 * @param resultArray
	 * @return an int[] the first index will be treated as a boolean value for if the
	 * 			result is already contained in the list(0 false 1 true). The second value will be the index 
	 * 			of where the value is or should be
	 */
	private int[] findIndex(int[] resultArray){
		int[] boolIndex = {0,0};
		if(results.isEmpty()) return boolIndex;
		int hi = results.size() - 1;
		int low = 0;
		int mid = 0;
		while (low <= hi) {
			mid = low + (hi - low) / 2;
			int comp = compArrays(resultArray, results.get(mid).amount);
			if (comp < 0)
				hi = mid - 1;
			if (comp > 0)
				low = mid + 1;
			if (comp == 0){
				boolIndex[0] = 1;
				boolIndex[1] = mid;
				return boolIndex;
			}
		}
		boolIndex[1] = mid;
		return boolIndex;
	}
	
	private int compArrays(int[] result1, int[] result2){
		int comp;
		for(int i=0; i<result1.length; i++){
			comp = result2[i] - result1[i];
			if(comp != 0) return comp;
		}
		return 0;
	}
	
	private List<PossResult> pointsArea(int[] low, int[] hi){
		List<PossResult> subList = results.subList(0, results.size()-1);
		for(int i=0; i<low.length; i++){
			subList = pointsArea(i, low[i], hi[i], subList);
		}
		return subList;
	}
	
	private List<PossResult> pointsArea(int index, int low, int hi, List<PossResult> subList){
		subList.sort(results.get(0).compByIndex(index));
		int lower = firstIndexOf(index, low, subList);
		int upper = lastIndexOf(index, hi, subList);
		if(lower == -1) lower = 0;
		if(upper == -1) upper = subList.size()-1;
		return subList.subList(lower, upper+1);
	}
	
	private int firstIndexOf(int i, int val, List<PossResult> subList){
		int low = 0;
    	int hi = subList.size()-1;
    	int index = -1;
    	while(low <= hi){
    		int mid = low+(hi-low)/2;
    		int comp = val - subList.get(mid).amount[i];
    		if(comp<0){//normal binary search
    			hi = mid-1;
    		}else if(comp>0){//normal binary search
    			low = mid+1;
    		}else {//if equal store the index and check below in the next iteration
    			index = mid;
    			hi = mid-1;
    		}
    	}
    	return index;
	}
	
	private int lastIndexOf(int i, int val, List<PossResult> subList){
		int low = 0;
    	int hi = subList.size()-1;
    	int index = -1;
    	while(low <= hi){//same thought process as about but looking at the other end
    		int mid = low+(hi-low)/2;
    		int comp = val - subList.get(mid).amount[i];
    		if(comp<0){
    			hi = mid-1;
    		}else if(comp>0){
    			low = mid+1;
    		}else{
    			index = mid;
    			low = mid+1;
    		}
    	}
    	return index;
	}
	
	private int resultNameIndexFromString(String name){
		for(int i=0; i<resultNames.length; i++){
			if(name.equalsIgnoreCase(resultNames[i])) return i;
		}
		return -1;
	}
	
	public Result subset(String[] resultNames, int[][] minMax){
		if(minMax[0].length != this.resultNames.length) throw new IllegalArgumentException();
		return new Result(resultNames, minMax, this);
	}
	
	public void printResult(){
		BigDecimal totalProb = BigDecimal.ZERO;
		System.out.printf("%14s %14s", "Chance", "Cumulative");
		for(String el: resultNames){
			System.out.printf("%14s ", el);
		}
		System.out.println();
		for(PossResult el: results){
			totalProb = totalProb.add(el.prob);
			System.out.printf("%14f ", el.prob.doubleValue());
			System.out.printf("%14f ", totalProb.doubleValue());
			for(int i=0; i<el.amount.length; i++){
				System.out.printf("%13d ", el.amount[i]);
			}
			System.out.println();
		}
		System.out.println("TotalProb: " + totalProb.doubleValue());
	}
	
	public void printResult(String sortBy, int[] low, int[] hi){
		int resultIndex = resultNameIndexFromString(sortBy);
		List<PossResult> subList = pointsArea(low, hi);
		subList.sort(results.get(0).compByIndex(resultIndex));
		BigDecimal totalProb = BigDecimal.ZERO;
		System.out.printf("%10s %10s", "Chance", "CumChan");
		System.out.printf("%10s ", sortBy);
		System.out.println();
		for(PossResult el: subList){
			totalProb = totalProb.add(el.prob);
			System.out.printf("%10f ", el.prob.doubleValue());
			System.out.printf("%10f ", totalProb.doubleValue());
			System.out.printf("%10d ", el.amount[resultIndex]);
			System.out.println();
		}
		System.out.println("TotalProb: " + totalProb.doubleValue());
	}
	//Check if works
	public Result addResults(Result added){
		String[] newResultNames = this.resultNames.clone();
		BigDecimal prob;
		int[] newList = new int[this.resultNames.length];
		List<PossResult> addedList = new ArrayList<>();
		PossResult called;
		PossResult argument;
		for(int i=0; i<this.results.size(); i++){
			called = this.results.get(i);
			for(int j=0; j<added.results.size(); j++){
				argument = added.results.get(j);
				prob = called.prob.multiply(argument.prob);
				for(int k=0; k<newList.length; k++){
					newList[k] = called.amount[k] 
							+ argument.amount[k];
				}
				addedList.add(new PossResult(prob, newList));
			}
		}
		return new Result(newResultNames, addedList);
	}
	

	private class PossResult implements Comparable<PossResult>{
		private BigDecimal prob;
		private int[] amount;

		
		private PossResult(BigDecimal prob, int[] amount){
			this.prob = prob;
			this.amount = amount.clone();

		}

		@Override
		public int compareTo(PossResult res) {
			int comp;
			for(int i=0; i<this.amount.length; i++){
				comp = res.amount[i] - this.amount[i];
				if(comp != 0) return comp;
			}
			return 0;
		}
		
		public Comparator<PossResult> compByIndex(int i){
			class IndexComparator implements Comparator<PossResult>{
				int index;
				
				private IndexComparator(int i){
					index = i;
				}
				@Override
				public int compare(PossResult result1, PossResult result2) {
					return result1.amount[index] - result2.amount[index];
				}
				
			}
			
			return new IndexComparator(i);
		}
	}
}
