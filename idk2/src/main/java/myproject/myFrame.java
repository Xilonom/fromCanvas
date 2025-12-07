package myproject;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;

import java.awt.*;

import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;


public class myFrame extends JFrame implements ActionListener {

    JButton createPanelBtn;
    Container contentPane;

    ArrayList<String> Data = new ArrayList<>();

    ArrayList<DraggableTextPanel> Panels = new ArrayList<>();

    ArrayList<String> LData = new ArrayList<>();

    public myFrame() {
        setTitle("NotAFrogButPanels");
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); 

        JMenuBar menu = new JMenuBar();
        setJMenuBar(menu);

        JMenu fileMenu = new JMenu("file");
        JMenu addMenu = new JMenu("add");

        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("Load");
        JMenuItem panelItem = new JMenuItem("Panel");

        

        panelItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                DraggableTextPanel newPanel = new DraggableTextPanel((int)(Math.random()*300),(int)(Math.random()*300),null); 
                Panels.add(newPanel);
                getContentPane().add(newPanel);
                revalidate(); 
                repaint();  
                
            }

        });
        addMenu.add(panelItem);

        saveItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Data.clear();
                for (int i = 0; i < Panels.size(); i++) {
                    Data.add(Integer.toString(Panels.get(i).posX));
                    Data.add(Integer.toString(Panels.get(i).posY));
                    Data.add(Panels.get(i).text);
                }
                System.err.println(Data);
                DataManager.save(Panels);
            }
        });

        loadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                    DataManager.load(getContentPane(), file);
                    revalidate(); 
                    repaint();  
                    System.err.println(file);
                }


                
            }
        });
        fileMenu.add(saveItem);
        fileMenu.add(loadItem);



        menu.add(fileMenu);
        menu.add(addMenu); 

        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'actionPerformed'");
    }

    


}