//awt allows us to ask questions of the OS
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;

public class GUI extends JFrame {

    public static void main(String[] args) {
        new GUI();
    }
    public GUI(){
        this.setSize(400,400);
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
        this.setResizable(false);
//      cleanup on close(red close button pressed)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      set the title of the window
        this.setTitle("Grid");

//      create panel
        JPanel panel = new JPanel();
//      create a label
        JLabel label1 = new JLabel("Tell me something");
        label1.setText("New Text");
        label1.setToolTipText("Doesn't do anything");
        panel.add(label1);

//      create a button
        JButton button1 = new JButton("Send");
        button1.setText("New Button");
        button1.setToolTipText("Its a button.");
        panel.add(button1);

//        text field
        JTextField textfield1 = new JTextField("Type here", 15);
        textfield1.setColumns(10);
        textfield1.setText("Type again");
        textfield1.setToolTipText("Its a text field");

        panel.add(textfield1);

//      add panel to the frame
        this.add(panel);




//      show the window
        this.setVisible(true);
    }
}
