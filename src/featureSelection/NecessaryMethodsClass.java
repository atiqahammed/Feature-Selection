/*--------------------------------------------------------------------------
implemented by Atiq Ahammed
last edited : 25-04-2017


--------------------------------------------------------------------------*/

package featureSelection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class NecessaryMethodsClass {

	private Iterator<Double> itr, itr2;

	public double mutualInformationUsingJointProbality(Feature X, Feature Y) {
		double mutualInformation = 0.0;

		double[] XValue = X.getFeatureValues();
		double[] YValue = Y.getFeatureValues();

		Map<Double, Integer> xMap = X.getMap();
		Set<Double> xSet = X.getSet();
		Map<Double, Integer> yMap = Y.getMap();
		Set<Double> ySet = Y.getSet();

		itr = xSet.iterator();
		while (itr.hasNext()) {
			double xValue = itr.next();
			itr2 = ySet.iterator();

			while (itr2.hasNext()) {
				double yValue = itr2.next();
				double count = 0.0;

				for (int i = 0; i < XValue.length; i++) {
					if (XValue[i] == xValue && YValue[i] == yValue)
						count += 1.0;
				}

				double jointProbability = count / XValue.length;

				if (jointProbability == 0)
					continue;
				
				double probabilityOfXiValue = (double) xMap.get(xValue) / XValue.length;
				double probabilityOfYiValue = (double) yMap.get(yValue) / XValue.length;
				double probabilityForLogFunction = jointProbability	/ (probabilityOfXiValue * probabilityOfYiValue);
				
				double TempResult = jointProbability * (Math.log(probabilityForLogFunction) / Math.log(2.0));
				mutualInformation += TempResult;
			}
		}
		return mutualInformation;
	}
	
	
	public double mutualInformationUsingDoubleEntropy(Feature X, Feature Y){
		double mutualInformation = 0.0;
		mutualInformation += X.getEntropy() + Y.getEntropy() - jointEntropyOf(X, Y);
		return mutualInformation;
	}


	private double jointEntropyOf(Feature X, Feature Y) {
		double entropy = 0.0;
		double[] XValue = X.getFeatureValues();
		double[] YValue = Y.getFeatureValues();

		Map<Double, Integer> xMap = X.getMap();
		Set<Double> xSet = X.getSet();
		Map<Double, Integer> yMap = Y.getMap();
		Set<Double> ySet = Y.getSet();
		
		itr = xSet.iterator();
		while (itr.hasNext()) {
			double xValue = itr.next();
			itr2 = ySet.iterator();

			while (itr2.hasNext()) {
				double yValue = itr2.next();
				double count = 0.0;

				for (int i = 0; i < XValue.length; i++) 
					if (XValue[i] == xValue && YValue[i] == yValue)
						count += 1.0;

				double jointProbability = count / XValue.length;

				if (jointProbability == 0)
					continue;
				
				double TempResult = jointProbability * (Math.log(jointProbability) / Math.log(2.0));
				entropy += TempResult;
			}
		}
		
		if(entropy < 0.0)
			entropy *= -1;
		
		return entropy;
	}


	public ArrayList<Feature> getRankedFeature(ArrayList<Feature> myFeatureList, Feature classFeature) {
		
		ArrayList<Feature> rankedFeatureList = new ArrayList<Feature>();
		showAllmutualInformation(myFeatureList, classFeature);
		// highest mutual informed feature feature has ranking position 1
		Feature featureToSelsect = getFirstFeature(myFeatureList, classFeature); 
		myFeatureList.remove(featureToSelsect);
		rankedFeatureList.add(featureToSelsect);
		firstFeatureRankingMessage(rankedFeatureList, myFeatureList);
		
		while(!myFeatureList.isEmpty()){
			featureToSelsect = getNextFeature(myFeatureList, rankedFeatureList, classFeature);
			myFeatureList.remove(featureToSelsect);
			rankedFeatureList.add(featureToSelsect);
			updateMessage(rankedFeatureList, myFeatureList);
		}
		System.out.println();
		return rankedFeatureList;
	}

	private void updateMessage(ArrayList<Feature> rankedFeatureList, ArrayList<Feature> myFeatureList) {
		System.out.println();
		System.out.println("Ranked feature size :  " + rankedFeatureList.size());
		viewFeatureIntheSet(rankedFeatureList);
		System.out.println("features not yet in ranked :  " + myFeatureList.size());
		viewFeatureIntheSet(myFeatureList);
		
	}

	private void firstFeatureRankingMessage(ArrayList<Feature> rankedFeatureList, ArrayList<Feature> myFeatureList) {
		System.out.println("\n1st feature is selected for max mutual information.");	
		updateMessage(rankedFeatureList, myFeatureList);
		
	}


	private void showAllmutualInformation(ArrayList<Feature> myFeatureList, Feature classFeature) {
		System.out.println("Mutual Information(Terget Class, Feature_xi):\n");
		for(int i=0; i<myFeatureList.size(); i++){
			System.out.print("Mutual information of ( ");
			System.out.print("target class; " + myFeatureList.get(i).getFeatureIndex() + " no feature) =  ");
			double mi = mutualInformationUsingDoubleEntropy(classFeature, myFeatureList.get(i));
			System.out.println(mi);
		}
	}


	private Feature getFirstFeature(ArrayList<Feature> myFeatureList, Feature classFeature) {
		int index = 0;
		double[] mutualInformations = new double[myFeatureList.size()];
		
		for (int i = 0; i < myFeatureList.size(); i++)
			mutualInformations[i] = mutualInformationUsingJointProbality(myFeatureList.get(i), classFeature);

		double highestScored = mutualInformations[0];
		for (int i = 1; i < mutualInformations.length; i++)
			if (highestScored < mutualInformations[i]) {
				highestScored = mutualInformations[i];
				index = i;
			}

		return myFeatureList.get(index);
	}


	private void viewFeatureIntheSet(ArrayList<Feature> featureList) {
		System.out.println();
		for(int i=0; i<featureList.size(); i++)
			System.out.println("feature No  " + featureList.get(i).getFeatureIndex());
		System.out.println();
		
	}


	private Feature getNextFeature(ArrayList<Feature> myFeatureList, ArrayList<Feature> selectedFeatureList,
			Feature classFeature) {
		
		double score[] = new double[myFeatureList.size()];
		
		for(int i=0; i<myFeatureList.size(); i++) {
			Feature temp = myFeatureList.get(i);
			score[i] = mutualInformationUsingJointProbality(temp, classFeature) - (mutualInformationWithSeletedFeature
					(temp, selectedFeatureList)/selectedFeatureList.size());
		}
		
		int selectedIndex = 0;
		double maxScore = score[0];
		for(int i=0; i<myFeatureList.size(); i++){
			if(score[i] > maxScore){
				maxScore = score[i];
				selectedIndex = i;
			}
			System.out.println(myFeatureList.get(i).featureIndex + " has Score = " + score[i]);
		}
		
		return myFeatureList.get(selectedIndex);
	}


	private double mutualInformationWithSeletedFeature(Feature feature, ArrayList<Feature> selectedFeatureList) {
		double totalMutualInformation = 0.0;
		for(int i=0; i<selectedFeatureList.size(); i++) 
			totalMutualInformation += mutualInformationUsingDoubleEntropy(feature, selectedFeatureList.get(i));
		return totalMutualInformation;
	}
}
