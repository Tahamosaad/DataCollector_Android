package com.saudisoft.mis_android.adapter;

import android.app.DialogFragment;
import android.content.Context;

/**
 * Created by Taha mosaad on {6/10/2018}.
 */

public class DialogAdapter extends DialogFragment {
    interface NoticeDialogListener {
         void onDialogPositiveClick(DialogFragment dialog, String name, String description);
         void onDialogNegativeClick(DialogFragment dialog);
    }

    // Use this instance of the interface to deliver action events
    NoticeDialogListener mListener;

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            mListener = (NoticeDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException( " must implement NoticeDialogListener");
        }
    }
}
