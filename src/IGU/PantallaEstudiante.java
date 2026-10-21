/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package IGU;

import Logica.Estudiante;
import Logica.GestorDatos;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class PantallaEstudiante extends javax.swing.JFrame {

    /**
     * Creates new form PantallaEstudiante
     */
    private Estudiante estudiante;
    private JTable tablaOfertadas, tablaInscritas;
    private DefaultTableModel modeloOfertadas, modeloInscritas;

    public PantallaEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
        initUI();
        cargarTodasLasTablas();
    }

    public PantallaEstudiante() {
        initUI();
    }

    private void initUI() {
        setTitle("Panel Estudiante - " + (estudiante != null ? estudiante.getNombre() : ""));
        setSize(1028, 608);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        // Título
        JLabel lblTitulo = new JLabel("MATRÍCULA Y ASIGNATURAS DE: " + (estudiante != null ? estudiante.getNombre().toUpperCase() : ""), SwingConstants.CENTER);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 16));
        add(lblTitulo, BorderLayout.NORTH);

        // Panel Central Dividido en 2 Tablas
        JPanel panelTablas = new JPanel(new GridLayout(1, 2, 15, 10));

        // --- TABLA IZQUIERDA: Materias Ofertadas ---
        JPanel panelIzquierdo = new JPanel(new BorderLayout(5, 5));
        panelIzquierdo.setBorder(BorderFactory.createTitledBorder("Materias Disponibles"));

        String[] colOfertadas = {"Código", "Nombre", "Totales", "Restantes", "Estado"};
        modeloOfertadas = new DefaultTableModel(colOfertadas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaOfertadas = new JTable(modeloOfertadas);
        panelIzquierdo.add(new JScrollPane(tablaOfertadas), BorderLayout.CENTER);

        JButton btnInscribir = new JButton("Inscribir Seleccionada ");
        btnInscribir.setFont(new Font("Arial", Font.BOLD, 12));
        panelIzquierdo.add(btnInscribir, BorderLayout.SOUTH);

        // --- TABLA DERECHA: Materias Inscritas por el Estudiante ---
        JPanel panelDerecho = new JPanel(new BorderLayout(5, 5));
        panelDerecho.setBorder(BorderFactory.createTitledBorder("Mis Materias Inscritas"));

        String[] colInscritas = {"Código", "Nombre"};
        modeloInscritas = new DefaultTableModel(colInscritas, 0) {
            @Override
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tablaInscritas = new JTable(modeloInscritas);
        panelDerecho.add(new JScrollPane(tablaInscritas), BorderLayout.CENTER);

        JButton btnCancelar = new JButton(" Eliminar / Cancelar Materia");
        btnCancelar.setFont(new Font("Arial", Font.BOLD, 12));
        panelDerecho.add(btnCancelar, BorderLayout.SOUTH);

        panelTablas.add(panelIzquierdo);
        panelTablas.add(panelDerecho);
        add(panelTablas, BorderLayout.CENTER);

        // Panel Inferior: Botón Volver
        JPanel panelInferior = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnVolver = new JButton("Volver al Inicio");
        panelInferior.add(btnVolver);
        add(panelInferior, BorderLayout.SOUTH);

        // Eventos de los Botones
        btnInscribir.addActionListener(e -> inscribirMateria());
        btnCancelar.addActionListener(e -> cancelarMateria());
        btnVolver.addActionListener(e -> {
            new PantallaPrincipal().setVisible(true);
            this.dispose();
        });
    }

    private void cargarTodasLasTablas() {
        // 1. Cargar Materias Ofertadas
        modeloOfertadas.setRowCount(0);
        Object[][] datosOfertadas = GestorDatos.getMatrizMaterias();
        for (Object[] fila : datosOfertadas) {
            modeloOfertadas.addRow(fila);
        }

        // 2. Cargar Materias Inscritas del estudiante actual
        modeloInscritas.setRowCount(0);
        if (estudiante != null) {
            Object[][] datosInscritas = GestorDatos.getMateriasInscritasEstudiante(estudiante.getNombre());
            for (Object[] fila : datosInscritas) {
                modeloInscritas.addRow(fila);
            }
        }
    }

    private void inscribirMateria() {
        int filaSel = tablaOfertadas.getSelectedRow();
        if (filaSel >= 0) {
            String codigo = modeloOfertadas.getValueAt(filaSel, 0).toString();
            String nombreMateria = modeloOfertadas.getValueAt(filaSel, 1).toString();

            if (estudiante != null) {
                // Validación 1: Verificar duplicados
                if (estudiante.yaEstaMatriculado(codigo)) {
                    JOptionPane.showMessageDialog(this, "¡Ya tienes inscrita la materia " + nombreMateria + "! No puedes matricularla dos veces.", "Materia Duplicada", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                // Validación 2: Descontar cupo e inscribir
                if (estudiante.matricularMateria(codigo)) {
                    JOptionPane.showMessageDialog(this, "¡Materia " + nombreMateria + " inscrita con éxito!", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarTodasLasTablas(); // Refresca ambas tablas
                } else {
                    JOptionPane.showMessageDialog(this, "No se pudo inscribir. No hay cupos disponibles.", "Sin Cupo", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una materia de la lista de disponibles.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
    }

    private void cancelarMateria() {
        int filaSel = tablaInscritas.getSelectedRow();
        if (filaSel >= 0) {
            String codigo = modeloInscritas.getValueAt(filaSel, 0).toString();
            String nombreMateria = modeloInscritas.getValueAt(filaSel, 1).toString();

            int confirm = JOptionPane.showConfirmDialog(this, "¿Seguro que desea eliminar/cancelar la materia " + nombreMateria + "?", "Confirmar Cancelación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (estudiante != null && estudiante.cancelarMateria(codigo)) {
                    JOptionPane.showMessageDialog(this, "Materia " + nombreMateria + " eliminada de tu horario. El cupo ha sido liberado.", "Cancelada", JOptionPane.INFORMATION_MESSAGE);
                    cargarTodasLasTablas(); // Refresca las dos tablas
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Seleccione una materia de tu lista de inscritas para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
        }
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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1028, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 608, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

   

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel jPanel1;
    // End of variables declaration//GEN-END:variables
}
