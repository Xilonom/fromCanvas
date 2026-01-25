package myproject;

import java.awt.Color;
import java.awt.Container;
import java.awt.Point;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class DataManager {
    
    public static File loadedFile;

    public static void saveTemplate(String type,String Name, Point pos,String text,Color color1,Color color2) {
            JSONArray objectListJson = new JSONArray();
            File selectedFile = new File("src\\main\\resources\\" + Name + ".json");
            
            JSONObject item = new JSONObject();
            item.put("type", type);
            item.put("positionX", pos.getX());
            item.put("positionY", pos.getY());
            item.put("text", text);
            item.put("name", Name);
            item.put("color1", String.valueOf(color1.getRGB()));
            item.put("color2", String.valueOf(color2.getRGB()));
            objectListJson.add(item);

            try (FileWriter file = new FileWriter(selectedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved as " + selectedFile.getName());
            } catch (IOException e) {
                
            }

    }

    public static void loadTemplate(File file) {
        try {
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(file));
                JSONArray array = (JSONArray)obj;
                for(Object o : array) {

                JSONObject jsonObj = (JSONObject)o;

                JMenuItem tmplt = new JMenuItem(((String)jsonObj.get("name")));

                String Type = ((String)jsonObj.get("type"));
                String text = ((String)jsonObj.get("text"));
                Color color1 = new Color(Integer.parseInt(((String)jsonObj.get("color1"))), true);
                Color color2 = new Color(Integer.parseInt(((String)jsonObj.get("color2"))), true);

                if ("TxtPanel".equals(Type)) {
                    tmplt.addActionListener(e -> {
                        DraggableTextPanel panel = new DraggableTextPanel(100, 100, text, color1, color2);
                        MyFrame.contentPane.add(panel);
                        MyFrame.panels.add(panel);

                    });
                }
                MyFrame.AddTemplateItem(tmplt);
            }
                
        } catch (IOException | ParseException e) {
            
        }

        
    }

    public static void save(ArrayList<DraggableTextPanel> panels, ArrayList<DraggableImagePanel> ImgPanels, ArrayList<Arrow> ArrowPanels) {
        if (loadedFile != null) { 
            JSONArray objectListJson = new JSONArray();
            
            for (DraggableTextPanel obj : panels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                item.put("text", obj.getText());
                item.put("color1", String.valueOf(obj.getColor1().getRGB()));
                item.put("color2", String.valueOf(obj.getColor1().getRGB()));
                objectListJson.add(item);
            }

            for (Arrow obj : ArrowPanels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("positionX_1", obj.getPosX_1());
                item.put("positionY_1", obj.getPosY_1());
                item.put("positionX_2", obj.getPosX_2());
                item.put("positionY_2", obj.getPosY_2());
                item.put("Head_size", obj.getHead_size());
                item.put("color", String.valueOf(obj.getColor().getRGB()));
                objectListJson.add(item);
            }

            for (DraggableImagePanel obj : ImgPanels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("Img", obj.getImg());
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                item.put("color1", String.valueOf(obj.getColor1().getRGB()));
                item.put("color2", String.valueOf(obj.getColor1().getRGB()));

                objectListJson.add(item);
            }

            try (FileWriter file = new FileWriter(loadedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved to " + loadedFile.getName());
            } catch (IOException e) {
                
            }
        } else {
            saveAs(panels, ImgPanels, ArrowPanels); 
        }
    }

    public static void saveAs(ArrayList<DraggableTextPanel> panels, ArrayList<DraggableImagePanel> ImgPanels, ArrayList<Arrow> ArrowPanels) {
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
                item.put("color1", String.valueOf(obj.getColor1().getRGB()));
                item.put("color2", String.valueOf(obj.getColor1().getRGB()));
                objectListJson.add(item);
            }

            for (Arrow obj : ArrowPanels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("positionX_1", obj.getPosX_1());
                item.put("positionY_1", obj.getPosY_1());
                item.put("positionX_2", obj.getPosX_2());
                item.put("positionY_2", obj.getPosY_2());
                item.put("Head_size", obj.getHead_size());
                item.put("color", String.valueOf(obj.getColor().getRGB()));
                objectListJson.add(item);
            }

            for (DraggableImagePanel obj : ImgPanels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("Img", obj.getImg());
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                item.put("color1", String.valueOf(obj.getColor1().getRGB()));
                item.put("color2", String.valueOf(obj.getColor1().getRGB()));
                objectListJson.add(item);
            }

            try (FileWriter file = new FileWriter(loadedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved as " + loadedFile.getName());
            } catch (IOException e) {
                
            }
        }
    }

    public static void load(Container contentPane, File file, ArrayList<DraggableTextPanel> pnlList, ArrayList<DraggableImagePanel> ImgPnlList, ArrayList<Arrow> ArrowPnlList) {
        try {
                contentPane.removeAll();
                loadedFile = file;
                JSONParser parser = new JSONParser();
                Object obj = parser.parse(new FileReader(file));
                JSONArray array = (JSONArray)obj;

                for(Object o : array) {

                    JSONObject jsonObj = (JSONObject)o;

                    String Type = ((String)jsonObj.get("type"));

                    

                    System.out.println(Type);

                    if ("TxtPanel".equals(Type)) {

                        String text = ((String)jsonObj.get("text"));
                        long posX = ((Number)jsonObj.get("positionX")).longValue(); 
                        long posY = ((Number)jsonObj.get("positionY")).longValue();
                        Color color1 = new Color(Integer.parseInt(((String)jsonObj.get("color1"))), true);
                        Color color2 = new Color(Integer.parseInt(((String)jsonObj.get("color2"))), true);

                        DraggableTextPanel panel = new DraggableTextPanel((int)posX, (int)posY, text, color1, color2);
                        contentPane.add(panel);
                        pnlList.add(panel);
                    }
                    else if ("ImgPanel".equals(Type)) {
                        String Img = ((String)jsonObj.get("Img"));
                        long posX = ((Number)jsonObj.get("positionX")).longValue(); 
                        long posY = ((Number)jsonObj.get("positionY")).longValue();
                        Color color1 = new Color(Integer.parseInt(((String)jsonObj.get("color1"))), true);
                        Color color2 = new Color(Integer.parseInt(((String)jsonObj.get("color2"))), true);
                        System.out.println(Img);

                        DraggableImagePanel panel = new DraggableImagePanel((int)posX, (int)posY, Img, color1, color2);
                        contentPane.add(panel);
                        ImgPnlList.add(panel);
                    }
                    else if ("Arrow".equals(Type)) {
                        long posX_1 = ((Number)jsonObj.get("positionX_1")).longValue(); 
                        long posY_1 = ((Number)jsonObj.get("positionY_1")).longValue();
                        long posX_2 = ((Number)jsonObj.get("positionX_2")).longValue(); 
                        long posY_2 = ((Number)jsonObj.get("positionY_2")).longValue();
                        Color color = new Color(Integer.parseInt(((String)jsonObj.get("color"))), true);
                        long Head_size = ((Number)jsonObj.get("Head_size")).longValue();

                        Arrow arrow = new Arrow((int)posX_1, (int)posY_1, (int)posX_2, (int)posY_2, color, (int)Head_size);
                        contentPane.add(arrow);
                        ArrowPnlList.add(arrow);
                    }
                
                }
        } catch (IOException | ParseException e) {
            
        }

        
    }
}