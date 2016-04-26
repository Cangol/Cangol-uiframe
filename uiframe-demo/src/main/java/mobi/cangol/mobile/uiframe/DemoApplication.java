/** 
 * Copyright (c) 2013 Cangol.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License")
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mobi.cangol.mobile.uiframe;

import mobi.cangol.mobile.CoreApplication;
import mobi.cangol.mobile.uiframe.db.DatabaseHelper;
import mobi.cangol.mobile.uiframe.utils.Constants;
import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.AppService;
import mobi.cangol.mobile.service.ServiceProperty;
import mobi.cangol.mobile.service.conf.ConfigService;
import mobi.cangol.mobile.service.status.StatusService;


/**
 * @Description MobileApplication.java 
 * @author Cangol
 * @date 2013-9-8
 */
public class DemoApplication  extends CoreApplication {
		public final String TAG=Constants.makeLogTag(DemoApplication.class);
		private StatusService statusService;
		private AppStatusListener appStatusListener;
		private DatabaseHelper databaseHelper;
		@Override
		public void onCreate() {
			this.setDevMode(true);
			super.onCreate();
			init();
		}
		public void init() {
			if (isDevMode())
				Log.v(TAG, "init");
			initAppService();
			initDataBase();
		}

		public void initAppService() {
			if (isDevMode())
				Log.v(TAG, "initAppService");
			
			Log.d(TAG,"初始化ConfigService");
			ConfigService configService = (ConfigService) getAppService(AppService.CONFIG_SERVICE);
			ServiceProperty p=configService.getServiceProperty();
			p.putString(ConfigService.APP_DIR, Constants.APP_DIR);
			p.putString(ConfigService.SHARED_NAME, Constants.SHARED);
			
			Log.v(TAG, "getAppDir:" + configService.getAppDir());
			
			Log.d(TAG,"初始化StatusService");
			statusService = (StatusService) getAppService(AppService.STATUS_SERVICE);
			appStatusListener = new AppStatusListener();
			statusService.registerStatusListener(appStatusListener);
			
		}

		public void initDataBase() {
			databaseHelper=DatabaseHelper.createDataBaseHelper(this);
			Log.d(TAG,"Database: Name="+databaseHelper.getDataBaseName()+", Version="+databaseHelper.getDataBaseVersion());
		}

		@Override
		public void exit() {
			super.exit();
		}
}
