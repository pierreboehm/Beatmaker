package org.pb.android.beatmaker.data.soundtrack;

import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

import org.androidannotations.annotations.AfterInject;
import org.androidannotations.annotations.EBean;
import org.androidannotations.annotations.RootContext;

@EBean
public class SoundTrackDao {

    @RootContext
    Context context;

    @AfterInject
    void init() {
        FlowManager.init(new FlowConfig.Builder(context).build());
    }

    /*public List<SoundTrack> getTracksForArea(String areaName) {
        return SQLite.select().from(SoundTrack.class).where(SoundTrack_Table.areaName.eq(areaName)).queryList();
    }*/

    /*public void deleteTracksOfArea(String areaName) {
        SQLite.delete().from(GeoTrack.class).where(GeoTrack_Table.areaName.eq(areaName));
    }*/
}
