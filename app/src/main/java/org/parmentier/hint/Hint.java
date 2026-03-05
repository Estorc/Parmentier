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

package org.parmentier.game.hint;
import java.util.function.Supplier;

class Hint {
    private String hintText; // The text of the hint
    private Integer hintCost; // Cost in points or other in-game currency
    private Integer weight; // Weight for random selection, higher means more likely to be selected
    private Supplier<Boolean> condition; // Condition to determine if the hint is available

    public Hint(String hintText , Integer hintCost, Integer weight, Supplier<Boolean> condition) {
        this.hintCost = hintCost;
        this.weight = weight;
        this.condition = condition;
        this.hintText = hintText;
    }


    public boolean isAvailable() {
        // Logic to check if the hint is available based on the condition
        return condition == null || condition.get();
    }

    public Integer getCost() {
        return hintCost;
    }

    public Integer getWeight() {
        return weight;
    }

    public String getHintText() {
        return hintText;
    }
}
