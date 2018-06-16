package com.project.sagi.queue;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class SocketService {
    private static Context m_activity;
    private static Socket mSocket;
    private static Emitter.Listener SignUpComplete;
    private static Emitter.Listener SignUpBusinessComplete;
    private static Emitter.Listener SearchCityComplete;
    private static Emitter.Listener SearchAddressComplete;

    private static Socket GetSocket(){
        if (mSocket == null) {
            try {
                mSocket = IO.socket("https://stale-swan-51.localtunnel.me");
                mSocket.connect();
            } catch (URISyntaxException e) {}
        }
        return mSocket;
    }

    public static void SignUp(clsUser user, Context activity) {
        m_activity = activity;

        Gson gson = new Gson();
        String json = gson.toJson(user);
        GetSocket().emit("SignUp", json);
    }

    public static void SignUpBusiness(clsBusiness business, Context activity) {
        m_activity = activity;
        Gson gson = new Gson();
        String json = gson.toJson(business);
        GetSocket().emit("SignUpBusiness", json);
    }

    public static void SearchCity(String city, Context activity) {
        if (city.length() > 0) {
            m_activity = activity;
            GetSocket().emit("SearchCity", city);
        }
    }

    public static void SearchAddress(String address, Context activity) {
        if (address.length() > 0) {
            m_activity = activity;
            String city = ((AutoCompleteTextView)((Activity)m_activity).findViewById(R.id.txtBusinessCity)).getText().toString();
            GetSocket().emit("SearchAddress", address, city);
        }
    }

    public static void WriteError(Exception ex) {
        StringWriter errors = new StringWriter();
        ex.printStackTrace(new PrintWriter(errors));
        GetSocket().emit("WriteError", ex.getMessage(), errors.toString());
    }

    public static void UploadImage(final Bitmap bitmap, Context activity, final String imageGuid) {
        m_activity = activity;
        new Thread(new Runnable() {
            public void run() {
                Bitmap scaleBitmap = clsGlobalHelper.scaleDown(bitmap, 700, false);
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                scaleBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] byteArray = baos.toByteArray();
                String encodedImage = Base64.encodeToString(byteArray,Base64.NO_WRAP);
                GetSocket().emit("UploadImage", encodedImage, imageGuid);
            }
        }).start();

    }

    public static void InitListeners() {
        SignUpComplete = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                ((Activity)m_activity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int exitCode = (int) args[0];
                        String Message = (String) args[1];
                        if (exitCode == 0) {
                            Toasty.success(m_activity, Message, Toast.LENGTH_SHORT, true).show();
                            //Intent myIntent = new Intent(m_activity, ManagerSettings.class);
                            //m_activity.startActivity(myIntent);
                        } else if (exitCode == 1) {
                            Toasty.warning(m_activity, Message, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.error(m_activity, Message, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
            }
        };
        GetSocket().on("SignUpComplete", SignUpComplete);


        SignUpBusinessComplete = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                ((Activity)m_activity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int exitCode = (int) args[0];
                        String Message = (String) args[1];
                        if (exitCode == 0) {
                            Toasty.success(m_activity, Message, Toast.LENGTH_SHORT, true).show();
                            //Intent myIntent = new Intent(m_activity, ManagerSettings.class);
                            //m_activity.startActivity(myIntent);
                        } else if (exitCode == 1) {
                            Toasty.warning(m_activity, Message, Toast.LENGTH_SHORT, true).show();
                        } else {
                            Toasty.error(m_activity, Message, Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
            }
        };
        GetSocket().on("SignUpBusinessComplete", SignUpBusinessComplete);

        SearchCityComplete = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                ((Activity)m_activity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int exitCode = (int) args[0];
                        String Cities = (String) args[1];
                        try {
                            JSONArray jsonArray = new JSONArray(Cities);
                            if (jsonArray != null) {
                                int len = jsonArray.length();
                                String[] arrOfCities = new String[len];
                                for (int i=0;i<len;i++){
                                    arrOfCities[i] = jsonArray.get(i).toString();
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(m_activity, android.R.layout.simple_dropdown_item_1line, arrOfCities);
                                AutoCompleteTextView textView = (AutoCompleteTextView)((Activity)m_activity).findViewById(R.id.txtBusinessCity);
                                textView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        GetSocket().on("SearchCityComplete", SearchCityComplete);

        SearchAddressComplete = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                ((Activity)m_activity).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int exitCode = (int) args[0];
                        String Addresses = (String) args[1];
                        try {
                            JSONArray jsonArray = new JSONArray(Addresses);
                            if (jsonArray != null) {
                                int len = jsonArray.length();
                                String[] arrOfAddresses = new String[len];
                                for (int i=0;i<len;i++){
                                    arrOfAddresses[i] = jsonArray.get(i).toString();
                                }
                                ArrayAdapter<String> adapter = new ArrayAdapter<String>(m_activity, android.R.layout.simple_dropdown_item_1line, arrOfAddresses);
                                AutoCompleteTextView textView = (AutoCompleteTextView)((Activity)m_activity).findViewById(R.id.txtBusinessStreet);
                                textView.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        };
        GetSocket().on("SearchAddressComplete", SearchAddressComplete);
    }
}
