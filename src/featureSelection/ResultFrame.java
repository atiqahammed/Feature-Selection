package featureSelection;

import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;

public class ResultFrame {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
/*	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResultFrame window = new ResultFrame();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}*/

	/**
	 * Create the application.
	 * @param predictionList 
	 * @param backwardselectionAccouracyInfo 
	 * @param backwardSelectedFeatureInfo 
	 * @param forwardSelectedFeatureInfo 
	 * @param forwardselectionAccouracyInfo 
	 * @param totalSelectedFeature 
	 * @param j 
	 * @param i 
	 * @param predictionList 
	 * @param backwardSelectedFeatureInfo 
	 * @param forwardSelectedFeatureInfo 
	 * @param totalSelectedFeature 
	 * @param j 
	 * @param i 
	 * @param predictionList2 
	 * @param backwardSelectedFeatureInfo2 
	 */
	public ResultFrame(int i, int j, ArrayList<String> totalSelectedFeature, ArrayList<String> forwardselectionAccouracyInfo, ArrayList<String> forwardSelectedFeatureInfo,
			ArrayList<String> backwardSelectedFeatureInfo, ArrayList<String> backwardselectionAccouracyInfo, ArrayList<String> predictionList) {
		initialize(i, j, totalSelectedFeature, forwardselectionAccouracyInfo, forwardSelectedFeatureInfo, backwardSelectedFeatureInfo, backwardselectionAccouracyInfo, predictionList);
	}
	
	
	public void run() {
		try {
			//ResultFrame window = new ResultFrame();
			frame.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 * @param predictionList 
	 * @param backwardselectionAccouracyInfo 
	 * @param backwardSelectedFeatureInfo 
	 * @param forwardSelectedFeatureInfo 
	 * @param forwardselectionAccouracyInfo 
	 * @param totalSelectedFeature 
	 * @param j 
	 * @param i 
	 */
	private void initialize(int i, int j, ArrayList<String> totalSelectedFeature, ArrayList<String> forwardselectionAccouracyInfo, ArrayList<String> forwardSelectedFeatureInfo, ArrayList<String> backwardSelectedFeatureInfo, ArrayList<String> backwardselectionAccouracyInfo, ArrayList<String> predictionList) {
		frame = new JFrame();
		frame.setBounds(100, 100, 1000, 444);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setResizable(false);
		frame.getContentPane().setLayout(null);
		
		JLabel featureNumberLabel = new JLabel("Number of feature :");
		featureNumberLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		featureNumberLabel.setBounds(42, 22, 161, 20);
		frame.getContentPane().add(featureNumberLabel);
		
		JLabel vewFeatureNumber = new JLabel("<feature number>");
		vewFeatureNumber.setFont(new Font("Tahoma", Font.BOLD, 16));
		vewFeatureNumber.setBounds(238, 22, 161, 20);
		vewFeatureNumber.setText(Integer.toString(i));
		frame.getContentPane().add(vewFeatureNumber);
		
		JLabel selectedFeatureLabel = new JLabel("Selected Feature :");
		selectedFeatureLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		selectedFeatureLabel.setBounds(52, 53, 149, 20);
		frame.getContentPane().add(selectedFeatureLabel);
		
		JLabel viewSelectedFeature = new JLabel("<selected>");
		viewSelectedFeature.setFont(new Font("Tahoma", Font.BOLD, 16));
		viewSelectedFeature.setBounds(238, 53, 161, 20);
		viewSelectedFeature.setText(Integer.toString(j));
		frame.getContentPane().add(viewSelectedFeature);
		
		JLabel SelectedFeatureList = new JLabel("Selected features");
		SelectedFeatureList.setFont(new Font("Tahoma", Font.BOLD, 13));
		SelectedFeatureList.setBounds(42, 120, 161, 20);
		frame.getContentPane().add(SelectedFeatureList);
		
		JComboBox selectedFeature = new JComboBox();
		selectedFeature.setModel(new DefaultComboBoxModel(totalSelectedFeature.toArray()));
		selectedFeature.setBounds(42, 146, 256, 20);
		frame.getContentPane().add(selectedFeature);
		
		JLabel AccuracyTestForward = new JLabel("Forward Selection Accuracy Testing");
		AccuracyTestForward.setFont(new Font("Tahoma", Font.BOLD, 13));
		AccuracyTestForward.setBounds(42, 194, 256, 20);
		frame.getContentPane().add(AccuracyTestForward);
		
		JComboBox comboBoxForword = new JComboBox();
		comboBoxForword.setModel(new DefaultComboBoxModel(forwardselectionAccouracyInfo.toArray()));
		comboBoxForword.setBounds(42, 225, 915, 20);
		frame.getContentPane().add(comboBoxForword);
		
		JLabel accuracyTestingBackward = new JLabel("Backward Selection Accuracy Testing");
		accuracyTestingBackward.setFont(new Font("Tahoma", Font.BOLD, 13));
		accuracyTestingBackward.setBounds(42, 256, 242, 20);
		frame.getContentPane().add(accuracyTestingBackward);
		
		JComboBox comboBoxBackward = new JComboBox();
		comboBoxBackward.setModel(new DefaultComboBoxModel(backwardselectionAccouracyInfo.toArray()));
		comboBoxBackward.setBounds(42, 284, 915, 20);
		frame.getContentPane().add(comboBoxBackward);
		
		JLabel PredictionFormate = new JLabel("Naive Bays Prediction Details");
		PredictionFormate.setFont(new Font("Tahoma", Font.BOLD, 13));
		PredictionFormate.setBounds(42, 324, 242, 20);
		frame.getContentPane().add(PredictionFormate);
		
		JComboBox comboBoxPrediction = new JComboBox();
		comboBoxPrediction.setModel(new DefaultComboBoxModel(predictionList.toArray()));
		comboBoxPrediction.setBounds(42, 355, 915, 20);
		frame.getContentPane().add(comboBoxPrediction);
		
		JLabel lblForwardSelectedFeatures = new JLabel("Forward selected features");
		lblForwardSelectedFeatures.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblForwardSelectedFeatures.setBounds(339, 121, 184, 20);
		frame.getContentPane().add(lblForwardSelectedFeatures);
		
		JComboBox forwordSelectedCombo = new JComboBox();
		forwordSelectedCombo.setModel(new DefaultComboBoxModel(forwardSelectedFeatureInfo.toArray()));
		forwordSelectedCombo.setBounds(339, 146, 256, 20);
		frame.getContentPane().add(forwordSelectedCombo);
		
		JLabel lblBackwardSelectedFeature = new JLabel("Backward selected features");
		lblBackwardSelectedFeature.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblBackwardSelectedFeature.setBounds(632, 121, 242, 20);
		frame.getContentPane().add(lblBackwardSelectedFeature);
		
		JComboBox backwardSelectedCombo = new JComboBox();
		backwardSelectedCombo.setModel(new DefaultComboBoxModel(backwardSelectedFeatureInfo.toArray()));
		backwardSelectedCombo.setBounds(630, 146, 256, 20);
		frame.getContentPane().add(backwardSelectedCombo);
		
		
		
		
		
	}
}
