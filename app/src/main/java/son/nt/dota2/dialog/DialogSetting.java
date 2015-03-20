package son.nt.dota2.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import son.nt.dota2.MsConst;
import son.nt.dota2.R;
import son.nt.dota2.utils.PreferenceUtil;

/**
 * A simple {@link android.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DialogSetting.IDialogListener} interface
 * to handle interaction events.
 * Use the {@link DialogSetting#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DialogSetting extends DialogFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private static final String ARG_PARAM4 = "param4";
    private static final String ARG_PARAM5 = "param5";

    // TODO: Rename and change types of parameters
    private String title;
    private String message;
    private int icon = -1;
    private String positive = "";
    private String negative = "";

    private IDialogListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DialogFinishWorkout.
     */
    // TODO: Rename and change types and number of parameters
    public static DialogSetting newInstance(String param1, String param2) {
        DialogSetting fragment = new DialogSetting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogSetting newInstance(String title, String message, int iconId, String positive, String negative) {
        DialogSetting fragment = new DialogSetting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, title);
        args.putString(ARG_PARAM2, message);
        args.putInt(ARG_PARAM3, iconId);
        args.putString(ARG_PARAM4, positive);
        args.putString(ARG_PARAM5, negative);
        fragment.setArguments(args);
        return fragment;
    }

    public static DialogSetting newInstanceSetting(String positive, String negative) {
        DialogSetting fragment = new DialogSetting();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM4, positive);
        args.putString(ARG_PARAM5, negative);
        fragment.setArguments(args);
        return fragment;
    }

    public DialogSetting() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_PARAM1);
            message = getArguments().getString(ARG_PARAM2);
            icon = getArguments().getInt(ARG_PARAM3);
            positive = getArguments().getString(ARG_PARAM4);
            negative = getArguments().getString(ARG_PARAM5);
        }
    }

    SeekBar volumeBar;
    ImageView imgRepeatOne, imgRepeatOff, imgRepeatOn;
    TextView txtVolume;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());


        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_settings, null);
        txtVolume = (TextView) view.findViewById(R.id.setting_txt_volume);
        volumeBar = (SeekBar) view.findViewById(R.id.setting_volume);
        view.findViewById(R.id.setting_btn_done).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        final AudioManager audioManager = (AudioManager) getActivity().getSystemService(Context.AUDIO_SERVICE);
        volumeBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
//        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, currentVolume, 0);
        volumeBar.setProgress(currentVolume);
        txtVolume.setText(String.valueOf(currentVolume));
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                    txtVolume.setText(String.valueOf(progress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        imgRepeatOne = (ImageView) view.findViewById(R.id.setting_repeat_one);
        imgRepeatOff = (ImageView) view.findViewById(R.id.setting_repeat_off);
        imgRepeatOn = (ImageView) view.findViewById(R.id.setting_repeat_on);
        imgRepeatOne.setOnClickListener(this);
        imgRepeatOff.setOnClickListener(this);
        imgRepeatOn.setOnClickListener(this);

        mode = MsConst.RepeatMode.getMode(PreferenceUtil.getPreference(getActivity(), MsConst.KEY_REPEAT, 1));
        updateLayout(mode);
        builder.setView(view);
        return builder.create();
    }

    MsConst.RepeatMode mode = MsConst.RepeatMode.MODE_OFF;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_repeat_off:
//                imgRepeatOn.setVisibility(View.VISIBLE);
//                imgRepeatOff.setVisibility(View.GONE);
//                imgRepeatOne.setVisibility(View.GONE);
                mode = MsConst.RepeatMode.MODE_ON;
                break;
            case R.id.setting_repeat_on:
//                imgRepeatOn.setVisibility(View.GONE);
//                imgRepeatOff.setVisibility(View.GONE);
//                imgRepeatOne.setVisibility(View.VISIBLE);
                mode = MsConst.RepeatMode.MODE_ONE;
                break;
            case R.id.setting_repeat_one:
//                imgRepeatOn.setVisibility(View.GONE);
//                imgRepeatOff.setVisibility(View.VISIBLE);
//                imgRepeatOne.setVisibility(View.GONE);
                mode = MsConst.RepeatMode.MODE_OFF;
                break;
        }
        updateLayout(mode);
        PreferenceUtil.setPreference(getActivity(), MsConst.KEY_REPEAT, mode.getValue());
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface IDialogListener {
        // TODO: Update argument type and name
        public void onIDialogCancel(MsConst.RepeatMode repeatMode, int currentVolume);

    }

    public void setOnIDialogListener(IDialogListener callback) {
        this.mListener = callback;
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mListener != null) {
            mListener.onIDialogCancel(mode, volumeBar.getProgress());
        }
    }

    private void updateLayout(MsConst.RepeatMode mode) {
        switch (mode) {
            case MODE_ON:
                imgRepeatOn.setVisibility(View.VISIBLE);
                imgRepeatOff.setVisibility(View.GONE);
                imgRepeatOne.setVisibility(View.GONE);
                break;
            case MODE_ONE:
                imgRepeatOn.setVisibility(View.GONE);
                imgRepeatOff.setVisibility(View.GONE);
                imgRepeatOne.setVisibility(View.VISIBLE);
                break;
            case MODE_OFF:
                imgRepeatOn.setVisibility(View.GONE);
                imgRepeatOff.setVisibility(View.VISIBLE);
                imgRepeatOne.setVisibility(View.GONE);
                break;
        }
    }
}
