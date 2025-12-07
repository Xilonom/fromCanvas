package myproject;

import java.io.FileWriter;
import java.io.FileReader;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.Reader;
import java.nio.Buffer;
import java.util.ArrayList;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

import javax.swing.JFileChooser;
import javax.swing.JFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import org.json.simple.JSONObject;


public class DataManager {
    
    public static File loadedFile;

    public static void save(ArrayList<DraggableTextPanel> panels) {
        if (loadedFile != null) { // Если файл уже выбран ранее
            JSONArray objectListJson = new JSONArray();
            
            for (DraggableTextPanel obj : panels) {
                JSONObject item = new JSONObject();
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                item.put("text", obj.getText());
                objectListJson.add(item);
            }

            try (FileWriter file = new FileWriter(loadedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved to " + loadedFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            saveAs(panels); // Запускаем процедуру выбора файла и пути
        }
    }

    public static void saveAs(ArrayList<DraggableTextPanel> panels) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null); // Показываем диалог выбора файла

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String extension = ".json";
            
            // Добавляем расширение .json, если оно отсутствует
            if (!selectedFile.getName().endsWith(".json")) {
                selectedFile = new File(selectedFile.getPath() + extension);
            }

            loadedFile = selectedFile; // Сохраняем путь выбранного файла

            JSONArray objectListJson = new JSONArray();
            
            for (DraggableTextPanel obj : panels) {
                JSONObject item = new JSONObject();
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                item.put("text", obj.getText());
                objectListJson.add(item);
            }

            try (FileWriter file = new FileWriter(loadedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved as " + loadedFile.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void load(Container contentPane, File file) {
        try {
                contentPane.removeAll();
                loadedFile = file;
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(file));
                JSONArray array = (JSONArray)obj;

                for(Object o : array) {
                JSONObject jsonObj = (JSONObject)o;
                long posX = ((Number)jsonObj.get("positionX")).longValue(); 
                long posY = ((Number)jsonObj.get("positionY")).longValue();
                String text = ((String)jsonObj.get("text"));


                DraggableTextPanel panel = new DraggableTextPanel((int)posX, (int)posY, text);
                contentPane.add(panel);
                
                }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
