package com.edu.senac.ferramentashardware;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

public class DadosTelFragment extends Fragment {

    TextView telefone, imei, modelo;

    boolean dadostel;
    String[] perms = {"android.permission.READ_PHONE_STATE","android.permission.READ_SMS","android.permission.READ_PHONE_NUMBERS"};
    int permsRequestCode = 200;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dadostel, container, false);

        requestPermissions(perms, permsRequestCode);

        telefone = view.findViewById(R.id.Telefone);
        imei = view.findViewById(R.id.Imei);
        modelo = view.findViewById(R.id.Modelo);

        telefone.setText(GetTel());
        imei.setText(GetImei());
        modelo.setText(GetModelo());

        return view;
    }


    public String GetModelo() {
        String manufacturer = Build.MANUFACTURER;
        String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return capitalize(model);
        }
        return capitalize(manufacturer) + " " + model;
    }

    private String capitalize(String s) {
        if (s == null || s.length() == 0) {
            return "";
        }
        char first = s.charAt(0);
        if (Character.isUpperCase(first)) {
            return s;
        } else {
            return Character.toUpperCase(first) + s.substring(1);
        }
    }

    public String GetImei() {
        TelephonyManager tm = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        //tm.getPhoneCount();    <--        números de chips do telefone
        return tm.getDeviceId(1)+"\n"+tm.getDeviceId(0);
    }

    public String GetTel() {
        TelephonyManager mTelephonyMgr;
        mTelephonyMgr = (TelephonyManager) getActivity().getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return mTelephonyMgr.getLine1Number();
    }
}
