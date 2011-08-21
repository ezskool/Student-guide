package no.studassen.social;

import android.app.Activity;
import no.studassen.main.R;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class EventActivity extends Activity implements OnClickListener{
	private TextView tv_event_title, tv_event_description, tv_event_link;
	private String link;
	private ImageView banner;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.event);
		
		Bundle extras = getIntent().getExtras();
		link = extras.getString("link");
		
		tv_event_title = (TextView)findViewById(R.id.tv_event_title);
		tv_event_description = (TextView)findViewById(R.id.tv_event_description);
		tv_event_link = (TextView)findViewById(R.id.tv_event_link);
	
		tv_event_title.setText(extras.getString("title"));
		tv_event_description.setText(extras.getString("description"));
			
		SpannableString content = new SpannableString(link);
		content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
		tv_event_link.setText(content);
		tv_event_link.setOnClickListener(this);
		
		banner = (ImageView)findViewById(R.id.iv_event_banner);
		banner.setImageResource(extras.getInt("bannerResource"));
	}

	@Override
	public void onClick(View v) {
		if(v==tv_event_link) {
			Intent browserIntent = new Intent("android.intent.action.VIEW", Uri.parse(link));
			startActivity(browserIntent);
		}
	}
}
