package pl.it_developers.geoquiz;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class QuizActivity extends AppCompatActivity {
    private static final String TAG = "QuizActivity";
    private static final String KEY_INDEX = "question_index";
    private static final String KEY_ANSWERS = "question_answers";
    private static final int CORRECT_ANSWER = 1;
    private static final int INCORRECT_ANSWER = 1;
    private static final int NO_ANSWER = 0;

    private Button trueButton;
    private Button falseButton;
    private ImageButton nextButton;
    private ImageButton prevButton;
    private TextView questionTextView;

    private Question[] questions = new Question[] {
        new Question(R.string.question_australia, true),
        new Question(R.string.question_oceans, true),
        new Question(R.string.question_mideast, false),
        new Question(R.string.question_africa, false),
        new Question(R.string.question_americas, true),
        new Question(R.string.question_asia, true),
    };

    //0 - brak odpowiedzi (domyślnie wypełnione), 1 dobra odpowiedz, -1 zła odpowiedz
    private int[] answers;

    private int currentIndex = 0;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(TAG, "onSaveInstanceState");
        outState.putInt(KEY_INDEX, currentIndex);
        outState.putIntArray(KEY_ANSWERS, answers);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");


        setContentView(R.layout.activity_quiz);

        answers = new int[questions.length];

        if (savedInstanceState != null) {
            currentIndex = savedInstanceState.getInt(KEY_INDEX);
            answers = savedInstanceState.getIntArray(KEY_ANSWERS);
        }

        questionTextView = (TextView) findViewById(R.id.question_text_view);
        questionTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % questions.length;
                showQuestion();
            }
        });

        trueButton = (Button) findViewById(R.id.true_button);
        trueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(true);
            }
        });

        falseButton = (Button) findViewById(R.id.false_button);
        falseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(false);
            }
        });

        nextButton = (ImageButton) findViewById(R.id.next_button);
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentIndex = (currentIndex + 1) % questions.length;
                showQuestion();
            }
        });

        prevButton = (ImageButton) findViewById(R.id.prev_button);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                --currentIndex;
                if (currentIndex < 0) {
                    currentIndex = questions.length - 1;
                }
                showQuestion();
            }
        });
        showQuestion();
    }

    private void showQuestion() {
        questionTextView.setText(questions[currentIndex].getTextResId());
        boolean enable = answers[currentIndex] == NO_ANSWER;
        setEnabledAnswerButtons(enable);
    }

    private void checkAnswer(boolean userPressedTrue) {
        boolean answerIsTrue = questions[currentIndex].isAnswerTrue();
        int messageResId = 0;

        if (userPressedTrue == answerIsTrue) {
            answers[currentIndex] = CORRECT_ANSWER;
            messageResId = R.string.correct_toast;
        } else {
            answers[currentIndex] = INCORRECT_ANSWER;
            messageResId = R.string.incorrect_toast;
        }

        setEnabledAnswerButtons(false);

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
        if (isAllAnswersWereGiven()) {
            String text = getResources().getString(R.string.all_answer_were_given_pre) +
                    getResult() +
                    getResources().getString(R.string.all_answer_were_given_post);
            Toast.makeText(this, text, Toast.LENGTH_LONG).show();
        }
    }

    private void setEnabledAnswerButtons(boolean enable) {
        trueButton.setEnabled(enable);
        falseButton.setEnabled(enable);
    }

    private boolean isAllAnswersWereGiven() {
        for (int i: answers) {
            if (i == NO_ANSWER) {
                return false;
            }
        }
        return true;
    }

    private int getResult() {
        int correctAnswers = 0;
        for (int i: answers) {
            if (i == CORRECT_ANSWER) {
                correctAnswers ++;
            }
        }

        return (int)(correctAnswers / answers.length * 100);
    }
}
