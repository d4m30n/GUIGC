package nz.ac.waikato.orca;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUIGCBench extends Application {

	private GUIGCSettingsPane settingsPane = new GUIGCSettingsPane();
	private Map<String, String> params;
	private Button startButton;

	// System Parameters
	private boolean autoRun = false;
	private boolean runAsyncGCOnSleep = false;
	private int timeInMinutes = 1;
	private int skipPrintOutput = 0;
	private long threadID = Thread.currentThread().getId();

	private void getSystemParameters() {
		String value = params.get("autoRun");
		if (value != null && value.equals("true")) {
			this.autoRun = true;
		}
		value = params.get("asyncGCOnSleep");
		if (value != null && value.equals("true")) {
			this.runAsyncGCOnSleep = true;
		}
		value = params.get("skip");
		if (value != null) {
			try {
				int tmp = Integer.parseInt(value);
				this.skipPrintOutput = tmp;
			} catch (NumberFormatException nfe) {
			}
		}
		value = params.get("runtime");
		if (value != null) {
			try {
				int tmp = Integer.parseInt(value);
				timeInMinutes = tmp;
			} catch (NumberFormatException nfe) {

			}
		}
		parseProgramParameter("reps", settingsPane.reps);
		parseProgramParameter("seed", settingsPane.seed);
		parseProgramParameter("depth", settingsPane.depth);
		parseProgramParameter("breadth", settingsPane.breadth);
		parseProgramParameter("buttons", settingsPane.buttons);
		parseProgramParameter("sleep", settingsPane.sleep);
		parseProgramParameter("hash", settingsPane.hash);
	}

	private void parseProgramParameter(String id, ParameterInterface<?> setting) {
		String value = params.get(id);
		if (value != null) {
			try {
				double tmp = Double.parseDouble(value);
				setting.set(tmp);
			} catch (NumberFormatException nfe) {
				return;
			}
		}
		return;
	}

	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage primaryStage) {
		params = super.getParameters().getNamed();
		try {
			getSystemParameters();
			PrintStream fileOut = new PrintStream("/dev/null");
			System.setErr(fileOut);

			String gcNames = "";
			for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
				gcNames += ",#" + gc.getName();
				gcNames += "," + gc.getName() + "(ms)";
			}

			primaryStage.setTitle("GUI GC Bench");

			startButton = new Button("Start!");
			startButton.setOnAction(this::startTest);
			startButton.setPrefWidth(300);

			VBox vbox = new VBox(settingsPane, startButton);
			vbox.setSpacing(20);
			vbox.setAlignment(Pos.CENTER);
			vbox.setStyle("-fx-font-size:30px");

			Scene scene = new Scene(vbox, 650, 500);

			primaryStage.setScene(scene);
			primaryStage.show();

			if (autoRun) {
				// settingsPane.setPrameters(seed);
				// System.out.println("Create,Show,Firing,Close,Sleep,Total" + gcNames);
				startButton.fire();
				System.exit(0);
			} else {
				// System.out.println("Create,Show,Firing,Close,Sleep,Total" + gcNames);
			}
		} catch (OutOfMemoryError oome) {
			System.out.println("OutOfMemoryError");
			System.exit(1);
		} catch (FileNotFoundException fnfe) {
			System.out.println(fnfe);
		}
	}

	public void startTest(ActionEvent event) {
		ControllerInterface controllerInterface = new ControllerNULL();
		MeasureInterface measureInterface = new MeasureSystem(threadID);
		ParameterInterface<?>[] parameters = { settingsPane.hash, settingsPane.sleep, settingsPane.buttons,
				settingsPane.depth, settingsPane.breadth };
		settingsPane.depth.setWeight(ParameterInterface.Weights.IGNORE);
		settingsPane.breadth.setWeight(ParameterInterface.Weights.IGNORE);
		settingsPane.sleep.setWeight(ParameterInterface.Weights.NEGATIVE);

		try {
			Controller controller = new Controller(controllerInterface, measureInterface, parameters, timeInMinutes,
					TimeUnit.MINUTES);
			controller.start(true, skipPrintOutput);
			while (controller.isRunning()) {
				// int reps = settingsPane.getReps();
				int seed = settingsPane.getSeed();
				int depth = settingsPane.getDepth();
				int breadth = settingsPane.getBreadth();
				int nButtons = settingsPane.getNButtons();
				int sleepTime = settingsPane.getSleepTime();
				controllerInterface.get();
				GUIGCStage.runTest(settingsPane, seed, depth, breadth, nButtons, sleepTime, runAsyncGCOnSleep);
			}
		} catch (Exception e) {

		}
	}
}
