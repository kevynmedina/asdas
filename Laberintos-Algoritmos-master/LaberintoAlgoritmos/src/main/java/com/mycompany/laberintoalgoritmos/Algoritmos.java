
package com.mycompany.laberintoalgoritmos;


import java.util.*;

public class Algoritmos {

    public Resultado buscarBFS(Laberinto laberinto) {
        //long inicioTiempo = System.currentTimeMillis();
        long inicioTiempo = System.nanoTime();
        int inicio = laberinto.getInicio();
        int fin = laberinto.getFin();
        Map<Integer, Integer> padres = new HashMap<>();
        Queue<Integer> cola = new LinkedList<>();
        Set<Integer> visitados = new HashSet<>();
        cola.offer(inicio);
        visitados.add(inicio);
        boolean encontrado = false;

        while (!cola.isEmpty()) {
            int actual = cola.poll();
            if (actual == fin) {
                encontrado = true;
                break;
            }
            for (int vecino : laberinto.obtenerVecinos(actual)) {
                if (!visitados.contains(vecino)) {
                    padres.put(vecino, actual);
                    visitados.add(vecino);
                    cola.offer(vecino);
                }
            }
        }
        List<Integer> ruta = encontrado ? reconstruirRuta(padres, inicio, fin) : new ArrayList<>();
        //long finTiempo = System.currentTimeMillis();
        long finTiempo = System.nanoTime();
        return new Resultado(ruta, "BFS", finTiempo - inicioTiempo, ruta.size());
    }
    
    public Resultado buscarDijkstra(Laberinto laberinto) {
        long inicioTiempo = System.nanoTime();
        int inicio = laberinto.getInicio();
        int fin = laberinto.getFin();
        Map<Integer, Integer> padres = new HashMap<>();
        Map<Integer, Integer> dist = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
        pq.offer(new int[]{inicio, 0});
        dist.put(inicio, 0);
        boolean encontrado = false;

        while (!pq.isEmpty()) {
            int[] actual = pq.poll();
            int nodo = actual[0], costo = actual[1];
            if (nodo == fin) {
                encontrado = true;
                break;
            }
            for (int vecino : laberinto.obtenerVecinos(nodo)) {
                int nuevoCosto = costo + 1;
                if (!dist.containsKey(vecino) || nuevoCosto < dist.get(vecino)) {
                    dist.put(vecino, nuevoCosto);
                    padres.put(vecino, nodo);
                    pq.offer(new int[]{vecino, nuevoCosto});
                }
            }
        }
        List<Integer> ruta = encontrado ? reconstruirRuta(padres, inicio, fin) : new ArrayList<>();
        long finTiempo = System.nanoTime();
        return new Resultado(ruta, "Dijkstra", finTiempo - inicioTiempo, ruta.size());
    }

    public Resultado buscarAStar(Laberinto laberinto) {
        long inicioTiempo = System.nanoTime();
        int inicio = laberinto.getInicio();
        int fin = laberinto.getFin();
        int filas = laberinto.getFilas();
        int columnas = laberinto.getColumnas();
        Map<Integer, Integer> padres = new HashMap<>();
        Map<Integer, Integer> gScore = new HashMap<>();
        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        gScore.put(inicio, 0);
        pq.offer(new int[]{inicio, 0});
        boolean encontrado = false;

        while (!pq.isEmpty()) {
            int[] actual = pq.poll();
            int nodo = actual[0];
            if (nodo == fin) {
                encontrado = true;
                break;
            }
            for (int vecino : laberinto.obtenerVecinos(nodo)) {
                int tentativeG = gScore.get(nodo) + 1;
                if (!gScore.containsKey(vecino) || tentativeG < gScore.get(vecino)) {
                    padres.put(vecino, nodo);
                    gScore.put(vecino, tentativeG);
                    double fScore = tentativeG + calcularHeuristica(vecino, fin, filas, columnas);
                    pq.offer(new int[]{vecino, (int)fScore});
                }
            }
        }
        List<Integer> ruta = encontrado ? reconstruirRuta(padres, inicio, fin) : new ArrayList<>();
        long finTiempo = System.nanoTime();
        return new Resultado(ruta, "A*", finTiempo - inicioTiempo, ruta.size());
    }
    
    public List<Resultado> compararAlgoritmos(Laberinto laberinto) {
        List<Resultado> resultados = new ArrayList<>();
        resultados.add(buscarBFS(laberinto));
        resultados.add(buscarDijkstra(laberinto));
        resultados.add(buscarAStar(laberinto));
        return resultados;
    }

    private List<Integer> reconstruirRuta(Map<Integer, Integer> padres, int inicio, int fin) {
        List<Integer> ruta = new ArrayList<>();
        Integer actual = fin;
        while (actual != null && !actual.equals(inicio)) {
            ruta.add(actual);
            actual = padres.get(actual);
        }
        if (actual != null) ruta.add(inicio);
        Collections.reverse(ruta);
        return ruta;
    }

    private double calcularHeuristica(int actual, int objetivo, int filas, int columnas) {
        int x1 = actual / columnas, y1 = actual % columnas;
        int x2 = objetivo / columnas, y2 = objetivo % columnas;
        return Math.abs(x1 - x2) + Math.abs(y1 - y2);
    }
}