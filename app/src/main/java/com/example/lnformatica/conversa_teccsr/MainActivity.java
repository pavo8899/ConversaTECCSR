package com.example.lnformatica.conversa_teccsr;

import android.Manifest;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;


import com.vikramezhil.droidspeech.DroidSpeech;
import com.vikramezhil.droidspeech.OnDSListener;
import com.vikramezhil.droidspeech.OnDSPermissionsListener;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDSListener, OnDSPermissionsListener {

    private static final int MY_PERMISSIONS_REQUEST_RECORD_AUDIO = 0 ;
    DroidSpeech droidSpeech;

    String Speech="Current Speech:\n";
    TextView SpeechView;

    FloatingActionButton fab;

    boolean Status=false;
    int Volume1,Volume2;

    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        droidSpeech = new DroidSpeech(this.getApplicationContext(), null);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        if(Status)
            fab.setImageResource(android.R.drawable.ic_media_pause);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FABChangeIcon(view);
            }
        });
        SpeechView=(TextView)findViewById(R.id.speech);
        SpeechView.setText(Speech);

        /* Set the listener */
        droidSpeech.setOnDroidSpeechListener(this);

        audioManager=(AudioManager)getSystemService(Context.AUDIO_SERVICE);
    }

    public void FABChangeIcon(View view)
    {
        if(!Status)
        {
            Snackbar.make(view, "Iniciando Reconocimiento", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            fab.setImageResource(android.R.drawable.ic_media_pause);
            Status=true;
            droidSpeech.startDroidSpeechRecognition();

            try{
                Volume1=audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,0,0);

            }catch (Exception ex){ Toast toast = Toast.makeText(this.getApplicationContext(), "Error:\n"+ex.getMessage(), Toast.LENGTH_SHORT);
                toast.show();}

        }
        else
        {
            Snackbar.make(view, "Deteniendo Reconocimiento", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show();
            fab.setImageResource(android.R.drawable.ic_media_play);
            Status=false;
            droidSpeech.closeDroidSpeechOperations();

            try{
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,Volume1,0);

            }catch (Exception ex){Toast toast = Toast.makeText(this.getApplicationContext(), "Error:\n"+ex.getMessage(), Toast.LENGTH_SHORT);
                toast.show();}
        }

    }

    public void Example()
    {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //droidSpeech Methods

    @Override
    public void onDroidSpeechSupportedLanguages(String currentSpeechLanguage, List<String> supportedSpeechLanguages)
    {
        // Triggered when the device default languages are retrieved
    }

    @Override
    public void onDroidSpeechRmsChanged(float rmsChangedValue)
    {
        // Triggered whenever the sound level in the speech of the user has changed
    }

    @Override
    public void onDroidSpeechLiveResult(String liveSpeechResult)
    {
        // Triggered during live speech of the user`
        SpeechView.setText(Speech+liveSpeechResult);
    }

    @Override
    public void onDroidSpeechFinalResult(String finalSpeechResult)
    {
        // Triggered after the user finishes the speech
        Speech=Speech+finalSpeechResult+'\n';
        SpeechView.setText(Speech);
    }

    @Override
    public void onDroidSpeechClosedByUser()
    {
        // Triggered if user closes the recognition progress view
    }

    @Override
    public void onDroidSpeechError(String errorMsg)
    {
        // Triggered when droid speech encounters an error
        Toast toast = Toast.makeText(this.getApplicationContext(), "Error:\n"+errorMsg+"\n\nDeteniendo Reconocimiento", Toast.LENGTH_SHORT);
        toast.show();

        fab.setImageResource(android.R.drawable.ic_media_play);
        Status=false;
        droidSpeech.closeDroidSpeechOperations();

        try{
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,Volume1,0);

        }catch (Exception ex){}

        ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.RECORD_AUDIO},MY_PERMISSIONS_REQUEST_RECORD_AUDIO);

    }

    @Override
    public void onDroidSpeechAudioPermissionStatus(boolean audioPermissionGiven, String errorMsgIfAny)
    {
        if(!audioPermissionGiven)
        {
            Toast toast = Toast.makeText(this.getApplicationContext(), "Error:--\n"+errorMsgIfAny, Toast.LENGTH_SHORT);
            toast.show();


        }

    }
}
