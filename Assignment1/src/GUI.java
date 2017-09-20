
//awt allows us to ask questions of the OS
import com.sun.xml.internal.bind.v2.TODO;

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    public static void main(String[] args) {
        new GUI();
    }

    public GUI(){
        JFrame frame = this;
        this.setSize(750,750);
//      this.setLocationRelativeTo(null);
//      create new instance of the toolkit
        Toolkit tk = Toolkit.getDefaultToolkit();
//      get the current screen size that we are working from
        Dimension dim = tk.getScreenSize();
//      centers the window
        int xPos = (dim.width / 2) - (this.getWidth() / 2);
        int yPos = (dim.height / 2) - (this.getHeight() / 2);
//      set the position of the window
        this.setLocation(xPos,yPos);
//      prevent the resizing of the window
//      cleanup on close(red close button pressed)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      set the title of the window
        this.setTitle("Grid");

//      create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));


        //create panel for text input
        JPanel textPanel = new JPanel();
//      text field
        JTextField matrixField = new JTextField("Enter matrix dimensions", 20);
        matrixField.setToolTipText("Matrix dimensions");
        matrixField.requestFocus();
        textPanel.add(matrixField);
        //create the submit button and add it to the text panel
        JButton submitButton = new JButton("Submit");
        textPanel.add(submitButton);

        //add the text panel to the main panel
        mainPanel.add(textPanel);


//      listen for submit button clicked
//      add panel to the frame
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//              grab the text field contents
                String textFieldString = matrixField.getText();
//              check if the string is not valid
                            if(!(textFieldString.equals("5")) && !(textFieldString.equals("7"))&&!(textFieldString.equals("9"))&&!(textFieldString.equals("11"))){
                                System.out.println("Failure");
                                return;
                                // handle error case here with a error window?
                                //TODO: pop up a window alerting the user of an error
                            }else{
                                if(mainPanel.getComponentCount()>1){
                                    //the user already added a grid so delete the grid and revalidate
                                    Component[] comp = mainPanel.getComponents();
                                    mainPanel.remove(comp[1]);
                                    frame.revalidate();
                                }
                                // convert the inputted string to an integer
                                int parsedTextFieldNumber = Integer.parseInt(textFieldString);
                                //create the grid of numbers
                                //create panel for grid
                                JPanel gridPanel = new JPanel();
                                //set the grid layout for the grid panel using the converted input
                                gridPanel.setLayout(new GridLayout(parsedTextFieldNumber,parsedTextFieldNumber,0,0));
                                //add labels
                                for(int i = 0;i<parsedTextFieldNumber;++i){
                                    for(int j = 0;j<parsedTextFieldNumber;++j){
                                        //create a label and add it to the layout
                                        JLabel label = new JLabel("1",SwingConstants.CENTER);
                                        //set the border for each cell
                                        label.setBorder(BorderFactory.createLineBorder(Color.black));
                                        //add the label to the grid
                                        gridPanel.add(label);
                                    }
                                }
                    mainPanel.add(gridPanel);
                    frame.revalidate();
                }

            }
        });
        this.add(mainPanel);
        System.out.println(mainPanel.getComponentCount());


//      show the window
        this.setVisible(true);
    }
    //get the grid number depending on the current row and column
//    public static int generateGridNumber(int currentRow, int currentColumn){
//        return 0;
//    }
//    public static int[][] create2DArray(int rows, int columns, ){
//        int[][] array = new int[rows][columns];
//        //TODO: implement array with generated grid numbers
//        for(int i = 0; i<rows;++i){
//            for(int j = 0;j<columns;++j){
//                array[i][j] = generateGridNumber(i,j);
//            }
//        }
//        return array;
//    }
}
