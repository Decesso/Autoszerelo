/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package autoszerelo.gui.dialogs;

import autoszerelo.database.controllers.JobJpaController;
import autoszerelo.database.entities.Job;
import autoszerelo.database.entities.Workers;
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

/**
 *
 * @author dmolnar
 */
public class DeleteJobDialog extends JDialog {
    private final JobJpaController controller;
    DefaultComboBoxModel jobModel;
    private boolean deleted = false;
    private boolean closed = false;
    private JComboBox tf0;
    private JLabel l0;
    public DeleteJobDialog() {
        this.controller = DatabaseEngine.getJobControllerInstance();
        setSize(500, 50);
        setTitle("Munkalap törlése");
        setLayout(new GridLayout(1, 3));
        
        l0 = new JLabel("Id");
        List<Job> jobs = controller.findJobEntities();
        jobModel = new DefaultComboBoxModel();
        tf0 = new JComboBox(jobModel);
        for(Job j: jobs) {
            jobModel.addElement(j);
        }
        add(l0);
        add(tf0);
        JButton button = new JButton("Torles");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent ae) {
                closed = true;
                //elĂŠg csak bezĂĄrni mert a gui automatikusan megvizsgĂĄlja az ĂŠrtĂŠkeit
                deleted = true;
                setVisible(false);
                
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
    public Integer getId() {
        return ((Job)jobModel.getElementAt(tf0.getSelectedIndex())).getId();
    }
    public boolean isDeleted() {
        return deleted;
    }
    public boolean isClosed(){
        return closed;
    }
        
}
