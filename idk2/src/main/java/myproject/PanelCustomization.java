package myproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;



public class PanelCustomization extends JFrame {

    public PanelCustomization() {
        setTitle("Settings");
        setSize(300, 400);
        setBackground(Color.BLACK);
        setVisible(true);
        setResizable(false);

        JPanel myPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(45,45,45), 0, this.getHeight(), new Color(20,20,20));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                g2d.dispose();
            }
        };
        myPanel.setLayout(null);
        myPanel.setBackground(Color.DARK_GRAY);

        JButton ConfirmBtn = new JButton("Confirm");
        ConfirmBtn.setLocation(40,285);
        ConfirmBtn.setSize(200,35);


        JLabel ClrLabel = new JLabel("Color");
        ClrLabel.setSize(100, 25);
        ClrLabel.setLocation(10, 75);

        JLabel NameLabel = new JLabel("Name");
        NameLabel.setSize(100, 25);
        NameLabel.setLocation(10, 15);

        JTextField NameField = new JTextField("sample");
        NameField.setSize(75,22);
        NameField.setLocation(10, 45);

        NameField.setBorder(BorderFactory.createLineBorder(new Color(100,100,100)));
        NameField.setPreferredSize(new Dimension(30,22));
        NameField.setHorizontalAlignment(JTextField.CENTER);

        JLabel TxtClrLabel = new JLabel("Text color");
        TxtClrLabel.setSize(100, 25);
        TxtClrLabel.setLocation(10, 135);

        JTextField rField = new JTextField("0");
        JTextField gField = new JTextField("0");
        JTextField bField = new JTextField("0");

        JTextField TxtrField = new JTextField("0");
        JTextField TxtgField = new JTextField("0");
        JTextField TxtbField = new JTextField("0");

        rField.setBorder(BorderFactory.createLineBorder(new Color(100,100,100)));
        rField.setPreferredSize(new Dimension(30,22));
        rField.setHorizontalAlignment(JTextField.CENTER);

        gField.setBorder(BorderFactory.createLineBorder(new Color(100,100,100)));
        gField.setPreferredSize(new Dimension(30,22));
        gField.setHorizontalAlignment(JTextField.CENTER);

        bField.setBorder(BorderFactory.createLineBorder(new Color(100,100,100)));
        bField.setPreferredSize(new Dimension(30,22));
        bField.setHorizontalAlignment(JTextField.CENTER);

        TxtrField.setBorder(BorderFactory.createLineBorder(new Color(100,100,100)));
        TxtrField.setPreferredSize(new Dimension(30,22));
        TxtrField.setHorizontalAlignment(JTextField.CENTER);

        TxtgField.setBorder(BorderFactory.createLineBorder(new Color(100,100,100)));
        TxtgField.setPreferredSize(new Dimension(30,22));
        TxtgField.setHorizontalAlignment(JTextField.CENTER);

        TxtbField.setBorder(BorderFactory.createLineBorder(new Color(100,100,100)));
        TxtbField.setPreferredSize(new Dimension(30,22));
        TxtbField.setHorizontalAlignment(JTextField.CENTER);


        JPanel Panelpreview = new JPanel();
        JTextArea textArea = new JTextArea("Text");

        Panelpreview.setOpaque(true);
        Panelpreview.setBackground(new Color(0, 0, 0));
        Panelpreview.setPreferredSize(new Dimension(75, 75));
        Panelpreview.setBorder(BorderFactory.createLineBorder(Color.black));
        Panelpreview.setBounds(50, 50, 200, 100);
        Panelpreview.setSize(75, 75);
        Panelpreview.setLocation(195, 25);
        Panelpreview.setBackground(Color.orange);

        textArea.setOpaque(false);
        textArea.setPreferredSize(new Dimension(Panelpreview.getSize().width - 10, Panelpreview.getSize().height - 10));
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setForeground(Color.WHITE);
        textArea.setEnabled(true);
        Panelpreview.add(textArea);

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(new Color(0, 0, 0, 0));
        inputPanel.setSize(300, 50);
        inputPanel.setLocation(-65, 100);

        rField.setText(String.valueOf(Panelpreview.getBackground().getRed()));
        gField.setText(String.valueOf(Panelpreview.getBackground().getGreen()));
        bField.setText(String.valueOf(Panelpreview.getBackground().getBlue()));

        inputPanel.add(new JLabel("R:"));
        inputPanel.add(rField);
        inputPanel.add(new JLabel("G:"));
        inputPanel.add(gField);
        inputPanel.add(new JLabel("B:"));
        inputPanel.add(bField);

        JPanel TxtinputPanel = new JPanel();
        TxtinputPanel.setBackground(new Color(0, 0, 0, 0));
        TxtinputPanel.setSize(300, 50);
        TxtinputPanel.setLocation(-65, 160);

        TxtrField.setText(String.valueOf(textArea.getForeground().getRed()));
        TxtgField.setText(String.valueOf(textArea.getForeground().getGreen()));
        TxtbField.setText(String.valueOf(textArea.getForeground().getBlue()));

        TxtinputPanel.add(new JLabel("R:"));
        TxtinputPanel.add(TxtrField);
        TxtinputPanel.add(new JLabel("G:"));
        TxtinputPanel.add(TxtgField);
        TxtinputPanel.add(new JLabel("B:"));
        TxtinputPanel.add(TxtbField);

        myPanel.add(inputPanel);
        myPanel.add(TxtinputPanel);
        myPanel.add(ClrLabel);
        myPanel.add(TxtClrLabel);
        myPanel.add(Panelpreview);
        myPanel.add(NameLabel);
        myPanel.add(NameField);
        myPanel.add(ConfirmBtn);

        add(myPanel);

        ConfirmBtn.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DataManager.saveTemplate("TxtPanel",NameField.getText(), new Point(100,100), textArea.getText(), Panelpreview.getBackground(), Panelpreview.getBackground());;
                File dir = new File(TemplateMenu.RESOURCES_DIR);
                File[] files = dir.listFiles();
                for (int i = files.length - 1; i >= 0; i--) {
                    if (files[i].isFile()) {
                        DataManager.loadTemplate(files[i]);
                    }

                }
                
            }
            
            
        });


        DocumentListener updateBackgroundColor = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateBackground(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateBackground(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateBackground(); }

            private void updateBackground() {
                try {
                    int r = Integer.parseInt(rField.getText());
                    int g = Integer.parseInt(gField.getText());
                    int b = Integer.parseInt(bField.getText());
                    r = Math.max(0, Math.min(255, r));
                    g = Math.max(0, Math.min(255, g));
                    b = Math.max(0, Math.min(255, b));
                    Panelpreview.setBackground(new Color(r, g, b));
                } catch (NumberFormatException ignored) {}
            }
        };


        DocumentListener updateTextColor = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) { updateText(); }
            @Override
            public void removeUpdate(DocumentEvent e) { updateText(); }
            @Override
            public void changedUpdate(DocumentEvent e) { updateText(); }

            private void updateText() {
                try {
                    int r = Integer.parseInt(TxtrField.getText());
                    int g = Integer.parseInt(TxtgField.getText());
                    int b = Integer.parseInt(TxtbField.getText());
                    r = Math.max(0, Math.min(255, r));
                    g = Math.max(0, Math.min(255, g));
                    b = Math.max(0, Math.min(255, b));
                    textArea.setForeground(new Color(r, g, b));
                } catch (NumberFormatException ignored) {}
            }
        };


        rField.getDocument().addDocumentListener(updateBackgroundColor);
        gField.getDocument().addDocumentListener(updateBackgroundColor);
        bField.getDocument().addDocumentListener(updateBackgroundColor);

        TxtrField.getDocument().addDocumentListener(updateTextColor);
        TxtgField.getDocument().addDocumentListener(updateTextColor);
        TxtbField.getDocument().addDocumentListener(updateTextColor);
    }

}
