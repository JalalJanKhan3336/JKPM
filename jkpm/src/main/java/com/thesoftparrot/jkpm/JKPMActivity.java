package com.thesoftparrot.jkpm;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public abstract class JKPMActivity extends AppCompatActivity {

    private static final String TAG = "JKPMActivity";
    private static final int PERMISSION_REQUEST_CODE = 8987;

    protected void askRuntimePermissions(final String... PERMISSIONS){
        if(!areAllPermissionsEnabled(PERMISSIONS))
            ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    protected boolean areAllPermissionsEnabled(String... PERMISSIONS){

        boolean isAllGranted = false;

        for(String permission : PERMISSIONS){
            if(ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED){
                isAllGranted = true;
            }else {
                isAllGranted = false;
                break;
            }
        }

        return isAllGranted;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode == PERMISSION_REQUEST_CODE){
            if(grantResults.length > 0){
                boolean isAllGranted = false;

                for(int result : grantResults){
                    if(result == PackageManager.PERMISSION_GRANTED){
                        isAllGranted = true;
                    }else {
                        isAllGranted = false;
                        break;
                    }
                }

                if(isAllGranted)
                    onPermissionsGranted();
                else
                    onPermissionsDenied();
            }
        }

    }

    protected void showDenialBox(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Permissions Required!");
        builder.setMessage("Permissions are required to proceed.");
        builder.setCancelable(false);

        builder.setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                gotoSettings();
            }
        });

        builder.create().show();
    }

    private void gotoSettings() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    protected abstract void onPermissionsGranted();
    protected abstract void onPermissionsDenied();
}
