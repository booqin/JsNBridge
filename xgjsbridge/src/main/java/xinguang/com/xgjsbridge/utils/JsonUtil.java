package xinguang.com.xgjsbridge.utils;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class JsonUtil {
    private JsonUtil(){}
    private static Gson gson = new Gson();

    /**
     * 实体对象转Json字符串
     *
     * @param o 实体对象
     * @return String
     */
    public static  String toJsonString(Object o) {
        return gson.toJson(o);
    }

    /**
     * 实体对象转JSONObject
     *
     * @param type
     * @param <T>
     * @return
     * @throws JSONException
     */
    public static <T> JSONObject toJson(T type) throws JSONException {
        return new JSONObject(toJsonString(type));
    }

    /**
     * Json字符串转实体对象
     *
     * @param json
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> T fromJsonString(String json, Class<T> clazz) {
        if (TextUtils.isEmpty(json)) {
            return null;
        }
        return gson.fromJson(json, clazz);
    }

    /**
     * 将数组转为相应对象的List
     *
     * @param <T>
     * @return
     */
    public static <T> List<T> fromJsonArray(String json, Class<T> klass) throws JsonSyntaxException {
        if(TextUtils.isEmpty(json)){
            return  null;
        }
        return gson.fromJson(json, new GsonList<T>(klass));
    }

    public static class GsonList<T> implements ParameterizedType {

        private Class<?> wrapped;

        public GsonList(Class<T> wrapped) {
            this.wrapped = wrapped;
        }

        public Type[] getActualTypeArguments() {
            return new Type[]{wrapped};
        }

        public Type getRawType() {
            return List.class;
        }

        public Type getOwnerType() {
            return null;
        }

    }

    public static <T> T fromJson(JsonReader reader, Type typeOfT) throws JsonIOException, JsonSyntaxException {
        return gson.fromJson(reader, typeOfT);
    }


}
