package son.nt.dota2.facebook;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.facebook.FacebookRequestError;
import com.facebook.GraphResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import son.nt.dota2.facebook.object.FbCmtData;
import son.nt.dota2.facebook.object.FbCmtFrom;
import son.nt.dota2.facebook.object.FbCmtSummary;
import son.nt.dota2.facebook.object.FbComments;
import son.nt.dota2.utils.TsLog;


public abstract class FbCommentsLoader extends FbLoaderGet<FbComments> {

    private static final String TAG = "FbCommentsLoader";
    TsLog log = new TsLog(TAG);

    public FbCommentsLoader(Context context, String graphpath, Bundle params) {
        super(context, graphpath, params);
    }

    @Override
    protected FbComments handleResult(GraphResponse response) {
        try {
            JSONObject jsonObject = response.getJSONObject();
            FacebookRequestError facebookError = response.getError();
            String err = facebookError.getErrorMessage();
            log.d("log>>>" + "err:" + err);
            JSONArray jsonArray = jsonObject.getJSONArray("data");
            FbComments comments = new FbComments();
            FbCmtData dto;
            String id = null;
            String created_time = null;
            String message = null;
            int like_count = -1;
            FbCmtFrom from = null;

            JSONObject jFrom = null;
            String fromId = null;
            String fromName = null;

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject ja = (JSONObject) jsonArray.get(i);

                message = null;
                if (MsUtils.iSJsonValueAvailable(ja, "message")) {
                    message = ja.getString("message");
                }

                if (TextUtils.isEmpty(message)) {
                    log.e("log>>>" + "Message NULL");
                } else {

                    if (MsUtils.iSJsonValueAvailable(ja, "id")) {
                        id = ja.getString("id");
                    }

                    if (MsUtils.iSJsonValueAvailable(ja, "created_time")) {
                        created_time = ja.getString("created_time");
                    }

                    if (MsUtils.iSJsonValueAvailable(ja, "like_count")) {
                        like_count = ja.getInt("like_count");
                    }

                    if (MsUtils.iSJsonValueAvailable(ja, "from")) {
                        jFrom = ja.getJSONObject("from");

                        if (MsUtils.iSJsonValueAvailable(jFrom, "id")) {
                            fromId = jFrom.getString("id");
                        }

                        if (MsUtils.iSJsonValueAvailable(jFrom, "name")) {
                            fromName = jFrom.getString("name");
                        }
                    }

                    // get commenter
                    // log.d("log>>>" + "bbb:" + jFrom.toString());
                    from = new FbCmtFrom(fromId, fromName);
                    dto = new FbCmtData(id, created_time, message, like_count, from);

                    comments.getData().add(dto);
                }

            }

            // get summaty

            JSONObject jSummary = (JSONObject) jsonObject.get("summary");
            int total_count = jSummary.getInt("total_count");
            comments.setSummary(new FbCmtSummary(total_count));
            return comments;
        } catch (Exception e) {
            Log.e("", "log>>>" + "error FbCommentsLoader:" + e.toString());
            return null;
        }
    }

}
