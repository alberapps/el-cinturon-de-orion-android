/*
 * Copyright (C) 2014 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package alberapps.android.cinturondeorion.uamp;

import android.app.Application;

import com.google.android.libraries.cast.companionlibrary.cast.CastConfiguration;
import com.google.android.libraries.cast.companionlibrary.cast.VideoCastManager;

import alberapps.android.cinturondeorion.R;
import alberapps.android.cinturondeorion.uamp.ui.FullScreenPlayerActivity;

/**
 * The {@link Application} for the uAmp application.
 */
public class UAMPApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        String applicationId = getResources().getString(R.string.cast_application_id);
        VideoCastManager.initialize(
                getApplicationContext(),
                new CastConfiguration.Builder(applicationId)
                        .enableWifiReconnection()
                        .enableAutoReconnect()
                        .enableDebug()
                        .setTargetActivity(FullScreenPlayerActivity.class)
                        .build());
    }
}
