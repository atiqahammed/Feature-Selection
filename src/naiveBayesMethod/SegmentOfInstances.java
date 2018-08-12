/*--------------------------------------------------------------------------
implemented by Atiq Ahammed
last edited : 25-04-2017


--------------------------------------------------------------------------*/


package naiveBayesMethod;

import java.util.ArrayList;

public class SegmentOfInstances {

	private ArrayList<SingleInstance> instancesOfSegment = new ArrayList<SingleInstance>();

	public ArrayList<SingleInstance> getInstancesOfSegment() {
		return instancesOfSegment;
	}

	public void addInstance(SingleInstance newOne) {
		instancesOfSegment.add(newOne);
	}

}
