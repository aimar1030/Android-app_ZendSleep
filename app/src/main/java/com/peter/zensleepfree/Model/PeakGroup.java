package com.peter.zensleepfree.Model;

import java.util.ArrayList;

/**
 * Created by peter on 6/24/16.
 */
public class PeakGroup {

    public ArrayList<Peak> peaks = new ArrayList<>();

    public double time() {
        Peak firstPeak = getFirstPeak();
        Peak lastPeak = getLastPeak();
        if (firstPeak == null || lastPeak == null) return 0;
        return lastPeak.t - firstPeak.t;
    }

    public double intervalWithOther(PeakGroup peakGroup) {
        Peak peak1 = peakGroup.getLastPeak();
        Peak peak2 = peakGroup.getFirstPeak();
        return peak2.t - peak1.t;
    }

    private Peak getFirstPeak() {
        if (peaks.size() < 1) {
            return null;
        }
        return peaks.get(0);
    }

    private Peak getLastPeak() {
        if (peaks.size() < 1) return null;
        return peaks.get(peaks.size() - 1);
    }

    public void addPeakToGroup(Peak peak) {
        peaks.add(peak);
    }

    public Peak getPrimaryPeak() {
        int peakCount = peaks.size();
        if (peakCount < 1)return null;
        Peak primaryPeak = peaks.get(0);
        for (int i = 0; i < peakCount; i++) {
            Peak peak = peaks.get(i);
            if (primaryPeak.peakValue < peak.peakValue) {
                primaryPeak = peak;
            }
        }
        return primaryPeak;
    }
}