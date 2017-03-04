import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

/**
 * Created by nafeedgbhs on 12/4/2015.
 */
/**
 * Created by nafeedgbhs on 12/4/2015.
 */
public class AboutController {

    private Main main;

    @FXML
    private Button closeButton;

    @FXML
    void closeButtonAction(ActionEvent event) {
        main.aboutStage.close();
        main.aboutStage = null;
    }

    public void setMain(Main main){
        this.main = main;
    }

}
