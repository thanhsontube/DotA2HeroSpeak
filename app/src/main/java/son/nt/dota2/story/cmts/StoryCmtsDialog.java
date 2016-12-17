package son.nt.dota2.story.cmts;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import son.nt.dota2.R;
import son.nt.dota2.comments.AdapterCmtsHistory;
import son.nt.dota2.comments.FullCmtsDto;
import son.nt.dota2.customview.CommentView;
import son.nt.dota2.data.HeroRepository;

/**
 * Created by Sonnt on 8/7/15.
 * Dialog hien thi cmts lien quan toi story
 * input : story Id -> search firebase -> diaplay
 * Support add new cmts
 */
public class StoryCmtsDialog extends DialogFragment implements StoryCmtsContract.View {


    StoryCmtsContract.Presenter mPresenter;

    @BindView(R.id.cmt_rcv)
    RecyclerView mRecyclerView;

    AdapterCmtsHistory mAdapter;

    @BindView(R.id.cmt_view)
    CommentView mCommentView;


    public static StoryCmtsDialog newInstance(String cmtID) {
        StoryCmtsDialog dialog = new StoryCmtsDialog();
        Bundle bundle = new Bundle();
        bundle.putString("data", cmtID);
        dialog.setArguments(bundle);
        return dialog;
    }

    @OnClick(R.id.cmt_close)
    public void onCloseClick() {
        dismiss();
    }

    @Override
    public Dialog getDialog() {
        return super.getDialog();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        LayoutInflater layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.dialog_story_cmts, null);
        ButterKnife.bind(this, view);

        mAdapter = new AdapterCmtsHistory(getContext(), new ArrayList<>(0), null);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter = new StoryCmtsPresenter(this, new HeroRepository());
        mPresenter.setStoryId(getArguments().getString("data", ""));
        mPresenter.getComments();

        mCommentView.setCallback(mess -> {
            mPresenter.putCommend(mess);
            dismiss();
        });

        AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                .setView(view).create();
        alertDialog.setCanceledOnTouchOutside(false);
        return alertDialog;
    }

    @Override
    public void showList(List<FullCmtsDto> fullCmtsDtos) {
        mAdapter.setData(fullCmtsDtos);
    }
}
