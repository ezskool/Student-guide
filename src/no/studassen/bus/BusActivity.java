package no.studassen.bus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;

import no.studassen.main.R;
import no.studassen.misc.Util;
import android.app.Activity;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BusActivity extends Activity implements OnClickListener{ 
	private final int DIALOG_ORACLE_ANSWER = 0;
	
	private EditText et_ask_bus;
	private Button btn_ask_bus;
	private String oracleAnswer = "";
	private BusFinder busFinder;
	private Dialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bus);

		et_ask_bus = (EditText)findViewById(R.id.et_ask_bus);
		btn_ask_bus = (Button)findViewById(R.id.btn_ask_bus);
		btn_ask_bus.setOnClickListener(this);

		try {
			busFinder = new BusFinder();
		} catch (URISyntaxException e) {
			Util.log("ATB URL invalid. It might have changed");
			e.printStackTrace();
			//TODO: vis melding til brukeren.
		}
	}

	@Override
	public void onClick(View v) {
		if(v==btn_ask_bus) {
			new BusDownloader().execute(et_ask_bus.getText().toString());
		}
	}

	
	//TODO: fungerer ikke om man spør 2 ganger på rad (dialog oppdaterer seg ikke)
	@Override
	public Dialog onCreateDialog(int id) {

		if(id==DIALOG_ORACLE_ANSWER) {
			
			dialog =  new Dialog(this);
			dialog.setContentView(R.layout.dialog_bus);
			dialog.setCanceledOnTouchOutside(true);
			TextView tv_bus_answer = (TextView)dialog.findViewById(R.id.tv_bus_answer);
			tv_bus_answer.setText(oracleAnswer);
			dialog.setTitle("Svar fra bussorakelet");
			Util.log(oracleAnswer);
			return dialog;
		}
		return null;
	}

	public void setOracleAnswer(String answer) {
		this.oracleAnswer  = answer;
	}

	private class BusDownloader extends AsyncTask<String, Void, Integer> {
		private final int DOWNLOAD_SUCCESSFUL = 0;
		private final int DOWNLOAD_FAILED = 1;
		private final int DOWNLOAD_FAILED_INVALID_URL = 2;
		private String answer;


		@Override
		protected Integer doInBackground(String... params) {
			Util.log("asking bus oracle:"+params[0]);
			try {
				answer = BusActivity.this.busFinder.askOracle(params[0]);
			}catch (MalformedURLException e) {
				Util.log("Asking bus oracle failed: Malformed Url");
				e.printStackTrace();
				return DOWNLOAD_FAILED_INVALID_URL;
			} catch (IOException e) {
				Util.log("Asking bus oracle failed: IOException");
				e.printStackTrace();
				return DOWNLOAD_FAILED;
			}

			return DOWNLOAD_SUCCESSFUL;
		}

		@Override
		protected void onPostExecute(Integer result) {
			if(result==DOWNLOAD_SUCCESSFUL) {
				BusActivity.this.oracleAnswer = answer;
				BusActivity.this.showDialog(DIALOG_ORACLE_ANSWER);
				Util.log("Bus Oracle download successful:"+BusActivity.this.oracleAnswer);
			}else if(result==DOWNLOAD_FAILED_INVALID_URL || result==DOWNLOAD_FAILED) {
				Util.log("Bus data download failed");
				Util.displayToastMessage(getString(R.string.download_failed_toast),BusActivity.this.getApplicationContext());
			}
		}
	}
}
