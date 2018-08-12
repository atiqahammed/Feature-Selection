/*--------------------------------------------------------------------------
implemented by Atiq Ahammed
last edited : 21-04-2017


--------------------------------------------------------------------------*/

package featureSelection;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Feature {

	protected double[] featureValues;
	protected Set<Double> set = new HashSet<Double>();
	protected Map<Double, Integer> map = new HashMap<Double, Integer>();
	protected Iterator<Double> itr, itr2;
	protected double entropy = 0.0;
	protected int featureIndex;

	
	// constructor....
	public Feature() {

	}
	// calculation of entropy
	protected void calculateEntropy() {
		double tempEntropy = 0.0;
		itr = set.iterator();
		while (itr.hasNext()) {
			double keyValue = itr.next();
			int count = map.get(keyValue);

			double probality = (double) count / featureValues.length;

			tempEntropy += probality * ((Math.log(probality)) / (Math.log(2.0)));
		}

		if (tempEntropy < 0)
			tempEntropy *= -1;

		entropy = tempEntropy;
	}
	
	protected void calculateValuesInSet() {
		for (int i = 0; i < featureValues.length; i++)
			set.add(featureValues[i]);
	}
	
	protected void countDataInMap() {
		itr = set.iterator();
		while (itr.hasNext()) {
			double temp = itr.next();
			map.put(temp, 0);
		}

		itr = set.iterator();
		while (itr.hasNext()) {
			double temp = itr.next();
			for (int i = 0; i < featureValues.length; i++)
				if (featureValues[i] == temp) {
					int temp2 = map.get(temp);
					map.put(temp, temp2 + 1);
				}
		}
	}

	public double[] getFeatureValues() {
		return featureValues;
	}

	public double getEntropy() {
		return entropy;
	}
	
	public Set<Double> getSet(){
		return set;
	}
	
	public Map<Double, Integer> getMap(){
		return map;
	}
	
	public void printFeatureValue(){
		for(int i=0; i<featureValues.length; i++)
			System.out.print(featureValues[i] + " ");
		System.out.println();
	}
	
	public int getFeatureIndex(){
		return featureIndex;
	}
	
}
