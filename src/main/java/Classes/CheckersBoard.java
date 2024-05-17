package Classes;

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
import Enums.PieceType;

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
    private Button[] buttons;
    private Group tileGroup;
    private Group pieceGroup;
    private Group textGroup;
    private Group buttonGroup;
    private double mouseX;
    private double mouseY;
    private int[] cordinations;
    private View view;
    int secondsPassed = 0;
    Pane root;
    Text text;
    Timeline[] timers;

    public CheckersBoard(){
        board = new Tile[WIDTH][HEIGHT];
        texts = new ArrayList<>();
        buttons = new Button[10];
        tileGroup = new Group();
        pieceGroup = new Group();
        textGroup = new Group();
        buttonGroup = new Group();
        cordinations = new int[2];
        timers = new Timeline[10];
    }

    /**
     * creates an empty board and initializes the data structures of the text and button
     * @return Parent
     */
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

        root.getChildren().addAll(tileGroup, pieceGroup, rectangle1, rectangle2, textGroup, buttonGroup);
        return root;
    }

    /**
     * creates the board with the pieces
     */
    public void createBoard(){
        tileGroup.getChildren().clear();
        int tileNumber = 1;
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                Tile tile = new Tile((x + y) % 2 == 0, x ,y);
                tile.setOnMouseClicked(e -> {
                    mouseX = e.getSceneX();
                    mouseY = e.getSceneY();

                    cordinations[0] = toBoard(mouseX);
                    cordinations[1] = toBoard(mouseY);
                    view.takeInput(toBoard(mouseX), toBoard(mouseY));
                });
                board[y][x] = tile;
                tileGroup.getChildren().add(tile);

                Text tileNumberText = new Text(String.valueOf(tileNumber));
                tileNumberText.setFont(Font.font(50));
                tileNumberText.setFill(Color.gray(0.3));

                tileNumberText.setX(x * TILE_SIZE + ((double) TILE_SIZE) / 2 - 20);
                tileNumberText.setY(y * TILE_SIZE + ((double) TILE_SIZE) / 2 + 20);

                tileNumber++;
            }
        }


    }

    /**
     * converts the pixel to coordinates
     * @param pixel - the pixel to be converted
     * @return int - the coordinate
     */
    private int toBoard(double pixel){
        return (int)(pixel / TILE_SIZE);
    }

    /**
     * changes the color of the piece
     * @param col - the column of the piece
     * @param row - the row of the piece
     * @param color - the color to be changed to
     */
    public void changePieceColor(int col, int row, String color){
        board[col][row].getPiece().setColor(color);
    }

    /**
     * adds a piece to the board
     * @param type - the type of the piece
     * @param col - the column of the piece
     * @param row - the row of the piece
     */
    public void addPiece(PieceType type, int col, int row){
        ViewPiece viewPiece = makePiece(type, col, row);
        board[col][row].setPiece(viewPiece);
        pieceGroup.getChildren().add(viewPiece);
    }

    /**
     * removes all the pieces from the board
     */
    public void removePieces(){
        createBoard();
        pieceGroup.getChildren().clear();
    }

    /**
     * creates a piece
     * @param type - the type of the piece
     * @param x - the x coordinate of the piece
     * @param y - the y coordinate of the piece
     * @return ViewPiece - the piece created
     */
    private ViewPiece makePiece(PieceType type, int x, int y){
        ViewPiece viewPiece = new ViewPiece(type, x, y, (type == PieceType.WHITEPIECE || type == PieceType.WHITEKING) ? "#fff9f4" : "#c40003");
        viewPiece.setOnMouseClicked(e -> {
            mouseX = e.getSceneX();
            mouseY = e.getSceneY();
            cordinations[0] = toBoard(mouseX);
            cordinations[1] = toBoard(mouseY);
            view.takeInput(toBoard(mouseX), toBoard(mouseY));
        });
        return viewPiece;
    }

    /**
     * initializes the board and adds the key event listener to the scene
     */
    public void initializeBoard(){
        Scene boardPane = new Scene(createContent());

        boardPane.setOnKeyPressed(event -> {
                view.onKeyPressed(event.getCode().getName());

        });

        primaryStage.setTitle("CheckersBoard");
        primaryStage.setScene(boardPane);
        primaryStage.show();
    }

    /**
     * starts the whole application by creating the split pane and the view
     * @param primaryStage - the stage to be updated
     */
    public void start(Stage primaryStage) {
        this.splitPane = new SplitPane();
        this.primaryStage = primaryStage;
        view = new View(this);
    }


    /**
     * creates a text message or overwrites an existing text message
     * @param message - the message to be displayed
     * @param x - the x coordinate of the message
     * @param y - the y coordinate of the message
     * @param bold - whether the text should be bold or not
     * @param textId - the id of the text
     */
    public void message(String message, int x, int y, boolean bold, int textId) {
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
    }

    /**
     * sets a timer with the given duration and the given seconds
     * @param setSeconds - the seconds to be set
     * @param duration - the duration of the timer
     * @param timerId - the id of the timer
     */
    public void setTimer(int setSeconds, int duration, int timerId){
        secondsPassed = setSeconds;
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), event -> {
            secondsPassed++;
            view.updateTimer(secondsPassed);
        }));

        timeline.setCycleCount(duration);
        timeline.play();
        timers[timerId] = timeline;
    }

    /**
     * stops the timer with the given id
     * @param timerId - the id of the timer
     */
    public void stopTimer(int timerId){
        if(timers[timerId] != null)
            timers[timerId].stop();
    }

    /**
     * asks a question with two answers
     * @param message - the message to be displayed
     * @param ans1 - the first answer
     * @param ans2 - the second answer
     * @param questionType - the type of the question
     */
    public void askTwoAnswers(String message, String ans1, String ans2, String questionType){
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
        });


        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(button1, button2);

        VBox root = new VBox(10);
        root.setSpacing(40);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(questionText, buttonBox);

        Scene scene = new Scene(root, 1000, 700);

        secondaryStage.setScene(scene);
        secondaryStage.setTitle("Checkers");
        secondaryStage.show();
    }

    /**
     * adds a button to the board
     * @param message - the message to be displayed on the button
     * @param x - the x coordinate of the button
     * @param y - the y coordinate of the button
     * @param buttonId - the id of the button
     */
    public void addButton(String message, int x, int y, int buttonId){
        Button button = new Button(message);

        button.setFont(new Font(40));
        button.setLayoutX(x);
        button.setLayoutY(y);

        button.setOnAction(event -> {
            view.buttonPushed(message);
        });


        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().add(button);

        VBox root = new VBox(10);
        root.setSpacing(40);
        root.setAlignment(Pos.CENTER);
        root.getChildren().add(buttonBox);

        if(buttons[buttonId] != null)
            buttonGroup.getChildren().remove(buttons[buttonId]);

        buttons[buttonId] = button;
        buttonGroup.getChildren().add(button);

    }

    /**
     * removes the button with the given id
     * @param buttonId - the id of the button
     */
    public void removeButton(int buttonId){
        Button button = buttons[buttonId];
        if(button != null)
            buttonGroup.getChildren().remove(button);
        buttons[buttonId] = null;
    }

    /**
     * quits the application
     */
    public void quit(){
        primaryStage.close();
    }

    /**
     * the main method that launches the application
     * @param args - the arguments to be passed
     */
    public static void main(String[] args) {
        launch();
    }

    /**
     * asks a question with three answers
     * @param message - the message to be displayed
     * @param ans1 - the first answer
     * @param ans2 - the second answer
     * @param ans3 - the third answer
     * @param questionType - the type of the question
     */
    public void askThreeAnswers(String message, String ans1, String ans2, String ans3, String questionType) {
        Stage secondaryStage = new Stage();

        Text questionText = new Text(message);

        questionText.setFont(new Font(40));
        Button button1 = new Button(ans1);
        Button button2 = new Button(ans2);
        Button button3 = new Button(ans3);

        button1.setFont(new Font(40));
        button2.setFont(new Font(40));
        button3.setFont(new Font(40));

        button1.setOnAction(event -> {
            view.buttonPushed(message, ans1, questionType);
            secondaryStage.close();
        });

        button2.setOnAction(event -> {
            view.buttonPushed(message, ans2, questionType);
            secondaryStage.close();
        });

        button3.setOnAction(event -> {
            view.buttonPushed(message, ans3, questionType);
            secondaryStage.close();
        });

        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(button1, button2, button3);

        VBox root = new VBox(10);
        root.setSpacing(40);
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(questionText, buttonBox);

        Scene scene = new Scene(root, 1000, 700);

        secondaryStage.setScene(scene);
        secondaryStage.setTitle("Checkers");
        secondaryStage.show();
    }
}