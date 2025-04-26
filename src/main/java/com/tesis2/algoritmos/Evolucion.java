package com.tesis2.algoritmos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

class Evolucion {

    int cantidadGeneraciones;
    int generacionActual;
    List<Solucion> seleccionados;
    List<Solucion> soluciones;
    List<Solucion> nuevosHijos;
    Poblacion poblacion;
    private float tasaC;
    private float tasaA;
    private int maxGeneracionesSinMejora;
    private float tasaM;

    Evolucion(Poblacion poblacion) {
        this.poblacion = poblacion;
        cantidadGeneraciones = poblacion.cantidadGeneraciones;
        generacionActual = 0;
        this.soluciones = poblacion.soluciones;
    }

    Evolucion(Poblacion poblacionGenetico, float tasaCGenetico, float tasaMGenetico, int maxGeneracionesSinMejoraGenetico, int maxGeneracionesGenetico, float tasaA) {
        poblacionGenetico.cantidadGeneraciones = maxGeneracionesGenetico;
        this.poblacion = poblacionGenetico;

        cantidadGeneraciones = poblacionGenetico.cantidadGeneraciones;
        generacionActual = 0;

        this.tasaC = tasaCGenetico;
        this.tasaM = tasaMGenetico;
        this.maxGeneracionesSinMejora = maxGeneracionesSinMejoraGenetico;
        this.tasaA = tasaA;

        this.soluciones = poblacionGenetico.soluciones;

    }

    void seleccion() {
        seleccionados = new ArrayList<Solucion>();
        int sizeSoluciones = soluciones.size();
        List<Float> probabilidades = new ArrayList<Float>();
        float fAcumulada = 0;
        for (int i = 0; i < sizeSoluciones; i++) {
            fAcumulada = fAcumulada + soluciones.get(i).puntaje;
            probabilidades.add(fAcumulada);
        }

        int cantSeleccionados = (int) (sizeSoluciones * tasaC);
        float rx = fAcumulada / cantSeleccionados;
        Random r1 = new Random();
        for (int i = 0; i < cantSeleccionados; i++) {
            float r1nextFloat = r1.nextFloat();
            float randomR = rx * i + r1nextFloat * rx;
            boolean found = false;
            for (int j = 0; j < sizeSoluciones && found == false; j++) {
                if (randomR < probabilidades.get(j)) {
                    seleccionados.add(soluciones.get(j));
                    found = true;
                }
            }
        }
    }

    String getStringResultado() {
        if (soluciones == null || soluciones.size() == 0) {
            return "empty";
        }
        return soluciones.get(0).getSolucionString();
    }

    String getStringResultado(int i) {
        if (soluciones == null || soluciones.size() == 0) {
            return "empty";
        }
        return soluciones.get(i).getSolucionString();
    }

    String getResultadoLive() {
        if (soluciones == null || soluciones.size() == 0) {
            return "empty";
        }
        return getStringResultado();
    }

    void crossover() {
        nuevosHijos = new ArrayList<Solucion>();
        int cantidadCrossover = seleccionados.size();
        Random r1 = new Random();
        for (int i = 0; i < cantidadCrossover; i++) {
            int randCromosoma = r1.nextInt(poblacion.cantidadCromosomas);
            int randCrossover = r1.nextInt(cantidadCrossover);

            Solucion solucion1 = new Solucion(seleccionados.get(i), seleccionados.get(randCrossover), randCromosoma);
            Solucion solucion2 = new Solucion(seleccionados.get(randCrossover), seleccionados.get(i), randCromosoma);
            nuevosHijos.add(solucion1);
            nuevosHijos.add(solucion2);
        }
    }

    void mutacion() {
        int sizeNuevosHijos = nuevosHijos.size();
        int cantMutaciones = 0;
        Random r1 = new Random();
        for (int k = 0; k < sizeNuevosHijos; k++) {
            for (int i = 0; i < this.poblacion.cantidadCromosomas; i++) {
                int r = r1.nextInt((int) (tasaM * 100));

                if (r == 1) {
                    cantMutaciones = cantMutaciones + 1;
                    int r2 = r1.nextInt(this.poblacion.cantidadCromosomas);
                    int cromosomaR2 = nuevosHijos.get(k).cromosoma.get(r2);
                    nuevosHijos.get(k).cromosoma.set(r2, nuevosHijos.get(k).cromosoma.get(i));
                    nuevosHijos.get(k).cromosoma.set(i, cromosomaR2);
                }
            }
        }
    }

    void agregaHijos() {
        int cantNuevosHijos = nuevosHijos.size();
        for (int i = 0; i < cantNuevosHijos; i++) {
            poblacion.soluciones.add(nuevosHijos.get(i));
        }
    }

    void busquedaLocal() {
        int cantHijos = nuevosHijos.size();
        for (int i = 0; i < cantHijos; i++) {
            nuevosHijos.get(i).localSearch(poblacion.barrasMaterial, poblacion.barrasPedido, this.tasaA);
        }
    }

    void evaluaSoluciones() {
        poblacion.evaluaSoluciones();
    }

    public static Comparator<Solucion> compareSolucion = new Comparator<Solucion>() {
        @Override
        public int compare(Solucion o1, Solucion o2) {
            if (o1.puntaje < o2.puntaje) {
                return 1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (o1.puntaje > o2.puntaje) {
                return -1;
            }
            return 0;
        }
    };

    boolean ordenado() {
        boolean resultado = true;
        for (int i = 0; i < poblacion.soluciones.size() - 1; i++) {
            resultado = resultado & (poblacion.soluciones.get(i).puntaje >= poblacion.soluciones.get(i + 1).puntaje);
        }
        return resultado;
    }

    void elitismo() {
        Collections.sort(poblacion.soluciones, compareSolucion);

        int sizeSoluciones = poblacion.soluciones.size();
        while (sizeSoluciones > poblacion.tamPoblacion) {
            poblacion.soluciones.remove(poblacion.soluciones.size() - 1);
            sizeSoluciones = poblacion.soluciones.size();
        }
    }

    void formateaMejorSolucion(Resultados resultadoG) {
        List<Material> materiales = new ArrayList<Material>();
        for (int i = 0; i < poblacion.barrasMaterial.size(); i++) {
            Material m = new Material(poblacion.barrasMaterial.get(i).largo);
            materiales.add(m);
        }
        materiales.sort(new Comparator<Material>() {
            @Override
            public int compare(Material o1, Material o2) {
                if (o1.largo < o2.largo) {
                    return 1;
                } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
                if (o1.largo > o2.largo) {
                    return -1;
                }
                return 0;
            }
        });
        int materialActual = 0;
        float largoActual = materiales.get(materialActual).largo;

        resultadoG.solucionFormateada1 = new ArrayList<Float>();
        resultadoG.solucionFormateada2 = new ArrayList<Float>();
        resultadoG.solucionFormateada3 = new ArrayList<Integer>();
        resultadoG.solucionFormateada4 = new ArrayList<Float>();

        float sumaLargos = 0;
        for (int i = 0; i < resultadoG.mejorSolucion.cromosoma.size(); i++) {
            if (largoActual >= poblacion.barrasPedido.get(resultadoG.mejorSolucion.cromosoma.get(i)).largo) {
                resultadoG.solucionFormateada1.add(materiales.get(materialActual).largo);
                resultadoG.solucionFormateada2.add(sumaLargos);
                sumaLargos = sumaLargos + poblacion.barrasPedido.get(resultadoG.mejorSolucion.cromosoma.get(i)).largo;
                resultadoG.solucionFormateada3.add(resultadoG.mejorSolucion.cromosoma.get(i));
                resultadoG.solucionFormateada4.add(poblacion.barrasPedido.get(resultadoG.mejorSolucion.cromosoma.get(i)).largo);
                largoActual = largoActual - poblacion.barrasPedido.get(resultadoG.mejorSolucion.cromosoma.get(i)).largo;
            } else {
                materialActual = materialActual + 1;
                sumaLargos = 0;
                if (materialActual < materiales.size()) {
                    largoActual = materiales.get(materialActual).largo;
                } else {
                    i = resultadoG.mejorSolucion.cromosoma.size();
                }
                i = i - 1;
            }
        }
    }
}
