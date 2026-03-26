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
import java.util.*;
import java.util.Random;

import org.parmentier.level.Bridge;
import org.parmentier.level.GridData;
import org.parmentier.level.Node;

/**
 * Represents a menu in the Parmentier puzzle game, serving as a base class for different
 * types of menus such as the main menu, settings menu, and pause menu.
 */
public class HintBulb {
    /** 
     * List of hints available in the hint bulb.
     * Each hint is associated with a specific condition that determines when it can be offered to the player
     */
    private List<Hint> hints;

    /** Instance of the HintBulb */
    private static HintBulb instance;

    /** 
     * Constructs a HintBulb with the specified list of hints.
     * @param hints The list of hints to be included in the hint bulb.
     */
    private HintBulb(List<Hint> hints) {
        this.hints = hints;
    }


    /**
     * Creates and initializes the HintBulb with a predefined set of hints.
     * Each hint is associated with a specific condition that determines when it can be offered to the player.
     * @return A new instance of HintBulb initialized with the predefined hints.
     */
    static private HintBulb create() {
       return new HintBulb(List.of(
            /*
             * Starting techniques
             */

            new Hint("Une île placée dans un coin ne peut avoir que deux voisins." +
                "Comme un lien ne peut contenir au plus que " + Bridge.MAX_STATE + " ponts, une île " + Bridge.MAX_STATE * 2 + " dans un coin doit envoyer deux ponts vers chacun de ses deux voisins." +
 b               "L’île est alors complète.", 15, 3, (grid) -> {
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
                }),

            //Isolation when a segment connects to an island
            new Hint("Un segment d'îles presque complet ne peut pas connecter son dernier pont vers une île si cela isole le segment du reste de la grille.", 
                80, 6, (grid) -> {
                List<Node> allNodes = grid.getNodes();
                int totalNodes = allNodes.size();
                if (totalNodes <= 2) return false;

                // BFS
                Map<Node, Set<Node>> componentOf = new HashMap<>();
                List<Set<Node>> components = new ArrayList<>();

                for (Node start : allNodes) {
                    if (!componentOf.containsKey(start)){
                        Set<Node> component = new HashSet<>();
                        Queue<Node> queue = new LinkedList<>();
                        queue.add(start);
                        while (!queue.isEmpty()) {
                            Node cur = queue.poll();
                            if (!component.contains(cur)){
                                component.add(cur);
                                for (Bridge b : cur.getBridges()) {
                                    if (b.getState() > 0) {
                                        Node next = (b.getFrom() == cur) ? b.getTo() : b.getFrom();
                                        if (!component.contains(next)) queue.add(next);
                                    }
                                }
                            }
                        }
                        for (Node n : component) componentOf.put(n, component);
                        components.add(component);
                    }
                }

                // For each component, verify if there is exactly 1 brige missing
                for (Set<Node> segment : components) {
                    long segmentMissing = segment.stream()
                        .mapToLong(n -> n.getValue() - n.getBridges().stream().mapToInt(Bridge::getState).sum())
                        .sum();
                    if (segmentMissing != 1) continue;

                    for (Node n : segment) {
                        int remaining = n.getValue() - n.getBridges().stream().mapToInt(Bridge::getState).sum();
                        if (remaining != 1) continue;

                        for (Bridge b : n.getBridges()) {
                            if (b.getState() <= 0){
                                Node other = (b.getFrom() == n) ? b.getTo() : b.getFrom();
                                Set<Node> otherComponent = componentOf.get(other);

                                if (otherComponent != segment){
                                    int otherRemaining = other.getValue() - other.getBridges().stream().mapToInt(Bridge::getState).sum();
                                    if (otherRemaining > 0){
                                        long otherMissing = otherComponent.stream()
                                            .mapToLong(on -> on.getValue() - on.getBridges().stream().mapToInt(Bridge::getState).sum())
                                            .sum();

                                        if (otherMissing == 1 && segment.size() + otherComponent.size() < totalNodes)return true;
                                    }
                                }
                            }
                        }
                    }
                }
                return false;
            }),

            // Isolation when a segment connects to another segment
            new Hint("Deux segments d'îles presque complets ne peuvent pas se connecter entre eux avec leurs deux ponts manquants si cela isolerait le groupe du reste de la grille.",
                85, 5, (grid) -> {
                List<Node> allNodes = grid.getNodes();
                int totalNodes = allNodes.size();
                if (totalNodes <= 2) return false;

                // BFS
                Map<Node, Set<Node>> componentOf = new HashMap<>();
                List<Set<Node>> components = new ArrayList<>();

                for (Node start : allNodes) {
                    if (!componentOf.containsKey(start)){
                        Set<Node> component = new HashSet<>();
                        Queue<Node> queue = new LinkedList<>();
                        queue.add(start);
                        while (!queue.isEmpty()) {
                            Node cur = queue.poll();
                            if (!component.contains(cur)){
                                component.add(cur);
                                for (Bridge b : cur.getBridges()) {
                                    if (b.getState() > 0) {
                                        Node next = (b.getFrom() == cur) ? b.getTo() : b.getFrom();
                                        if (!component.contains(next)) queue.add(next);
                                    }
                                }
                            }
                        }
                        for (Node n : component) componentOf.put(n, component);
                        components.add(component);
                    }
                }

                //For each pair of segments, each having exactly 2 missing bridges
                for (Set<Node> segmentA : components) {
                    long missingA = segmentA.stream()
                        .mapToLong(n -> n.getValue() - n.getBridges().stream().mapToInt(Bridge::getState).sum())
                        .sum();
                    if (missingA == 2){

                        for (Node nodeA : segmentA) {
                            int remainingA = nodeA.getValue() - nodeA.getBridges().stream().mapToInt(Bridge::getState).sum();
                            if (remainingA >= 2){
                                for (Bridge b : nodeA.getBridges()) {
                                    if (b.getState() < Bridge.MAX_STATE){
                                        Node nodeB = (b.getFrom() == nodeA) ? b.getTo() : b.getFrom();
                                        Set<Node> segmentB = componentOf.get(nodeB);
                                        if (segmentB != segmentA){
                                            long missingB = segmentB.stream()
                                                .mapToLong(n -> n.getValue() - n.getBridges().stream().mapToInt(Bridge::getState).sum())
                                                .sum();
                                            if (missingB == 2){
                                                int remainingB = nodeB.getValue() - nodeB.getBridges().stream().mapToInt(Bridge::getState).sum();
                                                if (remainingB >= 2){
                                                    if (segmentA.size() + segmentB.size() < totalNodes)return true;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                return false;
            })

        ));
    }

    /**
     * Gets the singleton instance of the HintBulb.
     * If the instance does not exist, it is created using the create() method.
     * @return The singleton instance of the HintBulb.
     */
    static public HintBulb get() {
      if (instance == null) {
          instance = create();
      }         
      return instance;
    }

    /**
     * Re-enables all hints in the hint bulb, allowing them to be offered to the player again.
     * This method can be called to reset the state of the hints after they have been used or disabled.
     */
    public void RenableHints() {
        for (Hint hint : hints) {
            hint.setEnabled(true);
        }
    }

    /**
     * Gets a random hint from the hint bulb based on the current grid data and the availability of the hints.
     * The method calculates the total weight of all available hints and selects one randomly based on their weights.
     * Once a hint is selected, it is disabled to prevent it from being offered again until the hints are re-enabled.
     * @param gridData The current grid data of the puzzle, which is used to evaluate the availability of the hints based on their conditions.
     * @return A randomly selected hint that is available based on the current grid data, or null if no hints are available.
     */
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
                    hint.useHint(); // Disable the hint after it's selected
                    return hint; // Return the selected hint
                }
            }
        }
        return null; // Fallback, should not reach here
    }
}
