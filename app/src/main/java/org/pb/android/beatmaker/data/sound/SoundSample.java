package org.pb.android.beatmaker.data.sound;

import android.content.Context;
import android.util.Log;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import org.pb.android.beatmaker.data.ContentTickContainer;
import org.pb.android.beatmaker.data.ContentTickContainer_;
import org.pb.android.beatmaker.fragment.ui.ClickableImageButton;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@Table(database = SoundSampleConfiguration.class)
public class SoundSample extends BaseModel implements Serializable {

    private static final String TAG = SoundSample.class.getSimpleName();

    @Column
    @PrimaryKey(autoincrement = true)
    int id;

    @Column
    String name;

    @Column
    int bpmValue;

    @Column
    int kickResourceId;

    @Column
    int snareResourceId;

    @Column
    int hiHatResourceId;

    @Column
    int toneResourceId;

    @Column
    String tickSamplesString;

    public SoundSample() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setBpmValue(int bpmValue) {
        this.bpmValue = bpmValue;
    }

    public int getBpmValue() {
        return bpmValue;
    }

    public void setResourceIds(int kickResourceId, int snareResourceId, int hiHatResourceId, int toneResourceId) {
        this.kickResourceId = kickResourceId;
        this.snareResourceId = snareResourceId;
        this.hiHatResourceId = hiHatResourceId;
        this.toneResourceId = toneResourceId;
    }

    public int getKickResourceId() {
        return kickResourceId;
    }

    public int getSnareResourceId() {
        return snareResourceId;
    }

    public int getHiHatResourceId() {
        return hiHatResourceId;
    }

    public int getToneResourceId() {
        return toneResourceId;
    }

    public void setTickSamples(List<ContentTickContainer> tickSamplesList) {
        this.tickSamplesString = getTickSamplesAsString(tickSamplesList);
    }

    public List<ContentTickContainer> getTickSamplesList(Context context) {
        int[][] tickSamplesArray = getTickSamplesArray(tickSamplesString);
        List<ContentTickContainer> resultList = new ArrayList<>();

        for (int index = 0; index <= 31; index++) {
            ContentTickContainer tickContainer = ContentTickContainer_.build(context, index);
            tickContainer.setButtonStates(tickSamplesArray[index]);
            resultList.add(tickContainer);
        }

        return resultList;
    }

    private String getTickSamplesAsString(List<ContentTickContainer> tickSamplesList) {
        String tickSamplesString = "";

        for (ContentTickContainer tickContainer : tickSamplesList) {
            tickSamplesString += (tickSamplesString.isEmpty() ? "{" : ",");
            int[] buttonStates = tickContainer.getButtonStates();
            tickSamplesString += String.format("{%s,%s,%s,%s}",
                    buttonStates[0], buttonStates[1], buttonStates[2], buttonStates[3]);
        }

        return tickSamplesString + "}";
    }

    private int[][] getTickSamplesArray(String tickSamplesString) {
        String regex = ",(?=([^\"]*\"[^\"]*\")*(?![^\"]*\"))";
        Pattern p = Pattern.compile(regex);
        String[] result = p.split(tickSamplesString);

        int sampleIndex = 0;
        int[][] resultArray = new int[33][4];
        boolean third = false;

        for (String resultString : result) {

            if (resultString.startsWith("{")) {
                resultArray[sampleIndex][0] = Integer.parseInt(resultString.replace("{", ""));

            } else if (resultString.endsWith("}")) {
                resultArray[sampleIndex][3] = Integer.parseInt(resultString.replace("}", ""));
            } else {
                if (!third) {
                    resultArray[sampleIndex][1] = Integer.parseInt(resultString);
                    third = true;
                } else {
                    resultArray[sampleIndex][2] = Integer.parseInt(resultString);
                    sampleIndex++;
                    third = false;
                }
            }
        }

        return resultArray;
    }
}