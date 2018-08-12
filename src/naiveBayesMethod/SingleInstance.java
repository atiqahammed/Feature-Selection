/*--------------------------------------------------------------------------
implemented by Atiq Ahammed
last edited : 25-04-2017


--------------------------------------------------------------------------*/
package naiveBayesMethod;

import java.util.ArrayList;

public class SingleInstance {
	private ArrayList <Double> attributes = new ArrayList<Double>();
	private double result;
	
	public SingleInstance(ArrayList<Double> attributes, double result) {
		super();
		this.attributes = attributes;
		this.result = result;
	}

	public ArrayList<Double> getAttributes() {
		return attributes;
	}

	public double getResult() {
		return result;
	}
}
