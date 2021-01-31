/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mikrozirkmixer1.pkg0;


import FreqAnalyzer.Globals;
import FreqAnalyzer.Sample;
import FreqAnalyzer.TimeSample;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.apache.commons.math3.stat.descriptive.moment.Mean;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;

/**
 * Container Class for file opener and saver.
 * @author eckerts
 */
public class FileIO {
    
    /**
     * Opens a sample file using a FileOpenDialog and returns a TimeSample object
     * created from the data.
     * @param sample Sample object to write the file data to
     * According to the settings in MirkoZirkFeqAnalyzerx.y/Globals outliers may
     * be removed. As well a data bias may be removed to make the graph oscillating
     * around the x-axis The third optional action is normalizing the data to get
     * a maximal Amplitude of 1.
     */
    public static TimeSample openSampleFile(){
        //load data
        new FileOpenDialog(null, true).setVisible(true);
        ArrayList<Double> data = new ArrayList<>();   
        int numberOfUnreadable = 0;
        Double value;
        try {
            Scanner dataFile = new Scanner(new File(Globals.pathToDataFile));
            try{
                while(dataFile.hasNextLine()){
                    String line = dataFile.nextLine();
                    if(line.isEmpty()){
                            data.add(data.get(data.size()-1));
                        } else{
                            try{
                                value = Double.valueOf(line);
                            } catch(Exception ex){
                                value = data.get(data.size()-1);
                                numberOfUnreadable++; 
                            }
                            data.add(value); 
                    }
                }
            } catch (Exception ex){
               JOptionPane.showMessageDialog(null, "Bad data format."
                + " Please check if your data has a point as digital separator.", "Error", 
                                JOptionPane.ERROR_MESSAGE); 
            }
            dataFile.close(); 
        } catch (FileNotFoundException ex) {
        Logger.getLogger(FreqAnalyzer.MainFrame.class.getName()).log(Level.SEVERE, null, ex);
        }

        //replace outliers, remove bias and normalize
        double[] values = new double[data.size()];
        for (int i=0; i<data.size(); ++i) values[i]=data.get(i);
        Mean mean = new Mean();
        StandardDeviation standardDev = new StandardDeviation();
        double average;
        double stDev;
        double maxAmplitude = 0;
        int numberOfOutliers = 0;
        String message = "";

        if (!data.isEmpty()){
            if(Globals.replaceOutliers == Globals.REPLACE_OUTLIERS_AUTO){               
                average = mean.evaluate(values);
                stDev = standardDev.evaluate(values);  
                if(data.get(0)-average > Globals.outliersTolerance*stDev) data.set(0, 0.0);
                for (int i=1; i<data.size(); ++i){
                    if(data.get(i)-average > Globals.outliersTolerance*stDev){
                        data.set(i, data.get(i-1));
                        numberOfOutliers++;
                    }
                }   
            }
            else if(Globals.replaceOutliers == Globals.REPLACE_OUTLIERS_MANUALLY){
                for (int i=1; i<data.size(); ++i){
                    if(data.get(i) < Globals.outliersLowerBound 
                            || data.get(i) > Globals.outliersUpperBound){
                        data.set(i, data.get(i-1));
                        numberOfOutliers++;
                    }
                }   
            }

            if(Globals.removeBias){
                values = new double[data.size()];
                for (int i=0; i<data.size(); ++i) values[i]=data.get(i);
                average = mean.evaluate(values);
                double dataValue;
                for (int i=0; i<data.size(); ++i){  
                    dataValue = data.get(i);
                    double newValue = dataValue-average;
                    data.set(i, newValue); //remove bias
                    if (Math.abs(newValue) > maxAmplitude) {
                        maxAmplitude = Math.abs(newValue);
                    } //get the max amplitude of the sample for normalization
                }
            } 
            
            if(Globals.normalization){
                for(int i=0; i<data.size(); ++i){
                    data.set(i, data.get(i)/maxAmplitude);                    
                }
                message = message + "Data has been normalized" + "\n";
                /*
                //Test
                System.out.println("Test normalization");
                maxAmplitude = 0;
                for(int i=0; i<data.size(); ++i){
                    if (Math.abs(data.get(i)) > maxAmplitude){
                        maxAmplitude = Math.abs(data.get(i));
                        System.out.println(maxAmplitude);
                    }
                }
                */
            } 
            
            message = message
                    + "Number of replaced unreadable Data values: "
                    + numberOfUnreadable + "\n"
                    + "Number of replaced Outliers: " + numberOfOutliers + "\n"
                    + "Size of Sample: " + data.size(); 
            
             JOptionPane.showMessageDialog(null, message, "Info", 
                     JOptionPane.INFORMATION_MESSAGE);
        }

        
        //load data to Sample
        return new TimeSample(data, Globals.samplingRate, true);
    }
    
}
