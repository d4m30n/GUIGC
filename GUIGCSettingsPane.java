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
	TextField repsField = new TextField("1000");
	TextField seedField = new TextField("438");
	TextField depthField = new TextField("8");
	TextField breadthField = new TextField("2");
	TextField nButtonsField = new TextField("1");
	TextField sleepTimeField = new TextField("100");
	TextField hashTimesField = new TextField("0");
	TextField randomDeleteField = new TextField("0");

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

	public int getHashNumber() {
		return Integer.parseInt(this.hashTimesField.getText());
	}

	public float getDeletePercentage() {
		return Float.parseFloat(this.randomDeleteField.getText());
	}

	Button buttonA, buttonB, buttonC, buttonD;

	public GUIGCSettingsPane() {
		buttonA = new Button("Preset A");
		buttonA.setOnAction(this::presetA);

		buttonB = new Button("Preset B");
		buttonB.setOnAction(this::presetB);

		buttonC = new Button("Preset C");
		buttonC.setOnAction(this::presetC);

		buttonD = new Button("Preset D");
		buttonD.setOnAction(this::presetD);

		VBox presetBox = new VBox(buttonA, buttonB, buttonC, buttonD);
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

		super.getChildren().addAll(presetBox, grid);
		super.setSpacing(20);
		super.setAlignment(Pos.CENTER);
		super.setStyle("-fx-font-size:30px");
	}

	public int getReps() {
		return Integer.parseInt(repsField.getText());
	}

	public int getSeed() {
		return Integer.parseInt(seedField.getText());
	}

	public int getDepth() {
		return Integer.parseInt(depthField.getText());
	}

	public int getBreadth() {
		return Integer.parseInt(breadthField.getText());
	}

	public int getNButtons() {
		return Integer.parseInt(nButtonsField.getText());
	}

	public int getSleepTime() {
		return Integer.parseInt(sleepTimeField.getText());
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
	}

	public void presetE(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("2");
		nButtonsField.setText("1");
		sleepTimeField.setText("100");
		hashTimesField.setText("100");
		randomDeleteField.setText("0.00001");
	}

	public void presetF(ActionEvent event) {
		repsField.setText("1000");
		seedField.setText("438");
		depthField.setText("8");
		breadthField.setText("2");
		nButtonsField.setText("1");
		sleepTimeField.setText("100");
		hashTimesField.setText("1000");
		randomDeleteField.setText("0.001");
	}
}
