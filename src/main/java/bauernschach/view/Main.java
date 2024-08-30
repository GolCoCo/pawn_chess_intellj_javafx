package bauernschach.view;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

/** Main GUI class. */
public class Main extends Application {

  /**
   * Launches the pawnchess GUI.
   *
   * @param args currently unused
   */
  public static void main(String[] args) {
    launch(args);
  }

  /** Creates a new instance of this class. */
  public Main() {}

  @Override
  public void start(Stage primaryStage) {
    primaryStage.setTitle("Bauernschach");
    // TODO: Build and show welcome screen
    Text txtTitle = new Text("Pawn Chess");
    txtTitle.setFont(Font.font("Verdana", FontWeight.BOLD, 40));
    
    Button btnStartGame = new Button("Start Game");
    btnStartGame.setFont(Font.font("Verdana", FontWeight.BOLD, 16));
    btnStartGame.setMinWidth(180);
    btnStartGame.setMinHeight(40);
    btnStartGame.setCursor(Cursor.HAND);
    btnStartGame.setOnAction((event)->{
    	primaryStage.hide();
    	ChessPane pane = new ChessPane(primaryStage);
    	pane.ShowChessPane();
    });
    
    GridPane gridPane = new GridPane();
    gridPane.setPadding(new Insets(10, 10, 10, 10)); 
    gridPane.setVgap(200); 
    gridPane.setHgap(50);
    
    gridPane.add(txtTitle, 0, 0); 
    gridPane.add(btnStartGame, 0, 1);
    
    VBox vBox = new VBox();
    vBox.getChildren().add(txtTitle);
    vBox.getChildren().add(btnStartGame);
    vBox.setAlignment(Pos.CENTER);
    vBox.setSpacing(150);
    
    Scene scene = new Scene(vBox);
    primaryStage.setWidth(500);
    primaryStage.setHeight(400);
    primaryStage.setScene(scene);
    primaryStage.show();
  }
}
