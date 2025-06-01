package com.mycompany.laberintoalgoritmos;

import java.io.*;
import java.util.*;

public class Laberinto {
    private Map<Integer, List<Integer>> grafo;
    private int filas;
    private int columnas;
    private int inicio;
    private int fin;
    
    public Laberinto() {
        this.grafo = new HashMap<>();
        this.filas = 0;
        this.columnas = 0;
        this.inicio = -1;
        this.fin = -1;
    }
    
    public Laberinto(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.grafo = new HashMap<>();
        this.inicio = -1;
        this.fin = -1;
    }
    
    // Getters
    public Map<Integer, List<Integer>> getGrafo() {
        return grafo;
    }
    
    public int getFilas() {
        return filas;
    }
    
    public int getColumnas() {
        return columnas;
    }
    
    public int getInicio() {
        return inicio;
    }
    
    public int getFin() {
        return fin;
    }
    
    // Setters
    public void setGrafo(Map<Integer, List<Integer>> grafo) {
        this.grafo = grafo;
    }
    
    public void setFilas(int filas) {
        this.filas = filas;
    }
    
    public void setColumnas(int columnas) {
        this.columnas = columnas;
    }
    
    public void setInicio(int inicio) {
        this.inicio = inicio;
    }
    
    public void setFin(int fin) {
        this.fin = fin;
    }
    
    /**
     * Crea un laberinto aleatorio conectado usando Recursive Backtracking
     */
    public void crearAleatorio(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.grafo = new HashMap<>();
        
        // Inicializar todas las celdas en el grafo
        for (int i = 0; i < filas * columnas; i++) {
            grafo.put(i, new ArrayList<>());
        }
        
        // Generar laberinto usando algoritmo de Recursive Backtracking
        generarLaberintoRecursivo();
        
        // Establecer puntos de inicio y fin por defecto
        this.inicio = 0;
        this.fin = filas * columnas - 1;
    }
    
    /**
     * Genera el laberinto usando el algoritmo Recursive Backtracking
     */
    private void generarLaberintoRecursivo() {
        Random random = new Random();
        boolean[] visitado = new boolean[filas * columnas];
        Stack<Integer> pila = new Stack<>();
        
        // Comenzar desde una celda aleatoria
        int celdaActual = random.nextInt(filas * columnas);
        visitado[celdaActual] = true;
        pila.push(celdaActual);
        
        while (!pila.isEmpty()) {
            celdaActual = pila.peek();
            List<Integer> vecinosNoVisitados = obtenerVecinosNoVisitados(celdaActual, visitado);
            
            if (!vecinosNoVisitados.isEmpty()) {
                // Elegir un vecino aleatorio
                int vecinoElegido = vecinosNoVisitados.get(random.nextInt(vecinosNoVisitados.size()));
                
                // Crear conexión bidireccional
                grafo.get(celdaActual).add(vecinoElegido);
                grafo.get(vecinoElegido).add(celdaActual);
                
                // Marcar como visitado y añadir a la pila
                visitado[vecinoElegido] = true;
                pila.push(vecinoElegido);
            } else {
                // No hay vecinos no visitados, retroceder
                pila.pop();
            }
        }
        
        // Añadir algunas conexiones adicionales para hacer el laberinto más interesante
        añadirConexionesAdicionales(random);
    }
    
    /**
     * Obtiene los vecinos no visitados de una celda
     */
    private List<Integer> obtenerVecinosNoVisitados(int celda, boolean[] visitado) {
        List<Integer> vecinos = new ArrayList<>();
        int fila = celda / columnas;
        int col = celda % columnas;
        
        // Verificar vecinos en las 4 direcciones
        int[] desplazamientosFila = {-1, 1, 0, 0}; // arriba, abajo, izquierda, derecha
        int[] desplazamientosCol = {0, 0, -1, 1};
        
        for (int i = 0; i < 4; i++) {
            int nuevaFila = fila + desplazamientosFila[i];
            int nuevaCol = col + desplazamientosCol[i];
            
            if (esValidaPosicion(nuevaFila, nuevaCol)) {
                int vecinoId = nuevaFila * columnas + nuevaCol;
                if (!visitado[vecinoId]) {
                    vecinos.add(vecinoId);
                }
            }
        }
        
        return vecinos;
    }
    
    /**
     * Verifica si una posición es válida dentro del laberinto
     */
    private boolean esValidaPosicion(int fila, int col) {
        return fila >= 0 && fila < filas && col >= 0 && col < columnas;
    }
    
    /**
     * Añade conexiones adicionales para crear ciclos
     */
    private void añadirConexionesAdicionales(Random random) {
        // Añadir algunas conexiones adicionales para crear ciclos (hace el laberinto más interesante)
        int conexionesAdicionales = Math.max(1, (filas * columnas) / 20); // ~5% de conexiones extra
        
        for (int i = 0; i < conexionesAdicionales; i++) {
            int celda1 = random.nextInt(filas * columnas);
            List<Integer> vecinosAdyacentes = obtenerVecinosAdyacentes(celda1);
            
            if (!vecinosAdyacentes.isEmpty()) {
                int celda2 = vecinosAdyacentes.get(random.nextInt(vecinosAdyacentes.size()));
                
                // Solo añadir si no existe ya la conexión
                if (!grafo.get(celda1).contains(celda2)) {
                    grafo.get(celda1).add(celda2);
                    grafo.get(celda2).add(celda1);
                }
            }
        }
    }
    
    /**
     * Obtiene todos los vecinos adyacentes de una celda
     */
    private List<Integer> obtenerVecinosAdyacentes(int celda) {
        List<Integer> vecinos = new ArrayList<>();
        int fila = celda / columnas;
        int col = celda % columnas;
        
        // Verificar vecinos en las 4 direcciones
        int[] desplazamientosFila = {-1, 1, 0, 0};
        int[] desplazamientosCol = {0, 0, -1, 1};
        
        for (int i = 0; i < 4; i++) {
            int nuevaFila = fila + desplazamientosFila[i];
            int nuevaCol = col + desplazamientosCol[i];
            
            if (esValidaPosicion(nuevaFila, nuevaCol)) {
                int vecinoId = nuevaFila * columnas + nuevaCol;
                vecinos.add(vecinoId);
            }
        }
        
        return vecinos;
    }
    
    /**
     * Crea un laberinto más denso con mayor conectividad
     */
    public void crearAleatorioComplejo(int filas, int columnas) {
        this.filas = filas;
        this.columnas = columnas;
        this.grafo = new HashMap<>();
        
        // Inicializar todas las celdas
        for (int i = 0; i < filas * columnas; i++) {
            grafo.put(i, new ArrayList<>());
        }
        
        Random random = new Random();
        
        // Crear un laberinto más conectado
        for (int fila = 0; fila < filas; fila++) {
            for (int col = 0; col < columnas; col++) {
                int celdaActual = fila * columnas + col;
                
                // Conectar con vecino derecho con 70% probabilidad
                if (col < columnas - 1 && random.nextDouble() < 0.7) {
                    int vecinoDerecha = fila * columnas + (col + 1);
                    grafo.get(celdaActual).add(vecinoDerecha);
                    grafo.get(vecinoDerecha).add(celdaActual);
                }
                
                // Conectar con vecino abajo con 70% probabilidad
                if (fila < filas - 1 && random.nextDouble() < 0.7) {
                    int vecinoAbajo = (fila + 1) * columnas + col;
                    grafo.get(celdaActual).add(vecinoAbajo);
                    grafo.get(vecinoAbajo).add(celdaActual);
                }
            }
        }
        
        // Asegurar conectividad mínima
        asegurarConectividad();
        
        this.inicio = 0;
        this.fin = filas * columnas - 1;
    }
    
    /**
     * Asegura que el laberinto esté completamente conectado
     */
    private void asegurarConectividad() {
        // Usar Union-Find para verificar y asegurar que el laberinto esté conectado
        int[] padre = new int[filas * columnas];
        for (int i = 0; i < padre.length; i++) {
            padre[i] = i;
        }
        
        // Unir componentes basándose en las conexiones existentes
        for (Map.Entry<Integer, List<Integer>> entrada : grafo.entrySet()) {
            int nodo1 = entrada.getKey();
            for (int nodo2 : entrada.getValue()) {
                union(padre, nodo1, nodo2);
            }
        }
        
        // Encontrar componentes desconectadas y conectarlas
        Random random = new Random();
        int raizPrincipal = find(padre, 0);
        
        for (int i = 1; i < filas * columnas; i++) {
            if (find(padre, i) != raizPrincipal) {
                // Encontrar una conexión para unir esta componente
                List<Integer> vecinosAdyacentes = obtenerVecinosAdyacentes(i);
                for (int vecino : vecinosAdyacentes) {
                    if (find(padre, vecino) == raizPrincipal) {
                        // Crear conexión
                        grafo.get(i).add(vecino);
                        grafo.get(vecino).add(i);
                        union(padre, i, vecino);
                        break;
                    }
                }
            }
        }
    }
    
    /**
     * Operación Find para Union-Find
     */
    private int find(int[] padre, int x) {
        if (padre[x] != x) {
            padre[x] = find(padre, padre[x]);
        }
        return padre[x];
    }
    
    /**
     * Operación Union para Union-Find
     */
    private void union(int[] padre, int x, int y) {
        int raizX = find(padre, x);
        int raizY = find(padre, y);
        if (raizX != raizY) {
            padre[raizX] = raizY;
        }
    }
    
    /**
     * Permite al usuario crear un laberinto manualmente
     */
    public void crearManual() {
        // TODO: Implementar interfaz para creación manual
        // Por ahora, crear un laberinto simple de ejemplo
        this.filas = 5;
        this.columnas = 5;
        this.grafo = new HashMap<>();
        
        // Inicializar nodos
        for (int i = 0; i < filas * columnas; i++) {
            grafo.put(i, new ArrayList<>());
        }
        
        // Crear algunas conexiones de ejemplo
        // Fila superior
        agregarConexion(0, 1);
        agregarConexion(1, 2);
        agregarConexion(3, 4);
        
        // Conexiones verticales
        agregarConexion(0, 5);
        agregarConexion(2, 7);
        agregarConexion(4, 9);
        
        // Fila media
        agregarConexion(5, 6);
        agregarConexion(7, 8);
        agregarConexion(8, 9);
        
        // Más conexiones verticales
        agregarConexion(6, 11);
        agregarConexion(9, 14);
        
        // Fila inferior
        agregarConexion(10, 15);
        agregarConexion(15, 20);
        agregarConexion(11, 16);
        agregarConexion(16, 21);
        agregarConexion(21, 22);
        agregarConexion(22, 23);
        agregarConexion(23, 24);
        
        this.inicio = 0;
        this.fin = 24;
    }
    
    /**
     * Agrega una conexión bidireccional entre dos nodos
     */
    private void agregarConexion(int nodo1, int nodo2) {
        if (grafo.containsKey(nodo1) && grafo.containsKey(nodo2)) {
            if (!grafo.get(nodo1).contains(nodo2)) {
                grafo.get(nodo1).add(nodo2);
            }
            if (!grafo.get(nodo2).contains(nodo1)) {
                grafo.get(nodo2).add(nodo1);
            }
        }
    }
    
    /**
     * Carga un laberinto desde un archivo CSV
     */
    public void cargarDesdeArchivo(String rutaArchivo) throws IOException {
        grafo = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            String linea = reader.readLine();
            if (linea == null) {
                throw new IOException("Archivo vacío");
            }
            
            // Leer dimensiones (primera línea)
            String[] dimensiones = linea.trim().split(",");
            if (dimensiones.length != 2) {
                throw new IOException("Formato incorrecto en la primera línea. Debe ser: filas,columnas");
            }
            
            this.filas = Integer.parseInt(dimensiones[0].trim());
            this.columnas = Integer.parseInt(dimensiones[1].trim());
            
            // Inicializar todos los nodos
            for (int i = 0; i < filas * columnas; i++) {
                grafo.put(i, new ArrayList<>());
            }
            
            // Leer lista de adyacencia
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;
                
                String[] partes = linea.split(":");
                if (partes.length != 2) {
                    throw new IOException("Formato incorrecto en línea: " + linea + 
                                        ". Debe ser: nodo:vecino1,vecino2,...");
                }
                
                int nodo = Integer.parseInt(partes[0].trim());
                String[] vecinos = partes[1].trim().split(",");
                
                for (String vecinoStr : vecinos) {
                    if (!vecinoStr.trim().isEmpty()) {
                        int vecino = Integer.parseInt(vecinoStr.trim());
                        
                        // Validar que los nodos estén en rango válido
                        if (nodo >= 0 && nodo < filas * columnas && 
                            vecino >= 0 && vecino < filas * columnas) {
                            
                            if (!grafo.get(nodo).contains(vecino)) {
                                grafo.get(nodo).add(vecino);
                            }
                            if (!grafo.get(vecino).contains(nodo)) {
                                grafo.get(vecino).add(nodo);
                            }
                        }
                    }
                }
            }
            
            // Establecer puntos de inicio y fin por defecto
            this.inicio = 0;
            this.fin = filas * columnas - 1;
            
        } catch (NumberFormatException e) {
            throw new IOException("Error al parsear números en el archivo: " + e.getMessage());
        }
    }

    /**
     * Carga un laberinto desde un string.
     */
    public void cargarDesdeString(String grafoManual) throws IOException {
        grafo = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new StringReader(grafoManual))) {
            String linea = reader.readLine();
            if (linea == null) {
                throw new IOException("String vacío");
            }

            // Leer dimensiones (primera línea)
            String[] dimensiones = linea.trim().split(",");
            if (dimensiones.length != 2) {
                throw new IOException("Formato incorrecto en la primera línea. Debe ser: filas,columnas");
            }

            this.filas = Integer.parseInt(dimensiones[0].trim());
            this.columnas = Integer.parseInt(dimensiones[1].trim());

            // Inicializar todos los nodos
            for (int i = 0; i < filas * columnas; i++) {
                grafo.put(i, new ArrayList<>());
            }

            // Leer lista de adyacencia
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] partes = linea.split(":");
                if (partes.length != 2) {
                    throw new IOException("Formato incorrecto en línea: " + linea +
                            ". Debe ser: nodo:vecino1,vecino2,...");
                }

                int nodo = Integer.parseInt(partes[0].trim());
                String[] vecinos = partes[1].trim().split(",");

                for (String vecinoStr : vecinos) {
                    if (!vecinoStr.trim().isEmpty()) {
                        int vecino = Integer.parseInt(vecinoStr.trim());

                        // Validar que los nodos estén en rango válido
                        if (nodo >= 0 && nodo < filas * columnas &&
                                vecino >= 0 && vecino < filas * columnas) {

                            if (!grafo.get(nodo).contains(vecino)) {
                                grafo.get(nodo).add(vecino);
                            }
                            if (!grafo.get(vecino).contains(nodo)) {
                                grafo.get(vecino).add(nodo);
                            }
                        }
                    }
                }
            }

            // Establecer puntos de inicio y fin por defecto
            this.inicio = 0;
            this.fin = filas * columnas - 1;

        } catch (NumberFormatException e) {
            throw new IOException("Error al parsear números en el string: " + e.getMessage());
        }
    }

    /**
     * Establece los puntos de inicio y fin del laberinto
     */
    public void establecerInicioFin(int inicio, int fin) {
        if (inicio >= 0 && inicio < filas * columnas && 
            fin >= 0 && fin < filas * columnas) {
            this.inicio = inicio;
            this.fin = fin;
        } else {
            throw new IllegalArgumentException("Puntos de inicio o fin fuera de rango");
        }
    }
    
    /**
     * Obtiene los vecinos de un nodo específico
     */
    public List<Integer> obtenerVecinos(int nodo) {
        return grafo.getOrDefault(nodo, new ArrayList<>());
    }
    
    /**
     * Verifica si el laberinto es válido
     */
    public boolean esValido() {
        // Verificar que el grafo no esté vacío
        if (grafo.isEmpty()) {
            return false;
        }
        
        // Verificar que los puntos de inicio y fin sean válidos
        if (inicio < 0 || inicio >= filas * columnas || 
            fin < 0 || fin >= filas * columnas) {
            return false;
        }
        
        // Verificar que los puntos de inicio y fin estén en el grafo
        if (!grafo.containsKey(inicio) || !grafo.containsKey(fin)) {
            return false;
        }
        
        // Verificar conectividad entre inicio y fin usando BFS
        return existeCamino(inicio, fin);
    }
    
    /**
     * Verifica si existe un camino entre dos nodos usando BFS
     */
    private boolean existeCamino(int origen, int destino) {
        if (origen == destino) return true;
        
        Set<Integer> visitados = new HashSet<>();
        Queue<Integer> cola = new LinkedList<>();
        
        cola.offer(origen);
        visitados.add(origen);
        
        while (!cola.isEmpty()) {
            int actual = cola.poll();
            
            for (int vecino : grafo.getOrDefault(actual, new ArrayList<>())) {
                if (vecino == destino) {
                    return true;
                }
                
                if (!visitados.contains(vecino)) {
                    visitados.add(vecino);
                    cola.offer(vecino);
                }
            }
        }
        
        return false;
    }
    
    /**
     * Convierte el laberinto a una representación string para depuración
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Laberinto ").append(filas).append("x").append(columnas).append("\n");
        sb.append("Inicio: ").append(inicio).append(", Fin: ").append(fin).append("\n");
        sb.append("Grafo:\n");
        
        for (Map.Entry<Integer, List<Integer>> entrada : grafo.entrySet()) {
            sb.append(entrada.getKey()).append(": ");
            sb.append(entrada.getValue().toString()).append("\n");
        }
        
        return sb.toString();
    }
    
    /**
     * Obtiene información estadística del laberinto
     */
    public String obtenerEstadisticas() {
        int totalNodos = grafo.size();
        int totalConexiones = grafo.values().stream().mapToInt(List::size).sum() / 2; // Dividir por 2 porque son bidireccionales
        double densidad = totalNodos > 0 ? (double) totalConexiones / (totalNodos * (totalNodos - 1) / 2) : 0;
        
        return String.format(
            "Estadísticas del Laberinto:\n" +
            "- Dimensiones: %dx%d\n" +
            "- Nodos totales: %d\n" +
            "- Nodos accesibles: %d\n" +
            "- Conexiones: %d\n" +
            "- Densidad: %.2f%%\n" +
            "- Válido: %s",
            filas, columnas, filas * columnas, totalNodos, 
            totalConexiones, densidad * 100, esValido() ? "Sí" : "No"
        );
    }
    
    /**
 * Carga un laberinto desde un archivo CSV con formato manual.
 * @param rutaArchivo Ruta del archivo CSV.
 * @throws IOException Si hay errores de lectura o formato.
    
   public void cargarLaberintoManual(String rutaArchivo) throws IOException {
       try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
           grafo = new HashMap<>();

           // Leer dimensiones (primera línea)
           String linea = reader.readLine();
           String[] dimensiones = linea.split(",");
           this.filas = Integer.parseInt(dimensiones[0]);
           this.columnas = Integer.parseInt(dimensiones[1]);
               this.inicio = 0;
               this.fin = columnas*filas-1;
           // Inicializar nodos
           for (int i = 0; i < filas * columnas; i++) {
               grafo.put(i, new ArrayList<>());
           }

           // Leer conexiones
           while ((linea = reader.readLine()) != null) {
               linea = linea.trim();


               String[] partes = linea.split(":");
               int nodo = Integer.parseInt(partes[0]);
               String[] vecinos = partes[1].split(",");

               for (String vecinoStr : vecinos) {
                   int vecino = Integer.parseInt(vecinoStr.trim());
                   agregarConexion(nodo, vecino); 
               }
           }
       } catch (NumberFormatException e) {
           throw new IOException("Error" + e.getMessage());
       }
   }*/
}