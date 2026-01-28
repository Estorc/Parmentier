package org.parmentier.level;

public class Level {
    private Node[][] levelArray;
    
    public Level(String filename) {
        levelArray = loadLevelFromFile(filename);
    }

    private Node[][] loadLevelFromFile(String filename) {
        try {
            java.nio.file.Path path = java.nio.file.Paths.get(getClass().getClassLoader().getResource(filename).toURI());
            String content = new String(java.nio.file.Files.readAllBytes(path));
            String[] lines = content.split("\n");
            Node[][] array = new Node[lines.length][];
            
            for (int i = 0; i < lines.length; i++) {
                Node lastNode = null;
                for (int j = 0; j < lines[i].length(); j++) {
                    int value = Character.getNumericValue(lines[i].charAt(j));
                    array[i] = array[i] == null ? new Node[lines[i].length()] : array[i];
                    if (value > 0) {
                        array[i][j] = new Node(j, i, value);
                        if (lastNode != null) {
                            lastNode.addBridgeTo(array[i][j]);
                        }
                        lastNode = array[i][j];
                    }
                }
            }

            for (int j = 0; j < lines[0].length(); j++) {
                Node lastNode = null;
                for (int i = 0; i < lines.length; i++) {
                    if (array[i][j] != null) {
                        if (lastNode != null) {
                            lastNode.addBridgeTo(array[i][j]);
                        }
                        lastNode = array[i][j];
                    }
                }
            }

            return array;
        } catch (Exception e) {
            e.printStackTrace();
            return new Node[0][0];
        }
    }

    public int getValueAt(int row, int col) {
        if (row < 0 || row >= levelArray.length || col < 0 || col >= levelArray[0].length) {
            throw new IndexOutOfBoundsException("Invalid row or column index");
        }
        if (levelArray[row][col] == null) {
            throw new IllegalArgumentException("No node at the specified position");
        }
        return levelArray[row][col].getValue();
    }

    public Node getNodeAt(int row, int col) {
        if (row < 0 || row >= levelArray.length || col < 0 || col >= levelArray[0].length) {
            throw new IndexOutOfBoundsException("Invalid row or column index");
        }
        return levelArray[row][col];
    }

    public void printLevel() {
        for (Node[] row : levelArray) {
            for (Node node : row) {
                if (node != null) {
                    System.out.print(node.getValue() + " ");
                    for (Bridge bridge : node.getBridges()) {
                        System.out.print("[" + (bridge.getDirection() == Bridge.Direction.HORIZONTAL ? "H" : "V") + ":" + bridge.getLength() + "] ");
                    }
                }
            }
            System.out.println();
        }
    }

    public int getWidth() {
        return levelArray.length;
    }

    public int getHeight() {
        return levelArray[0].length;
    }
}