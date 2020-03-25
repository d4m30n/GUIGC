package nz.ac.waikato.orca;

import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.text.TextFlow;
import javafx.scene.layout.*;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Button;
import java.util.Random;

import javafx.event.ActionEvent;

import java.io.UnsupportedEncodingException;
import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;

public class GUIGCStage extends Stage {
	private static int nextId = 0;
	private final int ID = nextId++;

	private Pane rootPane;
	private GUIGCSettingsPane settings; // Link to the initial settings.

	// private volatile int clicks = 0;

	private static ArrayList<GCStats> collectGCStats() {
		ArrayList<GCStats> gcList = new ArrayList<GCStats>();
		for (GarbageCollectorMXBean gc : ManagementFactory.getGarbageCollectorMXBeans()) {
			GCStats gcStats = new GCStats(System.currentTimeMillis(), gc.getName(), gc.getCollectionCount(),
					gc.getCollectionTime());
			gcList.add(gcStats);
		}
		return gcList;
	}

	private GUIGCStage(GUIGCSettingsPane settings, int seed, int depth, int breadth, int nButtons) {
		super.setTitle("GUI GC Stage " + ID);
		this.settings = settings;
		constructStage(depth, breadth, nButtons);
	}

	private Pane constructRandomPane() {
		Random random = new Random(settings.getSeed());
		switch (random.nextInt(9)) {
		case 0:
			return new AnchorPane();
		case 1:
			return new DialogPane();
		case 2:
			return new FlowPane();
		case 3:
			return new GridPane();
		case 4:
			return new HBox();
		case 5:
			return new StackPane();
		case 6:
			return new TextFlow();
		case 7:
			return new TilePane();
		default:
			return new VBox();
		}
	}

	private Pane constructNode(int depth, int breadth, int nButtons) {
		Pane parent = constructRandomPane();
		if (depth > 0) {
			for (int i = 0; i < breadth; i++) {
				parent.getChildren().addAll(constructNode(depth - 1, breadth, nButtons));
			}

			for (int i = 0; i < nButtons; i++) {
				Button button = new Button("Click");
				button.setOnAction(this::clicked);
				parent.getChildren().addAll(button);
			}
		}
		return parent;
	}

	private void constructStage(int depth, int breadth, int nButtons) {
		rootPane = constructNode(depth, breadth, nButtons);
		Scene scene = new Scene(rootPane, 300, 300);
		setScene(scene);
	}

	void clicked(ActionEvent event) {
		try {
			// clicks++;
			if (settings.getHashNumber() > 0) {
				// preform a hash when the button is clicked. NOTE: There is a setting that
				// determins the number of hashs.
				byte[] hashString = this.settings.randomString().getBytes("UTF-8");
				MessageDigest md = MessageDigest.getInstance("MD5");
				for (int times = 0; times < settings.getHashNumber(); times++) {
					md.reset();
					hashString = md.digest(hashString);
				}
				System.err.println("Hash: " + hashString);
			}
		} catch (NoSuchAlgorithmException nsae) {

		} catch (UnsupportedEncodingException uee) {

		}
	}

	private void paneDelete(Pane p, int place) {
		Iterator<Node> it = p.getChildren().iterator();
		while (it.hasNext()) {
			Node n = it.next();
			if (n instanceof Pane) {
				if (place >= settings.getDepthDelete() && settings.shouldIDelete()) {
					it.remove();
					return;
				}
				paneDelete((Pane) n, place++);
			}
		}
	}

	void clickButtons() {
		if (settings.getDepthDelete() != 0) {
			paneDelete(rootPane, 0);
		}
		clickButtons(rootPane);
	}

	void clickButtons(Pane parent) {
		if (parent == null) {
			return;
		}
		Iterator<Node> it = parent.getChildren().iterator();
		while (it.hasNext()) {
			Node item = it.next();
			if (item instanceof Button) {
				((Button) item).fire();
			}
			if (item instanceof Pane) {
				clickButtons((Pane) item);
			}
		}
	}

	public static void runTest(GUIGCSettingsPane settings, int seed, int depth, int breadth, int nButtons, int sleepTime,
			boolean runAsyncGCOnSleep) throws OutOfMemoryError {
		try {
			ArrayList<GCStats> gcs0 = collectGCStats();
			long[] t = new long[6];
			t[0] = System.currentTimeMillis();
			GUIGCStage testStage = new GUIGCStage(settings, seed, depth, breadth, nButtons);
			t[1] = System.currentTimeMillis();
			testStage.show();
			t[2] = System.currentTimeMillis();
			testStage.clickButtons();
			t[3] = System.currentTimeMillis();
			testStage.close();
			t[4] = System.currentTimeMillis();
			if (sleepTime > 0) {
				if (runAsyncGCOnSleep) {
					Runnable task = () -> {
						System.gc();
					};
					Thread thread = new Thread(task);
					thread.start();
				}
				Thread.sleep(sleepTime);
			}
			t[5] = System.currentTimeMillis();
			ArrayList<GCStats> gcs1 = collectGCStats();
			/**
			 * for (int i = 1; i < t.length; i++) { System.out.print((t[i] - t[i - 1]) +
			 * ","); } System.out.print((t[t.length - 1] - t[0]) + ",");
			 * 
			 * for (GCStats gcStats0 : gcs0) { for (GCStats gcStats1 : gcs1) { if
			 * (gcStats0.NAME.equals(gcStats1.NAME)) { System.out.print((gcStats1.COUNT -
			 * gcStats0.COUNT) + ","); System.out.print((gcStats1.TIME - gcStats0.TIME) +
			 * ","); } } }
			 * 
			 * System.out.println();
			 */

		} catch (Exception e) {
			e.printStackTrace(System.err);
		}
	}
}
