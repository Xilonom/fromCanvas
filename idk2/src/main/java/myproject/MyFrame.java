package myproject;

import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;



public class MyFrame extends JFrame implements ActionListener {

    Container contentPane;
    ArrayList<String> data = new ArrayList<>();
    ArrayList<DraggableTextPanel> panels = new ArrayList<>();
    ArrayList<DraggableImagePanel> imgPanels = new ArrayList<>();
    Point dragStartPoint = null; 

    public MyFrame() {
        setTitle("NotAFrogButPanels");
        setSize(800, 600); 
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        

        JPanel gradientPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2d = (Graphics2D) g.create();
                GradientPaint gp = new GradientPaint(0, 0, new Color(45,45,45), 0, this.getHeight(), new Color(20,20,20));
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, this.getWidth(), this.getHeight());
                g2d.dispose();
            }
        };
        gradientPanel.setBounds(0, 0, getWidth(), getHeight()); // Устанавливаем размер и позицию
        setContentPane(gradientPanel);
        getContentPane().setLayout(null);

        JMenuBar menu = new JMenuBar();
        menu.setOpaque(true);
        menu.setBackground(Color.BLACK);
        menu.setForeground(Color.WHITE);

        menu.setBorderPainted(false);
        setJMenuBar(menu);

        JMenu fileMenu = new JMenu("File");
        JMenu addMenu = new JMenu("Add");

        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("load");
        JMenuItem panelItem = new JMenuItem("Panel");
        JMenuItem imgPanelItem = new JMenuItem("ImagePanel");

        panelItem.addActionListener(e -> {
            try {
                DraggableTextPanel panel = new DraggableTextPanel(100, 100, "");
                contentPane.add(panel);
                panels.add(panel);
                revalidate(); 
                repaint();   
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании панели: " + ex.getMessage());
            }
            revalidate();
            repaint();
        });

        addMenu.add(panelItem);

        imgPanelItem.addActionListener(e -> {
            try {
                JFileChooser fileChooser = new JFileChooser();

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                DraggableImagePanel panel = new DraggableImagePanel(100, 100, fileChooser.getSelectedFile().getAbsolutePath());
                imgPanels.add(panel);
                contentPane.add(panel);
                revalidate();
                repaint();
                }
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании панели: " + ex.getMessage());
            }
            revalidate();
            repaint();
        });

        addMenu.add(imgPanelItem);

        saveItem.addActionListener(e -> {
            data.clear();
            for (int i=0; i<panels.size(); i++) {
                data.add(panels.get(i).getType());
                data.add(Integer.toString(panels.get(i).posX));
                data.add(Integer.toString(panels.get(i).posY));
                data.add(panels.get(i).text);
            }
            for (int i=0; i<imgPanels.size(); i++) {
                data.add(imgPanels.get(i).getType());
                data.add(Integer.toString(imgPanels.get(i).getPosX()));
                data.add(Integer.toString(imgPanels.get(i).getPosY()));
                data.add(imgPanels.get(i).getImg());
            }
            System.out.println(data);
            DataManager.save(panels,imgPanels);
        });

        loadItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                DataManager.load(getContentPane(), file, panels, imgPanels);
                revalidate();
                repaint();
                System.out.println(file);
            }
        });

        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        menu.add(fileMenu);
        menu.add(addMenu);

        MouseAdapter mouseHandler = new MouseAdapter() {
            boolean isDragging = false;

            
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) { 
                    dragStartPoint = e.getPoint();
                    isDragging = true;
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                if (isDragging && SwingUtilities.isRightMouseButton(e)) {
                    isDragging = false;
                }
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                if (!isDragging || !SwingUtilities.isRightMouseButton(e)) return;
                
                Point currentMousePos = e.getPoint();
                int dx = currentMousePos.x - dragStartPoint.x;
                int dy = currentMousePos.y - dragStartPoint.y;
                
                for (DraggableTextPanel p : panels) {
                    p.setLocation(p.getX() + dx, p.getY() + dy);
                }
                for (DraggableImagePanel p : imgPanels) {
                    p.setLocation(p.getX() + dx, p.getY() + dy);
                }
                dragStartPoint = currentMousePos;
                repaint();
            }
        };
        

        contentPane = getContentPane();
        contentPane.addMouseMotionListener(mouseHandler);
        contentPane.addMouseListener(mouseHandler);
        
        
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {}


}


