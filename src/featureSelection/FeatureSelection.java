/*--------------------------------------------------------------------------
implemented by Atiq Ahammed
last edited : 25-04-2017


--------------------------------------------------------------------------*/

package featureSelection;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFileChooser;

import naiveBayesMethod.NaiveBayesMethod;

public class FeatureSelection {

	private File featureFile;
	private int numberOfFeatures;
	private int numberOfInstances;
	private double[][] features;
	private int[] featureType;
	private Feature[] allFeature;
	private NecessaryMethodsClass myMethods = new NecessaryMethodsClass();
	private ArrayList<Feature> myFeatureList = new ArrayList<Feature>();
	private ArrayList<Feature> rankedFeature = new ArrayList<Feature>();
	private ArrayList<Feature> backwardSelectedSubset = new ArrayList<Feature>();
	private ArrayList<Feature> forwardSelectedSubset = new ArrayList<Feature>();
	private Set<Feature> selectedFeatureSubset = new HashSet<Feature>();
	private Feature classFeature;

	private ArrayList<String> totalSelectedFeature = new ArrayList<String>();
	private ArrayList<String> forwardSelectedFeatureInfo = new ArrayList<String>();
	private ArrayList<String> backwardSelectedFeatureInfo = new ArrayList<String>();
	private ArrayList<String> backwardselectionAccouracyInfo = new ArrayList<String>();
	private ArrayList<String> forwardselectionAccouracyInfo = new ArrayList<String>();
	public  ArrayList<String> predictionList = new ArrayList<String>();
	
	
	public FeatureSelection() throws IOException {
		// inputFeatureFile();
		inputFeatureFile2ndEdition();
		parseFeatures();
		addFeatureInATempSet();
		// testMethod();
		rank_feature();
		backwardSelection();
		forWardSelection();
		unionBackwordAndForward();
		outputProcess();

		// out.close();

	}

	private void outputProcess() {
		Iterator itr = selectedFeatureSubset.iterator();
		totalSelectedFeature.add(String.format("Total Seleced Features :: " + selectedFeatureSubset.size()));
		while (itr.hasNext()) {
			Feature tempFeature = (Feature) itr.next();
			String temp = String.format("Feature number :: %d", tempFeature.getFeatureIndex());
			//System.out.println(temp);
			totalSelectedFeature.add(temp);
		}
		
		ResultFrame resultFrame = new ResultFrame(numberOfFeatures-1, selectedFeatureSubset.size(),  
				totalSelectedFeature, forwardselectionAccouracyInfo, forwardSelectedFeatureInfo, 
				  backwardSelectedFeatureInfo, backwardselectionAccouracyInfo, predictionList);
		resultFrame.run();

	}

	private void unionBackwordAndForward() {
		while (!forwardSelectedSubset.isEmpty()) {
			selectedFeatureSubset.add(forwardSelectedSubset.get(0));
			forwardSelectedSubset.remove(0);
		}

		while (!backwardSelectedSubset.isEmpty()) {
			selectedFeatureSubset.add(backwardSelectedSubset.get(0));
			backwardSelectedSubset.remove(0);
		}

		System.out.println("total selected feature: " + selectedFeatureSubset.size());

	}

	private void forWardSelection() {
		
		predictionList.add("*** Forward Selection ***");
		predictionList.add("*************************");
		
		System.out.println("Forward selection method.\n");
		forwardselectionAccouracyInfo.add("*** Forward Selection ***");
		NaiveBayesMethod naivebayesTest = new NaiveBayesMethod(classFeature, predictionList);
		ArrayList<Feature> tempFeatureSet1 = new ArrayList<Feature>();

		double accuracy = 0.0;
		forwardselectionAccouracyInfo.add("Initialy max accuarcy :: 0.0");
		//System.out.println(rankedFeature.size());

		for (int i = 0; i < rankedFeature.size(); i++) {
			System.out.println();
			Feature tempFeature = rankedFeature.get(i);
			tempFeatureSet1.add(tempFeature);
			System.out.println("After adding " + (i + 1) + " no feature, Number of feature in that set is : "
					+ tempFeatureSet1.size());
			
			forwardselectionAccouracyInfo.add(String.format("After adding %d no ranked feature, total feature in feature set is %d ", (i+1), tempFeatureSet1.size()));
			
			double tempAccuracy = naivebayesTest.getAccuracy(tempFeatureSet1);
			if (tempAccuracy <= accuracy) {
				System.out.println("after adding " + (i + 1)
						+ " no ranked feature, accuracy decreaced \nso, It should not be taken");
				
				forwardselectionAccouracyInfo.add(String.format("After adding " + (i + 1)
						+ " no ranked feature, accuracy decreaced"));
				forwardselectionAccouracyInfo.add("So, It should not be taken");
				
				tempFeatureSet1.remove(tempFeature);
			}

			else {
				accuracy = tempAccuracy;
				System.out.println("after adding " + (i + 1)
						+ " no ranked feature, accuracy increaced \n so, It should be includeed.");
				
				forwardselectionAccouracyInfo.add(String.format("After adding " + (i + 1)
						+ " no ranked feature, accuracy increaced"));
				forwardselectionAccouracyInfo.add("So, It should be Included");
				
				forwardSelectedSubset.add(tempFeature);
			}
			
			forwardselectionAccouracyInfo.add(String.format("This moment, max Accuracy is " + accuracy));
			forwardselectionAccouracyInfo.add(String.format("This moment number of selected feature :: " + forwardSelectedSubset.size()));
			
			
			
			System.out.println(accuracy);
			System.out.println("This moment number of selected feature: " + forwardSelectedSubset.size());
		}

		System.out.println("Total forward Features: " + forwardSelectedSubset.size());
		forwardSelectedFeatureInfo.add(String.format("Total Forword Selected Features :: " + forwardSelectedSubset.size()));
		for(int i=0;i<forwardSelectedSubset.size(); i++){
			String temp=String.format("Feature number :: %d", forwardSelectedSubset.get(i).getFeatureIndex());
			//System.out.println(temp);
			forwardSelectedFeatureInfo.add(temp);
		}
	}

	private void backwardSelection() {
		predictionList.add("*** Backward Selection ***");
		predictionList.add("**************************");
		
		System.out.println("backward selection method.\n");
		backwardselectionAccouracyInfo.add("*** Backward Selection ***");
		NaiveBayesMethod naivebayesTest = new NaiveBayesMethod(classFeature, predictionList);
		ArrayList<Feature> tempFeatureSet1 = (ArrayList<Feature>) rankedFeature.clone();

		double accuracy = naivebayesTest.getAccuracy(tempFeatureSet1);
		String tempStr = String.format("Total accuracy with all features :: %f", accuracy);
		backwardselectionAccouracyInfo.add(tempStr);

		for (int i = numberOfFeatures - 2; i >= 0; i--) {
			System.out.println();
			Feature tempFeature = tempFeatureSet1.get(i);
			tempFeatureSet1.remove(i);
			/*System.out.println("After removing " + (i + 1) + " no feature, Number of feature in that set is : "
					+ tempFeatureSet1.size());*/
			tempStr = String.format("After removing %d no ranked feature, total feature in feature set is %d ", (i+1), tempFeatureSet1.size());
			System.out.println(tempStr);
			backwardselectionAccouracyInfo.add(tempStr);

			double tempAccuracy = naivebayesTest.getAccuracy(tempFeatureSet1);
			if (tempAccuracy < accuracy) {
				System.out.println("after removing " + (i + 1)
						+ " no ranked feature, accuracy decreaced \n*****so, It should not be removed");
				backwardselectionAccouracyInfo.add(String.format("after removing " + (i + 1) + " no ranked feature, accuracy decreaced"));
				backwardselectionAccouracyInfo.add("*****so, It should not be removed");
				backwardSelectedSubset.add(tempFeature);
			}

			else {
				accuracy = tempAccuracy;
				System.out.println("after removing " + (i + 1)
						+ " no ranked feature, accuracy increaced \n so, It should be removed");
				backwardselectionAccouracyInfo.add(String.format("after removing " + (i + 1) + " no ranked feature, accuracy increaced "));
				backwardselectionAccouracyInfo.add("so, It should be removed");
			}
			System.out.println(accuracy);
			backwardselectionAccouracyInfo.add(String.format("This moment, max Accuracy is " + accuracy));
			backwardselectionAccouracyInfo.add(String.format("This moment number of selected feature :: " + backwardSelectedSubset.size()));
			System.out.println("This moment number of selected feature :: " + backwardSelectedSubset.size());
		}

		System.out.println("Total Backword Selected Features: " + backwardSelectedSubset.size());
		backwardSelectedFeatureInfo.add(String.format("Total Backword Selected Features :: " + backwardSelectedSubset.size()));
		for(int index = 0; index< backwardSelectedSubset.size(); index ++){
			String temp = String.format("Feature number :: %d", backwardSelectedSubset.get(index).getFeatureIndex());
			//System.out.println(temp);
			backwardSelectedFeatureInfo.add(temp);
		}
		

	}

	public ArrayList<String> getPredictionList() {
		return predictionList;
	}

	public void setPredictionList(ArrayList<String> predictionList) {
		this.predictionList = predictionList;
	}

	private void rank_feature() {
		ArrayList<Feature> tempFeatureList = new ArrayList<Feature>(myFeatureList);
		rankedFeature = myMethods.getRankedFeature(tempFeatureList, classFeature);
	}

	private void addFeatureInATempSet() {
		for (int i = 0; i < numberOfFeatures - 1; i++) {
			myFeatureList.add(allFeature[i]);
		}
	}

	private void parseFeatures() {
		allFeature = new Feature[numberOfFeatures];
		for (int i = 0; i < numberOfFeatures; i++) {
			if (featureType[i] == 0)
				allFeature[i] = new DiscreteFeature(features[i], i + 1);
			else if (featureType[i] < 0)
				allFeature[i] = new ContinousFeature(features[i], i + 1);
			else
				allFeature[i] = new ContinousFeature(features[i], featureType[i], i + 1);
		}
		classFeature = allFeature[numberOfFeatures - 1];
	}

	private void inputFeatureFile2ndEdition() throws IOException {
		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			featureFile = fc.getSelectedFile();
			Scanner input = new Scanner(featureFile);

			numberOfFeatures = input.nextInt();
			numberOfInstances = input.nextInt();
			features = new double[numberOfFeatures][];
			for (int i = 0; i < numberOfFeatures; i++)
				features[i] = new double[numberOfInstances];
			featureType = new int[numberOfFeatures];

			for (int i = 0; i < numberOfFeatures; i++)
				featureType[i] = input.nextInt();

			for (int i = 0; i < numberOfInstances; i++)
				for (int j = 0; j < numberOfFeatures; j++)
					features[j][i] = input.nextDouble();
		}

	}

	private void inputFeatureFile() throws IOException {
		FileReader inputFile;
		BufferedReader stream;

		JFileChooser fc = new JFileChooser();
		int returnVal = fc.showOpenDialog(null);

		if (returnVal == JFileChooser.APPROVE_OPTION) {
			featureFile = fc.getSelectedFile();
			inputFile = new FileReader(featureFile);
			stream = new BufferedReader(inputFile);

			String stringStream = stream.readLine();
			String[] numbers = stringStream.split(" ");

			numberOfFeatures = Integer.parseInt(numbers[0]);
			numberOfInstances = Integer.parseInt(numbers[1]);

			features = new double[numberOfFeatures][];
			for (int i = 0; i < numberOfFeatures; i++)
				features[i] = new double[numberOfInstances];

			stringStream = stream.readLine();
			String[] type = stringStream.split(" ");
			featureType = new int[numberOfFeatures];
			for (int i = 0; i < type.length; i++)
				featureType[i] = Integer.parseInt(type[i]);

			int i = 0;
			stringStream = stream.readLine();
			while (stringStream != null) {
				String[] values = stringStream.split(" ");
				for (int j = 0; j < values.length; j++) {
					double temp = Double.parseDouble(values[j]);
					features[j][i] = temp;
				}
				i++;
				stringStream = stream.readLine();
			}
			stream.close();
		}
	}

}
