/** ********************************************************************************
 * represents a menu in the parmentier puzzle game, serving as a base class for different
 * types of menus such as the main menu, settings menu, and pause menu.
 ***********************************************************************************
 * @author estorc
 * @version v1.0
 * @package org.parmentier
 * @copyright copyright (c) 2026 parmentier's team gnu general public license.
 **********************************************************************************/
/*                             this file is part of
 *                                  parmentier
 *           (https://github.com/estorc/projet-genie-logiciel-l3-parmentier)
 ***********************************************************************************/

package org.parmentier.hint;
import org.parmentier.level.GridData;
import java.util.function.Function;


/**
 * Represents a hint in the Parmentier puzzle game, providing players with clues or assistance to solve the puzzles.
 * Each hint has associated text, a cost, a weight for random selection, and a condition that determines its availability.
 */
public class Hint {
    /** The text of the hint */
    private String hintText;
    /** The brief text of the hint */
    private String hintBriefText;
    /** Cost in points or other in-game currency */
    private Integer hintCost; 
    /** Weight for random selection, higher means more likely to be selected */
    private Integer weight; 
    /** Indicates whether the hint is currently enabled and can be used by the player */
    private int remainingUses;
    /** Condition to determine if the hint is available */
    private Function<GridData, Boolean> condition; 

    /**
     * Constructs a Hint with the specified text, cost, weight, and condition.
     * @param briefHintText The brief text of the hint, showed at the first use of the hint.
     * @param hintText The text of the hint to be displayed to the player.
     * @param hintCost The cost of using the hint, which may be deducted from the player's score or in-game currency.
     * @param weight The weight for random selection, where a higher weight increases the likelihood of this hint
     * being selected when multiple hints are available.
     * @param condition A function that takes the current grid data as input and returns a boolean indicating whether
     * the hint is available for use based on the current state of the puzzle.
     */
    public Hint(String briefHintText, String hintText, Integer hintCost, Integer weight, Function<GridData, Boolean> condition) {
        this.hintCost = hintCost;
        this.weight = weight;
        this.condition = condition;
        this.hintBriefText = briefHintText;
        this.hintText = hintText;
        this.remainingUses = 2; // By default, hints are enabled
    }

    /**
     * Checks if the hint is available based on the provided grid data and the condition function.
     * @param grid The current grid data of the puzzle, which is used to evaluate the condition for the hint's availability.
     * @return true if the hint is available (i.e., the condition is null or returns true), false otherwise.
     */
    public boolean isAvailable(GridData grid) {
        // Logic to check if the hint is available based on the condition
        return condition == null || condition.apply(grid);
    }

    /**
     * Sets the enabled state of the hint.
     * @param enabled A boolean value indicating whether the hint should be enabled (true) or disabled (false).
     */
    public void setEnabled(Boolean enabled) {
        this.remainingUses = enabled ? 2 : 0; // Reset remaining uses when enabled, set to 0 when disabled
    }

    /**
     * Uses the hint, reducing the remaining uses by one.
     * If the hint is used up (remaining uses reaches 0), it will be disabled.
     */
    public void useHint() {
        if (remainingUses > 0) {
            remainingUses--;
        }
    }

    /**
     * Checks if the hint is currently enabled.
     * @return true if the hint is enabled, false otherwise.
     */
    public Boolean isEnabled() {
        return remainingUses > 0;
    }

    /**
     * Gets the cost of using the hint.
     * @return The cost of the hint, which may be used to determine how much to deduct from the player's score
     * or in-game currency when the hint is used.
     */
    public Integer getCost() {
        return hintCost;
    }

    /**
     * Gets the weight of the hint for random selection.
     * @return The weight of the hint, which influences the likelihood of this hint being selected when multiple hints are available.
     * A higher weight means a higher chance of selection.
     */
    public Integer getWeight() {
        return weight;
    }

    /**
     * Gets the text of the hint to be displayed to the player.
     * @return The text of the hint, which provides clues or assistance to the player in solving the puzzle.
     */
    public String getHintText() {
        return remainingUses == 1 ? hintBriefText : hintText; // Return full hint text if it's the first use, otherwise return brief text
    }
}
