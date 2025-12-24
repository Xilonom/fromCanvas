package myproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

public class DraggableImagePanel extends JPanel {
    private Point startDragPos;
    private boolean dragging = false;


    private final int posX;
    private final int posY;

    String Type = "ImgPanel";


    private final Image originalImage;

    String imagePath = "";


    private Image processedImage;

    public DraggableImagePanel(int posX, int posY, String imgPath) {
        this.posX = posX;
        this.posY = posY;

        // Загружаем изображение
        originalImage = Toolkit.getDefaultToolkit().createImage(imgPath);

        imagePath = imgPath;

        // Ждем окончания загрузки изображения
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(originalImage, 0);
        try {
            mt.waitForID(0);
        } catch (InterruptedException ignored) {}

        // Настроим панель
        this.setOpaque(true);
        this.setBackground(Color.ORANGE);
        this.setPreferredSize(new Dimension(150, 150));
        this.setBorder(BorderFactory.createLineBorder(Color.BLACK));
        this.setLocation(new Point(posX, posY));
        this.setSize(150, 150);

        // Подгрузим начальное изображение
        updateProcessedImage();

        // Реакции на события мыши
        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startDragPos = e.getPoint();
                dragging = true;
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging) return;
                Point p = e.getPoint();
                int dx = p.x - startDragPos.x;
                int dy = p.y - startDragPos.y;
                Point l = getLocation();
                setLocation(l.x + dx, l.y + dy);
            }
        });
    }

    /**
     * Метод обработки изображения с сохранением пропорций.
     */
    private void updateProcessedImage() {
        if (originalImage != null) {
            int panelWidth = getWidth();
            int panelHeight = getHeight();
            int origImgWidth = originalImage.getWidth(this);
            int origImgHeight = originalImage.getHeight(this);

            // Рассчитываем новый размер изображения, учитывая ограничения по ширине и высоте
            float ratio = Math.min((float) panelWidth / origImgWidth, (float) panelHeight / origImgHeight);
            int newWidth = (int) (origImgWidth * ratio);
            int newHeight = (int) (origImgHeight * ratio);

            // Масштабируем изображение с плавностью
            processedImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();

        // Рисование градиентного фона
        Color topColor = new Color(255, 172, 54);
        Color bottomColor = new Color(255, 212, 54);
        GradientPaint gradient = new GradientPaint(0, 0, topColor, 0, height, bottomColor);
        g2d.setPaint(gradient);
        g2d.fill(new Rectangle2D.Double(0, 0, width, height));

        // Рисование изображения
        if (processedImage != null) {
            int xOffset = (width - processedImage.getWidth(this)) / 2;
            int yOffset = (height - processedImage.getHeight(this)) / 2;
            g.drawImage(processedImage, xOffset, yOffset, this);
        }
    }

    @Override
    public void setSize(Dimension d) {
        super.setSize(d);
        updateProcessedImage(); // Обновляем изображение при изменении размера панели
    }

    @Override
    public void setSize(int width, int height) {
        super.setSize(width, height);
        updateProcessedImage(); // Обновляем изображение при изменении размера панели
    }

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
}