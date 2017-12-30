package result;

import java.util.ArrayList;
import java.util.Comparator;

public class BinarySearchDeluxeArrayList {
    /**
     * Return the index of the first key in a[] that equals the search key, or -1 if no such key.
     * @param a
     * @param key
     * @param comparator
     * @return index
     */
    public static <Key> int firstIndexOf(ArrayList<Key> a, Key key, Comparator<Key> comparator){
    	valid(a, key, comparator);
    	int low = 0;
    	int hi = a.size()-1;
    	int index = -1;
    	while(low <= hi){
    		int mid = low+(hi-low)/2;
    		int comp = comparator.compare(key, a.get(mid));
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
    
    /**
     * Return the index of the last key in a[] that equals the search key, or -1 if no such key.
     * @param a
     * @param key
     * @param comparator
     * @return index
     */
    public static <Key> int lastIndexOf(ArrayList<Key> a, Key key, Comparator<Key> comparator){
    	valid(a, key, comparator);
    	int low = 0;
    	int hi = a.size()-1;
    	int index = -1;
    	while(low <= hi){//same thought process as about but looking at the other end
    		int mid = low+(hi-low)/2;
    		int comp = comparator.compare(key, a.get(mid));
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
    
    //private method to test if parameters are valid
    private static <Key> void valid(ArrayList<Key> a, Key key, Comparator<Key> comparator){
    	if( a.equals(null)||key.equals(null)||comparator.equals(null))
    		throw new NullPointerException();
    }
}