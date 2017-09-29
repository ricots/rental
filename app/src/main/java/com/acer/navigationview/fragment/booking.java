package com.acer.navigationview.fragment;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acer.navigationview.R;
import com.acer.navigationview.activity.login;
import com.acer.navigationview.activity.regis;
import com.acer.navigationview.koneksi.config;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by Admin on 04-06-2015.
 */
public class booking extends Fragment implements AdapterView.OnItemSelectedListener, View.OnClickListener {
    TextView id_mobil,pinjam_tgl,pinjam_kembali,pinjam_mobil,pinjam_lama,pinjam_total,pinjam_harga;
    EditText input_tgl_pinjam, input_tgl_kembali, input_harga;
    Spinner spin_mobil;
    private JSONArray result;
    private ArrayList<String> mobil;
    private SimpleDateFormat dateFormatter;
    private DatePickerDialog fromDatePickerDialog;
    private DatePickerDialog toDatePickerDialog,datepicker;
    Button check,booking;
    String pinjam, kembali;
    ProgressDialog PD;
    SharedPreferences sp;
    SharedPreferences.Editor spe;
    String ktp;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.content_fragment,container,false);
        sp = getActivity().getSharedPreferences(config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        ktp = sp.getString(config.EMAIL_SHARED_PREF, "Not Available");

        PD = new ProgressDialog(getActivity());
        PD.setMessage("silahkan tunggu.....");
        PD.setCancelable(false);
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        id_mobil = (TextView) v.findViewById(R.id.id_mobil);
        input_tgl_pinjam = (EditText) v.findViewById(R.id.input_tgl_pinjam);
        //input_tgl_pinjam.setInputType(InputType.TYPE_NULL);
        input_tgl_kembali = (EditText) v.findViewById(R.id.input_tgl_kembali);
        //input_tgl_kembali.setInputType(InputType.TYPE_NULL);
        input_harga = (EditText) v.findViewById(R.id.input_harga);
        spin_mobil = (Spinner) v.findViewById(R.id.spin_nama_mobil);
        mobil = new ArrayList<String>();
        spin_mobil.setOnItemSelectedListener(this);
        getData();
        setDateTime();
        setDateTimeField();

        pinjam_tgl = (TextView) v.findViewById(R.id.pinjam_tgl);
        pinjam_kembali = (TextView) v.findViewById(R.id.pinjam_kembali);
        pinjam_lama = (TextView) v.findViewById(R.id.pinjam_lama);
        pinjam_mobil = (TextView) v.findViewById(R.id.pinjam_mobil);
        pinjam_total = (TextView) v.findViewById(R.id.pinjam_total);
        pinjam_harga = (TextView) v.findViewById(R.id.pinjam_harga);


        //final List<Date> a= getDates(pinjam, kembali);
        check = (Button) v.findViewById(R.id.check);
        check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pinjam_tgl.setText("tanggal pinjam " + input_tgl_pinjam.getText().toString());
                pinjam_kembali.setText("tanggal kembali " +input_tgl_kembali.getText().toString());
                pinjam_mobil.setText("nama mobil " + spin_mobil.getSelectedItem().toString());
                pinjam = input_tgl_pinjam.getText().toString();
                kembali = input_tgl_kembali.getText().toString();
                final  String a = getDates(pinjam, kembali);
                pinjam_lama.setText(String.valueOf(a) + " hari");
                pinjam_harga.setText("harga sewa mobil perhari " + input_harga.getText().toString());

            }
        });

        pinjam_lama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                final  String a = getDates(pinjam, kembali);
                int lamanya = Integer.parseInt(a);
                int hrg = Integer.parseInt(input_harga.getText().toString());
                int total = lamanya * hrg;
                pinjam_total.setText("" + total);
            }
        });

        booking = (Button) v.findViewById(R.id.save);
        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transaksi();
            }
        });
        return v;
    }

    public void transaksi(){
        PD.show();
        final String ktp_member = ktp;
        final String pinjam = input_tgl_pinjam.getText().toString();
        final String kembali = input_tgl_kembali.getText().toString();
        final String idmobil = id_mobil.getText().toString();
        final String lama = pinjam_lama.getText().toString();
        final String tot_bayar = pinjam_total.getText().toString();


        StringRequest postRequest = new StringRequest(Request.Method.POST, config.TRANSAKSI,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        PD.dismiss();
                        Toast.makeText(getActivity(),
                                response.toString(),
                                Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                PD.dismiss();
                Toast.makeText(getActivity(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(config.KEY_ktp,ktp_member);
                params.put(config.KEY_TGL_PINJAM, pinjam);
                params.put(config.KEY_TGL_KEMBALI, kembali);
                params.put(config.KEY_ID_MOBIL,idmobil);
                params.put(config.KEY_LAMA,lama);
                params.put(config.KEY_TOT_BAYAR,tot_bayar);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(postRequest);
    }

    private static String getDates(String dateString1, String dateString2)
    {
        String dayDifference = null;

        try {
            //Dates to compare
            String CurrentDate=  dateString1;
            String FinalDate=  dateString2;

            Date date1;
            Date date2;

            SimpleDateFormat dates = new SimpleDateFormat("yyyy-MM-dd");

            //Setting dates
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(FinalDate);

            //Comparing dates
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);

            //Convert long to String
             dayDifference = Long.toString(differenceDates);

            Log.e("HERE","HERE: " + dayDifference);

        } catch (Exception exception) {
            Log.e("DIDN'T WORK", "exception " + exception);
        }

        return dayDifference;
    }



    private void setDateTime() {
        input_tgl_pinjam.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_tgl_pinjam.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        toDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_tgl_pinjam.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void setDateTimeField() {
        input_tgl_kembali.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        fromDatePickerDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_tgl_kembali.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));

        datepicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                input_tgl_kembali.setText(dateFormatter.format(newDate.getTime()));
            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    private void getData(){
        //Creating a string request
        final ProgressDialog loading = ProgressDialog.show(getActivity(),"Loading Data", "Please wait...",false,false);
        StringRequest stringRequest = new StringRequest(config.DAFTAR_MOBIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        JSONObject j = null;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray(config.JSON_ARRAY);

                            //Calling method getStudents to get the students from the JSON Array
                            getmobil(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        /*stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                3600, DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));*/
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());

        //Adding request to the queue
        requestQueue.add(stringRequest);
        stringRequest.setRetryPolicy(new RetryPolicy() {
            @Override
            public int getCurrentTimeout() {
                return 50000;
            }

            @Override
            public int getCurrentRetryCount() {
                return 50000;
            }

            @Override
            public void retry(VolleyError error) throws VolleyError {
                loading.dismiss();
                Toast.makeText(getActivity(),"silahkan coba lagi",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getmobil(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                mobil.add(json.getString(config.KEY_NAMA_MOBIL));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        spin_mobil.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, mobil));
    }

    private String get_id_mobil(int position){
        String id_mobil="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            id_mobil = json.getString(config.KEY_ID_MOBIL);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return id_mobil;
    }

    private String get_harga(int position){
        String harga="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            harga = json.getString(config.KEY_HARGA);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return harga;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        input_harga.setText(get_harga(position));
        id_mobil.setText(get_id_mobil(position));
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        input_harga.setText("");
        id_mobil.setText("");
    }

    @Override
    public void onClick(View v) {
        if(v == input_tgl_pinjam) {
            toDatePickerDialog.show();
        }

        if(v == input_tgl_kembali) {
            datepicker.show();
        }
    }
}
