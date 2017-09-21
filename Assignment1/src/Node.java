public class Node {

    private int xPos;
    private int yPos;
    private Node parent;

    public Node(int xPos, int yPos, Node parent){
        this.xPos = xPos;
        this.yPos = yPos;
        this.parent = parent;
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


}
