package my.app.khu.lockscreen;

import my.app.khu.lockscreen.MainActivity.PermissionReceiver;
import android.app.Activity;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.widget.Toast;

public class ShortcutActivity extends Activity {

	private static final int MAX_RETRY_COUNT = 4;
	private static final int RETRY_DELAY = 100;
	private DevicePolicyManager policyManager;
	private ComponentName adminComponent;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		adminComponent = new ComponentName(this,PermissionReceiver.class);
		if (policyManager.isAdminActive(adminComponent)) {
			lockScreen(policyManager);
		} else {
			Toast.makeText(this, "You must enable this app in device admin by open Lock Screen and click ENABLE button!", Toast.LENGTH_SHORT).show();
			finish();
		}
	}
	/**
	 * 
	 * @param aPolicyManager
	 */
	public void lockScreen(final DevicePolicyManager aPolicyManager) {
		final PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		final Handler handler = new Handler(getMainLooper());
		final int[] retryCount = new int[] { 0 };
		
		handler.post(new Runnable() {
			@SuppressWarnings("deprecation")
			@Override
			public void run() {
				if (powerManager.isScreenOn()&& retryCount[0] <= MAX_RETRY_COUNT) {
					aPolicyManager.lockNow();
					//unregisterReceiver(turnOffScreenReciever);
					retryCount[0]++;
					handler.postDelayed(this, RETRY_DELAY * retryCount[0]);
				} else {
					finish();
				}
			}
		});
	}
}
