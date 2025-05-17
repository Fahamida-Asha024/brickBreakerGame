package Breakbrick;
import javax.swing.JFrame;

public class Main {

 public static void main(String[] args) {

  JFrame obj = new JFrame();
  Gameplay gameplay = new Gameplay();
  obj.setBounds(10, 10, 700, 600);
  obj.setLocationRelativeTo(null);
  obj.setTitle("Brick Breaker!");
  obj.setResizable(false);
  obj.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  obj.add(gameplay); // Add before making it visible
  obj.setVisible(true);
     gameplay.requestFocusInWindow();
 }
}
