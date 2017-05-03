package com.gd.cpdemo;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Rajesh Kumar Dawar on 14-04-2017.
 */

public class StudentAdapter extends ArrayAdapter<Student> {

    Context context;
    int resource;
    ArrayList<Student> studentList;

    public StudentAdapter(Context context, int resource, ArrayList<Student> objects) {
        super(context, resource, objects);

        this.context = context;
        this.resource = resource;
        studentList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;
        LayoutInflater inflater= LayoutInflater.from(context);
        view=inflater.inflate(resource,parent,false);

        TextView txtName = (TextView)view.findViewById(R.id.txtName);
        TextView txtPhone = (TextView)view.findViewById(R.id.txtPhone);

        Student student = studentList.get(position);
        txtName.setText(student.getName());
        txtPhone.setText(student.getPhone());
        return view;

    }

}
