/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis2.algoritmos;

/**
 *
 * @author Andres
 */
public class Bloque {

    int punteroLugar;
    int largoEnGenes;
    float largoBloque;
    float desperdicio;

    Bloque(int anterior, int cantidadGenes, float sumaLargos, float f) {

        punteroLugar = anterior;
        largoEnGenes = cantidadGenes;
        largoBloque = sumaLargos;
        desperdicio = f;

    }

}
