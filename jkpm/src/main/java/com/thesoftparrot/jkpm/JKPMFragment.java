package com.thesoftparrot.jkpm;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public abstract class JKPMFragment extends Fragment {

    public JKPMFragment() {}

    private static final int PERMISSION_REQUEST_CODE = 5432;

    protected void askRuntimePermissions(Context context, final String... PERMISSIONS){
        if(!areAllPermissionsEnabled(context, PERMISSIONS))
            requestPermissions(PERMISSIONS, PERMISSION_REQUEST_CODE);
    }

    protected boolean areAllPermissionsEnabled(Context context, String... PERMISSIONS){

        boolean isAllGranted = false;

        for(String permission : PERMISSIONS){
            if(ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED){
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

    protected void showDenialBox(final Context context){

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Permissions Required!");
        builder.setMessage("Permissions are required to proceed.");
        builder.setCancelable(false);

        builder.setPositiveButton("Goto Settings", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                gotoSettings(context);
            }
        });

        builder.create().show();
    }

    private void gotoSettings(Context context) {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", context.getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    protected abstract void onPermissionsGranted();
    protected abstract void onPermissionsDenied();
}
