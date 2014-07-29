package my.app.khu.service;


import java.util.ArrayList;

import my.app.khu.R;
import my.app.khu.lockscreen.MainActivity.PermissionReceiver;
import my.app.khu.manager.Common;
import my.app.khu.manager.PInfo;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListPopupWindow;

@SuppressLint("ClickableViewAccessibility")
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ServiceFloating extends Service {

	private static final int MAX_RETRY_COUNT = 4;
	private static final int RETRY_DELAY = 100;

	public static  int ID_NOTIFICATION = 2018;

	private WindowManager windowManager;
	private ImageView chatHead;
	//private PopupWindow pwindo;

	boolean mHasDoubleClicked = false;
	long lastPressTime;
	private Boolean _enable = true;
	private Boolean flag_long_click = false;
	ArrayList<String> myArray;
	ArrayList<PInfo> apps;
	//List listCity;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override 
	public void onCreate() {
		super.onCreate();
		
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());

//		RetrievePackages getInstalledPackages = new RetrievePackages(getApplicationContext());
//		apps = getInstalledPackages.getInstalledApps(false);
//		myArray = new ArrayList<String>();
//
//		for(int i=0 ; i<apps.size() ; ++i) {
//			myArray.add(apps.get(i).appname);
//		}
//
//		listCity = new ArrayList();
//		for(int i=0 ; i<apps.size() ; ++i) {
//			listCity.add(apps.get(i));
//		}
		windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

		chatHead = new ImageView(this);
		
		chatHead.setImageResource(R.drawable.floating2);
		int color = Common.getColor(getApplicationContext());
		Drawable draw = chatHead.getDrawable();
		draw.setColorFilter(new PorterDuffColorFilter(color, Mode.MULTIPLY));
		chatHead.setImageDrawable(draw);
		
		if(prefs.getString("ICON", "floating2").equals("floating3")){
			chatHead.setImageResource(R.drawable.floating3);
		} else if(prefs.getString("ICON", "floating2").equals("floating4")){
			chatHead.setImageResource(R.drawable.floating4);
		} else if(prefs.getString("ICON", "floating2").equals("floating5")){
			chatHead.setImageResource(R.drawable.floating5);
		} else if(prefs.getString("ICON", "floating2").equals("floating5")){
			chatHead.setImageResource(R.drawable.floating2);
		}

		final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.WRAP_CONTENT,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		params.gravity = Gravity.TOP | Gravity.LEFT;
		params.x = 0;
		params.y = 100;

		windowManager.addView(chatHead, params);

		try {
			chatHead.setOnTouchListener(new View.OnTouchListener() {
				private WindowManager.LayoutParams paramsF = params;
				private int initialX;
				private int initialY;
				private float initialTouchX;
				private float initialTouchY;
				private DevicePolicyManager policyManager;
				private ComponentName adminComponent;

				@Override public boolean onTouch(View v, MotionEvent event) {
					switch (event.getAction()) {
					case MotionEvent.ACTION_DOWN:
						_enable = false;
						//flag_long_click = false;
						// Get current time in nano seconds.
						long pressTime = System.currentTimeMillis();


						// If double click...
						if (pressTime - lastPressTime <= 300) {
//							createNotification();
//							ServiceFloating.this.stopSelf();
//							mHasDoubleClicked = true;
							_enable = true;
							
							//				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
							//				intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
							//				getApplicationContext().startActivity(intent);
							
							if(_enable && flag_long_click) {
								policyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
								adminComponent = new ComponentName(getApplicationContext(), PermissionReceiver.class);
								if (policyManager.isAdminActive(adminComponent)) {
									lockScreen(policyManager);
									flag_long_click = false;
								} else {
									//Toast.makeText(this, "You must enable this app in device admin by open Lock Screen and click ENABLE button!", Toast.LENGTH_SHORT).show();
								}
							}
						}
						else {     // If not double click....
						//	mHasDoubleClicked = false;
						}
						lastPressTime = pressTime; 
						initialX = paramsF.x;
						initialY = paramsF.y;
						initialTouchX = event.getRawX();
						initialTouchY = event.getRawY();
						
						break;
					case MotionEvent.ACTION_UP:
						_enable = false;
						if (initialX == paramsF.x && initialY == paramsF.y) {
							flag_long_click = true;
						}
						break;
				
					case MotionEvent.ACTION_MOVE:
						_enable = false;
						paramsF.x = initialX + (int) (event.getRawX() - initialTouchX);
						paramsF.y = initialY + (int) (event.getRawY() - initialTouchY);
						windowManager.updateViewLayout(chatHead, paramsF);
						break;
					}
					return false;
				}
			});
		} catch (Exception e) {
		}

//		chatHead.setOnLongClickListener(new OnLongClickListener() {
//			
//			@Override
//			public boolean onLongClick(View v) {
//				Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//				startActivity(intent);
//				flag_long_click = false;
//				return false;
//			}
//		});
		chatHead.setOnClickListener(new View.OnClickListener() {

		

			@Override
			public void onClick(View arg0) {
				//initiatePopupWindow(chatHead); show list app menu
			
			}
		});

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
					//finish();
				}
			}
		});
	}

	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	private void initiatePopupWindow(View anchor) {
		try {
			Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
			ListPopupWindow popup = new ListPopupWindow(this);
			popup.setAnchorView(anchor);
			popup.setWidth((int) (display.getWidth()/(1.5)));
			//ArrayAdapter<String> arrayAdapter = 
			//new ArrayAdapter<String>(this,R.layout.list_item, myArray);
			//popup.setAdapter(new CustomAdapter(getApplicationContext(), R.layout.row, listCity));
			popup.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View view, int position, long id3) {
					//Log.w("tag", "package : "+apps.get(position).pname.toString());
					Intent i;
					PackageManager manager = getPackageManager();
					try {
						i = manager.getLaunchIntentForPackage(apps.get(position).pname.toString());
						if (i == null)
							throw new PackageManager.NameNotFoundException();
						i.addCategory(Intent.CATEGORY_LAUNCHER);
						startActivity(i);
					} catch (PackageManager.NameNotFoundException e) {

					}
				}
			});
			popup.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	public void createNotification(){
		Intent notificationIntent = new Intent(getApplicationContext(), ServiceFloating.class);
		PendingIntent pendingIntent = PendingIntent.getService(getApplicationContext(), 0, notificationIntent, 0);

		Notification notification = new Notification(R.drawable.floating2, "Click to start launcher",System.currentTimeMillis());
		notification.setLatestEventInfo(getApplicationContext(), "Start launcher" ,  "Click to start launcher", pendingIntent);
		notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONGOING_EVENT;

		NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);

		notificationManager.notify(ID_NOTIFICATION,notification);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (chatHead != null) windowManager.removeView(chatHead);
	}

}