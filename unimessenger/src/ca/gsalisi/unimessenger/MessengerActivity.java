package ca.gsalisi.unimessenger;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.app.ActionBar;
import android.app.ListActivity;
import android.database.DataSetObserver;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MessengerActivity extends ListActivity {

	private static final String FIREBASE_URL = "https://intense-fire-6647.firebaseio.com/";
	private MessageListAdapter listAdapter;
	private Firebase msgRef;
	private String username;
	private ValueEventListener connectedListener;
	private ListView listView;
	Typeface customTp;
	private boolean doneFirstPopulation;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_messenger);
		
		msgRef = new Firebase(FIREBASE_URL).child("unimessenger");
		
		customTp = Typeface.createFromAsset(this.getAssets(), 
				"bonvenocf_light.otf");
		
		username = "geoffrey";
		
		ActionBar actionBar = getActionBar();
		actionBar.setCustomView(R.layout.custom_action_bar);
		actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        ((TextView) findViewById(R.id.title_view)).setTypeface(customTp);
        ((TextView) findViewById(R.id.embedded_text_editor)).setTypeface(customTp);
        
		findViewById(R.id.send_button).setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				sendMessage();
			}
		});
		
		doneFirstPopulation = false;
		(new Handler()).postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				doneFirstPopulation = true;
			}
		}, 2000);
		
	}
	
	@Override
	protected void onStart(){
		super.onStart();
		
		listView = getListView();
		
		listAdapter = new MessageListAdapter(msgRef.limit(20), this, 
								R.layout.my_listview, username);
		listView.setAdapter(listAdapter);
		listAdapter.registerDataSetObserver(new DataSetObserver(){
			@Override
			public void onChanged(){
				super.onChanged();
				scrollListViewToBottom();
				listView.setSelection(listAdapter.getCount()-1);
				
			}
			
		});
		connectedListener = msgRef.getRoot().child(".info/connected").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                boolean connected = (Boolean)dataSnapshot.getValue();
                if (connected) {
                    Toast.makeText(MessengerActivity.this, "Connected!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MessengerActivity.this, "Disconnected!", Toast.LENGTH_SHORT).show();
                }
            }
			@Override
			public void onCancelled(FirebaseError arg0) {
				// TODO Auto-generated method stub
				
			}
        });

		
	}
	
	@Override
	public void onResume(){
		super.onResume();
		Log.d("onResume","Resumed Application");
		doneFirstPopulation = false;
		(new Handler()).postDelayed(new Runnable(){

			@Override
			public void run() {
				// TODO Auto-generated method stub
				doneFirstPopulation = true;
			}
		}, 2000);
		
	}
	 @Override
	 public void onStop() {
		 super.onStop();
		 Log.d("onStop","Stopped Application");
	     msgRef.getRoot().child(".info/connected").removeEventListener(connectedListener);
	     listAdapter.cleanup();
	 }

	private void sendMessage() {
		// TODO Auto-generated method stub
		EditText editText = (EditText) findViewById(R.id.embedded_text_editor);
		String inputMsg = editText.getText().toString();
		
		if(!inputMsg.equals("")){
			Message msg = new Message(inputMsg, username);
			
			msgRef.push().setValue(msg);
			editText.setText("");
		}
	}
	//scrolls list view to bottom
	private void scrollListViewToBottom() {
		listView.post(new Runnable() {
	        @Override
	        public void run() {
	            listView.setSelection(listAdapter.getCount() - 1);
	        }
	    });
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.messenger, menu);
		return true;
	}

	public void playSoundNotif() {
		// TODO Auto-generated method stub
		if(doneFirstPopulation){
			final SoundPool sp = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
			final int soundId = sp.load(this, R.raw.msg_notif, 1);
			(new Handler()).postDelayed(new Runnable(){
	
				@Override
				public void run() {
					// TODO Auto-generated method stub
					sp.play(soundId, 40, 40, 1, 0, 1);
				}}, 300);
			}
		}

}
