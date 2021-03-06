/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoszerelo.gui.dialogs;

import autoszerelo.database.controllers.JobJpaController;
import autoszerelo.database.entities.Job;
import autoszerelo.database.util.DatabaseEngine;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

/**
 *
 * @author dmolnar
 */
public class FinalizeJobDialog extends JDialog {
    private final JobJpaController controller;
    DefaultComboBoxModel jobModel;
    private boolean finalized = false;
    private boolean closed = false;
    private String dialogError = "";
    private JComboBox idBox;
    private final JLabel idLabel;
    public FinalizeJobDialog() {
        this.controller = DatabaseEngine.getJobControllerInstance();
        setSize(500, 50);
        setTitle("Munkalap veglegesitese");
        setLayout(new GridLayout(1, 3));
        
        idLabel = new JLabel("Id");
        List<Job> jobs = controller.findOpenJobEntities();
        if(jobs.isEmpty()) {
            dialogError = "Nincs lezárható munkalap";
            showErrorDialog();
            this.dispatchEvent(new WindowEvent(this, WindowEvent.WINDOW_CLOSING));
            return;
        }
        jobModel = new DefaultComboBoxModel();
        idBox = new JComboBox(jobModel);
        for(Job j: jobs) {
            jobModel.addElement(j);
        }
        add(idLabel);
        add(idBox);
        JButton button = new JButton("Veglegesit");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                if(formValid()) {
                    closed = true;
                    finalized = true;
                    setVisible(false);
                } else {
                    showErrorDialog();
                }
            }
        });
        add(button);
        
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent winEvt) {
                    closed = true;
                    setVisible(false);
            }
        });
        
        setModal(true);
        setVisible(true);
    }
    
    private void showErrorDialog() {
        JOptionPane.showMessageDialog(this,
        dialogError,
        "Véglegesítési hiba",
        JOptionPane.ERROR_MESSAGE);
    }

    private boolean formValid() {
        Job j = (Job)jobModel.getElementAt(idBox.getSelectedIndex());
        if(j.getAddress().isEmpty()) {
            dialogError = "A cím mező üres, nem lehet lezárni a kiválasztott munkalapot";
            return false;
        }
        if(j.getClientname().isEmpty()) {
            dialogError = "A kliens neve mező üres, nem lehet lezárni a kiválasztott munkalapot";
            return false;
        }
        if(j.getLength()==0) {
            dialogError = "A munka hossza 0, nem lehet lezárni a kiválasztott munkalapot";
            return false;
        }
        if(j.getLicenseNo().isEmpty()) {
            dialogError = "A rendszám mező üres, nem lehet lezárni a kiválasztott munkalapot";
            return false;
        }
        if(j.getState()==true) {
            dialogError = "A kiválasztott munkalap már le van zárva";
            return false;
        }
        return true;
    }    
    public Integer getId() {
        return ((Job)jobModel.getElementAt(idBox.getSelectedIndex())).getId();
    }
    public boolean isFinalized() {
        return finalized;
    }
    public boolean isClosed(){
        return closed;
    }
    
}
