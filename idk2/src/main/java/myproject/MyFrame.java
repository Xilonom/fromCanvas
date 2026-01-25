package myproject;

import java.awt.Color;
import java.awt.Container;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ContainerEvent;
import java.awt.event.ContainerListener;
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

    static Container contentPane;
    ArrayList<String> data = new ArrayList<>();
    static ArrayList<DraggableTextPanel> panels = new ArrayList<>();
    ArrayList<DraggableImagePanel> imgPanels = new ArrayList<>();
    ArrayList<Arrow> ArrowPanels = new ArrayList<>();
    Point dragStartPoint = null; 

    JMenu fileMenu = new JMenu("File");
    static JMenu addMenu = new JMenu("Add");
    JMenu SettingsMenu = new JMenu("Settings");

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
        menu.setOpaque(false); // Прозрачность
        menu.setBackground(new Color(0, 0, 0, 0)); // Полностью прозрачно
        menu.setForeground(Color.WHITE); // Или системный цвет
        menu.setBorderPainted(false);
        menu.setMargin(new Insets(0, 0, 0, 0)); // Убираем отступы


        menu.setBorderPainted(false);
        setJMenuBar(menu);

        JMenuItem saveItem = new JMenuItem("Save");
        JMenuItem loadItem = new JMenuItem("load");
        JMenuItem panelItem = new JMenuItem("Panel");
        JMenuItem imgPanelItem = new JMenuItem("ImagePanel");
        JMenuItem ArrowItem = new JMenuItem("Arrow");
        JMenuItem CustomizeItem = new JMenuItem("Customization");
        JMenuItem TemplateMenuItem = new JMenuItem("Template");

        saveItem.setMargin(new Insets(2, 10, 2, 10));
        loadItem.setMargin(new Insets(2, 10, 2, 10));
        panelItem.setMargin(new Insets(2, 10, 2, 10));
        imgPanelItem.setMargin(new Insets(2, 10, 2, 10));
        CustomizeItem.setMargin(new Insets(2, 10, 2, 10));
        TemplateMenuItem.setMargin(new Insets(2, 10, 2, 10));

        TemplateMenuItem.addActionListener(e -> {
            new TemplateMenu();
        });

        ArrowItem.addActionListener(e -> {
            try {
                Arrow Arrow = new Arrow(100, 100, 20, 20,Color.LIGHT_GRAY,12);
                contentPane.add(Arrow);
                ArrowPanels.add(Arrow);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании панели: " + ex.getMessage());
            }
        });

        panelItem.addActionListener(e -> {
            try {
                DraggableTextPanel panel = new DraggableTextPanel(100, 100, "", new Color(100,100,100), new Color(100,100,100));
                contentPane.add(panel);
                panels.add(panel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании панели: " + ex.getMessage());
            }
            revalidate();
            repaint();
        });

        getContentPane().addContainerListener(new ContainerListener() {
            @Override
            public void componentAdded(ContainerEvent e) {
                revalidate();
                repaint();
            }

            @Override
            public void componentRemoved(ContainerEvent e) {
                revalidate();
                repaint();
            }
        });


        imgPanelItem.addActionListener(e -> {
            try {
                JFileChooser fileChooser = new JFileChooser();

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {

                DraggableImagePanel panel = new DraggableImagePanel(100, 100, fileChooser.getSelectedFile().getAbsolutePath(), new Color(100,100,100), new Color(100,100,100));
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

        saveItem.addActionListener(e -> {
            DataManager.save(panels,imgPanels,ArrowPanels);
        });

        loadItem.addActionListener(e -> {
            JFileChooser fileChooser = new JFileChooser();
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = new File(fileChooser.getSelectedFile().getAbsolutePath());
                DataManager.load(getContentPane(), file, panels, imgPanels,ArrowPanels);
                revalidate();
                repaint();
                System.out.println(file);
            }
        });

        CustomizeItem.addActionListener(e -> {
            new PanelCustomization();
        });





        fileMenu.add(saveItem);
        fileMenu.add(loadItem);

        addMenu.add(imgPanelItem);
        addMenu.add(panelItem);
        addMenu.add(ArrowItem);

        SettingsMenu.add(CustomizeItem);
        SettingsMenu.add(TemplateMenuItem);

        menu.add(fileMenu);
        menu.add(addMenu);
        menu.add(SettingsMenu);

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

    public static void AddTemplateItem(JMenuItem itm) {
        for (int i = 0; i < addMenu.getItemCount(); i++) {
            JMenuItem existingItem = addMenu.getItem(i);
        
        
            if (existingItem != null && existingItem.getText() != null 
                    && existingItem.getText().equals(itm.getText())) {
                
                addMenu.remove(existingItem);
                addMenu.add(itm);
                break; 
            }
        }
        
        addMenu.add(itm);
    }

}


