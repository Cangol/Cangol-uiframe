/** 
 * Copyright (c) 2013 Cangol
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

import mobi.cangol.mobile.logging.Log;
import mobi.cangol.mobile.service.status.StatusListener;
import android.content.Context;

public class AppStatusListener implements StatusListener{

	@Override
	public void networkConnect(Context context) {
		Log.d("networkDisconnect ");
	}

	@Override
	public void networkDisconnect(Context context) {
		Log.d("networkDisconnect ");
	}

	@Override
	public void networkTo3G(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storageRemove(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void storageMount(Context context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callStateIdle() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callStateOffhook() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void callStateRinging() {
		// TODO Auto-generated method stub
		
	}

}
