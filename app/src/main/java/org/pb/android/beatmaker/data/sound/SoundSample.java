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
    String tickSamplesString;

    public SoundSample() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTickSamples(List<ContentTickContainer> tickSamplesList) {
        this.tickSamplesString = getTickSamplesAsString(tickSamplesList);
    }

    public List<ContentTickContainer> getTickSamplesList(Context context) {
        int[][] tickSamplesArray = getTickSamplesArray(tickSamplesString);
        List<ContentTickContainer> resultList = new ArrayList<>();

        for (int index = 0; index <= 31; index++) {
            ContentTickContainer tickContainer = ContentTickContainer_.build(context, index);
            List<ClickableImageButton> clickableImageButtonList = tickContainer.getClickableImageButtons();

            clickableImageButtonList.get(0).setState(tickSamplesArray[index][0] == 1);
            clickableImageButtonList.get(1).setState(tickSamplesArray[index][1] == 1);
            clickableImageButtonList.get(2).setState(tickSamplesArray[index][2] == 1);
            clickableImageButtonList.get(3).setState(tickSamplesArray[index][3] == 1);

            resultList.add(tickContainer);
        }

        return resultList;
    }

    private String getTickSamplesAsString(List<ContentTickContainer> tickSamplesList) {
        String tickSamplesString = "";

        for (ContentTickContainer tickContainer : tickSamplesList) {
            tickSamplesString += (tickSamplesString.isEmpty() ? "{" : ",");

            int kick = tickContainer.getClickableImageButtons().get(0).getState() ? 1 : 0;
            int snare = tickContainer.getClickableImageButtons().get(1).getState() ? 1 : 0;
            int hiHat = tickContainer.getClickableImageButtons().get(2).getState() ? 1 : 0;
            int tone = tickContainer.getClickableImageButtons().get(3).getState() ? 1 : 0;
            tickSamplesString += String.format("{%s,%s,%s,%s}", kick, snare, hiHat, tone);
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