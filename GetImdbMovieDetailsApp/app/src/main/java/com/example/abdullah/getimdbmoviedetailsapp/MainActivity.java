package com.example.abdullah.getimdbmoviedetailsapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.ParcelFileDescriptor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import static android.R.attr.key;
import static com.example.abdullah.getimdbmoviedetailsapp.R.id.listViewMovie;

public class MainActivity extends AppCompatActivity {

    private String restURL = "";
    private ArrayList<aMovie> myMovies = new ArrayList<aMovie>();

    Button searchButton, displayButton, saveButton;
    TextView textViewResult;
    EditText editText1;
    String jsonData = "";
    String allData = "";

    EditText eTextView;
    private static final int SAVE_REQUEST_CODE = 42;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        saveButton = (Button) findViewById(R.id.saveButton);
        searchButton = (Button) findViewById(R.id.searchButton);
        displayButton = (Button) findViewById(R.id.displayButton);
        editText1 = (EditText) findViewById(R.id.editText1);
        textViewResult = (TextView) findViewById(R.id.textViewId);

        saveButton.setVisibility(View.INVISIBLE);
        displayButton.setVisibility(View.INVISIBLE);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call search function
                String str = editText1.getText().toString();
                searchMovie(str);

                saveButton.setVisibility(View.VISIBLE);
                displayButton.setVisibility(View.VISIBLE);
            }
        });

        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Call display function
                displayMovieList();
            }
        });

    }

    private String parseJsonData(String result) {

        String data = "";
        try {

            JSONObject jsonRootObject = new JSONObject(result);

            String id = jsonRootObject.optString("imdbID").toString();
            String name = jsonRootObject.optString("Title").toString();
            String year = jsonRootObject.optString("Year").toString();
            String director = jsonRootObject.optString("Director").toString();
            String rating = jsonRootObject.optString("imdbRating").toString();
            String res = jsonRootObject.optString("Response").toString();
            String err = jsonRootObject.optString("Error").toString();
            if (res.equals("False")) {
                data +="Error: " + err +"\n";
            } else {
                data += "Found: \n imdb ID=" + id + "\n Title=" + name + "\n Year=" + year;
                data += "\n Director=" + director + "\n Rating=" + rating + "\n";
                //Add the movie to the ArrayList of movies
                myMovies.add(new aMovie(id, name, year, director, rating));
            }

        }
        catch (JSONException e) {
            e.printStackTrace();
        }

        return data;

    }

    private void searchMovie(String str) {

        //Split on comma
        String[] strArr = new String[10];
        strArr = str.split(",");

        //Call REST Api for each movie and process the data
        for (String str1 : str.split(",")) {

            String nm = "";
            int j = 0;
            int len = str1.split(" ").length;
            for (String str2 : str1.split(" ")) {
                if (j == len - 1) {
                    nm = nm + str2;
                } else {
                    nm = nm + str2 + "+";
                }
                j += 1;
            }
            //Create the url as per the movie name
            restURL = "http://www.omdbapi.com/?t=" + nm + "&y=&plot=short&r=json";

            ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
            if (networkInfo != null && networkInfo.isConnected()) {
                //Create new Task! (in new Thread)
                new DownloadMovieDetails().execute(restURL);
            } else {
                String str3 = "No network connection available";
                textViewResult.setText(str3);
            }

        }

        editText1.setText("");
    }

    private class DownloadMovieDetails extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            jsonData = downloadFromURL(urls[0]);
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            //super.onPostExecute(s);
            textViewResult.setText(parseJsonData(s));
        }
    }

    private String downloadFromURL(String url) {

        InputStream is = null;
        StringBuffer result = new StringBuffer();
        try {
            URL myUrl = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) myUrl.openConnection();
            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            //Log.d("MOVIE_SEARCH", "The response is: " + response);
            is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String line = "";
            while((line = reader.readLine()) != null) {
                result.append(line);
            }
        }
        catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        catch (ProtocolException e) {
            e.printStackTrace();
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    private void displayMovieList() {

        textViewResult.setText("");
        ArrayAdapter<aMovie> myAdapter = new MyListAdapter();
        ListView myListView = (ListView) findViewById(listViewMovie);
        myListView.setAdapter(myAdapter);

    }

    private class MyListAdapter extends ArrayAdapter<aMovie> {

        public MyListAdapter() {
            super(MainActivity.this, R.layout.movie_layout, myMovies);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.movie_layout, parent, false);
            }

            aMovie currentMovie = myMovies.get(position);
            TextView viewTitle = (TextView) itemView.findViewById(R.id.textViewTitle);
            viewTitle.setText(currentMovie.getTitle());
            TextView viewYear = (TextView) itemView.findViewById(R.id.textViewYear);
            viewYear.setText(currentMovie.getYear());
            TextView viewDirector = (TextView) itemView.findViewById(R.id.textViewDir);
            viewDirector.setText(currentMovie.getDirector());
            TextView viewRating = (TextView) itemView.findViewById(R.id.textViewRating);
            viewRating.setText(currentMovie.getRating());

            return itemView;

        }

    }

    public void createSaveFile(View view) {

        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE, "movieFile.txt");
        startActivityForResult(intent, SAVE_REQUEST_CODE);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Uri currentUri = null;

        if (resultCode == Activity.RESULT_OK) {

            if (requestCode == SAVE_REQUEST_CODE) {

                if(data != null) {
                    currentUri = data.getData();
                    writeFileContents(currentUri);
                }
            }
        }
    }

    private void writeFileContents(Uri currentUri) {

        try {

            ParcelFileDescriptor pfd = this.getContentResolver().openFileDescriptor(currentUri, "w");
            FileOutputStream fos = new FileOutputStream(pfd.getFileDescriptor());
            String data = "";
            for(aMovie obj: myMovies) {
                String name = obj.getTitle();
                String year = obj.getYear();
                String director = obj.getDirector();
                String rating = obj.getRating();
                data += "imdb ID="+key+"\nTitle="+name+"\nYear="+ year+"\nDirector="+ director+"\nRating="+ rating+"\n";
                data +="\n";
            }
            fos.write(data.getBytes());
            fos.close();
            pfd.close();
            Toast.makeText(this, "Movie File Written!!!", Toast.LENGTH_SHORT).show();

        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

}
