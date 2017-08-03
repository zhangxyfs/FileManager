package com.z7dream.filemanager;

import android.Manifest;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.z7dream.lib.model.FileConfig;
import com.z7dream.lib.service.FileUpdatingService;
import com.z7dream.manager.mvp.ui.FileBaseActivity;

public class MainActivity extends AppCompatActivity {
    private Button open, setting;
    private ConstraintLayout setCL;
    private EditText setTitle;
    private TextView textBtn;
    private ToggleButton displaySearch, displayToolbarSearch;
    private FileConfig fileConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fileConfig = new FileConfig();
        fileConfig.userToken = "123";
        fileConfig.fileBaseTitle = getString(R.string.mine_file_str);
        fileConfig.isToolbarSearch = true;
        fileConfig.isVisableSearch = true;
        Application.setFileConfig(fileConfig);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        FileUpdatingService.startObserver(getApplicationContext());
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                    }
                }).check();
        open = (Button) findViewById(R.id.open);
        setting = (Button) findViewById(R.id.setting);
        setCL = (ConstraintLayout) findViewById(R.id.setCL);
        setTitle = (EditText) findViewById(R.id.setTitle);
        textBtn = (TextView) findViewById(R.id.textBtn);
        displaySearch = (ToggleButton) findViewById(R.id.displaySearch);
        displayToolbarSearch = (ToggleButton) findViewById(R.id.displayToolbarSearch);

        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileBaseActivity.open(MainActivity.this);
            }
        });
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCL.setVisibility(setCL.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
            }
        });
        displaySearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (!b) {
                    displaySearch.setChecked(false);
                }
            }
        });
        displayToolbarSearch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    displayToolbarSearch.setChecked(true);
                }
            }
        });
        setTitle.setText(fileConfig.fileBaseTitle);
        displaySearch.setChecked(fileConfig.isVisableSearch);
        displayToolbarSearch.setChecked(fileConfig.isToolbarSearch);

        textBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fileConfig.fileBaseTitle = setTitle.getText().toString();
                fileConfig.isVisableSearch = displaySearch.isChecked();
                fileConfig.isToolbarSearch = displayToolbarSearch.isChecked();
                Application.setFileConfig(fileConfig);
                setCL.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "save complete.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
