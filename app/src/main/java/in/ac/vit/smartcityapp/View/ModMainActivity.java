package in.ac.vit.smartcityapp.View;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.ImageButton;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.ac.vit.smartcityapp.AppController;
import in.ac.vit.smartcityapp.Controller.CustomRVAdapter;
import in.ac.vit.smartcityapp.Model.Entities.DeviceConfig;
import in.ac.vit.smartcityapp.Model.Interfaces.ActivityAdapterCommunication;
import in.ac.vit.smartcityapp.R;


public class ModMainActivity extends AppCompatActivity implements RecognitionListener, ActivityAdapterCommunication{

    private static final String TAG = "TAG";
    @BindView(R.id.activity_main_record)
    ImageButton imgRecord ;

    @BindView(R.id.activity_main_recyclerView)
    RecyclerView rvGrid ;

    private CustomRVAdapter customRVAdapter ;
    List<DeviceConfig> deviceConfigList ;

    private SpeechRecognizer speechRecognizer = null ;
    private Intent recognizerIntent;
    private TextToSpeech textToSpeech ;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ButterKnife.bind(this) ;
        setContentView(R.layout.activity_main);
        
        init();
    }

    private void init() {
        addDataToArray() ;

        customRVAdapter = new CustomRVAdapter(this, deviceConfigList) ;
        rvGrid.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rvGrid.setAdapter(customRVAdapter);

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(this) ;
        speechRecognizer.setRecognitionListener(this);
        recognizerIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE,
                "en");
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                this.getPackageName());
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true) ;
        recognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_WEB_SEARCH);
        /*recognizerIntent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 3);*/

        textToSpeech = new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if(i != TextToSpeech.ERROR)
                    textToSpeech.setLanguage(Locale.ENGLISH) ;
            }
        });

    }

    private void addDataToArray() {
        deviceConfigList = new ArrayList<>() ;
        deviceConfigList.add(new DeviceConfig(1, "Street Light", false, "Simple")) ;
        deviceConfigList.add(new DeviceConfig(2, "Street Light", false, "Blink")) ;
        deviceConfigList.add(new DeviceConfig(3, "Building", false, "Simple")) ;
    }
    
    @Override
    public void onReadyForSpeech(Bundle bundle) {

    }

    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

    }

    @Override
    public void onEndOfSpeech() {

    }

    @Override
    public void onError(int i) {

    }

    @Override
    public void onResults(Bundle bundle) {

    }

    @Override
    public void onPartialResults(Bundle bundle) {

    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    public void notifyOnServer(final int id, final boolean status) {
        Log.d(TAG, "notifyOnServer() called with: id = [" + id + "], status = [" + status + "]");
        String irisServerUrl = "http://139.59.31.235:6969/device" ;

        StringRequest updateOnServerUrl = new StringRequest(Request.Method.POST, irisServerUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.i(TAG, "onResponse: " + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse response = error.networkResponse ;
                Log.i(TAG, "onErrorResponse: " + new String(response.data) );
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> postParms = new HashMap<>();

                String state ;
                if(status)
                    state = "1" ;
                else
                    state = "0" ;

                postParms.put("deviceID", String.valueOf(id+1)) ;
                postParms.put("deviceState", state) ;
                postParms.put("timeStamp", String.valueOf(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.ENGLISH))) ;
                return postParms ;
            }
        };

        AppController.getInstance().addToRequestQueue(updateOnServerUrl);
    }
}
