/**
 * Copyright (c) Facebook, Inc. and its affiliates.
 *
 * <p>This source code is licensed under the MIT license found in the LICENSE file in the root
 * directory of this source tree.
 */
package com.facebook.react.packagerconnection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import com.facebook.common.logging.FLog;
import com.facebook.infer.annotation.Assertions;
import com.facebook.react.modules.systeminfo.AndroidInfoHelpers;
import javax.annotation.Nullable;

public class PackagerConnectionSettings {
  private static final String TAG = PackagerConnectionSettings.class.getSimpleName();
  private static final String PREFS_DEBUG_SERVER_HOST_KEY = "debug_http_host";

  private final SharedPreferences mPreferences;
  private final String mPackageName;
  private final Context mAppContext;

  public PackagerConnectionSettings(Context applicationContext) {
    mPreferences = PreferenceManager.getDefaultSharedPreferences(applicationContext);
    mPackageName = applicationContext.getPackageName();
    mAppContext = applicationContext;
  }

  public String getDebugServerHost() {
    // Check host setting first. If empty try to detect emulator type and use default
    // hostname for those
    String hostFromSettings = mPreferences.getString(PREFS_DEBUG_SERVER_HOST_KEY, null);

    if (!TextUtils.isEmpty(hostFromSettings)) {
      return Assertions.assertNotNull(hostFromSettings);
    }

    String host = AndroidInfoHelpers.getServerHost(mAppContext);

    if (host.equals(AndroidInfoHelpers.DEVICE_LOCALHOST)) {
      FLog.w(
          TAG,
          "You seem to be running on device. Run '"
              + AndroidInfoHelpers.getAdbReverseTcpCommand(mAppContext)
              + "' "
              + "to forward the debug server's port to the device.");
    }

    return host;
  }

  public String getInspectorServerHost() {
    return AndroidInfoHelpers.getInspectorProxyHost(mAppContext);
  }

  public @Nullable String getPackageName() {
    return mPackageName;
  }
}
