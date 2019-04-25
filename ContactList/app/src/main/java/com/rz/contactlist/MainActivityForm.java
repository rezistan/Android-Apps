package com.rz.contactlist;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.rz.contactlist.MainActivity.ContactData;

public class MainActivityForm extends AppCompatActivity {
    public String fileName = "contactSave";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.form);

        //Gestion du Toast
        Context context = getApplicationContext();
        CharSequence text = getString(R.string.thanks);
        int duration = Toast.LENGTH_SHORT;

        final Toast toast = Toast.makeText(context, text, duration);

        //Toasts en cas d'erreur
        CharSequence notFilledText = getString(R.string.fillAll);
        final Toast errorCheck = Toast.makeText(context, notFilledText, duration);

        CharSequence errMail = getString(R.string.errorMail);
        final Toast errorMail = Toast.makeText(context, errMail, duration);

        CharSequence errPhone = getString(R.string.errorPhone);
        final Toast errorPhone = Toast.makeText(context, errPhone, duration);

        CharSequence errPostCode = getString(R.string.errorPostCode);
        final Toast errorPostCode = Toast.makeText(context, errPostCode, duration);

        //Action du bouton valider
        final Button ok = (Button) findViewById(R.id.valider);
        ok.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                hideSoftKeyboard(MainActivityForm.this);
                if (!checkFilled()) {
                    errorCheck.show();
                    final EditText name = (EditText)findViewById(R.id.name);
                    name.setError(getString(R.string.fillAll));
                }
                else if(!checkMail()){
                    errorMail.show();
                }
                else if(!checkPhone()){
                    errorPhone.show();
                }
                else if(!checkPostCode()){
                    errorPostCode.show();
                }
                else {
                    MainActivity.Contact newZer = recupInfos();

                    FileOutputStream fos = null;
                    ObjectOutputStream out = null;
                    try{
                        for(int i = 0; i < 100; i++) {
                            String chemin = fileName + i + ".ser";
                            File fich = new File(getFilesDir(), chemin);
                            if(!fich.exists()){
                                newZer.thisFileName = chemin;
                                fos = new FileOutputStream(new File(getFilesDir(), chemin), true);
                                out = new ObjectOutputStream(fos);
                                out.writeObject(newZer);
                                out.close();
                                fos.close();
                                i = 100;
                            }
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    finish();
                    Intent evtAll = new Intent(MainActivityForm.this, MainActivity.class);
                    startActivity(evtAll);

                    toast.show();
                }
            }
        });

    }

    private boolean checkMail(){
        final EditText mail = (EditText)findViewById(R.id.mailAddr);
        String email = mail.getText().toString();
        if(email.isEmpty()){
            return true;
        }

        final String EMAIL_PATTERN = "^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
        Pattern pattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);

        return matcher.matches();
    }

    private boolean checkPhone(){
        final EditText phone = (EditText)findViewById(R.id.phone);
        String num = phone.getText().toString();

        if (num.equals("") || ((num.length() >= 7) && (num.length() < 15))){
            return true;
        }
        return false;
    }

    private boolean checkPostCode(){
        final EditText postCode = (EditText)findViewById(R.id.postCode);
        String codePostal = postCode.getText().toString();

        if (codePostal.equals("") || codePostal.length() == 5){
            return true;
        }
        return false;
    }

    private boolean checkFilled(){
        final EditText name = (EditText)findViewById(R.id.name);
        String prenom = name.getText().toString();

        if(prenom.equals("")){
            return false;
        }

        return true;
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    private MainActivity.Contact recupInfos(){
        MainActivity.Contact boubess = new MainActivity.Contact();

        final EditText name = (EditText)findViewById(R.id.name);
        boubess.contactName = name.getText().toString();

        final EditText phone = (EditText)findViewById(R.id.phone);
        boubess.contactPhone = phone.getText().toString();

        final EditText mail = (EditText)findViewById(R.id.mailAddr);
        boubess.email = mail.getText().toString();

        final EditText address = (EditText)findViewById(R.id.address);
        boubess.address = address.getText().toString();

        final EditText postCode = (EditText)findViewById(R.id.postCode);
        boubess.postCode = postCode.getText().toString();

        boubess.contactIcon = R.drawable.up;

        return boubess;
    }


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

}
