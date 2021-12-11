package org.pb.android.beatmaker.data.sound;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

import java.io.Serializable;

@Table(database = SoundTrackConfiguration.class)
public class SoundTrack extends BaseModel implements Serializable {

    @Column
    @PrimaryKey(autoincrement = true)
    int id;

}