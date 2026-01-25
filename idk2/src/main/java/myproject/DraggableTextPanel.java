package myproject;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

public class DraggableTextPanel extends JPanel {
    private Point startDragPos;
    private boolean dragging = false;
    private JTextArea textArea;
    String Type = "TxtPanel";
    int posX;
    int posY;
    String text;
    Color color1;
    Color color2;

    public DraggableTextPanel(int posX, int posY, String text, Color clr1, Color clr2) {

        textArea = new JTextArea("");
        this.setOpaque(true);
        this.setBackground(Color.ORANGE);
        this.setPreferredSize(new Dimension(150, 150)); 
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBounds(50, 50, 200, 100);
        this.setSize(150,150);

        color1 = clr1;
        color2 = clr2;
        
        
        textArea.setOpaque(false); 
        textArea.setPreferredSize(new Dimension(this.getSize().width - 10,this.getSize().height - 10));
        textArea.setBackground(this.getBackground().darker());   
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setDisabledTextColor(Color.BLACK);
        textArea.setEnabled(false); 
        this.add(textArea);
        textArea.setText(text);

        float fontSize = Math.min(getWidth() / 10f, getHeight() / 10f); 
        Font currentFont = textArea.getFont();
        Font newFont = currentFont.deriveFont(fontSize); 
        textArea.setFont(newFont);

        this.setLocation(new Point(posX, posY));
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { 
                    textArea.setEnabled(true); 
                    textArea.requestFocus();   
                }
            }
        });

       
        textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    textArea.setEnabled(false); 
                    repaint();        
                           
                }
            }
        });


       
        textArea.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                startDragPos = SwingUtilities.convertPoint(textArea, e.getPoint(), DraggableTextPanel.this);
                dragging = true;
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                dragging = false;
            }
        });

        
        textArea.addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                if (!dragging || textArea.isEnabled()) return;
                
                Point pos = e.getPoint();
                int dx = pos.x - startDragPos.x;
                int dy = pos.y - startDragPos.y;
                Point loc = getLocation();
                setLocation(loc.x + dx, loc.y + dy);
                

            }
        });
    
       
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
                if (!dragging || textArea.isEnabled()) return; 
                Point pos = e.getPoint();
                int dx = pos.x - startDragPos.x;
                int dy = pos.y - startDragPos.y;
                Point loc = getLocation();
                setLocation(loc.x + dx, loc.y + dy);
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int width = getWidth();
        int height = getHeight();
        
        GradientPaint gradient = new GradientPaint(0, 0, color1, 0, height, color2);
        g2.setPaint(gradient);
        g2.fill(new Rectangle2D.Double(0, 0, width, height));
    }

    public int getPosX() {
        System.err.println((int) this.getLocation().getX());
        return (int) this.getLocation().getX(); 
    }

    public int getPosY() {
        System.err.println((int) this.getLocation().getY());
        return (int) this.getLocation().getY(); 
    }

    public String getText() {
        return textArea.getText();
    }

    public String getType() {
        return Type;
    }

    public Color getColor1() {
        return color1;
    }
    public Color getColor2() {
        return color2;
    }
    
}