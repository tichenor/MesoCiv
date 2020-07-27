package main.java.myFXtutorial;

import javafx.animation.SequentialTransition;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import main.java.myFXtutorial.classes.Modifier;
import main.java.myFXtutorial.classes.PurchaseResult;
import main.java.myFXtutorial.gui.RadioBox;
import main.java.myFXtutorial.utils.Assets;
import main.java.myFXtutorial.utils.Constants;
import main.java.myFXtutorial.utils.NumFormatter;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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

    /**
     * For looking up what upgrade a button represents.
     */
    private Map<Button, Modifier> upgradeButtonMap;

    /**
     * For looking up what achievement (using its name) an image represents. Images represent unlocked achievements
     * under the 'Achievements' tab.
     */
    private Map<ImageView, String> achievementIconMap;

    /**
     * Displays various information and notifications to the user.
     */
    private RadioBox radioBox;

    /**
     * Labels for displaying number of coins owned and estimated production of coins per second.
     */
    @FXML private Label coinsLabel;
    @FXML private Label perSecondLabel;

    /**
     * Container for displaying various tooltip information such as name, cost and effect of an upgrade when hovering
     * over it.
     */
    @FXML private VBox tooltipBox;
    // Labels for displaying tooltip information
    @FXML private Label tooltipTitle;
    @FXML private Label tooltipDescription;
    @FXML private Label tooltipEffect;
    @FXML private Label tooltipCost;

    /**
     * Container for displaying purchasable upgrades as they become available.
     */
    @FXML private TilePane upgradePanel;

    @FXML private TilePane achievementsPanel;

    /**
     * Container for displaying various text information about the game and game events, such as unlocking a new
     * achievement, reaching a milestone or fluff. This container is wrapped in the RadioBox class for customized
     * control.
     */
    @FXML private VBox radioBoxContainer;

    /**
     * Buttons for purchasing/leveling up buildings/structures/tiers.
     */
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

    /**
     * Labels for displaying cost, number owned/level, and production per second of buildings/tiers.
     */
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
     * Formatters for displaying numbers properly, such as cost, production per second, and so on.
     */
    private NumFormatter numFormatter;
    private DecimalFormat upgradeNumFormatter;


    /**
     * This method is called automatically once when the contents of the associated FXML file has been loaded.
     * Initial text of labels and buttons are set here. This occurs before the actual game model has been initialized,
     * so this method does NOT have access to a GameManager.
     */
    @FXML
    private void initialize() {
        // Coin info labels
        coinsLabel.setText("Coins: 0");
        perSecondLabel.setText("0/s");

        // Tooltip area labels
        clearTooltip();

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

        upgradeButtonMap = new HashMap<>();
        achievementIconMap = new HashMap<>();
        radioBox = new RadioBox(radioBoxContainer);

    }

    /**
     * Intended to be run once _after_ the game manager has been instantiated.
     */
    public void postInitialize() {
        // A helpful number formatter
        numFormatter = new NumFormatter.Builder()
                .groupDigits()
                .showHighestThousand()
                .showDecimals(2)
                .useAbbreviations(Constants.NUM_ABBREVIATIONS)
                .build();

        // Number formatting for number on upgrade buttons
        DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.ENGLISH);
        upgradeNumFormatter = new DecimalFormat("0.00", symbols);

        // Set the tooltip display labels to wrap text properly around the tooltip box
        tooltipTitle.wrapTextProperty().bind(tooltipBox.fillWidthProperty());
        tooltipDescription.wrapTextProperty().bind(tooltipBox.fillWidthProperty());
        tooltipEffect.wrapTextProperty().bind(tooltipBox.fillWidthProperty());
        tooltipCost.wrapTextProperty().bind(tooltipBox.fillWidthProperty());
    }

    @FXML
    private void increment() {
        gameManager.onClick();
        updateAll();
    }

    /**
     * Action event handler for purchasing/upgrading a single tier.
     * @param event
     */
    @FXML
    private void handleTierButtonAction(ActionEvent event) {
        //TODO: Make this handle all purchase events
        PurchaseResult result = null;
        Button source = null;
        source = (Button) event.getSource();
        if (source != null) {
            result = gameManager.onPurchase(Purchasables.TIER, tierMap.get(source));
        }
        if (result == PurchaseResult.OK) {
            updateAll();
            checkForAvailableTierUpgrades(tierMap.get(source));
        }
    }

    /**
     * Display a temporary message to the user.
     * @param msg A string message.
     */
    public void transmitMessage(String msg) {
        Label label = new Label(msg);
        label.setStyle("-fx-text-fill: black; -fx-padding: 10px;");
        SequentialTransition sequence = radioBox.createTransition(label, 5, 5, 1, 0);
        radioBox.submit(label, sequence);
    }

    /**
     * Should only be assigned to tier buttons in the FXML file.
     * @param event
     */
    @FXML
    private void showTierInfo(MouseEvent event) {
        Button source = null;
        source = (Button) event.getSource();
        if (source != null) {
            tooltipTitle.setText(Assets.tierName(tierMap.get(source)));
            tooltipDescription.setText(Assets.tierDescription(tierMap.get(source)));
        }
    }

    @FXML
    private void clearTooltip(MouseEvent event) {
        clearTooltip();
    }

    /**
     * Dynamically generate upgrade buttons whenever an upgrade becomes available.
     * @param tier
     */
    private void checkForAvailableTierUpgrades(int tier) {
        List<Modifier> tierUpgrades = gameManager.getAvailableUpgradesFor(tier);
        generateUpgradeButtons(tierUpgrades);
    }

    private void checkForAvailableGlobalUpgrades() {
        List<Modifier> globalUpgrades = gameManager.getAvailableGlobalUpgrades();
        generateUpgradeButtons(globalUpgrades);
    }

    private void generateUpgradeButtons(List<Modifier> modifiers) {

        for (Modifier m : modifiers) {

            if (upgradeButtonMap.containsValue(m))
                continue; // Don't want to add more than one button per upgrade

            // Make upgrade button corresponding to the available modifier
            Button upgButton = new Button(m.getName());

            // Set on action (click), mouse entered & mouse exited events (display description)
            upgButton.setOnAction(upgButtonHandler());
            upgButton.setOnMouseEntered(upgButtonMouseEntered());
            upgButton.setOnMouseExited(upgButtonMouseExited());

            // Add style and attach button to the upgrade panel
            upgButton.getStyleClass().add("upgrade-button");
            upgButton.setPrefSize(117, 80);
            upgradePanel.getChildren().add(upgButton);

            // Track which upgrade (modifier) a button represents
            upgradeButtonMap.put(upgButton, m);
            transmitMessage(m.getName() + " is now available.");
        }
    }

    /**
     * Generate an on-action event (clicking a button) for upgrade buttons generated dynamically.
     * @return
     */
    private EventHandler<ActionEvent> upgButtonHandler() {
        return new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                PurchaseResult result = null;
                Button source = null;
                source = (Button) event.getSource();
                if (source != null) {
                    result = gameManager.onUpgrade(upgradeButtonMap.get(source));
                }
                if (result == PurchaseResult.OK) {
                    updateAll();
                    upgradePanel.getChildren().remove(source);
                }
            }
        };
    }

    /**
     * Dynamic version of showTooltip (create an on-mouse-entered event) for dynamically generated buttons.
     * @return
     */
    private EventHandler<MouseEvent> upgButtonMouseEntered() {
        return mouseEvent -> {
            Button source;
            source = (Button) mouseEvent.getSource();
            if (source != null) {
                Modifier upgrade = upgradeButtonMap.get(source);
                if (upgrade != null) {
                    if (upgrade.getTarget() != null) { // Upgrade is targeting a particular generator (building)
                        tooltipTitle.setText(upgrade.getName() + "\n(" + upgrade.getTarget().getName() + " technology)");
                    } else { // Upgrade is "global" or other
                        tooltipTitle.setText(upgrade.getName() + "\n(" + upgrade.getType() + ")");
                    }
                    tooltipDescription.setText(upgrade.getDescription());
                    tooltipEffect.setText("Production bonus: x" + upgradeNumFormatter.format(upgrade.getMultiplier()));
                    tooltipCost.setText("Cost: " + numFormatter.format(upgrade.getBaseCost()));
                }
            }
        };
    }

    /**
     * Dynamic version of clearTooltip (when mouse exits, clear the text).
     * @return
     */
    private EventHandler<MouseEvent> upgButtonMouseExited() {
        return mouseEvent -> clearTooltip();
    }

    private EventHandler<MouseEvent> achievementIconMouseEntered() {
        return mouseEvent -> {
            ImageView source;
            source = (ImageView) mouseEvent.getSource();
            if (source != null) {
                String achievementName = achievementIconMap.get(source);
                if (achievementName != null) {
                    tooltipTitle.setText(achievementName);
                    Assets.AchievementData ad = Assets.achievements.get(achievementName);
                    if (ad != null) {
                        tooltipDescription.setText(ad.getDescription());
                        tooltipEffect.setText(getAchievementEffect(ad));
                    }
                }
            }
        };
    }

    private EventHandler<MouseEvent> achievementIconMouseExited() {
        return mouseEvent -> clearTooltip();
    }

    private String getAchievementEffect(Assets.AchievementData ad) {
        String effect;
        switch (ad.getType()) {
            case "bonusClick" ->
                    effect = "(+" + ad.getBonusValue() + " coin(s) per click).";
            case "bonusGlobalMultiplier" ->
                    effect = "(x" + upgradeNumFormatter.format(ad.getBonusValue()) + " to all production).";
            default -> effect = "No effect.";
        }
        return effect;
    }

    private void clearTooltip() {
        tooltipTitle.setText("");
        tooltipDescription.setText("");
        tooltipEffect.setText("");
        tooltipCost.setText("");
    }

    public void updateAll() {
        //TODO: Make this update everything and implement StrFormatter for all labels
        coinsLabel.setText("Coins: " + numFormatter.format(gameManager.getCoins()));
        perSecondLabel.setText(numFormatter.format(gameManager.getPerSecond()) + "/s");

        t1ProductionLabel.setText(productionLabel(1));
        t1CountLabel.setText(countLabel(1));
        t1CostLabel.setText(costLabel(1));

        t2ProductionLabel.setText(productionLabel(2));
        t2CountLabel.setText(countLabel(2));
        t2CostLabel.setText(costLabel(2));

        t3ProductionLabel.setText(productionLabel(3));
        t3CountLabel.setText(countLabel(3));
        t3CostLabel.setText(costLabel(3));

        t4ProductionLabel.setText(productionLabel(4));
        t4CountLabel.setText(countLabel(4));
        t4CostLabel.setText(costLabel(4));

        t5ProductionLabel.setText(productionLabel(5));
        t5CountLabel.setText(countLabel(5));
        t5CostLabel.setText(costLabel(5));

        t6ProductionLabel.setText(productionLabel(6));
        t6CountLabel.setText(countLabel(6));
        t6CostLabel.setText(costLabel(6));

        t7ProductionLabel.setText(productionLabel(7));
        t7CountLabel.setText(countLabel(7));
        t7CostLabel.setText(costLabel(7));

        t8ProductionLabel.setText(productionLabel(8));
        t8CountLabel.setText(countLabel(8));
        t8CostLabel.setText(costLabel(8));

        t9ProductionLabel.setText(productionLabel(9));
        t9CountLabel.setText(countLabel(9));
        t9CostLabel.setText(costLabel(9));

        t10ProductionLabel.setText(productionLabel(10));
        t10CountLabel.setText(countLabel(10));
        t10CostLabel.setText(costLabel(10));

        t11ProductionLabel.setText(productionLabel(11));
        t11CountLabel.setText(countLabel(11));
        t11CostLabel.setText(costLabel(11));

        checkForAvailableGlobalUpgrades();

        List<ProgressManager.Achievement> unlockedAchievements = gameManager.getUnlockedAchievements();
        for (ProgressManager.Achievement a : unlockedAchievements) {
            onUnlockedAchievement(a);
        }

    }

    private void onUnlockedAchievement(ProgressManager.Achievement a) {
        String message = "New achievement: " + a.getName();
        Assets.AchievementData ad = Assets.achievements.get(a.getName());
        transmitMessage(message);
        ImageView iv = new ImageView();
        iv.setImage(Assets.test);
        iv.setFitWidth(100);
        iv.setFitHeight(80);
        iv.setOnMouseEntered(achievementIconMouseEntered());
        iv.setOnMouseExited(achievementIconMouseExited());
        achievementIconMap.put(iv, a.getName());
        achievementsPanel.getChildren().add(iv);
    }

    private String productionLabel(int tier) {
        String perSecond = numFormatter.format(gameManager.getPerSecond(tier));
        String perLevel = numFormatter.format(gameManager.getPerSecondPerLevel(tier));
        return perSecond + "/s (+" + perLevel + "/s)";
    }

    private String countLabel(int tier) {
        return "Owned: " + gameManager.getLevelOf(tier);
    }

    private String costLabel(int tier) {
        return "Cost: " + numFormatter.format(gameManager.getCostOf(tier)) + " coins";
    }

    public void setGameManager(GameManager gm) {
        gameManager = gm;
    }
}
