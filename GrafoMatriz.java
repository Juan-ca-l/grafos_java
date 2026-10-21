import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

/**
 * Representa un grafo mutable mediante una matriz de adyacencia.
 * Las etiquetas de los vertices son unicas y los indices siguen el orden
 * en que estos se agregan.
 */
public class GrafoMatriz {
    /** Matriz que indica si existe una arista entre cada par de vertices. */
    private boolean[][] matriz;

    /** Etiquetas de los vertices, en el mismo orden que las filas de la matriz. */
    private final List<String> vertices;

    /** Indica si las aristas tienen una direccion definida. */
    private final boolean dirigido;

    /**
     * Crea un grafo no dirigido vacio.
     */
    public GrafoMatriz() {
        this(false);
    }

    /**
     * Crea un grafo vacio con la direccion especificada.
     *
     * @param dirigido {@code true} para un grafo dirigido; {@code false} para uno no dirigido
     */
    public GrafoMatriz(boolean dirigido) {
        this.vertices = new ArrayList<>();
        this.matriz = new boolean[0][0];
        this.dirigido = dirigido;
    }

    /**
     * Crea un grafo no dirigido con una cantidad inicial de vertices numerados desde cero.
     *
     * @param cantidadVertices cantidad de vertices iniciales
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public GrafoMatriz(int cantidadVertices) {
        this(cantidadVertices, false);
    }

    /**
     * Crea un grafo con una cantidad inicial de vertices numerados desde cero.
     *
     * @param cantidadVertices cantidad de vertices iniciales
     * @param dirigido {@code true} para un grafo dirigido; {@code false} para uno no dirigido
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public GrafoMatriz(int cantidadVertices, boolean dirigido) {
        this(dirigido);
        if (cantidadVertices < 0) {
            throw new IllegalArgumentException("La cantidad de vertices no puede ser negativa.");
        }
        for (int i = 0; i < cantidadVertices; i++) {
            insertarVertice(String.valueOf(i));
        }
    }

    /**
     * Devuelve la cantidad actual de vertices.
     *
     * @return numero de vertices del grafo
     */
    public int getNumVertices() {
        return vertices.size();
    }

    /**
     * Indica si este grafo es dirigido.
     *
     * @return {@code true} si las aristas tienen direccion
     */
    public boolean esDirigido() {
        return dirigido;
    }

    /**
     * Agrega un vertice al final del grafo.
     *
     * @param nombre etiqueta unica del nuevo vertice
     * @throws IllegalArgumentException si el nombre es nulo, vacio o ya existe
     */
    public void agregarVertice(String nombre) {
        insertarVertice(nombre);
    }

    /**
     * Agrega internamente un vertice y redimensiona la matriz de adyacencia.
     *
     * @param nombre etiqueta unica del vertice
     * @throws IllegalArgumentException si el nombre es nulo, vacio o ya existe
     */
    private void insertarVertice(String nombre) {
        validarNombre(nombre);
        if (vertices.contains(nombre)) {
            throw new IllegalArgumentException("Ya existe el vertice: " + nombre);
        }

        int nuevoTamano = vertices.size() + 1;
        boolean[][] nuevaMatriz = new boolean[nuevoTamano][nuevoTamano];
        for (int i = 0; i < vertices.size(); i++) {
            System.arraycopy(matriz[i], 0, nuevaMatriz[i], 0, vertices.size());
        }
        vertices.add(nombre);
        matriz = nuevaMatriz;
    }

    /**
     * Elimina un vertice y todas sus aristas incidentes.
     *
     * @param nombre etiqueta del vertice que se eliminara
     * @return {@code true} si se elimino; {@code false} si no existe
     */
    public boolean eliminarVertice(String nombre) {
        int indice = vertices.indexOf(nombre);
        if (indice < 0) {
            return false;
        }

        int nuevoTamano = vertices.size() - 1;
        boolean[][] nuevaMatriz = new boolean[nuevoTamano][nuevoTamano];
        for (int filaVieja = 0, filaNueva = 0; filaVieja < vertices.size(); filaVieja++) {
            if (filaVieja == indice) {
                continue;
            }
            for (int columnaVieja = 0, columnaNueva = 0; columnaVieja < vertices.size(); columnaVieja++) {
                if (columnaVieja != indice) {
                    nuevaMatriz[filaNueva][columnaNueva++] = matriz[filaVieja][columnaVieja];
                }
            }
            filaNueva++;
        }
        vertices.remove(indice);
        matriz = nuevaMatriz;
        return true;
    }

    /**
     * Cambia la etiqueta de un vertice sin alterar sus aristas.
     *
     * @param nombreActual etiqueta actual del vertice
     * @param nombreNuevo nueva etiqueta, que debe ser unica
     * @return {@code true} si se actualizo; {@code false} si el vertice actual no existe
     * @throws IllegalArgumentException si el nuevo nombre es nulo, vacio o ya pertenece a otro vertice
     */
    public boolean actualizarVertice(String nombreActual, String nombreNuevo) {
        validarNombre(nombreNuevo);
        int indice = vertices.indexOf(nombreActual);
        if (indice < 0) {
            return false;
        }
        if (!nombreActual.equals(nombreNuevo) && vertices.contains(nombreNuevo)) {
            throw new IllegalArgumentException("Ya existe el vertice: " + nombreNuevo);
        }
        vertices.set(indice, nombreNuevo);
        return true;
    }

    /**
     * Devuelve una copia de las etiquetas de los vertices en su orden actual.
     *
     * @return lista independiente de etiquetas
     */
    public List<String> listarVertices() {
        return new ArrayList<>(vertices);
    }

    /**
     * Devuelve las aristas en formato textual, respetando la direccion del grafo.
     *
     * @return lista de aristas como {@code origen -> destino} o {@code origen -- destino}
     */
    public List<String> listarAristas() {
        List<String> aristas = new ArrayList<>();
        for (int i = 0; i < getNumVertices(); i++) {
            int primerDestino = dirigido ? 0 : i;
            for (int j = primerDestino; j < getNumVertices(); j++) {
                if (matriz[i][j]) {
                    String conector = dirigido ? " -> " : " -- ";
                    aristas.add(vertices.get(i) + conector + vertices.get(j));
                }
            }
        }
        return aristas;
    }

    /**
     * Agrega una arista usando los indices actuales de los vertices.
     * En un grafo no dirigido, tambien crea la arista en sentido inverso.
     *
     * @param i indice del vertice de origen
     * @param j indice del vertice de destino
     * @throws IndexOutOfBoundsException si algun indice no pertenece al grafo
     */
    public void agregarArista(int i, int j) {
        validarVertice(i);
        validarVertice(j);
        matriz[i][j] = true;
        if (!dirigido) {
            matriz[j][i] = true;
        }
    }

    /**
     * Agrega una arista entre vertices identificados por sus etiquetas.
     *
     * @param origen etiqueta del vertice de origen
     * @param destino etiqueta del vertice de destino
     * @throws IllegalArgumentException si alguna etiqueta no existe
     */
    public void agregarArista(String origen, String destino) {
        agregarArista(indiceVertice(origen), indiceVertice(destino));
    }

    /**
     * Elimina una arista usando los indices actuales de los vertices.
     * En un grafo no dirigido, tambien elimina la arista inversa.
     *
     * @param i indice del vertice de origen
     * @param j indice del vertice de destino
     * @throws IndexOutOfBoundsException si algun indice no pertenece al grafo
     */
    public void eliminarArista(int i, int j) {
        validarVertice(i);
        validarVertice(j);
        matriz[i][j] = false;
        if (!dirigido) {
            matriz[j][i] = false;
        }
    }

    /**
     * Elimina una arista entre vertices identificados por sus etiquetas.
     *
     * @param origen etiqueta del vertice de origen
     * @param destino etiqueta del vertice de destino
     * @throws IllegalArgumentException si alguna etiqueta no existe
     */
    public void eliminarArista(String origen, String destino) {
        eliminarArista(indiceVertice(origen), indiceVertice(destino));
    }

    /**
     * Comprueba si existe una arista entre dos indices.
     *
     * @param i indice del vertice de origen
     * @param j indice del vertice de destino
     * @return {@code true} si existe la arista
     * @throws IndexOutOfBoundsException si algun indice no pertenece al grafo
     */
    public boolean existeArista(int i, int j) {
        validarVertice(i);
        validarVertice(j);
        return matriz[i][j];
    }

    /**
     * Comprueba si existe una arista entre dos etiquetas.
     *
     * @param origen etiqueta del vertice de origen
     * @param destino etiqueta del vertice de destino
     * @return {@code true} si existe la arista
     * @throws IllegalArgumentException si alguna etiqueta no existe
     */
    public boolean existeArista(String origen, String destino) {
        return existeArista(indiceVertice(origen), indiceVertice(destino));
    }

    /**
     * Calcula el grado de un vertice. En grafos dirigidos suma grados de entrada y salida.
     *
     * @param vertice indice del vertice
     * @return grado total, o la suma de entrada y salida si el grafo es dirigido
     * @throws IndexOutOfBoundsException si el indice no pertenece al grafo
     */
    public int obtenerGrado(int vertice) {
        return obtenerGradoSalida(vertice) + (dirigido ? obtenerGradoEntrada(vertice) : 0);
    }

    /**
     * Cuenta las aristas que llegan al vertice indicado.
     *
     * @param vertice indice del vertice
     * @return grado de entrada
     * @throws IndexOutOfBoundsException si el indice no pertenece al grafo
     */
    public int obtenerGradoEntrada(int vertice) {
        validarVertice(vertice);
        int grado = 0;
        for (int origen = 0; origen < getNumVertices(); origen++) {
            if (matriz[origen][vertice]) {
                grado++;
            }
        }
        return grado;
    }

    /**
     * Cuenta las aristas que salen del vertice indicado.
     *
     * @param vertice indice del vertice
     * @return grado de salida
     * @throws IndexOutOfBoundsException si el indice no pertenece al grafo
     */
    public int obtenerGradoSalida(int vertice) {
        validarVertice(vertice);
        int grado = 0;
        for (int destino = 0; destino < getNumVertices(); destino++) {
            if (matriz[vertice][destino]) {
                grado++;
            }
        }
        return grado;
    }

    /**
     * Devuelve el numero de aristas del grafo.
     *
     * @return cantidad de aristas; las aristas no dirigidas se cuentan una sola vez
     */
    public int getNumAristas() {
        int aristas = 0;
        for (int i = 0; i < getNumVertices(); i++) {
            int primerDestino = dirigido ? 0 : i;
            for (int j = primerDestino; j < getNumVertices(); j++) {
                if (matriz[i][j]) {
                    aristas++;
                }
            }
        }
        return aristas;
    }

    /**
     * Realiza un recorrido en anchura desde un indice y sigue solo las aristas salientes.
     *
     * @param inicio indice del vertice inicial
     * @return indices visitados en el orden del recorrido
     * @throws IndexOutOfBoundsException si el indice inicial no pertenece al grafo
     */
    public List<Integer> recorridoBFS(int inicio) {
        validarVertice(inicio);
        List<Integer> recorrido = new ArrayList<>();
        boolean[] visitado = new boolean[getNumVertices()];
        Queue<Integer> pendientes = new ArrayDeque<>();
        visitado[inicio] = true;
        pendientes.add(inicio);

        while (!pendientes.isEmpty()) {
            int actual = pendientes.remove();
            recorrido.add(actual);
            for (int vecino = 0; vecino < getNumVertices(); vecino++) {
                if (matriz[actual][vecino] && !visitado[vecino]) {
                    visitado[vecino] = true;
                    pendientes.add(vecino);
                }
            }
        }
        return recorrido;
    }

    /**
     * Realiza un recorrido en anchura desde una etiqueta.
     *
     * @param inicio etiqueta del vertice inicial
     * @return etiquetas visitadas en el orden del recorrido
     * @throws IllegalArgumentException si la etiqueta no existe
     */
    public List<String> recorridoBFS(String inicio) {
        return etiquetasDe(recorridoBFS(indiceVertice(inicio)));
    }

    /**
     * Realiza un recorrido en profundidad desde un indice y sigue solo las aristas salientes.
     *
     * @param inicio indice del vertice inicial
     * @return indices visitados en el orden del recorrido
     * @throws IndexOutOfBoundsException si el indice inicial no pertenece al grafo
     */
    public List<Integer> recorridoDFS(int inicio) {
        validarVertice(inicio);
        List<Integer> recorrido = new ArrayList<>();
        boolean[] visitado = new boolean[getNumVertices()];
        recorridoDFS(inicio, visitado, recorrido);
        return recorrido;
    }

    /**
     * Realiza un recorrido en profundidad desde una etiqueta.
     *
     * @param inicio etiqueta del vertice inicial
     * @return etiquetas visitadas en el orden del recorrido
     * @throws IllegalArgumentException si la etiqueta no existe
     */
    public List<String> recorridoDFS(String inicio) {
        return etiquetasDe(recorridoDFS(indiceVertice(inicio)));
    }

    /**
     * Procesa recursivamente los vertices alcanzables durante el recorrido en profundidad.
     *
     * @param actual indice del vertice que se procesa
     * @param visitado arreglo que registra los vertices ya visitados
     * @param recorrido lista donde se acumula el orden del recorrido
     */
    private void recorridoDFS(int actual, boolean[] visitado, List<Integer> recorrido) {
        visitado[actual] = true;
        recorrido.add(actual);
        for (int vecino = 0; vecino < getNumVertices(); vecino++) {
            if (matriz[actual][vecino] && !visitado[vecino]) {
                recorridoDFS(vecino, visitado, recorrido);
            }
        }
    }

    /**
     * Convierte una secuencia de indices a las etiquetas correspondientes.
     *
     * @param indices indices que se convertiran
     * @return etiquetas en el mismo orden
     */
    private List<String> etiquetasDe(List<Integer> indices) {
        List<String> resultado = new ArrayList<>();
        for (int indice : indices) {
            resultado.add(vertices.get(indice));
        }
        return resultado;
    }

    /**
     * Imprime por consola la matriz de adyacencia con las etiquetas de los vertices.
     */
    public void imprimirMatriz() {
        System.out.print("     ");
        for (String vertice : vertices) {
            System.out.printf("%8s", vertice);
        }
        System.out.println();

        for (int i = 0; i < getNumVertices(); i++) {
            System.out.printf("%5s", vertices.get(i));
            for (int j = 0; j < getNumVertices(); j++) {
                System.out.printf("%8d", matriz[i][j] ? 1 : 0);
            }
            System.out.println();
        }
    }

    /**
     * Busca el indice asociado a una etiqueta.
     *
     * @param nombre etiqueta que se buscara
     * @return indice actual de la etiqueta
     * @throws IllegalArgumentException si la etiqueta no existe
     */
    private int indiceVertice(String nombre) {
        int indice = vertices.indexOf(nombre);
        if (indice < 0) {
            throw new IllegalArgumentException("No existe el vertice: " + nombre);
        }
        return indice;
    }

    /**
     * Comprueba que una etiqueta sea distinta de nula y no este vacia.
     *
     * @param nombre etiqueta que se validara
     * @throws IllegalArgumentException si la etiqueta es nula o esta vacia
     */
    private void validarNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del vertice no puede estar vacio.");
        }
    }

    /**
     * Comprueba que un indice corresponda a un vertice existente.
     *
     * @param vertice indice que se validara
     * @throws IndexOutOfBoundsException si el indice esta fuera del rango actual
     */
    private void validarVertice(int vertice) {
        if (vertice < 0 || vertice >= getNumVertices()) {
            throw new IndexOutOfBoundsException(
                    "Vertice fuera de rango: " + vertice + ". Rango valido: 0-" + (getNumVertices() - 1));
        }
    }

    /**
     * Ejecuta un ejemplo de uso de grafos dirigidos y no dirigidos.
     *
     * @param args argumentos de linea de comandos, no utilizados
     */
    public static void main(String[] args) {
        GrafoNoDirigido noDirigido = new GrafoNoDirigido();
        noDirigido.agregarVertice("A");
        noDirigido.agregarVertice("B");
        noDirigido.agregarVertice("C");
        noDirigido.agregarArista("A", "B");
        noDirigido.agregarArista("B", "C");

        System.out.println("Grafo no dirigido:");
        System.out.println("Vertices: " + noDirigido.listarVertices());
        System.out.println("Aristas: " + noDirigido.listarAristas());
        System.out.println("BFS desde A: " + noDirigido.recorridoBFS("A"));

        GrafoDirigido dirigido = new GrafoDirigido();
        dirigido.agregarVertice("Inicio");
        dirigido.agregarVertice("Proceso");
        dirigido.agregarVertice("Fin");
        dirigido.agregarArista("Inicio", "Proceso");
        dirigido.agregarArista("Proceso", "Fin");

        System.out.println("\nGrafo dirigido:");
        System.out.println("Aristas: " + dirigido.listarAristas());
        System.out.println("Existe Fin -> Proceso: " + dirigido.existeArista("Fin", "Proceso"));
        dirigido.actualizarVertice("Proceso", "Revision");
        System.out.println("Despues de actualizar: " + dirigido.listarVertices());
        dirigido.eliminarVertice("Fin");
        System.out.println("Despues de eliminar Fin: " + dirigido.listarVertices());
    }
}