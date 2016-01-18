package com.ua.viktor.geotagg.dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

import com.ua.viktor.geotagg.R;

/**
 * Created by viktor on 17.01.16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class RemoveTagDialogFragment extends DialogFragment {
    String mListId;
    final static String LOG_TAG = RemoveTagDialogFragment.class.getSimpleName();

    /**
     * Public static constructor that creates fragment and passes a bundle with data into it when adapter is created
     */
    public static RemoveTagDialogFragment newInstance(String id) {
        RemoveTagDialogFragment removeListDialogFragment = new RemoveTagDialogFragment();
        Bundle bundle = new Bundle();
       // bundle.putString(Constants.KEY_LIST_ID, listId);
        removeListDialogFragment.setArguments(bundle);
        return removeListDialogFragment;
    }

    /**
     * Initialize instance variables with data from bundle
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //mListId = getArguments().getString(Constants.KEY_LIST_ID);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog)
                .setTitle(getActivity().getResources().getString(R.string.action_remove_list))
                .setMessage(getString(R.string.dialog_message_are_you_sure_remove_item))
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        removeList();
                        /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        /* Dismiss the dialog */
                        dialog.dismiss();
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert);

        return builder.create();
    }

    private void removeList() {
    }

}

