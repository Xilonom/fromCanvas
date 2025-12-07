package myproject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DraggableTextPanel extends JPanel {
    private Point startDragPos;
    private boolean dragging = false;
    private JTextArea textArea;
    int posX;
    int posY;
    String text;

    public DraggableTextPanel(int posX, int posY, String text) {

        textArea = new JTextArea("");
        this.setOpaque(true);
        this.setBackground(Color.ORANGE);
        this.setPreferredSize(new Dimension(200, 100)); 
        this.setBorder(BorderFactory.createLineBorder(Color.black));
        this.setBounds(50, 50, 200, 100);
        this.setSize(150,150);
        
        
        textArea.setOpaque(false); 
        textArea.setPreferredSize(new Dimension(this.getSize().width - 10,this.getSize().height - 10));
        textArea.setBackground(this.getBackground().darker());   
        textArea.setLineWrap(true);
        textArea.setWrapStyleWord(true);
        textArea.setDisabledTextColor(Color.BLACK);
        textArea.setEnabled(false); 
        this.add(textArea);
        textArea.setText(text);

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

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
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
    
}