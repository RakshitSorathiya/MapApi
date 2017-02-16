package ln.mapapi.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by comp-1 on 3/2/17.
 */

public class DataRequest {

    @SerializedName("address_components")
    @Expose
    private List<DataRequest> addressComponents = null;

    @SerializedName("formatted_address")
    @Expose
    private String formattedAddress;

    @SerializedName("place_id")
    @Expose
    private String placeId;

    @SerializedName("types")
    @Expose
    private List<String> types = null;

    public List<DataRequest> getAddressComponents() {
        return addressComponents;
    }

    public void setAddressComponents(List<DataRequest> addressComponents) {
        this.addressComponents = addressComponents;
    }

    public String getFormattedAddress() {
        return formattedAddress;
    }

    public void setFormattedAddress(String formattedAddress) {
        this.formattedAddress = formattedAddress;
    }


    public String getPlaceId() {
        return placeId;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public List<String> getTypes() {
        return types;
    }

    public void setTypes(List<String> types) {
        this.types = types;
    }

}
