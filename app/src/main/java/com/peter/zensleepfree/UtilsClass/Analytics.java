package com.peter.zensleepfree.UtilsClass;

import android.util.Log;

import com.peter.zensleepfree.Model.Average;
import com.peter.zensleepfree.Model.Peak;
import com.peter.zensleepfree.Model.PeakGroup;

import java.util.ArrayList;

/**
 * Created by peter on 6/24/16.
 */
public class Analytics {

    private ArrayList<short[]> buffers;
    private int movements, snorings;

    public Analytics(ArrayList<short[]> buffers) {
        this.buffers = buffers;
    }

    public void beingAnalyzing() {
        getMaxFromBuffers(buffers);
    }

    public void getMaxFromBuffers(ArrayList<short[]> buffers) {
        short[] maxs = new short[buffers.size() + 1];
        for (int i = 0; i < buffers.size(); i++) {
            short tempMax = buffers.get(i)[0];
            for (int j = 1; j < buffers.get(i).length; j++) {
                if (tempMax < buffers.get(i)[j]) {
                    tempMax = buffers.get(i)[j];
                }
            }
            maxs[i] = tempMax;
        }
        Log.d("Get Max Values", "Complete");
        getPeaksFromMaxs(maxs);
    }

    public void getPeaksFromMaxs(short[] maxs) {
        ArrayList<Peak> peaks = new ArrayList<>();
        for (int i = 100; i < maxs.length - 1; i++) {
            if (    maxs[i] > maxs[i - 1] &&
                    maxs[i] > maxs[i + 1] &&
                    maxs[i] > getAveragePeakValue(maxs, i) * 4)
            {
                Peak peak = new Peak();
                peak.peakValue = maxs[i];
                peak.t = 1.0f * i * Constant.BUFFER_INTERVAL_SECONDS/maxs.length;
                peaks.add(peak);
            }
        }
        getMovementsFromPeak(peaks);
    }

    public float getAveragePeakValue(short[] maxs, int index) {
        float total = 0.0f;
        for (int i = index - 100; i < index; i++) {
            total = total + maxs[i];
        }
        return total/100;
    }

    public void getMovementsFromPeak(ArrayList<Peak> peaks) {
        ArrayList<PeakGroup> peakGroups = new ArrayList<>();
        int peakCount = peaks.size();
        if (peakCount < 1) return;
        PeakGroup peakGroup = new PeakGroup();
        Peak lastPeak = peaks.get(0);
        peakGroup.addPeakToGroup(lastPeak);
        peakGroups.add(peakGroup);
        for (int i = 1; i < peakCount; i++) {
            Peak peak = peaks.get(i);
            if (peak.t - lastPeak.t <= 1.5f) {
                peakGroup.addPeakToGroup(peak);
            } else {
                peakGroup = new PeakGroup();
                peakGroup.addPeakToGroup(peak);
                peakGroups.add(peakGroup);
            }
            lastPeak = peak;
        }
        Log.d("Detect Movement Finish", "Movement Size " + String.valueOf(peakGroups.size()));
        movements = peakGroups.size();
        snorings = getSnoringFromMovement(peakGroups);

        analytics(snorings, movements);
    }

    public int getSnoringFromMovement(ArrayList<PeakGroup> peakGroups) {
        int peakGroupSize = peakGroups.size();
        if (peakGroupSize < 3) return 0;
        PeakGroup group1 = peakGroups.get(0);
        PeakGroup group2;
        int index = 1;
        int tempSnoringCount = 0;
        int snoringCount = 0;
        double am = (double) group1.getPrimaryPeak().peakValue;
        Average average = new Average(5);

        while (index < peakGroupSize) {
            group2 = peakGroups.get(index);
            boolean isSnoring = false;
            do {
                double dt = group2.intervalWithOther(group1);
                if (dt > 10.0) {
                    group1 = group2;
                    isSnoring = false;
                    break;
                }
                if (dt < 1.5) {
                    isSnoring = true;
                    break;
                }
                Peak peak1 = group1.getPrimaryPeak();
                Peak peak2 = group2.getPrimaryPeak();
                double td = peak2.t - peak1.t;
                double ax = peak2.peakValue;

                if (tempSnoringCount < 1) {
                    if (Math.abs(ax - am) < 0.50 * am) {
                    } else {
                        group1 = group2;
                        isSnoring = false;
                        break;
                    }
                } else {
                    double av = average.getAverage();
                    if (Math.abs(td - av) < 0.15 * av && Math.abs(ax - am) < 0.5 * am) {
                        Log.d("Is he snoring?", "Yes, Snoring now");
                    } else {
                        if (tempSnoringCount > 1) {
                            snoringCount += tempSnoringCount;
                        }
                        group1 = group2;
                        isSnoring = false;
                        break;
                    }
                }
                average.putValue(td);
                if( am < ax )
                    am = ax;
                tempSnoringCount++;
                group1 = group2;
                isSnoring = true;
            } while (false);
            if( !isSnoring )
            {
                tempSnoringCount = 0;
                average.reset();
                am = group1.getPrimaryPeak().peakValue;
            }
            index++;
        }
        return snoringCount + (tempSnoringCount>1?tempSnoringCount:0);
    }

    public void analytics(int snoring, int movement) {
        float status = -1;
        if( snoring >= 6 )
            status = 0;
        else if( movement > 80 )
            status = 1;
        else if( movement >= 0 )
            status = movement*1.0f/80.0f;
        else
            status = 1;//awake

        if( status > 1 )
            status = 1;

        if (status == 0) {
            Log.d("Sleep Status", "Deep sleep: " + String.valueOf(status));
        } else if (status == 1) {
            Log.d("Sleep Status", "Awake: " + String.valueOf(status));
        } else {
            Log.d("Sleep Status", "Normal Sleep: " + String.valueOf(status));
        }

        //it will record data for less than 24 hours
        if( ZenSleep.sleepStatus.size() < 24*3600/Constant.BUFFER_INTERVAL_SECONDS ) {
            // Correct to emphasize awake stage
            int nCnt = ZenSleep.sleepStatus.size();
            if( nCnt > 1 ) {
                float prevStage1 = ZenSleep.sleepStatus.get(nCnt - 1);
                float prevStage2 = ZenSleep.sleepStatus.get(nCnt - 2);
                if (prevStage2 < 0.65f && prevStage1 >= 0.65f) {
                    status = (prevStage1 + 0.65f)/2;
                }
            }
            ZenSleep.sleepStatus.add(status);
        }
    }
}
