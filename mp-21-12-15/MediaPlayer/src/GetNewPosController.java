/**
 * Created by USER on 12/14/2015.
 */
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.util.Duration;

public class GetNewPosController {
    private Main main;

    @FXML
    private TextField hField;

    @FXML
    private TextField mField;

    @FXML
    private TextField sField;

    @FXML
    void getNewPos(ActionEvent event) {
        try {
            double seekHour = Double.parseDouble(hField.getText());
            double seekMin = Double.parseDouble(mField.getText());
            double seekSec = Double.parseDouble(sField.getText());
            double sec = seekHour * 3600 + seekMin * 60 + seekSec;
            main.seekDuration = new Duration(sec * 1000);
            System.out.println(main.seekDuration);

            if ( main.seekDuration.greaterThanOrEqualTo(new Duration(0))
                    && main.seekDuration.lessThanOrEqualTo(main.mediaPlayerController.getMediaPlayer().getTotalDuration() )  )
            {
                main.mediaPlayerController.getMediaPlayer().seek( main.seekDuration );
            }
            else
            {
                System.out.println("Invalid duration");
            }



        }
        catch (Exception e){
            //System.out.println(e);
        }

        //main.getNewPosStage.close();
        //main.getNewPosStage = null;
    }

    public void setMain(Main main){
        this.main = main;
    }

}
