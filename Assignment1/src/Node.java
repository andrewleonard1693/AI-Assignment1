public class Node {

    private int rowPos;
    private int colPos;
    private Node parent;
    private int cellValue;
    private int pathCount;

    public Node(int rowPos, int colPos, int cellValue){
        this.rowPos = rowPos;
        this.colPos = colPos;
        this.cellValue = cellValue;
    }


    //getters for values
    public int getRowPos(){
        return rowPos;
    }
    public int getColPos(){
        return colPos;
    }
    public Node getParent(){
        return parent;
    }
    public int getCellValue(){
        return cellValue;
    }
    public int getPathCount(){
        return pathCount;
    }

    //setters for values
    public void setCellValue(int cellValue){
        this.cellValue = cellValue;
    }
    public void setParent(Node parent){
        this.parent = parent;
    }
    public void setPathCount(int pathCount){
        this.pathCount=pathCount;
    }

}
