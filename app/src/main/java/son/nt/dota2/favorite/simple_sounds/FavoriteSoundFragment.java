package son.nt.dota2.favorite.simple_sounds;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import son.nt.dota2.R;
import son.nt.dota2.base.AFragment;
import son.nt.dota2.data.HeroRepository;
import son.nt.dota2.dto.HeroResponsesDto;
import son.nt.dota2.dto.SpeakDto;
import son.nt.dota2.hero.hero_fragment.AdapterFragmentSound;
import son.nt.dota2.utils.OttoBus;


public class FavoriteSoundFragment extends AFragment implements FavoriteSoundContract.View {

    FavoriteSoundContract.Presenter mPresenter;


    private RecyclerView mRecyclerView;
    private AdapterFragmentSound mAdapter;
    private OnFragmentInteractionListener mListener;

    public static FavoriteSoundFragment newInstance() {
        FavoriteSoundFragment fragment = new FavoriteSoundFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public FavoriteSoundFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FavoriteSoundPresenter(this, new HeroRepository());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_saved, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getSafeActionBar().setTitle("My Playlist");
        OttoBus.register(this);
        initData();
        initLayout(view);
        updateLayout();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.fetchData();

    }

    private void initLayout(View view) {
        mRecyclerView = (RecyclerView) view.findViewById(R.id.saved_recycle_view);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mAdapter = new AdapterFragmentSound(context, new ArrayList<>(0));
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {


    }

    private void updateLayout() {

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

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

        void onSavedItemLongClick(SpeakDto dto);
    }


    @Override
    public void onDestroy() {
        OttoBus.unRegister(this);
        super.onDestroy();

    }


    @Override
    public void showHeroSoundsList(List<HeroResponsesDto> list) {
        mAdapter.setData(list);

    }
}
