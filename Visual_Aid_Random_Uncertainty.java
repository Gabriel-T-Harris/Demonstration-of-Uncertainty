import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Random;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/*
Purpose: Visual aid for random uncertainty
Programmer: Gabriel Toban Harris
Date: 2017-3-16/2017-3-17 (wrote a comment)/2017-3-19 (wrote a comment)/2017-3-20 (wrote a comment)/2017-3-25/2017-3-29/2017-4-1/2017-4-3/2017-4-11
*/

//Could be improved by allowing user input for points/iterations
public class Visual_Aid_Random_Uncertainty implements ActionListener
{
        private static int size = 600, write_start_point, write_read_end_point, counter = 0, number_points = 100000, iteration = 0, number_iterations = 100;// controls iterations;
        private static String caption_string_1 = "Pourcentage de précision pour ", caption_string_2 = " points est ";
        private static Random rng;
        private static final JProgressBar PROGRESS_BAR = new JProgressBar(0, 100);
        private static Point point_container[] = new Point[number_points];// # of points
        private static final DecimalFormat ROUND = new DecimalFormat("#.####");
        private static JLabel caption_label = new JLabel("Intial", SwingConstants.CENTER), ellipse_label = new JLabel("Couleur de l'ellipse", SwingConstants.CENTER),
                              in_ellipse_label = new JLabel("Point à l'intérieur de l'ellipse", SwingConstants.CENTER), out_ellipse_label = new JLabel("Point à l'extérieur de l'ellipse", SwingConstants.CENTER), border_label = new JLabel("Frontière", SwingConstants.CENTER)/*, number_points_label = new JLabel("Combien de points à peindre?", SwingConstants.CENTER)*/;
        private static JPanel main_panel = new JPanel(new BorderLayout()), legend = new JPanel();
        private static JFrame main_frame = new JFrame("Démonstration d'incertitude");
//      private static JButton submit_button = new JButton("Soumettre");
        private static ButtonGroup language;
        private static final JRadioButton ENGLISH_RADIO_BUTTON = new JRadioButton("English"), FRENCH_RADIO_BUTTON = new JRadioButton("Français");
//      public static JFormattedTextField number_points_text_field;

        public Visual_Aid_Random_Uncertainty()
        {
         PROGRESS_BAR.setValue(0);
         PROGRESS_BAR.setStringPainted(true);

         //legend stuff
             //labels
         ellipse_label.setFont(new Font("Arial", Font.BOLD, 14));
         in_ellipse_label.setFont(new Font("Arial", Font.BOLD, 14));
         in_ellipse_label.setForeground(Color.BLUE);
         out_ellipse_label.setFont(new Font("Arial", Font.BOLD, 14));
         out_ellipse_label.setForeground(Color.RED);
         border_label.setFont(new Font("Arial", Font.BOLD, 14));
         border_label.setForeground(Color.ORANGE);
//       number_points_label.setFont(new Font("Arial", Font.BOLD, 14));
             //radio buttons
         language = new ButtonGroup();
         ENGLISH_RADIO_BUTTON.addActionListener(this);
         FRENCH_RADIO_BUTTON.addActionListener(this);
         language.add(ENGLISH_RADIO_BUTTON);
         language.add(FRENCH_RADIO_BUTTON);
         FRENCH_RADIO_BUTTON.setSelected(true);

//       submit_button.addActionListener(this);
//
//       NumberFormat intFormat = NumberFormat.getInstance();
//       intFormat.setGroupingUsed(false);
//       NumberFormatter intFormatter = new NumberFormatter(intFormat);
//       intFormatter.setValueClass(Integer.class);
//       intFormatter.setMinimum(10);
//       intFormatter.setMaximum(10000);
//       intFormatter.setAllowsInvalid(true);
//       number_points_text_field = new JFormattedTextField(/*intFormatter*/);
//       number_points_text_field.setEditable(true);
//       number_points_text_field.setColumns(4);

         legend.setLayout(new BoxLayout(legend, BoxLayout.Y_AXIS));
         legend.add(ellipse_label);
         legend.add(in_ellipse_label);
         legend.add(out_ellipse_label);
         legend.add(border_label);
//       legend.add(number_points_label);
//       legend.add(number_points_text_field);
//       legend.add(submit_button);
         legend.add(ENGLISH_RADIO_BUTTON);
         legend.add(FRENCH_RADIO_BUTTON);

         caption_label.setFont(new Font("Arial", Font.BOLD, 25));

         main_panel.add(caption_label, BorderLayout.NORTH);
         main_panel.add(new Drawing(), BorderLayout.CENTER);
         main_panel.add(PROGRESS_BAR, BorderLayout.SOUTH);
         main_panel.add(legend, BorderLayout.EAST);

         main_frame.add(main_panel);
         /*x = y - 59 so that CENTER is square. Surrounding edges each, 8 for resizable and 3 for nonresizable; NORTH = 16; SOUTH = 21; title bar = 22.
        /note: NORTH isn't accurate after setting font for captions. + 13 to width for ellipse line*/
         main_frame.setSize(size + 13, size + 59);
         main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         main_frame.setLocationRelativeTo(null);
         main_frame.setVisible(true);
         while (true)
              {
               if (iteration < number_iterations - 1)
                 {
                  PROGRESS_BAR.setValue(100 * (iteration + 1) / number_iterations);
                  main_frame.repaint();
                  try
                     {
                      Thread.sleep(100);
                     }
                  catch (Exception ex)
                       {
                        ex.printStackTrace();
                       }
                  if (PROGRESS_BAR.getValue() % 10 == 0)
                     try
                        {
                         Thread.sleep(1300);
                        }
                  catch (Exception ex)
                       {
                        ex.printStackTrace();
                       }
                 }
               //reset values
               else
                   {
                    PROGRESS_BAR.setValue(100);
                    try
                       {
                        Thread.sleep(1600);
                       }
                    catch (Exception ex)
                         {
                          ex.printStackTrace();
                         }
                    counter = 0;
                    iteration = 0;
                    Arrays.fill(point_container, null);
                    PROGRESS_BAR.setValue(0);
                   }
              }
        }

        class Drawing extends JPanel
        {
         protected void paintComponent(Graphics g)
         {
          super.paintComponent(g);
          //Section of array to write to.
          write_start_point = (iteration * (point_container.length / number_iterations));
          write_read_end_point = ((iteration + 1) * (point_container.length / number_iterations));

          rng = new Random();
          for (; write_start_point < write_read_end_point; write_start_point++)
             {
              point_container[write_start_point] = new Point(rng.nextInt(getBounds().width + 1), rng.nextInt(getBounds().height + 1));
              //true if point within ellipse
              if (Math.pow((float)point_container[write_start_point].x / getBounds().width, 2) + Math.pow((float)(getBounds().height - point_container[write_start_point].y) / getBounds().height, 2) <= 1)
                 counter++;
             }
          for (int i = 0; i < write_read_end_point; i++)
             {
              //true if point within ellipse
              if (Math.pow((float)point_container[i].x / getBounds().width, 2) + Math.pow((float)(getBounds().height - point_container[i].y) / getBounds().height, 2) <= 1)
                 g.setColor(Color.BLUE);
              else
                   g.setColor(Color.RED);
              g.fillOval(point_container[i].x, point_container[i].y, 4, 4);
             }
          g.setColor(Color.ORANGE);
          g.drawRect(0, 0, getBounds().width - 1, getBounds().height - 1);//-1 so is in section
          g.setColor(Color.BLACK);
          g.drawArc(-getBounds().width, 0, getBounds().width * 2, getBounds().height * 2, 0, 90);
          caption_label.setText(caption_string_1 + write_read_end_point + caption_string_2 + String.valueOf(ROUND.format(100 * (((float)counter/write_read_end_point) / (Math.PI/4)))) + "%.");
          iteration++;
         }
        }

        public void actionPerformed(ActionEvent e)
        {
         Object source = e.getSource();

         //radio buttons set languages
         if (source == FRENCH_RADIO_BUTTON)
           {
            main_frame.setTitle("Démonstration d'incertitude");
            ellipse_label.setText("Couleur de l'ellipse");
            in_ellipse_label.setText("Point à l'intérieur de l'ellipse");
            out_ellipse_label.setText("Point à l'extérieur de l'ellipse");
            border_label.setText("Frontière");
//          number_points_label.setText("Combien de points à peindre?");
//          submit_button.setText("Soumettre");
            caption_string_1 = "Pourcentage de précision pour ";
            caption_string_2 = " points est ";
           }
         else if (source == ENGLISH_RADIO_BUTTON)
             {
              main_frame.setTitle("Demonstration of Uncertainty");
              ellipse_label.setText("Colour of ellipse");
              in_ellipse_label.setText("Point is inside ellipse");
              out_ellipse_label.setText("Point is outside ellipse");
              border_label.setText("Border");
//            number_points_label.setText("How many points to be painted?");
//            submit_button.setText("Submit");
              caption_string_1 = "Percentage of accuracy for ";
              caption_string_2 = " points is ";
             }

//       if (source == submit_button)
//         {
//          number_points = Integer.parseInt(number_points_text_field.getText());
//          point_container = new Point[Integer.parseInt(number_points_text_field.getText())];
//    	 try
//     	    {
//           Thread.sleep(1000);
//          }
//       catch (InterruptedException ex)
//            {
//             ex.printStackTrace();
//            }
//       main_frame.repaint();
//       }
    }

        public static void main(String[] args)
        {
         new Visual_Aid_Random_Uncertainty();
        }
}
