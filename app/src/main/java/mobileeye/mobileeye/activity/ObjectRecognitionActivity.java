
        package mobileeye.mobileeye.activity;

        import android.Manifest;
        import android.accounts.Account;
        import android.accounts.AccountManager;
        import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.graphics.Bitmap;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.os.Handler;
        import android.provider.MediaStore;
        import android.support.annotation.NonNull;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.util.Log;
        import android.view.View;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.android.gms.auth.GoogleAuthUtil;
        import com.google.android.gms.common.AccountPicker;
        import com.google.api.client.extensions.android.http.AndroidHttp;
        import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
        import com.google.api.client.googleapis.json.GoogleJsonResponseException;
        import com.google.api.client.http.HttpTransport;
        import com.google.api.client.json.JsonFactory;
        import com.google.api.client.json.gson.GsonFactory;
        import com.google.api.services.vision.v1.Vision;
        import com.google.api.services.vision.v1.model.AnnotateImageRequest;
        import com.google.api.services.vision.v1.model.BatchAnnotateImagesRequest;
        import com.google.api.services.vision.v1.model.BatchAnnotateImagesResponse;
        import com.google.api.services.vision.v1.model.EntityAnnotation;
        import com.google.api.services.vision.v1.model.Feature;
        import com.google.api.services.vision.v1.model.Image;

        import java.io.ByteArrayOutputStream;
        import java.io.IOException;
        import java.util.ArrayList;
        import java.util.List;
        import java.util.Locale;

        import mobileeye.mobileeye.GetTokenTask;
        import mobileeye.mobileeye.MenuReader;
        import mobileeye.mobileeye.R;

/**
 * Created by izabelawojciak on 06.11.2017.
 */

public class ObjectRecognitionActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int REQUEST_MAKE_PHOTO = 2;
    MenuReader menuReader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_object_recognition);
        resultTextView = (TextView) findViewById(R.id.result);

        resultTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(ObjectRecognitionActivity.this,
                        new String[]{Manifest.permission.GET_ACCOUNTS},
                        REQUEST_PERMISSIONS);

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        menuReader.read(getString(R.string.newPhoto));
                    }
                }, 500);
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        menuReader = new MenuReader(this);
        resultTextView.setText(getString(R.string.makePhoto));
        setViewText(getString(R.string.makePhoto));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            setViewText(getString(R.string.serwerResponse));

            try {
                callCloudVision(imageBitmap);
            } catch (IOException e) {
                Log.e(LOG_TAG, e.getMessage());
                e.printStackTrace();
            }
        }
        else if (requestCode == REQUEST_CODE_PICK_ACCOUNT) {
            if (resultCode == RESULT_OK) {
                String email = data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                AccountManager am = AccountManager.get(this);
                Account[] accounts = am.getAccountsByType(GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE);
                for (Account account : accounts) {
                    if (account.name.equals(email)) {
                        mAccount = account;
                        break;
                    }
                }
                getAuthToken();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "No Account Selected", Toast.LENGTH_SHORT)
                        .show();
            }
        }
        else if (requestCode == REQUEST_ACCOUNT_AUTHORIZATION) {
            if (resultCode == RESULT_OK) {
                Bundle extra = data.getExtras();
                onTokenReceived(extra.getString("authtoken"));
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Authorization Failed", Toast.LENGTH_SHORT)
                        .show();
            }
        }

//        if (requestCode == REQUEST_MAKE_PHOTO && resultCode == RESULT_OK) {
//            Toast.makeText(this, "Zapisano zdjecie make PHOTO", Toast.LENGTH_SHORT).show();
//            Bundle extras = data.getExtras();
//            byte[] image = extras.getByteArray("image_arr");
//            Bitmap bmp = BitmapFactory.decodeByteArray(image, 0,
//                    image.length);
//
//            ImageView photo = (ImageView) findViewById(R.id.image);
//            photo.setImageBitmap(bmp);
//        }
    }

    private void setViewText(final String text) {

        resultTextView.setText(text);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                menuReader.read(text);
            }
        }, 500);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void makePhoto() {
        Intent cameraCaptureIntent = new Intent(this, Camera_capture.class);
        startActivityForResult(cameraCaptureIntent, REQUEST_MAKE_PHOTO);
    }

    //************************************************************************************************

    private static String accessToken;
    static final int REQUEST_GALLERY_IMAGE = 10;
    static final int REQUEST_CODE_PICK_ACCOUNT = 11;
    static final int REQUEST_ACCOUNT_AUTHORIZATION = 12;
    static final int REQUEST_PERMISSIONS = 13;
    private final String LOG_TAG = "MainActivity";
    private TextView resultTextView;
    Account mAccount;
    ArrayList<EntityAnnotation> lista;
    ArrayList<EntityAnnotation> listaTekst;


    // PROSBA O DOSTEP DO API
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PERMISSIONS:
                if (grantResults.length > 0 &&
                        grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getAuthToken();
                }
                else {
                    Toast.makeText(ObjectRecognitionActivity.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    // WYSYLANIE DO GOOGLE
    private void callCloudVision(final Bitmap bitmap) throws IOException {

        new AsyncTask<Object, Void, String>() {
            @Override
            protected String doInBackground(Object... params) {
                try {
                    GoogleCredential credential = new GoogleCredential().setAccessToken(accessToken);
                    HttpTransport httpTransport = AndroidHttp.newCompatibleTransport();
                    JsonFactory jsonFactory = GsonFactory.getDefaultInstance();

                    Vision.Builder builder = new Vision.Builder
                            (httpTransport, jsonFactory, credential);
                    Vision vision = builder.build();

                    List<Feature> featureList = new ArrayList<>();
                    Feature labelDetection = new Feature();
                    labelDetection.setType("LABEL_DETECTION");
                    labelDetection.setMaxResults(10);
                    featureList.add(labelDetection);

                    Feature textDetection = new Feature();
                    textDetection.setType("TEXT_DETECTION");
                    textDetection.setMaxResults(10);
                    featureList.add(textDetection);

//                    Feature landmarkDetection = new Feature();
//                    landmarkDetection.setType("LANDMARK_DETECTION");
//                    landmarkDetection.setMaxResults(10);
//                    featureList.add(landmarkDetection);

                    List<AnnotateImageRequest> imageList = new ArrayList<>();
                    AnnotateImageRequest annotateImageRequest = new AnnotateImageRequest();
                    Image base64EncodedImage = getBase64EncodedJpeg(bitmap);
                    annotateImageRequest.setImage(base64EncodedImage);
                    annotateImageRequest.setFeatures(featureList);
                    imageList.add(annotateImageRequest);

                    BatchAnnotateImagesRequest batchAnnotateImagesRequest =
                            new BatchAnnotateImagesRequest();
                    batchAnnotateImagesRequest.setRequests(imageList);

                    Vision.Images.Annotate annotateRequest =
                            vision.images().annotate(batchAnnotateImagesRequest);
                    // Due to a bug: requests to Vision API containing large images fail when GZipped.
                    annotateRequest.setDisableGZipContent(true);
                    Log.d(LOG_TAG, "sending request");

                    BatchAnnotateImagesResponse response = annotateRequest.execute();

                    lista = (ArrayList<EntityAnnotation>) response.getResponses().get(0).getLabelAnnotations();
                    listaTekst = (ArrayList<EntityAnnotation>) response.getResponses().get(0).getTextAnnotations();

                    return convertResponseToString(response);

                } catch (GoogleJsonResponseException e) {
                    Log.e(LOG_TAG, "Request failed: " + e.getContent());
                } catch (IOException e) {
                    Log.d(LOG_TAG, "Request failed: " + e.getMessage());
                }
                return "Cloud Vision API request failed.";
            }

            protected void onPostExecute(String result) {
                String tekst = "";

                if(listaTekst == null && lista == null) {
                    tekst = getString(R.string.badConnection);
                }
                else if(lista != null) {
                    tekst = "Zeskanowano: ";

                    for(int i = 0; i < 2; i++) {
                        tekst += lista.get(i).getDescription() + "\n";
                    }
                }

                if(listaTekst != null) {
                    tekst += "Rozpoznano tekst: ";
                    tekst += listaTekst.get(0).getDescription() + "\n";
                }

                tekst += "\nKliknij, by ponowić skanowanie.";

                setViewText(tekst);
            }
        }.execute();
    }

    //ZAMIANA ODPOWIEDZI GOOGLE
    private String convertResponseToString(BatchAnnotateImagesResponse response) {
        StringBuilder message = new StringBuilder("Results:\n\n");
        message.append("Labels:\n");
        List<EntityAnnotation> labels = response.getResponses().get(0).getLabelAnnotations();
        if (labels != null) {
            for (EntityAnnotation label : labels) {
                message.append(String.format(Locale.getDefault(), "%.3f: %s",
                        label.getScore(), label.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing\n");
        }

        message.append("Texts:\n");
        List<EntityAnnotation> texts = response.getResponses().get(0)
                .getTextAnnotations();
        if (texts != null) {
            for (EntityAnnotation text : texts) {
                message.append(String.format(Locale.getDefault(), "%s: %s",
                        text.getLocale(), text.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing\n");
        }

        message.append("Landmarks:\n");
        List<EntityAnnotation> landmarks = response.getResponses().get(0)
                .getLandmarkAnnotations();
        if (landmarks != null) {
            for (EntityAnnotation landmark : landmarks) {
                message.append(String.format(Locale.getDefault(), "%.3f: %s",
                        landmark.getScore(), landmark.getDescription()));
                message.append("\n");
            }
        } else {
            message.append("nothing\n");
        }

        return message.toString();
    }


    // ZMIANA ROZMIARU BITMAPY
    public Bitmap resizeBitmap(Bitmap bitmap) {

        int maxDimension = 1024;
        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }


    // KODOWANIE BITMAPY
    public Image getBase64EncodedJpeg(Bitmap bitmap) {
        Image image = new Image();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        image.encodeContent(imageBytes);
        return image;
    }

    // WYBIERANIE UZYTKOWNIKA
    private void pickUserAccount() {
        String[] accountTypes = new String[]{GoogleAuthUtil.GOOGLE_ACCOUNT_TYPE};
        Intent intent = AccountPicker.newChooseAccountIntent(null, null, accountTypes, false, null, null, null, null);
        startActivityForResult(intent, REQUEST_CODE_PICK_ACCOUNT);
    }

    // PROSBA O TOKEN
    private void getAuthToken() {
        String SCOPE = "oauth2:https://www.googleapis.com/auth/cloud-platform";
        if (mAccount == null) {
            pickUserAccount();
        }
        else {
            new GetTokenTask(ObjectRecognitionActivity.this, mAccount, SCOPE, REQUEST_ACCOUNT_AUTHORIZATION).execute();
        }
    }

    // GDY TOKEN ODEBRANY
    public void onTokenReceived(String token){
        accessToken = token;
        dispatchTakePictureIntent();
    }
}