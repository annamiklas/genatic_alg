import models.Statistic;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartFrame;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

public class ChartDrawer {

    public static void createGraph(List<Statistic> statistics){
        XYSeries best = new XYSeries("best");
        XYSeries worst = new XYSeries("worst");
        XYSeries avg = new XYSeries("avg");

        for(int i = 0; i < statistics.size(); i++){
            best.add(i, statistics.get(i).getBest());
            worst.add(i, statistics.get(i).getWorst());
            avg.add(i, statistics.get(i).getAvg());
        }

        XYSeriesCollection dataset = new XYSeriesCollection();
        dataset.addSeries(best);
        dataset.addSeries(worst);
        dataset.addSeries(avg);


        JFreeChart chart = ChartFactory.createXYLineChart("GA", "generations", "fitness function",
                dataset, PlotOrientation.VERTICAL, true, false, false);


        ChartFrame frame1 = new ChartFrame("GA", chart);

        //frame1.setVisible(true);
        frame1.setSize(600, 600);


        try{
            OutputStream out = new FileOutputStream("GA.png");
            ChartUtils.writeChartAsPNG(out,
                    chart,
                    frame1.getWidth(),
                    frame1.getHeight());

        } catch(IOException ex){
            System.out.println(ex);
        }
    }
}
