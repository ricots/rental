package com.acer.navigationview.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.acer.navigationview.R;
import com.acer.navigationview.koneksi.config;

import java.util.HashMap;
import java.util.Map;

public class regis extends AppCompatActivity {
    EditText input_nm, input_ktp, input_pass,input_alamat;
    Button btnregis;
    ProgressDialog PD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regis);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Registrasi");
        getSupportActionBar().getThemedContext();
        toolbar.setTitleTextColor(0xFFFFFFFF);

        input_nm = (EditText) findViewById(R.id.input_nama);
        input_ktp = (EditText) findViewById(R.id.input_ktp);
        input_pass = (EditText) findViewById(R.id.input_password);
        input_alamat = (EditText) findViewById(R.id.input_alamat);


        PD = new ProgressDialog(this);
        PD.setMessage("silahkan tunggu.....");
        PD.setCancelable(false);

        btnregis = (Button) findViewById(R.id.regis);
        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regis_user();
            }
        });
    }

    public void regis_user() {
            final String ktp_member = input_ktp.getText().toString();
            final String nama_member = input_nm.getText().toString();
            final String alamat_member = input_alamat.getText().toString();
            final String pass_member = input_pass.getText().toString();

        if ((ktp_member.equals("")) && (nama_member.equals("")) && (alamat_member.equals(""))
                && (pass_member.equals(""))) {
            Toast.makeText(regis.this, "harap isi data", Toast.LENGTH_LONG).show();
            PD.dismiss();
        } else {
            PD.show();
            StringRequest postRequest = new StringRequest(Request.Method.POST, config.REGIS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            PD.dismiss();
                            Toast.makeText(getApplicationContext(),
                                    "berhasil registrasi",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(regis.this, login.class);
                            startActivity(intent);
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    PD.dismiss();
                    Toast.makeText(getApplicationContext(),
                            error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put(config.KEY_ktp, ktp_member);
                    params.put(config.KEY_NAMA, nama_member);
                    params.put(config.KEY_PASSWORD, pass_member);
                    params.put(config.KEY_ALAMAT, alamat_member);
                    return params;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(regis.this);
            requestQueue.add(postRequest);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // moveTaskToBack(true);;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
