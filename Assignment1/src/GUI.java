
//awt allows us to ask questions of the OS

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.concurrent.TimeUnit;

//command to sleep --> TimeUnit.seconds.sleep(1);
public class GUI extends JFrame {
    //global variables
    public static Node[][] gridOfNodes = null;
    public static int maxRows = 0;
    public static int maxColumns = 0;
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
        mainPanel.setLayout(new BorderLayout());


        //create panel for text input
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel,BoxLayout.Y_AXIS));

        //gridBuild panel
        JPanel gridBuild = new JPanel();


        JTextField textField = new JTextField("Input matrix dimensions",20);
        //create the generate button and add it to the text panel
        JButton generateButton = new JButton("Generate");
        JButton solveButton = new JButton("Solve");
        gridBuild.add(textField);
        gridBuild.add(generateButton);
        gridBuild.add(solveButton);

        buttonPanel.add(gridBuild);

        //iterate panel
        JPanel iteratePanel = new JPanel();
        JTextField iterationsTextField = new JTextField("Iterations for Hill Climb",20);
        JButton iterationsButton = new JButton("Generate");
        iteratePanel.add(iterationsTextField);
        iteratePanel.add(iterationsButton);



        buttonPanel.add(iteratePanel);

        //add the button panels to the main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        textField.requestFocus();

//      listen for submit button clicked
//      add panel to the frame
        generateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
//              grab the text field contents
//              check if the string is not valid
                String textFieldString = textField.getText();
                if(!(textFieldString.equals("5")) && !(textFieldString.equals("7"))&&!(textFieldString.equals("9"))&&!(textFieldString.equals("11"))){
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
                    maxRows = n;
                    maxColumns = n;
                    //create the grid of numbers
                    //create panel for grid
                    JPanel gridPanel = new JPanel();
                    //set the grid layout for the grid panel using the converted input
                    gridPanel.setLayout(new GridLayout(maxRows,maxColumns,0,0));
                    //create the 2d array
                    gridOfNodes = create2DArrayOfNodes(maxRows,maxColumns);
                    Node startNode = gridOfNodes[0][0];
                    Node goalNode = gridOfNodes[maxRows-1][maxColumns-1];
                    //add labels
                    for(int i = 0;i<maxRows;++i){
                        for(int j = 0;j<maxColumns;++j){
                            //create a label and add it to the layout
                            String labelNum = Integer.toString(gridOfNodes[i][j].getCellValue());
//                            gridRepresentation[i][j]= gridOfNodes[i][j].getLevel();
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

                    JPanel pathSuccessPanel = new JPanel();
                    JLabel pathSuccessLabel = new JLabel("text",JLabel.CENTER);
                    Font font = new Font("Arial Black",Font.BOLD,20);
                    pathSuccessLabel.setFont(font);
                    pathSuccessLabel.setBackground(Color.BLACK);
                    pathSuccessLabel.setOpaque(true);
                    mainPanel.add(pathSuccessPanel, BorderLayout.SOUTH);
                    //call BFS to see if there is a path
                    if(BFS(startNode,goalNode, gridOfNodes,visitedMatrix,maxRows,maxColumns)){
//                        gridRepresentation[0][0]=0;
                        if(pathSuccessPanel.getComponentCount()==1){
                            pathSuccessPanel.remove(0);
                        }
                        pathSuccessLabel.setText("There is a path.");
                        pathSuccessLabel.setForeground(Color.green);
                        pathSuccessPanel.add(pathSuccessLabel);
                    }
                    else{
                        if(pathSuccessPanel.getComponentCount()==1){
                            pathSuccessPanel.remove(0);
                        }
                        pathSuccessLabel.setText("There is no path.");
                        pathSuccessLabel.setForeground(Color.red);
                        pathSuccessPanel.add(pathSuccessLabel);
                    }
                    frame.revalidate();




                }
            }
        });


        //listen for solve button pressed
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(mainPanel.getComponentCount()==1){
                    //the user hasnt added a grid yet
                    //throw an error window
                    JOptionPane.showMessageDialog(frame,"You haven't generated a grid to solve.");
                    return;
                }
                //loop through the grid of nodes and create a 2d matrix from the
//                for(int i = 0;i<maxRows;++i){
//                    for(int j = 0;j<maxColumns;++j){
//                        //grab the level value and set the current cell to that value
//                        gridRepresentation[i][j] = gridOfNodes[i][j].getLevel();
//
//                    }
//                }
                //remove the grid panel
                Component[] comp = mainPanel.getComponents();
                for(int i=1;i<comp.length;++i){
                    mainPanel.remove(comp[i]);
                }
                frame.revalidate();
                frame.repaint();
                //create a new grid from the grid representation numbers
                JPanel gridPanel = new JPanel();
                //set the grid layout for the grid panel using the converted input
                gridPanel.setLayout(new GridLayout(maxRows,maxColumns,0,0));
                for(int i=0;i<maxRows;++i){
                    for(int j = 0;j<maxColumns;++j){
                        String labelNum = Integer.toString(gridOfNodes[i][j].getLevel());
                        //check for -1 meaning the node was never visited
                        if(gridOfNodes[i][j].getLevel()==0){
                            labelNum="X";
                        }
                        if(i==0&&j==0){
                            labelNum="0";
                        }
                        JLabel label = new JLabel(labelNum,SwingConstants.CENTER);
                        //set the border for each cell
                        label.setBorder(BorderFactory.createLineBorder(Color.black));
                        //add the label to the grid
                        gridPanel.add(label);
                    }
                }
//                gridRepresentation[]
                mainPanel.add(gridPanel);
                frame.revalidate();
                frame.repaint();

                //create a new grid
            }
        });




        //add the main panel to the fram
        this.add(mainPanel);

//      show the window
        this.setVisible(true);
        return;
    }



    /*---------UTILITY METHODS---------*/

    public static int getPuzzleValueFunction(){
        return 0;
    }
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
//        System.out.println("Max Rows: "+maxRows);
//        System.out.println("Max Col: "+maxCol);
        //Initialize an arraylist of Nodes
        ArrayList<Node> arrayOfNeighbors = new ArrayList<>();
//        System.out.println("Current cell value "+ currentNode.getCellValue());
//        System.out.println("Current cell position: ["+currentNode.getRowPos()+"] "+"["+currentNode.getColPos()+"]");
        //check if the Node has a top neighbor
        if(currentNode.getCellValue()<=currentNode.getRowPos()){
            //Node has a top neighbor
//            System.out.println("Top Neighbor position: "+"["+gridOfNodes[currentNode.getRowPos()-currentNode.getCellValue()][currentNode.getColPos()].getRowPos()+"] ["+gridOfNodes[currentNode.getRowPos()-currentNode.getCellValue()][currentNode.getColPos()].getColPos());
            //set the parent of the neighbor node to be the current node before adding it to the array of neighbors
            gridOfNodes[currentNode.getRowPos()-currentNode.getCellValue()][currentNode.getColPos()].setParent(currentNode);
            //add the neighbor node to the array of neighbors
            arrayOfNeighbors.add(gridOfNodes[currentNode.getRowPos()-currentNode.getCellValue()][currentNode.getColPos()]);

        }
        //check if the Node has a neighbor to the right of it

        if((maxCol-1)-currentNode.getColPos()>=currentNode.getCellValue()){
            //Node has a right neighbor
//            System.out.println("Right neighbor position: "+"["+gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()+currentNode.getCellValue()].getRowPos()+"] ["+gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()+currentNode.getCellValue()].getColPos());
            //set the parent
            gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()+currentNode.getCellValue()].setParent(currentNode);
            //add to the array
            arrayOfNeighbors.add(gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()+currentNode.getCellValue()]);
        }
        //check if the Node has a bottom neighbor
        if((maxRows-1)-currentNode.getRowPos()>=currentNode.getCellValue()){
            //Node has a bottom neighbor
//            System.out.println("Bottom neighbor row index "+(currentNode.getRowPos()+currentNode.getCellValue()));
//            System.out.println("Bottom neighbor col index "+currentNode.getColPos());
            //set the parent
            gridOfNodes[currentNode.getRowPos()+currentNode.getCellValue()][currentNode.getColPos()].setParent(currentNode);
            //add to the array
            arrayOfNeighbors.add(gridOfNodes[currentNode.getRowPos()+currentNode.getCellValue()][currentNode.getColPos()]);
        }
        //check if the node has a left neighbor
        if(currentNode.getCellValue()<=currentNode.getColPos()){
            //Node has a neighbor to the left of it
            //set the parent node
            gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()-currentNode.getCellValue()].setParent(currentNode);
            //add it to the array
            arrayOfNeighbors.add(gridOfNodes[currentNode.getRowPos()][currentNode.getColPos()-currentNode.getCellValue()]);
        }
    return arrayOfNeighbors;
    }
    public static boolean BFS(Node startNode,Node goalNode,Node[][] gridOfNodes,int[][] visitedMatrix, int maxRows, int maxCols){
        //initialize neighbor queue
        Queue<Node> neighborQ = new LinkedList<Node>();
        neighborQ.add(startNode);
        //set the level number for the start node
        startNode.setLevel(0);
        while(!(neighborQ.isEmpty())){
            Node currNode = neighborQ.remove();
            if(currNode.equals(goalNode)) {
                return true;
            }else {
                int rowPos = currNode.getRowPos();
                int colPos = currNode.getColPos();
                //check if visited
                if (visitedMatrix[rowPos][colPos] == 1) {
                    //the node has already been visited
                    continue;
                } else {
                    //mark the node as visited
                    visitedMatrix[rowPos][colPos] = 1;
                    //add all the neighbors of the current node to the queue
                    ArrayList<Node> arrayOfNeighbors = getNeighborsOfCurrentNode(currNode, gridOfNodes, maxRows, maxCols);
                    //add Nodes in array of neighbors to the queue
                    for (int i = 0; i < arrayOfNeighbors.size(); ++i) {
                        //check if any of the neighbors have been visited
                        if (visitedMatrix[arrayOfNeighbors.get(i).getRowPos()][arrayOfNeighbors.get(i).getColPos()] == 1) {
                            continue;
                        } else {
                            //set the level for each neighbor
                            arrayOfNeighbors.get(i).setLevel(currNode.getLevel() + 1);
                            neighborQ.add(arrayOfNeighbors.get(i));

                        }
                    }
                    //increment the level

                }
            }
        }
        return false;

    }

}
