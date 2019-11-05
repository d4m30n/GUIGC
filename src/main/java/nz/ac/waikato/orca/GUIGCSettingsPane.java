package nz.ac.waikato.orca;

import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.GridPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;
import javafx.geometry.Pos;
import javafx.event.ActionEvent;

import java.util.Random;

public class GUIGCSettingsPane extends HBox {

	final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
	final int MIN_CHAR = 1;
	final int MAX_CHAR = 50;
	private TextField repsField = new TextField("1000");
	private TextField seedField = new TextField("438");
	private TextField depthField = new TextField("8");
	private TextField breadthField = new TextField("2");
	private TextField nButtonsField = new TextField("1");
	private TextField sleepTimeField = new TextField("100");
	private TextField hashTimesField = new TextField("0");
	private TextField randomDeleteField = new TextField("0");
	private TextField depthDeleteField = new TextField("0");

	public final ParameterInteger reps = new ParameterInteger(1000, null, 1, "Reps");
	public final ParameterInteger seed = new ParameterInteger(438, "Seed");
	public final ParameterInteger depth = new ParameterInteger(8, 10, 1, "Depth");
	public final ParameterInteger breadth = new ParameterInteger(2, 10, 1, "Breadth");
	public final ParameterInteger buttons = new ParameterInteger(1, null, 1, "Buttons");
	public final ParameterInteger sleep = new ParameterInteger(1, null, 1, "Sleep");
	public final ParameterInteger hash = new ParameterInteger(1, null, 1, "Hash");
	public final ParameterFloat deleteRandom = new ParameterFloat(0, 50, 0, "Random Delete");
	public final ParameterInteger depthDelete = new ParameterInteger(0, 9, 0, "Depth Delete");

	private Random rand = null; // The random number generator.

	/**
	 * AUTHOR: Harry McCarthy Simple methord that checks if the random number
	 * generator has been set Need to check this when the program is running so cant
	 * set it when the object is created due to needing the seed.
	 */
	private void randCheck() {
		if (rand == null) {
			rand = new Random(getSeed());
		}
	}

	/**
	 * AUTHOR: Harry McCarthy Generates a random number from the seed in the
	 * settings.
	 * 
	 * @param low  The lowist number in the random range.
	 * @param high The highest number in the random range.
	 * @return Returns a random number between the range give.
	 */
	public int getRandomNumber(int low, int high) {
		randCheck();
		return rand.nextInt(high - low) + low;
	}

	public boolean shouldIDelete() {
		Float deletePercentage = getDeletePercentage() * 100;
		int randomNumber = getRandomNumber(0, 100 + 1);
		if (randomNumber >= deletePercentage) {
			return false;
		}
		return true;
	}

	/**
	 * AUTHOR: Harry McCarthy Generate a random string on the lenght defined above.
	 * 
	 * @return A random String
	 */
	public String randomString() {
		randCheck();
		String returnString = "";
		int numChars = getRandomNumber(MIN_CHAR, MAX_CHAR);
		for (int place = 0; place < numChars; place++) {
			returnString += ALPHABET.charAt(getRandomNumber(0, ALPHABET.length()));
		}
		return returnString;
	}

	public void setPrameters(int seed) {
		seedField.setText(seed + "");
		int depth = getRandomNumber(1, 10 + 1);
		int breadth = getRandomNumber(1, 10 + 1);
		int buttons = getRandomNumber(1, 3 + 1);
		int sleeptime = getRandomNumber(0, 100 + 1);
		int hashreps = getRandomNumber(0, 1000 + 1);
		float deletePercent = ((float) getRandomNumber(0, 100 + 1) / 100);
		int mindeletedepth = getRandomNumber(3, 5 + 1);
		System.out.println("reps,seed,depth,breadth,buttons,sleeptime,hashreps,delete percent,min delete depth");
		System.out.println("1000," + seed + "," + depth + "," + breadth + "," + buttons + "," + sleeptime + ","
				+ hashreps + "," + deletePercent + "," + mindeletedepth);
		repsField.setText("1000");
		depthField.setText(depth + "");
		breadthField.setText(breadth + "");
		nButtonsField.setText(buttons + "");
		sleepTimeField.setText(sleeptime + "");
		hashTimesField.setText(hashreps + "");
		randomDeleteField.setText(deletePercent + "");
		depthDeleteField.setText(mindeletedepth + "");
	}

	public int getHashNumber() {
		return hash.get();
	}

	public float getDeletePercentage() {
		return deleteRandom.get();
	}

	Button buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonG, buttonH;

	public GUIGCSettingsPane() {
		buttonA = new Button("Preset A");
		buttonA.setOnAction(this::presetA);

		buttonB = new Button("Preset B");
		buttonB.setOnAction(this::presetB);

		buttonC = new Button("Preset C");
		buttonC.setOnAction(this::presetC);

		buttonD = new Button("Preset D");
		buttonD.setOnAction(this::presetD);

		buttonE = new Button("Preset E");
		buttonE.setOnAction(this::presetE);

		buttonF = new Button("Preset F");
		buttonF.setOnAction(this::presetF);

		buttonG = new Button("Preset G");
		buttonG.setOnAction(this::presetG);

		buttonH = new Button("Preset H");
		buttonH.setOnAction(this::presetH);

		VBox presetBox = new VBox(buttonA, buttonB, buttonC, buttonD, buttonE, buttonF, buttonG, buttonH);
		presetBox.setSpacing(20);

		GridPane grid = new GridPane();
		grid.setAlignment(Pos.CENTER);
		GridPane.setRowSpan(grid, null);
		GridPane.setColumnSpan(grid, null);

		Label repsLabel = new Label("Reps: ");
		repsField.setPrefWidth(150);
		grid.add(repsLabel, 0, 0);
		grid.add(repsField, 1, 0);

		Label seedLabel = new Label("Seed: ");
		seedField.setPrefWidth(150);
		grid.add(seedLabel, 0, 1);
		grid.add(seedField, 1, 1);

		Label depthLabel = new Label("Depth: ");
		depthField.setPrefWidth(150);
		grid.add(depthLabel, 0, 2);
		grid.add(depthField, 1, 2);

		Label breadthLabel = new Label("Breadth: ");
		breadthField.setPrefWidth(150);
		grid.add(breadthLabel, 0, 3);
		grid.add(breadthField, 1, 3);

		Label nButtonsLabel = new Label("#Buttons: ");
		nButtonsField.setPrefWidth(150);
		grid.add(nButtonsLabel, 0, 4);
		grid.add(nButtonsField, 1, 4);

		Label sleepTimeLabel = new Label("Sleep Time (ms): ");
		sleepTimeField.setPrefWidth(150);
		grid.add(sleepTimeLabel, 0, 5);
		grid.add(sleepTimeField, 1, 5);

		Label hashTimesLabel = new Label("Number of time to run Hash (ms): ");
		hashTimesField.setPrefWidth(150);
		grid.add(hashTimesLabel, 0, 6);
		grid.add(hashTimesField, 1, 6);

		Label randomDeleteLabel = new Label("Delete Percentage: ");
		randomDeleteField.setPrefWidth(150);
		grid.add(randomDeleteLabel, 0, 7);
		grid.add(randomDeleteField, 1, 7);

		Label depthDeleteLabel = new Label("Start Delete Depth: ");
		depthDeleteField.setPrefWidth(150);
		grid.add(depthDeleteLabel, 0, 8);
		grid.add(depthDeleteField, 1, 8);

		super.getChildren().addAll(presetBox, grid);
		super.setSpacing(20);
		super.setAlignment(Pos.CENTER);
		super.setStyle("-fx-font-size:30px");
	}

	public int getReps() {
		return reps.get();
	}

	public int getSeed() {
		return seed.get();
	}

	public int getDepth() {
		return depth.get();
	}

	public int getBreadth() {
		return breadth.get();
	}

	public int getNButtons() {
		return buttons.get();
	}

	public int getSleepTime() {
		return sleep.get();
	}

	public int getDepthDelete() {
		return depthDelete.get();
	}

	public void presetA(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("2");
		nButtonsField.setText("1");
		sleepTimeField.setText("100");
		hashTimesField.setText("0");
		randomDeleteField.setText("0");
		depthDeleteField.setText("0");
	}

	public void presetB(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("1");
		nButtonsField.setText("1");
		sleepTimeField.setText("100");
		hashTimesField.setText("0");
		randomDeleteField.setText("0");
		depthDeleteField.setText("0");
	}

	public void presetC(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("2");
		nButtonsField.setText("2");
		sleepTimeField.setText("100");
		hashTimesField.setText("0");
		randomDeleteField.setText("0");
		depthDeleteField.setText("0");
	}

	public void presetD(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("2");
		nButtonsField.setText("1");
		sleepTimeField.setText("0");
		hashTimesField.setText("0");
		randomDeleteField.setText("0");
		depthDeleteField.setText("0");
	}

	public void presetE(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("2");
		nButtonsField.setText("1");
		sleepTimeField.setText("0");
		hashTimesField.setText("100");
		randomDeleteField.setText("0.10");
		depthDeleteField.setText("3");
	}

	public void presetF(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("2");
		nButtonsField.setText("1");
		sleepTimeField.setText("0");
		hashTimesField.setText("1000");
		randomDeleteField.setText("0.30");
		depthDeleteField.setText("3");
	}

	public void presetG(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("2");
		nButtonsField.setText("1");
		sleepTimeField.setText("0");
		hashTimesField.setText("100");
		randomDeleteField.setText("0.10");
		depthDeleteField.setText("3");
	}

	public void presetH(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("2");
		nButtonsField.setText("1");
		sleepTimeField.setText("0");
		hashTimesField.setText("1000");
		randomDeleteField.setText("0.30");
		depthDeleteField.setText("3");
	}
}
