/**
 * Created by USER on 12/3/2015.
 */
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.SwipeEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.util.Duration;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MediaPlayerController {
    private boolean isFullScreen;
    private Main main;
    private Media media;
    private MediaPlayer mediaPlayer;
    private Duration duration;
    private boolean atEndOfMedia ;
    private boolean repeat;
    private boolean stopRequested ;
    double volume = 1;
    double rate;
    private List<File> playList;
    int previous = 0;
    int next = 0;
    int isRandomized;
    File currentFile;
    int isLoop;

    @FXML
    BorderPane borderPane;

    @FXML
    private MenuItem helpMenuItem;

    @FXML
    private MenuItem aboutMenuItem;

    @FXML
    MediaView mediaView;

    @FXML
    private MenuItem muteButton;

    @FXML
    private MenuItem loopItem;

    @FXML
    private MenuItem randomizeItem;

    @FXML
    private MenuItem pauseButton;

    @FXML
    Button playPauseButton;

    @FXML
    Button sB;

    @FXML
    Button nB;


    @FXML
    javafx.scene.control.MenuBar menuBar;
    @FXML
    Button pB;

    @FXML
    private Slider timeSlider;

    @FXML
    private Label playTime;

    @FXML
    private Slider volumeSlider;

    @FXML
    Button vol;

    @FXML
    private HBox hBox;


    @FXML
    void about(ActionEvent event) throws Exception{



        main.showAbout();
    }

    @FXML
    void closeFile(ActionEvent event) {
        mediaPlayer.stop();
    }

    @FXML
    void decreseVolume(ActionEvent event) {
        decreseVolume();
    }

    @FXML
    void fastenSpeed(ActionEvent event) {
        try
        {
            System.out.println("Before fastening,     rate = " + rate);
            rate = mediaPlayer.getRate() + 0.2;
            mediaPlayer.setRate(rate);
            System.out.println("After fastening,     rate = " + rate);
        }
        catch (Exception e)
        {
            System.out.println("Couldn't fasten");
        }

    }

    @FXML
    void goToSTime(ActionEvent event) throws Exception{
        Duration specificTime = null;
        main.setSeekDuration();






        /*try
        {


            //mediaPlayer.seek( specificTime );
        }
        catch (Exception e)
        {
            System.out.println("Couldn't set time");
        }*/


        //System.out.println(specificTime);
    }



    @FXML
    void help(ActionEvent event) throws Exception{



        MediaPlayer.Status status = null;
        try
        {
            status = mediaPlayer.getStatus();
        }
        catch (Exception e)
        {
            System.out.println(e);
        }



        /*
        if (status != MediaPlayer.Status.UNKNOWN  && status != MediaPlayer.Status.HALTED && status != null)
        {
            if ( status == MediaPlayer.Status.PAUSED
                    || status == MediaPlayer.Status.READY
                    || status == MediaPlayer.Status.STOPPED)
            {
                // rewind the movie if we're sitting at the end


                if (atEndOfMedia) {
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                    atEndOfMedia = false;
                }
                mediaPlayer.play();


            //} else {
                //mediaPlayer.pause();
            }

        }
        */






        main.showHelp();
    }

    @FXML
    void increseVolume(ActionEvent event) {
        increseVolume();
    }

    @FXML
    void loop(ActionEvent event) {
        loop();
    }

    @FXML
    void mute(ActionEvent event) {
        mute();
    }

    @FXML
    void muteUnmute(ActionEvent event){
        mute();
    }

    @FXML
    void nextButton(ActionEvent event) {
        next = 1;
        System.out.println("Next will be played if available");
    }

    @FXML
    void normalSpeed(ActionEvent event) {
        try
        {
            System.out.println("Before normalizing,     rate = " + rate);
            rate = 1;
            mediaPlayer.setRate(rate);
            System.out.println("After normalizing,     rate = " + rate);
        }
        catch (Exception e)
        {
            System.out.println("Couldn't set the speed to normal");
        }

    }

    @FXML
    void openDirectory(ActionEvent event) {

        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Open directory");
        File directory = directoryChooser.showDialog(main.stage);

        if (directory == null)
        {
            System.out.println(" Directory not found !!!!! ");
            return;
        }

        File [] allFiles = directory.listFiles();

        try
        {
            main.trueSerialPlayList = new ArrayList<>();
            main.playList = new ArrayList<>();
            main.toPlayObservableList = FXCollections.observableArrayList();
            main.multipleFileRunThread.thr.stop();
            mediaPlayer.stop();
        }
        catch (Exception e)
        {
            System.out.println( "Previous things could not be closed" );
        }



        for (File file : allFiles)
        {
            if ( file.getName().endsWith(".mp3") || file.getName().endsWith("mp4") || file.getName().endsWith("flv") ) {
                main.trueSerialPlayList.add(file);
                System.out.println(file.getName());
            }
        }


        //main.playList.clear();
        main.playList.addAll(main.trueSerialPlayList);
        if (isRandomized == 1)
        {
            Collections.shuffle( main.playList );
        }


        //main.toPlayObservableList.clear();
        for ( int i = 0;i<main.playList.size();i++ )
        {
            System.out.println( main.playList.get(i) );
            main.toPlayObservableList.add( new ToPlay(main.playList.get(i).getPath() ) );
        }



        main.multipleFileRunThread = new MultipleFileRunThread( main, this );

    }

    @FXML
    void openFile(ActionEvent event) {
        openFile();
    }

    @FXML
    void openMultipleFile(ActionEvent event) {


        /*
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose multiple file");
        playList = fileChooser.showOpenMultipleDialog(main.stage);
        for(int i=0; i<playList.size(); i++)
            System.out.println(playList.get(i));
        */

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Choose multiple file");
        main.trueSerialPlayList = fileChooser.showOpenMultipleDialog(main.stage);



        if (main.trueSerialPlayList == null)
        {
            System.out.println(" Files not found !!!! ");
            return;
        }


        try
        {
            //main.trueSerialPlayList = new ArrayList<>();
            main.playList = new ArrayList<>();
            main.toPlayObservableList = FXCollections.observableArrayList();
            main.multipleFileRunThread.thr.stop();
            mediaPlayer.stop();
        }
        catch (Exception e)
        {
            System.out.println( "Previous things could not be closed" );
        }

        //main.playList.clear();
        main.playList.addAll(main.trueSerialPlayList);
        if (isRandomized == 1)
        {
            Collections.shuffle( main.playList );
        }


        //main.toPlayObservableList.clear();
        for ( int i = 0;i<main.playList.size();i++ )
        {
            System.out.println( main.playList.get(i) );
            main.toPlayObservableList.add( new ToPlay(main.playList.get(i).getPath() ) );
        }



        main.multipleFileRunThread = new MultipleFileRunThread( main, this );

    }

    @FXML
    void pause(ActionEvent event) {
        pause();
    }

    @FXML
    void playButton(){

        MediaPlayer.Status status;

        try
        {
            status = mediaPlayer.getStatus();


            if (status == MediaPlayer.Status.UNKNOWN  || status == MediaPlayer.Status.HALTED)
            {
                // don't do anything in these states
                return;
            }

            if ( status == MediaPlayer.Status.PAUSED
                    || status == MediaPlayer.Status.READY
                    || status == MediaPlayer.Status.STOPPED)
            {
                // rewind the movie if we're sitting at the end
                if (atEndOfMedia) {
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                    atEndOfMedia = false;
                }
                mediaPlayer.play();
            } else {
                mediaPlayer.pause();
            }
        }
        catch (Exception e)
        {
            System.out.println(e.getStackTrace());
        }


    }

    @FXML
    void playNext(ActionEvent event) {
        next = 1;
        System.out.println("Next will be played if available");
    }

    @FXML
    void playPrevious(ActionEvent event) {
        previous = 1;
        System.out.println("Previous will be played if available");
    }

    @FXML
    void prevButton(ActionEvent event) {
        previous = 1;
        System.out.println("Previous will be played if available");
    }



    @FXML
    void quit(ActionEvent event) {
        Platform.exit();
        System.exit(0);
    }

    @FXML
    void randomizePlaylist(ActionEvent event) {
        randomizePlaylist();
    }

    @FXML
    void slowenSpeed(ActionEvent event) {
        try
        {
            System.out.println("Before slowing,     rate = " + rate);
            rate = mediaPlayer.getRate() - 0.1;
            mediaPlayer.setRate(rate);
            System.out.println("After slowing,     rate = " + rate);
        }
        catch (Exception e)
        {
            System.out.println("Couldn't slowen the speed");
        }

    }

    @FXML
    void stop(ActionEvent event) {
        try
        {
            mediaPlayer.stop();
        }
        catch (Exception e)
        {
            System.out.println("Couldn't stop");
        }
    }

    @FXML
    void stopButton(ActionEvent event) {
        try
        {
            mediaPlayer.stop();
        }
        catch (Exception e)
        {
            System.out.println("Couldn't find media to stop");
        }

    }

    @FXML
    void takeSnapshoot(ActionEvent event) {
        takeSnapshoot();
    }

    @FXML
    void toggleFullScreen(ActionEvent event) {
        toggleFullscreen();

    }

    @FXML
    void viewPlaylist(ActionEvent event) throws Exception{
        main.showPlayList();
    }

    @FXML
    void zoom11(ActionEvent event) {
        try {

            int h = mediaPlayer.getMedia().getHeight();
            int w = mediaPlayer.getMedia().getWidth();

            main.stage.setWidth(w + 20);
            main.stage.setHeight(h + 50);
        }
        catch (Exception e){
            System.out.println("No media is running");
        }
    }

    @FXML
    void zoom12(ActionEvent event) {
        try {
            int h = mediaPlayer.getMedia().getHeight();
            int w = mediaPlayer.getMedia().getWidth();

            main.stage.setWidth(w / 2 + 20);
            main.stage.setHeight(h / 2 + 50);
        }
        catch (Exception e){
            System.out.println("No media is running");
        }
    }

    @FXML
    void zoom14(ActionEvent event) {
        try {
            int h = mediaPlayer.getMedia().getHeight();
            int w = mediaPlayer.getMedia().getWidth();

            main.stage.setWidth(w / 4 + 20);
            main.stage.setHeight(h / 4 + 50);
        }
        catch (Exception e){
            System.out.println("No media is running");
        }
    }

    @FXML
    void zoom21(ActionEvent event) {
        try {
            int h = mediaPlayer.getMedia().getHeight();
            int w = mediaPlayer.getMedia().getWidth();

            main.stage.setWidth(w * 2 + 20);
            main.stage.setHeight(h * 2 + 50);
        }
        catch (Exception e){
            System.out.println("No media is running");
        }
    }

    public void setMain(Main main){
        this.main = main;
    }

    public MediaPlayer getMediaPlayer(){return mediaPlayer;}


    public void makeMediaAndPlay(String path)
    {
        media = new Media ( new File(path).toURI().toString() );
        System.out.println("Successfully created media");
        if(mediaPlayer != null)
            mediaPlayer.stop();

        double pastVolume = volume;
        boolean pastIsMute = false;

        try
        {
            pastVolume = mediaPlayer.getVolume();
            pastIsMute = mediaPlayer.isMute();
        }
        catch (Exception e)
        {
            System.out.println("this is the first media playing.");
        }

        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setAutoPlay(true);
        Platform.runLater(() -> {
            mediaView.setMediaPlayer(mediaPlayer);
        });

        keyHandler();





        atEndOfMedia = false;
        stopRequested = false;


        mediaPlayer.setOnPlaying(new Runnable() {
            public void run() {
                if (stopRequested) {
                    mediaPlayer.pause();
                    stopRequested = false;
                } else {
                    playPauseButton.setGraphic(new ImageView(new Image("/icons/pause.png")));
                }
            }
        });


        mediaPlayer.setOnPaused(new Runnable() {
            public void run() {
                System.out.println("onPaused");
                playPauseButton.setGraphic(new ImageView(new Image("/icons/play.png")));
            }
        });



        mediaPlayer.setOnStopped(new Runnable() {
            public void run() {
                System.out.println("onStopped");
                playPauseButton.setGraphic(new ImageView(new Image("/icons/play.png")));
            }
        });

        mediaPlayer.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                updateValues();
            }
        });


        timeSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
            }
        });


        volumeSlider.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
            }
        });

        timeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeSlider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    mediaPlayer.seek(duration.multiply(timeSlider.getValue() / 100.0));
                }
            }
        });

        volumeSlider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (volumeSlider.isValueChanging()) {
                    mediaPlayer.setVolume(volumeSlider.getValue() / 100.0);
                }
            }
        });


        mediaPlayer.setOnReady(new Runnable() {
            public void run() {
                duration = mediaPlayer.getMedia().getDuration();
                updateValues();
            }
        });

        mediaPlayer.setCycleCount(repeat ? MediaPlayer.INDEFINITE : 1);
        mediaPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                if (!repeat) {
                    playPauseButton.setGraphic(new ImageView(new Image("/icons/play.png")));
                    stopRequested = true;
                    atEndOfMedia = true;
                }
            }
        });

        mediaPlayer.setVolume(pastVolume);
        mediaPlayer.setMute(pastIsMute);
    }

    void mute(){
        try {
            if (mediaPlayer.isMute()) {
                mediaPlayer.setMute(false);
                mediaPlayer.setVolume(volume);
                vol.setGraphic(new ImageView(new Image("/icons/vol.png")));
                muteButton.setText("Mute");
            } else {
                volume = mediaPlayer.getVolume();
                mediaPlayer.setMute(true);
                vol.setGraphic(new ImageView(new Image("/icons/mute.png")));
                muteButton.setText("Unmute");
            }
        }
        catch (Exception e){
            //
        }
    }

    void increseVolume(){
        try
        {
            System.out.println( " Before " + mediaPlayer.getVolume() );
            mediaPlayer.setVolume(mediaPlayer.getVolume() + .15);
            System.out.println(" After " + mediaPlayer.getVolume());
        }
        catch (Exception e)
        {
            System.out.println("Failed to increase volume");
        }

    }

    void decreseVolume(){
        try {
            System.out.println( " Before " + mediaPlayer.getVolume() );
            mediaPlayer.setVolume(mediaPlayer.getVolume() - .15);
            System.out.println(" After " + mediaPlayer.getVolume());
        }
        catch ( Exception e)
        {
            System.out.println("Failed to decrease volume");
        }

    }

    void toggleFullscreen(){
        if(!isFullScreen) {
            main.stage.setFullScreen(true);
            isFullScreen = true;
            menuBar.setVisible(false);
            hBox.setVisible(false);
        }
        else {
            main.stage.setFullScreen(false);
            isFullScreen = false;
            menuBar.setVisible(true);
            hBox.setVisible(true);
        }
    }

    void takeSnapshoot(){
        WritableImage image = mediaView.snapshot(new SnapshotParameters(), null);

        int i=1;
        File file = new File("RN-Snapshoot.png");
        while(file.exists()){
            file = new File("RN-Snapshoot"+i+".png");
            i++;
        }

        try {
            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", file);
            System.out.println("A full screenshot saved!");
        } catch (IOException e) {
            System.err.println(e);
        }
    }

    void loop(){
        if (isLoop == 0)
        {
            isLoop = 1;
            System.out.println("Setting isLoop to " + isLoop);
            loopItem.setText("Unloop");
        }

        else if (isLoop == 1)
        {
            isLoop = 0;
            System.out.println("Setting isLoop to " + isLoop);
            loopItem.setText("Loop");
        }
    }

    void randomizePlaylist(){
        if (isRandomized == 0)
        {
            isRandomized = 1;
            System.out.println("Setting isRandomized to " + isRandomized);
            randomizeItem.setText("Normalize");

            Collections.shuffle(main.playList);

            main.toPlayObservableList.clear();
            for ( int i = 0;i<main.playList.size();i++ )
            {
                System.out.println( main.playList.get(i) );
                main.toPlayObservableList.add( new ToPlay(main.playList.get(i).getPath() ) );
            }
        }
        else if (isRandomized == 1)
        {
            isRandomized = 0;
            randomizeItem.setText("Randomize");
            System.out.println("Setting isRandomized to " + isRandomized);

            main.playList.clear();
            main.playList.addAll(main.trueSerialPlayList);

            main.toPlayObservableList.clear();
            for ( int i = 0;i<main.playList.size();i++ )
            {
                System.out.println( main.playList.get(i) );
                main.toPlayObservableList.add( new ToPlay(main.playList.get(i).getPath() ) );
            }
        }
    }

    void pause(){
        try
        {
            MediaPlayer.Status status = mediaPlayer.getStatus();

            if (status == MediaPlayer.Status.UNKNOWN  || status == MediaPlayer.Status.HALTED)
            {
                // don't do anything in these states
                return;
            }

            if ( status == MediaPlayer.Status.PAUSED
                    || status == MediaPlayer.Status.READY
                    || status == MediaPlayer.Status.STOPPED)
            {
                // rewind the movie if we're sitting at the end
                if (atEndOfMedia) {
                    mediaPlayer.seek(mediaPlayer.getStartTime());
                    atEndOfMedia = false;
                }
                mediaPlayer.play();
                pauseButton.setText("Pause");
            } else {
                mediaPlayer.pause();
                pauseButton.setText("Play");
            }
        }
        catch (Exception e)
        {
            System.out.println("Couldn't pause");
        }

    }

    void openFile(){
        File file = main.chooseFile();

        if (file == null)
        {
            System.out.println("File not found ");
            return;
        }



        try
        {
            main.trueSerialPlayList = new ArrayList<>();
            main.playList = new ArrayList<>();
            main.toPlayObservableList = FXCollections.observableArrayList();
            main.multipleFileRunThread.thr.stop();
            mediaPlayer.stop();
        }
        catch (Exception e)
        {
            System.out.println( "Previous things could not be closed" );
        }

        if ( file.getName().endsWith(".mp3") || file.getName().endsWith("mp4") || file.getName().endsWith("flv") ) {
            main.trueSerialPlayList.add(file);
            System.out.println(file.getName());
        }



        //main.playList.clear();
        main.playList.addAll( main.trueSerialPlayList );
        if (isRandomized == 1)
        {
            Collections.shuffle( main.playList );
        }


        //main.toPlayObservableList.clear();
        for ( int i = 0;i<main.playList.size();i++ )
        {
            System.out.println( main.playList.get(i) );
            main.toPlayObservableList.add( new ToPlay(main.playList.get(i).getPath() ) );
        }



        main.multipleFileRunThread =  new MultipleFileRunThread( main, this );
    }

    void keyHandler(){
        Scene scene = main.stage.getScene();
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {


                KeyCode code = event.getCode();

                try
                {
                    if(code.equals(KeyCode.M) && event.isControlDown()){
                        mute();
                    }

                    else if(code.equals(KeyCode.P) && event.isControlDown()){
                        previous = 1;
                    }

                    else if(code.equals(KeyCode.N) && event.isControlDown()){
                        next = 1;
                    }

                    else if(code.equals(KeyCode.Q) && event.isControlDown()){
                        Platform.exit();
                        System.exit(0);
                    }

                    else if(code.equals(KeyCode.O) && event.isControlDown()){
                        openFile();
                    }

                    else if (code.equals(KeyCode.UP))
                        increseVolume();
                    else if (code.equals(KeyCode.DOWN))
                        decreseVolume();
                    else if(code.equals(KeyCode.F11))
                        toggleFullscreen();
                    else if(code.equals(KeyCode.F1)) {
                        try{
                            main.showHelp();
                        }
                        catch (Exception e){
                            //
                        }
                    }
                    else if(code.equals(KeyCode.V) && event.isControlDown()){
                        try{
                            main.showPlayList();
                        }
                        catch (Exception e){
                            //
                        }
                    }
                    else if (code.equals(KeyCode.S) && event.isControlDown())
                        takeSnapshoot();

                    else if(code.equals(KeyCode.L) && event.isControlDown()){
                        loop();
                    }

                    else if(code.equals(KeyCode.R) && event.isControlDown()){
                        randomizePlaylist();
                    }

                    else if(code.equals(KeyCode.LEFT)){
                        mediaPlayer.seek(Duration.seconds(mediaPlayer.getCurrentTime().toSeconds() - 10));
                    }

                    else if(code.equals(KeyCode.RIGHT)){
                        mediaPlayer.seek(Duration.seconds(mediaPlayer.getCurrentTime().toSeconds()+10));
                    }

                    else if(code.equals(KeyCode.SPACE))
                        pause();
                }
                catch (Exception e)
                {
                    System.out.println(" Couldn't execute key order ");
                }

            }
        });
    }


    void updateValues() {
        if (playTime != null && timeSlider != null && volumeSlider != null) {
            Platform.runLater(new Runnable() {
                public void run() {
                    Duration currentTime = mediaPlayer.getCurrentTime();
                    playTime.setText(formatTime(currentTime, duration));
                    timeSlider.setDisable(duration.isUnknown());
                    if (!timeSlider.isDisabled()
                            && duration.greaterThan(Duration.ZERO)
                            && !timeSlider.isValueChanging()) {
                        timeSlider.setValue(currentTime.divide(duration).toMillis()
                                * 100.0);
                    }
                    if (!volumeSlider.isValueChanging()) {
                        volumeSlider.setValue((int) Math.round(mediaPlayer.getVolume()
                                * 100));
                    }
                }
            });
        }
    }

    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int)Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int)Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 -
                    durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds,durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d",elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

}
