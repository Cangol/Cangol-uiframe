/**
 * Copyright (c) 2013 Cangol.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.uiframe.demo;


import hugo.weaving.DebugLog;
import leakcanary.LeakCanary;
import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.ServiceProperty;
import mobi.cangol.mobile.service.conf.ConfigService;
import mobi.cangol.mobile.service.status.StatusService;
import mobi.cangol.mobile.uiframe.demo.utils.Constants;


/**
 * @Description MobileApplication.java
 * @author Cangol
 * @date 2013-9-8
 */
@DebugLog
public class DemoApplication extends CoreApplication {
    public final String TAG = Constants.makeLogTag(DemoApplication.class);
    private StatusService statusService;
    private AppStatusListener appStatusListener;

    @Override
    public void onCreate() {
        this.setDevMode(true);
        super.onCreate();
        init();
        Log.setLogLevelFormat(android.util.Log.VERBOSE, false);
    }

    public void init() {
        if (isDevMode())
            Log.v(TAG, "init");
        initAppService();
    }

    public void initAppService() {
        if (isDevMode())
            Log.v(TAG, "initAppService");

        Log.d(TAG, "初始化ConfigService");
        ConfigService configService = (ConfigService) getAppService(AppService.CONFIG_SERVICE);
        ServiceProperty p = configService.getServiceProperty();
        p.putString(ConfigService.APP_DIR, Constants.APP_DIR);
        p.putString(ConfigService.SHARED_NAME, Constants.SHARED);
        Log.v(TAG, "getAppDir:" + configService.getAppDir());
    }
}
