package com.rz.customadapter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private ListView listViewArticles;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Drink[] DrinkData = new Drink[12];

        DrinkData[0] = new Drink(R.drawable.icon_music_drink, "Coca Cola",
                "La mort en bouteille");

        DrinkData[1] = new Drink(R.drawable.icon_pictures_drink,
                "Oasis", "Bah Oasis quoi");

        DrinkData[2] = new Drink(R.drawable.icon_spreadsheet_drink,
                "Caraibos", "Tu connaiiiis caraibos bananaaaaa");

        DrinkData[3] = new Drink(R.drawable.tampico, "Tampico",
                "Tampiiiicoooohooohoo");

        DrinkData[4] = new Drink(R.drawable.up, "7 up",
                "Seuveuneup Zer");

        DrinkData[5] = new Drink(R.drawable.minutemaid, "Minute Maid",
                "Naan di takamtikou");

        //Juste pour dérouler
        DrinkData[6] = new Drink(R.drawable.icon_music_drink, "Coca Cola2",
                "La mort en bouteille");

        DrinkData[7] = new Drink(R.drawable.icon_pictures_drink,
                "Oasis2", "Bah Oasis quoi");

        DrinkData[8] = new Drink(R.drawable.icon_spreadsheet_drink,
                "Caraibos2", "Tu connaiiiis caraibos bananaaaaa");

        DrinkData[9] = new Drink(R.drawable.tampico, "Tampico2",
                "Tampiiiicoooohooohoo");

        DrinkData[10] = new Drink(R.drawable.up, "7 up2",
                "Seuveuneup Zer");

        DrinkData[11] = new Drink(R.drawable.minutemaid, "Minute Maid2",
                "Naan di takamtikou");

        DrinkAdapter adapter = new DrinkAdapter(this,
                R.layout.listview_item_row, DrinkData);


        listViewArticles = (ListView) findViewById(R.id.listView1);
        listViewArticles.setAdapter(adapter);


        listViewArticles.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                String listItemText = ((TextView) view
                        .findViewById(R.id.textViewDrinkDescription)).getText()
                        .toString();

                String title = ((TextView) view
                        .findViewById(R.id.textViewDrinkName)).getText()
                        .toString();

                AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
                adb.setTitle(title);
                adb.setMessage("Votre choix : "+listItemText);
                adb.setPositiveButton("Ok", null);
                adb.show();

            }
        });
    }

    /*
     * You can put this in another java file. This will hold the data elements
     * of our list item.
     */
    class Drink {

        public int drinkIcon;
        public String drinkName;
        public String drinkDescription;

        // Constructor.
        public Drink(int drinkIcon, String drinkName,
                      String drinkDescription) {

            this.drinkIcon = drinkIcon;
            this.drinkName= drinkName;
            this.drinkDescription = drinkDescription;
        }
    }
}
