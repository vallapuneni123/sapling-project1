import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class Table extends JPanel implements  Runnable{
    int GAME_WIDTH=1000;
    int GAME_HEIGHT= (int) (1000*(0.555));
    Dimension SCREEN_SIZE= new Dimension(GAME_WIDTH,GAME_HEIGHT);

    int BALL_DIAMETER=20;

    int PEDDLE_HEIGHT=100;
    int PEDDLE_WIDTH=25;

    Image image;

    Ball ball;
    Graphics graphics;

    Thread gameThread;

    Peddle peddle1;
    Peddle peddle2;

    Score score=new Score(GAME_WIDTH,GAME_HEIGHT);

    Table()
    {
        this.setPreferredSize(SCREEN_SIZE);
        gameThread=new Thread(this);
        gameThread.start();
        this.setFocusable(true);
        this.addKeyListener((KeyListener) new MyKeys());
        newBall();
        newPeddle();
    }

    private void newPeddle() {
        peddle1=new Peddle(0,GAME_HEIGHT/2-PEDDLE_HEIGHT/2,PEDDLE_WIDTH,PEDDLE_HEIGHT,1);
        peddle2=new Peddle(GAME_WIDTH-PEDDLE_WIDTH,GAME_HEIGHT/2-PEDDLE_HEIGHT/2,PEDDLE_WIDTH,PEDDLE_HEIGHT,2);
    }

    private void newBall() {
        Random random=new Random();
        ball= new Ball(GAME_WIDTH/2,random.nextInt(GAME_HEIGHT),BALL_DIAMETER,BALL_DIAMETER);
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        image=createImage(getWidth(),getHeight());
        graphics=image.getGraphics();
        draw(graphics);
        g.drawImage(image,0,0,this);

    }

    private void draw(Graphics graphics) {
        ball.draw(graphics);
        peddle1.draw(graphics);
        peddle2.draw(graphics);
        score.draw(graphics);
    }
    public void move()
    {
        ball.move();
        peddle1.move();
        peddle2.move();
    }

    public void Collision()
    {
        if(ball.y<=0)
        {
            ball.yVelocity=-ball.yVelocity;
        }
        if(ball.y>=GAME_HEIGHT-BALL_DIAMETER)
        {
            ball.yVelocity=-ball.yVelocity;
        }



        if(ball.intersects(peddle1) || ball.intersects(peddle2)) {
            ball.xVelocity = -ball.xVelocity;
        }
        if(ball.x<0)
        {
            newPeddle();
            newBall();
            score.player2++;
        }
        if(ball.x>GAME_WIDTH)
        {
            newPeddle();
            newBall();
            score.player1++;
        }

        if(peddle1.y>=GAME_HEIGHT-PEDDLE_HEIGHT)
        {
            peddle1.yVelocity=-peddle1.yVelocity;
        }
        if(peddle1.y<0)
        {
            peddle1.yVelocity=-peddle1.yVelocity;
        }

        if(peddle2.y>=GAME_HEIGHT-PEDDLE_HEIGHT)
        {
            peddle2.yVelocity=-peddle2.yVelocity;
        }
        if(peddle2.y<0)
        {
            peddle2.yVelocity=-peddle2.yVelocity;
        }



    }

    @Override
    public void run(){
        long LastTime=System.nanoTime();
        double amountOfTicks=60.0;
        double ns=1000000000/amountOfTicks;
        double delta=0;
        while(true)
        {
            long now=System.nanoTime();
            delta+=(now-LastTime)/ns;
            LastTime=now;
            if(delta>=1)
            {
                move();
                repaint();
                Collision();
                delta--;
            }

        }
    }




    public class MyKeys extends KeyAdapter {


        @Override
        public void keyPressed(KeyEvent e) {
            peddle1.keyPressed(e);
            peddle2.keyPressed(e);

        }

        @Override
        public void keyReleased(KeyEvent e) {
            peddle1.keyReleased(e);
            peddle2.keyReleased(e);
        }
    }


}
