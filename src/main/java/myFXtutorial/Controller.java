package main.java.myFXtutorial;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.myFXtutorial.classes.Purchasable;
import main.java.myFXtutorial.classes.PurchaseResult;
import main.java.myFXtutorial.utils.Constants;
import main.java.myFXtutorial.utils.StrFormatter;

import java.util.HashMap;
import java.util.Map;

/**
 * The attribute fx:controller in an FXML file (on the root element) allows a java class to coordinate the behaviours
 * of the objects, usually interface elements (buttons, labels, etc.) defined by the FXML file. This controller class
 * contains methods for handling events such as clicking buttons and updating texts of labels. It queries values and
 * invokes methods from the GameManager accordingly. Its purpose is to coordinate the GUI elements and user events
 * with the logic and mechanics of the game.
 */
public class Controller {

    /**
     * Holds a reference to a GameManager instance to query values and invoke methods according to user input.
     */
    private GameManager gameManager;

    /**
     * This field allows lookup of the integer representing the tier of a building/structure corresponding to a button.
     */
    private Map<Button, Integer> tierMap;

    @FXML private Label coinsLabel;
    @FXML private Label perSecondLabel;

    @FXML private Button t1Button;
    @FXML private Button t2Button;
    @FXML private Button t4Button;
    @FXML private Button t6Button;
    @FXML private Button t5Button;
    @FXML private Button t3Button;
    @FXML private Button t7Button;
    @FXML private Button t8Button;
    @FXML private Button t9Button;
    @FXML private Button t10Button;
    @FXML private Button t11Button;

    @FXML private Label t1CostLabel;
    @FXML private Label t1CountLabel;
    @FXML private Label t1ProductionLabel;

    @FXML private Label t2CostLabel;
    @FXML private Label t2CountLabel;
    @FXML private Label t2ProductionLabel;

    @FXML private Label t3CostLabel;
    @FXML private Label t3CountLabel;
    @FXML private Label t3ProductionLabel;

    @FXML private Label t4CostLabel;
    @FXML private Label t4CountLabel;
    @FXML private Label t4ProductionLabel;

    @FXML private Label t5CostLabel;
    @FXML private Label t5CountLabel;
    @FXML private Label t5ProductionLabel;

    @FXML private Label t6CostLabel;
    @FXML private Label t6CountLabel;
    @FXML private Label t6ProductionLabel;

    @FXML private Label t7CostLabel;
    @FXML private Label t7CountLabel;
    @FXML private Label t7ProductionLabel;

    @FXML private Label t8CostLabel;
    @FXML private Label t8CountLabel;
    @FXML private Label t8ProductionLabel;

    @FXML private Label t9CostLabel;
    @FXML private Label t9CountLabel;
    @FXML private Label t9ProductionLabel;

    @FXML private Label t10CostLabel;
    @FXML private Label t10CountLabel;
    @FXML private Label t10ProductionLabel;

    @FXML private Label t11CostLabel;
    @FXML private Label t11CountLabel;
    @FXML private Label t11ProductionLabel;

    /**
     * This method is called automatically once when the contents of the associated FXML file has been loaded.
     * Initial text of labels and buttons are set here. This occurs before the actual game model has been initialized,
     * so this method does NOT have access to a GameManager.
     */
    @FXML
    private void initialize() {
        coinsLabel.setText("Coins: 0");
        perSecondLabel.setText("0/s");
        // Set button texts
        t1Button.setText(Constants.T1_NAME);
        t2Button.setText(Constants.T2_NAME);
        t3Button.setText(Constants.T3_NAME);
        t4Button.setText(Constants.T4_NAME);
        t5Button.setText(Constants.T5_NAME);
        t6Button.setText(Constants.T6_NAME);
        t7Button.setText(Constants.T7_NAME);
        t8Button.setText(Constants.T8_NAME);
        t9Button.setText(Constants.T9_NAME);
        t10Button.setText(Constants.T10_NAME);
        t11Button.setText(Constants.T11_NAME);

        // Initialize label texts for structures
        t1CostLabel.setText(StrFormatter.fCostLabel(0));
        t1CountLabel.setText(StrFormatter.fCountLabel(0));
        t1ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t2CostLabel.setText(StrFormatter.fCostLabel(0));
        t2CountLabel.setText(StrFormatter.fCountLabel(0));
        t2ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t3CostLabel.setText(StrFormatter.fCostLabel(0));
        t3CountLabel.setText(StrFormatter.fCountLabel(0));
        t3ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t4CostLabel.setText(StrFormatter.fCostLabel(0));
        t4CountLabel.setText(StrFormatter.fCountLabel(0));
        t4ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t5CostLabel.setText(StrFormatter.fCostLabel(0));
        t5CountLabel.setText(StrFormatter.fCountLabel(0));
        t5ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t6CostLabel.setText(StrFormatter.fCostLabel(0));
        t6CountLabel.setText(StrFormatter.fCountLabel(0));
        t6ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t7CostLabel.setText(StrFormatter.fCostLabel(0));
        t7CountLabel.setText(StrFormatter.fCountLabel(0));
        t7ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t8CostLabel.setText(StrFormatter.fCostLabel(0));
        t8CountLabel.setText(StrFormatter.fCountLabel(0));
        t8ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t9CostLabel.setText(StrFormatter.fCostLabel(0));
        t9CountLabel.setText(StrFormatter.fCountLabel(0));
        t9ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t10CostLabel.setText(StrFormatter.fCostLabel(0));
        t10CountLabel.setText(StrFormatter.fCountLabel(0));
        t10ProductionLabel.setText(StrFormatter.fProductionLabel(0));
        t11CostLabel.setText(StrFormatter.fCostLabel(0));
        t11CountLabel.setText(StrFormatter.fCountLabel(0));
        t11ProductionLabel.setText(StrFormatter.fProductionLabel(0));

        tierMap = new HashMap<>();
        tierMap.put(t1Button, 1);
        tierMap.put(t2Button, 2);
        tierMap.put(t3Button, 3);
        tierMap.put(t4Button, 4);
        tierMap.put(t5Button, 5);
        tierMap.put(t6Button, 6);
        tierMap.put(t7Button, 7);
        tierMap.put(t8Button, 8);
        tierMap.put(t9Button, 9);
        tierMap.put(t10Button, 10);
        tierMap.put(t11Button, 11);

    }

    @FXML
    private void increment() {
        gameManager.increment(1);
        coinsLabel.setText("Coins: " + gameManager.getCoins());
    }

    /**
     * Action event handler for purchasing/upgrading a single tier.
     * @param event
     */
    @FXML
    private void handleTierButtonAction(ActionEvent event) {
        //TODO: Make this handle all purchase events
        PurchaseResult result = null;
        Object source;
        source = (Button) event.getSource();
        if (source != null) {
            result = gameManager.onPurchase(Purchasables.TIER, tierMap.get(source));
        }
        if (result == PurchaseResult.OK) {
            updateAll();
        }
    }

    public void updateAll() {
        //TODO: Make this update everything and implement StrFormatter for all labels
        coinsLabel.setText("Coins: " + gameManager.getCoins());
        perSecondLabel.setText(gameManager.getPerSecond() + "/s");

        t1ProductionLabel.setText(gameManager.getPerSecond(1) + "/s");
        t1CountLabel.setText("Level: " + gameManager.getLevelOf(1));
        t1CostLabel.setText("Cost: " + gameManager.getCostOf(1));
    }

    public void setGameManager(GameManager gm) {
        gameManager = gm;
    }
}
