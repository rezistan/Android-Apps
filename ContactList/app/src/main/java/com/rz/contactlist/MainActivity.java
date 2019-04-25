package com.rz.contactlist;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ContactAdapter adapter;
    private ListView listView;
    private Contact personne = null;
    public String fileName = "contactSave";
    private int backButtonCount = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onBackPressed()
    {
        if(backButtonCount >= 1)
        {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        else
        {
            Toast.makeText(this, "Press the back button once again to close the application.", Toast.LENGTH_SHORT).show();
            backButtonCount++;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent evtAll = new Intent(MainActivity.this, MainActivityForm.class);
            startActivity(evtAll);

            backButtonCount = 0;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private ListView listViewArticles;

    static ArrayList<Contact> ContactData = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent evtAll = new Intent(MainActivity.this, MainActivityForm.class);
                startActivity(evtAll);
                backButtonCount = 0;
            }
        });

        ContactData = getListContacts();

        final ContactAdapter adapter = new ContactAdapter(this,
                R.layout.listview_item_row, ContactData);


        listViewArticles = (ListView) findViewById(R.id.listView1);
        listViewArticles.setAdapter(adapter);


        listViewArticles.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                Contact currentContact = ContactData.get(position);

                String listItemText = ((TextView) view
                        .findViewById(R.id.textViewContactPhone)).getText()
                        .toString();

                String title = ((TextView) view
                        .findViewById(R.id.textViewContactName)).getText()
                        .toString();

                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle(title);
                adb.setMessage(recupText(currentContact));
                adb.setPositiveButton("Ok", null);
                adb.show();
            }
        });

        listViewArticles.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Contact currentContact = ContactData.get(i);

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                //Yes button clicked
                                supprContact(currentContact);

                                //Refreshes the contact list after deleting
                                adapter.notifyDataSetChanged();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage(R.string.confirmation).setPositiveButton("Oui", dialogClickListener)
                        .setNegativeButton("Non", dialogClickListener).show();
                return true;
            }
        });
    }

    public void supprContact(com.rz.contactlist.MainActivity.Contact contact){
        ContactData.remove(contact);
        deleteFileC(contact.thisFileName);
    }

    private boolean deleteFileC(String filename) {
        File dir = getFilesDir();
        File file = new File(dir, filename);
        boolean deleted = file.delete();
        return deleted;
    }

    public ArrayList<Contact> getListContacts(){
        ArrayList<Contact> listContacts = new ArrayList<>();
        FileInputStream fis = null;
        ObjectInputStream in = null;
        try{
            for(int i = 0; i < 100; i++) {
                File fich = new File(getFilesDir(), fileName + i + ".ser");
                if(!fich.exists()){
                }else{
                    fis = new FileInputStream(new File(getFilesDir(), fileName + i + ".ser"));
                    System.out.println(getFilesDir());
                    in = new ObjectInputStream(fis);
                    personne = (Contact) in.readObject();
                    listContacts.add(personne);
                    in.close();
                    fis.close();
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return listContacts;
    }

    private String recupText(Contact contact){
        String contenuDialog = "";

        String num = contact.contactPhone;
        if(!num.isEmpty()){
            contenuDialog += getString(R.string.num)+" : "+num+"\n";
        }

        String email = contact.email;
        if(!email.isEmpty()){
            contenuDialog += getString(R.string.mail)+" : "+email+"\n";
        }

        String adresse = contact.address;
        if(!adresse.isEmpty()) {
            contenuDialog += getString(R.string.address) + " : " + adresse + "\n";
        }

        String codePostal = contact.postCode;
        if(!codePostal.isEmpty()){
            contenuDialog += getString(R.string.postCode)+" : "+codePostal+"\n";
        }

        return contenuDialog;
    }

    /*
     * You can put this in another java file. This will hold the data elements
     * of our list item.
     */
    public static class Contact implements Serializable{

        public int contactIcon;
        public String contactName;
        public String contactPhone;
        public String email;
        public String address;
        public String postCode;
        public String thisFileName;

        // Constructor.
        public Contact(){
            
        }

    }

}
