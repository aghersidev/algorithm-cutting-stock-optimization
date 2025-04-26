package com.tesis2.algoritmos;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import javafx.scene.chart.AreaChart;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingWorker;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.QuickChart;
import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.Styler;

public class DialogGenetico extends javax.swing.JDialog {

    /**
     * Creates new form NewJDialog
     */
    VentanaAlgoritmos va;
    SwingWorker<String, String> algoritmoGenetico;

    String resultado;
    Resultados resultadoG;
    CategoryChart chart1;
    XChartPanel xcp1;

    public DialogGenetico(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        va  = (VentanaAlgoritmos) parent;
        this.setTitle("Algoritmo Genetico");
        // Create Chart
        chart1 = new CategoryChartBuilder().width(800).height(600).title("Mejor Solucion").xAxisTitle("Material").yAxisTitle("Largo").build();
        // Customize Chart
        chart1.getStyler().setLegendPosition(Styler.LegendPosition.InsideNE);

        chart1.getStyler().setAvailableSpaceFill(.96);
        chart1.getStyler().setOverlapped(true);
        double[] aX = {1};
        double[] bY = {4};
        chart1.addSeries("Desperdicio", aX, bY);
        double[] aX2 = {1};
        double[] bY2 = {5};
        chart1.addSeries("Usado", aX2, bY2);
        xcp1 = new XChartPanel((CategoryChart) chart1);
        jTabbedPane1.addTab("Mejor Solucion Actual", xcp1);
        init2();
        algoritmoGenetico.execute();  // start the worker thread
    }

    void init2() {
        algoritmoGenetico = new SwingWorker<String, String>() {

            @Override
            protected String doInBackground() throws Exception {
                // Cargas los datos
                va.evolucionGenetico.generacionActual = 0;
                resultadoG.generaciones = new double[va.evolucionGenetico.cantidadGeneraciones];
                resultadoG.promedioSoluciones = new double[va.evolucionGenetico.cantidadGeneraciones];
                resultadoG.puntajesMejoresSoluciones = new double[va.evolucionGenetico.cantidadGeneraciones];

                while (va.evolucionGenetico.generacionActual < va.evolucionGenetico.cantidadGeneraciones) {
                    va.evolucionGenetico.generacionActual = va.evolucionGenetico.generacionActual + 1;
                    jLabel7.setText(Integer.toString(va.evolucionGenetico.generacionActual));
                    // va.evolucion.generacionActual = 0;
                    jLabel8.setText("Seleccion");
                    va.evolucionGenetico.seleccion();

                    jLabel8.setText("Crossver");
                    va.evolucionGenetico.crossover();
                    jLabel8.setText("Mutacion");
                    va.evolucionGenetico.mutacion();

                    jLabel8.setText("Agrega Hijos");
                    va.evolucionGenetico.agregaHijos();
                    jLabel8.setText("Evalua Soluciones");
                    va.evolucionGenetico.evaluaSoluciones();
                    jLabel8.setText("Elitismo");
                    va.evolucionGenetico.elitismo();

                    jLabel8.setText("Ready");
                    resultadoG.generaciones[va.evolucionGenetico.generacionActual - 1] = va.evolucionGenetico.generacionActual + 1;
                    resultadoG.puntajesMejoresSoluciones[va.evolucionGenetico.generacionActual - 1] = va.evolucionGenetico.poblacion.soluciones.get(0).puntaje;
                    resultadoG.promedioSoluciones[va.evolucionGenetico.generacionActual - 1] = va.evolucionGenetico.poblacion.getPromedioSoluciones();

                    resultadoG.mejorSolucion = va.evolucionGenetico.poblacion.getMejor();
                    va.evolucionGenetico.formateaMejorSolucion(resultadoG);
                    publish(resultadoG.resultadoString());

                    try {
                        Thread.sleep(10);
                    } catch (Exception e) {

                    }
                }

                resultadoG.soluciones = va.evolucionGenetico.poblacion.getDoubleSoluciones();
                resultadoG.indicesSoluciones = new double[resultadoG.soluciones.length];
                for (int i = 0; i < resultadoG.soluciones.length; i++) {
                    resultadoG.indicesSoluciones[i] = i + 1;
                }
                resultadoG.mejorSolucion = va.evolucionGenetico.poblacion.getMejor();
                va.evolucionGenetico.formateaMejorSolucion(resultadoG);
                publish(resultadoG.resultadoString());
                String s = va.evolucionGenetico.getStringResultado(0);
                return s;
            }

            @Override
            protected void done() {
                try {
                    String finalResult = get();
                    resultado = finalResult;
                    jButton1.setEnabled(true);
                    jButton2.setEnabled(true);
                    String latestResult = resultadoG.resultadoString();

                    String[] splitResult = latestResult.split(",");
                    double[] indices = new double[splitResult.length / 2];
                    double[] new1X = new double[splitResult.length / 2];
                    double[] new1Y = new double[splitResult.length / 2];
                    for (int i = 0; i < splitResult.length / 2; i++) {
                        indices[i] = i;
                        new1X[i] = Double.parseDouble(splitResult[i]);
                        new1Y[i] = Double.parseDouble(splitResult[i + splitResult.length / 2]);
                    }
                    // System
                    chart1.updateCategorySeries("Desperdicio", indices, new1X, null);
                    chart1.updateCategorySeries("Usado", indices, new1Y, null);
                    xcp1.revalidate();
                    xcp1.repaint();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void process(java.util.List<String> chunks) {
                // Get the latest result from the list
                String latestResult = chunks.get(chunks.size() - 1);

                String[] splitResult = latestResult.split(",");
                double[] indices = new double[splitResult.length / 2];
                

                double[] new1X = new double[splitResult.length / 2];
                double[] new1Y = new double[splitResult.length / 2];
                for (int i = 0; i < splitResult.length / 2; i++) {
                    indices[i] = i;
                    new1X[i] = Double.parseDouble(splitResult[i]);
                    new1Y[i] = Double.parseDouble(splitResult[i + splitResult.length / 2]);
                }
                // System
                chart1.updateCategorySeries("Desperdicio", indices, new1X, null);
                chart1.updateCategorySeries("Usado", indices, new1Y, null);
                xcp1.revalidate();
                xcp1.repaint();
            }
        };
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jTabbedPane1 = new javax.swing.JTabbedPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);

        jLabel1.setText("Generacion Actual");

        jLabel3.setText("Fase");

        jLabel7.setText("jLabel7");

        jLabel8.setText("jLabel8");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 120, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel8))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(jLabel7))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(jLabel8))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton1.setText("Volver");
        jButton1.setEnabled(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("Reporte");
        jButton2.setEnabled(false);
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton1)
                .addGap(21, 21, 21))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jTabbedPane1))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(7, 7, 7)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jButton1)
                    .addComponent(jButton2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTabbedPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 191, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        va.setGenetico(resultado);
        this.dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        Reporte r;
        r = new Reporte(this, "Resultados", ModalityType.MODELESS, this.resultadoG);
        r.setVisible(true);
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(DialogGenetico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(DialogGenetico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(DialogGenetico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(DialogGenetico.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the dialog */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DialogGenetico dialog = new DialogGenetico(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane jTabbedPane1;
    // End of variables declaration//GEN-END:variables
}
