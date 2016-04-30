package r.nemiforest.map;

import r.nemiforest.map.data.RenderItems;
import r.nemiforest.map.data.Resources;
import r.nemiforest.map.render.ImageRender;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

public class UI extends JFrame{
    private JPanel panel1;
    private ImageRender renderer;
    private JFileChooser fileSaveDialog;
    private JFileChooser fileOpenDialog;

    private KeyListener pasteListener = new KeyListener() {
        @Override
        public void keyTyped(KeyEvent e) {}

        @Override
        public void keyPressed(KeyEvent e) {
            int keyCode = e.getKeyCode();
            boolean ctrl = e.isControlDown();

            if(ctrl) {
                if (keyCode == 83) {
                    saveImage();
                } else if (keyCode == 86) {
                    pasteImage(true);
                }else if (keyCode == 79) {
                    openImage();
                } else if(keyCode == 90){
                    renderer.undo();
                }
            }

            if(!ctrl){
                if(keyCode >= 48 && keyCode <= 57) {
                    int id = (keyCode - 48 + 1) % 9;
                    renderer.setItem(RenderItems.values()[id]);
                }else if(keyCode == 45) {
                    renderer.setItem(RenderItems.PLANT);
                }else if(keyCode == 61) {
                    renderer.setItem(RenderItems.TRAITOR);
                }else if(keyCode == 90 || keyCode == 67){
                    renderer.setItem(null);
                }else if(keyCode == 68){
                    renderer.setItem(RenderItems.DELETE);
                }
            }
        }


        @Override
        public void keyReleased(KeyEvent e) {}
    };

    private void openImage() {
        try{
            if(fileOpenDialog.showOpenDialog(UI.this) == JFileChooser.APPROVE_OPTION){
                File result = fileOpenDialog.getSelectedFile();
                BufferedImage img = ImageIO.read(result);
                renderer.setMap(img);
            }
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(UI.this,"Error loading image!","Nothing interesting happens :'(",JOptionPane.ERROR_MESSAGE);
        }
    }


    private void handleClick(int x, int y){
        if(x >= 720 && x <= 1000) {
            if (y >= 66 && y <= 594) {
                handleSkillNodeClick(y);
            } else if (y >= 650 && y <= 782){
                if(y <= 710){
                    renderer.setItem(RenderItems.PLANT);
                }else{
                    renderer.setItem(RenderItems.TRAITOR);
                }
            } else {
                renderer.setItem(null);
            }
        }
    }

    private void handleSkillNodeClick(int y) {
        int index = (y - 66) / 60;
        renderer.setItem(RenderItems.values()[index]);
    }


    public UI(){
        this.add((renderer = new ImageRender()));
        this.addMenu();
        this.setMinimumSize(renderer.getMinimumSize());
        JUtil.setFixedSize(this.getContentPane(),renderer.getMinimumSize());
        this.pack();

        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setupMenu();
        addMouseListener();

        this.addKeyListener(pasteListener);

        fileSaveDialog = new JFileChooser();
        fileSaveDialog.setFileFilter(JUtil.imageSaveFilter);
        fileSaveDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileSaveDialog.setMultiSelectionEnabled(false);

        fileOpenDialog = new JFileChooser();
        fileOpenDialog.setFileFilter(JUtil.imageOpenFilter);
        fileOpenDialog.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileOpenDialog.setMultiSelectionEnabled(false);

        this.setTitle("/r/nemiforest - Map creator");
        this.setResizable(false);
    }

    public static void main(String[] args){
        new UI().setVisible(true);
    }


    private void pasteImage(boolean silent){
        try{
            Transferable transferable = Toolkit.getDefaultToolkit().getSystemClipboard().getContents(null);
            if (transferable != null && transferable.isDataFlavorSupported(DataFlavor.imageFlavor)){
                BufferedImage img = (BufferedImage) transferable.getTransferData(DataFlavor.imageFlavor);
                renderer.setMap(img);
            } else {
                if(!silent)
                    JOptionPane.showMessageDialog(UI.this,"Error loading clipboard!","Nothing interesting happens :'(",JOptionPane.ERROR_MESSAGE);
            }
        }catch (Exception e){
            e.printStackTrace();
            if(!silent)
                JOptionPane.showMessageDialog(UI.this,"Error loading clipboard!","Nothing interesting happens :'(",JOptionPane.ERROR_MESSAGE);
        }
    }


    private void setupMenu(){
        JPopupMenu menu = new JPopupMenu();

        JMenuItem loadImage = new JMenuItem("Load image");
        JMenuItem pasteImage = new JMenuItem("Paste image");
        JMenuItem exampleImage = new JMenuItem("Load example map");
        JMenuItem exportImage = new JMenuItem("Save Map");

        menu.add(loadImage);
        menu.add(pasteImage);
        menu.add(new JSeparator());
        menu.add(exampleImage);
        menu.add(new JSeparator());
        menu.add(exportImage);

        exportImage.addActionListener(l -> saveImage());
        loadImage.addActionListener(l -> openImage());
        pasteImage.addActionListener(l -> pasteImage(false));
        exampleImage.addActionListener(l -> renderer.setMap(Resources.instance().EXAMPLE));


        renderer.setMenu(menu);
    }

    private void saveImage() {
        try{
            if(fileSaveDialog.showSaveDialog(UI.this) == JFileChooser.APPROVE_OPTION){
                File file = fileSaveDialog.getSelectedFile();
                BufferedImage bi = new BufferedImage(this.renderer.getWidth(),this.renderer.getHeight(),BufferedImage.TYPE_INT_ARGB);
                this.renderer.paintMap(bi.getGraphics(),false);
                ImageIO.write(bi,"png",file);
            }
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showMessageDialog(UI.this,"Error saving image!","Nothing interesting happens :'(",JOptionPane.ERROR_MESSAGE);
        }
    }

    private void addMouseListener(){
        this.renderer.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }

            @Override
            public void mousePressed(MouseEvent e) {
                handleClick(e.getX(),e.getY());
            }
            @Override
            public void mouseReleased(MouseEvent e) {}

            @Override
            public void mouseEntered(MouseEvent e) {}

            @Override
            public void mouseExited(MouseEvent e) {}
        });
    }

    private void addMenu(){
        JMenu file,edit,tools,help;
        JMenuItem pasteImage,loadImage,loadImage_2,exampleImage,exportImage,undo;

        JMenuBar menu = new JMenuBar();
        menu.add(file = new JMenu("File"));
        menu.add(edit = new JMenu("Edit"));
        menu.add(tools = new JMenu("Tools"));
        menu.add(help = new JMenu("Help"));

        edit.add(pasteImage = new JMenuItem("Paste map"));
        file.add(loadImage = new JMenuItem("Load image"));
        file.add(exportImage = new JMenuItem("Save"));
        edit.add(loadImage_2 = new JMenuItem("Load image"));
        edit.add(undo = new JMenuItem("Undo"));
        help.add(exampleImage = new JMenuItem("Load example map"));


        exportImage.addActionListener(l -> saveImage());
        loadImage.addActionListener(l -> openImage());
        loadImage_2.addActionListener(l -> openImage());
        pasteImage.addActionListener(l -> pasteImage(false));
        exampleImage.addActionListener(l -> renderer.setMap(Resources.instance().EXAMPLE));
        undo.addActionListener(l -> UI.this.renderer.undo());
//

        for(RenderItems item : RenderItems.values()){
            final RenderItems _item = item;
            JMenuItem menuItem = new JMenuItem(item.getName());
            tools.add(menuItem);
            menuItem.addActionListener(l -> UI.this.renderer.setItem(_item));
        }

        JMenuItem menuItem = new JMenuItem("Path");
        tools.add(menuItem);
        menuItem.addActionListener(l -> UI.this.renderer.setItem(null));

        this.setJMenuBar(menu);
    }
}
