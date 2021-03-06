package com.onlinehocam.ozel.ders.okul.AsyncTasks;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.LinearLayout;


import com.onlinehocam.ozel.ders.okul.CustomToast;
import com.onlinehocam.ozel.ders.okul.GlobalVariables;
import com.onlinehocam.ozel.ders.okul.Marketer;
import com.onlinehocam.ozel.ders.okul.MarketerRegisterActivity;
import com.onlinehocam.ozel.ders.okul.R;
import com.onlinehocam.ozel.ders.okul.SoundHelper;
import com.onlinehocam.ozel.ders.okul.Student;
import com.onlinehocam.ozel.ders.okul.StudentRegisterActivity;
import com.onlinehocam.ozel.ders.okul.Tutor;
import com.onlinehocam.ozel.ders.okul.TutorRegisterActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;


public class UploadUserPhotoToDirectory extends AsyncTask<String, Void, String> {

    String KEY_ENCODING_OUTPUT = "UTF-8";
    String KEY_ENCODING_INPUT = "UTF-8";

    String KEY_URL_START = "http://134.209.234.180/";
    String KEY_URL_END = ".php";

    Activity activity;
    Context context;
    String photo_data_address_id;
    LinearLayout linearLayoutMainProgressBar;
    LinearLayout linearLayoutButtonRegisterUser;
    boolean isToDisplayProgressBarOnPreExecute = false;
    boolean isToDismissProgressBarOnPostExecute = false;

    Tutor tutor;
    Student student;
    Marketer marketer;

    String resultString;


    public UploadUserPhotoToDirectory(Activity activity, Context context, Tutor tutor, String photo_data_address_id, LinearLayout linearLayoutMainProgressBar, LinearLayout linearLayoutButtonRegisterUser, boolean isToDisplayProgressBarOnPreExecute, boolean isToDismissProgressBarOnPostExecute) {
        this.activity = activity;
        this.context = context;
        this.photo_data_address_id = photo_data_address_id;
        this.linearLayoutMainProgressBar = linearLayoutMainProgressBar;
        this.linearLayoutButtonRegisterUser = linearLayoutButtonRegisterUser;
        this.isToDisplayProgressBarOnPreExecute = isToDisplayProgressBarOnPreExecute;
        this.isToDismissProgressBarOnPostExecute = isToDismissProgressBarOnPostExecute;
        this.tutor = tutor;
    }

    public UploadUserPhotoToDirectory(Activity activity, Context context, Student student, String photo_data_address_id, LinearLayout linearLayoutMainProgressBar, LinearLayout linearLayoutButtonRegisterUser, boolean isToDisplayProgressBarOnPreExecute, boolean isToDismissProgressBarOnPostExecute) {
        this.activity = activity;
        this.context = context;
        this.photo_data_address_id = photo_data_address_id;
        this.linearLayoutMainProgressBar = linearLayoutMainProgressBar;
        this.linearLayoutButtonRegisterUser = linearLayoutButtonRegisterUser;
        this.isToDisplayProgressBarOnPreExecute = isToDisplayProgressBarOnPreExecute;
        this.isToDismissProgressBarOnPostExecute = isToDismissProgressBarOnPostExecute;
        this.student = student;
    }

    public UploadUserPhotoToDirectory(Activity activity, Context context, Marketer marketer, String photo_data_address_id, LinearLayout linearLayoutMainProgressBar, LinearLayout linearLayoutButtonRegisterUser, boolean isToDisplayProgressBarOnPreExecute, boolean isToDismissProgressBarOnPostExecute) {
        this.activity = activity;
        this.context = context;
        this.photo_data_address_id = photo_data_address_id;
        this.linearLayoutMainProgressBar = linearLayoutMainProgressBar;
        this.linearLayoutButtonRegisterUser = linearLayoutButtonRegisterUser;
        this.isToDisplayProgressBarOnPreExecute = isToDisplayProgressBarOnPreExecute;
        this.isToDismissProgressBarOnPostExecute = isToDismissProgressBarOnPostExecute;
        this.marketer = marketer;
    }

    @Override
    protected String doInBackground(String... params) {
        String type = params[0];

        resultString = Execute_BooleanQuery(type, params);
        return resultString;
    }

    private String Execute_BooleanQuery(String type, String... params) {
        String crUrl = KEY_URL_START + type + KEY_URL_END;
        try {
            URL url = new URL(crUrl);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);

            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, KEY_ENCODING_OUTPUT));


            String post_data = GetPostDataFromParams(KEY_ENCODING_OUTPUT, params);

            bufferedWriter.write(post_data);
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();

            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, KEY_ENCODING_INPUT));

            String result = "";
            String line = "";

            while ((line = bufferedReader.readLine()) != null) {
                result += line;
            }

            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();

            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return "failed due to MalformedURLException";
        } catch (IOException e) {
            e.printStackTrace();
            return "failed due to IOException";
        }
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    static String GetPostDataFromParams(String encoding, String... params) {
        StringBuilder sb = new StringBuilder();
        String crLine = "";


        for (int i = 0; i < ((params.length - 1) / 2); i++) {
            crLine = "";
            try {
                if (i != 0) {
                    crLine = "&";
                }
                crLine += URLEncoder.encode(params[1 + 2 * i], encoding) + "=" + URLEncoder.encode(params[1 + 2 * i + 1], encoding);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                return "failedPostData";
            }

            sb.append(crLine);
        }

        return sb.toString();
    }

    @Override
    protected void onPreExecute() {
        if (isToDisplayProgressBarOnPreExecute)
        {
            linearLayoutMainProgressBar.setVisibility(View.VISIBLE);
            linearLayoutButtonRegisterUser.setVisibility(View.GONE);
        }


    }

    @Override
    protected void onPostExecute(String result) {
        //super.onPostExecute(photoDataAddressId);

        int photoID = Integer.parseInt(resultString);

        //GlobalVariables.Log(context, "photoID: "+photoID);

        if(tutor != null)
        {
            if(photoID != -1)
            {
                tutor.photoID = photoID;
                AsyncTaskHelper.RegisterUserToServer(activity, context, tutor, linearLayoutMainProgressBar, linearLayoutButtonRegisterUser, false, true);
            }
            else
            {
                SoundHelper.PlayMediaPlayerSound(activity, SoundHelper.SOUNDS.CONNECTION_FAILED);
                new CustomToast(activity, context, context.getString(R.string.register_response_failed));
                ((TutorRegisterActivity)activity).isQuestionRequestPostedSuccessfully = false;
            }
        }
        else if(student != null)
        {
            if(photoID != -1)
            {
                student.photoID = photoID;
                AsyncTaskHelper.RegisterUserToServer(activity, context, student, linearLayoutMainProgressBar, linearLayoutButtonRegisterUser, false, true);
            }
            else
            {
                SoundHelper.PlayMediaPlayerSound(activity, SoundHelper.SOUNDS.CONNECTION_FAILED);
                new CustomToast(activity, context, context.getString(R.string.register_response_failed));
                ((StudentRegisterActivity)activity).isQuestionRequestPostedSuccessfully = false;
            }
        }
        else if(marketer != null)
        {
            if(photoID != -1)
            {
                marketer.photoID = photoID;
                AsyncTaskHelper.RegisterUserToServer(activity, context, marketer, linearLayoutMainProgressBar, linearLayoutButtonRegisterUser, false, true);
            }
            else
            {
                SoundHelper.PlayMediaPlayerSound(activity, SoundHelper.SOUNDS.CONNECTION_FAILED);
                new CustomToast(activity, context, context.getString(R.string.register_response_failed));
                ((MarketerRegisterActivity)activity).isQuestionRequestPostedSuccessfully = false;
            }
        }


        if (isToDismissProgressBarOnPostExecute)
        {
            linearLayoutMainProgressBar.setVisibility(View.GONE);
            linearLayoutButtonRegisterUser.setVisibility(View.VISIBLE);
        }
    }
}
