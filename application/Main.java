/**
 * Main.java created by mr.zeta0 on MacBook Pro in ATeamProjectApr 19, 2020
 *
 * Author: Jinhan Zhu, Xunwei Ye (jzhu382@wisc.edu, xye53@wisc.edu) Date: April 24, 2020
 * 
 * Course: CS400 Semester: Spring 2020 Lecture: 001
 * 
 * IDE: Eclipse IDE for Java Developers Version: 2019-06 (4.12.0) Build id: 20190614-1200
 * 
 * Device: ZHU-MACBOOKPRO OS: macOS Version: 10.14.6 OS Build: N/A
 *
 * List Collaborators: NONE
 *
 * Other Credits: NONE
 *
 * Known Bugs: NONE
 */
package application;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.*;
import java.util.zip.DataFormatException;
import javax.swing.JFileChooser;
import javafx.application.Application;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class Main extends Application {
	// store any command-line arguments that were entered.
	// NOTE: this.getParameters().getRaw() will get these also
	private List<String> args;

	private static final int WINDOW_WIDTH = 900;
	private static final int WINDOW_HEIGHT = 900;
	private static final String APP_TITLE = "Milk Weight Program";
	private FileManager fileManager;
	private DataManager dataManager;
	private CheeseFactory factory;

	ObservableList<sample_data> data = FXCollections.observableArrayList();
	ObservableList<sample_data1> data1 = FXCollections.observableArrayList();
	ObservableList<sample_data3> data2 = FXCollections.observableArrayList();

	// load data from file scene
	private Scene loadFileScene(Stage primaryStage, Scene mainScene) {
		BorderPane root = new BorderPane();

		Button back = new Button("Back to Menu");
		// Button saveReport = new Button("Save report");

		back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				data2.clear();
				primaryStage.setScene(mainScene);
			}
		});

		GridPane top = new GridPane();
		top.add(back, 0, 1);
		// top.add(new Button("Sava report"), 1, 1);
		top.setAlignment(Pos.CENTER);
		VBox v1 = new VBox();
		v1.getChildren().addAll(top, new Label(" "));

		root.setCenter(v1);
		// add table
		TableView table = new TableView<>();
		table.setEditable(false);
		TableColumn dateCol = new TableColumn("Date");
		dateCol.setPrefWidth(200);
		TableColumn idCol = new TableColumn<>("Farm Id");
		idCol.setPrefWidth(200);
		TableColumn weightCol = new TableColumn<>("Weight");
		weightCol.setPrefWidth(200);

		table.getColumns().addAll(dateCol, idCol, weightCol);
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("farmId"));
		weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

		table.setItems(data2);

		root.setBottom(table);

		Label title = new Label("Date From file");
		title.setFont(Font.font(java.awt.Font.SERIF, 25));
		VBox v = new VBox();
		v.getChildren().addAll(new Label(""), title, new Label(""));
		v.setAlignment(Pos.CENTER);
		root.setTop(v);

		Scene scene = new Scene(root, 600, 580);
		return scene;
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		// save args example
		args = this.getParameters().getRaw();

		this.fileManager = new FileManager();
		this.dataManager = new DataManager();
		this.factory = new CheeseFactory();

		// center pane part1: load data from files
		VBox readFile = new VBox();
		Label title1 = new Label("Read Data");
		Label space = new Label(" ");
		title1.setFont(Font.font(java.awt.Font.SERIF, 25));

		Button loadFile = new Button("Load File");// button that triggers file reader and should cause
													// alarm if catch exception

		readFile.getChildren().addAll(title1, loadFile);
		readFile.setSpacing(10);
		readFile.setAlignment(Pos.CENTER);

		// center panel part2: Editing data(add, replace, remove);
		VBox editData = new VBox();
		Label title2 = new Label("Manual Change Data");
		Label space2 = new Label(" ");
		title2.setFont(Font.font(java.awt.Font.SERIF, 25));
		// Add a vertex box for add box
		TextField addData = new TextField();// text field

		addData.setPrefWidth(170);
		addData.setPromptText("yyyy-mm-dd,farm id,weight");
		addData.setFont(Font.font(java.awt.Font.SERIF, 13));
		Label add = new Label("Add:       ");// label
		add.setFont(Font.font(java.awt.Font.SERIF, 20));

		Button addConfirm = new Button("CONFIRM");
		// add action for "add confirm button"
		addConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				try {
					DataItem addItem = new DataItem(addData.getText());

					// if the data added in wrong format
					if (!addItem.isCorrect()) {
						throw new DataFormatException();
					}

					// if the data cannot be added
					if (!factory.insertSingleData(addItem.getFarmIDString(), addItem.getDateString(),
							addItem.getWeight())) {
						throw new Exception();
					}
				} catch (DataFormatException e) {
					Alert addFail = new Alert(Alert.AlertType.WARNING, "Please add valid data in correct form:\n"
							+ "Add: yyyy-mm-dd(valid date),farm id,weight(must be a positive integer)!");
					addFail.setTitle("add data error");
					addFail.setHeaderText("Warning");
					addFail.showAndWait();

					addData.clear();
					return;
				} catch (Exception e) {
					Alert addFail = new Alert(Alert.AlertType.WARNING,
							"The data with specified date and farm id has existed, "
									+ "you may need to use editing rather than inserting!");
					addFail.setTitle("add data error");
					addFail.setHeaderText("Warning");
					addFail.showAndWait();

					addData.clear();
					return;
				}

				Alert addSuccess = new Alert(Alert.AlertType.CONFIRMATION, "Add data successful");
				addSuccess.setTitle("add data successful");
				ButtonType button1 = new ButtonType("OK");
				addSuccess.getButtonTypes().setAll(button1);
				addSuccess.showAndWait();
				addData.clear();
			}

		});

		// add a vertical box for replace
		TextField replaceData = new TextField();// text field
		replaceData.setPrefWidth(170);
		replaceData.setPromptText("yyyy-mm-dd,farm id");
		replaceData.setFont(Font.font(java.awt.Font.SERIF, 13));
		Label replace = new Label("Edit: ");// label
		replace.setFont(Font.font(java.awt.Font.SERIF, 20));

		// fixed box
		TextField toData = new TextField();// text field
		toData.setPrefWidth(170);
		toData.setPromptText("weight(must be an integer)");
		toData.setFont(Font.font(java.awt.Font.SERIF, 13));
		Label to = new Label("To New: ");// label
		to.setFont(Font.font(java.awt.Font.SERIF, 20));
		Button replaceConfirm = new Button("CONFIRM");
		// add action for replacement confirm data
		replaceConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				try {
					DataItem replaceItem = new DataItem(replaceData.getText() + "," + toData.getText());

					if (!replaceItem.isCorrect()) {
						throw new DataFormatException();
					}

					if (!factory.editSingleData(replaceItem.getFarmIDString(), replaceItem.getDateString(),
							replaceItem.getWeight())) {
						throw new Exception();
					}

				} catch (DataFormatException e) {
					// data format is incorrect
					Alert replaceFail = new Alert(Alert.AlertType.WARNING,
							"Please add valid data in correct form:\n"
									+ "Edit: yyyy-mm-dd(must be valid date),farm id\n"
									+ "To New: weight(must be a positive integer)!");
					replaceFail.setTitle("Edit data error");
					replaceFail.setHeaderText("Warning");
					replaceFail.showAndWait();
					replaceData.clear();
					toData.clear();
					return;
					// does not exist, can not use edit
				} catch (Exception e) {
					Alert replaceFail = new Alert(Alert.AlertType.WARNING,
							"The data with specified date and farm id does not exit, "
									+ "you may need to use inserting rather than editing!");
					replaceFail.setTitle("Edit data error");
					replaceFail.setHeaderText("Warning");
					replaceFail.showAndWait();
					replaceData.clear();
					toData.clear();
					return;
				}
				// success
				Alert replaceSuccess = new Alert(Alert.AlertType.CONFIRMATION, "Edit data successful");
				replaceSuccess.setTitle("Edit data successful");
				ButtonType button1 = new ButtonType("OK");
				replaceSuccess.getButtonTypes().setAll(button1);
				replaceSuccess.showAndWait();
				replaceData.clear();
				toData.clear();
			}

		});

		// add vertical box for remove
		TextField removeData = new TextField();// text field
		removeData.setPrefWidth(170);
		removeData.setPromptText("yyyy-mm-dd,farm id");
		removeData.setFont(Font.font(java.awt.Font.SERIF, 13));
		Label remove = new Label("Remove:");// label
		remove.setFont(Font.font(java.awt.Font.SERIF, 20));
		Button removeConfirm = new Button("CONFIRM");

		// add action for remove confirm button
		removeConfirm.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				try {
					DataItem removeItem = new DataItem(removeData.getText() + "," + "0");

					if (!removeItem.isCorrect()) {
						throw new DataFormatException();
					}

					if (!factory.removeSingleData(removeItem.getFarmIDString(), removeItem.getDateString())) {
						throw new Exception();
					}
				} catch (DataFormatException e) {
					// fail
					Alert removeFail = new Alert(Alert.AlertType.WARNING, "Please add valid data in correct form:\n"
							+ "Remove: yyyy-mm-dd(must be valid date),farm id!\n");
					removeFail.setTitle("remove data error");
					removeFail.setHeaderText("Warning");
					removeFail.showAndWait();
					removeData.clear();
					return;
				} catch (Exception e) {
					Alert removeFail = new Alert(Alert.AlertType.WARNING,
							"The data with specified date and farm id does not exit!\n");
					removeFail.setTitle("remove data error");
					removeFail.setHeaderText("Warning");
					removeFail.showAndWait();
					removeData.clear();
					return;
				}
				// success
				Alert removeSuccess = new Alert(Alert.AlertType.CONFIRMATION, "remove data successful");
				ButtonType button1 = new ButtonType("OK");
				removeSuccess.getButtonTypes().setAll(button1);
				removeSuccess.setTitle("remove data successful");
				removeSuccess.showAndWait();
			}

		});

		Button showData = new Button("Show Data");
		Button saveReport = new Button("Save Data");
		saveReport.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save data file");
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));

				// check if the file path has been chosen
				File file = fileChooser.showSaveDialog(primaryStage);
				if (file == null) {
					Alert showWarn = new Alert(Alert.AlertType.WARNING, "Please write data file into valid place!");
					showWarn.setTitle("Write file error");
					showWarn.setHeaderText("Warning");
					showWarn.showAndWait();
					return;
				} else {
					// TODO:写文件
					System.out.println(file.getAbsolutePath());// 绝对路径
					System.out.println(file);
					// write current data to file with user specified name
					fileManager.writeDataToFile(file.getName(), factory);
					Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
					ButtonType button1 = new ButtonType("OK");
					success.getButtonTypes().setAll(button1);
					success.setTitle("Successful");
					success.showAndWait();
				}
			}
		});

		HBox h = new HBox();
		h.getChildren().addAll(showData, saveReport);
		// arrange add/replace/remove boxes into a gridepane
		GridPane edit = new GridPane();

		edit.add(add, 0, 0);
		edit.add(addData, 1, 0);
		edit.add(addConfirm, 2, 0);
		edit.add(replace, 0, 1);
		edit.add(replaceData, 1, 1);
		edit.add(to, 0, 2);
		edit.add(toData, 1, 2);
		edit.add(replaceConfirm, 2, 2);
		edit.add(remove, 0, 3);
		edit.add(removeData, 1, 3);
		edit.add(removeConfirm, 2, 3);
		edit.add(h, 1, 4);
		// edit.add(saveReport, 2, 4);
		editData.getChildren().addAll(title2, space2, edit);
		editData.setSpacing(10);
		editData.setAlignment(Pos.CENTER);
		VBox vbox = new VBox();
		vbox.setAlignment(Pos.CENTER);
		vbox.setStyle("-fx-background-color:#335050;");

		// add a vertical box to get reports
		VBox getReport = new VBox();
		Label title3 = new Label("Get Report");
		Label space3 = new Label(" ");
		title3.setFont(Font.font(java.awt.Font.SERIF, 25));

		GridPane reportType = new GridPane();
		Button farmReport = new Button("Farm Report");
		farmReport.setPrefWidth(150);
		Button monthlyReport = new Button("Monthly Report");
		monthlyReport.setPrefWidth(150);
		Button annualReport = new Button("Annul Report");
		annualReport.setPrefWidth(150);
		Button dateReport = new Button("Date Range Report");

		dateReport.setPrefWidth(150);

		reportType.add(farmReport, 0, 0);
		reportType.add(monthlyReport, 0, 1);
		reportType.add(annualReport, 1, 0);
		reportType.add(dateReport, 1, 1);
		reportType.setAlignment(Pos.CENTER);
		getReport.getChildren().addAll(title3, space3, reportType);
		getReport.setAlignment(Pos.TOP_CENTER);

		vbox.getChildren().addAll(readFile, editData, getReport);
		vbox.setSpacing(40);

		// Main layout is Border Pane example (top,left,center,right,bottom)
		BorderPane root = new BorderPane();

		VBox leftPane = new VBox();
		leftPane.getChildren().add(new Label("Left"));
		// Add the vertical box to the center of the root pane
		Label title = new Label(APP_TITLE);
		title.setFont(Font.font(35));
		BorderPane.setAlignment(title, Pos.TOP_CENTER);
		root.setTop(title);
		root.setCenter(vbox);

		HBox Bottom = new HBox();

		// the bottom pane
		Button clearButton = new Button("Clear");
		Button closeButton = new Button("Done");
		Bottom.getChildren().addAll(clearButton, closeButton);
		Bottom.setAlignment(Pos.BASELINE_CENTER);
		root.setBottom(Bottom);

		// add action to clear button
		clearButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				factory.clearData();
				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
				ButtonType button1 = new ButtonType("OK");
				success.getButtonTypes().setAll(button1);
				success.setTitle("Successful");
				success.showAndWait();
			}

		});

		// add action to close button
		closeButton.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.close();
			}

		});
		FileInputStream input = new FileInputStream("images.jpeg");
		Image image = new Image(input);
		ImageView imageView = new ImageView(image);
		root.setRight(imageView);
		FileInputStream input1 = new FileInputStream("images1.jpeg");
		Image image1 = new Image(input1);
		ImageView imageView1 = new ImageView(image1);
		root.setLeft(imageView1);
		Scene mainScene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

		// scene1: get Farm report
		farmReport.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.setScene(farmReportScene(primaryStage, mainScene));
			}
		});

		// scene2: get monthly report
		monthlyReport.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.setScene(monthlyReportScene(primaryStage, mainScene));
			}
		});

		// scene3: get annual report
		annualReport.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.setScene(annualReportScene(primaryStage, mainScene));
			}
		});

		// scene4: get data range report
		dateReport.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.setScene(dateReportScene(primaryStage, mainScene));
			}
		});

		// scene5: get a report of current data
		showData.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.setScene(showData(primaryStage, mainScene));
			}

		});

		// add action to load file button
		loadFile.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Select data file");
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));

				// get a backup before adding data items to factory
				CheeseFactory prevFactory = new CheeseFactory(factory.getName(), factory.getMilkDataFromFarms());

				// choose multiple files
				List<File> filelist = fileChooser.showOpenMultipleDialog(primaryStage);
				if (filelist == null) {// if doesn't choose any file
					Alert showWarn = new Alert(Alert.AlertType.WARNING, "Please load valid data files!");
					showWarn.setTitle("Load file error");
					showWarn.setHeaderText("Warning");
					showWarn.showAndWait();
					return;
				}

				// load multiple files
				for (int i = 0; i < filelist.size(); i++) {
					File file = filelist.get(i);

					// if file is error, need to clear the factory
					if (!fileManager.readfile(file.toString(), factory)) {
						factory = prevFactory;
						Alert showError = new Alert(Alert.AlertType.ERROR, "Load file with error: " + file.getPath()
								+ "\n\nThere exist bad data!\n"
								+ "(Bad data means data with missing parts, error parts, or dupliate date & farm Id)!");
						showError.setTitle("Load file error");
						showError.setHeaderText("Error");
						showError.showAndWait();
						return;
					}
				}

				Alert showSuccess = new Alert(Alert.AlertType.CONFIRMATION, "Load successful");
				ButtonType button1 = new ButtonType("OK");
				showSuccess.getButtonTypes().setAll(button1);

				showSuccess.showAndWait();

				ObservableList<String> list = FXCollections
						.observableArrayList(factory.getMilkDataFromFarmsInStringList());

				String[] readData;
				for (int i = 0; i < list.size(); i++) {
					readData = list.get(i).trim().split(",");
					data2.add(new sample_data3(readData[0], readData[1], Integer.parseInt(readData[2])));
				}

				primaryStage.setScene(loadFileScene(primaryStage, mainScene));
			}

		});
		root.setStyle("-fx-background-color:#B4EEB4;");

		// Add the stuff and set the primary stage
		primaryStage.setTitle(APP_TITLE);
		primaryStage.setScene(mainScene);
		primaryStage.show();

	}

	/**
	 * Scene that show current data
	 * 
	 * @param primaryStage
	 * @param mainScene
	 * @return
	 */
	private Scene showData(Stage primaryStage, Scene mainScene) {
		BorderPane root = new BorderPane();

		Button back = new Button("Back to Menu");
		back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				data2.clear();
				primaryStage.setScene(mainScene);
			}

		});

		GridPane top = new GridPane();
		top.add(back, 0, 1);
		top.add(new Button("Sava Data"), 1, 1);
		top.setAlignment(Pos.CENTER);
		VBox v1 = new VBox();
		v1.getChildren().addAll(top, new Label(" "));

		root.setCenter(v1);
		// add table
		TableView table = new TableView<>();
		table.setEditable(false);
		TableColumn dateCol = new TableColumn("Date");
		dateCol.setPrefWidth(200);
		TableColumn idCol = new TableColumn<>("Farm Id");
		idCol.setPrefWidth(200);
		TableColumn weightCol = new TableColumn<>("Weight");
		weightCol.setPrefWidth(200);

		table.getColumns().addAll(dateCol, idCol, weightCol);
		dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
		idCol.setCellValueFactory(new PropertyValueFactory<>("farmId"));
		weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));

		// add all milk data to list and show
		data2.clear();
		System.out.println("before show");
		ObservableList<String> list = FXCollections.observableArrayList(factory.getMilkDataFromFarmsInStringList());
		System.out.println("after list");
		String[] readData;
		for (int i = 0; i < list.size(); i++) {
			System.out.println(list.get(i));
			readData = list.get(i).trim().split(",");
			data2.add(new sample_data3(readData[0], readData[1], Integer.parseInt(readData[2])));
		}
		table.setItems(data2);

		root.setBottom(table);

		Label title = new Label("Current Data");
		title.setFont(Font.font(java.awt.Font.SERIF, 25));
		// title.setAlignment(Pos.BASELINE_CENTER);
		VBox v = new VBox();
		v.getChildren().addAll(new Label(""), title, new Label(""));
		v.setAlignment(Pos.CENTER);
		root.setTop(v);

		Scene scene = new Scene(root, 600, 580);
		return scene;
	}

	/**
	 * 
	 * @param primaryStage
	 * @param mainScene
	 * @return
	 */
	private Scene farmReportScene(Stage primaryStage, Scene mainScene) {
		BorderPane root = new BorderPane();
		ArrayList<String> farmIDList = factory.getFarmIDList();
		Collections.sort(farmIDList);
		ComboBox<String> getId = new ComboBox<String>(FXCollections.observableArrayList(farmIDList));

		getId.setPrefWidth(150);
		getId.setPromptText("(select FarmId)");

		Label id = new Label("Farm Id:");// label
		id.setFont(Font.font(java.awt.Font.SERIF, 21));
		Button confirm1 = new Button("CONFIRM");

		ComboBox<Integer> combo_box = new ComboBox<Integer>(
				FXCollections.observableArrayList(dataManager.getAllYears(factory)));
		combo_box.setPrefWidth(150);
		combo_box.setPromptText("(select year)");
		Label year = new Label("Year:");
		year.setFont(Font.font(java.awt.Font.SERIF, 21));

		Button back = new Button("Back to Menu");
		back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.setScene(mainScene);
			}

		});

		GridPane top = new GridPane();

		Button save_report = new Button("Save Report");

		// label to show min, max, and average total weight of farm report
		Label min = new Label("Min Total: ");
		Label showMin = new Label("");
		Label max = new Label("Max Total: ");
		Label showMax = new Label("");
		Label ave = new Label("Average Weight: ");
		Label showAve = new Label("");
		min.setFont(Font.font(java.awt.Font.SERIF, 21));
		max.setFont(Font.font(java.awt.Font.SERIF, 21));
		ave.setFont(Font.font(java.awt.Font.SERIF, 21));
		showMin.setFont(Font.font(java.awt.Font.SERIF, 21));
		showMax.setFont(Font.font(java.awt.Font.SERIF, 21));
		showAve.setFont(Font.font(java.awt.Font.SERIF, 21));

		top.add(id, 0, 0);
		top.add(getId, 1, 0);
		top.add(confirm1, 2, 0);
		top.add(year, 0, 1);
		top.add(combo_box, 1, 1);
		top.add(back, 2, 1);
		top.add(min, 0, 3);
		top.add(showMin, 1, 3);
		top.add(max, 0, 4);
		top.add(showMax, 1, 4);
		top.add(ave, 0, 5);
		top.add(showAve, 1, 5);
		top.add(save_report, 2, 2);
		// top.add(data_analyze, 1, 2);

		top.setAlignment(Pos.CENTER);
		VBox v1 = new VBox();
		v1.getChildren().addAll(top, new Label(" "));

		root.setCenter(v1);
		// add table
		TableView table = new TableView<>();
		table.setEditable(false);
		TableColumn monthCol = new TableColumn<>("Month");
		monthCol.setPrefWidth(200);
		TableColumn weightCol = new TableColumn<>("Total of Chosen Farm (Click here to Sort)");
		weightCol.setPrefWidth(250);
		TableColumn percentCol = new TableColumn<>("Percent of Total of All Farms (Click here to Sort)");
		percentCol.setPrefWidth(300);
		table.getColumns().addAll(monthCol, weightCol, percentCol);

		monthCol.setCellValueFactory(new PropertyValueFactory<>("month"));
		// idCol.setCellValueFactory(new PropertyValueFactory<>("farmId"));
		weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
		percentCol.setCellValueFactory(new PropertyValueFactory<>("percent"));

		confirm1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				// there is nothing to choose in combo box
				if (factory.getMilkDataFromFarms().size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently, please load from file or add by yourself!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				// click on confirm without choosing farm id or year
				if (combo_box.getValue() == null || getId.getValue() == null) {
					Alert fail = new Alert(Alert.AlertType.WARNING, "Please select both farm ID and year!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				data1.clear();
				// show farm report data to table
				ObservableList<String> list = FXCollections.observableArrayList(
						dataManager.getFarmReport(getId.getValue(), combo_box.getValue(), factory));
				// min, max, sum total weight among 12 months
				Double maxWeight = 0.0;
				Double minWeight = Double.MAX_VALUE;
				Double sumWeight = 0.0;

				String[] readData;
				for (int i = 0; i < list.size(); i++) {
					readData = list.get(i).trim().split(",");
					double totalWeight = Double.parseDouble(readData[1]);
					if (totalWeight != 0.0) {
						data1.add(new sample_data1(Integer.parseInt(readData[0]), Integer.parseInt(readData[1]),
								readData[2]));
						if (totalWeight > maxWeight) {
							maxWeight = totalWeight;
						}
						if (totalWeight < minWeight) {
							minWeight = totalWeight;
						}
						sumWeight += totalWeight;
					}
				}

				// check if there has data to be set in table
				if (data1.size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently for your specified farm ID and year!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}
				// data1 = FXCollections.observableArrayList(new sample_data1(1, 10000, "1%"));

				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
				ButtonType button1 = new ButtonType("OK");
				success.getButtonTypes().setAll(button1);
				success.setTitle("Successful");
				success.showAndWait();
				table.setItems(data1);
				showMin.setText(minWeight.toString());
				showMax.setText(maxWeight.toString());
				Double aveWeight = sumWeight / data1.size();
				showAve.setText(aveWeight.toString());

			}

		});
		save_report.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				// there is nothing to choose in combo box
				if (factory.getMilkDataFromFarms().size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently, please load from file or add by yourself!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				// click on confirm without choosing farm id or year
				if (combo_box.getValue() == null || getId.getValue() == null) {
					Alert fail = new Alert(Alert.AlertType.WARNING, "Please select both farm ID and year!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}
				// show farm report data to table
				ArrayList<String> list = dataManager.getFarmReport(getId.getValue(), combo_box.getValue(), factory);
				ArrayList<String> farmReportStringList = new ArrayList<>();

				String[] readData;
				for (int i = 0; i < list.size(); i++) {
					readData = list.get(i).trim().split(",");
					if (Double.parseDouble(readData[1]) != 0.0) {
						farmReportStringList.add(list.get(i));
					}
				}

				// check if there has data to be saved in the file
				if (farmReportStringList.size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently for your specified farm ID and year!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				} else if (table.getItems().size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"Please press CONFIRM first to show your farm report!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save data file");
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));

				// choose multiple files
				File file = fileChooser.showSaveDialog(primaryStage);
				if (file == null) {
					Alert showWarn = new Alert(Alert.AlertType.WARNING, "Please write data file into valid place!");
					showWarn.setTitle("Write file error");
					showWarn.setHeaderText("Warning");
					showWarn.showAndWait();
					return;
				} else {
					// write farm report to file
					if (fileManager.writeFarmReportToFile(file.getName(), getId.getValue(),
							combo_box.getValue().toString(), farmReportStringList, factory)) {
						System.out.println(file.getAbsolutePath());// 绝对路径
						System.out.println(file);
						Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
						ButtonType button1 = new ButtonType("OK");
						success.getButtonTypes().setAll(button1);
						success.setTitle("Successful");
						success.showAndWait();
					} else {
						Alert showError = new Alert(Alert.AlertType.ERROR, "Write file with error: " + file.getPath()
								+ "\n\nThere exist bad data!\n"
								+ "(Bad data means data with missing parts, error parts, or dupliate date & farm Id)!");
						showError.setTitle("Write file error");
						showError.setHeaderText("Error");
						showError.showAndWait();
						return;
					}
				}

			}

		});

		root.setBottom(table);

		Label title = new Label("Get Farm Report");
		title.setFont(Font.font(java.awt.Font.SERIF, 25));
		// title.setAlignment(Pos.BASELINE_CENTER);
		VBox v = new VBox();
		v.getChildren().addAll(new Label(""), title, new Label(""));
		v.setAlignment(Pos.CENTER);
		root.setTop(v);

		Scene scene = new Scene(root, 750, 650);
		return scene;
	}

	private Scene monthlyReportScene(Stage primaryStage, Scene mainScene) {
		BorderPane root = new BorderPane();

		ComboBox<Integer> combo_box = new ComboBox<Integer>(
				FXCollections.observableArrayList(dataManager.getAllYears(factory)));
		combo_box.setPrefWidth(150);
		combo_box.setPromptText("(select year)");
		Label year = new Label("Year:");
		year.setFont(Font.font(java.awt.Font.SERIF, 21));
		Button back = new Button("Back to Menu");
		back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.setScene(mainScene);
			}
		});
		String months[] = { "1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12" };
		ComboBox<String> combo_box1 = new ComboBox<String>(FXCollections.observableArrayList(months));
		combo_box1.setPrefWidth(150);
		combo_box1.setPromptText("(select month)");
		Label month = new Label("Month:");
		month.setFont(Font.font(java.awt.Font.SERIF, 21));
		Button confirm1 = new Button("CONFIRM");

		GridPane top = new GridPane();
		Button save_report = new Button("Save Report");

		// label to show min, max, and average total weight of monthly report
		Label min = new Label("Min Total: ");
		Label showMin = new Label("");
		Label max = new Label("Max Total: ");
		Label showMax = new Label("");
		Label ave = new Label("Average: ");
		Label showAve = new Label("");
		min.setFont(Font.font(java.awt.Font.SERIF, 21));
		max.setFont(Font.font(java.awt.Font.SERIF, 21));
		ave.setFont(Font.font(java.awt.Font.SERIF, 21));
		showMin.setFont(Font.font(java.awt.Font.SERIF, 21));
		showMax.setFont(Font.font(java.awt.Font.SERIF, 21));
		showAve.setFont(Font.font(java.awt.Font.SERIF, 21));

		top.add(month, 0, 1);
		top.add(combo_box1, 1, 1);
		top.add(confirm1, 2, 0);
		top.add(year, 0, 0);
		top.add(combo_box, 1, 0);
		top.add(back, 2, 1);
		top.add(save_report, 2, 2);
		top.add(min, 0, 3);
		top.add(showMin, 1, 3);
		top.add(max, 0, 4);
		top.add(showMax, 1, 4);
		top.add(ave, 0, 5);
		top.add(showAve, 1, 5);

		top.setAlignment(Pos.CENTER);
		VBox v1 = new VBox();
		v1.getChildren().addAll(top, new Label(" "));

		root.setCenter(v1);
		// add table
		TableView table = new TableView<>();
		table.setEditable(false);

		TableColumn idCol = new TableColumn("Farm Id (Click here to Sort)");
		idCol.setPrefWidth(200);
		TableColumn weightCol = new TableColumn("Total Weight of Per Farm (Click here to Sort)");
		weightCol.setPrefWidth(250);
		TableColumn percentCol = new TableColumn("Percent of Total of All Farms (Click here to Sort)");
		percentCol.setPrefWidth(300);

		idCol.setCellValueFactory(new PropertyValueFactory<>("farmId"));
		weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
		percentCol.setCellValueFactory(new PropertyValueFactory<>("percent"));

		confirm1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {

				// there is nothing to choose in combo box
				if (factory.getMilkDataFromFarms().size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently, please load from file or add by yourself!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				data.clear();
				if (combo_box.getValue() == null || combo_box1.getValue() == null) {
					Alert fail = new Alert(Alert.AlertType.WARNING, "Please select both year and month!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;

				}

				// success
				// show farm report data to table
				ObservableList<String> list = FXCollections.observableArrayList(dataManager
						.getMonthlyReport(combo_box.getValue(), Integer.parseInt(combo_box1.getValue()), factory));

				// min, max, sum total weight among 12 months
				Double maxWeight = 0.0;
				Double minWeight = Double.MAX_VALUE;
				Double sumWeight = 0.0;

				String[] readData;
				for (int i = 0; i < list.size(); i++) {
					readData = list.get(i).trim().split(",");
					double totalWeight = Double.parseDouble(readData[1]);
					if (Double.parseDouble(readData[1]) != 0.0) {
						data.add(new sample_data(readData[0], Double.parseDouble(readData[1]), readData[2]));

						if (totalWeight > maxWeight) {
							maxWeight = totalWeight;
						}
						if (totalWeight < minWeight) {
							minWeight = totalWeight;
						}
						sumWeight += totalWeight;
					}

				}

				// check if there has data to be set in table
				if (data.size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently for your specified year and month!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}
				// data1 = FXCollections.observableArrayList(new sample_data1(1, 10000, "1%"));

				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
				ButtonType button1 = new ButtonType("OK");
				success.getButtonTypes().setAll(button1);
				success.setTitle("Successful");
				success.showAndWait();

				Collections.sort(data);
				// data1 = FXCollections.observableArrayList(new sample_data1(1, 10000, "1%"));
				table.setItems(data);
				showMin.setText(minWeight.toString());
				showMax.setText(maxWeight.toString());
				Double aveWeight = sumWeight / data.size();
				showAve.setText(aveWeight.toString());
			}

		});

		save_report.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				// there is nothing to choose in combo box
				if (factory.getMilkDataFromFarms().size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently, please load from file or add by yourself!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				// click on confirm without choosing farm id or month
				if (combo_box.getValue() == null || combo_box1.getValue() == null) {
					Alert fail = new Alert(Alert.AlertType.WARNING, "Please select both year and month!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;

				}

				// show monthly report data to table
				ArrayList<String> list = dataManager.getMonthlyReport(combo_box.getValue(),
						Integer.parseInt(combo_box1.getValue()), factory);
				ArrayList<String> monthlyReportStringList = new ArrayList<>();
				Collections.sort(list);

				String[] readData;
				for (int i = 0; i < list.size(); i++) {
					readData = list.get(i).trim().split(",");
					if (Double.parseDouble(readData[1]) != 0.0) {
						monthlyReportStringList.add(list.get(i));
					}
				}

				// check if there has data to be saved in the file
				if (monthlyReportStringList.size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently for your specified year and month!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				} else if (table.getItems().size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"Please press CONFIRM first to show your monthly report!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save data file");
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));

				// choose multiple files
				File file = fileChooser.showSaveDialog(primaryStage);
				if (file == null) {
					Alert showWarn = new Alert(Alert.AlertType.WARNING, "Please write data file into valid place!");
					showWarn.setTitle("Write file error");
					showWarn.setHeaderText("Warning");
					showWarn.showAndWait();
					return;
				} else {
					// write monthly report to file
					if (fileManager.writeMonthlyReportToFile(file.getName(), combo_box.getValue().toString(),
							combo_box1.getValue(), monthlyReportStringList, factory)) {
						System.out.println(file.getAbsolutePath());
						System.out.println(file);
						Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
						ButtonType button1 = new ButtonType("OK");
						success.getButtonTypes().setAll(button1);
						success.setTitle("Successful");
						success.showAndWait();
					} else {
						Alert showError = new Alert(Alert.AlertType.ERROR, "Write file with error: " + file.getPath()
								+ "\n\nThere exist bad data!\n"
								+ "(Bad data means data with missing parts, error parts, or dupliate date & farm Id)!");
						showError.setTitle("Write file error");
						showError.setHeaderText("Error");
						showError.showAndWait();
						return;
					}
				}

			}
		});

		// table.setItems(data);
		table.getColumns().addAll(idCol, weightCol, percentCol);
		root.setBottom(table);

		Label title = new Label("Get Mothly Report");
		title.setFont(Font.font(java.awt.Font.SERIF, 25));
		// title.setAlignment(Pos.BASELINE_CENTER);
		VBox v = new VBox();
		v.getChildren().addAll(new Label(""), title, new Label(""));
		v.setAlignment(Pos.CENTER);
		root.setTop(v);

		Scene scene = new Scene(root, 750, 580);
		return scene;
	}

	private Scene annualReportScene(Stage primaryStage, Scene mainScene) {
		BorderPane root = new BorderPane();

		ComboBox<Integer> combo_box = new ComboBox<Integer>(
				FXCollections.observableArrayList(dataManager.getAllYears(factory)));
		combo_box.setPrefWidth(150);
		combo_box.setPromptText("(select year)");
		Label year = new Label("Year:");
		year.setFont(Font.font(java.awt.Font.SERIF, 21));
		Button back = new Button("Back to Menu");
		back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.setScene(mainScene);
			}

		});

		Button confirm1 = new Button("CONFIRM");

		GridPane top = new GridPane();
		Button save_report = new Button("Save Report");

		// label to show min, max, and average total weight of monthly report
		Label min = new Label("Min Total: ");
		Label showMin = new Label("");
		Label max = new Label("Max Total: ");
		Label showMax = new Label("");
		Label ave = new Label("Average: ");
		Label showAve = new Label("");
		min.setFont(Font.font(java.awt.Font.SERIF, 21));
		max.setFont(Font.font(java.awt.Font.SERIF, 21));
		ave.setFont(Font.font(java.awt.Font.SERIF, 21));
		showMin.setFont(Font.font(java.awt.Font.SERIF, 21));
		showMax.setFont(Font.font(java.awt.Font.SERIF, 21));
		showAve.setFont(Font.font(java.awt.Font.SERIF, 21));

		top.add(back, 2, 1);
		top.add(year, 0, 0);
		top.add(combo_box, 1, 0);
		top.add(confirm1, 2, 0);
		top.add(save_report, 2, 2);
		top.add(min, 0, 3);
		top.add(showMin, 1, 3);
		top.add(max, 0, 4);
		top.add(showMax, 1, 4);
		top.add(ave, 0, 5);
		top.add(showAve, 1, 5);
		// top.add(data_analyze, 1, 1);
		top.setAlignment(Pos.CENTER);

		VBox v1 = new VBox();
		v1.getChildren().addAll(top, new Label(" "));

		root.setCenter(v1);
		// add table
		TableView table = new TableView<>();
		table.setEditable(false);

		TableColumn idCol = new TableColumn<>("Farm Id (Click here to Sort)");
		idCol.setPrefWidth(200);
		TableColumn weightCol = new TableColumn<>("Total Weight of Per Farm (Click here to Sort)");
		weightCol.setPrefWidth(250);
		TableColumn percentCol = new TableColumn<>("Percent of Total of All Farms (Click here to Sort)");
		percentCol.setPrefWidth(300);
		table.getColumns().addAll(idCol, weightCol, percentCol);

		idCol.setCellValueFactory(new PropertyValueFactory<>("farmId"));
		weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
		percentCol.setCellValueFactory(new PropertyValueFactory<>("percent"));

		confirm1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				// there is nothing to choose in combo box
				if (factory.getMilkDataFromFarms().size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently, please load from file or add by yourself!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				data.clear();
				// user does not select one year to show
				if (combo_box.getValue() == null) {
					Alert fail = new Alert(Alert.AlertType.WARNING, "Please select a year!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				// show farm report data to table
				ObservableList<String> list = FXCollections
						.observableArrayList(dataManager.getAnnualReport(combo_box.getValue(), factory));

				// min, max, sum total weight among 12 months
				Double maxWeight = 0.0;
				Double minWeight = Double.MAX_VALUE;
				Double sumWeight = 0.0;

				String[] readData;
				for (int i = 0; i < list.size(); i++) {
					readData = list.get(i).trim().split(",");
					double totalWeight = Double.parseDouble(readData[1]);
					if (Double.parseDouble(readData[1]) != 0.0) {
						data.add(new sample_data(readData[0], Double.parseDouble(readData[1]), readData[2]));

						if (totalWeight > maxWeight) {
							maxWeight = totalWeight;
						}
						if (totalWeight < minWeight) {
							minWeight = totalWeight;
						}
						sumWeight += totalWeight;
					}
				}

				// check if there has data to be set in table
				if (data.size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently for your specified year!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}
				// data1 = FXCollections.observableArrayList(new sample_data1(1, 10000, "1%"));

				Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
				ButtonType button1 = new ButtonType("OK");
				success.getButtonTypes().setAll(button1);
				success.setTitle("Successful");
				success.showAndWait();

				Collections.sort(data);
				// data1 = FXCollections.observableArrayList(new sample_data1(1, 10000, "1%"));
				table.setItems(data);
				showMin.setText(minWeight.toString());
				showMax.setText(maxWeight.toString());
				Double aveWeight = sumWeight / data.size();
				showAve.setText(aveWeight.toString());
			}

		});
		save_report.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				// there is nothing to choose in combo box
				if (factory.getMilkDataFromFarms().size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently, please load from file or add by yourself!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				// click on confirm without choosing farm id or month
				if (combo_box.getValue() == null || combo_box.getValue() == null) {
					Alert fail = new Alert(Alert.AlertType.WARNING, "Please select a year!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;

				}

				// show annual report data to table
				ArrayList<String> list = dataManager.getAnnualReport(combo_box.getValue(), factory);
				ArrayList<String> annualReportStringList = new ArrayList<>();
				Collections.sort(list);

				String[] readData;
				for (int i = 0; i < list.size(); i++) {
					readData = list.get(i).trim().split(",");
					if (Double.parseDouble(readData[1]) != 0.0) {
						annualReportStringList.add(list.get(i));
					}
				}

				// check if there has data to be saved in the file
				if (annualReportStringList.size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"There is no data currently for your specified year!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				} else if (table.getItems().size() == 0) {
					Alert fail = new Alert(Alert.AlertType.WARNING,
							"Please press CONFIRM first to show your annual report!");
					fail.setTitle("Select data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					return;
				}

				FileChooser fileChooser = new FileChooser();
				fileChooser.setTitle("Save data file");
				fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));

				// choose multiple files
				File file = fileChooser.showSaveDialog(primaryStage);
				if (file == null) {
					Alert showWarn = new Alert(Alert.AlertType.WARNING, "Please write data file into valid place!");
					showWarn.setTitle("Write file error");
					showWarn.setHeaderText("Warning");
					showWarn.showAndWait();
					return;
				} else {
					// write annual report to file
					if (fileManager.writeAnnualReportToFile(file.getName(), combo_box.getValue().toString(),
							annualReportStringList, factory)) {
						System.out.println(file.getAbsolutePath());
						System.out.println(file);
						Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
						ButtonType button1 = new ButtonType("OK");
						success.getButtonTypes().setAll(button1);
						success.setTitle("Successful");
						success.showAndWait();
					} else {
						Alert showError = new Alert(Alert.AlertType.ERROR, "Write file with error: " + file.getPath()
								+ "\n\nThere exist bad data!\n"
								+ "(Bad data means data with missing parts, error parts, or dupliate date & farm Id)!");
						showError.setTitle("Write file error");
						showError.setHeaderText("Error");
						showError.showAndWait();
						return;
					}
				}

			}
		});
		root.setBottom(table);

		Label title = new Label("Get Annual Report");
		title.setFont(Font.font(java.awt.Font.SERIF, 25));
		// title.setAlignment(Pos.BASELINE_CENTER);
		VBox v = new VBox();
		v.getChildren().addAll(new Label(""), title, new Label(""));
		v.setAlignment(Pos.CENTER);
		root.setTop(v);

		Scene scene = new Scene(root, 750, 580);
		return scene;
	}

	private Scene dateReportScene(Stage primaryStage, Scene mainScene) {
		BorderPane root = new BorderPane();
		TextField startDate = new TextField();// text field
		startDate.setPrefWidth(150);
		startDate.setPromptText("yyyy-mm-dd");
		startDate.setFont(Font.font(java.awt.Font.SERIF, 15));
		Label start = new Label("Start date: ");// label
		start.setFont(Font.font(java.awt.Font.SERIF, 21));
		Button confirm1 = new Button("CONFIRM");

		TextField endDate = new TextField();// text field
		endDate.setPrefWidth(150);
		endDate.setPromptText("yyyy-mm-dd");
		endDate.setFont(Font.font(java.awt.Font.SERIF, 15));
		Label end = new Label("End date: ");// label
		end.setFont(Font.font(java.awt.Font.SERIF, 21));
		Button back = new Button("Back to Menu");
		back.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				primaryStage.setScene(mainScene);
			}

		});

		GridPane top = new GridPane();
		Button save_report = new Button("Save Report");
		Button data_analyze = new Button("Show Min/Max/Ave");

		// label to show min, max, and average total weight of date range report
		Label min = new Label("Min: ");
		Label showMin = new Label("");
		Label max = new Label("Max: ");
		Label showMax = new Label("");
		Label ave = new Label("Average: ");
		Label showAve = new Label("");
		min.setFont(Font.font(java.awt.Font.SERIF, 21));
		max.setFont(Font.font(java.awt.Font.SERIF, 21));
		ave.setFont(Font.font(java.awt.Font.SERIF, 21));
		showMin.setFont(Font.font(java.awt.Font.SERIF, 21));
		showMax.setFont(Font.font(java.awt.Font.SERIF, 21));
		showAve.setFont(Font.font(java.awt.Font.SERIF, 21));

		top.add(start, 0, 0);
		top.add(startDate, 1, 0);
		top.add(confirm1, 2, 0);
		top.add(end, 0, 1);
		top.add(endDate, 1, 1);
		top.add(back, 2, 1);
		top.add(save_report, 2, 2);
		top.add(min, 0, 3);
		top.add(showMin, 1, 3);
		top.add(max, 0, 4);
		top.add(showMax, 1, 4);
		top.add(ave, 0, 5);
		top.add(showAve, 1, 5);
		// top.add(data_analyze, 1, 2);
		top.setAlignment(Pos.CENTER);
		VBox v1 = new VBox();
		v1.getChildren().addAll(top, new Label(" "));

		root.setCenter(v1);
		// add table
		TableView table = new TableView<>();
		table.setEditable(false);

		TableColumn idCol = new TableColumn<>("Farm Id (Click here to Sort)");
		idCol.setPrefWidth(200);
		TableColumn weightCol = new TableColumn<>("Total Weight of Per Farm (Click here to Sort)");
		weightCol.setPrefWidth(250);
		TableColumn percentCol = new TableColumn<>("Percent of Total of All Farms (Click here to Sort)");
		percentCol.setPrefWidth(300);
		table.getColumns().addAll(idCol, weightCol, percentCol);

		idCol.setCellValueFactory(new PropertyValueFactory<>("farmId"));
		weightCol.setCellValueFactory(new PropertyValueFactory<>("weight"));
		percentCol.setCellValueFactory(new PropertyValueFactory<>("percent"));

		confirm1.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				try {
					// there is no data currently in factory
					if (factory.getMilkDataFromFarms().size() == 0) {
						Alert fail = new Alert(Alert.AlertType.WARNING,
								"There is no data currently, please load from file or add by yourself!");
						fail.setTitle("Select data error");
						fail.setHeaderText("Warning");
						fail.showAndWait();
						return;
					}

					data.clear();

					String[] startDateString = startDate.getText().trim().split("-");
					String[] endDateString = endDate.getText().trim().split("-");
					Integer year1 = Integer.parseInt(startDateString[0]);
					Integer month1 = Integer.parseInt(startDateString[1]);
					Integer day1 = Integer.parseInt(startDateString[2]);

					Integer year2 = Integer.parseInt(endDateString[0]);
					Integer month2 = Integer.parseInt(endDateString[1]);
					Integer day2 = Integer.parseInt(endDateString[2]);

					if ((year1 * 10000 + month1 * 100 + day1) > (year2 * 10000 + month2 * 100 + day2)) {
						throw new Exception();
					}

					// show date range report data to table
					ObservableList<String> list = FXCollections.observableArrayList(
							dataManager.getDateRangeReport(year1, month1, day1, year2, month2, day2, factory));

					// min, max, sum total weight among 12 months
					Double maxWeight = 0.0;
					Double minWeight = Double.MAX_VALUE;
					Double sumWeight = 0.0;
					String[] readData;
					for (int i = 0; i < list.size(); i++) {
						readData = list.get(i).trim().split(",");
						double totalWeight = Double.parseDouble(readData[1]);
						if (Double.parseDouble(readData[1]) != 0.0) {
							data.add(new sample_data(readData[0], Double.parseDouble(readData[1]), readData[2]));

							if (totalWeight > maxWeight) {
								maxWeight = totalWeight;
							}
							if (totalWeight < minWeight) {
								minWeight = totalWeight;
							}
							sumWeight += totalWeight;
						}
					}

					// check if there has data to be set in table
					if (data.size() == 0) {
						Alert fail = new Alert(Alert.AlertType.WARNING,
								"There is no data currently for your specified date range!");
						fail.setTitle("Select data error");
						fail.setHeaderText("Warning");
						fail.showAndWait();
						return;
					}
					// data1 = FXCollections.observableArrayList(new sample_data1(1, 10000, "1%"));

					Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
					ButtonType button1 = new ButtonType("OK");
					success.getButtonTypes().setAll(button1);
					success.setTitle("Successful");
					success.showAndWait();

					Collections.sort(data);
					table.setItems(data);
					showMin.setText(minWeight.toString());
					showMax.setText(maxWeight.toString());
					Double aveWeight = sumWeight / data.size();
					showAve.setText(aveWeight.toString());
				} catch (Exception e) {
					Alert fail = new Alert(Alert.AlertType.WARNING, "Please input valid data in correct form: \n"
							+ "yyyy/mm/dd(must be valid date)!" + "\n\nNote: End date should be after start date!");
					fail.setTitle("Input data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					startDate.clear();
					endDate.clear();
					return;
				}
			}
		});
		save_report.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent arg0) {
				try {
					// there is nothing to choose in combo box
					if (factory.getMilkDataFromFarms().size() == 0) {
						Alert fail = new Alert(Alert.AlertType.WARNING,
								"There is no data currently, please load from file or add by yourself!");
						fail.setTitle("Select data error");
						fail.setHeaderText("Warning");
						fail.showAndWait();
						return;
					}

					String[] startDateString = startDate.getText().trim().split("-");
					String[] endDateString = endDate.getText().trim().split("-");
					Integer year1 = Integer.parseInt(startDateString[0]);
					Integer month1 = Integer.parseInt(startDateString[1]);
					Integer day1 = Integer.parseInt(startDateString[2]);

					Integer year2 = Integer.parseInt(endDateString[0]);
					Integer month2 = Integer.parseInt(endDateString[1]);
					Integer day2 = Integer.parseInt(endDateString[2]);

					if ((year1 * 10000 + month1 * 100 + day1) > (year2 * 10000 + month2 * 100 + day2)) {
						throw new Exception();
					}

					// show date range report data to table
					ArrayList<String> list = dataManager.getDateRangeReport(year1, month1, day1, year2, month2, day2,
							factory);
					ArrayList<String> dateRangeReportStringList = new ArrayList<>();
					Collections.sort(list);

					String[] readData;
					for (int i = 0; i < list.size(); i++) {
						readData = list.get(i).trim().split(",");
						if (Double.parseDouble(readData[1]) != 0.0) {
							dateRangeReportStringList.add(list.get(i));
						}
					}
					// check if there has data to be saved in the file
					if (dateRangeReportStringList.size() == 0) {
						Alert fail = new Alert(Alert.AlertType.WARNING,
								"There is no data currently for your specified date range!");
						fail.setTitle("Select data error");
						fail.setHeaderText("Warning");
						fail.showAndWait();
						return;
					} else if (table.getItems().size() == 0) {
						Alert fail = new Alert(Alert.AlertType.WARNING,
								"Please press CONFIRM first to show your date range report!");
						fail.setTitle("Select data error");
						fail.setHeaderText("Warning");
						fail.showAndWait();
						return;
					}

					FileChooser fileChooser = new FileChooser();
					fileChooser.setTitle("Save data file");
					fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CSV", "*.csv"));

					// choose multiple files
					File file = fileChooser.showSaveDialog(primaryStage);
					if (file == null) {
						Alert showWarn = new Alert(Alert.AlertType.WARNING, "Please write data file into valid place!");
						showWarn.setTitle("Write file error");
						showWarn.setHeaderText("Warning");
						showWarn.showAndWait();
						return;
					} else {
						// write date range report to file
						if (fileManager.writeDateRangeReportToFile(file.getName(), startDate.getText(), endDate.getText(),
								dateRangeReportStringList, factory)) {
							System.out.println(file.getAbsolutePath());
							System.out.println(file);
							Alert success = new Alert(Alert.AlertType.CONFIRMATION, "Successful");
							ButtonType button1 = new ButtonType("OK");
							success.getButtonTypes().setAll(button1);
							success.setTitle("Successful");
							success.showAndWait();
						} else {
							Alert showError = new Alert(Alert.AlertType.ERROR, "Write file with error: "
									+ file.getPath() + "\n\nThere exist bad data!\n"
									+ "(Bad data means data with missing parts, error parts, or dupliate date & farm Id)!");
							showError.setTitle("Write file error");
							showError.setHeaderText("Error");
							showError.showAndWait();
							return;
						}
					}

				} catch (Exception e) {
					Alert fail = new Alert(Alert.AlertType.WARNING, "Please input valid data in correct form: \n"
							+ "yyyy/mm/dd(must be valid date)!" + "\n\nNote: End date should be after start date!");
					fail.setTitle("Input data error");
					fail.setHeaderText("Warning");
					fail.showAndWait();
					startDate.clear();
					endDate.clear();
					return;
				}

			}
		});
		root.setBottom(table);

		Label title = new Label("Get Date Range Report");
		title.setFont(Font.font(java.awt.Font.SERIF, 25));
		// title.setAlignment(Pos.BASELINE_CENTER);
		VBox v = new VBox();
		v.getChildren().addAll(new Label(""), title, new Label(""));
		v.setAlignment(Pos.CENTER);
		root.setTop(v);

		Scene scene = new Scene(root, 750, 580);
		return scene;
	}

	/**
	 * 
	 * sample_data - TODO Describe purpose of this user-defined type
	 * 
	 * @author mr.zeta0
	 *
	 */
	public static class sample_data implements Comparable<sample_data> {
		SimpleStringProperty farmId;
		SimpleDoubleProperty weight;
		SimpleStringProperty percent;

		@Override
		public int compareTo(sample_data data2) {
			return getFarmId().compareTo(data2.getFarmId());
		}

		public sample_data(String farmId, double weight, String percent) {
			this.farmId = new SimpleStringProperty(farmId);
			this.weight = new SimpleDoubleProperty(weight);
			this.percent = new SimpleStringProperty(percent);
		}

		public String getPercent() {
			return percent.get();
		}

		public String getFarmId() {
			return farmId.get();
		}

		public double getWeight() {
			return weight.get();
		}
	}

	public static class sample_data1 {
		SimpleIntegerProperty month;
		SimpleDoubleProperty weight;
		SimpleStringProperty percent;

		public sample_data1(int num, double weight, String percent) {
			this.month = new SimpleIntegerProperty(num);
			this.weight = new SimpleDoubleProperty(weight);
			this.percent = new SimpleStringProperty(percent);

		}

		public String getPercent() {
			return percent.get();
		}

		public double getWeight() {
			return weight.get();
		}

		public int getMonth() {
			return month.get();
		}

	}

	public static class sample_data3 {

		SimpleIntegerProperty date;
		SimpleIntegerProperty weight;
		SimpleStringProperty farmId;

		public sample_data3(String date, String farmId, int weight) {
			String dateArray[] = date.trim().split("-");
			int year = Integer.parseInt(dateArray[0]);
			int month = Integer.parseInt(dateArray[1]);
			int day = Integer.parseInt(dateArray[2]);
			this.date = new SimpleIntegerProperty(year * 10000 + month * 100 + day);
			this.farmId = new SimpleStringProperty(farmId);
			this.weight = new SimpleIntegerProperty(weight);
		}

		public Integer getDate() {
			return date.get();
		}

		public int getWeight() {
			return weight.get();
		}

		public String getFarmId() {
			return farmId.get();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}

}
