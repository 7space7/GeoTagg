package com.ua.viktor.geotagg.dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.ua.viktor.geotagg.R;
import com.ua.viktor.geotagg.utils.Constants;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by viktor on 17.01.16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AddTagDialogFragment extends DialogFragment {
    private static final String TAG = AddTagDialogFragment.class.getName();

    private EditText mEditTextListName;
    private ImageButton mAddInmageButton;
    private View rootView;
    private Bitmap mBitmap;
    private ImageView mImageView;
    private String imageToUploadUri;
    private Double mLongitude;
    private Double mLatitude;


    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int EXTERNAL_STORAGE = 200;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLongitude = getArguments().getDouble(Constants.KEY_LONGTITUDE);
        mLatitude=getArguments().getDouble(Constants.KEY_LATITUDE);
    }


    public static AddTagDialogFragment newInstance(double longitude,double latitude) {
        AddTagDialogFragment addListDialogFragment = new AddTagDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.KEY_LONGTITUDE, longitude);
        bundle.putDouble(Constants.KEY_LATITUDE,latitude);
        addListDialogFragment.setArguments(bundle);
        return addListDialogFragment;
    }

    /**
     * Open the keyboard automatically when the dialog fragment is opened
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.CustomTheme_Dialog);

        LayoutInflater inflater = getActivity().getLayoutInflater();
        rootView = inflater.inflate(R.layout.dialog_add_item, null);
        mEditTextListName = (EditText) rootView.findViewById(R.id.edit_text_list_name);
        mAddInmageButton = (ImageButton) rootView.findViewById(R.id.add_imageButton);
        mImageView = (ImageView) rootView.findViewById(R.id.photo);

        mAddInmageButton.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};

                int permsRequestCode = 200;

                requestPermissions(perms, permsRequestCode);
                gallery();

            }
        });


        mEditTextListName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    addTagList();
                }
                return true;
            }
        });


        builder.setView(rootView)
                .setPositiveButton(R.string.positive_button_create, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        addTagList();
                    }
                });

        return builder.create();
    }


    public void addTagList() {

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        imageToUploadUri = sp.getString("file_uri", null);
        if (imageToUploadUri != null) {
            Log.v("lol", imageToUploadUri);
        }
    }

    public void gallery() {
        Intent i = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == getActivity().RESULT_OK
                && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                    filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
//+
            String fileName = Uri.parse(cursor.getString(column_index)).getLastPathSegment().toString();

            cursor.close();


            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor spe = sp.edit();
            spe.putString("file_uri", picturePath).apply();


            mBitmap = BitmapFactory.decodeFile(picturePath);
            mImageView.setImageBitmap(mBitmap);

            Log.v(TAG, "resultCode: " + fileName);
        } else {
            Log.i(TAG, "resultCode: " + resultCode);
        }

    }

    public String saveToInternalSorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getActivity());
        // path to /data/data/yourapp/app_data/imageDir
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        // Create imageDir
        File mypath = new File(directory, "profile.jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE) {

        }
    }

}
