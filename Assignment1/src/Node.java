public class Node {

    private int xPos;
    private int yPos;
    private Node parent;
    private int cellValue;

    public Node(int xPos, int yPos, int cellValue){
        this.xPos = xPos;
        this.yPos = yPos;
        this.cellValue = cellValue;
    }


    //getters for values
    public int getXPos(){
        return xPos;
    }
    public int getYPos(){
        return yPos;
    }
    public Node getParent(){
        return parent;
    }
    public int getCellValue(){
        return cellValue;
    }

    //setters for values
    public void setCellValue(int cellValue){
        this.cellValue = cellValue;
    }
    public void setParent(Node parent){
        this.parent = parent;
    }

}
