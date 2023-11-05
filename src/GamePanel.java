import javax.swing.JPanel;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;

public class GamePanel extends JPanel implements ActionListener{

    static final int Screen_width=600;
    static final int Screen_hight=600;
    static final int unit_size=20;
    static final int Game_units=(Screen_hight*Screen_width)/unit_size;
    static final int delay=250;

    final int x[]= new int[Game_units];
    final int y[]= new int[Game_units];
    int bodyParts=2;

    int applesEaten;
    int appleX;
    int appleY;

    char direction='R';
    boolean running= false;
    Timer timer;
    Random random;



    GamePanel(){
        random= new Random();
        this.setPreferredSize(new Dimension(Screen_width, Screen_hight));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        StartGame();
    }

    public void StartGame(){
        newApple();
        running=true;
        timer=new Timer(delay, this);
        timer.start();
        
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        draw(g);

    }

    public void newApple(){
        appleX=random.nextInt((int)(Screen_width/unit_size))*unit_size;
        appleY=random.nextInt((int)(Screen_hight/unit_size))*unit_size;
    }

    public void draw(Graphics g){
        if(running){
            for(int i=0; i<Screen_hight/unit_size; i++){
                g.drawLine(i*unit_size, 0, i*unit_size, Screen_hight);
                g.drawLine(0, i*unit_size, Screen_width,i*unit_size);
            }
            g.setColor(Color.red);
            g.fillOval(appleX, appleY, unit_size, unit_size);

            for(int i=0; i<bodyParts; i++){
                if(i==0){
                    g.setColor(Color.green);
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                }
                else{
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], unit_size, unit_size);
                }
            }
            g.setColor(Color.red);
            g.setFont(new Font("Ink Free", Font.BOLD, 20));
            FontMetrics metrics= getFontMetrics(g.getFont());
            g.drawString("score:"+applesEaten, (Screen_width - metrics.stringWidth("Score:"+applesEaten))/2, g.getFont().getSize());
        }
        else{
            gameOver(g);
        }
    }

    public void move(){
        for(int i=bodyParts; i>0; i--){
            x[i]=x[i-1];
            y[i]=y[i-1];
        }
        switch(direction){
            case 'O':
                y[0]=y[0]-unit_size;
                break;
            case 'D':
                y[0]=y[0]+unit_size;break;
            case 'L':
                x[0]=x[0]-unit_size;break;
            case 'R':
                x[0]=x[0]+unit_size;break;
        }
    }

    public void checkApple(){
        if((x[0]==appleX) && (y[0]==appleY)){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    
    public void checkCollisions(){
        for(int i=bodyParts; i>0; i--){
            if(x[0]==x[i] && y[0]==y[i]){
                running=false;

            }
        }

        if(x[0]<0 || x[0]>Screen_width || y[0]<0 || y[0]>Screen_width){
            running=false;
        }

        if(!running)timer.stop();



    }

    public void gameOver(Graphics g){
        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, 40));
        FontMetrics metrics1= getFontMetrics(g.getFont());
        g.drawString("score:"+applesEaten, (Screen_width - metrics1.stringWidth("Final Score:"+applesEaten))/2, g.getFont().getSize());


        g.setColor(Color.red);
        g.setFont(new Font("Ink Free", Font.BOLD, unit_size*2));
        FontMetrics metrics= getFontMetrics(g.getFont());
        g.drawString("G_A_M_E____O_V_E_R", (Screen_width - metrics.stringWidth("G_A_M_E____O_V_E_R"))/2, Screen_hight/2);
    }





    @Override
    public void actionPerformed(ActionEvent e){
        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();

    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction!='R'){
                        direction='L';
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction!='L')direction='R';
                    break;
                case KeyEvent.VK_UP:
                    if(direction!='D')direction='O';
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction!='O')direction='D';
                    break;
            }
        }
    }
}
