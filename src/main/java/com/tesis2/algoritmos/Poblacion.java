package com.tesis2.algoritmos;

import java.util.ArrayList;
import java.util.List;

public class Poblacion {

    List<Solucion> soluciones;
    List<Barra> barrasPedido;
    List<Material> barrasMaterial;
    int cantidadGeneraciones;
    int tamPoblacion;
    int cantidadCromosomas;

    public Poblacion(int cantG, int tamP) {
        this.cantidadGeneraciones = cantG;
        this.tamPoblacion = tamP;
        soluciones = new ArrayList<Solucion>();
        barrasPedido = new ArrayList<Barra>();
        barrasMaterial = new ArrayList<Material>();
    }

    Poblacion(Poblacion poblacionBase) {
        this.cantidadGeneraciones = poblacionBase.cantidadGeneraciones;
        this.tamPoblacion = poblacionBase.tamPoblacion;
        soluciones = new ArrayList<Solucion>();
        for (int i = 0; i < poblacionBase.soluciones.size(); i++) {
            soluciones.add(new Solucion(null, poblacionBase.soluciones.get(i), 0));
        }
        this.cantidadCromosomas = poblacionBase.cantidadCromosomas;
        barrasPedido = poblacionBase.barrasPedido;
        barrasMaterial = poblacionBase.barrasMaterial;
        evaluaSoluciones();
    }

    void evaluaSoluciones() {
        int cantSoluciones = soluciones.size();
        for (int i = 0; i < cantSoluciones; i++) {
            soluciones.get(i).evalua(barrasPedido, barrasMaterial);
        }
    }

    Solucion getMejor() {
        return this.soluciones.get(0);
    }

    double[] getDoubleSoluciones() {
        double[] solus = new double[soluciones.size()];
        for (int i = 0; i < soluciones.size(); i++) {
            solus[i] = soluciones.get(soluciones.size() - 1 - i).puntaje;
        }
        return solus;
    }

    double getPromedioSoluciones() {
        double solus = 0;
        for (int i = 0; i < soluciones.size(); i++) {
            solus = solus + soluciones.get(soluciones.size() - 1 - i).puntaje;
        }
        return solus / soluciones.size();
    }
}
