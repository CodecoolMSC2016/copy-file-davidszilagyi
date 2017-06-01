package form;

import main.Wizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by David Szilagyi on 2017. 06. 01..
 */
public class Form extends JFrame{
    JPanel thePanel, textPanel, progressBar, buttonsPanel;
    JLabel sourceLabel, destLabel;
    JTextField sourceField, destField;
    JButton startButton, stopButton;
    JProgressBar pb;
    lForButton action;
    Thread thread = null;

    public Form() {
        this.setTitle("Copy a file");
        this.setSize(500, 200);
        this.setLocationRelativeTo(null);
        this.thePanel = new JPanel();
        this.textPanel = new JPanel();
        this.textPanel.setLayout(new GridLayout(2,2));
        this.progressBar = new JPanel();
        this.buttonsPanel = new JPanel();
        this.buttonsPanel.setLayout(new GridLayout(1,2));
        this.sourceLabel = new JLabel("Source:");
        this.destLabel = new JLabel("Destination: ");
        this.sourceField = new JTextField("",25);
        this.destField = new JTextField("", 25);
        this.startButton = new JButton("Copy");
        this.stopButton = new JButton("Stop");
        this.stopButton.setEnabled(false);
        this.pb = new JProgressBar(0, 100);
        this.textPanel.add(sourceLabel);
        this.textPanel.add(sourceField);
        this.textPanel.add(destLabel);
        this.textPanel.add(destField);
        this.progressBar.add(pb);
        this.buttonsPanel.add(startButton);
        this.buttonsPanel.add(stopButton);
        this.thePanel.add(textPanel);
        this.thePanel.add(progressBar);
        this.thePanel.add(buttonsPanel);
        this.action = new lForButton();
        startButton.addActionListener(action);
        stopButton.addActionListener(action);
        this.add(thePanel);
        this.setVisible(true);
    }
    private class lForButton implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            switch (e.getActionCommand()) {
                case "Copy":
                    String from = sourceField.getText();
                    String to = destField.getText();
                    changeFields();
                    Wizard wizard = new Wizard(from, to, pb);
                    thread = new Thread(wizard);
                    thread.start();
                    break;
                case "Stop":
                    thread.interrupt();
                    JOptionPane.showMessageDialog(Form.this, "Process stopped", "Stopped", JOptionPane.ERROR_MESSAGE);
                    changeFields();
                    break;
                default:
                    break;
            }

        }
    }

    private void changeFields() {
        sourceField.setEnabled(!sourceField.isEnabled());
        destField.setEnabled(!destField.isEnabled());
        startButton.setEnabled(!startButton.isEnabled());
        stopButton.setEnabled(!stopButton.isEnabled());
        pb.setValue(0);
        thread = null;
    }
}
