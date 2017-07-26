package fr.gauthierperea.spherewheather;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.content.ContextCompat;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONObject;


public class First extends AppCompatActivity {
    boolean celciusAct = true;
    boolean farenheitAct = false;
    boolean kelvinAct = false;

    double tempCel = 22;
    double tempFar = 0;
    double tempKel = 0;
    String temp = "Cel";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        final EditText villeVal = (EditText) findViewById(R.id.insert_ville);
        final EditText nomVal = (EditText) findViewById(R.id.insert_nom);
        final Button valid = (Button) findViewById(R.id.btn_valid);
        //Ecouteur bouton de validation du formulaire

        valid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setContentView(R.layout.activity_weather);
                final Button celcius = (Button) findViewById(R.id.btn_celcius);
                final Button farenheit = (Button) findViewById(R.id.btn_farenheit);
                final Button kelvin = (Button) findViewById(R.id.btn_kelvin);
                final TextView degre_text = (TextView) findViewById(R.id.degre);
                final String api_url = "http://api.openweathermap.org/data/2.5/weather?q="+ villeVal.getText() +"&appid=591a82496f056b1939408ade0da061d0";
                getTemp(api_url, degre_text, villeVal, nomVal, temp);
                celcius.setOnClickListener(new View.OnClickListener() {
                       @Override
                       public void onClick(View v) {
                           if (!celciusAct) {
                               celcius.setBackgroundColor(ContextCompat.getColor(First.this, R.color.colorAccent));
                               kelvin.setBackgroundColor(ContextCompat.getColor(First.this, R.color.background));
                               farenheit.setBackgroundColor(ContextCompat.getColor(First.this, R.color.background));
                               getTemp(api_url, degre_text, villeVal, nomVal, temp);
                               kelvinAct = false;
                               celciusAct = true;
                               farenheitAct = false;
                               temp = "cel";
                               degre_text.setText(tempKel + " °C");
                           }
                       }
                   });
                //Ecouteur bouton menu 2 (Farenheit)
                farenheit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!farenheitAct) {
                            farenheit.setBackgroundColor(ContextCompat.getColor(First.this, R.color.colorAccent));
                            celcius.setBackgroundColor(ContextCompat.getColor(First.this, R.color.background));
                            kelvin.setBackgroundColor(ContextCompat.getColor(First.this, R.color.background));
                            getTemp(api_url, degre_text, villeVal, nomVal, temp);
                            farenheitAct = true;
                            celciusAct = false;
                            kelvinAct = false;
                            temp = "far";
                            degre_text.setText(tempFar + "°F");
                        }
                    }
                });

                //Ecouteur bouton menu 3 (Kelvin)
                kelvin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!kelvinAct) {
                            kelvin.setBackgroundColor(ContextCompat.getColor(First.this, R.color.colorAccent));
                            celcius.setBackgroundColor(ContextCompat.getColor(First.this, R.color.background));
                            farenheit.setBackgroundColor(ContextCompat.getColor(First.this, R.color.background));
                            getTemp(api_url, degre_text, villeVal, nomVal, temp);
                            kelvinAct = true;
                            celciusAct = false;
                            farenheitAct = false;
                            temp = "kel";
                            degre_text.setText(tempKel + " K");
                        }
                    }
                });
            }
        });

    }

    public void getTemp(final String api_url,
                          final TextView degre_text,
                          final EditText villeVal,
                          final EditText nomVal,
                          final String temp) {
        final TextView textWeather = (TextView) findViewById(R.id.text_weather);
        final TextView title = (TextView) findViewById(R.id.title);

                try {
                    double res = 0;
                    System.out.println("try");
                    URL url = new URL(api_url);

                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.connect();
                    InputStream inputStream = connection.getInputStream();
                    String result = InputStreamOperations.InputStreamToString(inputStream);

                    // On récupère le JSON complet
                    JSONObject jsonObject = new JSONObject(result);
                    JSONObject info = jsonObject.getJSONObject("main");

                    double temperature = Double.parseDouble(info.getString("temp"));
                    double tempConvert = convertTemp(temperature);
                    System.out.println(convertTemp(temperature));

                    String degrees = (tempCel + "°C");

                    degre_text.setText(degrees);

                    String temp_text = "";

                    if (tempCel <= 20 && tempCel >= 10) {
                        temp_text = "il fait bon à " + villeVal.getText() + " mais sortez tout de même couvert";
                    } else if (tempCel > 30) {
                        temp_text = "il fait vraiment très chaud à " + villeVal.getText() + "pensez à vous hydrater";
                    } else if (tempCel <= 10 && tempCel > 5) {
                        temp_text = "La température est fraiche" + villeVal.getText() + "habillez vous chaudement";
                    } else if (tempCel < 5) {
                        temp_text = "Winter is coming";
                    }

                    title.setText("Bonjour" + nomVal.getText() + " ! ");
                    textWeather.setText(temp_text);

                } catch (Exception e) {
                    e.printStackTrace();
                }
        };

        public double convertTemp(double temperature){

            double convertTemperature = 0.0;
            switch(temp){
                case "far":
                    convertTemperature = temperature * 1.8 - 459.7;
                    break;
                case "cel":
                    convertTemperature = temperature - 273.15;
                    break;
                case "kel":
                    convertTemperature = temperature * 1.8 - 459.7;
                    break;
            }
            return convertTemperature;
        };

}

