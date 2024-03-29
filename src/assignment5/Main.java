/* CRITTERS Main.java
 * EE422C Project 5 submission by
 * Shashank Kambhampati
 * skk834
 * 16445
 * Pranav Harathi
 * sh44674
 * 16460
 * Slip days used: 0
 * Fall 2016
 */
package assignment5; // cannot be in default package
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextArea;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import java.lang.reflect.Modifier;
import java.io.File;
import java.util.Collections;
import java.net.URL;

public class Main extends Application{
    private static String myPackage;    // package of Critter file.  Critter cannot be in default pkg.
    private static boolean running = false;
	private static boolean display = false;
    private static final long RUNWAIT = 1000;
	public static final int SCREENHEIGHT = 700;
	public static final int SCREENWIDTH = 1200;

    public static GridPane world;
    private static TextArea out;

    // Gets the package name.  The usage assumes that Critter and its subclasses are all in the same package.
    static {
        myPackage = Critter.class.getPackage().toString().split(" ")[1];
    }

	/**
	 * Appends the given text to the output on screen.
	 * @pram outText the text to append
	 */
    public static void setOutputText(String outText){
        out.appendText(outText + "\n");
    }

	private static void stats(String critterName){
		try{
			java.util.List<Critter> statList = Critter.getInstances(critterName);

			Class<?>[] params = {List.class};


			Class<? extends Critter> stats = Class.forName(myPackage + "." + critterName)
					.asSubclass(Critter.class);

			stats.getMethod("runStats", params).invoke(null, statList);
		} catch(Exception e){
			setOutputText("Error processing previous command");
		}
	}

    private static TextField numericTextField(){
        TextField textField = new TextField();
        textField.textProperty().addListener( (observable, oldVal, newVal) ->{
            if(!newVal.matches("\\d*")){
                textField.setText(newVal.replaceAll("[^\\d]", ""));
            }
        });

        return textField;
    }

    private static List<String> getCritterList() {
        String name = Critter.class.getSimpleName();
        name += ".class";

        URL file = Critter.class.getResource(name);
        String dir = file.getPath();
        dir = dir.substring(0, dir.lastIndexOf("/"));

        File current = new File(dir);

        List<String> critters = new ArrayList<>();
        File[] list = current.listFiles();
        
        for(File e : list){
            String className = e.getName();
            if(className.endsWith(".class")){

                className = className.substring(0, className.length() - 6);

                try{
                    Class<? extends Critter> critterClass = Class.forName(myPackage + "." + className).asSubclass(Critter.class);
                    if(!Modifier.isAbstract(critterClass.getModifiers())){
                        critters.add(className);
                    }
                    
                } catch(Throwable err){
                    
                }
            }
        }

        Collections.sort(critters);

        return critters;

    }

    private static void addButtons(GridPane grid){
        List<String> critters = getCritterList();

		// checkboxes
		int start = 7;
		grid.add(new Label("During animation, run stats on:"),0, start);
		start++;
		ArrayList<CheckBox> animateStats = new ArrayList<CheckBox>();
		for(String critterName : critters){
			CheckBox toAdd = new CheckBox(critterName);
			animateStats.add(toAdd);
			grid.add(toAdd, 0, start);
			start++;
		}
        
        // seed ui elements
        TextField seedText = numericTextField();
        seedText.setPromptText("seed number");
        Button seedButton = new Button("seed");
        EventHandler<ActionEvent> seedAction = actionEvent ->{
            int seed = 0;
            try{
                seed = Integer.parseInt(seedText.getText());
            } catch (NumberFormatException e) { }
			Critter.setSeed(seed);
            seedText.clear();
        };
        seedButton.setOnAction(seedAction);
        seedText.setOnAction(seedAction);
        grid.add(seedText, 0, 0);
        grid.add(seedButton, 1, 0);

        // step ui elements
        Button stepButton = new Button("step");
        TextField stepText = numericTextField();
        stepText.setPromptText("number of steps");
        EventHandler<ActionEvent> stepAction = actionEvent ->{
			int steps;
            try{
                steps = Integer.parseInt(stepText.getText());
            } catch(NumberFormatException e){
				steps = 1;
            }

			for(int i = 0; i < steps; i++){
				Critter.worldTimeStep();
			}

			Critter.displayWorld();
			stepText.clear();
        };
        stepButton.setOnAction(stepAction);
        stepText.setOnAction(stepAction);
        grid.add(stepText, 0, 1);
        grid.add(stepButton, 1, 1);

        // make ui elements
        ComboBox<String> makeText = new ComboBox<>();
        TextField makeNumber = numericTextField();
        Button makeButton = new Button("make");

        HBox makebox = new HBox();
        makebox.getChildren().addAll(makeText, makeNumber);

        for(String e : critters){
            makeText.getItems().add(e);
        }

        makeNumber.setPromptText("number");
        makeNumber.setPrefWidth(80);
		makeText.setValue("Algae");
        EventHandler<ActionEvent> makeAction = actionEvent -> {
            int num;
            try{
                num = Integer.parseInt(makeNumber.getText());

            } catch (NumberFormatException e){
                num = 1;
            }

            try{
                for(int i = 0; i < num; i++){
                    Critter.makeCritter(makeText.getValue());
                }
            } catch(InvalidCritterException e){
				setOutputText("Error processing previous command");
            }
            Critter.displayWorld();
            makeNumber.clear();
        };
        makeButton.setOnAction(makeAction);
        makeNumber.setOnAction(makeAction);
        grid.add(makebox, 0, 3);
        grid.add(makeButton, 1, 3);

        // stats ui elements
        Button statsButton = new Button("stats");
        ComboBox<String> statsText = new ComboBox<>();

        for(String e : critters){
            statsText.getItems().add(e);
        }

		statsText.setValue("Algae");

        EventHandler<ActionEvent> statsEvent = actionEvent -> {
			stats(statsText.getValue());
        };
        statsButton.setOnAction(statsEvent);
        grid.add(statsText, 0, 4);
        grid.add(statsButton, 1, 4);

        // quit ui element
        Button quitButton = new Button("quit");
        quitButton.setOnAction(actionEvent ->{
            Stage stage = (Stage) quitButton.getScene().getWindow();
            stage.close();
        });
        grid.add(quitButton, 0, 6);



        List<Node> uiElements = new ArrayList<>();
		uiElements.addAll(animateStats);
        uiElements.add(quitButton);
        uiElements.add(statsButton);
        uiElements.add(stepButton);
        uiElements.add(stepText);
        uiElements.add(statsText);
        uiElements.add(seedButton);
        uiElements.add(seedText);
        uiElements.add(makeText);
        uiElements.add(makeButton);
        uiElements.add(makeNumber);

        //run ui elements
        Button runStart = new Button("run");
        Button runEnd = new Button("stop");
        Slider runSteps = new Slider(1, 30, 1);
        uiElements.add(runStart);
		uiElements.add(runSteps);

        runStart.setOnAction(actionEvent ->{
            for(Node e : uiElements){
                e.setDisable(true);
            }
            int stepCount = (int) Math.round(runSteps.getValue());
            running = true;
            Thread runner = new Thread(() ->{
                while(running){
                    for(int i = 0; i < stepCount; i++){
                        Critter.worldTimeStep();
                    }
					Platform.runLater(() -> {
							Critter.displayWorld();
							for(CheckBox box : animateStats){
								if(box.isSelected()) stats(box.getText());
							}
						});
                    try{
                        Thread.sleep(RUNWAIT);
                    } catch(InterruptedException e){} 
                }
				});
				runner.setDaemon(true);
				runner.start();
        });

        runEnd.setOnAction(actionEvent ->{
            running = false;
            for(Node e : uiElements){
                e.setDisable(false);
            }
        });
        grid.add(runSteps, 0, 2);
        grid.add(runStart, 1, 2);
        grid.add(runEnd, 2, 2);

    }

    @Override
	/**
	 * Starts the JavaFX project.
	 * @param primaryStage The stage to display on
	 */
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Critters");

        GridPane input = new GridPane();
        world = new GridPane();
		out = new TextArea();
		out.setEditable(false);
		out.setPrefHeight(80);
		out.setMaxHeight(80);
		out.setMinHeight(80);
        input.setPadding(new Insets(10, 10, 10, 10));
        input.setHgap(5);
        input.setVgap(5);

        addButtons(input);


        BorderPane mainScreen = new BorderPane();

        mainScreen.setLeft(input);
        mainScreen.setCenter(world);
		mainScreen.setBottom(out);
        mainScreen.setPadding(new Insets(10, 10, 10, 10));

        primaryStage.setScene(new Scene(mainScreen, SCREENWIDTH, SCREENHEIGHT));
		Critter.displayWorld();
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }

}
