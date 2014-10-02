package Mangu.showcountdown;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class MyAlarmService extends Service {
	private NotificationManager mManager;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@SuppressWarnings("static-access")
	   @Override
	   public void onStart(Intent intent, int startId)
	   {
	       super.onStart(intent, startId);
	      
	       mManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
	       Intent intent1 = new Intent(this.getApplicationContext(),MyActivity.class);
	       
	       Notification notification = new Notification(R.drawable.ic_launcher,"Alarma a�adida", System.currentTimeMillis());
	       intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
	 
	       PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
	       notification.flags |= Notification.FLAG_AUTO_CANCEL;
	       notification.setLatestEventInfo(this.getApplicationContext(), "TV Countdown", "Alarma a�adida", pendingNotificationIntent);
	 
	       mManager.notify(0, notification);
	    }

}
