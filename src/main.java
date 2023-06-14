import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class main {
    //holds the images
    static BufferedImage newImageC = null;
    static BufferedImage currentImage = null;
    static BufferedImage newImageE = null;


    //finds all the surrounding (3x3) pixels
    public static List<int[]> allNeighbors(int x, int y){
        List<int[]> neighbors = new ArrayList<>(); //hold the found pixels
        if(x==0 && y == 0){ //checks if position is top left corner
            neighbors.add(new int[]{y + 1, x}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x + 1}); //adds pixel to list
            neighbors.add(new int[]{y, x + 1}); //adds pixel to list
        }else if(x==currentImage.getHeight()-1 && y==currentImage.getWidth()-1){ //checks if position is bottom right corner
            neighbors.add(new int[]{y - 1, x}); //adds pixel to list
            neighbors.add(new int[]{y - 1, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y, x - 1}); //adds pixel to list
        }else if(x==0 && y==currentImage.getWidth()-1){ //checks if position is top right corner
            neighbors.add(new int[]{y, x + 1}); //adds pixel to list
            neighbors.add(new int[]{y - 1, x}); //adds pixel to list
            neighbors.add(new int[]{y - 1, x + 1}); //adds pixel to list
        }else if(x==currentImage.getHeight()-1 && y==0){ //checks if position in bottom left corner
            neighbors.add(new int[]{y, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x}); //adds pixel to list
        }else if(x==0){ //checks if position is on the top edge
            neighbors.add(new int[]{y - 1, x}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x}); //adds pixel to list
            neighbors.add(new int[]{y - 1, x + 1}); //adds pixel to list
            neighbors.add(new int[]{y, x + 1}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x + 1}); //adds pixel to list
        }else if(y==0){ //checks if position is on the left edge
            neighbors.add(new int[]{y, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y, x + 1}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x + 1}); //adds pixel to list
        }else if(x==currentImage.getHeight()-1){ //checks if position is on the bottom edge
            neighbors.add(new int[]{y - 1, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y - 1, x}); //adds pixel to list
            neighbors.add(new int[]{y, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x}); //adds pixel to list
        }else if (y==currentImage.getWidth()-1){ //checks if position is on the right edge
            neighbors.add(new int[]{y - 1, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y - 1, x}); //adds pixel to list
            neighbors.add(new int[]{y - 1, x + 1}); //adds pixel to list
            neighbors.add(new int[]{y, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y, x + 1}); //adds pixel to list
        } else{
            neighbors.add(new int[]{y - 1, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y - 1, x}); //adds pixel to list
            neighbors.add(new int[]{y - 1, x + 1}); //adds pixel to list
            neighbors.add(new int[]{y, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y, x + 1}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x - 1}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x}); //adds pixel to list
            neighbors.add(new int[]{y + 1, x + 1}); //adds pixel to list
        }
        return neighbors; //return list of pixels
    }

    //finds the median color value in the 3x3
    public static int findMedian(Color[][] c , List<int[]> neighbors){
        int[] colors = new int[neighbors.size()]; //holds the colors of the 3x3
        int i=0;
        for (int[] neighbor: neighbors) { //cycles through each neighbor
            colors[i] = c[neighbor[0]][neighbor[1]].getBlue(); //add the color to the color array
            i++; //increases i by 1
        }
        Arrays.sort(colors); //sort the colors in order
        int median = colors.length/2; //finds the median color position in array
        return colors[median]; //returns the median color
    }

    //creates the smoothed Color[][] using median method
    public static Color[][] medianSmoothing(Color[][] image){
        Color[][] newIm = new Color[currentImage.getWidth()][currentImage.getHeight()];
        //cycles through each pixel
        for(int x=0;x<image[0].length;x++) {
            for (int y = 0; y<image.length; y++) {
                List<int[]> neighbors = allNeighbors(x,y); //gets the 3x3 for the pixel
                int newColor = findMedian(image, neighbors); //finds the new color for the pixel
                newIm[y][x] = new Color(newColor, newColor, newColor); //sets the new color for the pixel
            }
        }
        return newIm; //returns new Color[][]
    }

    //finds the mean color value in the 3x3
    public static int findMean(Color[][] c , List<int[]> neighbors, int x, int y){
        int colors = 0;
        int i=1; //keeps track of how many colors there are
        for (int[] neighbor: neighbors) { //cycles through each neighbor
            colors += c[neighbor[0]][neighbor[1]].getBlue(); //adds the color value to the other color values
            i++;
        }
        colors += c[y][x].getBlue(); //adds the initial position's color
        colors = colors/(i); //divides sum of colors by number of colors
        return colors; //returns new color
    }

    //creates the smoothed Color[][] using mean method
    public static Color[][] meanSmoothing(Color[][] image){
        Color[][] newIm = new Color[currentImage.getWidth()][currentImage.getHeight()];
        //cycles through each pixel
        for(int x=0;x<image[0].length;x++) {
            for (int y = 0; y<image.length; y++) {
                List<int[]> neighbors = allNeighbors(x,y); //gets the 3x3 for the pixel
                int newColor = findMean(image, neighbors, x, y); //finds the new color for the pixel
                newIm[y][x] = new Color(newColor, newColor, newColor); //sets the new color for the pixel
            }
        }
        return newIm; //returns new Color[][]
    }

    //gets the array used in the edge detection
    public static int[][] findArray(Color[][] c , List<int[]> neighbors, int x, int y){
        int[][] arr = new int[3][3];
        arr[1][1] = c[y][x].getBlue(); //sets starting color to middle of the 3x3
        for (int[] neighbor: neighbors) { //cycles through each neighbor
            int color = c[neighbor[0]][neighbor[1]].getBlue(); //stores the color value to put into position

            //finds the position of each pixel and puts it in the corresponding position of the 3x3
            if(x-neighbor[1]==1 && y-neighbor[0]==1){
                arr[0][0] = color;
            }else if(x-neighbor[1]==1 && y==neighbor[0]){
                arr[1][0] = color;
            }else if(x-neighbor[1]==1){
                arr[2][0] = color;
            }else if(x==neighbor[1] && y-neighbor[0]==1){
                arr[0][1] = color;
            }else if(x==neighbor[1]){
                arr[2][1] = color;
            }else if(y-neighbor[0]==1){
                arr[0][2] = color;
            }else if(y==neighbor[0]){
                arr[1][2] = color;
            }else{
                arr[2][2] = color;
            }
        }
        return arr; //returns the 3x3
    }

    //creates the edge image using sobel
    public static Color[][] Sobel(Color[][] image){
        Color[][] newIm = new Color[currentImage.getWidth()][currentImage.getHeight()];
        //cycles through each pixel
        for(int x=0;x<image[0].length;x++) {
            for (int y = 0; y<image.length; y++) {
                List<int[]> neighbors = allNeighbors(x,y); //gets the 3x3 for the pixel
                int[][] sobel = findArray(image, neighbors, x, y); //positions the 3x3 for the pixel
                //finds the new color
                int newColor = Math.abs((sobel[0][0]+(2*sobel[0][1])+sobel[0][2])-(sobel[2][0]+(2*sobel[2][1])+sobel[2][2]))
                        + Math.abs((sobel[0][2]+(2*sobel[1][2])+sobel[2][2])-(sobel[0][0]+(2*sobel[1][0])+sobel[0][2]));
                //handles edge of picture
                if(newColor>255){
                    newColor=255;
                }
                newIm[y][x] = new Color(newColor, newColor, newColor); //sets the new color for the pixel
            }
        }
        return newIm; //returns new Color[][]
    }

    //creates the edge image using Prewitt
    public static Color[][] Prewitt(Color[][] image){
        Color[][] newIm = new Color[currentImage.getWidth()][currentImage.getHeight()];
        //cycles through each pixel
        for(int x=0;x<image[0].length;x++) {
            for (int y = 0; y<image.length; y++) {
                List<int[]> neighbors = allNeighbors(x,y); //gets the 3x3 for the pixel
                int[][] arr = findArray(image, neighbors, x, y);//positions the 3x3 for the pixel
                //finds the new color
                int newColor = Math.abs((arr[0][0]+arr[0][1]+arr[0][2])-(arr[2][0]+arr[2][1]+arr[2][2]))
                        + Math.abs((arr[0][2]+arr[1][2]+arr[2][2])-(arr[0][0]+arr[1][0]+arr[0][2]));
                //handles edge of picture
                if(newColor>255){
                    newColor=255;
                }
                newIm[y][x] = new Color(newColor, newColor, newColor); //sets the new color for the pixel
            }
        }
        return newIm; //returns new Color[][]
    }


    //propmts user for choices and responds based on choices
    public static void main(String []args){
        //initializes the variables
        boolean stop = false;
        Scanner sc = new Scanner(System.in);
        File image = new File("images/Screenshot1843.png");
        Color[][] newColors = new Color[0][0];
        Color[][] edges = new Color[0][0];

        while(!stop) { //loops until user exits
            System.out.println(
                    """
                            Choose Photo
                            -----------------------------------
                            1. Man looking through telescope
                            2. Houses on a street
                            3. Lake with docks and bridge
                            4. Exit
                            """); //prints photo menu
            int photo = sc.nextInt(); //reads in user input
            sc.nextLine();
            if(photo > 3|| photo < 1){
                stop = true; //stops if user chooses to exit
            }else {
                //sets photo
                switch (photo) {
                    case 1 -> image = new File("images/Screenshot1843.png");
                    case 2 -> image = new File("images/img_1.png");
                    case 3 -> image = new File("images/img_2.png");
                    default -> stop = true;
                }
                try {
                    currentImage = ImageIO.read(image); //reads in photo
                } catch (IOException e) {
                    e.printStackTrace(); //catches any errors
                }

                Color[][] currentColors = new Color[currentImage.getWidth()][currentImage.getHeight()];

                //cycles through and sets current image
                for (int i = 0; i < currentImage.getHeight(); i++) {
                    for (int j = 0; j < currentImage.getWidth(); j++) {
                        currentColors[j][i] = new Color(currentImage.getRGB(j, i));
                    }
                }

                System.out.println(
                        """
                                Choose Smoothing and Edge Detection Process
                                -----------------------------------------------
                                1. Smoothing: Median, Edge Detection: Sobel
                                2. Smoothing: Median, Edge Detection: Prewitt
                                3. Smoothing: Mean, Edge Detection: Sobel
                                4. Smoothing: Mean, Edge Detection: Prewitt
                                """); //prints smoothing and edge detection menu
                int process = sc.nextInt(); //reads in user input
                sc.nextLine();
                switch (process) {
                    case 1 -> {
                        newColors = medianSmoothing(currentColors); //sets newColors
                        edges = Sobel(newColors); //sets edges
                    }
                    case 2 -> {
                        newColors = medianSmoothing(currentColors); //sets newColors
                        edges = Prewitt(newColors); //sets edges
                    }
                    case 3 -> {
                        newColors = meanSmoothing(currentColors); //sets newColors
                        edges = Sobel(newColors); //sets edges
                    }
                    case 4 -> {
                        newColors = meanSmoothing(currentColors); //sets newColors
                        edges = Prewitt(newColors); //sets edges
                    }
                    default -> stop = true; //sets stop to false
                }

                newImageC = new BufferedImage(newColors.length, newColors[0].length,
                            BufferedImage.TYPE_INT_RGB); //initializes new Color image

                //cycles through and set the colors for the image
                for (int x = 0; x < newColors.length; x++) {
                    for (int y = 0; y < newColors[0].length; y++) {
                        newImageC.setRGB(x, y, newColors[x][y].getRGB());
                    }
                }
                //Displays image using JFrame
                String windowTitle = "";
                new JFrame(windowTitle) {
                    {
                        final JLabel label = new JLabel("", new ImageIcon(newImageC), SwingConstants.CENTER);
                        add(label); //adds image to window
                        pack();
                        setVisible(true); //shows image
                    }
                };

                newImageE = new BufferedImage(edges.length, edges[0].length,
                        BufferedImage.TYPE_INT_RGB); //initializes new Edge image

                //cycles through and set the colors for the image
                for (int x = 0; x < edges.length; x++) {
                    for (int y = 0; y < edges[0].length; y++) {
                        newImageE.setRGB(x, y, edges[x][y].getRGB());
                    }
                }
                //Displays image using JFrame
                new JFrame(windowTitle) {
                    {
                        final JLabel label = new JLabel("", new ImageIcon(newImageE), SwingConstants.CENTER);
                        add(label); //adds image to window
                        pack();
                        setVisible(true); //shows image
                    }
                };
            }
        }
    }
}
