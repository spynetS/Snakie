import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

/*
    TODO
    snake shall die when outside screen
*/

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

        addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==39&&snakeDirection!=0) snakeDirection = 2;//left
                else if(e.getKeyCode()==37&&snakeDirection!=2) snakeDirection=0;//right
                else if(e.getKeyCode()==38&&snakeDirection!=3) snakeDirection = 1;//Up
                else if(e.getKeyCode()==40&&snakeDirection!=1) snakeDirection = 3;//down

            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
    }
    private boolean SamePos(int x, int y)
    {
        for(SnakePiece piece:snake)
        {
            if(x == piece.x&&y == piece.y)
                return true;
        }
        return false;
    }
    private int getRandomNumber(int max)
    {
        Random r = new Random();
        return r.nextInt(max);
    }
    private void SpawnNewFood()
    {
        int x = getRandomNumber(recwidth-1);
        int y = getRandomNumber(recheight-1);
        while(SamePos(x,y))
        {
            x = getRandomNumber(recwidth-1);
            y = getRandomNumber(recheight-1);
        }
        foodpos[0] = x;
        foodpos[1] = y;
    }

    public void GameOver()
    {
        score = 0;
        Score.setText("Score: 0");
        snakeDirection = 0;
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
        return window.getHeight()-((gamePanelHeight*2));
    }
    public int getHeightD()
    {
        return getHeight()/recheight;
    }
    public int getWidthD()
    {
        return getWidth()/recheight;
    }

    private void DrawBoard(Graphics g)
    {
        //Draws all the scares of the board in different sizes dependent on the window size
        for(int i = 0;i<getHeight()/20;i++) {
            for(int ii = 0;ii<getWidth()/20;ii++) {
                //Uses only getHeightD so the squares are squares and not rectangles
                if((ii+i)%2==0)
                {
                    g.setColor(new Color(0, 69, 17));
                }
                else
                {
                    g.setColor(new Color(0, 94, 23));
                }
                g.fillRect(ii*getWidth()/20, i*getHeightD(), getWidth()/20, getHeightD() );

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
                g.setColor(new Color(0, 201, 50));
            }
            else
            {
                g.setColor(new Color(0, 173, 43));
            }
            g.fillRect(snakePiece.x*getWidthD(),snakePiece.y*getHeightD(),getWidth()/20,getHeightD());
        }

    }
    private void DrawFood(Graphics g)
    {
        g.setColor(Color.RED);
        g.fillOval(foodpos[0]*getWidth()/20,foodpos[1]*getHeightD(),getWidth()/20,getHeightD());
    }
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        DrawBoard(g);
        DrawFood(g);
        DrawSnake(g);
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
        //Checks if the snake has hit it self
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
           if(piece.x<0||piece.x*getWidth()/20>(getWidth())||piece.y<0||piece.y>recheight-1)
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
        window.setSize(800,800);
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        Score = new JLabel("Score: ");
        Snakie.window = window;
        Snakie snakePanel = new Snakie();
        snakePanel.setBackground(Color.black);
        snakePanel.setFocusable(true);
        snakePanel.GameOver();

        JPanel gamePanel = new JPanel();
        Score.setText("Score: ");
        Score.setForeground(new Color(191, 191, 191));
        //add more settings
        JComboBox<String> combox = new JComboBox<>();
        combox.addItem("200");
        combox.addItem("150");
        combox.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                speed = Integer.parseInt(Objects.requireNonNull(combox.getSelectedItem()).toString());
                snakePanel.GameOver();
            }
        });
        combox.setFocusable(false);
        gamePanel.add(combox);
        gamePanel.add(Score);
        gamePanel.setBackground(new Color(0, 102, 17));

        window.add(gamePanel,BorderLayout.NORTH);
        window.add(snakePanel,BorderLayout.CENTER);

        window.setVisible(true);
        double t = 0.0;
        double dt = 1 / 60.0;

        double currentTime = System.nanoTime();

        while (true) {

            double newTime = System.nanoTime();
                double frameTime = newTime - currentTime;
                currentTime = newTime;
                while ( frameTime > 0.0 )
                {
                    try {
                        Thread.sleep(speed);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    double deltaTime = Math.min(frameTime, dt);
                    frameTime -= deltaTime;
                    t += deltaTime;
                    snakePanel.Update();

                }

        }
    }
}
