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
import nz.ac.waikato.orca.ControllerInterface.controllerType;

public class GUIGCBench extends Application {
	private static final String CLASS = "GUIGCBench";

	private GUIGCSettingsPane settingsPane = new GUIGCSettingsPane();
	private Map<String, String> params;
	private Button startButton;

	// System Parameters
	private boolean autoRun = false;
	private boolean runAsyncGCOnSleep = false;
	private int timeInMinutes = 1;
	private int skipPrintOutput = 0;
	private controllerType cType = controllerType.NULL;
	private Double _cpuSetpoint = null;
	private Double _memorySetpoint = null;
	private int measureInterval = -1;

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
		value = params.get("minterval");
		if (value != null) {
			try {
				int tmp = Integer.parseInt(value);
				measureInterval = tmp;
			} catch (NumberFormatException nfe) {
				measureInterval = -1;
			}
		}
		value = params.get("cpu");
		if (value != null) {
			try {
				double tmp = Double.parseDouble(value);
				_cpuSetpoint = tmp;
			} catch (NumberFormatException nfe) {
				_cpuSetpoint = null;
			}
		}
		value = params.get("memory");
		if (value != null) {
			try {
				double tmp = Double.parseDouble(value);
				_memorySetpoint = tmp;
			} catch (NumberFormatException nfe) {
				_memorySetpoint = null;
			}
		}
		value = params.get("cType");
		if (value != null) {
			if (value.compareTo(controllerType.NULL.get()) == 0)
				cType = controllerType.NULL;
			else if (value.compareTo(controllerType.PID.get()) == 0)
				cType = controllerType.PID;
			else if (value.compareTo(controllerType.LQR.get()) == 0)
				cType = controllerType.LQR;
			else
				cType = controllerType.NULL;
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

	private ControllerLQR createLQRController(MeasureInterface ms) {
		double[][] A = { { -0.007, 0 }, { 0, -0.005 } };
		double[][] B = { { 0.09997, -0.25809, 0.40654, 0.83393, 1.88689 },
				{ 0.0002844, -0.0029369, 0.0053073, 0.0188238, 0.0456887 } };
		double[][] C = { { 1, 0 }, { 0, 1 } };
		double[][] D = { { 0, 0, 0, 0, 0 }, { 0, 0, 0, 0, 0 } };
		double[][] Q = { { 1, 0 }, { 0, 1 } };
		double[][] R = { { 0.1, 0, 0, 0, 0 }, { 0, 0.1, 0, 0, 0 }, { 0, 0, 5, 0, 0 }, { 0, 0, 0, 3, 0 },
				{ 0, 0, 0, 0, 4 } };
		ms.measure();
		double[] measurments = ms.getMeasurements();
		double[] x = { ModelLQR.encodeMeasurement(measurments[0], ModelLQR.CPU),
				ModelLQR.encodeMeasurement(measurments[1], ModelLQR.MEMORY) };
		double[] u = { settingsPane.hash.get(), settingsPane.sleep.get(), settingsPane.buttons.get(),
				settingsPane.breadth.get(), settingsPane.depth.get() };
		int[] uIDs = { settingsPane.hash.ID, settingsPane.sleep.ID, settingsPane.buttons.ID, settingsPane.breadth.ID,
				settingsPane.depth.ID };
		ControllerLQR controller = null;
		try {
			controller = new ControllerLQR(A, B, C, D, Q, R, x, u, uIDs);
		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		return controller;
	}

	public void startTest(ActionEvent event) {
		MeasureInterface measureInterface = null;
		if (measureInterval != -1) {
			try {
				measureInterface = new MeasureSystem(measureInterval, TimeUnit.SECONDS, _cpuSetpoint, _memorySetpoint);
			} catch (Exception e) {
				e.printStackTrace();
				System.exit(-2);
			}
		} else {
			measureInterface = new MeasureSystem(_cpuSetpoint, _memorySetpoint);
		}
		ControllerInterface controllerInterface;
		switch (cType) {
			case NULL:
				controllerInterface = new ControllerNULL();
				break;
			case LQR:
				controllerInterface = createLQRController(measureInterface);
				break;
			case PID:
				String[] KValues = params.get("K").split(":");
				int intHistory = -1;
				try {
					intHistory = Integer.parseInt(params.get("intHistory"));
				} catch (Exception e) {

				}
				controllerInterface = new ControllerPID(Double.parseDouble(KValues[0]), Double.parseDouble(KValues[1]),
						Double.parseDouble(KValues[2]), intHistory);
				break;
			default:
				controllerInterface = new ControllerNULL();
		}

		ParameterInterface<?>[] parameters = { settingsPane.hash, settingsPane.sleep, settingsPane.buttons,
				settingsPane.depth, settingsPane.breadth };
		settingsPane.sleep.setWeight(ParameterInterface.Weights.NEGATIVE);
		settingsPane.depth.setWeight(ParameterInterface.Weights.IGNORE);
		settingsPane.breadth.setWeight(ParameterInterface.Weights.IGNORE);
		settingsPane.buttons.setWeight(ParameterInterface.Weights.IGNORE);

		try {
			Controller controller = new Controller(controllerInterface, measureInterface, parameters, timeInMinutes,
					TimeUnit.MINUTES, settingsPane.seed.get());
			controller.start(true, skipPrintOutput);
			while (controller.isRunning()) {
				// int reps = settingsPane.getReps();
				int seed = settingsPane.getSeed();
				int depth = settingsPane.getDepth();
				int breadth = settingsPane.getBreadth();
				int nButtons = settingsPane.getNButtons();
				int sleepTime = settingsPane.getSleepTime();
				double[] evaluated = controllerInterface.get();
				/*
				 * if (evaluated != null) { System.out.printf("%s-CPU:%.2f\tMemory:%.2f%n",
				 * CLASS, Math.exp(evaluated[0] + intercepts[0]), Math.exp(evaluated[1] +
				 * intercepts[1]));
				 */
				double cpu = measureInterface.getMeasurements()[0];
				GUIGCStage.runTest(settingsPane, seed, depth, breadth, nButtons, sleepTime, runAsyncGCOnSleep);
			}
		} catch (Exception e) {
			System.out.printf("%s-ERROR:%s%n", CLASS, e.toString());
		}
	}
}
