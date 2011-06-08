package studentguiden.ntnu.news;

import studentguiden.ntnu.main.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

public class NewsActivity extends Activity implements OnClickListener{

	private ImageView btn_back;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.news);

		btn_back = (ImageView)findViewById(R.id.btn_back);
		btn_back.setOnClickListener(this);
	}


	@Override
	public void onClick(View v) {
		if(v==btn_back) {
			super.finish();
		}
	}
}
