/*--------------------------------------------------------------------------
implemented by Atiq Ahammed
last edited : 25-04-2017


--------------------------------------------------------------------------*/
package featureSelection;

public class ContinousFeature extends Feature {

	private double[] tempFeatureValues;
	
	public ContinousFeature(double[] values,  int index) {
		super();
		tempFeatureValues = values;
		featureIndex = index;
		parseValues();		
		calculateValuesInSet();
		countDataInMap();
		calculateEntropy();
	}
	
	
	public ContinousFeature(double[] values, int classNumbers, int index) {
		super();
		tempFeatureValues = values;
		featureIndex = index;
		parseValues(classNumbers);
		
		calculateValuesInSet();
		countDataInMap();
		calculateEntropy();
	}
	
	
	
	
	// fixed classinterval from user.........
	private void parseValues(int kInInt) {
		double highestValue = getHighValue();
		double lowestValue = getLowestValue();

		double classInterval = (int) ((highestValue - lowestValue) / kInInt) + 1.0;

		double[] startingValues = new double[kInInt];
		double[] endingValues = new double[kInInt];

		startingValues[0] = (int) lowestValue;
		endingValues[0] = startingValues[0] + classInterval;

		for (int i = 1; i < kInInt; i++) {
			startingValues[i] = endingValues[i - 1];
			endingValues[i] = startingValues[i] + classInterval;
		}

		featureValues = new double[tempFeatureValues.length];
		for (int i = 0; i < tempFeatureValues.length; i++) {
			int indexValue = -1;
			for (int j = 0; j < kInInt; j++) {
				if (tempFeatureValues[i] >= startingValues[j] && tempFeatureValues[i] < endingValues[j]) {
					indexValue = j;
					break;
				}
			}

			featureValues[i] = indexValue;
		}
	}
	
	
	
	// statistical classification for interval
	private void parseValues() {
		double highestValue = getHighValue();
		double lowestValue = getLowestValue();
		
		// need for formula 
		double k = 1 + 3.322 * ((Math.log(tempFeatureValues.length)) / Math.log(10.0));
		int kInInt = (int) k;
		
		double classInterval = (int) ((highestValue - lowestValue) / kInInt) + 1.0;
		
		double[] startingValues = new double[kInInt];
		double[] endingValues = new double[kInInt];

		startingValues[0] = (int) lowestValue;
		endingValues[0] = startingValues[0] + classInterval;

		for (int i = 1; i < kInInt; i++) {
			startingValues[i] = endingValues[i - 1];
			endingValues[i] = startingValues[i] + classInterval;
		}
		
		featureValues = new double[tempFeatureValues.length];
		for (int i = 0; i < tempFeatureValues.length; i++) {
			int indexValue = -1;
			for (int j = 0; j < kInInt; j++) {
				if (tempFeatureValues[i] >= startingValues[j] && tempFeatureValues[i] < endingValues[j]) {
					indexValue = j;
					break;
				}
			}

			featureValues[i] = indexValue;
		}
	}

	private double getLowestValue() {
		double lowestValue = tempFeatureValues[0];

		for (int i = 1; i < tempFeatureValues.length; i++) {
			if (tempFeatureValues[i] < lowestValue)
				lowestValue = tempFeatureValues[i];
		}
		return lowestValue;
	}
	
	private double getHighValue() {
		double highestValue = tempFeatureValues[0];

		for (int i = 1; i < tempFeatureValues.length; i++) {
			if (tempFeatureValues[i] > highestValue)
				highestValue = tempFeatureValues[i];
		}
		return highestValue;
	}

}
