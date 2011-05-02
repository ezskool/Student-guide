package studentguiden.ntnu.news;

import studentguiden.ntnu.main.R;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class NewsActivity extends Activity implements OnClickListener{
	
	private ImageView btn_refresh, btn_back;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);
		
		btn_refresh = (ImageView)findViewById(R.id.btn_refresh);
		btn_back = (ImageView)findViewById(R.id.btn_back);
		btn_refresh.setOnClickListener(this);
		btn_back.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if(v==btn_back) {
			super.finish();
		}else if(v==btn_refresh) {
			//TODO: refresh
		}
	}

}
