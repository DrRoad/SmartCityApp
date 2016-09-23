package in.ac.vit.smartcityapp.View;

import android.content.Intent;
import android.os.Handler;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.rey.material.widget.Switch;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ac.vit.smartcityapp.AppController;
import in.ac.vit.smartcityapp.Controller.CustomRVAdapter;
import in.ac.vit.smartcityapp.Model.Entities.DeviceConfig;
import in.ac.vit.smartcityapp.Model.Interfaces.ActivityAdapterCommunication;
import in.ac.vit.smartcityapp.R;
import rx.Observer;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.subjects.PublishSubject;

public class MainActivity extends AppCompatActivity implements RecognitionListener, ActivityAdapterCommunication{


    private static final String TAG = "TAG";
    @BindView(R.id.activity_main_recyclerView) RecyclerView recyclerViewGrid ;
    @BindView(R.id.activity_main_record) ImageButton recordButton ;

    private TextToSpeech textToSpeech ;
    private String fullText, text ;

    private CustomRVAdapter customRVAdapter ;
    List<DeviceConfig> deviceConfigList ;

    private SpeechRecognizer speechRecognizer = null ;
    private Intent recognizerIntent;
    private boolean isOn = false ;

    private PublishSubject<String> debouncer ;
    private Subscription subscription ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this) ;
        //createObservables();
        init() ;

    }

    private void init() {
        addDataToArray() ;
        customRVAdapter = new CustomRVAdapter(this, deviceConfigList) ;
        recyclerViewGrid.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        recyclerViewGrid.setAdapter(customRVAdapter);

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

    @OnClick(R.id.activity_main_record)
    void startRecording(){

        if(!isOn || speechRecognizer != null){
            speechRecognizer.startListening(recognizerIntent);
        }
        else
            speechRecognizer.stopListening();

        isOn = !isOn ;

    }
    @Override
    public void onBeginningOfSpeech() {

    }

    @Override
    public void onRmsChanged(float v) {

    }

    @Override
    public void onBufferReceived(byte[] bytes) {

        Log.d(TAG, "onBufferReceived() called with: bytes = [" + bytes + "]");
    }

    @Override
    public void onEndOfSpeech() {
        Log.i(TAG, "onEndOfSpeech: ");


    }

    @Override
    public void onError(int i) {
        String message ;
        switch (i) {
            case SpeechRecognizer.ERROR_AUDIO:
                message = "Audio recording error";
                break;
            case SpeechRecognizer.ERROR_CLIENT:
                message = "Client side error";
                break;
            case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                message = "Insufficient permissions";
                break;
            case SpeechRecognizer.ERROR_NETWORK:
                message = "Network error";
                break;
            case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                message = "Network timeout";
                break;
            case SpeechRecognizer.ERROR_NO_MATCH:
                message = "No match";
                break;
            case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                message = "RecognitionService busy";
                break;
            case SpeechRecognizer.ERROR_SERVER:
                message = "error from server";
                break;
            case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                message = "No speech input";
                break;
            default:
                message = "Didn't understand, please try again.";
                break;
        }
        Log.i(TAG, "onError: " + message);
    }

    @Override
    public void onResults(Bundle bundle) {
        ArrayList<String> matches = bundle
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        for (String result : matches)
            Log.i(TAG, "onPartialResults: " + result);
    }

    @Override
    public void onPartialResults(Bundle bundle) {
        ArrayList<String> matches = bundle
                .getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

        for (String result : matches) {

            Log.i(TAG, "onPartialResults: \n" + fullText);
            text = result.toLowerCase();
        }
            /*textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null) ;*/

        new Handler().postDelayed(new Runnable(){
            @Override
            public void run() {
                textAutomation(text);
            }
        }, 600);

        //debouncer.onNext(text);


    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
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
                Log.i(TAG, "VOLLEY onErrorResponse: " + new String(response.data) );
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

    private void createObservables(){
        debouncer = PublishSubject.create() ;
        subscription = debouncer
                .debounce(400, TimeUnit.MILLISECONDS)
                .map(new Func1<String, String>() {
                    @Override
                    public String call(String s) {
                        Log.d(TAG, "call() called with: s = [" + s + "]");
                        textAutomation(s);
                        return "bullshit";
                    }
                })
                .subscribeOn(AndroidSchedulers.mainThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "onNext() called with: s = [" + s + "]");
                    }
                }) ;
    }


    void textAutomation(String text){

        Log.d(TAG, "textAutomation() called with: text = [" + text + "]");
        View view  ;

        if((text.contains("on") || text.contains("onn")) && (text.contains("blink") || (text.contains("blinking"))) ){
            customRVAdapter.toggleChange(2, true);
            notifyOnServer(2, true);
            textToSpeech.speak("Okay your highness, Turning the blinking street lights on. Glad to serve you.",TextToSpeech.QUEUE_FLUSH, null ) ;

            view = recyclerViewGrid.getChildAt(2) ;
            if(view!= null){
                Log.d(TAG, "onPartialResults: view not null");
                Switch switchButton = (Switch) view.findViewById(R.id.list_item_switch_) ;
                switchButton.setChecked(true);
            }

            speechRecognizer.stopListening();
            isOn = !isOn ;

        }else if(text.contains("off")){
            customRVAdapter.toggleChange(1, false);
            notifyOnServer(1, false);
            view = recyclerViewGrid.getChildAt(1) ;
            if(view!= null){
                Switch switchButton = (Switch) view.findViewById(R.id.list_item_switch_) ;
                switchButton.setChecked(false);
            }
            textToSpeech.speak("Okay Sir, Turning the street lights off",TextToSpeech.QUEUE_FLUSH, null ) ;
            speechRecognizer.stopListening();
            isOn = !isOn ;

        }else if (text.contains("on")){
            customRVAdapter.toggleChange(1, true);
            notifyOnServer(1, true);
            view = recyclerViewGrid.getChildAt(1) ;

            if(view!= null){
                Switch switchButton = (Switch) view.findViewById(R.id.list_item_switch_) ;
                switchButton.setChecked(true);
            }
            textToSpeech.speak("Okay Sir, Turning the stable street lights on",TextToSpeech.QUEUE_FLUSH, null ) ;
            speechRecognizer.stopListening();
            isOn = !isOn ;
        }else {
            if(text.length() > 0){
                Toast.makeText(this, "Invalid Command", Toast.LENGTH_SHORT).show();
                speechRecognizer.stopListening();
            }
        }
    }
}
