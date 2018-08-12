/*--------------------------------------------------------------------------
implemented by Atiq Ahammed
last edited : 25-04-2017


--------------------------------------------------------------------------*/

package featureSelection;

public class DiscreteFeature extends Feature{

	public DiscreteFeature(double[] values, int index) {
		super();
		featureValues = values;
		featureIndex = index;
		
		calculateValuesInSet();
		countDataInMap();
		calculateEntropy();
	}

}
