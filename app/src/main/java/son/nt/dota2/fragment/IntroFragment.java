package son.nt.dota2.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.HeroManager;
import son.nt.dota2.R;
import son.nt.dota2.base.AObject;
import son.nt.dota2.base.AbsFragment;
import son.nt.dota2.dto.save.SaveRoles;
import son.nt.dota2.htmlcleaner.role.RoleDto;
import son.nt.dota2.utils.FileUtil;
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
    LinearLayout linearLayout;

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
        update();

    }

    private TextView txtLore;
    private ImageView img;
    View view;
    TextView txtRoles;
    ImageView imgRole;
    List<RoleDto> list = new ArrayList<RoleDto>();

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

        linearLayout = (LinearLayout) view.findViewById(R.id.intro_ll_roles);
        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        try {
            AObject aObject = FileUtil.getObject(getActivity(), SaveRoles.class.getSimpleName());
            if (aObject != null) {
                SaveRoles saveRoles = (SaveRoles) aObject;
                list.clear();
                list.addAll(saveRoles.list);
            }

        } catch (Exception e) {

        }

        for (String s: HeroManager.getInstance().getHero(heroId).roles) {
            view = layoutInflater.inflate(R.layout.row_intro_role, null);
            txtRoles = (TextView) view.findViewById(R.id.row_intro_name);
            txtRoles.setText(s);
            imgRole = (ImageView) view.findViewById(R.id.row_intro_image);

            if (list.size() > 0) {
                imgRole.setVisibility(View.VISIBLE);
                Glide.with(imgRole.getContext()).load(getImg(s)).diskCacheStrategy(DiskCacheStrategy.ALL)
                        .fitCenter().into(imgRole);
            } else {
                imgRole.setVisibility(View.GONE);
            }


            linearLayout.addView(view);
        }
    }

    private String getImg (String role) {
        for (RoleDto d: list) {
            if (d.name.trim().toLowerCase().equals(role.trim().toLowerCase())) {
                return d.linkIcon;
            }
        }
        return null;
    }

    @Override
    public void initListener() {

    }

    private void update() {
        try
        {
            AObject aObject = FileUtil.getObject(getActivity(), SaveRoles.class.getSimpleName());
            if (aObject != null) {
                SaveRoles saveRoles = (SaveRoles) aObject;

                return;
            }
            ParseQuery<ParseObject> query = ParseQuery.getQuery(RoleDto.class.getSimpleName());
            query.addAscendingOrder("no");
            query.findInBackground(new FindCallback<ParseObject>() {
                @Override
                public void done(List<ParseObject> l, ParseException e) {
                    if (e != null) {
                        return;
                    }
                    List<RoleDto> list = new ArrayList<RoleDto>();
                    RoleDto dto;
                    for (ParseObject p :l) {
                        dto = new RoleDto();
                        dto.name = p.getString("name");
                        dto.slogan = p.getString("slogan");
                        dto.linkIcon = p.getString("linkIcon");
                        list.add(dto);
                    }
                    try {
                        FileUtil.saveObject(getActivity(),new SaveRoles(list),SaveRoles.class.getSimpleName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }

                }
            });

        } catch (Exception e) {

        }

    }
}
