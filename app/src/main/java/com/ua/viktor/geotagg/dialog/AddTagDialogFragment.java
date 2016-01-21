package com.ua.viktor.geotagg.dialog;

import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.ua.viktor.geotagg.R;
import com.ua.viktor.geotagg.data.TagsContract;
import com.ua.viktor.geotagg.utils.Constants;
import com.ua.viktor.geotagg.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by viktor on 17.01.16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class AddTagDialogFragment extends DialogFragment {
    private static final String TAG = AddTagDialogFragment.class.getName();

    private EditText mEditTextListName;
    private EditText mEditTextUrlImage;
    private ImageButton mAddInmageButton;
    private View rootView;
    private Bitmap mBitmap;
    private ImageView mImageView;
    private String imageToUploadUri;
    private Double mLongitude;
    private Double mLatitude;
    private String mPhotoPath;


    private static final int PICK_IMAGE_REQUEST = 1;
    private static final int EXTERNAL_STORAGE = 200;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLongitude = getArguments().getDouble(Constants.KEY_LONGTITUDE);
        mLatitude = getArguments().getDouble(Constants.KEY_LATITUDE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == EXTERNAL_STORAGE) {

        } else {

        }
    }

    public static AddTagDialogFragment newInstance(double longitude, double latitude) {
        AddTagDialogFragment addListDialogFragment = new AddTagDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.KEY_LONGTITUDE, longitude);
        bundle.putDouble(Constants.KEY_LATITUDE, latitude);
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
        builder.setPositiveButton(R.string.positive_button_create, null);


        LayoutInflater inflater = getActivity().getLayoutInflater();
        rootView = inflater.inflate(R.layout.dialog_add_item, null);

        mEditTextListName = (EditText) rootView.findViewById(R.id.edit_text_list_name);
        mAddInmageButton = (ImageButton) rootView.findViewById(R.id.add_imageButton);
        mImageView = (ImageView) rootView.findViewById(R.id.photo);
        mEditTextUrlImage = (EditText) rootView.findViewById(R.id.edit_text_list_url);


        mEditTextUrlImage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String ImageUrl = s.toString();
                Picasso.with(getActivity()).load(ImageUrl).into(mImageView);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        mAddInmageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] perms = {"android.permission.READ_EXTERNAL_STORAGE"};

                int permsRequestCode = 200;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(perms, permsRequestCode);
                }
                gallery();

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


        final String photoUrl = mEditTextUrlImage.getText().toString();
        String tagName=mEditTextListName.getText().toString();

        boolean validName=isNameValid(tagName);
        boolean validImageUrl=isUrlValid(photoUrl);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String filePath = sp.getString(Constants.KEY_FILE_PATH, null);
        String fileName = sp.getString(Constants.KEY_FILE_NAME, null);

        if (filePath != null || fileName != null) {
            try {
                mPhotoPath = saveToInternalSorage(filePath, fileName);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else if (!photoUrl.equals("") || filePath == null || fileName == null||validImageUrl==true) {
            mPhotoPath = savaImageFromUrl(photoUrl);
        }
        if (mLatitude != null || mLongitude != null||mPhotoPath!=null) {
            ContentValues values = new ContentValues();
            values.put(TagsContract.TagEntry.COLUMN_ICON, mPhotoPath);
            values.put(TagsContract.TagEntry.COLUMN_DESCRIPTION, "lolka");
            values.put(TagsContract.TagEntry.COLUMN_COORD_LAT, mLatitude);
            values.put(TagsContract.TagEntry.COLUMN_COORD_LONG, mLongitude);
            values.put(TagsContract.TagEntry.COLUMN_DATE, Utils.getDateTime());

            getActivity().getContentResolver().insert(TagsContract.TagEntry.CONTENT_URI, values);
            Toast.makeText(getActivity(), "Sucsses", Toast.LENGTH_SHORT).show();
            AddTagDialogFragment.this.getDialog().cancel();
        }
    }

    private boolean isNameValid(String name) {
        if (name.equals("")) {
           // mEditTextListName.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    private boolean isUrlValid(String photoUrl) {
        boolean isUrl = Patterns.WEB_URL.matcher(photoUrl).matches();
        if (isUrl != true) {
            //mEditTextUrlImage.setError(getResources().getString(R.string.error_cannot_be_empty));
            return false;
        }
        return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
        imageToUploadUri = sp.getString(Constants.KEY_FILE_PATH, null);
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
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);

            String picturePath = cursor.getString(columnIndex);
            String pictureName = Uri.parse(cursor.getString(column_index)).getLastPathSegment().toString();

            cursor.close();

            SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getActivity());
            SharedPreferences.Editor spe = sp.edit();
            spe.putString(Constants.KEY_FILE_PATH, picturePath).apply();
            spe.putString(Constants.KEY_FILE_NAME, pictureName).apply();


            File file = new File(picturePath);
            Picasso.with(getActivity()).load(file).into(mImageView);


            Log.v(TAG, "fileName: " + pictureName);
            Log.v(TAG, "filePath: " + picturePath);
        } else {
            Log.i(TAG, "resultCode: " + resultCode);
        }

    }


    public String saveToInternalSorage(final String picturePath, final String pictureName) throws FileNotFoundException {
        File file=new File(picturePath);
        ContextWrapper cw = new ContextWrapper(getActivity());
        final File directory = cw.getDir(Constants.NAME_DIRECTORY, Context.MODE_PRIVATE);

        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(file));
        // Create imageDir
        File mypath=new File(directory,pictureName);

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            // Use the compress method on the BitMap object to write image to the OutputStream
            b.compress(Bitmap.CompressFormat.JPEG, 80, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return directory.getAbsolutePath()+"/"+pictureName;
    }


    public String savaImageFromUrl(String imageUrl) {
        final String imageName = imageUrl.substring(imageUrl.lastIndexOf("/") + 1, imageUrl.length());

        ContextWrapper cw = new ContextWrapper(getActivity());

        final File directory = cw.getDir(Constants.NAME_DIRECTORY, Context.MODE_PRIVATE);
        Picasso.with(getActivity()).load(imageUrl).into(new Target() {

            @Override
            public void onBitmapLoaded(final Bitmap bitmap, Picasso.LoadedFrom from) {
                new Thread(new Runnable() {

                    @Override
                    public void run() {


                        // Create imageDir
                        File file = new File(directory, imageName);
                        try {
                            file.createNewFile();
                            FileOutputStream ostream = new FileOutputStream(file);
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, ostream);
                            ostream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }).start();

            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
                if (placeHolderDrawable != null) {
                }
            }
        });
        return directory.getAbsolutePath() + "/" + imageName;
    }


}
