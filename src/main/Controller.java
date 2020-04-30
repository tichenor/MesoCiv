package main;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import utils.Constants;
import utils.Formatter;

public class Controller {

    private int t1Count = 0, t2Count = 0, t3Count = 0,
            t4Count = 0, t5Count = 0, t6Count = 0, t7Count = 0,
            t8Count = 0, t9Count = 0, t10Count = 0, t11Count = 0;

    private int coins = 0;

    @FXML private Label coinsLabel;

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


    @FXML
    private void initialize() {
        coinsLabel.setText("Coins: " + coins);

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
        t1CostLabel.setText(Formatter.fCostLabel(0));
        t1CountLabel.setText(Formatter.fCountLabel(0));
        t1ProductionLabel.setText(Formatter.fProductionLabel(0));
        t2CostLabel.setText(Formatter.fCostLabel(0));
        t2CountLabel.setText(Formatter.fCountLabel(0));
        t2ProductionLabel.setText(Formatter.fProductionLabel(0));
        t3CostLabel.setText(Formatter.fCostLabel(0));
        t3CountLabel.setText(Formatter.fCountLabel(0));
        t3ProductionLabel.setText(Formatter.fProductionLabel(0));
        t4CostLabel.setText(Formatter.fCostLabel(0));
        t4CountLabel.setText(Formatter.fCountLabel(0));
        t4ProductionLabel.setText(Formatter.fProductionLabel(0));
        t5CostLabel.setText(Formatter.fCostLabel(0));
        t5CountLabel.setText(Formatter.fCountLabel(0));
        t5ProductionLabel.setText(Formatter.fProductionLabel(0));
        t6CostLabel.setText(Formatter.fCostLabel(0));
        t6CountLabel.setText(Formatter.fCountLabel(0));
        t6ProductionLabel.setText(Formatter.fProductionLabel(0));
        t7CostLabel.setText(Formatter.fCostLabel(0));
        t7CountLabel.setText(Formatter.fCountLabel(0));
        t7ProductionLabel.setText(Formatter.fProductionLabel(0));
        t8CostLabel.setText(Formatter.fCostLabel(0));
        t8CountLabel.setText(Formatter.fCountLabel(0));
        t8ProductionLabel.setText(Formatter.fProductionLabel(0));
        t9CostLabel.setText(Formatter.fCostLabel(0));
        t9CountLabel.setText(Formatter.fCountLabel(0));
        t9ProductionLabel.setText(Formatter.fProductionLabel(0));
        t10CostLabel.setText(Formatter.fCostLabel(0));
        t10CountLabel.setText(Formatter.fCountLabel(0));
        t10ProductionLabel.setText(Formatter.fProductionLabel(0));
        t11CostLabel.setText(Formatter.fCostLabel(0));
        t11CountLabel.setText(Formatter.fCountLabel(0));
        t11ProductionLabel.setText(Formatter.fProductionLabel(0));

    }

    @FXML
    private void increment() {
        coins++;
        coinsLabel.setText("Coins: " + coins);
    }




}
