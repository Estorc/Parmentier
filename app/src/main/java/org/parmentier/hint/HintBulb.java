/** ********************************************************************************
 * Represents a menu in the Parmentier puzzle game, serving as a base class for different
 * types of menus such as the main menu, settings menu, and pause menu.
 ***********************************************************************************
 * @author Estorc
 * @version v1.0
 * @package org.parmentier
 * @copyright Copyright (c) 2026 Parmentier's team GNU GENERAL PUBLIC LICENSE.
 **********************************************************************************/
/*                             This file is part of
 *                                  Parmentier
 *           (https://github.com/Estorc/Projet-Genie-Logiciel-L3-Parmentier)
 ***********************************************************************************/

package org.parmentier.hint;
import java.util.List;
import java.util.Random;
import org.parmentier.level.GridData;

public class HintBulb {
    private List<Hint> hints;
    private static HintBulb instance;
    private HintBulb(List<Hint> hints) {
        this.hints = hints;
    }


    static private HintBulb create() {
       return new HintBulb(List.of(
            new Hint("Try connecting the nodes with the highest values first.", 10, 5, (GridData) -> true),
            new Hint("Look for nodes that are close to each other to create bridges.", 15, 3, (GridData) -> true),
            new Hint("Remember that you can only have a maximum of two bridges between any two nodes.", 20, 2, (GridData) -> true),
            new Hint("Focus on completing one area of the grid before moving on to others.", 25, 1, (GridData) -> true)
        ));
    }

    static public HintBulb get() {
      if (instance == null) {
          instance = create();
      }         
      return instance;
    }

    public void RenableHints() {
        for (Hint hint : hints) {
            hint.setEnabled(true);
        }
    }

    public Hint getRandomHint(GridData gridData) {
        if (hints.isEmpty()) {
            return null; // No hints available
        }
        Random random = new Random();
        
        // Calculate the total weight of all available hints
        int totalWeight = hints.stream()
            .filter(hint -> hint.isAvailable(gridData)) // Only consider available hints
            .filter(Hint::isEnabled)
            .mapToInt(Hint::getWeight)
            .sum();
        
        if (totalWeight == 0) {
            return null; // No available hints
        }

        // Generate a random number between 0 and totalWeight
        int randomWeight = random.nextInt(totalWeight);

        int cumulativeWeight = 0;
        for (Hint hint : hints) {
            if (hint.isAvailable(gridData) && hint.isEnabled()) {
                cumulativeWeight += hint.getWeight();
                if (randomWeight < cumulativeWeight) {
                    hint.setEnabled(false); // Disable the hint after it's selected
                    return hint; // Return the selected hint
                }
            }
        }
        return null; // Fallback, should not reach here
    }
}
