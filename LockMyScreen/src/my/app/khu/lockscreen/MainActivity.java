package my.app.khu.lockscreen;

import my.app.khu.R;
import my.app.khu.service.ServiceFloating;
import my.app.khu.widget.MyWidgetProvider;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class MainActivity extends Activity {

	private static final int MAX_RETRY_COUNT = 4;
	private static final int RETRY_DELAY = 100;

	private static final int REQUEST_CODE_ENABLE_ADMIN = 2;
	public static final String TAG = MainActivity.class.getSimpleName();
	private TextView txt_display;
	private TextView txt_2;
	private TextView txt_3;
	private TextView txt_4;
	private TextView txt_5;
	private TextView txt_6;
	private Button btn_start_floating;
	private Button btn_stop_floating;
	private Button btn_active;
	private Button btn_deactive;
	private Button btn_shortcut;
	private Button btn_pick_color;
	private AdView mAdView;
	private ComponentName adminComponent;
	private TurnOffScreenReciever turnOffScreenReciever;
	private DevicePolicyManager policyManager;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);
		
		txt_display = (TextView)findViewById(R.id.textView1);
		txt_2 = (TextView)findViewById(R.id.textView2);
		txt_display.setText(Html.fromHtml("<b>" + "* Lock the device with a single touch" + "</b>" +  "<br />" + 
	            "<b>" + "* Power efficient: kills itself after locking screen" + "</b>" + "<br /> <br />" + 
	              "Press" + "<b>" + " ACTIVE" + "</b>" + " Now to active lock screen!!!"));
		txt_2.setText(Html.fromHtml("Press" + "<b>" + " DEACTIVE" + "</b>" + " and uncheck" + "<b>" + " LockScreen" + "</b>" +" to deactive app!!!"));
		txt_3 = (TextView)findViewById(R.id.textView3);
		txt_3.setText(Html.fromHtml("Create a" + "<b>" + " SHORTCUT" + "</b>" + " on home screen"));
		
		//txt_4 = (TextView)findViewById(R.id.textView4);
		//txt_4.setText(Html.fromHtml("Create a" + "<b>" + " FLOATING BUTTON" + "</b>" + " anywhere"));
		
//		txt_5 = (TextView)findViewById(R.id.textView5);
//		txt_5.setText(Html.fromHtml("Stop" + "<b>" + " FLOATING BUTTON" + "</b>"));
		txt_6 = (TextView)findViewById(R.id.textView6);
		txt_6.setText(Html.fromHtml("Change conf" + "<b>" + " FLOATING BUTTON" + "</b>"));
				
		btn_active = (Button)findViewById(R.id.btn_active);
		btn_deactive = (Button)findViewById(R.id.btn_deactive);
		btn_shortcut = (Button)findViewById(R.id.btn_create_shortcut);
		//btn_start_floating = (Button)findViewById(R.id.btn_floating);
		//btn_stop_floating = (Button)findViewById(R.id.btn_stop_floating);
		btn_pick_color = (Button)findViewById(R.id.btn_pick_color);
		
		adminComponent = new ComponentName(MainActivity.this,PermissionReceiver.class);
		policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
		
		mAdView = (AdView) findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();

		//Load the adView with the ad request.
		mAdView.loadAd(adRequest);
		
		if (policyManager.isAdminActive(adminComponent)) {
			//lockScreen(policyManager);
			btn_active.setEnabled(false);
			btn_deactive.setEnabled(true);
		}
		
		btn_pick_color.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				startActivity(new Intent(getBaseContext(),PickColorActivity.class));
			}
		});
		btn_active.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				requestPermission(adminComponent);
				//register turn off screen reciever
				//Toast.makeText(getBaseContext(), "Active successful", Toast.LENGTH_SHORT).show();
				
				
			}
		});
		btn_deactive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				policyManager.removeActiveAdmin(adminComponent);
				btn_deactive.setEnabled(false);
				btn_active.setEnabled(true);
			}
		});
		btn_shortcut.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent shortcutIntent = new Intent(getApplicationContext(), ShortcutActivity.class);
				shortcutIntent.setAction(Intent.ACTION_MAIN);

				Intent addIntent = new Intent();
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "Lock Screen");
				addIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, Intent.ShortcutIconResource.fromContext(getApplicationContext(),R.drawable.icon));

				addIntent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
				getApplicationContext().sendBroadcast(addIntent); 
				finish();
			}
		});
		
		// Floating button
		Bundle bundle = getIntent().getExtras();

		if(bundle != null && bundle.getString("LAUNCH").equals("YES")) {
			startService(new Intent(MainActivity.this, ServiceFloating.class));
		}
//		btn_start_floating.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				startService(new Intent(MainActivity.this, ServiceFloating.class));
//			}
//		});
//		btn_stop_floating.setOnClickListener(new View.OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//				stopService(new Intent(MainActivity.this, ServiceFloating.class));
//			}
//		});
//		turnOffScreenReciever = new TurnOffScreenReciever();
//		registerReceiver(turnOffScreenReciever, new IntentFilter(MyWidgetProvider.WIDGET_BUTTON));	
		
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) 
        {
           // ServiceFloating.flag_long_click = false;
        }
        return super.onKeyDown(keyCode, event);
	}
	@Override
	protected void onResume() {
		mAdView.pause();
		Bundle bundle = getIntent().getExtras();

		if(bundle != null && bundle.getString("LAUNCH").equals("YES")) {
			startService(new Intent(MainActivity.this, ServiceFloating.class));
		}
		super.onResume();
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		mAdView.destroy();
	}
	@Override
	protected void onPause() {
		mAdView.pause();
		super.onPause();
	}
	@Override
	protected void onActivityResult(int aRequestCode, int aResultCode, Intent aData ) {
		super.onActivityResult(aRequestCode, aResultCode, aData);
		if (aResultCode == RESULT_OK && aRequestCode == REQUEST_CODE_ENABLE_ADMIN ) {
			//DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
			//lockScreen(policyManager);
			btn_deactive.setEnabled(true);
			btn_active.setEnabled(false);
		} else {
			askUserToRetry();
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

	/**
	 * 
	 */
	@SuppressLint("NewApi")
	private void askUserToRetry() {
		AlertDialog.Builder builder;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
			builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_DARK);
		} else {
			builder = new AlertDialog.Builder(this);
		}

		builder.setTitle(R.string.request_permission_title);
		builder.setMessage(R.string.request_permission_message);
		builder.setPositiveButton(android.R.string.ok,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface aDialog, int aButton) {
						ComponentName adminComponent = new ComponentName(MainActivity.this, PermissionReceiver.class);
						requestPermission(adminComponent);
					}
				});

		builder.setNegativeButton(android.R.string.cancel,new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface aDialog, int aButton) {
						aDialog.dismiss();
						finish();
					}
				});

		builder.create().show();
	}
	/**
	 * 
	 * @param aAdminComponent
	 */
	public void requestPermission(ComponentName aAdminComponent) {
		String explanation = getResources().getString(R.string.request_permission_explanation);

		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
		intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, aAdminComponent);
		intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, explanation);

		startActivityForResult(intent, REQUEST_CODE_ENABLE_ADMIN);
	}
	

	/**
	 * 
	 * @author QUOC NGUYEN
	 *
	 */
	public static final class PermissionReceiver extends DeviceAdminReceiver {

		@Override
		public void onDisabled(Context aContext, Intent aIntent) {
			Toast.makeText(aContext, R.string.on_permission_disabled, Toast.LENGTH_LONG).show();
		}
	}
	/**
	 * 
	 * @author QUOC NGUYEN
	 *
	 */
	public class TurnOffScreenReciever extends BroadcastReceiver {

		
		@SuppressWarnings("unused")
		@Override
		public void onReceive(Context context, Intent intent) {
			try {
				DevicePolicyManager policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
				//lockScreen(policyManager);
				Log.d(TAG, "OK");
			}catch(Exception ex){
				//Toast.makeText(getApplicationContext(), "You must Active Screen!", Toast.LENGTH_SHORT).show();
				requestPermission(adminComponent);
			}
			
		}
		
	}
	
	/**
	 * 
	 * @author QUOC NGUYEN
	 *
	 */
	public class MyReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
				Log.v("$$$$$$", "In Method:  ACTION_SCREEN_OFF");
				// onPause() will be called.
			} else if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
				Log.v("$$$$$$", "In Method:  ACTION_SCREEN_ON");
				turnOffScreenReciever = new TurnOffScreenReciever();
	    		registerReceiver(turnOffScreenReciever, new IntentFilter(MyWidgetProvider.WIDGET_BUTTON));	

			} else if (intent.getAction().equals(Intent.ACTION_USER_PRESENT)) {
				Log.v("$$$$$$", "In Method:  ACTION_USER_PRESENT");
				// Handle resuming events
			}
		}

	}
}
