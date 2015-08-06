package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import son.nt.dota2.HeroManager;
import son.nt.dota2.R;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.htmlcleaner.HTTPParseUtils;
import son.nt.dota2.utils.Logger;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IntroFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link IntroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroFragment extends AbsFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String TAG = "IntroFragment";

    // TODO: Rename and change types of parameters
    private String heroId;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment IntroFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IntroFragment newInstance(String heroID) {
        IntroFragment fragment = new IntroFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, heroID);
        fragment.setArguments(args);
        return fragment;
    }

    public IntroFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            heroId = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

    @Override
    public void initData() {

    }

    private TextView txtLore;
    private ImageView img;

    @Override
    public void initLayout(View view) {
        txtLore = (TextView) view.findViewById(R.id.intro_lore);
        txtLore.setText("");
        String text = HeroManager.getInstance().getHero(heroId).lore;
        Logger.debug(TAG, ">>>" + "initLayout text:" + text + ";heroId:" + heroId);
        if (!TextUtils.isEmpty(text)) {
            txtLore.setText(text);
        }
        img = (ImageView) view.findViewById(R.id.intro_image);
        Glide.with(getActivity()).load(HeroManager.getInstance().getHero(heroId).avatarThumbnail).fitCenter().into(img);
    }

    @Override
    public void initListener() {
        HTTPParseUtils.getInstance().setCallback(new HTTPParseUtils.IParseCallBack() {
            @Override
            public void onFinish() {
                String text = HeroManager.getInstance().getHero(heroId).lore;
                if (!TextUtils.isEmpty(text)) {
                    txtLore.setText(text);
                }
            }
        });

    }
}
