package edu.msu.waitinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements android.view.View.OnClickListener {
    SQLiteDatabase db;
    EditText editsearchname,editempname,editempmail,editempsalary;
    Button Add, Delete, Modify, View,search,Clear ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Create database,StudentDB database name
        db=openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        //create table Student
        db.execSQL("CREATE TABLE IF NOT EXISTS Student(EmpId INTEGER PRIMARY KEY AUTOINCREMENT,EmpName VARCHAR,EmpMail VARCHAR,EmpSalary VARCHAR);");
        editsearchname = (EditText) findViewById(R.id.edtemployeename);
        editempname = (EditText) findViewById(R.id.editText);
        editempmail = (EditText) findViewById(R.id.editText2);
        editempsalary = (EditText) findViewById(R.id.editText3);
        Add = (Button) findViewById(R.id.btnsave);
        Clear = (Button) findViewById(R.id.btnclear);

        Delete= (Button) findViewById(R.id.btndel);
        Modify= (Button) findViewById(R.id.btnupdate);
        View= (Button) findViewById(R.id. btnselect);
        search=(Button) findViewById(R.id. btnselectperticular);
        Add.setOnClickListener(this);
        Clear.setOnClickListener(this);
        Delete.setOnClickListener(this);
        Modify.setOnClickListener(this);
        View.setOnClickListener(this);
        search.setOnClickListener(this);
    }
//method for toast message
    public void msg(Context context,String str)
    {
        Toast.makeText(this,str,Toast.LENGTH_SHORT).show();

    }
//method for alertdialog
    public void pop(Context context,String str)
    {
       new AlertDialog.Builder(context)
            .setTitle("Records")
                .setMessage(str)

    // Specifying a listener allows you to take an action before dismissing the dialog.
    // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            // Continue with delete operation
        }
    })
            // A null listener allows the button to dismiss the dialog and take no further action.
            .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnsave)
        {
            // code for save data
            if(editempname.getText().toString().trim().length()==0||
                    editempmail.getText().toString().trim().length()==0||
                    editempsalary.getText().toString().trim().length()==0)
            {
                msg(this, "Please enter all values");
                return;
            }
            db.execSQL("INSERT INTO Student(EmpName,EmpMail,EmpSalary)VALUES('"+ editempname.getText()+"','"+ editempmail.getText()+ "','"+    editempsalary.getText()+"');");
            msg(this, "Record added");
        }

        else if(v.getId()==R.id.btnupdate)
        {
            //code for update data
            if(editsearchname.getText().toString().trim().length()==0)
            {//if user pressed update without entering name
                msg(this, "Enter Student Name");
                return;
            }
            //if name is valid and change it made, the system updates
            Cursor c=db.rawQuery("SELECT * FROM Student WHERE EmpName='"+ editsearchname.getText()+"'", null);
            if(c.moveToFirst()) {
                db.execSQL("UPDATE Student  SET EmpName ='"+ editempname.getText()+"', EmpMail='"+ editempmail.getText()+"',EmpSalary='"+      editempsalary.getText()+"' WHERE EmpName ='"+editsearchname.getText()+"'");
                msg(this, "Record Modified");
            }
            else
                //if the name entered by the user is invalid, following toast pops up
            {
                msg(this, "Invalid Student Name");
            }
        }
        else if(v.getId()==R.id.btndel)
        {
            //code for delete data
            if(editsearchname.getText().toString().trim().length()==0)
            {
                msg(this, " Please enter Student  Name ");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM Student WHERE EmpName ='"+ editsearchname.getText()+"'", null);
            if(c.moveToFirst())
            {
                db.execSQL("DELETE FROM Student WHERE EmpName ='"+ editsearchname.getText()+"'");
                msg(this, "Record Deleted");
            }
            else
            {
                msg(this, "Invalid Student Name ");
            }
        }
        else if(v.getId()==R.id.btnclear)
        {
           editempmail.setText("");
            editsearchname.setText("");
            editempsalary.setText("");
            editempname.setText("");

        }
        else if (v.getId() == R.id.btnselect)
        {
            //code for select all data
            Cursor c=db.rawQuery("SELECT * FROM Student", null);
            if(c.getCount()==0)
            {
                //if there is no data saved in the database, follwing error pops up
                msg(this, "No records found");
                return;
            }
            //if there is data, the list of data saved pops up
            StringBuffer buffer=new StringBuffer();
            while(c.moveToNext())
            {
                buffer.append("Student Name: "+c.getString(1)+"\n");
                buffer.append("Class ID: "+c.getString(2)+"\n");
                buffer.append("Year Priority: "+c.getString(3)+"\n\n");

            }
            pop(this, buffer.toString());
        }

        else if(v.getId()==R.id.btnselectperticular)
        {
            //code for select particular data
            if(editsearchname.getText().toString().trim().length()==0)
            {
                msg(this, "Enter Student Name");
                return;
            }
            Cursor c=db.rawQuery("SELECT * FROM Student WHERE EmpName='"+editsearchname.getText()+"'", null);
            if(c.moveToFirst())
            {
                editempname.setText(c.getString(1));
                editempmail.setText(c.getString(2));
                editempsalary.setText(c.getString(3));

                          }
            else
            {
                msg(this, "Invalid Student Name");
            }
        }
    }
}
