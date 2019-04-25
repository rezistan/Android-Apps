package com.rz.form_h;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                hideSoftKeyboard(MainActivity.this);
                if (!checkFilled()) {
                    errorCheck.show();
                    final EditText name = (EditText)findViewById(R.id.name);
                    name.setError(getString(R.string.fillAll));
                    final EditText mail = (EditText)findViewById(R.id.mailAddr);
                    mail.setError(getString(R.string.fillAll));
                    final EditText surname = (EditText)findViewById(R.id.surname);
                    surname.setError(getString(R.string.fillAll));
                    final EditText date = (EditText)findViewById(R.id.birthday);
                    date.setError(getString(R.string.fillAll));
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
                    toast.show();
                    showDialogInfos();
                }
            }
        });

        //Gestion du DatePicker
        final Calendar myCalendar = Calendar.getInstance();

        final EditText dateNaissance = (EditText) findViewById(R.id.birthday);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener(){
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth){
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

            private void updateLabel(){
                String myFormat = getString(R.string.dateFormat);
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

                dateNaissance.setText(sdf.format(myCalendar.getTime()));
            }
        };

        dateNaissance.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(MainActivity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        //Changement d'avatar
        final ImageButton img = (ImageButton) findViewById(R.id.avatar);
        img.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                selectAvatar();
            }
        });

    }

    private void showDialogInfos(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        String contenuDialog = recupText();

        // 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage(contenuDialog)
                .setTitle(R.string.dialog_title);

        // 3. Get the AlertDialog from create()
        final AlertDialog dialog = builder.create();

        dialog.show();
    }

    private void selectAvatar() {// custom dialog
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.radiobutton_dialog);
        List<String> stringList=new ArrayList<>();  // here is list
        stringList.add(getString(R.string.roundAvatar));
        stringList.add(getString(R.string.squareAvatar));

        RadioGroup rg = (RadioGroup) dialog.findViewById(R.id.radio_group);

        for(int i=0;i<stringList.size();i++){
            RadioButton rb=new RadioButton(MainActivity.this); // dynamically creating RadioButton and adding to RadioGroup.
            rb.setText(stringList.get(i));
            rg.addView(rb);
        }

        dialog.show();

        //Traitement de la selection d'avatar
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            final ImageButton img = (ImageButton) findViewById(R.id.avatar);
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int childCount = group.getChildCount();
                for (int x = 0; x < childCount; x++) {
                    RadioButton btn = (RadioButton) group.getChildAt(x);
                    if (btn.getId() == checkedId) {
                        String choix = btn.getText().toString();
                        if (choix.equals(getString(R.string.squareAvatar))) {
                            img.setImageResource(R.mipmap.ic_launcher);
                            dialog.hide();
                        }
                        else if (choix.equals(getString(R.string.roundAvatar))) {
                            img.setImageResource(R.mipmap.ic_launcher_round);
                            dialog.hide();
                        }
                    }
                }
            }
        });
    }

    private boolean checkMail(){
        final EditText mail = (EditText)findViewById(R.id.mailAddr);
        String email = mail.getText().toString();

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
        final RadioButton man = (RadioButton)findViewById(R.id.man);
        final RadioButton woman = (RadioButton)findViewById(R.id.woman);

        if(!man.isChecked() && !woman.isChecked() ){
            return false;
        }

        final EditText name = (EditText)findViewById(R.id.name);
        String prenom = name.getText().toString();

        final EditText surname = (EditText)findViewById(R.id.surname);
        String nom = surname.getText().toString();

        final EditText birthday = (EditText)findViewById(R.id.birthday);
        String date = birthday.getText().toString();

        if(prenom.equals("") || nom.equals("") || date.equals("")){
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

    private String recupText(){
        String contenuDialog = "";

        final RadioButton man = (RadioButton)findViewById(R.id.man);
        final RadioButton woman = (RadioButton)findViewById(R.id.woman);

        if(man.isChecked()){
            contenuDialog = getString(R.string.issaMan)+"\n\n";
        }
        else if(woman.isChecked()){
            contenuDialog = getString(R.string.issaWoman)+"\n\n";
        }

        final EditText name = (EditText)findViewById(R.id.name);
        String prenom = name.getText().toString();
        contenuDialog += getString(R.string.name)+" : "+prenom+"\n";

        final EditText surname = (EditText)findViewById(R.id.surname);
        String nom = surname.getText().toString();
        contenuDialog += getString(R.string.surname)+" : "+nom+"\n";

        final EditText birthday = (EditText)findViewById(R.id.birthday);
        String date = birthday.getText().toString();
        contenuDialog += getString(R.string.birthday)+" : "+date+"\n";

        final EditText phone = (EditText)findViewById(R.id.phone);
        String num = phone.getText().toString();
        if(!num.isEmpty()){
            contenuDialog += getString(R.string.phone)+" : "+num+"\n";
        }

        final EditText mail = (EditText)findViewById(R.id.mailAddr);
        String email = mail.getText().toString();
        contenuDialog += getString(R.string.mail)+" : "+email+"\n";

        final EditText address = (EditText)findViewById(R.id.address);
        String adresse = address.getText().toString();
        if(!adresse.isEmpty()) {
            contenuDialog += getString(R.string.address) + " : " + adresse + "\n";
        }
        final EditText postCode = (EditText)findViewById(R.id.postCode);
        String codePostal = postCode.getText().toString();
        if(!codePostal.isEmpty()){
            contenuDialog += getString(R.string.postCode)+" : "+codePostal+"\n";
        }

        return contenuDialog;
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
