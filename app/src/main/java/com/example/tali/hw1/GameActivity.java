package com.example.tali.hw1;
import android.Manifest;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.os.Handler;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tali.hw1.db.Player;
import com.example.tali.hw1.viewmodel.PlayerViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.*;
import java.util.Arrays;

public class GameActivity extends AppCompatActivity{
    public static final String SCORE = "SCORE";
    private int numOfCubes, maxTime, numOfMatches = 0, sizeOfMatrix;
    private long timeLeft;
    private String name;
    private int numClick = 0, firstClick = -1, secondClick= -1, borderColor;
    private GridView gridview;
    private TextView textViewName, textViewTimer;
    private ImageAdapter imageAdapter;
    private boolean timeIsUp = false;
    private CountDownTimer countDownTimer;
    private Runnable matchRunnable;
    private Handler handler;
    private Random rnd = new Random();
    protected Location mLastLocation;
    private static final String TAG = GameActivity.class.getSimpleName();
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    /**
     * Provides the entry point to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle data = getIntent().getExtras();
        numOfCubes = data.getInt(LevelsActivity.NUM_OF_CUBES);
        maxTime = data.getInt(LevelsActivity.TIME);
        name = data.getString(EntryActivity.NAME);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        bindUI();
        startTimer();
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!checkPermissions()) {
            requestPermissions();
        } else {
            getLastLocation();
        }
    }

    private void bindUI()
    {
        textViewName = findViewById(R.id.textViewName);
        textViewName.setText(name);
        textViewTimer = findViewById(R.id.textViewTimer);
        gridview = findViewById(R.id.gridView);

        // In easy level, choose the number of columns to be 2
        if (numOfCubes == LevelsActivity.EASY_NUM_OF_CUBES){
            gridview.setNumColumns(numOfCubes);
            gridview.requestLayout();
        }

        final Integer[] images = getImages();
        imageAdapter = new ImageAdapter(this, images);
        gridview.setAdapter(imageAdapter);
        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // The image is defaultImage and Timer is still running
                if(!((MemoryImageView)v).getState() && !timeIsUp) {
                    numClick++;
                    MemoryImageView img_view = (MemoryImageView) v;
                    // Show imageView animal picture
                    img_view.setImageResource(img_view.getImageId());
                    img_view.setState(true);

                    if(numClick % 2 != 0) {
                        firstClick = position;
                        // Mark card, each two cards marked with the same color
                        borderColor = rnd.nextInt();
                        setBorderColor(img_view, borderColor);
                    }
                    else {
                        secondClick = position;
                        setBorderColor(img_view, borderColor);
                    }
                    if(firstClick != -1 && secondClick != -1){
                        checkForCouple();
                    }
                }
            }
        });
    }

    private void gameEnd(){
        String result = "";
        if(numOfMatches == sizeOfMatrix / 2) {
            addScore();
            result = getString(R.string.ac_level_win_message);
        }
        Intent resIntent = new Intent();
        resIntent.putExtra(LevelsActivity.RESULT, result);
        setResult(RESULT_OK, resIntent);
        finish();
    }

    private void addScore(){

        double score = sizeOfMatrix * (maxTime - timeLeft);
        PlayerBuilder pb = new PlayerBuilder(this, name, score, mLastLocation);
        new Thread(pb).start();
    }

    private void startTimer()
    {
        countDownTimer = new CountDownTimer(maxTime * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int minutesLeft = (int)(millisUntilFinished/ 1000) / 60;
                int secondsLeft = (int)millisUntilFinished/ 1000;
                textViewTimer.setText(String.format("%d:%02d", minutesLeft , secondsLeft));
                timeLeft = millisUntilFinished / 1000;
            }

            @Override
            public void onFinish() {
                timeIsUp = true;
                textViewTimer.setText(String.format("%d:%02d", 0 , 0));
                // Show game end message
                Toast.makeText(getApplicationContext(), R.string.ac_game_end_message, Toast.LENGTH_LONG).show();
                gameEnd();
            }
        }.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Intent resIntent = new Intent();
        setResult(RESULT_CANCELED, resIntent);
        if(countDownTimer != null)
            countDownTimer.cancel();
        if(handler != null && matchRunnable != null)
            handler.removeCallbacks(matchRunnable);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /** Mark or remove marking**/
    private void setBorderColor(MemoryImageView imageView, int borderColor){
        imageView.setCropToPadding(true);
        imageView.setBackgroundColor(borderColor);
    }

    private void checkForCouple(){
        final MemoryImageView imgView1 = (MemoryImageView)gridview.getChildAt(firstClick);
        final MemoryImageView imgView2 = (MemoryImageView)gridview.getChildAt(secondClick);
        firstClick = secondClick = -1;
        handler = new Handler();
        matchRunnable = new Runnable() {
            @Override
            public void run() {
                // If images are not equal return to default pictures
                if (imgView1.getImageId() != imgView2.getImageId()) {
                    imgView1.setImageResource(MemoryImageView.DEFAULT_IMAGE_ID);
                    imgView1.setState(false);
                    imgView2.setImageResource(MemoryImageView.DEFAULT_IMAGE_ID);
                    imgView2.setState(false);
                } else
                    numOfMatches++;
                if(numOfMatches == sizeOfMatrix / 2){ // User Won
                    gameEnd();
                }
                // Remove marking
                setBorderColor(imgView1, getResources().getColor(R.color.colorBackGroundWhite));
                setBorderColor(imgView2, getResources().getColor(R.color.colorBackGroundWhite));
            }
        };
       handler.postDelayed(matchRunnable, 1000);
    }


    /**
     * Provides a simple way of getting a device's location and is well suited for
     * applications that do not require a fine-grained location and that do not need location
     * updates. Gets the best and most recent location currently available, which may be null
     * in rare cases when a location is not available.
     * <p>
     * Note: this method should be called after location permission has been granted.
     */
    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                        } else {
                            Log.w(TAG, "getLastLocation:exception", task.getException());
                            showSnackbar(getString(R.string.no_location_detected));
                        }
                    }
                });
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(R.id.game_activity_container);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void startLocationPermissionRequest() {
        ActivityCompat.requestPermissions(GameActivity.this,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                REQUEST_PERMISSIONS_REQUEST_CODE);
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.i(TAG, "Displaying permission rationale to provide additional context.");

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest();
                        }
                    });

        } else {
            Log.i(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest();
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.i(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.i(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.
                getLastLocation();
            } else {
                // Permission denied.

                // Notify the user via a SnackBar that they have rejected a core permission for the
                // app, which makes the Activity useless. In a real app, core permissions would
                // typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.
                showSnackbar(R.string.permission_denied_explanation, R.string.settings,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                // Build intent that displays the App settings screen.
                                Intent intent = new Intent();
                                intent.setAction(
                                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package",
                                        BuildConfig.APPLICATION_ID, null);
                                intent.setData(uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        });
            }
        }
    }

    private Integer[] getImages()
    {
        // Decide size of matrix according to numOfCubes(from LevelsActivity)
        if(numOfCubes == LevelsActivity.HARD_NUM_OF_CUBES)
            sizeOfMatrix = numOfCubes * LevelsActivity.MEDIUM_NUM_OF_CUBES;
        else
            sizeOfMatrix = numOfCubes * numOfCubes;

        // Shuffle all animals pictures
        List shuffledList = Arrays.asList(all_animals);
        Collections.shuffle(shuffledList);
        Integer[] arrayFrom = (Integer[])shuffledList.toArray();

        int half_of_images = sizeOfMatrix/LevelsActivity.EASY_NUM_OF_CUBES;
        Integer[] images = new Integer[sizeOfMatrix];
        // Copy half_of_images from all_animals array to images array
        System.arraycopy(arrayFrom, 0, images, 0, half_of_images);
        // Copy the same images, to get pairs of animals
        System.arraycopy(arrayFrom, 0, images, half_of_images, half_of_images);

        // Shuffle images array
        List list = Arrays.asList( images );
        Collections.shuffle(list);
        return (Integer[]) list.toArray();
    }

    // References to animal images
    Integer[] all_animals = {
            R.drawable.awl, R.drawable.bear, R.drawable.bird,
            R.drawable.cat, R.drawable.cow, R.drawable.deer,
            R.drawable.fox, R.drawable.lion, R.drawable.monkey,
            R.drawable.pig, R.drawable.rabbit, R.drawable.squirrel,
    };

}
