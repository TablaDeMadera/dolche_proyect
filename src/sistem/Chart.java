/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sistem;

import java.awt.Dimension;
import java.util.HashSet;
import javax.swing.JDialog;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
/**
 *
 * @author Capital
 */
public class Chart extends JDialog {
    public JFrame parentFrame;
    
    public Chart(JFrame parentFrame){
        super(parentFrame);
        this.parentFrame = parentFrame;
        setTitle("Graph");
        setSize( 600, 600 );
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        XYDataset ds = createDataset();
        JFreeChart chart = ChartFactory.createXYLineChart("Test Chart",
             "x", "y", ds, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel cp = new ChartPanel(chart);
        getContentPane().add(cp);
        cp.setMaximumSize(new Dimension(cp.getParent().getWidth(), 0));
    }
 
    private static XYDataset createDataset() {
        DefaultXYDataset ds = new DefaultXYDataset();
        double[][] data = { {0.1, 0.2, 0.3}, {1, 2, 3} };
        ds.addSeries("series1", data);
        return ds;
    }
    
}
