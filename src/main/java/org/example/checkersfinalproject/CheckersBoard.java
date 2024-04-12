package org.example.checkersfinalproject;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;

public class CheckersBoard extends Application {
    public static final int TILE_SIZE = 100;
    public static final int WIDTH = 8;
    public static final int HEIGHT = 8;
    private Stage primaryStage;
    private SplitPane splitPane;
    private Tile[][] board;
    private ArrayList<Text> texts;
    private Group tileGroup;
    private Group pieceGroup;
    private Group textGroup;
    private double mouseX;
    private double mouseY;
    private int[] cordinations;
    private boolean takeInput;
    private View view;
    int secondsPassed = 0;
    Pane root;
    Text text;
    Timeline[] timers;

    public CheckersBoard(){
        board = new Tile[WIDTH][HEIGHT];
        texts = new ArrayList<>();
        tileGroup = new Group();
        pieceGroup = new Group();
        textGroup = new Group();
        cordinations = new int[2];
        takeInput = false;
        timers = new Timeline[10];
    }
    private Parent createContent(){
        Pane root = new Pane();
        root.setPrefSize(1100, 800);

        createBoard();
        Rectangle rectangle1 = new Rectangle(800, 0, 300, 400);
        Rectangle rectangle2 = new Rectangle(800, 400, 300, 400);

        rectangle1.setFill(Color.WHITE);
        rectangle1.setStroke(Color.BLACK);
        rectangle1.setStrokeWidth(2);

        rectangle2.setFill(Color.WHITE);
        rectangle2.setStroke(Color.BLACK);
        rectangle2.setStrokeWidth(2);

        root.getChildren().addAll(tileGroup, pieceGroup, rectangle1, rectangle2, textGroup);
        return root;
    }

    public void createBoard(){
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x ,y);
                tile.setOnMouseClicked(e -> {
                    mouseX = e.getSceneX();
                    mouseY = e.getSceneY();
                    //System.out.println("x: " + toBoard(mouseX));
                    //System.out.println("Y: " + toBoard(mouseY));

                    cordinations[0] = toBoard(mouseX);
                    cordinations[1] = toBoard(mouseY);
                    takeInput = false;
                    view.takeInput(toBoard(mouseX), toBoard(mouseY));
                });
                board[y][x] = tile;
                tileGroup.getChildren().add(tile);

            }
        }


    }

//    public Pane createTextBoard(){
//        Pane root = new Pane();
//        root.setPrefSize(100, 800);
//        Button switchToScene1Button = new Button("Open Window 1");
//        StackPane layout2 = new StackPane(switchToScene1Button);
//        Scene scene2 = new Scene(layout2, 100, 800);
//        root.getChildren().addAll(switchToScene1Button);
//        return root;
//    }
    public void updatePieces(Tile[][] updatedBoard){
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                if(updatedBoard[y][x].hasPiece()){
                    if(board[y][x].hasPiece()){
                        if(board[y][x].getPiece().getType() != updatedBoard[y][x].getPiece().getType()) {
                            pieceGroup.getChildren().remove(board[y][x].getPiece());
                            board[y][x].setPiece(makePiece(PieceType.REDPIECE, x, y));
                            pieceGroup.getChildren().add(board[y][x].getPiece());
                        }
                    }

                    else if(board[y][x].hasPiece()){
                        pieceGroup.getChildren().remove(board[y][x].getPiece());
                        board[y][x].setPiece(null);
                    }
                }
            }
        }
    }

    private int toBoard(double pixel){
        return (int)(pixel / TILE_SIZE);
    }

    public void addPiece(PieceType type, int col, int row){
        ViewPiece viewPiece = makePiece(type, col, row);
        board[col][row].setPiece(viewPiece);
        pieceGroup.getChildren().add(viewPiece);
    }

    public void removePieces(){
        createBoard();
        pieceGroup.getChildren().clear();
    }

    private ViewPiece makePiece(PieceType type, int x, int y){
        ViewPiece viewPiece = new ViewPiece(type, x, y);
        viewPiece.setOnMouseClicked(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            //System.out.println("x: " + toBoard(mouseX));
            //.out.println("Y: " + toBoard(mouseY));
            cordinations[0] = toBoard(mouseX);
            cordinations[1] = toBoard(mouseY);
            takeInput = false;
            view.takeInput(toBoard(mouseX), toBoard(mouseY));
        });
        return viewPiece;
    }

    public int[] getInput(){
        cordinations = new int[2];
        takeInput = true;
        return cordinations;
    }

    public void initializeBoard(){
        Scene boardPane = new Scene(createContent());
        //Pane textPane = createTextBoard();
//        SplitPane splitPane = new SplitPane();
//        splitPane.getItems().addAll(new StackPane(), new StackPane());
//        splitPane.setDividerPositions(0.5f);

        // Set Scenes to StackPanes within SplitPane
        //((StackPane) splitPane.getItems().get(0)).getChildren().add(boradScene);
//        ((StackPane) splitPane.getItems().get(0)).getChildren().add(boardPane);
//        ((StackPane) splitPane.getItems().get(1)).getChildren().add(textPane);

        //Scene scene = new Scene(boardPane);



        primaryStage.setTitle("CheckersBoard");
        primaryStage.setScene(boardPane);
        primaryStage.show();
    }
    public void start(Stage primaryStage) throws IOException {
//        Scene scene = new Scene(createContent());
//        primaryStage.setTitle("CheckersBoard");
//        primaryStage.setScene(scene);
//        primaryStage.show();

        this.splitPane = new SplitPane();
        this.primaryStage = primaryStage;
        view = new View(this);
    }

    public static void main(String[] args) {
        launch();
    }

    public void message(String message, int x, int y, boolean bold, int textId) {
//        Text text = new Text("Right Border Text");
//        text.setFont(Font.font("Arial", FontWeight.BOLD, 14));
//        text.setFill(Color.WHITE);
//        text.setX(810);
//        text.setY(150);

//        text = new Text(message);
//        text.setFont(Font.font("Arial", FontWeight.BOLD, 40));
//        text.setFill(Color.BLACK);
//        text.setX(820);
//        text.setY(100);
//        textGroup.getChildren().add(text);

        text = new Text(message);
        text.setFont(Font.font("Arial", bold ? FontWeight.BOLD : FontWeight.NORMAL, 40));
        text.setFill(Color.BLACK);
        text.setX(x);
        text.setY(y);

        if(textId < texts.size()){
            textGroup.getChildren().remove(texts.get(textId));
            texts.set(textId, text);
            textGroup.getChildren().add(text);
        }

        else{
            texts.add(textId, text);
            textGroup.getChildren().add(text);
        }

        //textGroup.getChildren().add(text);
    }

    public void setTimer(int setSeconds, int duration, int timerId){
        secondsPassed = setSeconds;
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsPassed++;
            System.out.println(setSeconds + ", " + duration);
            view.updateTimer(secondsPassed);
//                message(secondsPassed);
//                timerText.setText("Timer: " + secondsPassed + " seconds");
        }));

        timeline.setCycleCount(duration);
        timeline.play();

//        if(timerId < timers.size())
//            timers.set(timerId, timeline);
//        else
//            timers.add(timerId, timeline);
        timers[timerId] = timeline;
    }

    public void stopTimer(int timerId){
//        if(timerId < timers.size())
        if(timers[timerId] != null)
            timers[timerId].stop();
    }

//    public void message(String message, int x, int y, boolean bold, int timerId, boolean alert) {
//
//    }

    public void askTwoAnswers(String message, String ans1, String ans2, String questionType, boolean alert) {
        Stage secondaryStage = new Stage();

        Text questionText = new Text(message);

        questionText.setFont(new Font(40));
        Button button1 = new Button(ans1);
        Button button2 = new Button(ans2);

        button1.setFont(new Font(40));
        button2.setFont(new Font(40));
        if (alert) {
            button1.setOnAction(event -> {
                view.buttonPushed(message, ans1, questionType);
                secondaryStage.close();
            });

            button2.setOnAction(event -> {
                view.buttonPushed(message, ans2, questionType);
                secondaryStage.close();
            });
        }

        else{
            button1.setOnAction(event -> {
                view.buttonPushed(message, ans1, questionType);
            });

            button2.setOnAction(event -> {
                view.buttonPushed(message, ans2, questionType);
            });
        }

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(button1, button2);

        VBox root = new VBox(10);
        root.setSpacing(40);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(questionText, buttonBox);

        Scene scene = new Scene(root, 1100, 800);
        if(alert){
            secondaryStage.setScene(scene);
            secondaryStage.setTitle("Checkers");
            secondaryStage.show();
        }

        else{
            primaryStage.setScene(scene);
            primaryStage.setTitle("Checkers");
            primaryStage.show();
        }
//

        // Creating a new stage for the second window

    }
    public void alertMessage(String message) {
        Text questionText = new Text(message);

        questionText.setFont(new Font(40));

        VBox root = new VBox(10);
        root.setSpacing(40);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(questionText);

        Scene scene = new Scene(root, 1100, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public void alertAskTwoAnswers(String message, String ans1, String ans2, String questionType) {
        Stage secondaryStage = new Stage();

        Text questionText = new Text(message);

        questionText.setFont(new Font(40));
        Button button1 = new Button(ans1);
        Button button2 = new Button(ans2);

        button1.setFont(new Font(40));
        button2.setFont(new Font(40));
        button1.setOnAction(event -> {
            view.buttonPushed(message, ans1, questionType);
            secondaryStage.close();
        });

        button2.setOnAction(event -> {
            view.buttonPushed(message, ans2, questionType);
            secondaryStage.close();
            primaryStage.close();
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(button1, button2);

        VBox root = new VBox(10);
        root.setSpacing(40);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(questionText, buttonBox);

        Scene scene = new Scene(root, 1100, 800);

//        primaryStage.setScene(scene);
//        primaryStage.setTitle("Start Game");
//        primaryStage.show();

        // Creating a new stage for the second window
        secondaryStage.setScene(scene);
        secondaryStage.setTitle("game");
        secondaryStage.show();
//        Button showAlertButton = new Button(ans1);
//        showAlertButton.setOnAction(e -> view.buttonPushed(message, ans1, questionType));
//
//        StackPane root = new StackPane();
//        root.getChildren().add(showAlertButton);
//
//        Scene scene = new Scene(root, 300, 250);
//
//        primaryStage.setTitle("Alert Dialog Example");
//        primaryStage.setScene(scene);
//        primaryStage.show();
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("alert");
//        alert.setHeaderText(message);
//        //alert.setContentText("Hello! This is a simple alert dialog example.");
//
//        alert.showAndWait();
    }

    public void quit(){
        primaryStage.close();
    }

}