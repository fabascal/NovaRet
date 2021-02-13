package com.example.novaret.Volumetrico;

import org.json.JSONException;
import org.json.JSONObject;

public interface VolumetricoListener {
    void processFinish(JSONObject output) throws JSONException;
}
