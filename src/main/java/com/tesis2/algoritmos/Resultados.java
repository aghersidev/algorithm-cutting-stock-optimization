/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tesis2.algoritmos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Andres
 */
public class Resultados {

    double mejorSolucionInicialPuntaje;
    double promedioSolucionesInicialPuntaje;

    double[] puntajesMejoresSoluciones;
    double[] promedioSoluciones;
    double[] soluciones;

    double[] generaciones;
    double[] indicesSoluciones;

    Solucion mejorSolucion;
    List<Float> solucionFormateada1;
    List<Float> solucionFormateada2;
    List<Integer> solucionFormateada3;
    List<Float> solucionFormateada4;

    String resultadoString() {
        String s1 = "";
        String s2 = "";
        int count1 = 0;
        int count2 = 0;

        for (int i = 0; i < solucionFormateada1.size(); i++) {
            if (i == 0) {
                s1 = s1 + solucionFormateada1.get(i) + ",";
                count1 = count1 + 1;
            } else {
                if (i == solucionFormateada1.size() - 1) {
                    s2 = s2 + (solucionFormateada2.get(i - 1) + solucionFormateada4.get(i - 1)) + ",";
                    count2 = count2 + 1;
                    if (solucionFormateada2.get(i) <= 0.01) {
                        s1 = s1 + solucionFormateada1.get(i) + ",";
                        count1 = count1 + 1;
                        s2 = s2 + (solucionFormateada2.get(i) + solucionFormateada4.get(i  )) + ",";
                        count2 = count2 + 1;
                    }
                } else {
                    if (solucionFormateada2.get(i) <= 0.01) {
                        s1 = s1 + solucionFormateada1.get(i) + ",";
                        count1 = count1 + 1;
                        s2 = s2 + (solucionFormateada2.get(i - 1) + solucionFormateada4.get(i - 1)) + ",";
                        count2 = count2 + 1;
                    }
                }
            }

        }

        String s = s1 + s2;
        return s;
    }
}
