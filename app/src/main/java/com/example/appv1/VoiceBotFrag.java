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
import androidx.fragment.app.Fragment;

import com.google.android.material.chip.Chip;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

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
    private TextView quantityValue;
    private TextView monthValue;
    private Chip chip1;

    private Chip chip2;

    private Chip chip3;
    private Button startButton;
    private TextToSpeech textToSpeech;
    private int totalQuestions = 4;
    private int questionsAsked = 0;
    private List<String> responses = new ArrayList<>(Collections.nCopies(totalQuestions, ""));

    private String[] hindiQuestions = {
            "पहला प्रश्न है: प्रकार चुनें: बिहान लाख, कीटनाशक, नायलों का बैग, में से कोई एक चुनिए ?",
            "दूसरा प्रश्न: ब्रांड चुनें: ब्रांड का नाम क्या है?",
            "तीसरा प्रश्न: मात्रा चुनें: कितने मात्रा में चाहिए?",
            "चौथा प्रश्न: महीना चुनें: किस महीने में चाहिए?"
    };

    // Your other variables, methods, and UI elements should be here.
    private String[] AcceptedInputs = {"बिहान लाख", "कीटनाशक", "नायलों का बैग"};

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
        nextQuestionButton.setOnClickListener(this::askNextQuestion);
        prevQuestionButton.setOnClickListener(this::askPrevQuestion);


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
                                        highlightMatchingChip(recognizedText);

                                    }

                                    else if(questionsAsked == 1) {

                                        brandValue.setText(recognizedText);
                                    }
                                    else if(questionsAsked == 2) {
                                        quantityValue.setText(recognizedText);
                                    }
                                    else if(questionsAsked == 3 ) {
                                        monthValue.setText(recognizedText);
                                    }
                                    responses.set(questionsAsked, recognizedText);
                                    questionsAsked++;




                                }
//
                            }
                        }
                    }
                }
        );

        return view;
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

        int closestMatchIndex = -1;
        int minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < AcceptedInputs.length; i++) {
            String acceptedInput = AcceptedInputs[i];
            int distance = calculateLevenshteinDistance(recognizedText, acceptedInput);

            if (distance < minDistance) {
                closestMatchIndex = i;
                minDistance = distance;
            }
        }
        // Map the indices to the Chip variables
        Chip selectedChip = null;
        if (closestMatchIndex == 0) {
            selectedChip = chip1;
        } else if (closestMatchIndex == 1) {
            selectedChip = chip2;
        } else if (closestMatchIndex == 2) {
            selectedChip = chip3;
        }


        Log.d("Debug",String.valueOf(closestMatchIndex));

        if(selectedChip != null) {
            selectedChip.setChipBackgroundColorResource(R.color.green);
        }




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
        startButton.setVisibility(View.GONE);
        micImageView.setVisibility(View.VISIBLE);
        prevQuestionButton.setVisibility(View.VISIBLE);
        nextQuestionButton.setVisibility(View.VISIBLE);
        displayQuestion(0);
        askNextQuestion(null);

    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = textToSpeech.setLanguage(Locale.forLanguageTag("hi-IN"));
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                // Handle the case where Hindi language is not available or not supported
            } else {
                // Successfully initialized and set the language
                // Speak the first question when the fragment starts
                speakQuestion("कृपया जारी रखने के लिए स्टार्ट बटन दबाएं");
            }
        } else {
            // Handle TTS initialization error
        }
    }
    private void speakQuestion(String question) {
        if (textToSpeech != null) {
            textToSpeech.speak(question, TextToSpeech.QUEUE_FLUSH, null, null);
        }
    }
    public void askNextQuestion(View view) {
        if(questionsAsked < totalQuestions) {
            int progress = ((questionsAsked+1) * 100) / totalQuestions;
            progressBar.setProgress(progress);
            displayQuestion(questionsAsked);


            // Speak the next question in Hindi
            if (questionsAsked < hindiQuestions.length) {
                speakQuestion(hindiQuestions[questionsAsked]);

            }
            Log.d("Debug", String.valueOf(questionsAsked));
        }
        else {
            prevQuestionButton.setVisibility(View.GONE);
            nextQuestionButton.setVisibility(View.GONE);
            questionDisplay.setText("दर्ज हो गए");
            micImageView.setVisibility(View.GONE);

            speakQuestion("धन्यवाद! आपके उत्तर दर्ज हो गए");
            Log.d("Result", responses.toString());

        }


    }

    public void askPrevQuestion(View view) {
        if(questionsAsked > 0) {
            questionsAsked--;
            int progress = ((questionsAsked+1) * 100) / totalQuestions;
            progressBar.setProgress(progress);
            displayQuestion(questionsAsked);
            // Speak the next question in Hindi
            if (questionsAsked < hindiQuestions.length) {
                speakQuestion(hindiQuestions[questionsAsked]);
            }
        }
    }
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
