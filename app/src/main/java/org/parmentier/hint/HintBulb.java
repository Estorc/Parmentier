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

package org.parmentier.game.hint;
import java.util.List;
import java.util.Random;

class HintBulb {
    private List<Hint> hints;

    public Hint getRandomHint() {
        if (hints.isEmpty()) {
            return null; // No hints available
        }
        Random random = new Random();
        int index = random.nextInt(hints.size());
        return hints.get(index);
    }
}
