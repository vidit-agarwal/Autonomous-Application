package com.hugeardor.vidit.autonomous;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
import static com.hugeardor.vidit.autonomous.R.attr.colorAccent;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    public EditText editText;
    public TextView textView;
    public ListView lv ;
   String [] SavedFiles ;
    ArrayAdapter arrayAdapter;
    // public Calendar c = Calendar.getInstance();
    //SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    //String fd = df.format(c.getTime());
     String fd = DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString();
    // public String path = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous";
    File path = new File(Environment.getExternalStorageDirectory(), "Autonomous");
   // public Button save, load;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        context = this;

        lv = (ListView)findViewById(R.id.lv);
        //System.out.println("Current time => "+c.getTime());

      //  SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        //String formattedDate = df.format(c.getTime());
        editText = (EditText) findViewById(R.id.editView);
        textView = (TextView) findViewById(R.id.textView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
       /* save = (Button) findViewById(R.id.bSave);
        load = (Button) findViewById(R.id.bLoad);*/

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

       FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.fab1) ;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

       // File dir = new File(path);
        if(!path.exists())
        //dir.mkdirs();
        {
          path.mkdirs() ;
        }
        ShowSavedFiles();



            }


    public void writeMessage(View view){     //this will create a file in internal stoage in a folder

        fd =DateFormat.format("MM-dd-yyyyy-h-mmssaa", System.currentTimeMillis()).toString() ;
        //File file = new File (path + "/saveFile" +fd+".txt"); //file path to save
        File file = new File(path, fd  +".txt") ;
        String [] saveText = String.valueOf(editText.getText()).split(System.getProperty("line.separator"));

        editText.setText("");

       final Toast tst = Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_LONG);
        tst.show();
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                tst.cancel();
            }
        }, 100);
        Save (file, saveText);
        //ShowSavedFiles();
    }
    void ShowSavedFiles() { //this will list all the files present in a folder in internal storage into a list view

        ArrayList fileList = new ArrayList();
        File f = new File(path.toString());

                for (File file : f.listFiles()) {

                    fileList.add(file.getName());
                    //fileList.add(file.getPath()) ;
                }
            arrayAdapter = new ArrayAdapter(this, R.layout.support_simple_spinner_dropdown_item, fileList);

            Log.i("MainActivity", "File List is=" + fileList);
            lv.setAdapter(arrayAdapter);
            lv.setClickable(true);
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectfromlist = (String) lv.getItemAtPosition(position);
                    Intent pass = new Intent(MainActivity.this, Editclass.class);
                    pass.putExtra("item" , selectfromlist);
                    startActivity(pass);


                }
            });
            lv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectfromlist = (String) lv.getItemAtPosition(position);


                    alert_dialog(selectfromlist);

                    return true;
                }

            });

        }
    public void alert_dialog(final String s)   //this dialog is for deletion of a file from the folder
    {
        final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("Delete!");

        alertDialogBuilder.setIcon(R.drawable.del);
        alertDialogBuilder.setMessage("Sure , you want to delete ? !");

        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub

                File f = new File(path.toString()) ;

                for(File file: f.listFiles()){

                    if(file.getName().equals(s))
                    {

                        File dfl = new File(path, s);
                        boolean del = dfl.delete();
                        if(del) {
                            final Toast toast = Toast.makeText(getApplicationContext(), "File Deleted :" + s + " !", Toast.LENGTH_SHORT);
                            toast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 100);
                            ShowSavedFiles();
                        }
                        else{
                            final Toast toast = Toast.makeText(getApplicationContext(), "Error in Deletion" , Toast.LENGTH_SHORT);
                            toast.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    toast.cancel();
                                }
                            }, 100);
                            ShowSavedFiles();
                        }

                    }
                }


            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                final Toast toast = Toast.makeText(getApplicationContext(), "Nothing Deleted !!", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 100);

            }
        });


        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(RED);
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(BLUE);

    }
    public void saveMessage(View view){   // this will call function which will list all the files present in a folder

        ShowSavedFiles();


    }

    public static void Save(File file, String[] data)
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

    public static String[] Load(File file)
    {
        FileInputStream fis = null;
        try
        {
            fis = new FileInputStream(file);
        }
        catch (FileNotFoundException e) {e.printStackTrace();}
        InputStreamReader isr = new InputStreamReader(fis);
        BufferedReader br = new BufferedReader(isr);

        String test;
        int anzahl=0;
        try
        {
            while ((test=br.readLine()) != null)
            {
                anzahl++;
            }
        }
        catch (IOException e) {e.printStackTrace();}

        try
        {
            fis.getChannel().position(0);
        }
        catch (IOException e) {e.printStackTrace();}

        String[] array = new String[anzahl];

        String line;
        int i = 0;
        try
        {
            while((line=br.readLine())!=null)
            {
                array[i] = line;
                i++;
            }
        }
        catch (IOException e) {e.printStackTrace();}
        return array;
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("WARNING !");

            alertDialogBuilder.setIcon(R.drawable.exit2);
            alertDialogBuilder.setMessage("DO U WANT TO EXIT ??");

            //alertDialogBuilder.setViewBackground(HALLOWEEN_ORANGE);
            alertDialogBuilder.setPositiveButton("EXIT",new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                   // Toast.makeText(getApplicationContext(),"EXIT SUCCESSFUL !!!",Toast.LENGTH_SHORT).show();
                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    finish();

                }
            });
            alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub

                }
            });


            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(RED);
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(GREEN);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            /*Intent set= new Intent(MainActivity.this, SettingsActivity.class) ;
            startActivity(set);*/
            final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Information!");

            alertDialogBuilder.setIcon(R.drawable.info);
            alertDialogBuilder.setMessage("This App is developed by :\nVidit Agarwal\nviditvivo@gmail.com");

            //alertDialogBuilder.setViewBackground(HALLOWEEN_ORANGE);
            alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                    // Toast.makeText(getApplicationContext(),"EXIT SUCCESSFUL !!!",Toast.LENGTH_SHORT).show();


                }
            });



            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();

            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorAccent));


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    protected void onPause()
    {
        super.onPause();
        //saveState();
    }
    protected void onResume()
    {
        super.onResume();
    }
}
