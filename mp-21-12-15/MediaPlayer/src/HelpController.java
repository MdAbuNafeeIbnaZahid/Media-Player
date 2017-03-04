import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.text.TextFlow;

/**
 * Created by nafeedgbhs on 12/4/2015.
 */
public class HelpController {

    Main main;



    @FXML
    private Button closeButton;


    @FXML
    void closeButtonAction(ActionEvent event) {
        main.helpStage.close();
        main.helpStage = null;
    }

    public void setMain(Main main){this.main = main;}

}
