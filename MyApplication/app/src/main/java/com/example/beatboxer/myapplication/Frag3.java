package com.example.beatboxer.myapplication;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Frag3 extends Fragment {
    public static ListView list;
    String[] links, names;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view =   inflater.inflate(R.layout.frag3_layout,container,false);

        return view;    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        list = (ListView)view.findViewById(R.id.list2);
        String json = "[{\"id\":1,\"link\":\"http://www.vk.com\",\"name\":\"Авиаперевозки\"}, {\"id\":1,\"link\":\"http://www.google.com\",\"name\":\"Авиа и ЖД кассы\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Авиапредприятия\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Автоаксессуары\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Автозаправки\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Автозапчасти\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Автомасла и спецжидкости\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Автомойки\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Авторазборки\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Авторемонт\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Автосигнализация\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Автостоянки\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Аптеки\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Ателье\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Багетные мастерские\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Базы отдыха\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Yandex\"},{\"id\":1,\"link\":\"http://www.yandex.ru\",\"name\":\"Yandex\"}]";
        try{
            JSONArray array = new JSONArray(json);
            int size = array.length();
            links = new String[size];
            names = new String[size];
            for(int n = 0; n < size; n++) {
                JSONObject obj = array.getJSONObject(n);
                links[n] = obj.getString("link");
                names[n] = obj.getString("name");
            }
        } catch (JSONException e){
            Log.d("$", e.toString());
        }
        final ArrayAdapter adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, names);
        list.setAdapter(adapter);

    }

}


