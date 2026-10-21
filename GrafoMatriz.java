import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

public class GrafoMatriz {
    private boolean[][] matriz;
    private int numVertices;

    public GrafoMatriz(int vertices) {
        if (vertices < 0) {
            throw new IllegalArgumentException("La cantidad de vertices no puede ser negativa.");
        }
        this.numVertices = vertices;
        this.matriz = new boolean[vertices][vertices];
    }

    public int cantidadVertices() {
        return numVertices;
    }

    public int cantidadAristas() {
        int cantidad = 0;
        for (int i = 0; i < numVertices; i++) {
            for (int j = i + 1; j < numVertices; j++) {
                if (matriz[i][j]) {
                    cantidad++;
                }
            }
        }
        return cantidad;
    }

    public int agregarVertice() {
        boolean[][] nuevaMatriz = new boolean[numVertices + 1][numVertices + 1];
        for (int i = 0; i < numVertices; i++) {
            System.arraycopy(matriz[i], 0, nuevaMatriz[i], 0, numVertices);
        }
        matriz = nuevaMatriz;
        return numVertices++;
    }

    public void eliminarVertice(int vertice) {
        validarVertice(vertice);
        boolean[][] nuevaMatriz = new boolean[numVertices - 1][numVertices - 1];
        int nuevaFila = 0;
        for (int i = 0; i < numVertices; i++) {
            if (i == vertice) {
                continue;
            }
            int nuevaColumna = 0;
            for (int j = 0; j < numVertices; j++) {
                if (j == vertice) {
                    continue;
                }
                nuevaMatriz[nuevaFila][nuevaColumna++] = matriz[i][j];
            }
            nuevaFila++;
        }
        matriz = nuevaMatriz;
        numVertices--;
    }

    public void agregarArista(int i, int j) {
        validarVertice(i);
        validarVertice(j);
        if (i == j) {
            throw new IllegalArgumentException("Este grafo no admite aristas de un vertice a si mismo.");
        }
        matriz[i][j] = true;
        matriz[j][i] = true;
    }

    public void eliminarArista(int i, int j) {
        validarVertice(i);
        validarVertice(j);
        matriz[i][j] = false;
        matriz[j][i] = false;
    }

    public boolean hayArista(int i, int j) {
        validarVertice(i);
        validarVertice(j);
        return matriz[i][j];
    }

    public int grado(int vertice) {
        validarVertice(vertice);
        int grado = 0;
        for (int j = 0; j < numVertices; j++) {
            if (matriz[vertice][j]) {
                grado++;
            }
        }
        return grado;
    }

    public List<Integer> obtenerVecinos(int vertice) {
        validarVertice(vertice);
        List<Integer> vecinos = new ArrayList<>();
        for (int j = 0; j < numVertices; j++) {
            if (matriz[vertice][j]) {
                vecinos.add(j);
            }
        }
        return vecinos;
    }

    public List<Integer> recorridoAnchura(int inicio) {
        validarVertice(inicio);
        List<Integer> recorrido = new ArrayList<>();
        boolean[] visitado = new boolean[numVertices];
        Deque<Integer> cola = new ArrayDeque<>();
        visitado[inicio] = true;
        cola.addLast(inicio);

        while (!cola.isEmpty()) {
            int actual = cola.removeFirst();
            recorrido.add(actual);
            for (int vecino = 0; vecino < numVertices; vecino++) {
                if (matriz[actual][vecino] && !visitado[vecino]) {
                    visitado[vecino] = true;
                    cola.addLast(vecino);
                }
            }
        }
        return recorrido;
    }

    public List<Integer> recorridoProfundidad(int inicio) {
        validarVertice(inicio);
        List<Integer> recorrido = new ArrayList<>();
        boolean[] visitado = new boolean[numVertices];
        Deque<Integer> pila = new ArrayDeque<>();
        pila.push(inicio);

        while (!pila.isEmpty()) {
            int actual = pila.pop();
            if (visitado[actual]) {
                continue;
            }
            visitado[actual] = true;
            recorrido.add(actual);
            for (int vecino = numVertices - 1; vecino >= 0; vecino--) {
                if (matriz[actual][vecino] && !visitado[vecino]) {
                    pila.push(vecino);
                }
            }
        }
        return recorrido;
    }

    public boolean esConexo() {
        return numVertices == 0 || recorridoAnchura(0).size() == numVertices;
    }

    public void mostrarMatriz() {
        System.out.print("    ");
        for (int j = 0; j < numVertices; j++) {
            System.out.printf("%3d", j);
        }
        System.out.println();
        for (int i = 0; i < numVertices; i++) {
            System.out.printf("%3d ", i);
            for (int j = 0; j < numVertices; j++) {
                System.out.printf("%3d", matriz[i][j] ? 1 : 0);
            }
            System.out.println();
        }
    }

    private void validarVertice(int vertice) {
        if (vertice < 0 || vertice >= numVertices) {
            throw new IndexOutOfBoundsException("Vertice fuera de rango: " + vertice);
        }
    }

    public static void main(String[] args) {
        GrafoMatriz grafo = new GrafoMatriz(5);
        grafo.agregarArista(0, 1);
        grafo.agregarArista(0, 2);
        grafo.agregarArista(1, 3);
        grafo.agregarArista(2, 3);
        grafo.agregarArista(3, 4);

        System.out.println("Matriz de adyacencia (1 = hay arista):");
        grafo.mostrarMatriz();
        System.out.println("Cantidad de vertices: " + grafo.cantidadVertices());
        System.out.println("Cantidad de aristas: " + grafo.cantidadAristas());
        System.out.println("Vecinos del vertice 3: " + grafo.obtenerVecinos(3));
        System.out.println("Grado del vertice 3: " + grafo.grado(3));
        System.out.println("Recorrido BFS desde 0: " + grafo.recorridoAnchura(0));
        System.out.println("Recorrido DFS desde 0: " + grafo.recorridoProfundidad(0));
        System.out.println("Es conexo: " + grafo.esConexo());
    }
}
