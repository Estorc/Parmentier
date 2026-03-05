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

import org.parmentier.level.Bridge;
import org.parmentier.level.GridData;

public class HintBulb {
    private List<Hint> hints;
    private static HintBulb instance;
    private HintBulb(List<Hint> hints) {
        this.hints = hints;
    }


    static private HintBulb create() {
       return new HintBulb(List.of(
            new Hint("Si une île n’a qu’un seul voisin, alors tous les ponts requis doivent être placés vers ce voisin." +
                "Une fois ces ponts posés, l’île est complétée et peut être marquée comme terminée.", 10, 5, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> node.getBridges().size() == 1) // Only consider islands with one neighbor
                    .anyMatch(node -> {
                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().size(); // Get the number of existing
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
            }),
            new Hint("Une île placée dans un coin ne peut avoir que deux voisins." +
                "Comme un lien ne peut contenir au plus que " + Bridge.MAX_STATE + " ponts, une île " + Bridge.MAX_STATE * 2 + " dans un coin doit envoyer deux ponts vers chacun de ses deux voisins." +
                "L’île est alors complète.", 15, 3, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> grid.isCorner(node) && node.getValue() == Bridge.MAX_STATE * 2) // Only consider islands in corners
                    .anyMatch(node -> {
                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().size(); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),
            new Hint("Une île située sur un bord peut avoir trois voisins." +
                "Une île " + Bridge.MAX_STATE * 3 + " sur un bord doit donc envoyer " + Bridge.MAX_STATE + " ponts vers chacun de ses trois voisins." +
                "L’île est alors complète.", 20, 2, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> grid.isEdge(node) && node.getValue() == Bridge.MAX_STATE * 3) // Only consider islands in corners
                    .anyMatch(node -> {
                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().size(); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),
            new Hint("Une île au centre peut avoir quatre voisins." +
                "Une île " + Bridge.MAX_STATE * 4 + " doit donc envoyer " + Bridge.MAX_STATE + " ponts vers chacun de ses quatre voisins." +
                "L’île est alors complète.", 25, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> grid.isCenter(node) && node.getValue() == Bridge.MAX_STATE * 4) // Only consider islands in corners
                    .anyMatch(node -> {
                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().size(); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                })
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
