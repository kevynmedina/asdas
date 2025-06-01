package com.mycompany.laberintoalgoritmos;
import java.util.*;

public class Resultado {
    private List<Integer> ruta;
    private String algoritmo;
    private long tiempoEjecucion;
    private int longitudRuta;
    
    public Resultado(List<Integer> ruta, String algoritmo, long tiempoEjecucion, int longitudRuta) {
        this.ruta = ruta;
        this.algoritmo = algoritmo;
        this.tiempoEjecucion = tiempoEjecucion;
        this.longitudRuta = longitudRuta;
    }
    
    public List<Integer> obtenerRuta() {
        return ruta;
    }
    
    public long obtenerTiempo() {
        return tiempoEjecucion;
    }
    
    public boolean esOptima(List<Resultado> otras) {
        // TODO: Comparar con otras soluciones
        return false;
    }
    
    public void mostrar() {
        System.out.println("Algoritmo: " + algoritmo);
        System.out.println("Tiempo: " + tiempoEjecucion + " ms");
        System.out.println("Longitud ruta: " + longitudRuta);
        System.out.println("Ruta: " + ruta);
    }
    
    // Getters
    public String getAlgoritmo() { return algoritmo; }
    public long getTiempoEjecucion() { return tiempoEjecucion; }
    public int getLongitudRuta() { return longitudRuta; }
}
