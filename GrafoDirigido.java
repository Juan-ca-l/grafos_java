/**
 * Grafo en el que cada arista tiene un sentido desde el vertice de origen al destino.
 */
public class GrafoDirigido extends GrafoMatriz {
    /**
     * Crea un grafo dirigido vacio.
     */
    public GrafoDirigido() {
        super(true);
    }

    /**
     * Crea un grafo dirigido con vertices iniciales numerados desde cero.
     *
     * @param cantidadVertices cantidad inicial de vertices
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public GrafoDirigido(int cantidadVertices) {
        super(cantidadVertices, true);
    }
}