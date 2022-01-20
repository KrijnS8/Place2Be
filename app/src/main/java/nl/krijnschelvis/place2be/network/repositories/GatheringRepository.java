package nl.krijnschelvis.place2be.network.repositories;

import java.util.List;

import nl.krijnschelvis.place2be.network.models.Gathering;
import nl.krijnschelvis.place2be.network.models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface GatheringRepository {

    @FormUrlEncoded
    @POST("add-gathering")
    Call<Gathering> addGathering(@Field("latitude") double latitude,
                            @Field("longitude") double longitude,
                            @Field("street") String street,
                            @Field("postalCode") String postalCode,
                            @Field("city") String city,
                            @Field("state") String state,
                            @Field("country") String country);

    @GET("get-all-gatherings")
    Call<List<Gathering>> getAllGatherings();
}
