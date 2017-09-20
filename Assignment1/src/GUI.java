
//awt allows us to ask questions of the OS
import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {

    public static void main(String[] args) {
        new GUI();
    }

    public GUI(){
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
        this.setResizable(false);
//      cleanup on close(red close button pressed)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//      set the title of the window
        this.setTitle("Grid");

//      create panel
        JPanel panel = new JPanel();
//      text field
        JTextField matrixField = new JTextField("Enter matrix dimensions", 20);
        matrixField.setToolTipText("Matrix dimensions");
        matrixField.requestFocus();
        panel.add(matrixField);

        JButton submitButton = new JButton("Submit");
        panel.add(submitButton);

//      listen for submit button clicked
        submitButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("Submit button pressed");
            }
        });
//      add panel to the frame
        this.add(panel);




//      show the window
        this.setVisible(true);
    }
}
