package com.sakubs.wordbank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnDragListener,
        View.OnLongClickListener {

    /* The draggable words in the word bank. */
    protected String[] wordBankList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources resources = getResources();
        wordBankList = resources.getStringArray(R.array.word_bank);

        TextView word1 = (TextView) findViewById(R.id.word1);

        word1.setText(wordBankList[0]);
        word1.setTag("word 1");
        word1.setOnLongClickListener(this);

        findViewById(R.id.word_bank).setOnDragListener(this);
        findViewById(R.id.word1_slot).setOnDragListener(this);
        findViewById(R.id.word2_slot).setOnDragListener(this);
    }

    @Override
    public boolean onDrag(View view, DragEvent dragEvent) {
        switch (dragEvent.getAction()) {
            case DragEvent.ACTION_DRAG_STARTED:
                // Determines if this View can accept the dragged data
                if (dragEvent.getClipDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN)) {
                    return true;
                }
                return false;

            case DragEvent.ACTION_DRAG_ENTERED:
                // Applies a GRAY color tint to the View. Return true; the return value is ignored.
                view.getBackground().setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
                view.invalidate();
                return true;

            case DragEvent.ACTION_DRAG_LOCATION:
                // Ignore the event
                return true;

            case DragEvent.ACTION_DRAG_EXITED:
                view.getBackground().clearColorFilter();
                view.invalidate();
                return true;

            case DragEvent.ACTION_DROP:
                ClipData.Item item = dragEvent.getClipData().getItemAt(0);
                String dragData = item.getText().toString();

                Toast.makeText(this, "Dragged data is " + dragData, Toast.LENGTH_SHORT).show();
                view.getBackground().clearColorFilter();
                view.invalidate();

                View vw = (View) dragEvent.getLocalState();
                ViewGroup owner = (ViewGroup) vw.getParent();
                owner.removeView(vw);

                LinearLayout container = (LinearLayout) view;
                container.addView(vw);
                vw.setVisibility(View.VISIBLE);

                return true;

            case DragEvent.ACTION_DRAG_ENDED:
                view.getBackground().clearColorFilter();
                view.invalidate();

                if (dragEvent.getResult())
                    Toast.makeText(this, "The drop was handled.", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "The drop didn't work.", Toast.LENGTH_SHORT).show();
                return true;

            default:
                Log.e("DragDrop Example", "Unknown action type received by OnDragListener.");
                break;
        }
        return false;
    }

    @Override
    public boolean onLongClick(View view) {
        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());
        /* Create a new ClipData using the tag as a label, the plain text MIME type, and
         * the already-created item. This will create a new ClipDescription object within the
         * ClipData, and set its MIME type entry to "text/plain"
         */
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};
        ClipData clipData = new ClipData(view.getTag().toString(), mimeTypes, item);

        View.DragShadowBuilder dragshadow = new View.DragShadowBuilder(view);

        view.startDrag(clipData, dragshadow, view, 0);

        return true;
    }
}
