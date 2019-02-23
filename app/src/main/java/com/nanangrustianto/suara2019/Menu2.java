package com.nanangrustianto.suara2019;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by nanangrustianto.com on 18/09/17.
 */


public class Menu2 extends Fragment {

    @BindView(R.id.checkBox_gps)
    CheckBox checkBoxGPS;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //returning our layout file
        //change R.layout.yourlayoutfilename for each of your fragments
        View rootView = inflater.inflate(R.layout.fragment_menu2, container, false);

        ButterKnife.bind(this, rootView);

        setCheckBox();

        checkBoxGPS.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    showGPSDialog();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setCheckBox();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //you can set the title for your toolbar here for different fragments different titles
        getActivity().setTitle("Pengaturan");
    }

    private void setCheckBox(){
        // check GPS for status checkbox, checkbox checklist if GPS active
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            checkBoxGPS.setChecked(true);
        } else {
            checkBoxGPS.setChecked(false);
        }
    }

    private void showGPSDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("GPS tidak aktif, apakah akan di aktfkan?")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intentGPSSetting = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(intentGPSSetting);
                            }
                        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
}
