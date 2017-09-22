
//awt allows us to ask questions of the OS

import java.awt.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

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
        this.setTitle("Local Search");

//      create main panel
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel,BoxLayout.Y_AXIS));


        //create panel for text input
        JPanel buttonPanel = new JPanel();
        JTextField textField = new JTextField("Input matrix dimensions",20);
        //create the submit button and add it to the text panel
        JButton generateButton = new JButton("Generate");
        buttonPanel.add(textField);
        buttonPanel.add(generateButton);

        //add the text panel to the main panel
        textField.requestFocus();
        mainPanel.add(buttonPanel);


//      listen for submit button clicked
//      add panel to the frame
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//              grab the text field contents
//              check if the string is not valid
                String textFieldString = textField.getText();
                if(!(textFieldString.equals("5")) && !(textFieldString.equals("7"))&&!(textFieldString.equals("9"))&&!(textFieldString.equals("11"))){
                    System.out.println("Failure");
                    // handle error case here with a error window?
                    JOptionPane.showMessageDialog(frame,"You've entered an invalid grid size. Valid sizes: 5, 7, 9, 11");
                    return;

                }else{
                    //check if the user already pressed the submit button so we don't keep adding grids to the layout
                    if(mainPanel.getComponentCount()>1){
                        //the user already added a grid so delete the grid and revalidate
                        Component[] comp = mainPanel.getComponents();
                        mainPanel.remove(comp[1]);
                        frame.revalidate();
                    }
                    //p
                    int n = Integer.parseInt(textFieldString);
                    int maxRows = n;
                    int maxColumns = n;
                    //create the grid of numbers
                    //create panel for grid
                    JPanel gridPanel = new JPanel();
                    //set the grid layout for the grid panel using the converted input
                    gridPanel.setLayout(new GridLayout(maxRows,maxColumns,0,0));
                    //create the 2d array
                    Node[][] gridOfNodes = create2DArrayOfNodes(maxRows,maxColumns);
                    Node startNode = gridOfNodes[0][0];
                    Node goalNode = gridOfNodes[maxRows-1][maxColumns-1];
                    //add labels
                    for(int i = 0;i<maxRows;++i){
                        for(int j = 0;j<maxColumns;++j){
                            //create a label and add it to the layout
                            String labelNum = Integer.toString(gridOfNodes[i][j].getCellValue());
                            JLabel label = new JLabel(labelNum,SwingConstants.CENTER);
                            //set the border for each cell
                            label.setBorder(BorderFactory.createLineBorder(Color.black));
                            //add the label to the grid
                            gridPanel.add(label);
                        }
                    }
                    mainPanel.add(gridPanel);
                    frame.revalidate();
                    int[][] visitedMatrix = create2DVisitedMatrix(maxRows,maxColumns);
                    //call BFS to see if there is a path
                    System.out.println(BFS(startNode,goalNode,gridOfNodes,visitedMatrix,maxRows,maxColumns));




                }
            }
        });
        this.add(mainPanel);

//      show the window
        this.setVisible(true);
        return;
    }



    /*---------UTILITY METHODS---------*/


    //get the grid number depending on the current row and column
    public static int generateGridNumber(int currentRow, int currentColumn,int maxRows, int maxColumns){
        int[] findMaxNumberOfMoves = new int[4];
        currentRow++;
        currentColumn++;
        findMaxNumberOfMoves[0] = maxRows-currentRow;
        findMaxNumberOfMoves[1] = currentRow-1;
        findMaxNumberOfMoves[2] = maxColumns-currentColumn;
        findMaxNumberOfMoves[3] = currentColumn-1;
        int maxNumOfMoves = Arrays.stream(findMaxNumberOfMoves).max().getAsInt();
        Random rand = new Random();
        int randomNumInValidRange = rand.nextInt((maxNumOfMoves - 1) + 1) + 1;

        return randomNumInValidRange;
    }
    //creates a valid 2d array of grid numbers
    public static Node[][] create2DArrayOfNodes(int rows, int columns){
        Node[][] array = new Node[rows][columns];
        for(int i = 0; i<rows;++i){
            for(int j = 0;j<columns;++j){
                array[i][j] = new Node(i,j,generateGridNumber(i,j,rows,columns));
            }
        }
        //set the goal cell to 0
        array[rows-1][columns-1].setCellValue(0);
        return array;
    }
    //creates the visited array initialized all to 0
    public static int[][] create2DVisitedMatrix(int rows, int columns){
        int[][] array = new int[rows][columns];
        for(int i = 0;i<rows;++i){
            for(int j = 0;j<columns;++j){
                array[i][j]=0;
            }
        }
        return array;
    }

    //---------BFS Methods---------

    public static ArrayList<Node> getNeighborsOfCurrentNode(Node currentNode,Node[][] gridOfNodes,int maxRows, int maxCol){
        System.out.println("Max Rows: "+maxRows);
        System.out.println("Max Col: "+maxCol);
        //Initialize an arraylist of Nodes
        ArrayList<Node> arrayOfNeighbors = new ArrayList<>();
        System.out.println("Current cell value "+ currentNode.getCellValue());
        System.out.println("Current cell position: ["+currentNode.getRowPos()+"] "+"["+currentNode.getColPos()+"]");
        //check if the Node has a top neighbor
        if(currentNode.getCellValue()<=currentNode.getRowPos()){
            //Node has a top neighbor
            System.out.println("Top Neighbor position: "+"["+gridOfNodes[currentNode.getRowPos()-currentNode.getCellValue()][currentNode.getColPos()].getRowPos()+"] ["+gridOfNodes[currentNode.getRowPos()-currentNode.getCellValue()][currentNode.getColPos()].getColPos());
            arrayOfNeighbors.add(gridOfNodes[currentNode.getRowPos()-currentNode.getCellValue()][currentNode.getColPos()]);

        }
        //check if the Node has a neighbor to the right of it

        if((maxCol-1)-currentNode.getColPos()>=currentNode.getCellValue()){
            //Node has a right neighbor
            System.out.println("Right neighbor position: "+"["+gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()+currentNode.getCellValue()].getRowPos()+"] ["+gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()+currentNode.getCellValue()].getColPos());
            arrayOfNeighbors.add(gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()+currentNode.getCellValue()]);
        }
        //check if the Node has a bottom neighbor
        if((maxRows-1)-currentNode.getRowPos()>=currentNode.getCellValue()){
            //Node has a bottom neighbor
            System.out.println("Bottom neighbor row index "+(currentNode.getRowPos()+currentNode.getCellValue()));
            System.out.println("Bottom neighbor col index "+currentNode.getColPos());
            arrayOfNeighbors.add(gridOfNodes[currentNode.getRowPos()+currentNode.getCellValue()][currentNode.getColPos()]);
        }
        //check if the node has a left neighbor
        if(currentNode.getCellValue()<=currentNode.getColPos()){
            //Node has a neighbor to the left of it
            arrayOfNeighbors.add(gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()-currentNode.getCellValue()]);
        }
    return arrayOfNeighbors;
    }
    public static boolean BFS(Node startNode,Node goalNode,Node[][] gridOfNodes,int[][] visitedMatrix, int maxRows, int maxCols){
        //initialize neighborqueue
        Queue<Node> neighborQ = new LinkedList<Node>();
        neighborQ.add(startNode);
        while(!(neighborQ.isEmpty())){
            Node currNode = neighborQ.remove();
            if(currNode.equals(goalNode)) {
                return true;
            }else{
                int rowPos = currNode.getRowPos();
                int colPos = currNode.getColPos();
                //check if visited
                if(visitedMatrix[rowPos][colPos]==1){
                    //the node has already been visited
                    continue;
                }else{
                    //mark the node as visited
                    visitedMatrix[rowPos][colPos]=1;
                    //add all the neighbors of the current node to the queue
                    ArrayList<Node> arrayOfNeighbors = getNeighborsOfCurrentNode(currNode,gridOfNodes,maxRows,maxCols);
                    //add Nodes in array of neighbors to the queue
                    for(int i = 0;i<arrayOfNeighbors.size();++i){
                        neighborQ.add(arrayOfNeighbors.get(i));
                    }

                }
            }
        }
        return false;

    }

}
