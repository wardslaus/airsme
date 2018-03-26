package com.airsme.activity;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.airsme.R;
import com.airsme.appconfig.Globals;
import com.airsme.models.DBUtil;
import com.airsme.models.ListenerMgr;
import com.airsme.models.MessegeItem;
import com.airsme.models.Messeges;
import com.airsme.models.Proxy;
import com.airsme.models.Tender;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;

public class Message extends AppCompatActivity {
   public  static Proxy receiver;
    public  static Tender subject;

    private static final String TAG = "ChatActivity";

    private ChatArrayAdapter chatArrayAdapter;
    private ListView listView;
    private EditText chatText;
    private Button buttonSend;
    private boolean side = true;

    Messeges chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        buttonSend = (Button) findViewById(R.id.send);

        listView = (ListView) findViewById(R.id.msgview);

        chatArrayAdapter = new ChatArrayAdapter(getApplicationContext(), R.layout.right);
        listView.setAdapter(chatArrayAdapter);

        chatText = (EditText) findViewById(R.id.msg);
        chatText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    return sendChatMessage();
                }
                return false;
            }
        });
        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                sendChatMessage();
            }
        });

        listView.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        listView.setAdapter(chatArrayAdapter);

        //to scroll the list view to bottom on data change
        chatArrayAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatArrayAdapter.getCount() - 1);
            }
        });

        // new RoundViews(this).themeControls((LinearLayout) findViewById(R.id.msg_main));
        getSupportActionBar().hide();

        getMsgs();
    }
    private boolean sendChatMessage() {
        //chatArrayAdapter.add(new ChatMessage(side, chatText.getText().toString()));
        chat.getMessages().add(new MessegeItem(Globals.CURRENT_USER.getID(),
                (Globals.CURRENT_USER.getID().equalsIgnoreCase(subject.getPKeyValue())? receiver.getPKeyValue():subject.getPKeyValue())
                , chatText.getText().toString()));
        DBUtil.createModel(chat);
        chatText.setText("");
        return true;
    }
    private void getMsgs(){
        chat=new Messeges(subject, receiver);
        DBUtil.retriaveModelByKey(chat, new ListenerMgr() {
            @Override
            public void methodHolder(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(Messeges.class)==null){
                    return;
                }
                chat=dataSnapshot.getValue(Messeges.class);
                for(MessegeItem mi:chat.getMessages()){
                    chatArrayAdapter.add(new ChatMessage(mi.getFrom().equalsIgnoreCase(Globals.CURRENT_USER.getID()),
                            mi.getContent()));
                }
            }
        }.onchangeListener(), true);
    }

}

class ChatMessage {
    public boolean left;
    public String message;

    public ChatMessage(boolean left, String message) {
        super();
        this.left = left;
        this.message = message;
    }
}

class ChatArrayAdapter extends ArrayAdapter<ChatMessage> {

    private TextView chatText;
    private List<ChatMessage> chatMessageList = new ArrayList<ChatMessage>();
    private Context context;

    @Override
    public void add(ChatMessage object) {
        chatMessageList.add(object);
        super.add(object);
    }

    public ChatArrayAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context = context;
    }

    public int getCount() {
        return this.chatMessageList.size();
    }

    public ChatMessage getItem(int index) {
        return this.chatMessageList.get(index);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ChatMessage chatMessageObj = getItem(position);
        View row = convertView;
        LayoutInflater inflater = (LayoutInflater) this.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (chatMessageObj.left) {
            row = inflater.inflate(R.layout.right, parent, false);
        }else{
            row = inflater.inflate(R.layout.left, parent, false);
        }
        chatText = (TextView) row.findViewById(R.id.msgr);
        chatText.setText(chatMessageObj.message);
        return row;
    }
}