package myproject.GUI;

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
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
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

import myproject.Customization.PanelCustomization;
import myproject.Customization.TemplateMenu;
import myproject.Data.DataManager;
import myproject.Objects.Arrow;
import myproject.Objects.DraggableImagePanel;
import myproject.Objects.DraggableTextPanel;

public class MyFrame extends JFrame implements ActionListener {

    private static final int FRONT_LAYER = 99;
    private static final int DEFAULT_SPAWN_SIZE = 150;
    private static final int TEMPLATE_AUTO_POSITION_SENTINEL = 1462683;

    public static boolean IsCursorBusy;
    public static boolean IsEditing;
    public static DraggableTextPanel currentlyEditingPanel = null;
    public static Arrow currentlyEditingArrow = null;
    public static DraggableImagePanel currentlyEditingImagePanel = null;
    static Container contentPane;
    static final ArrayList<DraggableTextPanel> panels = new ArrayList<>();
    final ArrayList<DraggableImagePanel> imgPanels = new ArrayList<>();
    final ArrayList<Arrow> ArrowPanels = new ArrayList<>();

    public static JLayeredPane BackgroundPanel;

    private static int canvasOffsetX = 0;
    private static int canvasOffsetY = 0;
    private Point dragStart;
    private int zoomFactor = 1;

    JMenu fileMenu = new JMenu("File");
    static JMenu addMenu = new JMenu("Add");
    JMenu SettingsMenu = new JMenu("Settings");

    public MyFrame() {
        setTitle("NotAFrogButPanels");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

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
                if (SwingUtilities.isLeftMouseButton(e)) {
                    clearEditingState();
                }
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

        contentPane.addMouseWheelListener(new MouseWheelListener() {

            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                
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

        applyMenuMargin(saveItem, loadItem, panelItem, imgPanelItem, CustomizeItem, TemplateMenuItem, ArrowItem);

        TemplateMenuItem.addActionListener(e -> {
            new TemplateMenu();
        });

        ArrowItem.addActionListener(e -> {
            try {
                Point pos = getRandomVisiblePosition(DEFAULT_SPAWN_SIZE, DEFAULT_SPAWN_SIZE);
                Arrow Arrow = new Arrow(pos.x, pos.y, pos.x-75, pos.y-75,null,Color.LIGHT_GRAY,12);
                contentPane.add(Arrow);
                ArrowPanels.add(Arrow);
                bringToFront(Arrow);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании панели: " + ex.getMessage());
            }
        });

        panelItem.addActionListener(e -> {
            try {
                DraggableTextPanel panel = new DraggableTextPanel(TEMPLATE_AUTO_POSITION_SENTINEL, TEMPLATE_AUTO_POSITION_SENTINEL, "", new Color(24, 24, 26), new Color(24, 24, 26));
                contentPane.add(panel);
                panels.add(panel);
                bringToFront(panel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании панели: " + ex.getMessage());
            }
            refreshUi();
        });

        imgPanelItem.addActionListener(e -> {
            try {
                Point pos = getRandomVisiblePosition(DEFAULT_SPAWN_SIZE, DEFAULT_SPAWN_SIZE);
                DraggableImagePanel panel = new DraggableImagePanel(pos.x, pos.y, null, new Color(24, 24, 26), new Color(24, 24, 26));
                contentPane.add(panel);
                imgPanels.add(panel);
                bringToFront(panel);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Ошибка при создании панели: " + ex.getMessage());
            }
            refreshUi();
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
                refreshUi();
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
        boolean replaced = false;
        for (int i = 0; i < addMenu.getItemCount(); i++) {
            JMenuItem existingItem = addMenu.getItem(i);
        
        
            if (existingItem != null && existingItem.getText() != null 
                    && existingItem.getText().equals(itm.getText())) {
                
                addMenu.remove(existingItem);
                addMenu.add(itm);
                replaced = true;
                break;
            }
        }
        
        if (!replaced) {
            addMenu.add(itm);
        }
    }
    private static Rectangle getVisibleArea() {
    int width = contentPane.getWidth();
    int height = contentPane.getHeight();
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

    if (maxX < minX) maxX = minX;
    if (maxY < minY) maxY = minY;

    for (int attempt = 0; attempt < 120; attempt++) {
        int x = minX + (int) (Math.random() * Math.max(1, (maxX - minX + 1)));
        int y = minY + (int) (Math.random() * Math.max(1, (maxY - minY + 1)));
        int screenX = x + canvasOffsetX;
        int screenY = y + canvasOffsetY;

        if (!intersectsExistingObjects(screenX, screenY, panelWidth, panelHeight)) {
            return new Point(screenX, screenY);
        }
    }

    int x = minX + (int) (Math.random() * Math.max(1, (maxX - minX + 1)));
    int y = minY + (int) (Math.random() * Math.max(1, (maxY - minY + 1)));
    return new Point(x + canvasOffsetX, y + canvasOffsetY);
    }

    private static boolean intersectsExistingObjects(int x, int y, int width, int height) {
        if (contentPane == null) return false;

        Rectangle newBounds = new Rectangle(x, y, width, height);
        int spacing = 8;
        newBounds.grow(spacing, spacing);

        for (Component comp : contentPane.getComponents()) {
            if (comp == null || !comp.isVisible()) continue;
            if (newBounds.intersects(comp.getBounds())) {
                return true;
            }
        }
        return false;
    }


    public static void AddNewJMenuItem(JMenuItem btn, String text, Color color1, Color color2) {
        btn.addActionListener(e -> {
            DraggableTextPanel panel = new DraggableTextPanel(TEMPLATE_AUTO_POSITION_SENTINEL, TEMPLATE_AUTO_POSITION_SENTINEL, text, color1, color2);
            MyFrame.contentPane.add(panel);
            MyFrame.panels.add(panel);
            BackgroundPanel.setLayer(panel, FRONT_LAYER);
            if (contentPane != null) {
                contentPane.revalidate();
                contentPane.repaint();
            }
        });
    }

    private void applyMenuMargin(JMenuItem... items) {
        Insets margin = new Insets(2, 10, 2, 10);
        for (JMenuItem item : items) {
            item.setMargin(margin);
        }
    }

    private void bringToFront(Component component) {
        BackgroundPanel.setLayer(component, FRONT_LAYER);
    }

    private void refreshUi() {
        revalidate();
        repaint();
    }

    public static void beginPanelEditing(DraggableTextPanel panel) {
        if (panel == null || currentlyEditingPanel == panel) {
            return;
        }

        if (currentlyEditingArrow != null) {
            currentlyEditingArrow.exitEdit();
        }
        if (currentlyEditingImagePanel != null) {
            currentlyEditingImagePanel.exitEditMode();
        }
        if (currentlyEditingPanel != null) {
            currentlyEditingPanel.exitEditMode();
        }

        currentlyEditingPanel = panel;
        currentlyEditingArrow = null;
        currentlyEditingImagePanel = null;
        IsEditing = true;
    }

    public static void beginArrowEditing(Arrow arrow) {
        if (arrow == null || currentlyEditingArrow == arrow) {
            return;
        }

        if (currentlyEditingPanel != null) {
            currentlyEditingPanel.exitEditMode();
        }
        if (currentlyEditingImagePanel != null) {
            currentlyEditingImagePanel.exitEditMode();
        }
        if (currentlyEditingArrow != null) {
            currentlyEditingArrow.exitEdit();
        }

        currentlyEditingArrow = arrow;
        currentlyEditingPanel = null;
        currentlyEditingImagePanel = null;
        IsEditing = true;
    }

    public static void beginImageEditing(DraggableImagePanel imagePanel) {
        if (imagePanel == null || currentlyEditingImagePanel == imagePanel) {
            return;
        }

        if (currentlyEditingPanel != null) {
            currentlyEditingPanel.exitEditMode();
        }
        if (currentlyEditingArrow != null) {
            currentlyEditingArrow.exitEdit();
        }
        if (currentlyEditingImagePanel != null) {
            currentlyEditingImagePanel.exitEditMode();
        }

        currentlyEditingImagePanel = imagePanel;
        currentlyEditingPanel = null;
        currentlyEditingArrow = null;
        IsEditing = true;
    }

    public static void clearEditingState() {
        if (currentlyEditingPanel != null) {
            currentlyEditingPanel.exitEditMode();
        }
        if (currentlyEditingArrow != null) {
            currentlyEditingArrow.exitEdit();
        }
        if (currentlyEditingImagePanel != null) {
            currentlyEditingImagePanel.exitEditMode();
        }
        currentlyEditingPanel = null;
        currentlyEditingArrow = null;
        currentlyEditingImagePanel = null;
        IsEditing = false;
    }

}


