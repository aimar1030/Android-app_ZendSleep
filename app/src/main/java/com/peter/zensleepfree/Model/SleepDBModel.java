package com.peter.zensleepfree.Model;

/**
 * Created by peter on 6/25/16.
 */
public class SleepDBModel {
    public String id;
    public int status;
    public int startTimeSec;
    public int elapsedSec;
    public int stagesLotSec;
    public int mood;
    public int dream;
    public String note;
    public float[] stages;

    public SleepDBModel() {
        id = "";
        status = -1;
        startTimeSec = -1;
        elapsedSec = -1;
        stagesLotSec = -1;
        mood = -1;
        dream = -1;
        note = "";
        stages = new float[]{};
    }

    public String getId() {
        return id;
    }

    public int getStatus() {
        return status;
    }

    public int getStartTimeSec() {
        return startTimeSec;
    }

    public int getElapsedSec() {
        return elapsedSec;
    }

    public int getStagesLotSec() {
        return stagesLotSec;
    }

    public int getMood() {
        return mood;
    }

    public int getDream() {
        return dream;
    }

    public String getNote() {
        return note;
    }

    public float[] getStages() {
        return stages;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setStartTimeSec(int startTimeSec) {
        this.startTimeSec = startTimeSec;
    }

    public void setElapsedSec(int elapsedSec) {
        this.elapsedSec = elapsedSec;
    }

    public void setStagesLotSec(int stagesLotSec) {
        this.stagesLotSec = stagesLotSec;
    }

    public void setMood(int mood) {
        this.mood = mood;
    }

    public void setDream(int dream) {
        this.dream = dream;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setStages(float[] stages) {
        this.stages = stages;
    }

    public float getEfficiency() {
        float eff;
        float totalStage = 0;
        int nCnt = this.getStages().length;
        for (int i = 0; i < nCnt; i++) {
            float s = this.getStages()[i];
            if (s < 0.3) {
                s = 0;
            } else if (s < 0.65) {
                s = 0.65f * 0.25f;
            } else {
                s = s * 0.4f;
            }
            totalStage += (1 - s);
        }

        eff = totalStage/nCnt;
        return eff;
    }
}
