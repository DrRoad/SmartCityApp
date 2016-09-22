package in.ac.vit.smartcityapp.View;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.ac.vit.smartcityapp.Controller.CustomRVAdapter;
import in.ac.vit.smartcityapp.Model.Entities.DeviceConfig;
import in.ac.vit.smartcityapp.R;

public class MainActivity extends AppCompatActivity implements RecognitionListener{


    private static final String TAG = "TAG";
    @BindView(R.id.activity_main_recyclerView) RecyclerView recyclerViewGrid ;
    @BindView(R.id.activity_main_record) ImageButton recordButton ;

    private CustomRVAdapter customRVAdapter ;
    List<DeviceConfig> deviceConfigList ;

    private SpeechRecognizer speechRecognizer = null ;
    private Intent recognizerIntent;

    private boolean isOn = false ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this) ;
        init() ;

    }

    private void init() {
        addDataToArray() ;
        customRVAdapter = new CustomRVAdapter(this, deviceConfigList) ;
        recyclerViewGrid.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
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


    }

    private void addDataToArray() {
        deviceConfigList = new ArrayList<>() ;
        deviceConfigList.add(new DeviceConfig(1, "Fridge", true, "temp: 16deg")) ;
        deviceConfigList.add(new DeviceConfig(1, "Washing Machine", false, "offline")) ;
        deviceConfigList.add(new DeviceConfig(1, "Television", false, "last: 1 hour")) ;
        deviceConfigList.add(new DeviceConfig(1, "Main Lights", true, "220W")) ;
        deviceConfigList.add(new DeviceConfig(1, "Fridge", true, "temp: 16deg")) ;
        for (int i = 0; i < 12; i++) {
            deviceConfigList.add(new DeviceConfig(1, "Fridge", true, "temp: 16deg")) ;
        }
    }

    @OnClick(R.id.activity_main_record)
    void startRecording(){
        if(!isOn)
            speechRecognizer.startListening(recognizerIntent);
        else
            speechRecognizer.stopListening();

        isOn = !isOn ;

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
        Log.d(TAG, "onBufferReceived() called with: bytes = [" + bytes + "]");
    }

    @Override
    public void onEndOfSpeech() {

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

        for (String result : matches)
            Log.i(TAG, "onPartialResults: " + result);
    }

    @Override
    public void onEvent(int i, Bundle bundle) {

    }

    @Override
    protected void onPause() {
        super.onPause();

        speechRecognizer.destroy();
    }
}
