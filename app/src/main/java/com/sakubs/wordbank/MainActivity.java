package com.sakubs.wordbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.app.Activity;
import android.content.ClipData;
import android.graphics.Typeface;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private TextView option1, option2, option3, choice1, choice2, choice3;

    /* The draggable words in the word bank. */
    protected String[] wordBankList;
    protected String[] correctAnswers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();
        wordBankList = resources.getStringArray(R.array.word_bank);
        correctAnswers = resources.getStringArray(R.array.correct_answers);

        //views to drag
        option1 = (TextView)findViewById(R.id.option_1);
        option2 = (TextView)findViewById(R.id.option_2);
        option3 = (TextView)findViewById(R.id.option_3);

        //views to drop onto
        choice1 = (TextView)findViewById(R.id.choice_1);
        choice2 = (TextView)findViewById(R.id.choice_2);
        choice3 = (TextView)findViewById(R.id.choice_3);

        // Set answer key

        //set touch listeners
        option1.setOnTouchListener(new ChoiceTouchListener());
        option2.setOnTouchListener(new ChoiceTouchListener());
        option3.setOnTouchListener(new ChoiceTouchListener());

        //set drag listeners
        choice1.setOnDragListener(new ChoiceDragListener());
        choice2.setOnDragListener(new ChoiceDragListener());
        choice3.setOnDragListener(new ChoiceDragListener());

        // Set values
        option1.setText(wordBankList[0]);
        option2.setText(wordBankList[1]);
        option3.setText(wordBankList[2]);
        choice1.setText(getString(R.string.sentence1, "____"));
        choice2.setText(getString(R.string.sentence2, "____"));
        choice3.setText(getString(R.string.sentence3, "____"));
    }

    private final class ChoiceTouchListener implements OnTouchListener {

        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                // Set up the drag
                ClipData data = ClipData.newPlainText("", "");
                DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

                // Start dragging the item touched
                view.startDrag(data, shadowBuilder, view, 0);
                return true;
            }
            return false;
        }
    }

    private class ChoiceDragListener implements OnDragListener {

        @Override
        public boolean onDrag(View view, DragEvent dragEvent) {
            switch (dragEvent.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    //no action necessary
                    break;

                case DragEvent.ACTION_DRAG_ENTERED:
                    //no action necessary
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    //no action necessary
                    break;

                case DragEvent.ACTION_DROP:
                    //handle the dragged view being dropped over a drop view
                    View dropView = (View) dragEvent.getLocalState();

                    //stop displaying the view where it was before it was dragged
                    dropView.setVisibility(View.INVISIBLE);

                    //view dragged item is being dropped on
                    TextView dropTarget = (TextView) view;

                    //view being dragged and dropped
                    TextView dropped = (TextView) dropView;

                    //update the text in the target view to reflect the data being dropped
                    String word = dropped.getText().toString();
                    String sentence = dropTarget.getText().toString().replace("____", word);

                    boolean isCorrect = false;

                    for (int i = 0; i < correctAnswers.length; i++){
                        if (sentence.equals(correctAnswers[i])){
                            isCorrect = true;
                            break;
                        }
                    }

                    if (isCorrect) {
                        dropTarget.setText(sentence);

                        //make it bold to highlight the fact that an item has been dropped
                        dropTarget.setTypeface(Typeface.DEFAULT_BOLD);


                        //if an item has already been dropped here, there will be a tag
                        Object tag = dropTarget.getTag();

                        //if there is already an item here, set it back visible in its original place
                        if (tag != null) {
                            //the tag is the view id already dropped here
                            int existingID = (Integer) tag;
                            //set the original view visible again
                            findViewById(existingID).setVisibility(View.VISIBLE);
                        }

                        //set the tag in the target view to the ID of the view being dropped
                        dropTarget.setTag(dropped.getId());
                    } else {
                        //set the original view visible again.
                        findViewById(dropped.getId()).setVisibility(View.VISIBLE);

                        dropTarget.setText(dropTarget.getText().toString().replace(word, "____"));
                        dropTarget.setTag(null);

                    }

                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    //no action necessary
                    break;
                default:
                    break;
            }
            return true;
        }
    }
}
