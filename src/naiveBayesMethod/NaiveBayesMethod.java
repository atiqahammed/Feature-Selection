/*--------------------------------------------------------------------------
implemented by Atiq Ahammed
last edited : 25-04-2017


--------------------------------------------------------------------------*/

package naiveBayesMethod;

import java.io.File;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.io.PrintWriter;
import javax.print.attribute.standard.PrinterLocation;
import featureSelection.Feature;
import featureSelection.FeatureSelection;

public class NaiveBayesMethod {

	private ArrayList<Feature> myFeatures = new ArrayList<Feature>();
	private Feature classFeature;
	private int typesOfClass;
	private int numberOfInstance;
	private int numberOfValidation; 
	public ArrayList<String> predictionList;


	public NaiveBayesMethod(Feature classFeature, ArrayList<String> predictionList) {
		this.classFeature = classFeature;
		numberOfInstance = classFeature.getFeatureValues().length;
		this.predictionList = predictionList;
	}

	public double getAccuracy(ArrayList<Feature> myFeatures) {
		this.myFeatures = myFeatures;
		SingleInstance allInstance[] = parseAllInstance(myFeatures);
		SegmentOfInstances differnetInstance[] = new SegmentOfInstances[classFeature.getSet().size()];
		Map<Double, Integer> classIndex = new TreeMap<Double, Integer>();

		int index = 0;
		for (double d : classFeature.getSet()) {
			classIndex.put(d, index);
			index++;
		}

		for (int i = 0; i < classFeature.getSet().size(); i++)
			differnetInstance[i] = new SegmentOfInstances();
		for (int i = 0; i < numberOfInstance; i++)
			differnetInstance[classIndex.get(allInstance[i].getResult())].addInstance(allInstance[i]);

		numberOfValidation = numberOfInstance;
		int maxLoopCount = 0;
		for (int i = 0; i < differnetInstance.length; i++) {
			numberOfValidation = Math.min(numberOfValidation, differnetInstance[i].getInstancesOfSegment().size());
			maxLoopCount = Math.max(maxLoopCount, differnetInstance[i].getInstancesOfSegment().size());
		}

		if (numberOfValidation > 10)
			numberOfValidation = 10;

		SegmentOfInstances validationSegment[] = getValidationSegment(maxLoopCount, differnetInstance);
		return averageAccuracy(validationSegment);
	}

	private SegmentOfInstances[] getValidationSegment(int maxLoopCount, SegmentOfInstances[] differnetInstance) {
		SegmentOfInstances validationSegment[] = new SegmentOfInstances[numberOfValidation];
		for (int i = 0; i < numberOfValidation; i++)
			validationSegment[i] = new SegmentOfInstances();

		for (int i = 0; i < maxLoopCount; i++)
			for (int j = 0; j < differnetInstance.length; j++) {
				ArrayList<SingleInstance> tempSegment = differnetInstance[j].getInstancesOfSegment();

				if (!tempSegment.isEmpty()) {
					validationSegment[i % numberOfValidation].addInstance(tempSegment.get(0));
					tempSegment.remove(0);
				}
			}

		return validationSegment;
	}

	private double averageAccuracy(SegmentOfInstances[] validationSegment) {
		double accuracy = 0.0;

		for (int i = 0; i < numberOfValidation; i++) {
			ArrayList<SingleInstance> testSegment = validationSegment[i].getInstancesOfSegment();
			ArrayList<SingleInstance> trainSegment = new ArrayList<SingleInstance>();

			for (int j = 0; j < numberOfValidation; j++)
				if (j != i)
					for (int k = 0; k < validationSegment[j].getInstancesOfSegment().size(); k++) {
						SingleInstance tempInstance = validationSegment[j].getInstancesOfSegment().get(k);
						trainSegment.add(tempInstance);
					}

			//System.out.println("validation number : " + (i + 1) + ", number of train instance : " + trainSegment.size()
			//		+ ", number of test instance : " + testSegment.size());
			
			predictionList.add(String.format("validation number :: %d, number of train instance :: %d, number of test instance :: %d", (i+1), trainSegment.size(),  testSegment.size()));
			
			// (i + 1) trainSegment.size() testSegment.size()
			accuracy += calculateAccuracy(testSegment, trainSegment);
		}
		System.out.println("total accuracy : " + (accuracy / numberOfValidation));
		predictionList.add(String.format("total accuracy : " + (accuracy / numberOfValidation)));
		return (accuracy / numberOfValidation);
	}

	private SingleInstance[] parseAllInstance(ArrayList<Feature> myFeatures2) {
		SingleInstance allInstance[] = new SingleInstance[numberOfInstance];

		for (int i = 0; i < numberOfInstance; i++) {
			ArrayList<Double> attributes = new ArrayList<Double>();

			for (int j = 0; j < myFeatures.size(); j++)
				attributes.add(myFeatures.get(j).getFeatureValues()[i]);

			allInstance[i] = new SingleInstance(attributes, classFeature.getFeatureValues()[i]);
		}
		return allInstance;
	}

	private double calculateAccuracy(ArrayList<SingleInstance> testSegment, ArrayList<SingleInstance> trainSegment) {
		double totalCorrect = 0.0;

		for (int i = 0; i < testSegment.size(); i++) {
			double predictResult = getPrediction(testSegment.get(i), trainSegment);
			double test = testSegment.get(i).getResult();

			if (predictResult == test) {
				totalCorrect += 1.0;
				//System.out.println("correct predict on test instance number : " + (i + 1));
				predictionList.add(String.format("correct predict on test instance number : " + (i + 1)));
			}
		}

		//System.out.println("total correct prediction : " + (int) totalCorrect);
		predictionList.add(String.format("total correct prediction : " + (int) totalCorrect));

		return (totalCorrect / testSegment.size());
	}

	private double getPrediction(SingleInstance singleInstance, ArrayList<SingleInstance> trainSegment) {

		double probabilityOfAns[] = new double[classFeature.getSet().size()];
		int index = 0;

		for (double tempRes : classFeature.getSet()) {
			double tempAns = 1;
			tempAns *= ProbabilityOf(tempRes, trainSegment);
			for (int j = 0; j < singleInstance.getAttributes().size(); j++) {
				tempAns *= probabilityOfAttributeAndClass(tempRes, singleInstance.getAttributes().get(j), j,
						trainSegment) / ProbabilityOf(tempRes, trainSegment);
			}
			probabilityOfAns[index] = tempAns;
			index++;
		}

		double finalRes = -10000.00;
		index = 0;
		double HighestValue = -3.0;
		for (double tempRes : classFeature.getSet()) {
			if (probabilityOfAns[index] > HighestValue) {
				HighestValue = probabilityOfAns[index];
				finalRes = tempRes;
			}
			index++;
		}

		return finalRes;
	}

	private double probabilityOfAttributeAndClass(double tempRes, Double valueOfAttribute, int indexOfAttribute,
			ArrayList<SingleInstance> trainSegment) {

		double count = 0.0;
		for (int i = 0; i < trainSegment.size(); i++) {
			double tempAttributeValue = trainSegment.get(i).getAttributes().get(indexOfAttribute);
			double tempResOfTrainAttribute = trainSegment.get(i).getResult();
			if (tempAttributeValue == valueOfAttribute && tempRes == tempResOfTrainAttribute) {
				count++;
			}
		}
		return (count / trainSegment.size());
	}

	private double ProbabilityOf(double tempRes, ArrayList<SingleInstance> trainSegment) {
		double count = 0.0;
		for (int i = 0; i < trainSegment.size(); i++) {
			if (trainSegment.get(i).getResult() == tempRes)
				count += 1;
		}
		return count / trainSegment.size();
	}

}
