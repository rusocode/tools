package astar_pathfinding;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * El algoritmo A* evalua los costos y encuentra la ruta mas corta hacia el objetivo.
 * <p>
 * Asumiendo que el npc solo se mueve en 4 direcciones, el algoritmo evalua desde el nodo inicial los 4 nodos
 * adyacentes y calcula el costo mas bajo (F o G en caso de que F sea igual) para establecerlo como verificado. Este
 * proceso se repite verificando los nodos adyacentes con menor costo a los nodos padres (parent) hasta llegar al
 * objetivo.
 * <p>
 * En caso de que el algoritmo choque con nodos solidos, explorara rutas alternativas a traves de los nodos del
 * conjunto abierto. Es decir que buscara el proximo nodo con menor costo entre los nodos adyacentes verificados. Si
 * no encuentra una ruta alternativa, quiere decir que esta encerrado en nodos solidos, por lo tanto ya no hay nodos
 * abiertos por explorar y es necesario romper el bucle para evitar un IndexOutOfBoundsException.
 */

public class AStar extends JPanel {

    // Screen settings
    final int maxRow = 10;
    final int maxCol = 15;
    final int nodeSize = 70;
    final int screenWidth = nodeSize * maxCol;
    final int screenHeight = nodeSize * maxRow;

    // Node
    Node[][] node;
    Node startNode, goalNode, currentNode;
    // Los nodos abiertos representan los nodos a comparar
    ArrayList<Node> openList = new ArrayList<>();

    // Others
    boolean goalReached;
    /* Cuenta los pasos en cada ciclo hasta un limite para evitar que se congele el juego en caso de que no encuentre la
     * ruta por un determinado tiempo. */
    int step;

    public AStar() {
        setPreferredSize(new Dimension(screenWidth, screenHeight));
        setBackground(Color.black);
        setLayout(new GridLayout(maxRow, maxCol));
        addKeyListener(new KeyHandler(this));
        setFocusable(true);

        node = new Node[maxRow][maxCol];

        // Crea los nodos y los agrega al panel
        for (int row = 0; row < maxRow; row++) {
            for (int col = 0; col < maxCol; col++) {
                node[row][col] = new Node(row, col);
                add(node[row][col]);
            }
        }

        // Establece el nodo de inicio y el nodo objetivo
        setStartNode(6, 3);
        setGoalNode(3, 11);

        openPath();
        // closePath();

        // Establece los costos a cada nodo
        setCostOnNodes();

    }

    private void setStartNode(int row, int col) {
        node[row][col].setAsStart();
        startNode = node[row][col];
        currentNode = startNode;
        openList.add(currentNode);
    }

    private void setGoalNode(int row, int col) {
        node[row][col].setAsGoal();
        goalNode = node[row][col];
    }

    private void setSolidNode(int row, int col) {
        node[row][col].setAsSolid();
    }

    /**
     * Abre la ruta con nodos solidos.
     */
    private void openPath() {
        setSolidNode(2, 10);
        setSolidNode(3, 10);
        setSolidNode(4, 10);
        setSolidNode(5, 10);
        setSolidNode(6, 10);
        setSolidNode(7, 10);
        setSolidNode(2, 6);
        setSolidNode(2, 7);
        setSolidNode(2, 8);
        setSolidNode(2, 9);
        setSolidNode(7, 11);
        setSolidNode(7, 12);
        setSolidNode(1, 6);
    }

    /**
     * Cierra la ruta con nodos solidos.
     */
    private void closePath() {
        setSolidNode(4, 1);
        setSolidNode(4, 2);
        setSolidNode(4, 3);
        setSolidNode(4, 4);
        setSolidNode(5, 1);
        setSolidNode(6, 1);
        setSolidNode(7, 1);
        setSolidNode(8, 1);
        setSolidNode(8, 2);
        setSolidNode(8, 3);
        setSolidNode(8, 4);
        setSolidNode(8, 5);
        setSolidNode(7, 5);
        setSolidNode(6, 5);
        setSolidNode(5, 5);
        setSolidNode(4, 5);
    }

    private void setCostOnNodes() {
        for (int row = 0; row < maxRow; row++)
            for (int col = 0; col < maxCol; col++)
                getCost(node[row][col]);
    }

    private void getCost(Node node) {
        // Obtiene el G Cost (distancia desde el nodo inicio)
        int xDistance = Math.abs(node.col - startNode.col);
        int yDistance = Math.abs(node.row - startNode.row);
        node.gCost = xDistance + yDistance;

        // Obtiene el H Cost (distancia desde el nodo objetivo)
        xDistance = Math.abs(node.col - goalNode.col);
        yDistance = Math.abs(node.row - goalNode.row);
        node.hCost = xDistance + yDistance;

        // Obtiene el F Cost (el costo total)
        node.fCost = node.gCost + node.hCost;

        // Muestra el costo en cada nodo
        if (node != startNode && node != goalNode)
            node.setText("<html>F: " + node.fCost + "<br>G: " + node.gCost + "</html>");

    }

    /**
     * Este metodo solo funciona con una ruta abierta.
     */
    public void search() {
        if (!goalReached) {

            currentNode.setAsChecked();
            openList.remove(currentNode);

            int row = currentNode.row;
            int col = currentNode.col;
            if (row - 1 >= 0) openNode(node[row - 1][col]);
            if (col - 1 >= 0) openNode(node[row][col - 1]);
            if (row + 1 < maxRow) openNode(node[row + 1][col]);
            if (col + 1 < maxCol) openNode(node[row][col + 1]);

            int bestNodeIndex = 0;
            int bestNodefCost = 999;

            for (int i = 0; i < openList.size(); i++) {
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                } else if (openList.get(i).fCost == bestNodefCost)
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) bestNodeIndex = i;
            }

            currentNode = openList.get(bestNodeIndex);

            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }

        }
    }

    public void autoSearch() {
        // Mientras el algoritmo no haya llegado al objetivo y la cantidad de pasos no supere el limite
        while (!goalReached && step < 300) {

            /* Establece el nodo actual como verificado y lo elimina de la lista de nodos abiertos. Esto significa que
             * ya fue verificado como mejor candidato para la ruta, por lo tanto ya no es necesario evaluarlo. */
            currentNode.setAsChecked();
            openList.remove(currentNode);

            // Obtiene la fila y columna del nodo actual
            int row = currentNode.row;
            int col = currentNode.col;
            // Abre los nodos adyacentes al actual siempre y cuando se eviten abrir los nodos ubicados a los extremos de la grilla
            if (row - 1 >= 0) openNode(node[row - 1][col]);
            if (col - 1 >= 0) openNode(node[row][col - 1]);
            if (row + 1 < maxRow) openNode(node[row + 1][col]);
            if (col + 1 < maxCol) openNode(node[row][col + 1]);

            // Encuentra el mejor nodo
            int bestNodeIndex = 0;
            int bestNodefCost = 999;
            // Compara los nodos de la lista abierta y verifica que nodo tiene el mejor F Cost o G Cost
            for (int i = 0; i < openList.size(); i++) {
                // Verifica si el F Cost es mas bajo que el F Cost del mejor nodo actual
                if (openList.get(i).fCost < bestNodefCost) {
                    bestNodeIndex = i;
                    bestNodefCost = openList.get(i).fCost;
                }
                // Si el F Cost es igual, verifica el G Cost
                else if (openList.get(i).fCost == bestNodefCost)
                    if (openList.get(i).gCost < openList.get(bestNodeIndex).gCost) bestNodeIndex = i;
            }

            // Si ya no hay nodos abiertos por explorar, entonces rompe el bucle para evitar un IndexOutOfBoundsException
            if (openList.isEmpty()) break;

            // Obtiene el siguiente mejor nodo
            currentNode = openList.get(bestNodeIndex);

            // Si el mejor nodo es igual al objetivo
            if (currentNode == goalNode) {
                goalReached = true;
                trackThePath();
            }

            step++;
        }
    }

    /**
     * Abre el nodo adyacente al mejor nodo para poder evaluarlo.
     */
    private void openNode(Node node) {
        if (!node.open && !node.checked && !node.solid) {
            // Abre el nodo
            node.setAsOpen();
            // Configura el nodo actual como su padre para verificar los proximos nodos adyacentes a este y poder marcar la ruta mas corta
            node.parent = currentNode;
            // Agrega el nodo a la lista de abiertos para poder compararlo
            openList.add(node);
        }
    }

    /**
     * Marca la ruta mas corta desde el nodo objetivo hasta el nodo inicial.
     */
    private void trackThePath() {
        Node current = goalNode;
        while (current != startNode) {
            current = current.parent;
            if (current != startNode) current.setBackground(Color.green);
        }
    }

}
