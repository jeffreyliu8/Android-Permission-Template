package javis.wifidetect;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private static final String REQUESTED_PERMISSION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private TextView textView;

    private final static int MY_PERMISSIONS_REQUEST_LOCATION = 22; // A random number

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = findViewById(R.id.text);
        AppCompatButton button = findViewById(R.id.button);
        if (button != null) {
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Location is disabled, enable it?")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {
                                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                                    }
                                })
                                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                    public void onClick(final DialogInterface dialog, final int id) {

                                    }
                                }).show();
                    } else {
                        tryGetPermissionAndExecuteTask();
                    }
                }
            });
        }
    }

    private void tryGetPermissionAndExecuteTask() {
        // check if we have the permission
        if (ContextCompat.checkSelfPermission(this,
                REQUESTED_PERMISSION)
                != PackageManager.PERMISSION_GRANTED) {

            // if not, we explain why we need it first
            new AlertDialog.Builder(this)
                    .setTitle("Explain why we need this permission")
                    .setMessage("With this permission, we can get locations")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            showSystemPermissionRequestWindow();
                        }
                    })
                    .show();
        } else { // we already have permission
            getAvailableWifiAP();
        }
    }

    private void showSystemPermissionRequestWindow() {
        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                REQUESTED_PERMISSION)) {

            textView.setText("Got rejected the first time, and now we need this permission again");
            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
        }

        ActivityCompat.requestPermissions(this,
                new String[]{REQUESTED_PERMISSION},
                MY_PERMISSIONS_REQUEST_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                    // add your logic here
                    getAvailableWifiAP();
                } else {
                    textView.setText("permission denied ");
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void getAvailableWifiAP() {
        textView.setText("now we have permission");
    }
}