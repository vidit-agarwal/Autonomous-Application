package com.hugeardor.vidit.autonomous;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
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

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.output.TaggedOutputStream;

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
import java.util.Date;
import java.util.List;

import static android.graphics.Color.BLUE;
import static android.graphics.Color.CYAN;
import static android.graphics.Color.GREEN;
import static android.graphics.Color.RED;
import static android.graphics.Color.YELLOW;
import static com.hugeardor.vidit.autonomous.R.attr.colorAccent;
import static com.hugeardor.vidit.autonomous.R.attr.layout;
import static com.hugeardor.vidit.autonomous.R.attr.toolbarId;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    Context context;
    private String rest_pref = "MyPref" ;
    public EditText editText;
    public TextView textView;
    //public ListView lv ;

    //ArrayAdapter arrayAdapter;
    private ListView lvf; // new
    private fileAdapter fad; // new
    private List<file> mfilelist; // new


    File path = new File(Environment.getExternalStorageDirectory(), "Autonomous");
    File path_backup = new File(Environment.getExternalStorageDirectory(), "Autonomous_Backup");
    String name ;
    SharedPreferences pref ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        pref= getSharedPreferences(rest_pref, MODE_PRIVATE);
        final String f_name = pref.getString("file name", "");
        if(f_name.isEmpty())
        {
        }
        else
        {

            final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle(" Pop Up!");

            alertDialogBuilder.setIcon(R.drawable.not_save);
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setMessage("File " + f_name + " was not saved!\nWant to recover it from backup ?");
            alertDialogBuilder.setPositiveButton("Recover",new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub
                        restore(f_name);
                    final Toast success = Toast.makeText(getApplicationContext() , "Successfully recovered" , Toast.LENGTH_SHORT);
                    success.show();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            success.cancel();
                        }
                    }, 350);
                    pref = getSharedPreferences(rest_pref, MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.remove("file name");//its remove name field from your SharedPreferences
                    editor.commit();
                    ShowSavedFiles();


                }
            });
            alertDialogBuilder.setNegativeButton("Don't Recover",new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // TODO Auto-generated method stub
                    dialog.dismiss();
                }
            });


            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(RED);
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(GREEN);



        }

        context = this;

        //lv = (ListView)findViewById(R.id.lv);

        lvf = (ListView)findViewById(R.id.lv);

        editText = (EditText) findViewById(R.id.editView);
        editText.setVisibility(View.GONE);
        textView = (TextView) findViewById(R.id.textView);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

      // FloatingActionButton fab1 = (FloatingActionButton)findViewById(R.id.fab1) ;
        FloatingActionButton trash = (FloatingActionButton)findViewById(R.id.trash) ;

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if(!path.exists())
        {
          path.mkdirs() ;
        }
        ShowSavedFiles();

    }


    public void restore(String f_name)
    {
        // this function will be called when the edit class activity get destroyed and data is in back up . so make that data save in original file also .

        File rest = new File(path_backup.toString()); //source path
       //File rest_to = new File(path.toString()) ; // destination path

        for (File file_rest : rest.listFiles()) {

            if (file_rest.getName().equals(f_name))
            {

                try{


                    File f = new File(path.toString()) ;

                    for(File file: f.listFiles()) {
                        if(file.getName().equals(f_name)){

                        File dfl = new File(path, file.getName());
                        boolean del = dfl.delete();
                        if(del){
                            String sourcePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous_Backup/"+file_rest.getName();
                            File src = new File(sourcePath);
                            String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous/" + f_name;
                            File des = new File(destinationPath);
                            FileUtils.copyFile(src, des);
                        }
                        else
                        {
                            final Toast error = Toast.makeText(getApplicationContext(), "Error in Recovering" , Toast.LENGTH_SHORT);
                            error.show();
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    error.cancel();
                                }
                            }, 200);
                        }

                        }
                        else
                        {
                            String sourcePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous_Backup/"+f_name;
                            File src = new File(sourcePath);
                            String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous/" +f_name;
                            File des = new File(destinationPath);
                            FileUtils.copyFile(src, des);
                        }


                    }




                }
                catch(IOException e)
                {
                    /*Toast error = Toast.makeText(getApplicationContext() , "Error in Copying" , Toast.LENGTH_SHORT);
                    error.show();
*/                  e.printStackTrace();
                }


            }
            else {
               final  Toast toast = Toast.makeText(getApplicationContext(), "File not found in backup", Toast.LENGTH_SHORT);
                toast.show();
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toast.cancel();
                    }
                }, 200);

            }
        }

    }

   public void trash(View view)
    {
        final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Trash !");

        alertDialogBuilder.setIcon(R.drawable.del1);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setMessage("Delete all files & backups");

        //alertDialogBuilder.setViewBackground(HALLOWEEN_ORANGE);
        alertDialogBuilder.setPositiveButton("Delete",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                // TODO Auto-generated method stub
                File f = new File(path.toString()) ;

                for(File file: f.listFiles()) {

                    File dfl = new File(path, file.getName());
                    boolean del = dfl.delete();

                }
                File f1 = new File(path_backup.toString()) ;

                for(File file: f1.listFiles()) {

                    File dfl = new File(path_backup, file.getName());
                    boolean del = dfl.delete();

                }


                ShowSavedFiles();

            }
        });
        alertDialogBuilder.setNegativeButton("CANCEL",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                    dialog.dismiss();
            }
        });


        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(RED);
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(GREEN);




    }


    public void writeMessage(View view){     //this will create a file in internal stoage in a folder

        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);

        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Save",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                // get user input and set it to result
                                // edit text
                                name = userInput.getText().toString();
                                File file = new File(path, name  +".txt") ;
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
                                ShowSavedFiles();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int id) {
                                dialog.dismiss();
                            }
                        });


        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(RED);
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(GREEN);


    }
     public void ShowSavedFiles() { //this will list all the files present in a folder in internal storage into a list view


         mfilelist = new ArrayList<>();

       // ArrayList fileList = new ArrayList();
        File f = new File(path.toString());

             if(f.listFiles().length!=0){
                for (File file : f.listFiles()) {
                   // Date lastModified = new Date(file.lastModified());
                    //fileList.add(file.getName());
                    String fd = DateFormat.format("dd/MM/yyyy , h:mm:ss aa", System.currentTimeMillis()).toString();
                   mfilelist.add(new file(file.getName(), fd));
                   // fileList.add(lastModified);
                    //fileList.add(file.getPath()) ;
                }}
         else {
                 final Toast toast = Toast.makeText(getApplicationContext(), "Nothing To Show !" , Toast.LENGTH_SHORT);
                 toast.show();
                 Handler handler = new Handler();
                 handler.postDelayed(new Runnable() {
                     @Override
                     public void run() {
                         toast.cancel();
                     }
                 }, 400);
             }


         fad = new fileAdapter(getApplicationContext(), mfilelist);
         lvf.setAdapter(fad);
            lvf.setClickable(true);
            lvf.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    String selectfromlist =((TextView)view.findViewById(R.id.file_name)).getText().toString();



                    //String selectfromlist = (String) lvf.getItemAtPosition(position);
                    Intent pass = new Intent(MainActivity.this, Editclass.class);
                    pass.putExtra("item" , selectfromlist);
                    startActivity(pass);
                    /*
                   Toast toast = Toast.makeText(getApplicationContext(), selected , Toast.LENGTH_SHORT);
                    toast.show();
*/
                }
            });
            lvf.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                    String selectfromlist =((TextView)view.findViewById(R.id.file_name)).getText().toString();

                    alert_dialog(selectfromlist);

                    return true;
                }

            });

        }
    public void alert_dialog(final String s)   //this dialog is for deletion of a file from the folder
    {
        final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Delete!");

        alertDialogBuilder.setIcon(R.drawable.del);
        alertDialogBuilder.setMessage("Sure , you want to delete ? !");
        alertDialogBuilder.setCancelable(false);
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

        alertDialogBuilder.setNeutralButton("Delete All",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

              confirmation();

            }
        });


        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(RED);
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(BLUE);
        alertDialog.getButton(alertDialog.BUTTON_NEUTRAL).setTextColor(CYAN);

    }
    public void confirmation()
    {
        final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
        alertDialogBuilder.setTitle("Delete All!");

        alertDialogBuilder.setIcon(R.drawable.del);
        alertDialogBuilder.setMessage("Delete all files ? !");
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener()
        {

            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                File f = new File(path.toString()) ;

                for(File file: f.listFiles()) {

                    File dfl = new File(path, file.getName());
                    boolean del = dfl.delete();

                }
                ShowSavedFiles();

            }
        });
        alertDialogBuilder.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });




        AlertDialog alertDialog=alertDialogBuilder.create();
        alertDialog.show();

        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(RED);
        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(BLUE);


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

    public void restore_all()
    {
        // this function will be called when the restore button in drwyer is called

        File rest = new File(path_backup.toString()); //source path
        File rest_to = new File(path.toString()) ;   // destination path

      if(rest.listFiles().length!=0)
      {
          for (File file_rest : rest.listFiles()) {
              if (rest_to.listFiles().length != 0) {


                  for (File file_rest_to : rest_to.listFiles()) {

                      try {
                          if (file_rest.getName().equals(file_rest_to.getName())) {



                              File dfl = new File(path, file_rest_to.getName());
                              boolean del = dfl.delete();
                              if (del) {
                                  String sourcePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous_Backup/" + file_rest.getName();
                                  File src = new File(sourcePath);
                                  String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous/" + file_rest.getName();
                                  File des = new File(destinationPath);
                                  FileUtils.copyFile(src, des);
                              } else {
                                  final Toast error = Toast.makeText(getApplicationContext(), "Error in Restoring", Toast.LENGTH_SHORT);
                                  error.show();
                                  Handler handler = new Handler();
                                  handler.postDelayed(new Runnable() {
                                      @Override
                                      public void run() {
                                          error.cancel();
                                      }
                                  }, 100);
                              }



                          }  //
                          else {
                              String sourcePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous_Backup/" + file_rest.getName();
                              File src = new File(sourcePath);
                              String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous/" + file_rest.getName();
                              File des = new File(destinationPath);
                              FileUtils.copyFile(src, des);
                          }


                      } catch (IOException e) {

                          e.printStackTrace();
                      }
                  }
              }
              else {

                  try{ String sourcePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous_Backup/" + file_rest.getName();
                      File src = new File(sourcePath);
                      String destinationPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/Autonomous/" + file_rest.getName();
                      File des = new File(destinationPath);
                      FileUtils.copyFile(src, des);}
                  catch(IOException e)
                  {
                      e.printStackTrace();
                  }
              }

          }
          final Toast show = Toast.makeText(getApplicationContext() , "All files in backup are restored to Main Directory", Toast.LENGTH_SHORT);
          show.show();
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                  show.cancel();
              }
          }, 200);
      }
      else
      {
          final Toast toast = Toast.makeText(getApplicationContext(), "No files in backup !" , Toast.LENGTH_SHORT);
          toast.show();
          Handler handler = new Handler();
          handler.postDelayed(new Runnable() {
              @Override
              public void run() {
                  toast.cancel();
              }
          }, 200);
      }

}





    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        else {
            final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Warning !");

            alertDialogBuilder.setIcon(R.drawable.exit2);
            alertDialogBuilder.setMessage("Do you want to exit ??");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Exit",new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub

                    Intent startMain = new Intent(Intent.ACTION_MAIN);
                    startMain.addCategory(Intent.CATEGORY_HOME);
                    startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(startMain);
                    finish();

                }
            });
            alertDialogBuilder.setNegativeButton("Don't Exit",new DialogInterface.OnClickListener() {

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

            final AlertDialog.Builder alertDialogBuilder=new AlertDialog.Builder(MainActivity.this);
            alertDialogBuilder.setTitle("Information!");

            alertDialogBuilder.setIcon(R.drawable.info);
            alertDialogBuilder.setMessage("This App is developed by :\nVidit Agarwal\nviditvivo@gmail.com");
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton("Ok",new DialogInterface.OnClickListener()
            {

                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    // TODO Auto-generated method stub


                }
            });



            AlertDialog alertDialog=alertDialogBuilder.create();
            alertDialog.show();

            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(BLUE);


        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

       /* if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

*/
       if(id == R.id.nav_rest)
       {
           restore_all();

           ShowSavedFiles();

       }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    protected void onPause()
    {
        super.onPause();
    }
    @Override

    protected void onResume()
    {
        super.onResume();
    }
    @Override
    protected  void onStop()
    {
        super.onStop();
    }


}
