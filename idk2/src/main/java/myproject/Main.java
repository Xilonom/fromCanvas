package myproject;

import java.awt.EventQueue;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.UIManager;

import com.formdev.flatlaf.FlatDarculaLaf;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                
                UIManager.setLookAndFeel(new FlatDarculaLaf());
            } catch (Exception e) {
                System.err.println("Ошибка при установке FlatLaf: " + e.getMessage());
            }

            MyFrame frame = new MyFrame();
            
            
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setUndecorated(true); 
            frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
            frame.setVisible(true);
            File dir = new File(TemplateMenu.RESOURCES_DIR);
            File[] files = dir.listFiles();
            for (int i = files.length - 1; i >= 0; i--) {
            if (files[i].isFile()) {
                DataManager.loadTemplate(files[i]);
            }

        }
        });
    }
}
