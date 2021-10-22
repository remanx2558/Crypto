package com.paytm.pgplus.crypto.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.paytm.pgplus.crypto.blockchain.Block;
import com.paytm.pgplus.crypto.blockchain.BlockChain;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonHelper {
    static Gson gson = new GsonBuilder().create();

    public static JSONObject blockTojsonObject(Block block) throws JSONException {
        String jsonInString = new Gson().toJson(block);
        JSONObject mJSONObject = new JSONObject(jsonInString);
        return mJSONObject;
    }
    public static Block jsonObjectToBlock(JsonElement object) throws JSONException {
        Block block=gson.fromJson(String.valueOf(object), Block.class);
        return block;

    }
    public static BlockChain jsonToBlockChain(String json_Chain){
        BlockChain blockChain=gson.fromJson(json_Chain,BlockChain.class);
        return blockChain;
    }
}
