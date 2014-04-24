package ca.gsalisi.unimessenger;

import android.graphics.Color;
import android.media.AudioManager;
import android.media.SoundPool;
import android.view.View;
import android.widget.TextView;
import com.firebase.client.Query;

public class MessageListAdapter extends FirebaseListAdapter<Message> {

	String username;
	MessengerActivity main;
	
	public MessageListAdapter(Query limit, MessengerActivity activity,
			int myListView, String username) {

		super(limit, Message.class, myListView, activity);
		this.username = username;
		this.main = activity;
	}
	@Override
	protected void populateView( View view, Message message){
		String msgUser = message.getUser();
		TextView userNameView = (TextView) view.findViewById(R.id.userText);
		TextView msgView = (TextView) view.findViewById(R.id.userMessage);
		
		userNameView.setText(msgUser);
		
		if(username.equals(msgUser)){
			userNameView.setTextColor(Color.parseColor("#354B5E"));
			view.setBackgroundColor(Color.parseColor("#dcddd8"));
			
		}else{
			userNameView.setTextColor(Color.parseColor("#D74B4B"));
			view.setBackgroundColor(Color.parseColor("#eeeeee"));
		}
		userNameView.setTypeface(main.customTp);
		msgView.setTypeface(main.customTp);
		
		((TextView) view.findViewById(R.id.userMessage)).setText(message.getMsg());
		
		
	}
	

}
