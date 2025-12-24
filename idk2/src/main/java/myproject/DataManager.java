package myproject;

import java.awt.Container;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class DataManager {
    
    public static File loadedFile;

    public static void save(ArrayList<DraggableTextPanel> panels, ArrayList<DraggableImagePanel> ImgPanels) {
        if (loadedFile != null) { 
            JSONArray objectListJson = new JSONArray();
            
            for (DraggableTextPanel obj : panels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                item.put("text", obj.getText());
                objectListJson.add(item);
            }

            for (DraggableImagePanel obj : ImgPanels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("Img", obj.getImg());
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());

                objectListJson.add(item);
            }

            try (FileWriter file = new FileWriter(loadedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved to " + loadedFile.getName());
            } catch (IOException e) {
                
            }
        } else {
            saveAs(panels, ImgPanels); 
        }
    }

    public static void saveAs(ArrayList<DraggableTextPanel> panels, ArrayList<DraggableImagePanel> ImgPanels) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null); 

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            String extension = ".json";
            
           
            if (!selectedFile.getName().endsWith(".json")) {
                selectedFile = new File(selectedFile.getPath() + extension);
            }

            loadedFile = selectedFile; 

            JSONArray objectListJson = new JSONArray();
            
            for (DraggableTextPanel obj : panels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                item.put("text", obj.getText());
                objectListJson.add(item);
            }

            for (DraggableImagePanel obj : ImgPanels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("Img", obj.getImg());
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                objectListJson.add(item);
            }

            try (FileWriter file = new FileWriter(loadedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved as " + loadedFile.getName());
            } catch (IOException e) {
                
            }
        }
    }

    public static void load(Container contentPane, File file, ArrayList<DraggableTextPanel> pnlList, ArrayList<DraggableImagePanel> ImgPnlList) {
        try {
                contentPane.removeAll();
                loadedFile = file;
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(file));
                JSONArray array = (JSONArray)obj;

                for(Object o : array) {

                    JSONObject jsonObj = (JSONObject)o;

                    String Type = ((String)jsonObj.get("type"));
                    long posX = ((Number)jsonObj.get("positionX")).longValue(); 
                    long posY = ((Number)jsonObj.get("positionY")).longValue();

                    System.out.println(Type);

                    if ("TxtPanel".equals(Type)) {

                        String text = ((String)jsonObj.get("text"));

                        DraggableTextPanel panel = new DraggableTextPanel((int)posX, (int)posY, text);
                        contentPane.add(panel);
                        pnlList.add(panel);
                    }
                    else if ("ImgPanel".equals(Type)) {
                        String Img = ((String)jsonObj.get("Img"));
                        System.out.println(Img);

                        DraggableImagePanel panel = new DraggableImagePanel((int)posX, (int)posY, Img);
                        contentPane.add(panel);
                        ImgPnlList.add(panel);
                    }
                
                }
        } catch (IOException | ParseException e) {
            
        }
    }
}