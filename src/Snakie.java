import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

public class Snakie extends JPanel{

    public static final int recwidth = 20;
    public static final int recheight = 20;
    public static JFrame window;
    static int gamePanelHeight = 20;

    public Snakie()
    {

    }


    public int getWidth()
    {
        return window.getWidth();
    }
    public int getHeight()
    {
        return window.getHeight();
    }
    public int getHeightD()
    {
        return getHeight()/recheight;
    }

    private void DrawBoard(Graphics g)
    {
        //Draws all the scares of the board in different sizes dependent on the window size
        for(int i = 0;i<getHeight()/20;i++) {
            for(int ii = 0;ii<getWidth()/20;ii++) {
                //Uses only getHeightD so the squares are squares and not rectangles
                g.drawRect(ii*getHeightD(), i*getHeightD(), getHeightD(), getHeightD() );

            }
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DrawBoard(g);
    }

    public static void main(String[] args)
    {
        JFrame window = new JFrame();
        window.setTitle("S N A K I E");
        window.setSize(800,600+gamePanelHeight);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel gamePanel = new JPanel();
        JLabel Score = new JLabel("Score: ");
        Score.setText("Score: ");
        gamePanel.add(Score);

        Snakie snakePanel = new Snakie();
        Snakie.window = window;


        window.add(gamePanel,BorderLayout.NORTH);
        window.add(snakePanel,BorderLayout.CENTER);

        window.setVisible(true);

        while(true)
        {   //Game loop
            snakePanel.repaint();
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
