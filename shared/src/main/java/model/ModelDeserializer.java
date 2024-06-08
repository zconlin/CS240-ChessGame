package model;

import resultclasses.LoginResult;
import resultclasses.RegisterResult;
import chess.ChessGame;
import chess.ChessGame;
import com.google.gson.*;

import java.io.InputStreamReader;

public class ModelDeserializer {
    public static <T> T deserialize(InputStreamReader reader, Class<T> responseClass) {
        GsonBuilder gsonBuilder = new GsonBuilder();

        gsonBuilder.registerTypeAdapter(ChessGame.class, new ChessGame.ChessGameTA());
        gsonBuilder.registerTypeAdapter(LoginResult.class, new LoginResult.LoginResultTypeAdapter());
        gsonBuilder.registerTypeAdapter(RegisterResult.class, new RegisterResult.RegisterResultTypeAdapter());
        return gsonBuilder.create().fromJson(reader, responseClass);
    }
}
