
//awt allows us to ask questions of the OS

import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;
import java.io.*;
import java.util.concurrent.TimeUnit;

//command to sleep --> TimeUnit.seconds.sleep(1);
public class GUI extends JFrame {
    //global variables
    public static Node[][] gridOfNodes = null;
    public static int maxRows = 0;
    public static int maxColumns = 0;
    public static void main(String[] args) {
        new GUI();
        createDataForTask3();

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

        //Add labels for tasks
        JLabel task1Label = new JLabel("Task 1");
        JTextField textField = new JTextField("Input matrix dimensions",20);
        //create the generate button and add it to the text panel
        JButton generateButton = new JButton("Generate");
        JButton solveButton = new JButton("Solve");
        gridBuild.add(task1Label);
        gridBuild.add(textField);
        gridBuild.add(generateButton);
        gridBuild.add(solveButton);

        buttonPanel.add(gridBuild);


        //parse text file panel
        JPanel textFileInputPanel = new JPanel();
        JLabel task2Label = new JLabel("Task 2");
        JTextField fileTextField = new JTextField("Enter the name of the file containing the puzzle to be solved");
        JButton generatePuzzleButton = new JButton("Generate");
        JButton fileSolveButton = new JButton("Solve");
        textFileInputPanel.add(task2Label);
        textFileInputPanel.add(fileTextField);
        textFileInputPanel.add(generatePuzzleButton);
        textFileInputPanel.add(fileSolveButton);
        buttonPanel.add(textFileInputPanel);



        //iterate panel
        JPanel iteratePanel = new JPanel();
        JLabel task3Label = new JLabel("Task 3");
        JTextField iterationsTextField = new JTextField("Iterations for Hill Climb",20);
        JButton iterationsButton = new JButton("Generate");
        iteratePanel.add(task3Label);
        iteratePanel.add(iterationsTextField);
        iteratePanel.add(iterationsButton);



        buttonPanel.add(iteratePanel);

        //hill climbing vs hill climbing with restarts panel (task 4)
        JPanel hillClimbingVsHillClimbingWithRestarts = new JPanel();
        JLabel task4Label = new JLabel("Task 4");
        JTextField numOfRestarts = new JTextField("Number of restarts",20);
        JTextField numOfHillClimbIterations = new JTextField("Number of hill climb iterations",20);
        JButton hillClimbWithRestartsSolveButton = new JButton("Solve");
        //add elements to panel
        hillClimbingVsHillClimbingWithRestarts.add(task4Label);
        hillClimbingVsHillClimbingWithRestarts.add(numOfRestarts);
        hillClimbingVsHillClimbingWithRestarts.add(numOfHillClimbIterations);
        hillClimbingVsHillClimbingWithRestarts.add(hillClimbWithRestartsSolveButton);
        //add panel to overall button panel
        buttonPanel.add(hillClimbingVsHillClimbingWithRestarts);




        //add the button panels to the main panel
        mainPanel.add(buttonPanel, BorderLayout.NORTH);
        textField.requestFocus();

//      listen for generate button clicked for random puzzle
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
                    if(!(thereIsNoGrid(mainPanel))){
                        //the user already added a grid so delete the grid and revalidate
                       removeGrid(mainPanel);
                       frame.revalidate();
                       frame.repaint();
                    }
                    //p
                    int n = Integer.parseInt(textFieldString);
                    maxRows = n;
                    maxColumns = n;
                    //create and add the grid of numbers
                    Node[][] gridOfNodes=createAndAddRandomGrid(mainPanel,maxRows,maxColumns);
                    //create references to the start and goal nodes
                    Node startNode = gridOfNodes[0][0];
                    Node goalNode = gridOfNodes[maxRows-1][maxColumns-1];
                    //update the window
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


        //listen for solve button pressed for random puzzle
        solveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(thereIsNoGrid(mainPanel)){
                    //the user hasnt added a grid yet
                    //throw an error window
                    JOptionPane.showMessageDialog(frame,"You haven't generated a grid to solve.");
                    return;
                }
                //removed the grid and revalidate the frame
                removeGrid(mainPanel);
                frame.revalidate();
                frame.repaint();

                //create a new grid from the node levels
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

        //listen for generate button pressed for text file input
        generatePuzzleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //grab the name of the file inputted into the text field
                String nameOfFile = fileTextField.getText();
                System.out.println(nameOfFile);
                if(nameOfFile.length()==0){
                    //add an error message for length of 0 file name
                    JOptionPane.showMessageDialog(frame,"File name cannot be empty.");
                    return;
                }
                try {

                    //br = new BufferedReader(new FileReader(FILENAME));
                    FileReader fr = new FileReader(nameOfFile);
                    BufferedReader br = new BufferedReader(fr);

                    String currentLine;
                    int currentRow = 0;
                    int currentColumn = 0;

                    int gridDimension = Integer.parseInt(br.readLine());
                    //initialize a grid of Nodes
                    gridOfNodes = new Node[gridDimension][gridDimension];
                    //create a visited array
                    int[][] visitedArr = create2DVisitedMatrix(gridDimension,gridDimension);
                    //loop through the lines
                    while ((currentLine = br.readLine()) != null) {
                        for(int i=0;i<currentLine.length();i++){
                            if(currentLine.charAt(i)==' '){
                                continue;
                            }else{
//                                System.out.print(currentLine.charAt(i)+" ");
                                gridOfNodes[currentRow][currentColumn]= new Node(currentRow,currentColumn,Character.getNumericValue(currentLine.charAt(i)));
//                                System.out.println(gridOfNodesFromTextFile[currentRow][currentColumn].getCellValue());
                                currentColumn+=1;
                            }
                        }
                        currentRow+=1;
                        currentColumn=0;
                    }
                    //remove the grid if there is one there
                    if(!(thereIsNoGrid(mainPanel))){
                        removeGrid(mainPanel);
                    }
                    //create the grid and add it
                    JPanel gridPanel = new JPanel();
                    //set the grid layout for the grid panel using the converted input
                    gridPanel.setLayout(new GridLayout(gridDimension,gridDimension,0,0));

                    for(int i = 0;i<gridDimension;i++){
                        for(int j=0;j<gridDimension;j++){
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
                    //initialize start and goal node
                    Node startNode = gridOfNodes[0][0];
                    Node goalNode = gridOfNodes[gridDimension-1][gridDimension-1];

                    mainPanel.add(gridPanel);
                    JPanel pathSuccessPanel = new JPanel();
                    JLabel pathSuccessLabel = new JLabel("text",JLabel.CENTER);
                    Font font = new Font("Arial Black",Font.BOLD,20);
                    pathSuccessLabel.setFont(font);
                    pathSuccessLabel.setBackground(Color.BLACK);
                    pathSuccessLabel.setOpaque(true);
                    mainPanel.add(pathSuccessPanel, BorderLayout.SOUTH);
                    //call BFS to see if there is a path
                    if(BFS(startNode,goalNode,gridOfNodes,visitedArr,gridDimension,gridDimension)){
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
                    frame.revalidate();
                    frame.repaint();


                } catch (IOException err) {

                    return;

                }

            }
        });
        fileSolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(thereIsNoGrid(mainPanel)){
                    //the user hasnt added a grid yet
                    //throw an error window
                    JOptionPane.showMessageDialog(frame,"You haven't generated a grid to solve.");
                    return;
                }
                //removed the grid and revalidate the frame
                removeGrid(mainPanel);
                frame.revalidate();
                frame.repaint();

                //create a new grid from the node levels
                JPanel gridPanel = new JPanel();
                //set the grid layout for the grid panel using the converted input
                int rows = gridOfNodes.length;
                int columns = gridOfNodes.length;
                gridPanel.setLayout(new GridLayout(rows,columns,0,0));
                for(int i=0;i<rows;++i){
                    for(int j = 0;j<columns;++j){
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


            }
        });

        //generate button for hill climbing pressed
        iterationsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //grab the text field contents
                String numOfIterations = iterationsTextField.getText();
                //set a boolean to check if the string is a valid number
                boolean validString = true;
                int numberOfIterations=0;
                //loop through the string checking if each character is a digit
                for (int i = 0; i < numOfIterations.length(); i++) {
                    if (!Character.isDigit(numOfIterations.charAt(i))) {
                        validString=false;
                        break;
                    }
                }
                if(!validString){
                    //throw an error
                    JOptionPane.showMessageDialog(frame,"You've entered an invalid number.");
                    return;
                }else{
                    //check if the number of iterations is less than 50
                    numberOfIterations = Integer.parseInt(numOfIterations);
                    if(numberOfIterations<50){
                        //throw an error
                        JOptionPane.showMessageDialog(frame,"Iterations must be 50 or more.");
                        return;
                    }
                }//end validating string text field contents


                //check if there is a grid
                if(thereIsNoGrid(mainPanel)){
                    //throw an error
                    JOptionPane.showMessageDialog(frame,"Generate a grid to perform a hill climb first");
                    return;
                }

                //start a timer
                long startTime = System.nanoTime();
                int evaluationScore = 0;
                int newEvaluationScore=0;

                //calculate the evaluation score for the starting grid
                boolean isSolvable=false;
                int initialEvalScore = evaluationFunction(gridOfNodes,gridOfNodes.length,gridOfNodes.length);
                evaluationScore = initialEvalScore;
                    System.out.println("Initial evalutation score: "+evaluationScore);
                    //loop the amount of iterations
                    for(int i = 0;i<numberOfIterations;i++){
                        Random rand = new Random();
                        int randomRow = rand.nextInt(gridOfNodes.length);
                        int randomCol = rand.nextInt(gridOfNodes.length);
                        //loop until the random cell is not the goal node
                        while(randomRow==gridOfNodes.length-1 && randomCol==gridOfNodes.length-1){
                            randomRow=rand.nextInt(gridOfNodes.length);
                            randomCol=rand.nextInt(gridOfNodes.length);
                        }
                        //reset the grid's node levels
                        gridOfNodes = clearGridNodeLevels(gridOfNodes);
//                    System.out.println("random row: "+randomRow);
//                    System.out.println("random col: "+randomCol);
                        //store the node in a temp variable so we can hold onto it if we need to change the grid back
                        int oldCellNumber = gridOfNodes[randomRow][randomCol].getCellValue();
//                    System.out.println("old cell number: "+oldCellNumber);
                        //generate a valid random number for that cells position
                        int newCellNumber = generateGridNumber(randomRow,randomCol,gridOfNodes.length,gridOfNodes.length);
//                    System.out.println("new cell number: "+newCellNumber);

                        //change the node's cell value to that new number
                        gridOfNodes[randomRow][randomCol].setCellValue(newCellNumber);
                        //run BFS to set the grid node levels with the possibly new cell value number
                        isSolvable = BFS(gridOfNodes[0][0],gridOfNodes[gridOfNodes.length-1][gridOfNodes.length-1],gridOfNodes,create2DVisitedMatrix(gridOfNodes.length,gridOfNodes.length),gridOfNodes.length,gridOfNodes.length);
                        newEvaluationScore = evaluationFunction(gridOfNodes,gridOfNodes.length,gridOfNodes.length);
                        if(isSolvable){//puzzle can be solved
                            if(evaluationScore<0||newEvaluationScore<evaluationScore){//newEvaluation will be positive so we immediately set evaluation score to new evaluation score
                                evaluationScore=newEvaluationScore;
                            }else{
                                gridOfNodes[randomRow][randomCol].setCellValue(oldCellNumber);
                            }
                        }else{ //puzzle cant be solved
                            if(evaluationScore>0){
                                gridOfNodes[randomRow][randomCol].setCellValue(oldCellNumber);


                            }else{
                                if(newEvaluationScore>evaluationScore){
                                    evaluationScore=newEvaluationScore;
                                }
                                else{
                                    gridOfNodes[randomRow][randomCol].setCellValue(oldCellNumber);
                                }
                            }

                        }

                    }
                System.out.println("Ending evaluation score: "+evaluationScore);
                //remove the current grid and create a new one and add it
                removeGrid(mainPanel);
                addGridToLayout(mainPanel, gridOfNodes);
                long endTime = System.nanoTime();
                long totalTime = endTime-startTime;
                double seconds = (double)totalTime / 1000000000.0;
                frame.revalidate();
                frame.repaint();
                //add a penal showing the evaluation function and
                JPanel statPanel = new JPanel();
                statPanel.setLayout(new BoxLayout(statPanel,BoxLayout.Y_AXIS));
//                mainPanel.add(statPanel);
                //add a path success label, the old evaluation score, the new evaluation score and the time it took to calculate
                Font font = new Font("Arial Black",Font.BOLD,20);

                //path success label
                JLabel pathSuccess = new JLabel("There is no path.", JLabel.CENTER);
                pathSuccess.setFont(font);
                pathSuccess.setBackground(Color.BLACK);
                pathSuccess.setForeground(Color.RED);

                //initial eval score label
                JLabel initialEvalScoreLabel = new JLabel("Initial Evaluation Score: "+Integer.toString(initialEvalScore),JLabel.CENTER);
                initialEvalScoreLabel.setFont(font);


                //new eval score label
                JLabel newEvalScoreLabel = new JLabel("New Evaluation Score: "+Integer.toString(newEvaluationScore),JLabel.CENTER);
                newEvalScoreLabel.setFont(font);


                //time taken label
                JLabel timeTaken = new JLabel("Time Elapsed: "+Double.toString(seconds),JLabel.CENTER);
                timeTaken.setFont(font);


                if(isSolvable){
                    pathSuccess.setText("There is a path.");
                    pathSuccess.setForeground(Color.GREEN);
                    initialEvalScoreLabel.setForeground(Color.GREEN);
                    newEvalScoreLabel.setForeground(Color.GREEN);
                    timeTaken.setForeground(Color.GREEN);
                }
                statPanel.add(pathSuccess);
                statPanel.add(initialEvalScoreLabel);
                statPanel.add(newEvalScoreLabel);
                statPanel.add(timeTaken);

                mainPanel.add(statPanel, BorderLayout.SOUTH);

                //add a penal showing the evaluation function and
                frame.revalidate();
                frame.repaint();

            }
        });

        hillClimbWithRestartsSolveButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //check if there is a grid for us to run on
                if(thereIsNoGrid(mainPanel)){
                    JOptionPane.showMessageDialog(frame,"You haven't generated a grid to solve.");
                    return;

                }
                boolean fieldsAreValid = true;
                String numOfRestartsInput = numOfRestarts.getText();
                String numOfIterationsInput = numOfHillClimbIterations.getText();
                for (int i = 0; i < numOfRestartsInput.length(); i++) {
                    if (!Character.isDigit(numOfRestartsInput.charAt(i))) {
                        fieldsAreValid=false;
                        break;
                    }
                }
                for (int i = 0; i < numOfIterationsInput.length(); i++) {
                    if (!Character.isDigit(numOfIterationsInput.charAt(i))) {
                        fieldsAreValid=false;
                        break;
                    }
                }
                if(!fieldsAreValid){
                    JOptionPane.showMessageDialog(frame,"You have not input valid numbers.");
                    return;
                }
                int numberOfRestartsInteger = Integer.parseInt(numOfRestartsInput);
                int numberOfHillClimbsInteger = Integer.parseInt(numOfIterationsInput);
                //at this point there are valid numbers inputted and there is a grid to solve
                int dim = gridOfNodes.length;
                Node[][] pureHillClimbingGrid = new Node[dim][dim];
                Node[][] hillClimbingWithRestartsGrid = new Node[dim][dim];
                //copy the generated grid of nodes
                for(int i = 0;i<dim;i++){
                    for(int j = 0;j<dim;j++){
                        pureHillClimbingGrid[i][j]= new Node(i,j,gridOfNodes[i][j].getCellValue());
                        hillClimbingWithRestartsGrid[i][j]= new Node(i,j,gridOfNodes[i][j].getCellValue());
                    }
                }
                //initialize array list of grids
                ArrayList<Node[][]> arrayOfGrids = new ArrayList<>();
                //corresponding evaluation functions
                ArrayList<Integer> evaluationFunctionValues = new ArrayList();

                //font used for
                Font font = new Font("Arial Black",Font.BOLD,20);
                int initialEvalScore = evaluationFunction(gridOfNodes,dim,dim);

                //PureHillClimbingSolution

                long pureHillStartTime = System.nanoTime();
                pureHillClimbingGrid = hillClimb(pureHillClimbingGrid,numberOfHillClimbsInteger,dim);
                int pureHillResultingEvalFunc = evaluationFunction(pureHillClimbingGrid,dim,dim);
                long pureHillEndTime = System.nanoTime();
                long totalTime = pureHillEndTime-pureHillStartTime;
                double totalTimeForPureHillClimbing = (double)totalTime/1000000000.0;

                //at this point the pure hill climbing solution is done

                //hill Climbing with random restarts
                long restartsTime = System.nanoTime();
                for(int i=0;i<numberOfRestartsInteger;i++){
                    hillClimbingWithRestartsGrid = hillClimb(hillClimbingWithRestartsGrid,numberOfHillClimbsInteger, dim);
                    arrayOfGrids.add(hillClimbingWithRestartsGrid);
                    evaluationFunctionValues.add(evaluationFunction(hillClimbingWithRestartsGrid,dim,dim));
                    hillClimbingWithRestartsGrid=create2DArrayOfNodes(dim,dim);
                }
                long restartsEndTime = System.nanoTime();
                long totalRestartsTime = restartsEndTime-restartsTime;
                double totalRestartTimeInSeconds = (double)totalRestartsTime/1000000000.0;

                //find the location of the best evaluation function
                int num = Integer.MAX_VALUE;
                boolean hasPositiveInt = false;
                for(int i = 0;i<evaluationFunctionValues.size();i++){
                    if(num>evaluationFunctionValues.get(i)&&evaluationFunctionValues.get(i)>0){
                        num = evaluationFunctionValues.get(i);
                        hasPositiveInt=true;
                    }
                }
                if(!hasPositiveInt){
                    num=Integer.MIN_VALUE;
                    for(int i = 0;i<evaluationFunctionValues.size();i++){
                        if(evaluationFunctionValues.get(i)>num){
                            num = evaluationFunctionValues.get(i);
                        }
                    }
                }
                //get the grid at the position of num
                Node[][] restartGrid = arrayOfGrids.get(num);
                int hillCLimbWithRestartsResultingEval = evaluationFunction(restartGrid,dim,dim);
                //pure hill labels
                JLabel initialEvalHill = new JLabel("Initial evaluation function: "+Integer.toString(initialEvalScore));
                JLabel initialEvalRestart = new JLabel("Initial evaluation function: "+Integer.toString(initialEvalScore));
                //set styles for thew initial eval function
                initialEvalHill.setBackground(Color.BLACK);
                initialEvalHill.setForeground(Color.GREEN);
                initialEvalHill.setFont(font);
                initialEvalHill.setOpaque(true);
                initialEvalRestart.setBackground(Color.BLACK);
                initialEvalRestart.setForeground(Color.GREEN);
                initialEvalRestart.setFont(font);
                initialEvalRestart.setOpaque(true);

                JLabel pureHillEval = new JLabel("Resulting evaluation function: "+Integer.toString(pureHillResultingEvalFunc));
                JLabel pureHillRunTime = new JLabel("Time elapsed: "+Double.toString(totalTimeForPureHillClimbing));
                //pure hill styling
                pureHillEval.setForeground(Color.GREEN);
                pureHillEval.setBackground(Color.BLACK);
                pureHillEval.setFont(font);
                pureHillEval.setOpaque(true);

                pureHillRunTime.setForeground(Color.GREEN);
                pureHillRunTime.setBackground(Color.BLACK);
                pureHillRunTime.setFont(font);
                pureHillRunTime.setOpaque(true);


                //restaarts labels
                JLabel restartsEval = new JLabel("Resulting evaluation function: "+Integer.toString(hillCLimbWithRestartsResultingEval));
                JLabel restartsRunTime = new JLabel("Time elapsed: "+Double.toString(totalRestartTimeInSeconds));
                //restarts styling
                restartsEval.setForeground(Color.GREEN);
                restartsEval.setBackground(Color.BLACK);
                restartsEval.setFont(font);
                restartsEval.setOpaque(true);

                restartsRunTime.setForeground(Color.GREEN);
                restartsRunTime.setBackground(Color.BLACK);
                restartsRunTime.setFont(font);
                restartsRunTime.setOpaque(true);

                //check if the pure hill climbing approch is  not solvable
                if(pureHillResultingEvalFunc<0){
                    initialEvalHill.setForeground(Color.RED);
                    pureHillEval.setForeground(Color.RED);
                    pureHillRunTime.setForeground(Color.RED);
                }
                if(hillCLimbWithRestartsResultingEval<0){
                    initialEvalRestart.setForeground(Color.RED);
                    restartsEval.setForeground(Color.RED);
                    restartsEval.setForeground(Color.RED);

                }



                //add both grids to the layout
                removeGrid(mainPanel);
                //create a panel to add both of the grids side by side
                JPanel sideBySideGrids = new JPanel();
                sideBySideGrids.setLayout(new GridLayout(2,2));
                //create the panels for the stats for each process
                JPanel pureHillStats = new JPanel();
                pureHillStats.setLayout(new BoxLayout(pureHillStats,BoxLayout.Y_AXIS));
                JPanel hillRestartsStats = new JPanel();
                hillRestartsStats.setLayout(new BoxLayout(hillRestartsStats,BoxLayout.Y_AXIS));

                //add stat labels to the stat panels
                //add pure hill climbing stats
                pureHillStats.add(initialEvalHill);
                pureHillStats.add(pureHillEval);
                pureHillStats.add(pureHillRunTime);

                //add hill climbing with restarts stats
                hillRestartsStats.add(initialEvalRestart);
                hillRestartsStats.add(restartsEval);
                hillRestartsStats.add(restartsRunTime);

                addGridToLayout(sideBySideGrids,pureHillClimbingGrid);
                addGridToLayout(sideBySideGrids,restartGrid);
                //add stats panels to the grid panels
                sideBySideGrids.add(pureHillStats);
                sideBySideGrids.add(hillRestartsStats);

                mainPanel.add(sideBySideGrids,BorderLayout.CENTER);
                frame.revalidate();
                frame.repaint();









            }
        });




        //add the main panel to the fram
        this.add(mainPanel);

//      show the window
        this.setVisible(true);
        return;
    }



    /*---------UTILITY METHODS---------*/

    public static int evaluationFunction(Node[][] gridOfNodes, int maxRows, int maxCols){
        int evaluation=0;
        //check if goal was visited
        //if yes, evaluation = goal node level
        if(gridOfNodes[maxRows-1][maxCols-1].getLevel()>0){
            evaluation=gridOfNodes[maxRows-1][maxCols-1].getLevel();
        }
        //if not, evaluation = -(#of non visited cells)
        else{
            int nonvisited=0;
            int i=0;
            int j=0;
            for(i=0; i<maxRows; i++){
                for(j=0; j<maxCols; j++){
                    if(gridOfNodes[i][j].getLevel()==0){
                        nonvisited++;
                    }
                }
            }
            nonvisited*=-1;
            evaluation=nonvisited+1;
        }
        return evaluation;
    }
//    public static void solvePuzzle(Node[][] gridOfNodes,int maxRows, int maxColumns){
//        //run bfs on the nodes in the grid of nodes
//
//        if(BFS(gridOfNodes[0][0],gridOfNodes[maxRows-1][maxColumns-1],gridOfNodes,create2DVisitedMatrix(maxRows,maxColumns),maxRows,maxColumns)){
//
//        }
//    }
    public static void createDataForTask3(){
        Node[][] five;
        Node[][] seven = new Node[7][7];
        Node[][] nine = new Node[9][9];
        Node[][] eleven = new Node[11][11];
        int[] numberOfIterations = {50,250,500,1000,2000,5000,10000,50000,100000,250000,500000,1000000};

        five = create2DArrayOfNodes(11,11);
        Node[][] copyOfFive = new Node[11][11];
        for(int x = 0;x<11;x++){
            for(int y=0;y<11;y++){
                copyOfFive[x][y]= new Node(x,y,five[x][y].getCellValue());
            }
        }
        seven = create2DArrayOfNodes(7,7);
        nine = create2DArrayOfNodes(9,9);
        eleven = create2DArrayOfNodes(11,11);
        try{
            PrintWriter wr = new PrintWriter("task3-plotResults-for-n=11.txt", "UTF-8");
            //write the grid
            wr.println("grid rep");
            for(int b=0;b<11;b++){
                for(int c = 0;c<11;c++){
                    wr.print(five[b][c].getCellValue()+" ");
                }
                wr.println();

            }
            Node startNode = five[0][0];
            Node goalNode = five[10][10];
            int evaluationScore = 0;
            boolean start = BFS(five[0][0],five[10][10],five,create2DVisitedMatrix(11,11),11,11);
            int initialEval = evaluationFunction(five,11,11);
            for(int a=0;a<numberOfIterations.length;a++){
                int iterations = numberOfIterations[a];
                wr.println("Number of iteration: "+iterations);
                wr.println("Initial function value :"+initialEval);
                evaluationScore = initialEval;
                int newEvalScore=0;
                for(int l=0;l<11;l++){
                    for(int m=0;m<11;m++){
                        five[l][m].setCellValue(copyOfFive[l][m].getCellValue());
                    }
                }
//                five = copyOfFive.clone();
                for(int f=0;f<11;f++){
                    for(int g = 0;g<11;g++){
                        wr.print(five[f][g].getCellValue()+" ");
                    }
                    wr.println();

                }


                for(int i=0;i<iterations;i++){
                    Random rand = new Random();
                    int randomRow = rand.nextInt(11);
                    int randomCol = rand.nextInt(11);
                    //loop until the random cell is not the goal node
                    while(randomRow==10 && randomCol==10){
                        randomRow=rand.nextInt(11);
                        randomCol=rand.nextInt(11);
                    }
                    //reset the grid's node levels
                    five = clearGridNodeLevels(five);
                    int oldCellNumber = five[randomRow][randomRow].getCellValue();
                    int newCellNumber = generateGridNumber(randomRow,randomCol,11,11);
                    five[randomRow][randomCol].setCellValue(newCellNumber);
                    boolean isSolvable = BFS(startNode,goalNode,five,create2DVisitedMatrix(11,11),11,11);
                    if(isSolvable){
                        newEvalScore = evaluationFunction(five,11,11);
                        if(evaluationScore<0){
                            if(newEvalScore>=evaluationScore){
                                five[randomRow][randomCol].setCellValue(newCellNumber);
                                evaluationScore=newEvalScore;
                            }else{
                                five[randomRow][randomCol].setCellValue(oldCellNumber);

                            }
                        }else{
                            if(newEvalScore<=evaluationScore){
                                five[randomRow][randomCol].setCellValue(newCellNumber);
                                evaluationScore=newEvalScore;
                            }else{
                                five[randomRow][randomCol].setCellValue(oldCellNumber);

                            }
                        }
                    }else{
                        newEvalScore = evaluationFunction(five,11,11);
                        if(evaluationScore<0){
                            if(newEvalScore>=evaluationScore){
                                five[randomRow][randomCol].setCellValue(newCellNumber);
                                evaluationScore=newEvalScore;
                            }else{
                                five[randomRow][randomCol].setCellValue(oldCellNumber);

                            }
                        }
                    }

                }
                wr.println("Final function value: "+evaluationScore);
            }

            wr.close();
        } catch (IOException e) {
            System.out.println("error writing file");
        }


    }
    public static void addGridToLayout(JPanel mainPanel, Node[][] gridOfNodes){
        //create panel for grid
        JPanel gridPanel = new JPanel();
        //set the grid layout for the grid panel using the converted input
        gridPanel.setLayout(new GridLayout(gridOfNodes.length,gridOfNodes.length,0,0));
        //add labels
        for(int i = 0;i<gridOfNodes.length;++i){
            for(int j = 0;j<gridOfNodes.length;++j){
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
    }
    public static Node[][] createAndAddRandomGrid(JPanel mainPanel,int maxRows, int maxColumns){
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
        return gridOfNodes;
    }
    public boolean thereIsNoGrid(JPanel mainPanel){
        if(mainPanel.getComponentCount()==1){
            return true;
        }
        return false;
    }

    public static void removeGrid(JPanel mainPanel){
        //remove the grid panel
        Component[] comp = mainPanel.getComponents();
        for(int i=1;i<comp.length;++i){
            mainPanel.remove(comp[i]);
        }
    }
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

    public static Node[][] clearGridNodeLevels(Node[][] gridOfNodes){
        for(int i=0;i<gridOfNodes.length;i++){
            for(int j = 0;j<gridOfNodes.length;j++){
                gridOfNodes[i][j].setLevel(0);
            }
        }
        return gridOfNodes;
    }
    public static boolean BFS(Node startNode,Node goalNode,Node[][] gridOfNodes,int[][] visitedMatrix, int maxRows, int maxCols){
        //initialize neighbor queue
        Queue<Node> neighborQ = new LinkedList<Node>();
        //set the level number for the start node
        int level = 0;
        startNode.setLevel(level);
        neighborQ.add(startNode);
        boolean hasPath = false;
        while(!(neighborQ.isEmpty())){
            Node currNode = neighborQ.remove();
            if(currNode.equals(goalNode)) {
                 hasPath = true;
            }
            else {
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
                    //sort
                    Collections.sort(arrayOfNeighbors, new Comparator<Node>(){
                        public int compare(Node o1, Node o2){
                            if(o1.getCellValue() == o2.getCellValue())
                                return 0;
                            return o1.getCellValue() < o2.getCellValue() ? -1 : 1;
                        }
                    });

                    //add Nodes in array of neighbors to the queue
//                    System.out.println("Current node: "+currNode.getCellValue());
//                    System.out.println("Current node position : "+currNode.getRowPos()+currNode.getColPos());

                    for (int i = 0; i < arrayOfNeighbors.size(); i++) {
//                        System.out.println("Neighbor of current node: "+arrayOfNeighbors.get(i).getCellValue());
                        //check if any of the neighbors have been visited
                        if (visitedMatrix[arrayOfNeighbors.get(i).getRowPos()][arrayOfNeighbors.get(i).getColPos()] == 1) {
//                            System.out.println("already visited neighbor: "+arrayOfNeighbors.get(i).getCellValue());
                            continue;
//                            neighborQ.add(arrayOfNeighbors.get(i));
                        } else {
                            arrayOfNeighbors.get(i).setLevel(currNode.getLevel() + 1);
//                            arrayOfNeighbors.get(i).setLevel(arrayOfNeighbors.get(i).getParent().getLevel() + 1);
//                            arrayOfNeighbors.get(i).setLevel(level);
//                            System.out.println(arrayOfNeighbors.get(i).getLevel());
                            neighborQ.add(arrayOfNeighbors.get(i));

                        }
                    }
                    //increment the level

                }
            }
        }
        return hasPath;

    }
    public static Node[][] hillClimb(Node[][] grid, int iterations,int dimension){
        int evaluationScore = 0;
        int newEvaluationScore=0;

        //calculate the evaluation score for the starting grid
        boolean isSolvable=false;
        int initialEvalScore = evaluationFunction(grid,dimension,dimension);
        evaluationScore = initialEvalScore;
        //loop the amount of iterations
        for(int i = 0;i<iterations;i++){
            Random rand = new Random();
            int randomRow = rand.nextInt(dimension);
            int randomCol = rand.nextInt(dimension);
            //loop until the random cell is not the goal node
            while(randomRow==dimension-1 && randomCol==dimension-1){
                randomRow=rand.nextInt(dimension);
                randomCol=rand.nextInt(dimension);
            }
            //reset the grid's node levels
            grid = clearGridNodeLevels(grid);
//                    System.out.println("random row: "+randomRow);
//                    System.out.println("random col: "+randomCol);
            //store the node in a temp variable so we can hold onto it if we need to change the grid back
            int oldCellNumber = grid[randomRow][randomCol].getCellValue();
//                    System.out.println("old cell number: "+oldCellNumber);
            //generate a valid random number for that cells position
            int newCellNumber = generateGridNumber(randomRow,randomCol,dimension,dimension);
//                    System.out.println("new cell number: "+newCellNumber);

            //change the node's cell value to that new number
            grid[randomRow][randomCol].setCellValue(newCellNumber);
            //run BFS to set the grid nodegrid levels with the possibly new cell value number
            isSolvable = BFS(grid[0][0],grid[dimension-1][dimension-1],grid,create2DVisitedMatrix(dimension,dimension),dimension,dimension);
            newEvaluationScore = evaluationFunction(grid,dimension,dimension);
            if(isSolvable){//puzzle can be solved
                if(evaluationScore<0||newEvaluationScore<evaluationScore){//newEvaluation will be positive so we immediately set evaluation score to new evaluation score
                    evaluationScore=newEvaluationScore;
                }else{
                    grid[randomRow][randomCol].setCellValue(oldCellNumber);
                }
            }else{ //puzzle cant be solved
                if(evaluationScore>0){
                    grid[randomRow][randomCol].setCellValue(oldCellNumber);


                }else{
                    if(newEvaluationScore>evaluationScore){
                        evaluationScore=newEvaluationScore;
                    }
                    else{
                        grid[randomRow][randomCol].setCellValue(oldCellNumber);
                    }
                }

            }

        }
        return grid;
    }

}
