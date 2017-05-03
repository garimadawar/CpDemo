package com.gd.cpdemo;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

import static com.gd.cpdemo.R.id.listView;
import static com.gd.cpdemo.Util.INSERT_STUDENT_PHP;
import static java.lang.System.out;

public class MainActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    @InjectView(R.id.editTextName)
    EditText txtName;

    @InjectView(R.id.editTextPhone)
    EditText txtPhone;

    @InjectView(R.id.editTextEmail)
    EditText txtEmail;

    @InjectView(R.id.radioButtonMale)
    RadioButton rbMale;
    @InjectView(R.id.radioButtonFemale)
    RadioButton rbFemale;

    @InjectView(R.id.spinnerCity)
    Spinner spCity;
    @InjectView(R.id.buttonSubmit)
    Button btnSubmit;

    RequestQueue requestQueue;
    ProgressDialog progressDialog;
    ContentResolver resolver;
    Student student, rcvStudent;
    ConnectivityManager connectivityManager;
    NetworkInfo networkInfo;
    ArrayAdapter<String> adapter;
    boolean updateMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressDialog= new ProgressDialog(this);
        progressDialog.setMessage("Please wait");
        progressDialog.setCancelable(false);

        ButterKnife.inject(this);
        student = new Student();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item);
        adapter.add("--Select City--");
        adapter.add("Ludhiana");
        adapter.add("Chandigarh");
        adapter.add("Jalandhar");
        adapter.add("Ambala");
        adapter.add("Delhi");
        adapter.add("Patna");

        spCity.setAdapter(adapter);
        spCity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                             @Override
                                             public void onItemSelected(AdapterView<?> parent, View view, int i, long l) {
                                                 if (i != 0) {
                                                     student.setCity(adapter.getItem(i));
                                                 }
                                             }

                                             @Override
                                             public void onNothingSelected(AdapterView<?> parent) {

                                             }
                                         }
        );

        requestQueue = Volley.newRequestQueue(this);

        rbMale.setOnCheckedChangeListener(this);
        rbFemale.setOnCheckedChangeListener(this);

        resolver = getContentResolver();

        Intent rcv = getIntent();

        updateMode = rcv.hasExtra("keyStudent");

        if (updateMode) {
            rcvStudent = (Student) rcv.getSerializableExtra("keyStudent");

            txtName.setText(rcvStudent.getName());
            txtPhone.setText(rcvStudent.getPhone());
            txtEmail.setText(rcvStudent.getEmail());


            if (rcvStudent.getGender().equals("Male")) {
                rbMale.setChecked(true);
            } else {
                rbFemale.setChecked(true);
            }

            int p = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                if (adapter.getItem(i).equals(rcvStudent.getCity())) {
                    p = i;
                    break;
                }
            }

            spCity.setSelection(p);

            btnSubmit.setText("Update");
        }
    }

    boolean isNetworkConnected() {

        connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        networkInfo = connectivityManager.getActiveNetworkInfo();

        return (networkInfo != null && networkInfo.isConnected());

    }

    public void clickHandler(View view) {



            student.setName(txtName.getText().toString().trim());
            student.setPhone(txtPhone.getText().toString().trim());
          student.setEmail(txtEmail.getText().toString().trim());
            insertIntoCloud();
/*
           if (validateFields()) {
                if (isNetworkConnected())
                    insertIntoCloud();
                else
                    Toast.makeText(this, "Please connect to Internet", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Please correct Input", Toast.LENGTH_LONG).show();
            }*/
        //insertIntoDb();
    }

     void insertIntoCloud() {
         String url = "";

         if (!updateMode) {
             url = Util.INSERT_STUDENT_PHP;
         } else {
             url = Util.UPDATE_STUDENT_PHP;
         }

         progressDialog.show();
         Log.e("user",student.toString());
         StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
             @Override
             public void onResponse(String response) {
                 Toast.makeText(MainActivity.this, "Succes"+response, Toast.LENGTH_SHORT).show();
                 progressDialog.dismiss();
            /*
                 try {
                     JSONObject jsonObject = new JSONObject(response);
                     int success = jsonObject.getInt("success");
                     String message = jsonObject.getString("message");

                     if (success == 1) {
                         Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();

                         if (updateMode)
                             finish();

                     } else {
                         Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
                     }
                     progressDialog.dismiss();
                 } catch (Exception e) {
                     e.printStackTrace();
                     progressDialog.dismiss();
                     Toast.makeText(MainActivity.this, "Some Exception" + e, Toast.LENGTH_LONG).show();
                 }*/
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
                 progressDialog.dismiss();
                 Toast.makeText(MainActivity.this, "Some Error" + error.getMessage(), Toast.LENGTH_LONG).show();
             }
         }) {
             @Override
             protected Map<String, String> getParams() throws AuthFailureError {
                 Map<String, String> map = new HashMap<>();

                 if (updateMode)
                     map.put("id", String.valueOf(rcvStudent.getId()));

                 map.put("name", student.getName());
                 map.put("phone", student.getPhone());
                 map.put("email", student.getEmail());
                 map.put("gender", student.getGender());
                 map.put("city", student.getCity());
                 return map;
             }
         };

         requestQueue.add(request); // execute the request, send it ti server

         clearFields();
     }
     public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        if (b) {
            if (id == R.id.radioButtonMale) {
                student.setGender("Male");
            } else {
                student.setGender("Female");
            }
        }
    }

    public void insertIntoDb() {

        ContentValues values = new ContentValues();

        values.put(Util.COL_NAME, student.getName());
        values.put(Util.COL_PHONE, student.getPhone());
        values.put(Util.COL_EMAIL, student.getEmail());
        values.put(Util.COL_GENDER, student.getGender());
        values.put(Util.COL_CITY, student.getCity());

        Uri dummy = resolver.insert(Util.STUDENT_URI, values);
        Toast.makeText(this, student.getName() + " Registered Successfully " + dummy.getLastPathSegment(), Toast.LENGTH_LONG).show();

        clearFields();
    }



    void clearFields(){
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        spCity.setSelection(0);
        rbMale.setChecked(false);
        rbFemale.setChecked(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
         menu.add(0,101,0,"All Students");
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id= item.getItemId();
        if(id == 101){
            Intent i = new Intent(MainActivity.this,ListStudentsActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    boolean validateFields(){
        boolean flag = true;

        if(student.getName().isEmpty()){
            flag = false;
            txtName.setError("Please Enter Name");
        }

        if(student.getPhone().isEmpty()){
            flag = false;
            txtPhone.setError("Please Enter Phone");
        }else{
            if(student.getPhone().length()<10){
                flag = false;
                txtPhone.setError("Please Enter 10 digits Phone Number");
            }
        }

        if(student.getEmail().isEmpty()){
            flag = false;
            txtEmail.setError("Please Enter Email");
        }else{
            if(!(student.getEmail().contains("@") && student.getEmail().contains("."))){
                flag = false;
                txtEmail.setError("Please Enter correct Email");
            }
        }
        return flag;
    }
}

