package keyfinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

@SuppressWarnings("serial")
public class KeyFinderUI extends JFrame {
    private JButton importButton;
    private JTextArea resultTextArea;

    
    public KeyFinderUI() {
        setTitle("Thomas Fazzari - KeyFinder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(300, 150); 
        setResizable(false); 

        importButton = new JButton("Importer le morceau au format .WAV");
        resultTextArea = new JTextArea(20,50);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setEditable(false);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(importButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(resultTextArea, BorderLayout.CENTER);

        importButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String key = Keyfinder.findKey(selectedFile); 
                    resultTextArea.setText("Clé : " + key);
                }
            }
        });


        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new KeyFinderUI().setVisible(true);
            }
        });
    }
}

