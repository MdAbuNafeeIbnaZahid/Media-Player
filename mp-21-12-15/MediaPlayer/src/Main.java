import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.media.MediaPlayer;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by USER on 12/3/2015.
 */

public class Main extends Application{
    Stage stage, helpStage, aboutStage, playListStage, getNewPosStage;
    MediaPlayerController mediaPlayerController;
    AboutController aboutController;
    HelpController helpController;
    PlayListController playListController;
    Duration seekDuration;
    MultipleFileRunThread multipleFileRunThread;

    List<File> trueSerialPlayList = new ArrayList<>();
    List<File> playList = new ArrayList<>();

    static ObservableList<ToPlay> toPlayObservableList = FXCollections.observableArrayList();
    static ObservableList<ToPlay> trueSerialToPlayObservableList = FXCollections.observableArrayList();

    @Override
    public void start(Stage stage) throws Exception{
        this.stage = stage;
        initialize();

    }

    public void initialize() throws Exception{
        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("mediaPlayer.fxml"));
        Parent root = loader.load();

        // Loading the controller
        mediaPlayerController = loader.getController();
        mediaPlayerController.setMain(this);

        mediaPlayerController.playPauseButton.setGraphic(new ImageView(new Image("/icons/play.png")));
        mediaPlayerController.sB.setGraphic(new ImageView(new Image("/icons/stop.png")));
        mediaPlayerController.pB.setGraphic(new ImageView(new Image("/icons/prev.png")));
        mediaPlayerController.nB.setGraphic(new ImageView(new Image("/icons/next.png")));
        mediaPlayerController.vol.setGraphic(new ImageView(new Image("/icons/vol.png")));

        // Set the primary stage
        stage.setTitle("RN Media Player");
        Scene scene = new Scene(root, 800, 550);



        mediaPlayerController.mediaView.fitWidthProperty().bind(scene.widthProperty().subtract(20));
        mediaPlayerController.mediaView.fitHeightProperty().bind(scene.heightProperty().subtract(60));
        stage.setScene(scene);
        stage.setResizable(true);

        stage.show();
        //

        mediaPlayerController.keyHandler();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Main stage is closing");
                System.exit(0);
            }
        });

    }

    public File chooseFile(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose file");
        File file;
        try
        {
            file = fileChooser.showOpenDialog(stage);
            System.out.println( file.getPath() );
            return file;
        }
        catch (Exception e)
        {
            System.out.println("File not found");
            return null;
        }


    }



    public void showAbout() throws Exception
    {
        // If no about stage is open, then I will open a stage
        if (aboutStage == null)
        {
            // XML Loading using FXMLLoader
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("about.fxml"));
            Parent root = loader.load();

            // Loading the controller
            aboutController = loader.getController();
            aboutController.setMain(this);

            // Set the primary stage
            aboutStage = new Stage();
            Scene scene = new Scene(root, 600, 400);
            aboutStage.setTitle("About RN Media Player");
            aboutStage.setScene(scene);
            aboutStage.setResizable(true);
            aboutStage.show();


            aboutStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                public void handle(WindowEvent we) {
                    System.out.println("About stage is closing");
                    aboutStage = null;
                }
            });
        }

        else
        {
            System.out.println("Already an about stage open");
            return;
        }

    }



    public void showHelp() throws Exception
    {

        // Checking if already an help stage open
        if (helpStage != null)
        {
            System.out.println("Already an help stage open ");
            return;
        }

        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("help.fxml"));
        Parent root = loader.load();

        // Loading the controller
        helpController = loader.getController();
        helpController.setMain(this);

        // Set the primary stage
        helpStage = new Stage();
        helpStage.setTitle("RN Media Player");
        helpStage.setScene(new Scene(root, 600, 400));
        helpStage.setResizable(true);
        helpStage.show();


        helpStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Help stage is closing");
                helpStage = null;
            }
        });
    }



    public void showPlayList() throws Exception
    {

        // Checking if already an play list stage open
        if (playListStage != null)
        {
            System.out.println("Already an play list stage open ");
            return;
        }

        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("playList.fxml"));
        Parent root = loader.load();

        // Loading the controller
        playListController = loader.getController();
        playListController.setMain(this);
        playListController.setFilePathColumn();
        playListController.setTableViewItem(toPlayObservableList);

        // Set the primary stage
        playListStage = new Stage();
        playListStage.setTitle("RN Media Player");
        playListStage.setScene(new Scene(root, 600, 400));
        playListStage.setResizable(true);
        playListStage.show();


        playListStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Play list stage is closing");
                playListStage = null;
            }
        });
    }

    public void setSeekDuration() throws Exception
    {
        // Checking if already an play list stage open
        if (getNewPosStage != null)
        {
            System.out.println("Already an play list stage open ");
            return;
        }

        // XML Loading using FXMLLoader
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("getNewPos.fxml"));
        Parent root = loader.load();

        // Loading the controller
        GetNewPosController getNewPosController = loader.getController();
        getNewPosController.setMain(this);

        // Set the primary stage
        getNewPosStage = new Stage();
        Scene scene = new Scene(root, 300, 150);
        getNewPosStage.setTitle("Enter new position");
        getNewPosStage.setScene(scene);
        getNewPosStage.setResizable(true);
        getNewPosStage.show();


        getNewPosStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Play list stage is closing");
                getNewPosStage = null;
            }
        });


    }


}
