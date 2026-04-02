package myproject;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

public class DraggableImagePanel extends JPanel {
    private Point startDragPos;
    private boolean dragging = false;
    private boolean resizing = false;
    private boolean mouseOver = false;

    private final int posX;
    private final int posY;
    Color color1;
    Color color2;

    String Type = "ImgPanel";

    private Image originalImage;
    String imagePath = "";
    
    private BufferedImage processedImage;
    private JButton selectImageButton;
    
    // Константы для изменения размера
    private static final int RESIZE_HANDLE_SIZE = 20;
    private static final int MIN_SIZE = 100;
    private static final int MAX_SIZE = 800;
    private static final int DEFAULT_SIZE = 200;
    
    private Dimension resizeStartSize;
    private Point resizeStartPos;
    private Rectangle resizeZone;
    private int handleOffset = 5;
    
    // Соотношение сторон изображения
    private double imageAspectRatio = 1.0;
    
    // Константы для отступов и закруглений
    private static final int PADDING = 5;
    private static final int PANEL_CORNER_RADIUS = 25;
    private static final int IMAGE_CORNER_RADIUS = 20;

    public DraggableImagePanel(int posX, int posY, String imgPath, Color clr1, Color clr2) {
        
        this.posX = posX;
        this.posY = posY;

        // Загружаем изображение
        if (imgPath != null && !imgPath.isEmpty()) {
            loadImage(imgPath);
        }

        color1 = clr1;
        color2 = clr2;

        setOpaque(false);
        setPreferredSize(new Dimension(DEFAULT_SIZE, DEFAULT_SIZE));
        setLocation(new Point(posX, posY));
        setSize(DEFAULT_SIZE, DEFAULT_SIZE);
        setLayout(null);

        // Создаем кнопку выбора изображения
        createSelectButton();

        updateProcessedImage();
        updateResizeZone();
        addMouseListeners();
    }

    /**
     * Загрузка изображения с определением пропорций
     */
    private void loadImage(String path) {
        Image tempImage = Toolkit.getDefaultToolkit().createImage(path);
        
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(tempImage, 0);
        try {
            mt.waitForID(0);
        } catch (InterruptedException ignored) {}
        
        originalImage = tempImage;
        imagePath = path;
        
        // Определяем пропорции изображения
        int imgWidth = originalImage.getWidth(null);
        int imgHeight = originalImage.getHeight(null);
        if (imgWidth > 0 && imgHeight > 0) {
            imageAspectRatio = (double) imgWidth / imgHeight;
            
            // Подстраиваем размер панели под пропорции изображения
            adjustSizeToImageAspectRatio();
        }
    }
    
    /**
     * Подстраивает размер панели под пропорции изображения
     */
    private void adjustSizeToImageAspectRatio() {
        int currentWidth = getWidth();
        int currentHeight = getHeight();
        
        // Вычисляем новые размеры с сохранением пропорций
        int newWidth, newHeight;
        
        if (imageAspectRatio >= 1.0) {
            // Широкое изображение - подстраиваем по ширине
            newWidth = currentWidth;
            newHeight = (int) (currentWidth / imageAspectRatio);
        } else {
            // Высокое изображение - подстраиваем по высоте
            newHeight = currentHeight;
            newWidth = (int) (currentHeight * imageAspectRatio);
        }
        
        // Проверяем минимальные и максимальные размеры
        newWidth = constrain(newWidth, MIN_SIZE, MAX_SIZE);
        newHeight = constrain(newHeight, MIN_SIZE, MAX_SIZE);
        
        setSize(newWidth, newHeight);
    }

    /**
     * Создает кнопку выбора изображения
     */
    private void createSelectButton() {
        selectImageButton = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Полупрозрачный фон с закругленными углами
                g2.setColor(new Color(255, 255, 255, 200));
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
                
                // Рисуем иконку-плюс
                g2.setColor(new Color(100, 100, 100));
                g2.setStroke(new BasicStroke(3f));
                
                int centerX = getWidth() / 2;
                int centerY = getHeight() / 2;
                int size = Math.min(getWidth(), getHeight()) / 4;
                
                // Горизонтальная линия
                g2.drawLine(centerX - size, centerY, centerX + size, centerY);
                // Вертикальная линия
                g2.drawLine(centerX, centerY - size, centerX, centerY + size);
                
                // Текст
                g2.setFont(new Font("Arial", Font.PLAIN, 12));
                g2.setColor(Color.DARK_GRAY);
                String text = "Выбрать изображение";
                int textWidth = g2.getFontMetrics().stringWidth(text);
                g2.drawString(text, centerX - textWidth / 2, centerY + size + 15);
                
                g2.dispose();
            }
        };
        
        selectImageButton.setBounds(PADDING, PADDING, getWidth() - 2 * PADDING, getHeight() - 2 * PADDING);
        selectImageButton.setContentAreaFilled(false);
        selectImageButton.setBorderPainted(false);
        selectImageButton.setFocusPainted(false);
        selectImageButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        selectImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser();
                fileChooser.setFileFilter(new FileNameExtensionFilter("Изображения", "jpg", "jpeg", "png", "gif", "bmp"));

                if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                    imagePath = fileChooser.getSelectedFile().getAbsolutePath();
                    loadAndProcessImage();
                }
            }
        });
        
        // Показываем кнопку только если нет изображения
        if (originalImage == null) {
            add(selectImageButton);
        }
    }

    /**
     * Загружает и обрабатывает изображение
     */
    private void loadAndProcessImage() {
        loadImage(imagePath);
        
        // Удаляем кнопку
        if (selectImageButton != null && selectImageButton.getParent() != null) {
            remove(selectImageButton);
        }
        
        updateProcessedImage();
        repaint();
    }

    /**
     * Обработка изображения с высоким качеством и закругленными углами
     */
    private void updateProcessedImage() {
        if (originalImage != null) {
            int panelWidth = getWidth() - 2 * PADDING;
            int panelHeight = getHeight() - 2 * PADDING;
            
            if (panelWidth <= 0 || panelHeight <= 0) return;
            
            // Создаем высококачественное BufferedImage
            BufferedImage tempImage = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = tempImage.createGraphics();
            
            // Настройки для высокого качества рендеринга
            g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
            g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
            
            // Рисуем изображение
            g2d.drawImage(originalImage, 0, 0, panelWidth, panelHeight, null);
            g2d.dispose();
            
            // Создаем изображение с закругленными углами
            processedImage = new BufferedImage(panelWidth, panelHeight, BufferedImage.TYPE_INT_ARGB);
            g2d = processedImage.createGraphics();
            
            // Настройки для сглаживания
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            
            // Создаем форму с закругленными углами
            RoundRectangle2D roundedRect = new RoundRectangle2D.Float(0, 0, panelWidth, panelHeight, 
                                                                       IMAGE_CORNER_RADIUS, IMAGE_CORNER_RADIUS);
            
            // Устанавливаем clip для закругления углов
            g2d.setClip(roundedRect);
            
            // Рисуем изображение
            g2d.drawImage(tempImage, 0, 0, null);
            
            g2d.dispose();
        }
    }

    /**
     * Добавление слушателей мыши для перетаскивания и изменения размера
     */
    private void addMouseListeners() {
        MouseHandler mouseHandler = new MouseHandler();
        addMouseListener(mouseHandler);
        addMouseMotionListener(mouseHandler);
        
        // Добавляем слушатели для кнопки, чтобы она не блокировала перетаскивание панели
        if (selectImageButton != null) {
            selectImageButton.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    // Если нажали на кнопку, но изображения нет - передаем событие панели для перетаскивания
                    if (originalImage == null) {
                        MouseEvent panelEvent = SwingUtilities.convertMouseEvent(selectImageButton, e, DraggableImagePanel.this);
                        DraggableImagePanel.this.dispatchEvent(panelEvent);
                    }
                }
                
                @Override
                public void mouseDragged(MouseEvent e) {
                    if (originalImage == null) {
                        MouseEvent panelEvent = SwingUtilities.convertMouseEvent(selectImageButton, e, DraggableImagePanel.this);
                        DraggableImagePanel.this.dispatchEvent(panelEvent);
                    }
                }
            });
        }
    }
    
    /**
     * Проверка, находится ли курсор в зоне изменения размера (правый нижний угол)
     */
    private boolean isInResizeZone(Point point) {
        if (originalImage == null || resizeZone == null) return false;
        return resizeZone.contains(point);
    }
    
    /**
     * Проверка, находится ли курсор рядом с зоной изменения размера
     */
    private boolean isNearResizeZone(Point point) {
        if (originalImage == null || resizeZone == null) return false;
        int tolerance = 10;
        Rectangle extendedZone = new Rectangle(
            resizeZone.x - tolerance, resizeZone.y - tolerance,
            resizeZone.width + tolerance * 2, resizeZone.height + tolerance * 2
        );
        return extendedZone.contains(point);
    }
    
    /**
     * Обновление зоны изменения размера (только правый нижний угол)
     */
    private void updateResizeZone() {
        int width = getWidth();
        int height = getHeight();
        resizeZone = new Rectangle(
            width - RESIZE_HANDLE_SIZE - handleOffset, 
            height - RESIZE_HANDLE_SIZE - handleOffset, 
            RESIZE_HANDLE_SIZE, 
            RESIZE_HANDLE_SIZE
        );
    }
    
    /**
     * Внутренний класс для обработки событий мыши
     */
    private class MouseHandler extends MouseAdapter {
        private Point dragStartScreen;
        private Rectangle startBounds;
        
        @Override
        public void mousePressed(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) return;
            
            Point point = e.getPoint();
            
            // Проверяем, находимся ли мы в зоне изменения размера (правый нижний угол)
            if (isInResizeZone(point)) {
                // Начинаем изменение размера
                resizing = true;
                dragging = false;
                
                // Сохраняем начальные данные для изменения размера
                dragStartScreen = e.getLocationOnScreen();
                startBounds = getBounds();
                resizeStartPos = SwingUtilities.convertPoint(DraggableImagePanel.this, point, getParent());
                resizeStartSize = getSize();
                
                setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                e.consume();
            } 
            else {
                // Начинаем перетаскивание (работает всегда, даже без изображения)
                dragging = true;
                resizing = false;
                startDragPos = SwingUtilities.convertPoint(DraggableImagePanel.this, point, getParent());
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                e.consume();
            }
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if (dragging || resizing) {
                dragging = false;
                resizing = false;
                
                // Обновляем курсор
                if (mouseOver) {
                    Point point = e.getPoint();
                    if (isNearResizeZone(point)) {
                        setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                    } else {
                        setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                    }
                } else {
                    setCursor(Cursor.getDefaultCursor());
                }
                
                updateResizeZone();
                revalidate();
                repaint();
                e.consume();
            }
        }
        
        @Override
        public void mouseEntered(MouseEvent e) {
            mouseOver = true;
            // При входе мыши показываем соответствующий курсор
            if (originalImage != null) {
                Point point = e.getPoint();
                if (isNearResizeZone(point)) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            } else {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
            repaint();
        }
        
        @Override
        public void mouseExited(MouseEvent e) {
            mouseOver = false;
            if (!dragging && !resizing) {
                setCursor(Cursor.getDefaultCursor());
            }
            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if (!SwingUtilities.isLeftMouseButton(e)) {
                MyFrame.IsCursorBusy = false;
                return;
            }

            MyFrame.IsCursorBusy = true;
            
            if (resizing && dragStartScreen != null && startBounds != null) {
                // Изменение размера
                Point currentScreen = e.getLocationOnScreen();
                int dx = currentScreen.x - dragStartScreen.x;
                int dy = currentScreen.y - dragStartScreen.y;
                
                // Используем максимальное изменение для сохранения пропорций
                int delta = Math.max(dx, dy);
                
                // Вычисляем новый размер с сохранением пропорций
                int newWidth, newHeight;
                
                if (imageAspectRatio >= 1.0) {
                    // Для широких изображений
                    newWidth = constrain(startBounds.width + delta, MIN_SIZE, MAX_SIZE);
                    newHeight = (int) (newWidth / imageAspectRatio);
                    
                    // Проверяем ограничения по высоте
                    if (newHeight > MAX_SIZE) {
                        newHeight = MAX_SIZE;
                        newWidth = (int) (newHeight * imageAspectRatio);
                    } else if (newHeight < MIN_SIZE) {
                        newHeight = MIN_SIZE;
                        newWidth = (int) (newHeight * imageAspectRatio);
                    }
                } else {
                    // Для высоких изображений
                    newHeight = constrain(startBounds.height + delta, MIN_SIZE, MAX_SIZE);
                    newWidth = (int) (newHeight * imageAspectRatio);
                    
                    // Проверяем ограничения по ширине
                    if (newWidth > MAX_SIZE) {
                        newWidth = MAX_SIZE;
                        newHeight = (int) (newWidth / imageAspectRatio);
                    } else if (newWidth < MIN_SIZE) {
                        newWidth = MIN_SIZE;
                        newHeight = (int) (newWidth / imageAspectRatio);
                    }
                }
                
                // Применяем новые размеры (позиция не меняется, так как тянем за правый нижний угол)
                setBounds(startBounds.x, startBounds.y, newWidth, newHeight);
                
                // Обновляем обработанное изображение
                updateProcessedImage();
                
                // Обновляем кнопку если она есть
                if (selectImageButton != null && selectImageButton.getParent() != null) {
                    selectImageButton.setBounds(PADDING, PADDING, getWidth() - 2 * PADDING, getHeight() - 2 * PADDING);
                }
                
                // Обновляем зону изменения размера
                updateResizeZone();
                
                revalidate();
                repaint();
            } 
            else if (dragging && startDragPos != null) {
                // Перетаскивание
                Point currentPos = SwingUtilities.convertPoint(DraggableImagePanel.this, e.getPoint(), getParent());
                int dx = currentPos.x - startDragPos.x;
                int dy = currentPos.y - startDragPos.y;
                Point currentLocation = getLocation();
                setLocation(currentLocation.x + dx, currentLocation.y + dy);
                startDragPos = currentPos;
            }
            else {
                MyFrame.IsCursorBusy = false;
            }
            e.consume();
        }
        
        @Override
        public void mouseMoved(MouseEvent e) {
            Point point = e.getPoint();
            
            if (originalImage != null) {
                if (isNearResizeZone(point)) {
                    setCursor(Cursor.getPredefinedCursor(Cursor.SE_RESIZE_CURSOR));
                } else {
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            } 
            else {
                setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
            }
        }
    }
    
    /**
     * Ограничение значения в заданных пределах
     */
    private int constrain(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
       
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        int width = getWidth();
        int height = getHeight();
        
        // Создаем форму панели с закругленными углами
        RoundRectangle2D panelShape = new RoundRectangle2D.Double(0, 0, width, height, PANEL_CORNER_RADIUS, PANEL_CORNER_RADIUS);
        
        // Рисуем градиентный фон с закругленными углами
        GradientPaint gradient = new GradientPaint(0, 0, color1, 0, height, color2);
        g2d.setPaint(gradient);
        g2d.fill(panelShape);

        // Рисование изображения с отступом от края и закругленными углами
        if (processedImage != null) {
            // Изображение уже имеет закругленные углы благодаря обработке в updateProcessedImage()
            g2d.drawImage(processedImage, PADDING, PADDING, null);
        }
        
        // Рисуем только одну ручку изменения размера в правом нижнем углу
        if (originalImage != null && mouseOver && resizeZone != null) {
            drawResizeHandle(g2d);
        }
        
        g2d.dispose();
    }
    
    /**
     * Рисование единственной ручки изменения размера в правом нижнем углу
     */
    private void drawResizeHandle(Graphics2D g2d) {
        int x = resizeZone.x;
        int y = resizeZone.y;
        int size = RESIZE_HANDLE_SIZE;
        
        // Полупрозрачный фон
        g2d.setColor(new Color(50, 50, 50, 180));
        g2d.fillOval(x, y, size, size);
        
        // Белая окантовка
        g2d.setColor(new Color(255, 255, 255, 220));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawOval(x, y, size, size);
        
        // Диагональная линия для обозначения направления изменения размера
        g2d.setColor(new Color(255, 255, 255, 220));
        g2d.setStroke(new BasicStroke(2f));
        g2d.drawLine(x + 5, y + size - 5, x + size - 5, y + 5);
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        updateProcessedImage();
        updateResizeZone();
        if (selectImageButton != null && selectImageButton.getParent() != null) {
            selectImageButton.setBounds(PADDING, PADDING, getWidth() - 2 * PADDING, getHeight() - 2 * PADDING);
        }
        repaint();
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        updateProcessedImage();
        updateResizeZone();
        if (selectImageButton != null && selectImageButton.getParent() != null) {
            selectImageButton.setBounds(PADDING, PADDING, getWidth() - 2 * PADDING, getHeight() - 2 * PADDING);
        }
        repaint();
    }
    
    @Override
    public void setBounds(int x, int y, int width, int height) {
        super.setBounds(x, y, width, height);
        updateResizeZone();
        repaint();
    }

    // Геттеры
    public int getPosX() {
        return (int) this.getLocation().getX();
    }

    public int getPosY() {
        return (int) this.getLocation().getY();
    }

    public String getType() {
        return Type;
    }
    
    public String getImg() {
        return imagePath;
    }
    
    public Color getColor1() {
        return color1;
    }
    
    public Color getColor2() {
        return color2;
    }
    
    public int getPanelWidth() {
        return getWidth();
    }
    
    public int getPanelHeight() {
        return getHeight();
    }
    
    public Dimension getPanelSize() {
        return getSize();
    }
    
    public double getImageAspectRatio() {
        return imageAspectRatio;
    }
    
    public boolean hasImage() {
        return originalImage != null;
    }
}