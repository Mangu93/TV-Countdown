package Mangu.showcountdown;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyActivity extends Activity {
	// String necesarios
	private static TextView result_show;
	private static String result_show_string;
	private static final String show_init = "http://api.trakt.tv/show/seasons.json/a5cddd67244d70d9fb782579fa64bc87/";
	private static final String episode_init = "http://api.trakt.tv/show/season.json/a5cddd67244d70d9fb782579fa64bc87/";
	private static final String summary_init = "http://api.trakt.tv/show/episode/summary.json/a5cddd67244d70d9fb782579fa64bc87/";
	// Objetos en la UI
	private Button about;
	private ImageView banner_show;
	private EditText input_text;
	private Button search_sender;
	static String vv = "";
	private TextView text_result;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my);
		MyActivity.result_show = (TextView) findViewById(R.id.result_show);
		result_show_string = result_show.getText().toString();
		this.search_sender = ((Button) findViewById(R.id.search_sender));
		this.banner_show = ((ImageView) findViewById(R.id.banner_show));
		this.text_result = ((TextView) findViewById(R.id.text_result));
		this.input_text = ((EditText) findViewById(R.id.input_text));
		this.about = ((Button) findViewById(R.id.about_button));

		this.search_sender.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				try {
					StrictMode
							.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
									.permitAll().build());
					String show_to_search = input_text.getText().toString();
					Toast.makeText(getApplicationContext(), getText(R.string.wait)+""+show_to_search, Toast.LENGTH_LONG).show();
					show_to_search = changeShow(show_to_search);
					show_to_search = show_to_search.toLowerCase().replace(" ",
							"-");

					String str2 = show_init + show_to_search;
					Downloader localBackgroundDownload = new Downloader(
							show_to_search);
					localBackgroundDownload.execute(str2);
					
					/*
					 * String season_number = String.valueOf(new JSONObject(
					 * (makeJSON(localBackgroundDownload.execute(str2) .get(5L,
					 * TimeUnit.SECONDS)))).get("season")); String str5 =
					 * episode_init + show_to_search + "/" + season_number;
					 * 
					 * // hacer un execute // localBackgroundDownload = new
					 * Downloader(); JSONArray localJSONArray = new JSONArray(
					 * localBackgroundDownload.execute(str5).get(5L,
					 * TimeUnit.SECONDS));
					 * 
					 * String episode_number = getRightEpisode(localJSONArray);
					 * JSONObject localJSONObject = localJSONArray
					 * .getJSONObject(Integer.parseInt(episode_number) - 1); //
					 * Aqui esta el fallo. String episode_title =
					 * String.valueOf(localJSONObject .getString("title"));
					 * String iso_date = String.valueOf(localJSONObject
					 * .get("first_aired_iso")); final String final_date =
					 * getFinalDate(iso_date); String str10 = summary_init +
					 * show_to_search + "/" + season_number + "/" +
					 * episode_number; // localBackgroundDownload = new
					 * Downloader(); JSONObject banner = new
					 * JSONObject(localBackgroundDownload
					 * .execute(str10).get(5L, TimeUnit.SECONDS)); String
					 * banner_url = banner.getJSONObject("show")
					 * .getJSONObject("images").getString("banner"); if
					 * (checkDate(iso_date)) { if
					 * (episode_title.equalsIgnoreCase("TBA")) { episode_title =
					 * getApplicationContext().getString( R.string.tba); }
					 * result_show.setText((getText(R.string.result_en)) + " " +
					 * episode_title); text_result.setVisibility(View.VISIBLE);
					 * text_result.setText((getText(R.string.date_next)) + " " +
					 * final_date); InputStream localIS = getImage(banner_url);
					 * Bitmap localBitmap = BitmapFactory
					 * .decodeStream(localIS);
					 * banner_show.setImageBitmap(localBitmap);
					 * banner_show.setVisibility(View.VISIBLE); new
					 * AlertDialog.Builder(MyActivity.this) .setTitle(
					 * getText(R.string.title_reminder) + " " + episode_title)
					 * .setMessage(R.string.setCalendar)
					 * .setPositiveButton(R.string.yes, new
					 * DialogInterface.OnClickListener() {
					 * 
					 * @Override public void onClick( DialogInterface dialog,
					 * int which) { Calendar calendar = Calendar .getInstance();
					 * calendar.set( Calendar.DAY_OF_MONTH, getDay(final_date));
					 * calendar.set(Calendar.MONTH, getMonth(final_date));
					 * calendar.set(Calendar.YEAR, getYear(final_date));
					 * calendar.set(Calendar.AM_PM, Calendar.PM); calendar.set(
					 * Calendar.HOUR_OF_DAY, 9); calendar.set(Calendar.MINUTE,
					 * 0); calendar.set(Calendar.SECOND, 0);
					 * 
					 * AlarmManager alarmManager = (AlarmManager)
					 * getSystemService(Context.ALARM_SERVICE); Intent myIntent
					 * = new Intent( MyActivity.this, MyAlarmService.class);
					 * PendingIntent pendingIntent = PendingIntent .getService(
					 * MyActivity.this, 0, myIntent, 0); alarmManager
					 * .set(AlarmManager.RTC, calendar.getTimeInMillis(),
					 * pendingIntent); dialog.cancel(); } })
					 * .setNegativeButton(R.string.no, new
					 * DialogInterface.OnClickListener() {
					 * 
					 * @Override public void onClick( DialogInterface dialog,
					 * int which) { // Do nothing. dialog.cancel(); }
					 * }).setIcon(R.drawable.ic_launcher) .show();
					 * localIS.close(); } else {
					 * result_show.setText(R.string.no_episodes);
					 * banner_show.setVisibility(View.INVISIBLE);
					 * text_result.setVisibility(View.INVISIBLE); }
					 */
				} catch (Exception exx) {
					result_show.setText(R.string.no_exists);
					banner_show.setVisibility(View.INVISIBLE);
					text_result.setVisibility(View.INVISIBLE);
				}

			}
		});
		this.about.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent localIntent = new Intent(getApplicationContext(),
						about_me.class);
				startActivity(localIntent);
			}
		});
	}

	private static String makeJSON(String paramString) {
		StringBuilder localStringBuilder = new StringBuilder(paramString);
		if (paramString.length() > 1) {
			localStringBuilder.deleteCharAt(0);
			localStringBuilder.deleteCharAt(-1 + localStringBuilder.length());
		}
		String str = localStringBuilder.toString();
		return str;
	}

	private static String getFinalDate(String paramString) {
		String str1 = paramString.substring(0, 4);
		String str2 = paramString.substring(5, 7);
		return paramString.substring(8, 10) + "-" + str2 + "-" + str1;
	}

	private int getDay(String paramString) {
		return Integer.getInteger(paramString.substring(0, 2),
				new Date().getDate());
	}

	private static int getMonth(String paramString) {
		return Integer.getInteger(paramString.substring(3, 5),
				new Date().getMonth());
	}

	private static int getYear(String paramString) {
		return Integer.getInteger(paramString.substring(6, 10),
				new Date().getYear());
	}

	/**
	 * @author Adrian Marin Returns true if date given is after actual date
	 * @param paramString
	 * @return i
	 * @throws ParseException
	 */
	private static boolean checkDate(String paramString) throws ParseException {
		int i = 0;
		SimpleDateFormat localSimpleDateFormat = new SimpleDateFormat(
				"yyyy-MM-dd");
		Date localDate1 = localSimpleDateFormat.parse(paramString);
		Date localDate2 = localSimpleDateFormat.parse(localSimpleDateFormat
				.format(new Date()));
		if ((localDate1.after(localDate2)) || (localDate1.equals(localDate2)))
			i = 1;
		return i == 0 ? false : true;
	}

	private static InputStream getImage(String paramString) throws IOException {
		return ((HttpURLConnection) new URL(paramString).openConnection())
				.getInputStream();
	}

	/**
	 * @author Adrian Marin This method should the good number of the episode.
	 * @param localJSON
	 * @return numberOfEpisode
	 */

	private static String getRightEpisode(JSONArray localJSON)
			throws ParseException {
		String numberOfEpisode = "";
		for (int i = 0; i < localJSON.length(); i++) {
			JSONObject localObject = localJSON.getJSONObject(i);
			numberOfEpisode = String.valueOf(localObject.get("episode"));
			String iso_date = (String.valueOf(localObject
					.get("first_aired_iso")));
			if (checkDate(iso_date)) {
				break;
			}
		}
		return numberOfEpisode;
	}

	/**
	 * @author Adrian Marin This method is bullshit but is the only thing I can
	 *         do, I can't use the switch statement with a String
	 * @param paramString
	 * @return str
	 */
	private String changeShow(String paramString) {
		String str = paramString;
		if (paramString.equalsIgnoreCase("juego de tronos")) {
			str = "game-of-thrones";
		}
		if (paramString.equalsIgnoreCase("la cupula")) {
			str = "under-the-dome";
		}
		if (paramString.equalsIgnoreCase("big bang theory")) {
			str = "the-big-bang-theory";
		}
		if (paramString.equalsIgnoreCase("doctor who")) {
			str = "doctor-who-2005";
		}
		if (paramString.equalsIgnoreCase("the bridge")) {
			str = "the-bridge-2013";
		}
		if ((paramString.equalsIgnoreCase("2 chicas sin blanca"))
				|| (paramString.equalsIgnoreCase("dos chicas sin blanca"))) {
			str = "2-broke-girls";
		}
		if (paramString.equalsIgnoreCase("sobrenatural")) {
			str = "supernatural";
		}
		if (paramString.equalsIgnoreCase("once upon a time")) {
			str = "once-upon-a-time-2011";
		}
		if (paramString.equalsIgnoreCase("wilfred")) {
			str = "wilfred-us";
		}
		if (paramString.equalsIgnoreCase("Walking dead")) {
			str = "the-walking-dead";
		}
		if (paramString.equalsIgnoreCase("cronicas vampiricas")) {
			str = "the-vampire-diaries";
		}
		if ((paramString.equalsIgnoreCase("agents of shield"))
				|| (paramString.equalsIgnoreCase("agentes de shield"))
				|| (paramString.equalsIgnoreCase("Marvel Agents of Shield"))) {
			str = "marvels-agents-of-shield";
		}
		if (paramString.equalsIgnoreCase("masterchef")) {
			str = "masterchef-us";
		}
		if (paramString.equalsIgnoreCase("faking it")) {
			str = "faking-it-2014";
		}
		return str;
	}

	private class Downloader extends
			AsyncTask<String, Pair<String, Object>, String> {
		private String show_to_search;

		/*
		 * http://stackoverflow.com/questions/8183111/accessing-views-from-other-
		 * thread-android
		 */
		public Downloader(String show_to_search) {
			this.show_to_search = show_to_search;
		}

		@Override
		protected String doInBackground(String... arg0) {

			try {
				JSONObject season_number_json = new JSONObject(
						makeJSON(download(arg0[0])));
				String season_number = String.valueOf(season_number_json
						.get("season"));
				String str5 = episode_init + show_to_search + "/"
						+ season_number;
				JSONArray localJSONArray = new JSONArray(download(str5));
				String episode_number = getRightEpisode(localJSONArray);
				JSONObject localJSONObject = localJSONArray
						.getJSONObject(Integer.parseInt(episode_number) - 1);

				String episode_title = String.valueOf(localJSONObject
						.getString("title"));
				String iso_date = String.valueOf(localJSONObject
						.get("first_aired_iso"));
				final String final_date = getFinalDate(iso_date);
				String str10 = summary_init + show_to_search + "/"
						+ season_number + "/" + episode_number;

				JSONObject banner = new JSONObject(download(str10));
				String banner_url = banner.getJSONObject("show")
						.getJSONObject("images").getString("banner");
				if (checkDate(iso_date)) {
					if (episode_title.equalsIgnoreCase("TBA")) {
						episode_title = getApplicationContext().getString(
								R.string.tba);
					}
					/*
					 * result_show.setText((getText(R.string.result_en)) + " " +
					 * episode_title);
					 */
					Pair<String, Object> pair_rs = new Pair<String, Object>(
							"result_show", episode_title);
					publishProgress(pair_rs);
					/*
					 * text_result.setVisibility(View.VISIBLE);
					 * text_result.setText((getText(R.string.date_next)) + " " +
					 * final_date);
					 */
					Pair<String, Object> pair_tr = new Pair<String, Object>(
							"text_result", final_date);
					publishProgress(pair_tr);
					InputStream localIS = getImage(banner_url);
					Bitmap localBitmap = BitmapFactory.decodeStream(localIS);
					Pair<String, Object> pair_bm = new Pair<String, Object>(
							"banner", localBitmap);
					publishProgress(pair_bm);
					/*
					 * banner_show.setImageBitmap(localBitmap);
					 * banner_show.setVisibility(View.VISIBLE);
					 */
				}else {
					Pair<String,Object> pair_else = new Pair<String,Object>("else", "");
					publishProgress(pair_else);
				}
			} catch (ParseException e) {
			} catch (FileNotFoundException exx) {
			} catch (IOException ex) {
				/*
				 * Pair<String, Object> pair_ex = new Pair<String,
				 * Object>("IOException", ex); publishProgress(pair_ex);
				 */
			}

			return "";

		}

		@Override
		protected void onProgressUpdate(Pair<String, Object>... values) {
			if (values[0].first.equalsIgnoreCase("result_show")) {
				if (values[0].second instanceof String) {
					result_show.setText((getText(R.string.result_en)) + " "
							+ values[0].second);
				}
			}
			if (values[0].first.equalsIgnoreCase("text_result")) {
				if (values[0].second instanceof String) {
					text_result.setVisibility(View.VISIBLE);
					text_result.setText((getText(R.string.date_next)) + " "
							+ values[0].second);
				}
			}
			if (values[0].first.equalsIgnoreCase("banner")) {
				if (values[0].second instanceof Bitmap) {
					banner_show.setImageBitmap((Bitmap) values[0].second);
					banner_show.setVisibility(View.VISIBLE);
				}
			}if (values[0].first.equalsIgnoreCase("else")) {
				banner_show.setVisibility(View.INVISIBLE);
				text_result.setVisibility(View.INVISIBLE);
				result_show.setText(R.string.no_episodes);
				
			}
			/*
			 * if (values[0].first.equalsIgnoreCase("IOException")) {
			 * 
			 * }
			 */
		}

		private String download(String... arg0) throws IOException {
			String input = "";
			try {
				URL url = new URL(arg0[0]);
				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.setRequestMethod("GET");

				BufferedReader buff = new BufferedReader(new InputStreamReader(
						conn.getInputStream()));

				String text;
				while ((text = buff.readLine()) != null) {
					input += text;
				}
				buff.close();

			} catch (MalformedURLException localMalformedURLException) {
				localMalformedURLException.printStackTrace();
			}
			return input;
		}

	}
}
