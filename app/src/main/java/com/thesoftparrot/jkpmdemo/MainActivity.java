package com.thesoftparrot.jkpmdemo;

import android.Manifest;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.thesoftparrot.jkpm.JKPMActivity;

public class MainActivity extends JKPMActivity {

    private Button camBtn, galBtn, locBtn;

    private static final String[] PERMISSIONS = new String[]{
            Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_CONTACTS,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        camBtn = findViewById(R.id.camera_permission_btn);
        galBtn = findViewById(R.id.check_permissions_btn);
        locBtn = findViewById(R.id.all_permissions_btn);

        camBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askRuntimePermissions(Manifest.permission.CAMERA);
            }
        });

        galBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(areAllPermissionsEnabled(PERMISSIONS)){
                    Toast.makeText(MainActivity.this, "Permissions are enabled", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(MainActivity.this, "Permissions are NOT enabled", Toast.LENGTH_SHORT).show();
            }
        });

        locBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askRuntimePermissions(PERMISSIONS);
            }
        });

    }

    @Override
    protected void onPermissionsGranted() {
        Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPermissionsDenied() {
        Toast.makeText(this, "Denied", Toast.LENGTH_SHORT).show();
        showDenialBox();
    }
}