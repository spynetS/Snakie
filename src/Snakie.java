import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;
import java.util.ArrayList;
import java.util.Random;


public class Snakie extends JPanel{

    public static final int recwidth = 20;
    public static final int recheight = 20;
    public static int speed = 200;
    public static JFrame window;
    static int gamePanelHeight = 20;
    private int snakeDirection = 0; //0-3
    int[] foodpos = new int[]{10,10};
    int score = 0;
    public static JLabel Score;

    ArrayList<SnakePiece> snake = new ArrayList<>();

    public Snakie()
    {
        snake.add(new SnakePiece(10,3));
        snake.add(new SnakePiece(11,3));
        snake.add(new SnakePiece(12,3));
        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==39) snakeDirection = 2;
                else if(e.getKeyCode()==37) snakeDirection=0;
                else if(e.getKeyCode()==38) snakeDirection = 1;//Up
                else if(e.getKeyCode()==40) snakeDirection = 3;//down
                System.out.println(snake.get(0).x+" ble "+snake.get(0).y);

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }

    private void SpawnNewFood()
    {
        Random w = new Random();
        Random h = new Random();
        int x = w.nextInt(recwidth-1);
        int y = h.nextInt(recheight-1);
        System.out.println(x+" "+y);
        foodpos[0] = x;
        foodpos[1] = y;
    }

    private void GameOver()
    {
        System.out.println("Game over");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        snake.clear();
        snake.add(new SnakePiece(10,3));
        snake.add(new SnakePiece(11,3));
        snake.add(new SnakePiece(12,3));
    }

    public int getWidth()
    {
        return window.getWidth();
    }
    public int getHeight()
    {
        return window.getHeight()-gamePanelHeight;
    }
    public int getHeightD()
    {
        return getHeight()/recheight;
    }

    private void DrawBoard(Graphics g)
    {
        g.setColor(Color.GREEN);
        //Draws all the scares of the board in different sizes dependent on the window size
        for(int i = 0;i<getHeight()/20;i++) {
            for(int ii = 0;ii<getWidth()/20;ii++) {
                //Uses only getHeightD so the squares are squares and not rectangles
                g.drawRect(ii*getHeightD(), i*getHeightD(), getHeightD(), getHeightD() );

            }
        }
    }
    private void DrawSnake(Graphics g)
    {
        //Draws all the pieces of the snake
        for(SnakePiece snakePiece : snake)
        {
            if(snakePiece.equals(snake.get(0)))
            {
                g.setColor(Color.lightGray);
            }
            else
            {
                g.setColor(Color.gray);
            }
            g.fillRect(snakePiece.x*getHeightD(),snakePiece.y*getHeightD(),getHeightD(),getHeightD());
        }

    }
    private void DrawFood(Graphics g)
    {
        g.setColor(Color.RED);
        g.fillOval(foodpos[0]*getHeightD(),foodpos[1]*getHeightD(),getHeightD(),getHeightD());
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DrawBoard(g);
        DrawSnake(g);
        DrawFood(g);
    }

    private void CheckDirection()
    {
        //So when the snake is moving i remove the last piece
        // And add a new piece with the new coordinates as the first piece in the list

        SnakePiece piece = new SnakePiece(snake.get(0).x,snake.get(0).y);
        switch (snakeDirection)
        {
            case 0:
                piece.x+=-1;
                break;

            case 1:
                piece.y+=-1;
                break;

            case 2:
                piece.x+=1;
                break;

            case 3:
                piece.y+=1;
                break;
        }
        snake.add(0,piece);
        snake.remove(snake.size()-1);

    }
    private void CollisionDetection()
    {
        //Checks if the have hit the food
        if(snake.get(0).x == foodpos[0]&&snake.get(0).y == foodpos[1])
        {
            SnakePiece snakePiece = snake.get(snake.size()-1);
            snake.add(snakePiece);
            SpawnNewFood();
            score++;
            Score.setText("Score: "+score);
        }
        SnakeCollision:
        for(SnakePiece piece : snake)
        {
            for(SnakePiece piece2 : snake)
            {
                if(piece!=piece2&& piece.equals(piece2))
                {
                    GameOver();
                    break SnakeCollision;
                }
            }
        }
        for(SnakePiece piece : snake)
        {
           if(piece.x<0||piece.x>(getWidth()/recwidth)||piece.y<0||piece.y>recheight)
           {
               GameOver();
               break;
           }
        }
    }

    public void Update()
    {
        repaint();
        CheckDirection();
        CollisionDetection();

    }
    public static void main(String[] args)
    {
        JFrame window = new JFrame();
        window.setTitle("S N A K I E");
        window.setSize(800,600+gamePanelHeight);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JPanel gamePanel = new JPanel();
        Score = new JLabel("Score: ");
        Score.setText("Score: ");
        //add more settings
        gamePanel.add(Score);

        Snakie snakePanel = new Snakie();
        Snakie.window = window;
        snakePanel.setBackground(Color.black);
        snakePanel.setFocusable(true);

        window.add(gamePanel,BorderLayout.NORTH);
        window.add(snakePanel,BorderLayout.CENTER);

        window.setVisible(true);

        while(true)
        {   //Game loop
            snakePanel.Update();
            try {
                Thread.sleep(speed);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
