package br.edu.ifto.pdmii.avaliacao02.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import br.edu.ifto.pdmii.avaliacao02.R;
import br.edu.ifto.pdmii.avaliacao02.model.Score;

public class ScoreListAdapter extends BaseAdapter {
    private final Context mContext;
    private final List<Score> mData;

    static class ViewHolder {
        protected TextView playerTextView;
        protected TextView scoreAverrageTextView;
        protected TextView scoreDescriptionTextView;
    }

    public ScoreListAdapter(Context context, List<Score> scores) {
        this.mContext = context;
        this.mData = scores;
    }

    public List<Score> getData() {
        return mData;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int i) {
        return mData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Score score = mData.get(i);
        ViewHolder mViewHolder;

        if (view == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            view = layoutInflater.inflate(R.layout.score_list_item, viewGroup, false);
            mViewHolder = new ViewHolder();

            mViewHolder.playerTextView = view.findViewById(R.id.text_player_name);
            mViewHolder.scoreAverrageTextView = view.findViewById(R.id.text_score_avg);
            mViewHolder.scoreDescriptionTextView = view.findViewById(R.id.text_score_desc);

            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }

        mViewHolder.playerTextView.setText(score.player);
        mViewHolder.scoreAverrageTextView.setText(mContext.getString(R.string.score_avg, score.averrage));
        mViewHolder.scoreDescriptionTextView.setText(mContext.getString(R.string.score_desc,
                score.namePoints, score.experiencePoints));

        return view;
    }
}
