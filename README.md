# Welcome to the MicroZirkMixer!

MicroZirkMixer is a Java desktop application written by me (Stefan Eckert) to support my sister Ruth in her scientific work with blood flow velocity data collected by laser doppler flowmetry (LDF). It allows a weighted superposition of two data samples, frequency filtering of this mix with a kind of equalizer and finally the comparison of the filtered superposition to a third data sample on the screen.
It uses parts of the package commons-math3-3.6.1 and parts of the source package of MicroZirkFreqAnalyzer ([https://github.com/eckertst/MicroZirkFreqAnalyzer](https://github.com/eckertst/MicroZirkFreqAnalyzer)).
It is provided here "as is" and without any sort of warrenty concerning correctness or usability or anything else. It is published under [Apache license v2.0](https://github.com/eckertst/MicroZirkMixer/blob/main/LICENSE). You are allowed and invited to use, change or expend it according to this license.

## How to use the App

### Install and start

1. Get the MicroZirkMix_1_3.zip file frome here: https://github.com/eckertst/MicroZirkMixer/tree/main and unzip it in any Folder ("your folder") on your computer. 
2. Go to your folder and run the jar-file MikroZirkFreqAnalyzer1.0.jar there (It's still named 1.0 because my versioning is not very professional...) In Windows a double click should do or rightclick and open with java. If this doesn't work try the command line with `java -jar PathToYourFolder\MikroZirkFreqAnalyzer1.0.jar`

### Load Data
The data has to be in a textfile (name.txt oder name.asc), just one number per line. Decimal seperator has to be a point, no comma. Perhaps there are some single lines with rubbish in your data. That's no problem: The app will replace such errors by the value in the line before. With a high sampling rate this doesn't corrupt the data too much.

If possible, the number of values should be a little bit smaller (about 10) than a power of two. The Fast Fourier Transform algorithm which is used for frequency analysis works only with exactly a power of two values. Therefore the program fills up with zeroes up to the next power of two, if the number doesn't match. So, if you stay slightly below one of these numbers (1024, 2048, 4096, 8192, 16384, 32768 or 65536) you avoid thousands of additional zeroes that could disturb the analysis.
        
### Adjustments

1. Replace Outliers
Outliers can be recognized automatically or manually. They are replaced by the value before. 
* automatically: the smaller you choose the tolerance value (default 3) the stricter the filter works. If a data graph looks "cut off", just take a higher tolerance to get the maxima into your image. If the data graph consists mainly of vertical lines you have to choose a smaller value because the outliers (vertical lines) give the picture a false scaling.
* manually: Just give a upper and a lower bound for the values.

2. Sampling Rate
That is the frequency of the recording you have done with some LDF equipment (Values per second). With a false value here the Presentation of the data on the time axis and the frequency analysis will be completely incorrect!

3. Zoom transformed data to ... % of full range
The frequencies in blood flow velocity are low (f < 1,5 1/s). So here you can choose how much of the full frequency range shall be shown after transformation. Of course you can zoom in and out later. 

4. Remove Bias from Data
Bias here means the shift of the oscillation away from the x-axis. So removing the bias gives you a graph oscillating around the x-axis as assumed for the FFT-algorithm.
If you just want to see the origial data, don't remove the bias. But if you want to do anything else with this application, you should remove it to make the FFT-algorithm  work correctly.

5. Normalize Values
The amplitudes of the data might be different in two measurements according to the way the LDF probe is atached to the skin. If you want to do a weighted superposition of two samples you have to have the same amplitude in both samples. If "normalize values" is ticked, the maximal amplitude of the data is normalized to the value 1.
Use normalization always in combination with Bias removement.
So, if you realy want to mix two samples as is the purpose of this app, then use both.

### Zoom/Shift

The data displays allow shifting (drag the mouse) and zooming (mouse wheel)

### Weighted Mix

With the vertical slider on the right you can weight the mix of the two data samples by choosing a percentage.

### Equalizer

This feature works like an equalizer on a mixing desk: You have several frequency bands (separated by a vertical line) and controls to set the amplification for each band (the square with the "1.0" in it). Use the mouse wheel to change the amplification factor. If you like to, you can change the bounds of the frequency bands by dragging them with the mouse. The Equalizer display is zoomed together with the frequency displays. So if you see a chaos of several band bounds and amplification controls, just go to the frequency display and zoom in there.

### Pedal

Here you can load a data sample that shall be checked against the Mix you have produced so far. In our measurements that was a sample with signals from a foot pedal. That's why it is called "pedal" here.

###  Known problems

Some of the buttons (save the mix; save ore open a equalizer file) are dead. We didn't nead those features, so I stopped writing the code for them.

## How to get the source code

If you want to take a look at the source code or clone it to change the application for your own purposes go here: [https://github.com/eckertst/MicroZirkMixer/tree/master](https://github.com/eckertst/MicroZirkMixer/tree/master) Make sure to get the "master" branch not the "main".
You'll find a javadoc.zip there which was automatically generated. It was not maintained consequently during programming but it might be of some help nevertheless. Unzip it and open the index.html with your browser.
