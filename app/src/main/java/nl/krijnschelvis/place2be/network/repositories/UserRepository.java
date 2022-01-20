package nl.krijnschelvis.place2be.network.repositories;

import nl.krijnschelvis.place2be.network.models.User;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserRepository {

    @FormUrlEncoded
    @POST("register-user")
    Call<User> registerUser(@Field("firstName") String firstName,
                            @Field("lastName") String lastName,
                            @Field("email") String email,
                            @Field("password") String password);

    @GET("get-user-data")
    Call<User> getUser(@Query("email") String email,
                       @Query("password") String password);
}
