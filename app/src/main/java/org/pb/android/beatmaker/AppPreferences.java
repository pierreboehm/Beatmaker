package org.pb.android.beatmaker;

import org.androidannotations.annotations.sharedpreferences.DefaultInt;
import org.androidannotations.annotations.sharedpreferences.SharedPref;

@SharedPref(value = SharedPref.Scope.UNIQUE)
public interface AppPreferences {

    @DefaultInt(83)
    int getDefaultBpm();

}
