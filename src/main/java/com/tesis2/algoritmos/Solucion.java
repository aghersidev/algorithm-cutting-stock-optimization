package com.tesis2.algoritmos;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

class Solucion {

    float puntaje;
    List<Integer> cromosoma;
    private int cantidadCromosomas;

    Solucion(int cantidadCromosomas) {
        cromosoma = new ArrayList<Integer>();
        this.cantidadCromosomas = cantidadCromosomas;
        for (int i = 0; i < cantidadCromosomas; i++) {
            cromosoma.add(new Integer(i));
        }
        Collections.shuffle(cromosoma);
    }

    Solucion(Solucion s1, Solucion s2, int index) {
        this.cantidadCromosomas = s2.cantidadCromosomas;
        cromosoma = new ArrayList<Integer>();
        for (int i = 0; i < index; i++) {
            cromosoma.add(new Integer(s1.cromosoma.get(i)));
        }
        for (int i = index; i < cantidadCromosomas; i++) {
            cromosoma.add(new Integer(s2.cromosoma.get(i)));
        }
        fixCromosoma();
    }

    String getSolucionString() {
        String s = "Puntaje: " + this.puntaje + ":";
        for (int i = 0; i < cantidadCromosomas; i++) {
            s = s + "[" + cromosoma.get(i) + "]";
        }
        return s;
    }

    void evalua(List<Barra> barrasPedido, List<Material> barrasMaterial) {

        int sizeBarrasPedido = barrasPedido.size();
        int cantMaterialesConsumidos = 1;
        float sumaLargos = 0;
        int materialActual = 0;
        float materialUsado = 0;
        float desperdicio = 0;

        float actLargoMaterial = barrasMaterial.get(0).largo;
        materialUsado = actLargoMaterial;
        float sumaTodosLargos = 0;

        for (int i = 0; i < sizeBarrasPedido; i++) {
            //printf("Desperdicio: %f -- LargoMaterial: %f -- LargoBarra: %f\n",desperdicio,actLargoMaterial, barrasPedido.at(cromosoma[i]).largo);
            if (i >= cromosoma.size()) {
            }
            if (actLargoMaterial - sumaLargos >= barrasPedido.get(cromosoma.get(i)).largo) {
                sumaLargos = sumaLargos + barrasPedido.get(cromosoma.get(i)).largo;
            } else {
                desperdicio = desperdicio + actLargoMaterial - sumaLargos;
                cantMaterialesConsumidos = cantMaterialesConsumidos + 1;
                materialActual = materialActual + 1;
                if (materialActual < barrasMaterial.size()) {
                    actLargoMaterial = barrasMaterial.get(materialActual).largo;
                    materialUsado = materialUsado + actLargoMaterial;
                }
                sumaTodosLargos = sumaTodosLargos + sumaLargos;
                sumaLargos = barrasPedido.get(cromosoma.get(i)).largo;
            }
        }
        puntaje = 1 - (desperdicio / sumaTodosLargos);
        if (puntaje > 1) {
            for (int i = 0; i < cromosoma.size(); i++) {
            }
            System.exit(0);
        }
    }

    private void fixCromosoma() {

        List<Integer> indices = new ArrayList<Integer>();
        int cantidadIndices = 0;
        for (int i = 0; i < cantidadCromosomas; i++) {
            indices.add(-1);
        }

        Random r1 = new Random();
        for (int i = 0; i < cantidadCromosomas; i++) {
            if (indices.get(cromosoma.get(i)) == -1) {
                indices.set(cromosoma.get(i), i);
                cantidadIndices = cantidadIndices + 1;
            } else {
                int r = r1.nextInt(2);
                if (r == 0) {
                    cromosoma.set(i, -1);
                } else {
                    cromosoma.set(indices.get(cromosoma.get(i)), -1);
                    indices.set(cromosoma.get(i), i);
                }
            }
        }
        int indiceFaltantes = 0;
        List<Integer> faltantes = new ArrayList<Integer>();
        for (int i = 0; i < cantidadCromosomas; i++) {
            if (indices.get(i) == -1) {
                faltantes.add(i);
                indiceFaltantes = indiceFaltantes + 1;
            }
        }
        Collections.shuffle(faltantes);
        int curFaltantes = 0;
        for (int i = 0; i < cantidadCromosomas; i++) {
            if (cromosoma.get(i) == -1) {
                cromosoma.set(i, faltantes.get(curFaltantes));
                curFaltantes = curFaltantes + 1;
            }
        }
    }

    int llena(List<Integer> nuevoCromosoma, int a, int ax, List<Integer> b, int bx, int largoNuevo
    ) {
        for (int i = 0; i < ax; i++) {
            if (cromosoma.get(a + i) >= 0) {
                nuevoCromosoma.add(new Integer(cromosoma.get(a + i)));
                largoNuevo = largoNuevo + 1;
            }
        }
        return largoNuevo;
    }
    public static Comparator<Bloque> comparaTamanios = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            if (o1.desperdicio < o2.desperdicio) {
                return 1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (o1.desperdicio > o2.desperdicio) {
                return -1;
            }
            return 0;
        }
    };

    void localSearchOriginal(List<Material> barrasMaterial, List<Barra> barrasPedido, float tasaA) {
        List<Bloque> bloques = new ArrayList<Bloque>();
        float sumaLargos = 0;
        int materialActual = 0;

        float actLargoMaterial = barrasMaterial.get(materialActual).largo;
        float desperdicioTotal = 0;
        int cantidadGenes = 1;
        int totalGenes = 0;

        int anterior = 0;

        Bloque bActual = new Bloque(anterior, cantidadGenes, barrasPedido.get(cromosoma.get(0)).largo, actLargoMaterial - barrasPedido.get(cromosoma.get(0)).largo);
        for (int i = 0; i < this.cantidadCromosomas; i++) {
            if ((actLargoMaterial - bActual.largoBloque) >= barrasPedido.get(cromosoma.get(i)).largo) {
                bActual.largoEnGenes = bActual.largoEnGenes + 1;
                bActual.largoBloque = bActual.largoBloque + barrasPedido.get(cromosoma.get(i)).largo;
                bActual.desperdicio = bActual.desperdicio - barrasPedido.get(cromosoma.get(i)).largo;
            } else {
                bloques.add(bActual);
                materialActual = materialActual + 1;
                actLargoMaterial = barrasMaterial.get(materialActual).largo;
                anterior = anterior + bActual.largoEnGenes;
                Bloque bActual2 = new Bloque(anterior, 1, barrasPedido.get(cromosoma.get(i)).largo, actLargoMaterial - barrasPedido.get(cromosoma.get(i)).largo);
                bActual = bActual2;
            }
        }
        int cant = 0;
        for (int i = 0; i < bloques.size(); i++) {
            Bloque b = bloques.get(i);
            cant = cant + b.largoEnGenes;
        }

        if (cantidadCromosomas - totalGenes > 0 && false) {
            Bloque bloqueTuple = new Bloque(anterior, cantidadCromosomas - totalGenes, sumaLargos, actLargoMaterial - sumaLargos);
            bloques.add(bloqueTuple);
        }
        // Hasta aqui, tengo los bloques, tengo los tamanios y largos de los bloques y los desperdicios.
        // [PunteroLugar][LargoEnGenes][LargoBloque][Desperdicio]
        // Lo que hay q hacer es llenar los Desperdicios con LargoBloque
        List<Bloque> set3 = bloques;

        Collections.sort(set3, comparaTamanios); // Mas a menos
        List<Integer> nuevoCromosoma = new ArrayList<Integer>();
        int largoActual = 0;
        int cantidadTotal = 0;

        Random rand1 = new Random();
        for (int i = 0; i < set3.size(); i++) {
            if (set3.get(i).desperdicio >= 0) {
                largoActual = llena(nuevoCromosoma, set3.get(i).punteroLugar, set3.get(i).largoEnGenes, null, 0, largoActual);
                for (int j = i + 1; j < set3.size(); j++) {
                    if (set3.get(j).desperdicio >= 0) {
                        for (int k = 0; k < set3.get(j).largoEnGenes; k++) {

                            int puntero = set3.get(j).punteroLugar + k;
                            int cromosomaPuntero = cromosoma.get(puntero);
                            if (cromosoma.get(puntero) > -1
                                    && set3.get(i).desperdicio >= barrasPedido.get(cromosoma.get(set3.get(j).punteroLugar + k)).largo
                                    && cromosoma.get(puntero) > -1) {
                                if (rand1.nextFloat() < tasaA) {
                                    largoActual = llena(nuevoCromosoma, set3.get(j).punteroLugar + k, 1, null, 0, largoActual);
                                    cromosoma.set(set3.get(j).punteroLugar + k, -1);
                                    cantidadTotal = cantidadTotal + 1;
                                }
                            }
                        }
                    } else {
                        largoActual = llena(nuevoCromosoma, set3.get(j).punteroLugar, set3.get(j).largoEnGenes, null, 0, largoActual);
                    }
                }
            }
        }

        if (cantidadTotal == 0 && false) {

            return;
        }
        cromosoma = nuevoCromosoma;
        fixCromosoma();
    }
    public static Comparator<Bloque> comparaDesperdicio = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            if (o1.desperdicio < o2.desperdicio) {
                return -1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (o1.desperdicio > o2.desperdicio) {
                return 1;
            }
            return 0;
        }
    };
    public static Comparator<Bloque> comparaT = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            if (o1.largoBloque < o2.largoBloque) {
                return -1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (o1.largoBloque > o2.largoBloque) {
                return 1;
            }
            return 0;
        }
    };
    public static Comparator<Bloque> comparaBMIMenos = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            float b1 = o1.desperdicio / o1.largoBloque / o1.largoBloque;
            float b2 = o2.desperdicio / o2.largoBloque / o2.largoBloque;

            if (b1 > b2) {
                return -1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (b1 < b2) {
                return 1;
            }
            return 0;
        }
    };
    public static Comparator<Bloque> comparaPorMas = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            float b1 = o1.desperdicio / o1.largoBloque;
            float b2 = o2.desperdicio / o2.largoBloque;

            if (b1 > b2) {
                return -1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (b1 < b2) {
                return 1;
            }
            return 0;
        }
    };
    public static Comparator<Bloque> comparaPorMenos = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            float b1 = o1.desperdicio / o1.largoBloque;
            float b2 = o2.desperdicio / o2.largoBloque;

            if (b1 > b2) {
                return -1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (b1 < b2) {
                return 1;
            }
            return 0;
        }
    };
    public static Comparator<Bloque> comparaBMIMas = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            float b1 = o1.desperdicio / o1.largoBloque / o1.largoBloque;
            float b2 = o2.desperdicio / o2.largoBloque / o2.largoBloque;

            if (b1 < b2) {
                return -1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (b1 > b2) {
                return 1;
            }
            return 0;
        }
    };
    public static Comparator<Bloque> comparaTotal = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            float b1 = o1.desperdicio + o1.largoBloque;
            float b2 = o2.desperdicio + o2.largoBloque;

            if (b1 < b2) {
                return -1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (b1 > b2) {
                return 1;
            }
            return 0;
        }
    };

    public static Comparator<Bloque> comparaTamanios2 = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            if (o1.desperdicio < o2.desperdicio) {
                return 1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (o1.desperdicio > o2.desperdicio) {
                return -1;
            }
            return 0;
        }
    };
    public static Comparator<Bloque> comparaTamanios22 = new Comparator<Bloque>() {
        @Override
        public int compare(Bloque o1, Bloque o2) {
            if (o1.desperdicio > o2.desperdicio) {
                return 1;
            } // Devuelve un entero positivo si la altura de o1 es mayor que la de o2
            if (o1.desperdicio < o2.desperdicio) {
                return -1;
            }
            return 0;
        }
    };

    void localSearch(List<Material> barrasMaterial, List<Barra> barrasPedido, float tasaA) {
        List<Bloque> bloques = new ArrayList<Bloque>();
        float sumaLargos = 0;
        int materialActual = 0;

        float actLargoMaterial = barrasMaterial.get(materialActual).largo;
        float desperdicioTotal = 0;
        int cantidadGenes = 1;
        int totalGenes = 0;

        int anterior = 0;

        Bloque bActual = new Bloque(anterior, cantidadGenes, barrasPedido.get(cromosoma.get(0)).largo, actLargoMaterial - barrasPedido.get(cromosoma.get(0)).largo);
        for (int i = 0; i < this.cantidadCromosomas; i++) {
            if ((actLargoMaterial - bActual.largoBloque) >= barrasPedido.get(cromosoma.get(i)).largo) {
                bActual.largoEnGenes = bActual.largoEnGenes + 1;
                bActual.largoBloque = bActual.largoBloque + barrasPedido.get(cromosoma.get(i)).largo;
                bActual.desperdicio = bActual.desperdicio - barrasPedido.get(cromosoma.get(i)).largo;
            } else {
                bloques.add(bActual);
                materialActual = materialActual + 1;
                actLargoMaterial = barrasMaterial.get(materialActual).largo;
                anterior = anterior + bActual.largoEnGenes;
                Bloque bActual2 = new Bloque(anterior, 1, barrasPedido.get(cromosoma.get(i)).largo, actLargoMaterial - barrasPedido.get(cromosoma.get(i)).largo);
                bActual = bActual2;
            }
        }
        int cant = 0;
        for (int i = 0; i < bloques.size(); i++) {
            Bloque b = bloques.get(i);
            cant = cant + b.largoEnGenes;
        }

        if (cantidadCromosomas - totalGenes > 0 && false) {
            Bloque bloqueTuple = new Bloque(anterior, cantidadCromosomas - totalGenes, sumaLargos, actLargoMaterial - sumaLargos);
            bloques.add(bloqueTuple);
        }
        // Hasta aqui, tengo los bloques, tengo los tamanios y largos de los bloques y los desperdicios.
        // [PunteroLugar][LargoEnGenes][LargoBloque][Desperdicio]
        // Lo que hay q hacer es llenar los Desperdicios con LargoBloque
        List<Bloque> set3 = bloques;

        Collections.sort(set3, comparaDesperdicio); // Mas a menos
        List<Integer> nuevoCromosoma = new ArrayList<Integer>();
        int largoActual = 0;
        int cantidadTotal = 0;
        List<Bloque> set4 = bloques;

        Collections.sort(set4, comparaT); // Mas a menos

        Collections.sort(set3, comparaBMIMenos); // Mas a menos
        Collections.sort(set4, comparaBMIMenos); // Mas a menos

// LocalSearch3 set3 comparaDesperdicio +-, set4 comparaT +-
//        Collections.sort(set3, comparaBMIMas); // Mas a menos
        //    Collections.sort(set4, comparaBMIMenos); // Mas a menos
        // 0.861
/*                Collections.sort(set3, comparaBMIMenos); // Mas a menos
        Collections.sort(set4, comparaBMIMenos); // Mas a menos
 0.867*/
 /* Collections.sort(set3, comparaBMIMas); // Mas a menos
        Collections.sort(set4, comparaBMIMas); // Mas a menos
0-847*/
 /*   Collections.sort(set3, comparaBMIMenos); // Mas a menos
        Collections.sort(set4, comparaPorMas); // Mas a menos
 86*/
 /*Collections.sort(set3, comparaBMIMenos); // Mas a menos
        Collections.sort(set4, comparaPorMenos); // Mas a menos
        867*/
 /*Collections.sort(set3, comparaBMIMenos); // Mas a menos
        Collections.sort(set4, comparaBMIMas); // Mas a menos
        86*/
 /*CCollections.sort(set3, comparaPorMenos); // Mas a menos
        Collections.sort(set4, comparaPorMenos); // Mas a menos
         865*/
 /*Collections.sort(set3, comparaBMIMenos); // Mas a menos
        Collections.sort(set4, comparaTotal); // Mas a menos
         */
        Random rand1 = new Random();
        for (int i = 0; i < set3.size(); i++) {
            if (set3.get(i).desperdicio >= 0) {
                largoActual = llena(nuevoCromosoma, set3.get(i).punteroLugar, set3.get(i).largoEnGenes, null, 0, largoActual);
                for (int j = set4.size() - 1; j > 0; j--) {
                    if (set3.get(j).desperdicio >= 0 && !set3.get(i).equals(set4.get(j))) {
                        for (int k = 0; k < set3.get(j).largoEnGenes; k++) {

                            int puntero = set3.get(j).punteroLugar + k;
                            int cromosomaPuntero = cromosoma.get(puntero);
                            if (cromosoma.get(puntero) > -1
                                    && set3.get(i).desperdicio >= barrasPedido.get(cromosoma.get(set3.get(j).punteroLugar + k)).largo
                                    && cromosoma.get(puntero) > -1) {
                                if (rand1.nextFloat() < tasaA) {
                                    largoActual = llena(nuevoCromosoma, set3.get(j).punteroLugar + k, 1, null, 0, largoActual);
                                    cromosoma.set(set3.get(j).punteroLugar + k, -1);
                                    cantidadTotal = cantidadTotal + 1;
                                }
                            }
                        }
                    } else {
                        largoActual = llena(nuevoCromosoma, set3.get(j).punteroLugar, set3.get(j).largoEnGenes, null, 0, largoActual);
                    }
                }
            }
        }

        if (cantidadTotal == 0 && false) {

            return;
        }
        cromosoma = nuevoCromosoma;
        fixCromosoma();
    }

    void localSearch2(List<Material> barrasMaterial, List<Barra> barrasPedido, float tasaA) {
        List<Bloque> bloques = new ArrayList<Bloque>();
        float sumaLargos = 0;
        int materialActual = 0;

        float actLargoMaterial = barrasMaterial.get(materialActual).largo;
        float desperdicioTotal = 0;
        int cantidadGenes = 1;
        int totalGenes = 0;

        int anterior = 0;

        Bloque bActual = new Bloque(anterior, cantidadGenes, barrasPedido.get(cromosoma.get(0)).largo, actLargoMaterial - barrasPedido.get(cromosoma.get(0)).largo);
        for (int i = 0; i < this.cantidadCromosomas; i++) {
            if ((actLargoMaterial - bActual.largoBloque) >= barrasPedido.get(cromosoma.get(i)).largo) {
                bActual.largoEnGenes = bActual.largoEnGenes + 1;
                bActual.largoBloque = bActual.largoBloque + barrasPedido.get(cromosoma.get(i)).largo;
                bActual.desperdicio = bActual.desperdicio - barrasPedido.get(cromosoma.get(i)).largo;
            } else {
                bloques.add(bActual);
                materialActual = materialActual + 1;
                actLargoMaterial = barrasMaterial.get(materialActual).largo;
                anterior = anterior + bActual.largoEnGenes;
                Bloque bActual2 = new Bloque(anterior, 1, barrasPedido.get(cromosoma.get(i)).largo, actLargoMaterial - barrasPedido.get(cromosoma.get(i)).largo);
                bActual = bActual2;
            }
        }
        int cant = 0;
        for (int i = 0; i < bloques.size(); i++) {
            Bloque b = bloques.get(i);
            cant = cant + b.largoEnGenes;
        }

        if (cantidadCromosomas - totalGenes > 0 && false) {
            Bloque bloqueTuple = new Bloque(anterior, cantidadCromosomas - totalGenes, sumaLargos, actLargoMaterial - sumaLargos);
            bloques.add(bloqueTuple);
        }
        // Hasta aqui, tengo los bloques, tengo los tamanios y largos de los bloques y los desperdicios.
        // [PunteroLugar][LargoEnGenes][LargoBloque][Desperdicio]
        // Lo que hay q hacer es llenar los Desperdicios con LargoBloque
        List<Bloque> set3 = bloques;

        Collections.sort(set3, comparaDesperdicio); // Mas a menos
        List<Integer> nuevoCromosoma = new ArrayList<Integer>();
        int largoActual = 0;
        int cantidadTotal = 0;

        Random rand1 = new Random();
        for (int i = 0; i < set3.size(); i++) {
            if (set3.get(i).desperdicio >= 0) {
                largoActual = llena(nuevoCromosoma, set3.get(i).punteroLugar, set3.get(i).largoEnGenes, null, 0, largoActual);
                for (int j = set3.size() - 1; j > i; j--) {
                    if (set3.get(j).desperdicio >= 0) {
                        for (int k = 0; k < set3.get(j).largoEnGenes; k++) {

                            int puntero = set3.get(j).punteroLugar + k;
                            int cromosomaPuntero = cromosoma.get(puntero);
                            if (cromosoma.get(puntero) > -1
                                    && set3.get(i).desperdicio >= barrasPedido.get(cromosoma.get(set3.get(j).punteroLugar + k)).largo
                                    && cromosoma.get(puntero) > -1) {
                                if (rand1.nextFloat() < tasaA) {
                                    largoActual = llena(nuevoCromosoma, set3.get(j).punteroLugar + k, 1, null, 0, largoActual);
                                    cromosoma.set(set3.get(j).punteroLugar + k, -1);
                                    cantidadTotal = cantidadTotal + 1;
                                }
                            }
                        }
                    } else {
                        largoActual = llena(nuevoCromosoma, set3.get(j).punteroLugar, set3.get(j).largoEnGenes, null, 0, largoActual);
                    }
                }
            }
        }

        if (cantidadTotal == 0 && false) {

            return;
        }
        cromosoma = nuevoCromosoma;
        fixCromosoma();
    }

}
