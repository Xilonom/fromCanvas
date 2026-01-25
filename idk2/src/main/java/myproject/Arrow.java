package myproject;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

import javax.swing.AbstractAction;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

public class Arrow extends JPanel {
    private Point startPoint;
    private Point endPoint;
    private Color color;
    private int headSize = 10;
    private boolean editing = false;
    private boolean draggingStart = false;
    private boolean draggingEnd = false;
    private Point offset;

    public Arrow(int x1, int y1, int x2, int y2, Color color, int headSize) {
        this.startPoint = new Point(x1, y1);
        this.endPoint = new Point(x2, y2);
        this.color = color;
        this.headSize = headSize; // Сохраняем размер


        setOpaque(false);
        setFocusable(true);
        setBounds(calculateBounds());

        setOpaque(false);
        setFocusable(true);
        setBounds(calculateBounds());


        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1) {
                    if (isPointOnLine(e.getPoint())) {
                        enterEditingMode();
                    }
                }
            }
        });


        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!editing || e.getButton() != MouseEvent.BUTTON1) return;

                Point local = e.getPoint();
                Point sLocal = toLocal(startPoint);
                Point eLocal = toLocal(endPoint);

                if (sLocal.distance(local) < 10) {
                    draggingStart = true;
                    offset = new Point(local.x - sLocal.x, local.y - sLocal.y);
                } else if (eLocal.distance(local) < 10) {
                    draggingEnd = true;
                    offset = new Point(local.x - eLocal.x, local.y - eLocal.y);
                }
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                draggingStart = false;
                draggingEnd = false;
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!editing || (!draggingStart && !draggingEnd)) return;

                Point local = e.getPoint();
                if (draggingStart) {
                    startPoint.setLocation(
                        local.x - offset.x + getX(),
                        local.y - offset.y + getY()
                    );
                } else if (draggingEnd) {
                    endPoint.setLocation(
                        local.x - offset.x + getX(),
                        local.y - offset.y + getY()
                    );
                }
                updateBounds();
                repaint();
            }
        });


        getInputMap(WHEN_FOCUSED).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "escape"
        );
        getActionMap().put("escape", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                exitEditingMode();
            }
        });
    }


    private boolean isPointOnLine(Point p) {
        Point startLocal = toLocal(startPoint);
        Point endLocal = toLocal(endPoint);

        double distance = Line2D.ptSegDist(
            (double) startLocal.x, (double) startLocal.y,
            (double) endLocal.x, (double) endLocal.y,
            p.x, p.y
        );
        return distance < 5;
    }

    private void enterEditingMode() {
        editing = true;
        repaint();
        requestFocusInWindow();
    }

    private void exitEditingMode() {
        if (!editing) return;
        editing = false;
        draggingStart = false;
        draggingEnd = false;
        repaint();
    }

    @Override
    public boolean contains(int x, int y) {
        if (editing) {
            Point p = new Point(x, y);
            Point sLocal = toLocal(startPoint);
            Point eLocal = toLocal(endPoint);
            return (sLocal.distance(p) < 10) || (eLocal.distance(p) < 10);
        }
        return isPointOnLine(new Point(x, y));
    }

    
    private Point toLocal(Point global) {
        return new Point(global.x - getX(), global.y - getY());
    }

    private Rectangle calculateBounds() {
        int minX = Math.min(startPoint.x, endPoint.x) - 10;
        int minY = Math.min(startPoint.y, endPoint.y) - 10;
        int maxX = Math.max(startPoint.x, endPoint.x) + 10;
        int maxY = Math.max(startPoint.y, endPoint.y) + 10;
        return new Rectangle(minX, minY, maxX - minX, maxY - minY);
    }

    private void updateBounds() {
        setBounds(calculateBounds());
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Включение сглаживания (антиалиасинга)
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                    RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING,
                    RenderingHints.VALUE_RENDER_QUALITY);


        // Остальные настройки (толщина линии, цвет и т.д.)
        g2.setStroke(new BasicStroke(3.0f));
        g2.setColor(color);


        Point sLocal = toLocal(startPoint);
        Point eLocal = toLocal(endPoint);


        g2.drawLine(sLocal.x, sLocal.y, eLocal.x, eLocal.y);
        drawArrowHead(g2, eLocal, sLocal, 7 );


        if (editing) {
            int d = 10;
            g2.setColor(Color.DARK_GRAY);
            g2.fillOval(sLocal.x - d/2, sLocal.y - d/2, d, d);
            g2.fillOval(eLocal.x - d/2, eLocal.y - d/2, d, d);
        }
    }


    private void drawArrowHead(Graphics2D g2, Point tip, Point base, double extension) {
        double dx = tip.x - base.x;
        double dy = tip.y - base.y;
        double length = Math.sqrt(dx * dx + dy * dy);


        if (length == 0) return;

        dx /= length;
        dy /= length;

        int extendedX = (int) (tip.x + dx * extension);
        int extendedY = (int) (tip.y + dy * extension);


        double angle = Math.atan2(dy, dx);

        Polygon arrow = new Polygon();
        arrow.addPoint(extendedX, extendedY);

        // Используем this.headSize для расчёта вершин треугольника
        arrow.addPoint(
            (int) (extendedX - this.headSize * Math.cos(angle - Math.PI / 6)),
            (int) (extendedY - this.headSize * Math.sin(angle - Math.PI / 6))
        );
        arrow.addPoint(
            (int) (extendedX - this.headSize * Math.cos(angle + Math.PI / 6)),
            (int) (extendedY - this.headSize * Math.sin(angle + Math.PI / 6))
        );

        g2.fill(arrow);
    }


    public String getType() {return "Arrow";}
    public double getPosX_1() { return (int)startPoint.getX(); }
    public double getPosY_1() { return (int)startPoint.getY(); }
    public double getPosX_2() { return (int)endPoint.getX(); }
    public double getPosY_2() { return (int)endPoint.getY(); }
    public Point getEndPoint() { return endPoint; }
    public Color getColor() { return color; }
    public int getHead_size() {return headSize;}
}
