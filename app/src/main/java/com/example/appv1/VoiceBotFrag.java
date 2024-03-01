
package com.example.appv1;

import static android.app.Activity.RESULT_OK;

import android.os.Bundle;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.chip.Chip;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class VoiceBotFrag extends Fragment implements TextToSpeech.OnInitListener {
    private ImageView micImageView;
    private TextView questionDisplay;
    private boolean isListening = false;
    private ImageView voicebotImageView;
    private ProgressBar progressBar;
    private Button nextQuestionButton;
    private Button prevQuestionButton;
    private TextView percentageTextView;
    private TextView brandValue;
    private TextView placeholder;


    private TextView quantityValue;
    private TextView monthValue;
    private Chip chip1;

    private Chip chip2;

    private Chip chip3;
    private Button startButton;
    private TextToSpeech textToSpeech;
    private int totalQuestions = 4;
    private int questionsAsked = 0;
    private String requirementType = "";
    private String quantity = "";
    private String brand = "";
    private String month = "";

    private TextView[] allTextView = {placeholder, brandValue, quantityValue, monthValue};

    private String[] hindiQuestions = {
            "पहला प्रश्न है: प्रकार चुनें: ब्रूड लाख, कीटनाशक, नायलों का बैग, में से कोई एक चुनिए ?",
            "दूसरा प्रश्न: ब्रांड चुनें: ब्रांड का नाम क्या है?",
            "तीसरा प्रश्न: मात्रा चुनें: कितने मात्रा में चाहिए?",
            "चौथा प्रश्न: महीना चुनें: किस महीने में चाहिए?"
    };

    // Your other variables, methods, and UI elements should be here.
    private String[] AcceptedInputsFirstQuestion = {"ब्रूड लाख", "कीटनाशक", "नायलों का बैग"};

    private String[] AcceptedInputsFinalQuestion = {"जनवरी", "फरवरी", "मार्च",  "अप्रैल", "मई", "जून", "जुलाई", "अगस्त", "सितंबर", "अक्टूबर", "नवंबर", "दिसंबर"};

    // Use ActivityResultLauncher for voice recognition
    private ActivityResultLauncher<Intent> voiceRecognitionLauncher;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_voice_bot, container, false);

        // Initialize your UI elements and set up click listeners as needed
        // (e.g., nextQuestionButton, prevQuestionButton, micImageView, etc.)
        nextQuestionButton = view.findViewById(R.id.nextButton);
        prevQuestionButton = view.findViewById(R.id.prevButton);
        micImageView = view.findViewById(R.id.micImageIcon);
        questionDisplay = view.findViewById(R.id.questionDisplay);
        startButton = view.findViewById(R.id.startButton);
        progressBar = view.findViewById(R.id.progressBar);

        brandValue = view.findViewById(R.id.brandValue);
        monthValue = view.findViewById(R.id.monthValue);
        quantityValue = view.findViewById(R.id.quantityValue);
        chip1 = view.findViewById(R.id.chip1);
        chip2 = view.findViewById(R.id.chip2);
        chip3 = view.findViewById(R.id.chip3);

        startButton.setOnClickListener(this::onStartClick);
        micImageView.setOnClickListener(this::onMicClick);


        nextQuestionButton.setOnClickListener(this::nullFunction);
        prevQuestionButton.setOnClickListener(this::nullFunction);


        // Initialize the Text-to-Speech engine
        textToSpeech = new TextToSpeech(getContext(), this);

        // Initialize the voice recognition launcher
        voiceRecognitionLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                activityResult -> {
                    if (activityResult.getResultCode() == RESULT_OK) {
                        Intent data = activityResult.getData();
                        if (data != null) {
                            ArrayList<String> recognizedResults = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                            if (recognizedResults != null && recognizedResults.size() > 0) {
                                // Get the recognized text
                                String recognizedText = recognizedResults.get(0);


                                Toast.makeText(requireContext(), recognizedText, Toast.LENGTH_LONG).show();
                                // Save the recognized text to responses
                                if(questionsAsked < totalQuestions) {
                                    Log.d("Debug Questions Asked", String.valueOf(questionsAsked));
                                    if(questionsAsked == 0) {
//                                        validateVoiceInput(recognizedText, placeholder, questionsAsked);
                                        askNextQuestion(view, recognizedText);
                                    }
                                    else if(questionsAsked == 1) {
                                           askNextQuestion(view, recognizedText);
//                                        validateVoiceInput(recognizedText, brandValue, questionsAsked);
                                    }
                                    else if(questionsAsked == 2) {
//                                        validateVoiceInput(recognizedText, quantityValue, questionsAsked);
                                        askNextQuestion(view, recognizedText);
                                    }
                                    else if(questionsAsked == 3 ) {
//                                        validateVoiceInput(recognizedText, monthValue, questionsAsked);
                                        askNextQuestion(view, recognizedText);
                                    }
                                }
                            }
                        }
                    }
                }
        );

        return view;
    }

    private void nullFunction(View view) {
    }

    private void displayQuestion(int questionIndex) {
        // Update UI to display the question at index questionIndex
        String fullQuestion = hindiQuestions[questionIndex];
        int colonIndex = fullQuestion.lastIndexOf(':');

        if (colonIndex != -1 && colonIndex + 1 < fullQuestion.length()) {
            String questionPart = fullQuestion.substring(colonIndex + 1).trim();
            questionDisplay.setText(questionPart);
        } else {
            // If there's no ":" symbol, set the full question
            questionDisplay.setText(fullQuestion);
        }
    }
    private int calculateLevenshteinDistance(String a, String b) {
        int[][] dp = new int[a.length() + 1][b.length() + 1];

        for (int i = 0; i <= a.length(); i++) {
            for (int j = 0; j <= b.length(); j++) {
                if (i == 0) {
                    dp[i][j] = j;
                } else if (j == 0) {
                    dp[i][j] = i;
                } else if (a.charAt(i - 1) == b.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(Math.min(dp[i - 1][j], dp[i][j - 1]), dp[i - 1][j - 1]);
                }
            }
        }

        return dp[a.length()][b.length()];
    }

    private void highlightMatchingChip(String recognizedText) {

    }

    public boolean containsOnlyNumbers(String input) {
        // Define a regex pattern to match only numbers
        String regex = "^[0-9]+$";

        // Use the matches() method to check if the input matches the pattern
        return input.matches(regex);
    }
    private boolean validateVoiceInput(String recognizedText, TextView[] correspondingTextView, int questionNumber, View view) {
        switch (questionNumber) {
            case 0:
                int closestMatchIndex = -1;
                int minDistance = Integer.MAX_VALUE;
                boolean acceptedInputFound = false;

                for (int i = 0; i < AcceptedInputsFirstQuestion.length; i++) {
                    String acceptedInput = AcceptedInputsFirstQuestion[i];
                    int distance = calculateLevenshteinDistance(recognizedText, acceptedInput);

                    if (distance < minDistance && distance <= 5) {
                        closestMatchIndex = i;
                        minDistance = distance;
                        acceptedInputFound = true;
                    }
                }
                Chip selectedChip = null;
                if (acceptedInputFound) {
                    // Map the index to the corresponding Chip variable
                    if (closestMatchIndex == 0) {
                        selectedChip = chip1;
                    } else if (closestMatchIndex == 1) {
                        selectedChip = chip2;
                    } else if (closestMatchIndex == 2) {
                        selectedChip = chip3;
                    }

                    // Check if the selectedChip is not null before setting its background color
                    if (selectedChip != null) {
                        // Update the requirementType variable with the selected chip value
                        requirementType = AcceptedInputsFirstQuestion[closestMatchIndex];

                        selectedChip.setChipBackgroundColorResource(R.color.green);
                        return true;
                    }
                } else {
                    // Handle the case where no accepted input is found within the threshold
                    Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                    textToSpeech.speak("प्रदान किया गया इनपुट अमान्य था, कृपया ब्रूड लाख, कीटनाक या नायलों का बैग चुनें", TextToSpeech.QUEUE_FLUSH, null, null);
                    requirementType = "";
                    // selectedChip might be null here, so avoid calling its methods
                    if (selectedChip != null) {
                        selectedChip.setChipBackgroundColorResource(R.color.grey);
                    }
                    return false;
                }


            case 1:
                brandValue.setText(recognizedText);
                brand = recognizedText;
                return true;

            case 2:
                if (containsOnlyNumbers(recognizedText)) {
                    if(Integer.parseInt(recognizedText) <= 100){
                        quantity = recognizedText;
                        quantityValue.setText(recognizedText);
                        // Check if correspondingTextView is not null before setting its text
                        if (correspondingTextView[2] != null) {
                            correspondingTextView[2].setText(recognizedText);
                        }
                        return true;
                    }else{

                    }
                    textToSpeech.speak("कृपया 100 से कम या उसके बराबर का मान चुनें", TextToSpeech.QUEUE_FLUSH, null, null);
                    return false;
                } else {
                    textToSpeech.speak("आपके इनपुट में केवल संख्याएँ होनी चाहिए", TextToSpeech.QUEUE_FLUSH, null, null);
                    return false;
                }

            case 3:
                closestMatchIndex = -1;
                minDistance = Integer.MAX_VALUE;
                acceptedInputFound = false;

                for (int i = 0; i < AcceptedInputsFinalQuestion.length; i++) {
                    String acceptedInput = AcceptedInputsFinalQuestion[i];
                    int distance = calculateLevenshteinDistance(recognizedText, acceptedInput);

                    if (distance < minDistance && distance <= 2) {
                        closestMatchIndex = i;
                        minDistance = distance;
                        acceptedInputFound = true;
                    }
                }
                if (acceptedInputFound) {
                    Log.v("MONTH", String.valueOf(month));
                    month = AcceptedInputsFinalQuestion[closestMatchIndex];
                    Log.v("MONTH", String.valueOf(month));

                    monthValue.setText(month); // Update the TextView to display the selected month
                    submitForm();

                    return true;
                } else {
                    Toast.makeText(requireContext(), "Invalid input", Toast.LENGTH_SHORT).show();
                    textToSpeech.speak("आपके इनपुट में वर्ष का केवल एक महीना शामिल होना चाहिए", TextToSpeech.QUEUE_FLUSH, null, null);
                    return false;
                }

        }
        return false; // Return false by default if questionNumber doesn't match any case
    }

    public void onMicClick(View view) {
        // Start voice recognition for the current question
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "hi-IN");
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1);
        voiceRecognitionLauncher.launch(intent);
    }
    public void onStartClick(View view) {
//        questionsAsked = 1;
        startButton.setVisibility(View.GONE);
        micImageView.setVisibility(View.VISIBLE);
        displayQuestion(0);
        speakQuestion(hindiQuestions[0]);
//        askNextQuestion(null);

    }

    @Override
    public void onInit(int status) {
        prevQuestionButton.setVisibility(View.INVISIBLE);
        nextQuestionButton.setVisibility(View.INVISIBLE);
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.forLanguageTag("hi-IN"));
//            int result = textToSpeech.setLanguage(new Locale ("hi","IN"));
//            String availableLangs = textToSpeech.getAvailableLanguages();

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle the case where Hindi language is not available or not supported
                speakQuestion("hindi has not been configured for text to speech, please allow the phone to use text to speech in hindi by navigating to settings");
            } else {
                // Successfully initialized and set the language
                // Speak the first question when the fragment starts
                speakQuestion("कृपया जारी रखने के लिए स्टार्ट बटन दबाएं");
            }
        } else {
            // Handle TTS initialization error
            textToSpeech.speak("Text to speech failed to initialize", TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
    private void speakQuestion(String question) {

        if (textToSpeech != null) {
            textToSpeech.speak(question, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }

    public void askNextQuestion(View view, String recognizedText) {

            // Validate the input for the current question
            if (validateVoiceInput(recognizedText, allTextView, questionsAsked, view)) {
                // If input is valid, proceed to the next question
                questionsAsked++;
                progressBar.setProgress((questionsAsked * 100) / totalQuestions);
                if(questionsAsked < 4){

                    displayQuestion(questionsAsked);
                    speakQuestion(hindiQuestions[questionsAsked]);
                }

            }
    }

    private void submitForm() {
        // Proceed with final submission
        questionDisplay.setText("दर्ज हो गए");
        micImageView.setVisibility(View.GONE);

        // Build the JSON request body
        MediaType mediaType = MediaType.parse("application/json");
        String requestBodyJson = String.format(
                "{\n" +
                        "    \"type\":\"%s\",\n" +
                        "    \"quantity\":%s,\n" +
                        "    \"month\":\"%s\",\n" +
                        "    \"Brand\":\"%s\"\n" +
                        "}",
                requirementType, quantity, month, brand
        );
        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create the request with the JSON body
        RequestBody body = RequestBody.create(mediaType, requestBodyJson);
        Request request = new Request.Builder()
                .url(getString(R.string.url) + "/farmer/requirements")
                .post(body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Authorization", "Bearer " + LoginActivity.getToken(getContext()))
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    Log.d("Form Submitted", response.toString());

                    requireActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(requireContext(), "Requirement Submitted Successfully", Toast.LENGTH_LONG).show();
                        }
                    });

                    speakQuestion("धन्यवाद! आपके उत्तर दर्ज हो गए");

                    DashboardFrag dashboardFrag = new DashboardFrag();
                    FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout, dashboardFrag);
                    transaction.addToBackStack(null);
                    transaction.commit();

                } else {
                    Log.d("Failed", "Request Failed");
                    Log.d("Failed", response.toString());
                }
            }

            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
            }
        });
    }

//    public void askPrevQuestion(View view) {
//        if(questionsAsked > 0) {
//            questionsAsked--;
//            int progress = ((questionsAsked+1) * 100) / totalQuestions;
//            progressBar.setProgress(progress);
//            displayQuestion(questionsAsked);
//            // Speak the next question in Hindi
//            if (questionsAsked < hindiQuestions.length) {
//                speakQuestion(hindiQuestions[questionsAsked]);
//            }
//        }
//    }
    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }


    // Your other methods, such as onMicClick, onStartClick, askNextQuestion, askPrevQuestion, etc.
}
