public class Node {

    private int rowPos;
    private int colPos;
    private Node parent;
    private int cellValue;
    private int numVisits;

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
    public int getNumVisits(){
        return numVisits;
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
    public void setNumVisits(int newNumVisits){
        this.numVisits = newNumVisits;
    }

}
