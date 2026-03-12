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
import java.util.ArrayList;

import org.parmentier.level.Bridge;
import org.parmentier.level.GridData;
import org.parmentier.level.Node;

public class HintBulb {
    private List<Hint> hints;
    private static HintBulb instance;
    private HintBulb(List<Hint> hints) {
        this.hints = hints;
    }


    static private HintBulb create() {
       return new HintBulb(List.of(
            /*
             * Starting techniques
             */

            new Hint("Une île placée dans un coin ne peut avoir que deux voisins." +
                "Comme un lien ne peut contenir au plus que " + Bridge.MAX_STATE + " ponts, une île " + Bridge.MAX_STATE * 2 + " dans un coin doit envoyer deux ponts vers chacun de ses deux voisins." +
                "L’île est alors complète.", 15, 3, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> grid.isCorner(node) && node.getValue() == Bridge.MAX_STATE * 2) // Only consider islands in corners
                    .anyMatch(node -> {
                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the total number of existing bridges
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
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
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
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            //Special cases of 3 in the corner, 5 on the side and 7 in the middle
            new Hint("Une île " + (Bridge.MAX_STATE * 2 -1) + " placée au bord avec une île 1 comme voisin doit envoyer 1 pont vers le voisin d'indice 1 et" + Bridge.MAX_STATE + "ponts vers l'autre voisin. L’île est alors complète.", 
                30, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> grid.isCorner(node) && node.getValue() == Bridge.MAX_STATE *2-1) // Only consider islands in corners
                    .anyMatch(node -> {
                        boolean hasNeighborWithOne = node.getBridges().stream()
                            .anyMatch(bridge -> {
                                Node from = bridge.getFrom();
                                Node to = bridge.getTo();
                                Node neighbor = (from == node) ? to : from;
                                return neighbor.getValue() == 1;
                            });
                        if (!hasNeighborWithOne) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            new Hint("Une île " + (Bridge.MAX_STATE * 4 - 1) + " placée au centre avec une île 1 comme voisin doit envoyer 1 pont vers le voisin d'indice 1 et " + Bridge.MAX_STATE + " ponts vers ses 3 autres voisins. L’île est alors complète.", 
                35, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> grid.isCenter(node) && node.getValue() == Bridge.MAX_STATE * 4-1) // Only consider islands in corners
                    .anyMatch(node -> {
                        boolean hasNeighborWithOne = node.getBridges().stream()
                            .anyMatch(bridge -> {
                                Node from = bridge.getFrom();
                                Node to = bridge.getTo();
                                Node neighbor = (from == node) ? to : from;
                                return neighbor.getValue() == 1;
                            });
                        if (!hasNeighborWithOne) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            new Hint("Une île " + (Bridge.MAX_STATE * 3 -1) + " placée sur un bord avec une île 1 comme voisin doit envoyer 1 pont vers le voisin d'indice 1 et " + Bridge.MAX_STATE +  " ponts vers ses 2 autres voisins. L’île est alors complète.", 
                40, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> grid.isEdge(node) && node.getValue() == Bridge.MAX_STATE * 3-1) // Only consider islands in corners
                    .anyMatch(node -> {
                        boolean hasNeighborWithOne = node.getBridges().stream()
                            .anyMatch(bridge -> {
                                Node from = bridge.getFrom();
                                Node to = bridge.getTo();
                                Node neighbor = (from == node) ? to : from;
                                return neighbor.getValue() == 1;
                            });
                        if (!hasNeighborWithOne) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            //Special case of 4 on the side
            new Hint("Une île " + Bridge.MAX_STATE * 2 + " placée sur un bord avec 2 îles 1 comme voisins doit envoyer 1 pont sur chaque voisin d'indice 1 et " + Bridge.MAX_STATE + " ponts vers son dernier voisin. L’île est alors complète.", 
                45, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> node.getBridges().size() == 3 && node.getValue() == Bridge.MAX_STATE * 2) 
                    .anyMatch(node -> {
                        long neighborsWithOne = node.getBridges().stream()
                            .filter(b -> {
                                Node neighbor = (b.getFrom() == node) ? b.getTo() : b.getFrom();
                                return neighbor.getValue() == 1;
                            }).count();
                        if (neighborsWithOne != 2) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            //Special case of 6 in the middle
            new Hint("Une île " + Bridge.MAX_STATE * 3 + " au centre avec 4 voisins (dont une île 1) doit envoyer au moins 1 pont sur chaque voisin.", 
                50, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> grid.isCenter(node) && node.getValue() == Bridge.MAX_STATE * 3) 
                    .anyMatch(node -> {
                        boolean hasNeighborWithOne = node.getBridges().stream().anyMatch(bridge -> {
                                Node from = bridge.getFrom();
                                Node to = bridge.getTo();
                                Node neighbor = (from == node) ? to : from;
                                return neighbor.getValue() == 1;
                            });
                        if (!hasNeighborWithOne) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            //Isolation of a two-island segment
            new Hint("Une île " + (Bridge.MAX_STATE -1) + " ne peux pas être relié avec une autre île 1.", 
                55, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> node.getValue() == Bridge.MAX_STATE -1) 
                    .anyMatch(node -> {
                        boolean hasNeighborWithOne = node.getBridges().stream()
                            .anyMatch(bridge -> {
                                Node from = bridge.getFrom();
                                Node to = bridge.getTo();
                                Node neighbor = (from == node) ? to : from;
                                return neighbor.getValue() == 1;
                            });
                        if(!hasNeighborWithOne) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            new Hint("Une île " + Bridge.MAX_STATE + " ne peux pas être relié avec une autre île " + Bridge.MAX_STATE + ".", 
                60, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> node.getValue() == Bridge.MAX_STATE) 
                    .anyMatch(node -> {
                        boolean hasNeighborWithTwo = node.getBridges().stream()
                            .anyMatch(bridge -> {
                                Node from = bridge.getFrom();
                                Node to = bridge.getTo();
                                Node neighbor = (from == node) ? to : from;
                                return neighbor.getValue() == 2;
                            });
                        if(!hasNeighborWithTwo) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            //Isolation of a three-island segment
            new Hint("Une île " + Bridge.MAX_STATE + " avec deux voisins d'indice 1 ne peuvent pas être reliés comme ceci: 1 - 2 - 1.", 
                65, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> node.getBridges().size() == 3 && node.getValue() == Bridge.MAX_STATE) 
                    .anyMatch(node -> {
                        long neighborsWithOne = node.getBridges().stream()
                            .filter(b -> {
                                Node neighbor = (b.getFrom() == node) ? b.getTo() : b.getFrom();
                                return neighbor.getValue() == 1;
                            }).count();
                        if (neighborsWithOne != 2) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            //Isolation when a segment connects to an island
            new Hint("Une île " + Bridge.MAX_STATE + " avec deux voisins d'indice 1 ne peuvent pas être reliés comme ceci: 1 - 2 - 1.", 
                70, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> node.getBridges().size() == 3 && node.getValue() == Bridge.MAX_STATE) 
                    .anyMatch(node -> {
                        long neighborsWithOne = node.getBridges().stream()
                            .filter(b -> {
                                Node neighbor = (b.getFrom() == node) ? b.getTo() : b.getFrom();
                                return neighbor.getValue() == 1;
                            }).count();
                        if (neighborsWithOne != 2) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
                        return existingBridges < requiredBridges; // Check if there are still bridges needed
                    });
                }),

            new Hint("Une île " + (Bridge.MAX_STATE + 1) + " avec un voisin d'indice 1 et un voisin d'indice 2 ne peuvent pas être reliés comme ceci: 1 - 3 - 2 ou 2 - 3 - 1.", 
                75, 1, (grid) -> {
                return grid.getNodes().stream()
                    .filter(node -> node.getBridges().size() == 3 && node.getValue() == Bridge.MAX_STATE) 
                    .anyMatch(node -> {
                        boolean hasNeighborWithOne = node.getBridges().stream()
                            .anyMatch(bridge -> {
                                Node from = bridge.getFrom();
                                Node to = bridge.getTo();
                                Node neighbor = (from == node) ? to : from;
                                return neighbor.getValue() == 1;
                            });

                        boolean hasNeighborWithTwo = node.getBridges().stream()
                            .anyMatch(bridge -> {
                                Node from = bridge.getFrom();
                                Node to = bridge.getTo();
                                Node neighbor = (from == node) ? to : from;
                                return neighbor.getValue() == 2;
                            });
                        if(!hasNeighborWithTwo || !hasNeighborWithOne) return false;

                        int requiredBridges = node.getValue(); // Get the required number of bridges for this island
                        int existingBridges = node.getBridges().stream().reduce(0, (sum, bridge) -> sum + bridge.getState(), Integer::sum); // Get the number of existing bridges
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

        System.out.println("Checking available hints...");
        
        // Calculate the total weight of all available hints
        int totalWeight = hints.stream()
            .filter(hint -> hint.isAvailable(gridData)) // Only consider available hints
            .filter(Hint::isEnabled)
            .mapToInt(Hint::getWeight)
            .sum();
        
        if (totalWeight == 0) {
            return null; // No available hints
        }

        System.out.println("Total weight of available hints: " + totalWeight);

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
