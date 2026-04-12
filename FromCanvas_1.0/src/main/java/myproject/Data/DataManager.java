package myproject.Data;

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

import myproject.GUI.MyFrame;
import myproject.Objects.Arrow;
import myproject.Objects.DraggableImagePanel;
import myproject.Objects.DraggableTextPanel;




public class DataManager {
    
    private static final String CANVAS_EXTENSION = ".fcnv";
    private static final String TEMPLATE_EXTENSION = ".fcnt";
    private static final int AUTO_POSITION_SENTINEL = 1462683;

    public static File loadedFile;

    @SuppressWarnings("unchecked")
    public static void saveTemplate(String type,String Name, Point pos,int width,int height,String text,Color color1,Color color2) {
            JSONArray objectListJson = new JSONArray();
            File selectedFile = new File("src\\main\\resources\\" + Name + TEMPLATE_EXTENSION);
            
            JSONObject item = new JSONObject();
            item.put("type", type);
            item.put("positionX", pos.getX());
            item.put("positionY", pos.getY());
            item.put("text", text);
            item.put("name", Name);
            item.put("pnl_height", height);
            item.put("pnl_Width", width);
            item.put("color1", String.valueOf(color1.getRGB()));
            item.put("color2", String.valueOf(color2.getRGB()));
            objectListJson.add(item);

            try (FileWriter file = new FileWriter(selectedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved as " + selectedFile.getName());
            } catch (IOException e) {
                System.err.println("Failed to save template: " + e.getMessage());
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
                    MyFrame.AddNewJMenuItem(tmplt,text,color1,color2);
                }
                MyFrame.AddTemplateItem(tmplt);
            }
                
        } catch (IOException | ParseException e) {
            System.err.println("Failed to load template: " + e.getMessage());
        }

        
    }

    @SuppressWarnings("unchecked")
    public static void save(ArrayList<DraggableTextPanel> panels, ArrayList<DraggableImagePanel> ImgPanels, ArrayList<Arrow> ArrowPanels) {
        if (loadedFile != null) { 
            JSONArray objectListJson = new JSONArray();
            
            for (DraggableTextPanel obj : panels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                item.put("text", obj.getText());
                item.put("pnl_height",obj.getPanelHeight());
                item.put("pnl_Width",obj.getPanelWidth());
                item.put("color1", String.valueOf(obj.getColor1().getRGB()));
                item.put("color2", String.valueOf(obj.getColor2().getRGB()));
                objectListJson.add(item);
            }

            for (Arrow obj : ArrowPanels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("positionX_1", obj.getPosX_1());
                item.put("positionY_1", obj.getPosY_1());
                item.put("positionX_2", obj.getPosX_2());
                item.put("positionY_2", obj.getPosY_2());
                item.put("control_X", obj.getControlX());
                item.put("control_Y", obj.getControlY());
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
                item.put("pnl_height",obj.getPanelHeight());
                item.put("pnl_Width",obj.getPanelWidth());
                item.put("color1", String.valueOf(obj.getColor1().getRGB()));
                item.put("color2", String.valueOf(obj.getColor2().getRGB()));

                objectListJson.add(item);
            }

            try (FileWriter file = new FileWriter(loadedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved to " + loadedFile.getName());
            } catch (IOException e) {
                System.err.println("Failed to save file: " + e.getMessage());
            }
        } else {
            saveAs(panels, ImgPanels, ArrowPanels); 
        }
    }

    @SuppressWarnings("unchecked")
    public static void saveAs(ArrayList<DraggableTextPanel> panels, ArrayList<DraggableImagePanel> ImgPanels, ArrayList<Arrow> ArrowPanels) {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showSaveDialog(null); 

        if (result == JFileChooser.APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            
           
            if (!selectedFile.getName().endsWith(CANVAS_EXTENSION)) {
                selectedFile = new File(selectedFile.getPath() + CANVAS_EXTENSION);
            }

            loadedFile = selectedFile; 

            JSONArray objectListJson = new JSONArray();
            
            for (DraggableTextPanel obj : panels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("positionX", obj.getPosX());
                item.put("positionY", obj.getPosY());
                item.put("text", obj.getText());
                item.put("pnl_height",obj.getPanelHeight());
                item.put("pnl_Width",obj.getPanelWidth());
                item.put("color1", String.valueOf(obj.getColor1().getRGB()));
                item.put("color2", String.valueOf(obj.getColor2().getRGB()));
                objectListJson.add(item);
            }

            for (Arrow obj : ArrowPanels) {
                JSONObject item = new JSONObject();
                item.put("type", obj.getType());
                item.put("positionX_1", obj.getPosX_1());
                item.put("positionY_1", obj.getPosY_1());
                item.put("positionX_2", obj.getPosX_2());
                item.put("positionY_2", obj.getPosY_2());
                item.put("control_X", obj.getControlX());
                item.put("control_Y", obj.getControlY());
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
                item.put("pnl_height",obj.getPanelHeight());
                item.put("pnl_Width",obj.getPanelWidth());
                item.put("color1", String.valueOf(obj.getColor1().getRGB()));
                item.put("color2", String.valueOf(obj.getColor2().getRGB()));
                objectListJson.add(item);
            }

            try (FileWriter file = new FileWriter(loadedFile)) {
                file.write(objectListJson.toJSONString());
                System.out.println("Successfully saved as " + loadedFile.getName());
            } catch (IOException e) {
                System.err.println("Failed to save file: " + e.getMessage());
            }
        }
    }

    public static void load(Container contentPane, File file, ArrayList<DraggableTextPanel> pnlList, ArrayList<DraggableImagePanel> ImgPnlList, ArrayList<Arrow> ArrowPnlList) {
        try {
                contentPane.removeAll();
                pnlList.clear();
                ImgPnlList.clear();
                ArrowPnlList.clear();
                MyFrame.clearEditingState();
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
                        long height = ((Number)jsonObj.get("pnl_height")).longValue(); 
                        long width = ((Number)jsonObj.get("pnl_Width")).longValue(); 

                        boolean isAutoPosition = (int) posX == AUTO_POSITION_SENTINEL || (int) posY == AUTO_POSITION_SENTINEL;
                        DraggableTextPanel panel = new DraggableTextPanel(
                            isAutoPosition ? AUTO_POSITION_SENTINEL : (int) posX,
                            isAutoPosition ? AUTO_POSITION_SENTINEL : (int) posY,
                            text, color1, color2
                        );
                        panel.setPanelSize((int) width, (int)height);
                        contentPane.add(panel);
                        pnlList.add(panel);
                    }
                    else if ("ImgPanel".equals(Type)) {
                        String Img = ((String)jsonObj.get("Img"));
                        long posX = ((Number)jsonObj.get("positionX")).longValue(); 
                        long posY = ((Number)jsonObj.get("positionY")).longValue();
                        Color color1 = new Color(Integer.parseInt(((String)jsonObj.get("color1"))), true);
                        Color color2 = new Color(Integer.parseInt(((String)jsonObj.get("color2"))), true);
                        long height = ((Number)jsonObj.get("pnl_height")).longValue(); 
                        long width = ((Number)jsonObj.get("pnl_Width")).longValue(); 

                        DraggableImagePanel panel = new DraggableImagePanel((int)posX, (int)posY, Img, color1, color2, (int)width, (int)height);
                        contentPane.add(panel);
                        ImgPnlList.add(panel);
                    }
                    else if ("Arrow".equals(Type)) {
                        long posX_1 = ((Number)jsonObj.get("positionX_1")).longValue(); 
                        long posY_1 = ((Number)jsonObj.get("positionY_1")).longValue();
                        long posX_2 = ((Number)jsonObj.get("positionX_2")).longValue(); 
                        long posY_2 = ((Number)jsonObj.get("positionY_2")).longValue();
                        long cntrlX = ((Number)jsonObj.get("control_X")).longValue();
                        long cntrlY = ((Number)jsonObj.get("control_Y")).longValue();
                        Color color = new Color(Integer.parseInt(((String)jsonObj.get("color"))), true);
                        long Head_size = ((Number)jsonObj.get("Head_size")).longValue();

                        Arrow arrow = new Arrow((int)posX_1, (int)posY_1, (int)posX_2, (int)posY_2,new Point((int)cntrlX,(int)cntrlY), color, (int)Head_size);
                        contentPane.add(arrow);
                        ArrowPnlList.add(arrow);
                    }
                
                }
        } catch (IOException | ParseException e) {
            System.err.println("Failed to load file: " + e.getMessage());
        }

        
    }
}