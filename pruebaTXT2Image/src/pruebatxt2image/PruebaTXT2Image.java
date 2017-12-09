/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pruebatxt2image;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.awt.image.SampleModel;
import java.awt.image.WritableRaster;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import javax.imageio.ImageIO;
import javax.swing.*;

/**
 *
 * @author invitado
 */
public class PruebaTXT2Image {

    /**
     * @param args the command line arguments
     */
    
    private static SampleModel sampleModel;
    
    public static void main(String[] args) {
        home_view a = new home_view();
        a.setVisible(true);
        
                
    }
    
    static public int[][] compute(File file)
    {
        try 
        {
            BufferedImage img= ImageIO.read(file);
            Raster raster=img.getData();
            sampleModel = raster.getSampleModel();
            int w=raster.getWidth(),h=raster.getHeight();
            int pixels[][]=new int[w][h];
            for (int x=0;x<w;x++)
            {
                for(int y=0;y<h;y++)
                {
                    pixels[x][y]=raster.getSample(x,y,0);
                }
            }

            return pixels;

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }
    
    public static Image getImage(int pixels[][])
    {
        int w = pixels.length;
        int h = pixels[0].length;
        
        WritableRaster raster = Raster.createWritableRaster(sampleModel, new Point(0, 0));
        for (int i = 0; i < w; i++) {
            for (int j = 0; j < h; j++) {
                raster.setSample(i, j, 0, pixels[i][j]);
            }
        }

        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        image.setData(raster);

        File output = new File("check.jpg");
        try {
            ImageIO.write(image, "jpg", output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return image;
    }
    
    public void procesar(String input){
        home_view vista = new home_view();
        vista.setLog("convirtiendo "+ input +" a imagen");
        
        String text = input;

        /*
           Because font metrics is based on a graphics context, we need to create
           a small, temporary image so we can ascertain the width and height
           of the final image
         */
        BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        Font font = new Font("Arial", Font.PLAIN, 100);//1000
        g2d.setFont(font);
        FontMetrics fm = g2d.getFontMetrics();
        int width = fm.stringWidth(text);
        int height = fm.getHeight();
        g2d.dispose();

        img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        g2d = img.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ALPHA_INTERPOLATION, RenderingHints.VALUE_ALPHA_INTERPOLATION_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
        g2d.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        g2d.setFont(font);
        fm = g2d.getFontMetrics();
        g2d.setColor(Color.BLACK);
        g2d.drawString(text, 0, fm.getAscent());
        g2d.dispose();
        try {
            //File image_file = new File("Textdsa.pgm");
            ImageIO.write(img, "png", new File("besito.png"));
            
            //int[][] pixeles = compute(image_file);//AUIQ AQUI
            //Image final_imagen = getImage(pixeles);
            
            // fill array with values, check entire image
            
        try(  PrintWriter out = new PrintWriter( "imagen_matrix.pgm" )  ){
            out.println( "P2" );
            out.println( width + " " + height );
            out.println( "255" );
            
            int[][] array2d = new int[width][height];
        
            for (int row = 0; row < height; row++) {
                String linea = "";
                for (int column = 0; column < width; column++) {

                    int clr = img.getRGB(column,row); // get color of pixel at position
                    //array2d[row][column] = clr;
                    //System.out.print(array2d[row][column]+" ");

                    if (clr == 0) { // white is -1
                        array2d[column][row] = 0;

                    } else {
                        array2d[column][row] = 255;
                    }
                    System.out.print(array2d[column][row]+" ");
                    linea += array2d[column][row]+" ";
                }
                out.println( linea );
                System.out.println("");
            }           
        }        
        //System.out.println(Arrays.deepToString(array2d));            
        } catch (IOException ex) {
            ex.printStackTrace();
        }  

    } 
    
    public static void show_img(){
        BufferedImage img = null;
        try 
        {
            img = ImageIO.read(new File("C:/ImageTest/pic2.jpg")); // eventually C:\\ImageTest\\pic2.jpg
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
    }
}
