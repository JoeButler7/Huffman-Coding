import javax.swing.*;
import java.awt.*;

public class Canvas extends JPanel {
    @Override
    public void paintComponent(Graphics g){
        this.setLayout(new BorderLayout());
        g.setFont(new Font("Times New Roman",72,72));
        g.drawString("HUFFPRESS",getWidth()/2,0);

    }



}
