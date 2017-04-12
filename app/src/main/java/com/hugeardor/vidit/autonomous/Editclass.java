package com.hugeardor.vidit.autonomous;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.RED;
import static com.hugeardor.vidit.autonomous.MainActivity.Load;

/**
 * Created by vidit on 31/3/17.
 */

public class Editclass extends Activity {

    private SharedPreferences pref ;
    private String rest_pref = "MyPref" ;
    public EditText ed;
    String rest_item ;
    //public ProgressDialog pg;
    File path = new File(Environment.getExternalStorageDirectory(), "Autonomous");

    final Handler h = new Handler();
    final int delay = 15000; //milliseconds
    Runnable runnable;
    File path_backup = new File(Environment.getExternalStorageDirectory(), "Autonomous_Backup");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);


        if (!path_backup.exists())

        {
            path_backup.mkdirs();
        }
        ed = (EditText) findViewById(R.id.ed);

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);

        Intent receive = getIntent();
        final String item = receive.getStringExtra("item");
        rest_item = item;
        pref = getSharedPreferences(rest_pref, MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("file name" , rest_item);
        editor.commit();

        File f = new File(path.toString());

        File data_path = new File(path, item);

        String[] loadText = Load(data_path);

        String finalString = "";

        for (int i = 0; i < loadText.length; i++) {
            finalString += loadText[i] + System.getProperty("line.separator");
        }

        ed.setText(finalString);


        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(item);


            }
        });


        //back_up(item);
        h.postDelayed(new Runnable() {
            public void run() {
                //do something
                back_up(item);

                runnable = this;
                h.postDelayed(this, delay);


            }
        }, delay);

    }







    public void back_up(final String item) {


          File file = new File(path_backup, item); // path to backup_folder
                String[] saveText = String.valueOf(ed.getText()).split(System.getProperty("line.separator"));
               final Toast auto = Toast.makeText(getApplicationContext() , "Auto Saving !" , Toast.LENGTH_SHORT);
                auto.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                auto.cancel();
            }
        }, 200);

                File f = new File(path_backup.toString());

                for (File file_backup : f.listFiles()) {


                    if (file_backup.getName().equals(item)) {
                        File dfl = new File(path_backup, item);
                        boolean del = dfl.delete();


                    }
                }

                Save_backup(file, saveText);

    }

    public static void Save_backup(File file, String[] data) {

        // this method is for writig file in the folder of backup
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                for (int i = 0; i < data.length; i++) {
                    fos.write(data[i].getBytes());
                    if (i < data.length - 1) {
                        fos.write("\n".getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void save(String item) {

        // this method will be called when save button is clicked
        final File file = new File(path, item );
        final String[] saveText = String.valueOf(ed.getText()).split(System.getProperty("line.separator"));
        final File dfl = new File(path, item);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(Editclass.this);
        alertDialogBuilder.setTitle("Message!");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setIcon(R.drawable.msg1);
        alertDialogBuilder.setMessage("Save file : " + item + "!");
        alertDialogBuilder.setPositiveButton("Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                // Intent back = new Intent(Editclass.this, MainActivity.class);
                boolean del = dfl.delete();
                Save_f(file, saveText);
                h.removeCallbacksAndMessages(null); // changed



                pref= getSharedPreferences(rest_pref, MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.remove("file name");//its remove name field from your SharedPreferences
                editor.commit();

                Intent startMain = new Intent(Editclass.this, MainActivity.class);
                //startMain.addCategory(Intent.CATEGORY_HOME);
                //startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(startMain);
                finish();



            }
        });
        alertDialogBuilder.setNegativeButton("Don't Save", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
            }
        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(BLUE);
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(RED);

    }

    public static void Save_f(File file, String[] data) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            try {
                for (int i = 0; i < data.length; i++) {
                    fos.write(data[i].getBytes());
                    if (i < data.length - 1) {
                        fos.write("\n".getBytes());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } finally {
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }



    @Override
    public void onBackPressed() {

        final Toast dsp = Toast.makeText(getApplicationContext() , "Save File First" , Toast.LENGTH_SHORT);
        dsp.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                dsp.cancel();
            }
        }, 150);


    }

    @Override
    protected void onPause() {
        h.removeCallbacksAndMessages(null);

        super.onPause();
    }

    @Override

    protected void onResume() {
        h.postDelayed(runnable, delay);

        super.onResume();

    }


}