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
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
/**
 *
 * @author Capital
 */
public class Chart extends JDialog {
    public JFrame parentFrame;
    public String title;
    
    public Chart(JFrame parentFrame,String tit, float [] ser, float prome, float dis){
        super(parentFrame);
        this.title = tit;
        this.parentFrame = parentFrame;
        setTitle("KPI Graph");
        setSize( 600, 600 );
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        XYDataset ds = createDataset(ser, prome, dis);
        JFreeChart chart = ChartFactory.createXYLineChart(title,
             "Muestra", "", ds, PlotOrientation.VERTICAL, true, true, false);
        ChartPanel cp = new ChartPanel(chart);
        getContentPane().add(cp);
        cp.setMaximumSize(new Dimension(cp.getParent().getWidth(), 0));
    }
 
    private static  XYDataset createDataset(float [] y, float prom, float desu) {
        XYSeriesCollection ds = new XYSeriesCollection();
        //---graficar muestras
        XYSeries serie0 = new XYSeries("Samples");
        for(int j=0; j<y.length-1; j++){
                serie0.add(j, y[j]);
        }
        ds.addSeries(serie0);
        //---graficar promedio
        XYSeries serie1= new XYSeries("Prom");
        serie1.add(0, prom);
        serie1.add(y.length-2, prom);
        ds.addSeries(serie1);
        //---graficar limite sup
        XYSeries serie2= new XYSeries("LSup");
        serie2.add(0, prom+desu);
        serie2.add(y.length-2, prom+desu);
        ds.addSeries(serie2);
        //---graficar limite inf
        XYSeries serie3= new XYSeries("LInf");
        serie3.add(0, prom-desu);
        serie3.add(y.length-2, prom-desu);
        ds.addSeries(serie3);
        return ds;
    }
    
}
