package ln.mapapi.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import ln.mapapi.R;
import ln.mapapi.api.ApiFactory;
import ln.mapapi.request.DataRequest;
import ln.mapapi.response.DataResponse;
import ln.mapapi.service.DataService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    DataRequest dataRequest;
    DataResponse dataResponse;
    ProgressDialog loading;

    private TextView txtLongName;
    private EditText edAddress;
    private Button btnGetResult;
    private FloatingActionButton fabMap;

    public String lat = null;
    public String lng = null;
    public String placeName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        dataRequest = new DataRequest();
        dataResponse = new DataResponse();

        fabMap.setVisibility(View.INVISIBLE);

        btnGetResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (edAddress.getText().toString().equals("") ||
                        edAddress.getText().toString().equals(null) ||
                        edAddress.getText().toString().length() == 0)
                {

                    showToast("Please enter address");

                }
                else {
                    Log.d("abc","else part");

                    loading = ProgressDialog.show(MainActivity.this,"Fetching Data","Please wait...",false,false);

                    DataService dataService = ApiFactory.createService(DataService.class);

                    Call<JsonObject> dataRequestCall = dataService.getResult(edAddress.getText().toString());

                    dataRequestCall.enqueue(new Callback<JsonObject>() {
                        @Override
                        public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {

                            loading.dismiss();

                            Log.d("abc","on Response");

                            if (response.isSuccessful()) {

                                fabMap.setVisibility(View.VISIBLE);

                                Log.e("Response",response.body().toString());

                                dataResponse = new Gson().fromJson(response.body(),DataResponse.class);

                                lat = dataResponse.getResults().get(0).getGeometry().getLocation().getLat().toString();

                                lng = dataResponse.getResults().get(0).getGeometry().getLocation().getLng().toString();

                                placeName = dataResponse.getResults().get(0).getAddressComponents().get(0).getLongName();

                                txtLongName.setText(dataResponse.getResults().get(0).getAddressComponents().get(2).getLongName());

                            } else {
                                showToast("Please try again");
                            }

                        }

                        @Override
                        public void onFailure(Call<JsonObject> call, Throwable t) {
                            Log.d("abc","onFailure");
                            loading.dismiss();

                        }
                    });

                }

            }
        });

        fabMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showMap(lat,lng,placeName);

            }
        });

    }

    public void initViews()
    {
        txtLongName = (TextView) findViewById(R.id.txt_long_name);
        btnGetResult = (Button) findViewById(R.id.btn_get_result);
        edAddress = (EditText) findViewById(R.id.ed_address);
        fabMap = (FloatingActionButton) findViewById(R.id.fab_map);
    }

    protected void showMap(String lati, String longi, String place) {

        boolean installedMaps = false;

        // CHECK IF GOOGLE MAPS IS INSTALLED
        PackageManager pkManager = getPackageManager();
        try {
            @SuppressWarnings("unused")
            PackageInfo pkInfo = pkManager.getPackageInfo("com.google.android.apps.maps", 0);
            installedMaps = true;
        } catch (Exception e) {
            e.printStackTrace();
            installedMaps = false;
        }

        // SHOW THE MAP USING CO-ORDINATES FROM THE CHECKIN
        if (installedMaps == true) {
            String geoCode = "geo:0,0?q=" + lati + ","
                    + longi + "(" + place + ")";
            Intent sendLocationToMap = new Intent(Intent.ACTION_VIEW,
                    Uri.parse(geoCode));
            startActivity(sendLocationToMap);
        } else if (installedMaps == false) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    MainActivity.this);

            // SET THE ICON
            alertDialogBuilder.setIcon(R.mipmap.ic_launcher);

            // SET THE TITLE
            alertDialogBuilder.setTitle("Google Maps Not Found");

            // SET THE MESSAGE
            alertDialogBuilder
                    .setMessage(R.string.noMapsInstalled)
                    .setCancelable(false)
                    .setNeutralButton("Got It",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int id) {
                                    dialog.dismiss();
                                }
                            });

            // CREATE THE ALERT DIALOG
            AlertDialog alertDialog = alertDialogBuilder.create();

            // SHOW THE ALERT DIALOG
            alertDialog.show();
        }
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_SHORT).show();
    }

}
