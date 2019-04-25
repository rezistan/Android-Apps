package com.rz.fluxmeteo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Button time = (Button) findViewById(R.id.date);

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHeure heure = new getHeure();

                String toShow = "";
                try {
                    toShow = heure.execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                TextView monHeure = (TextView) findViewById(R.id.textView);
                monHeure.setText(toShow);
            }
        });

        final Button google = (Button) findViewById(R.id.google);
        final WebView minipage = (WebView) findViewById(R.id.webview);
        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minipage.loadUrl("http://www.google.sn/");
                minipage.setWebViewClient(new WebViewClient());
            }
        });

        final Button texthtml = (Button) findViewById(R.id.html);
        texthtml.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strHtml = "<html><body><b> Ceci est un texte au format HTML </b></br>qui s’affiche très simplement</body></html>";
                minipage.loadData(strHtml , "text/html; charset=utf-8", "UTF-8");
            }
        });

        final Button urlmeteo = (Button) findViewById(R.id.meteo_url);
        urlmeteo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                minipage.loadUrl("http://api.meteorologic.net/forecarss?p=Lyon");
            }
        });


    }
}
