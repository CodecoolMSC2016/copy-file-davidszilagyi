package form;

import main.Wizard;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by David Szilagyi on 2017. 06. 01..
 */
public class Form extends JFrame {
    JPanel thePanel;

    public Form() {
        this.setTitle("Copy a file");
        this.setSize(300, 400);
        this.setLocationRelativeTo(null);
        this.thePanel = new JPanel();
        appendPanel();
        appendPanel();
        this.add(thePanel);
        this.setVisible(true);
    }

    public void appendPanel() {
        Panel panel = new Panel();
        this.thePanel.add(panel.textPanel);
        this.thePanel.add(panel.progressBar);
        this.thePanel.add(panel.buttonsPanel);
    }

    private class Panel extends JPanel {
        JPanel textPanel, progressBar, buttonsPanel;
        JLabel sourceLabel, destLabel, pbLabel;
        JTextField sourceField, destField;
        JButton startButton, stopButton;
        JProgressBar pb;
        lForButton action;
        Thread thread = null;

        public Panel() {
            this.textPanel = new JPanel();
            this.textPanel.setLayout(new GridLayout(2, 1));
            this.progressBar = new JPanel();
            progressBar.setLayout(new GridLayout(2, 1));
            this.buttonsPanel = new JPanel();
            this.buttonsPanel.setLayout(new GridLayout(1, 2));
            this.sourceLabel = new JLabel("Source:");
            sourceLabel.setHorizontalAlignment(JLabel.RIGHT);
            this.destLabel = new JLabel("Destination: ");
            destLabel.setHorizontalAlignment(JLabel.RIGHT);
            this.sourceField = new JTextField("", 25);
            this.destField = new JTextField("", 25);
            this.startButton = new JButton("Copy");
            this.stopButton = new JButton("Stop");
            this.stopButton.setEnabled(false);
            this.pb = new JProgressBar(0, 100);
            this.pbLabel = new JLabel("Waiting...");
            this.textPanel.add(sourceLabel);
            this.textPanel.add(sourceField);
            this.textPanel.add(destLabel);
            this.textPanel.add(destField);
            this.progressBar.add(pbLabel, 0);
            this.progressBar.add(pb, 1);
            this.buttonsPanel.add(startButton);
            this.buttonsPanel.add(stopButton);
            this.action = new lForButton();
            startButton.addActionListener(action);
            stopButton.addActionListener(action);
        }

        private class lForButton implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                switch (e.getActionCommand()) {
                    case "Copy":
                        Wizard wizard = new Wizard(sourceField.getText(), destField.getText()) {
                            public void changePb(int current) {
                                SwingUtilities.invokeLater(new Runnable() {
                                    @Override
                                    public void run() {
                                        pbLabel.setText(String.format("Done: %d%s", current, "%"));
                                        pb.setValue(current);
                                        if (pb.getValue() >= 100) {
                                            //JOptionPane.showMessageDialog(Form.this, "Process finished", "Success", JOptionPane.INFORMATION_MESSAGE);
                                            changeFields();
                                            thread = null;
                                        }
                                    }

                                });
                            }
                        };
                        thread = new Thread(wizard);
                        SwingUtilities.invokeLater(() -> changeFields());
                        thread.start();
                        break;
                    case "Stop":
                        thread.interrupt();
                        JOptionPane.showMessageDialog(Form.this, "Process stopped", "Stopped", JOptionPane.ERROR_MESSAGE);
                        changeFields();
                        thread = null;
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
            pbLabel.setText("Waiting...");
            pb.setValue(0);
        }
    }
}
