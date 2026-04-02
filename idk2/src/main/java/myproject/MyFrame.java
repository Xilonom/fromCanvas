package myproject;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
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
import javax.swing.JLayeredPane;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;

public class MyFrame extends JFrame implements ActionListener {

    static boolean IsCursorBusy;
    static boolean IsEditing;
    public static DraggableTextPanel currentlyEditingPanel = null;
    public static Arrow currentlyEditingArrow = null;
    static Container contentPane;
    static ArrayList<DraggableTextPanel> panels = new ArrayList<>();
    ArrayList<DraggableImagePanel> imgPanels = new ArrayList<>();
    ArrayList<Arrow> ArrowPanels = new ArrayList<>();

    public static JLayeredPane BackgroundPanel;

    private static int canvasOffsetX = 0;
    private static int canvasOffsetY = 0;
    private Point dragStart;

    JMenu fileMenu = new JMenu("File");
    static JMenu addMenu = new JMenu("Add");
    JMenu SettingsMenu = new JMenu("Settings");

    public MyFrame() {
        setTitle("NotAFrogButPanels");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        BackgroundPanel = new JLayeredPane() {
            private final Color BACKGROUND_COLOR = new Color(0,0,0);
            private final Color DOT_COLOR = new Color(45,45,45);
            private final int DOT_SIZE = 3;
            private final int GRID_STEP = 40;

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g.create();

                g2d.setColor(BACKGROUND_COLOR);
                g2d.fillRect(0, 0, getWidth(), getHeight());

                
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                g2d.setColor(DOT_COLOR);

                int startX = -(canvasOffsetX % GRID_STEP);
                int startY = -(canvasOffsetY % GRID_STEP);

                for (int x = startX; x < getWidth(); x += GRID_STEP) {
                    for (int y = startY; y < getHeight(); y += GRID_STEP) {
                        g2d.fillOval(x, y, DOT_SIZE, DOT_SIZE);
                    }
                }

                g2d.dispose();
            }
        };

        BackgroundPanel.setBounds(0, 0, getWidth(), getHeight());
        setContentPane(BackgroundPanel);
        getContentPane().setLayout(null);
        contentPane = getContentPane();


        

        contentPane.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e) || IsCursorBusy == false) {
                    dragStart = e.getPoint();
                }
                else {
                    dragStart = null;
                }
            }
        });

        contentPane.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (dragStart != null) {
                    int dx = e.getX() - dragStart.x;
                    int dy = e.getY() - dragStart.y;
                    
                    canvasOffsetX -= dx;
                    canvasOffsetY -= dy;
                    
                    for (Component comp : contentPane.getComponents()) {
                        comp.setLocation(comp.getX() + dx, comp.getY() + dy);
                    }
                    dragStart = e.getPoint();
                    repaint();
                }
            }
        });

        JMenuBar menu = new JMenuBar();
        menu.setOpaque(false);
        menu.setBackground(new Color(0, 0, 0, 0));
        menu.setForeground(Color.WHITE);
        menu.setBorderPainted(false);
        menu.setMargin(new Insets(0, 0, 0, 0));

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
                Point pos = getRandomVisiblePosition(150, 150);
                Arrow Arrow = new Arrow(pos.x, pos.y, pos.x-75, pos.y-75,null,Color.LIGHT_GRAY,12);
                contentPane.add(Arrow);
                ArrowPanels.add(Arrow);
                if (ArrowPanels.size() > 1) {
                    BackgroundPanel.setLayer(ArrowPanels.get(ArrowPanels.indexOf(Arrow) - 1),Arrow.DEFAULT_LAYER);
                }
                BackgroundPanel.setLayer(Arrow,99);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании панели: " + ex.getMessage());
            }
        });

        panelItem.addActionListener(e -> {
            try {
                DraggableTextPanel panel = new DraggableTextPanel(1462683, 1462683, "", new Color(24, 24, 26), new Color(24, 24, 26));
                contentPane.add(panel);
                panels.add(panel);
                if (ArrowPanels.size() > 1) {
                    BackgroundPanel.setLayer(panels.get(panels.indexOf(panel) - 1),panel.DEFAULT_LAYER);
                }
                BackgroundPanel.setLayer(panel,99);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании панели: " + ex.getMessage());
            }
            revalidate();
            repaint();
        });

        imgPanelItem.addActionListener(e -> {
            try {
                Point pos = getRandomVisiblePosition(150, 150);
                DraggableImagePanel panel = new DraggableImagePanel(pos.x, pos.y, null, new Color(24, 24, 26), new Color(24, 24, 26));
                contentPane.add(panel);
                //ImagePanels.add(panel);
                BackgroundPanel.setLayer(panel,99);
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
    private static Rectangle getVisibleArea() {
    // Размер панели (где рисуется фон)
    int width = contentPane.getWidth();
    int height = contentPane.getHeight();

    // Видимая область в "мире" (с учётом смещения)
    int visibleX = -canvasOffsetX;
    int visibleY = -canvasOffsetY;

    return new Rectangle(visibleX, visibleY, width, height);
    }

    public static Point getRandomVisiblePosition(int panelWidth, int panelHeight) {
    Rectangle visible = getVisibleArea();

    
    int minX = visible.x;
    int maxX = visible.x + visible.width - panelWidth;
    int minY = visible.y;
    int maxY = visible.y + visible.height - panelHeight;
    
    int x = minX + (int) (Math.random() * (maxX - minX));
    int y = minY + (int) (Math.random() * (maxY - minY));
    
    // Компенсируем смещение фона: преобразуем из «мировых» в «экранные» координаты
    return new Point(x + canvasOffsetX, y + canvasOffsetY);
    }


    public static void AddNewJMenuItem(JMenuItem btn, String text, Color color1, Color color2) {
        btn.addActionListener(e -> {
            Point pos = getRandomVisiblePosition(150, 150);
            DraggableTextPanel panel = new DraggableTextPanel(pos.x, pos.y, text, color1, color2);
            MyFrame.contentPane.add(panel);
            MyFrame.panels.add(panel);

        });
    }

}


