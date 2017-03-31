package com.hugeardor.vidit.autonomous;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
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

    public EditText ed;
    public ProgressDialog pg;
    File path = new File(Environment.getExternalStorageDirectory(), "Autonomous");

    String fd = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();

    final  Handler h = new Handler();
    final int delay = 15000; //milliseconds
    Runnable runnable;
    File path_backup = new File(Environment.getExternalStorageDirectory(), "Autonomous_Backup");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit);
        pg=new ProgressDialog(this);
        pg.setMessage("Backing Up");
        pg.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pg.setIndeterminate(true);
        pg.setProgress(0);
        pg.setCancelable(true);
        if(!path_backup.exists())
        //dir.mkdirs();
        {
            path_backup.mkdirs() ;
        }
        ed = (EditText) findViewById(R.id.ed);

        FloatingActionButton fab3 = (FloatingActionButton) findViewById(R.id.fab3);

        Intent receive = getIntent();
        final String item = receive.getStringExtra("item");

        File f = new File(path.toString()) ;

        File data_path = new File(path, item);

        String [] loadText = Load(data_path);

        String finalString = "";

        for (int i = 0; i < loadText.length; i++)
        {
            finalString += loadText[i] + System.getProperty("line.separator");
        }

        ed.setText(finalString);



        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save(item);
            }
        });


        back_up(item);




    }

    public void back_up(final String item)
    {


        h.postDelayed(new Runnable(){
            public void run(){
                //do something

                //fd =DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString() ;

                File file = new File(path_backup, item +".txt") ; // path to backup_folder
                String [] saveText = String.valueOf(ed.getText()).split(System.getProperty("line.separator"));


                pg.show();


                Thread wait= new Thread()
                {
                    public void run()
                    {
                        try{

                            sleep(3000);
                        }
                        catch(Exception e)
                        {
                            e.printStackTrace();
                        }
                        finally{
                            pg.cancel();
                        }
                    }
                };
                wait.start();

                File f = new File(path_backup.toString()) ;

                for(File file_backup: f.listFiles()) {

                    if (file_backup.getName().equals(item)) {
                        File dfl = new File(path_backup, item);
                        boolean del = dfl.delete();




                    }
                }

                Save_backup(file, saveText);
                runnable=this;
                h.postDelayed(this, delay);

            }
        }, delay);
    }
    public static void Save_backup(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }
    }

    public void save(String item)
    {

        fd = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString() ;
        //File file = new File (path + "/saveFile" +fd+".txt"); //file path to save
        File file = new File(path, fd +".txt") ;
        String [] saveText = String.valueOf(ed.getText()).split(System.getProperty("line.separator"));

        ed.setText("");
        File dfl = new File(path, item);
        boolean del = dfl.delete();
        Save_f (file, saveText);

         final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Message!");

        alertDialogBuilder.setIcon(R.drawable.msg1);
        alertDialogBuilder.setMessage("Old file\n "+ item + " \nhas changed to\n " + file.getName() + " \ndue to change in content detected "+"!");

        //alertDialogBuilder.setViewBackground(HALLOWEEN_ORANGE);
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                   // Intent back = new Intent(Editclass.this, MainActivity.class);
                h.removeCallbacks(runnable);
                startActivity(new Intent(Editclass.this,MainActivity.class));
            }
        });


        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
         alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(BLUE);


    }
    public static void Save_f(File file, String[] data)
    {
        FileOutputStream fos = null;
        try
        {
            fos = new FileOutputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        try
        {
            try
            {
                for (int i = 0; i<data.length; i++)
                {
                    fos.write(data[i].getBytes());
                    if (i < data.length-1)
                    {
                        fos.write("\n".getBytes());
                    }
                }
            }
            catch (IOException e) {e.printStackTrace();}
        }
        finally
        {
            try
            {
                fos.close();
            }
            catch (IOException e) {e.printStackTrace();}
        }




    }


    @Override
    public void onBackPressed() {

      // do nothing


        }
        @Override
    public void onPause(){
            h.removeCallbacks(runnable);
            super.onPause();
    }



}


