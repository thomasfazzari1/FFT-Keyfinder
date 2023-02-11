package keyfinder;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import com.formdev.flatlaf.FlatDarkLaf;

@SuppressWarnings("serial")
public class KeyFinderUI extends JFrame {
    private JButton importButton;
    private JTextArea resultTextArea;

    
    public KeyFinderUI() {

    	
    	
        setTitle("@thomasfazzari1 KeyFinder");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(300, 100); 
        setResizable(false); 


        
        try {
            UIManager.setLookAndFeel( new FlatDarkLaf() );
            UIManager.put( "Button.arc", 30 );
            UIManager.put( "TextComponent.arc", 999 );
        } catch( Exception ex ) {
            System.err.println( "Failed to initialize LaF" );
        }

        

        importButton = new JButton("Importer et analyser (.WAV)");
        importButton.setForeground(Color.WHITE);
    


        resultTextArea = new JTextArea(20,50);
        resultTextArea.setLineWrap(true);
        resultTextArea.setWrapStyleWord(true);
        resultTextArea.setEditable(false);
        resultTextArea.setForeground(Color.WHITE);
        resultTextArea.setFont( UIManager.getFont( "h2.font" ) );

        resultTextArea.setText("Clé : ");
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.add(importButton);

        add(buttonPanel, BorderLayout.NORTH);
        add(resultTextArea, BorderLayout.CENTER);

        importButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    String key = Keyfinder.findKey(selectedFile); 
                    resultTextArea.setText("Clé : "+ key);
                }
            }
        });


        setLocationRelativeTo(null);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new KeyFinderUI().setVisible(true);
            }
        });
    }

}

