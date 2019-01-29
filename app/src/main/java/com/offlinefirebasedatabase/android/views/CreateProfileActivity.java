package com.offlinefirebasedatabase.android.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.offlinefirebasedatabase.android.models.Profile;
import com.offlinefirebasedatabase.android.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class CreateProfileActivity extends AppCompatActivity {

    private EditText idET, nameET, emailET, numberET;
    private Button mProfileBtn;
    private DatabaseReference mDatabaseReference;
    private String id, name, email, number, imagePath;
    private ImageView takePicIV;
    private CircleImageView imageview;

    private static final String IMAGE_DIRECTORY = "/offlineDatabaseImage";
    private int GALLERY = 1, CAMERA = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference("Profile");

        idET = (EditText) findViewById(R.id.id_et);
        nameET = (EditText) findViewById(R.id.name_et);
        emailET = (EditText) findViewById(R.id.email_et);
        numberET = (EditText) findViewById(R.id.number_et);
        takePicIV = (ImageView) findViewById(R.id.takePic_iv);
        imageview = (CircleImageView) findViewById(R.id.img_profile);

        mProfileBtn = (Button) findViewById(R.id.createProfileBtn);

        initViews();
    }

    private void initViews() {

        mProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                id = idET.getText().toString();
                name = nameET.getText().toString();
                email = emailET.getText().toString();
                number = numberET.getText().toString();

                if (TextUtils.isEmpty(id)) {
                    Toast.makeText(CreateProfileActivity.this, "Enter Id", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(name)) {
                    Toast.makeText(CreateProfileActivity.this, "Enter Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(CreateProfileActivity.this, "Enter Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(CreateProfileActivity.this, "Enter Number", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(imagePath) || imagePath.equals("")) {
                    Toast.makeText(CreateProfileActivity.this, "Click Picture", Toast.LENGTH_SHORT).show();
                    return;
                }

                Profile profile = new Profile();
                profile.setId(id);
                profile.setName(name);
                profile.setEmail(email);
                profile.setNumber(number);


                Uri file = Uri.fromFile(new File(imagePath));
                profile.setImage(file.toString());

                mDatabaseReference.child(id).setValue(profile);

                Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();

                Toast.makeText(CreateProfileActivity.this, "Success - Uploaded to Firebase Database", Toast.LENGTH_SHORT).show();

            }
        });


        takePicIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showPictureDialog();

            }
        });
    }

    private void showPictureDialog(){
        AlertDialog.Builder pictureDialog = new AlertDialog.Builder(this);
        pictureDialog.setTitle("Select Action");
        String[] pictureDialogItems = {
                "Select photo from gallery",
                "Capture photo from camera" };
        pictureDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        pictureDialog.show();
    }

    public void choosePhotoFromGallary() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }

    private void takePhotoFromCamera() {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == this.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY) {
            if (data != null) {
                Uri contentURI = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), contentURI);
                    String path = saveImage(bitmap);
                    imagePath = path;
                    Toast.makeText(CreateProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
                    imageview.setImageBitmap(bitmap);

                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(CreateProfileActivity.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        } else if (requestCode == CAMERA) {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            String path = saveImage(thumbnail);
            imagePath = path;
            imageview.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(CreateProfileActivity.this, "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }

        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

}
