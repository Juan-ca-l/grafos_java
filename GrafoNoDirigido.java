/**
 * Grafo en el que cada arista conecta dos vertices sin establecer un sentido.
 */
public class GrafoNoDirigido extends GrafoMatriz {
    /**
     * Crea un grafo no dirigido vacio.
     */
    public GrafoNoDirigido() {
        super(false);
    }

    /**
     * Crea un grafo no dirigido con vertices iniciales numerados desde cero.
     *
     * @param cantidadVertices cantidad inicial de vertices
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public GrafoNoDirigido(int cantidadVertices) {
        super(cantidadVertices, false);
    }
}