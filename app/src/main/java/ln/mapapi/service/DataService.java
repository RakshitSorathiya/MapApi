package ln.mapapi.service;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by comp-1 on 3/2/17.
 */

public interface DataService {

    @GET("maps/api/geocode/json?")
    Call<JsonObject> getResult(@Query("address") String address);

}

