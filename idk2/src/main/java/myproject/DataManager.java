package myproject;

import java.io.FileWriter;
import java.io.FileReader;
import java.awt.Container;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.Buffer;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

//import org.json.simple.JSONObject;


public class DataManager {
    

    public static void save(ArrayList<DraggableTextPanel> Panels) {

        JSONArray objectListJson = new JSONArray();

        for(DraggableTextPanel obj : Panels){
            JSONObject item = new JSONObject();
        
            item.put("positionX", obj.getPosX());
            item.put("positionY", obj.getPosY());
            item.put("text", obj.getText());
            
            objectListJson.add(item);
        }

        try (FileWriter file = new FileWriter("output.json")) {
        file.write(objectListJson.toJSONString());
        System.out.println("Successfully saved to output.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load(Container contentPane) {
        try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader("output.json"));
                JSONArray array = (JSONArray)obj;
                System.err.println("LOADING...");


                for(Object o : array) {
                JSONObject jsonObj = (JSONObject)o;
                
                // Извлекаем значения полей
                long posX = ((Number)jsonObj.get("positionX")).longValue(); // Приведение типов
                long posY = ((Number)jsonObj.get("positionY")).longValue();
                String text = ((String)jsonObj.get("text"));
                System.err.println("LOADING...");
                // Создаем новый объект и добавляем его в список
                DraggableTextPanel panel = new DraggableTextPanel((int)posX, (int)posY, text);
                contentPane.add(panel);


                System.err.println("LOADING...");
                

                //Panels.add(panel);
        }







        } catch (Exception e) {
        }
    }
}
