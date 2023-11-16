package pl.cyfrowypolsat.cpplayercore.tv.leanback.modified;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.leanback.widget.Action;
import androidx.leanback.widget.Presenter;

import pl.cyfrowypolsat.cpplayercore.R;

public class TvPrimaryControlButtonPresenter extends Presenter {

    static class PrimaryActionViewHolder extends Presenter.ViewHolder {
        ImageButton mButton;

        public PrimaryActionViewHolder(View view) {
            super(view);
            mButton = view.findViewById(R.id.primary_button);
        }
    }


    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cppl_cr_tv_control_button_primary, parent, false);
        return new PrimaryActionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder viewHolder, Object item) {
        Action action = (Action) item;
        PrimaryActionViewHolder vh = (PrimaryActionViewHolder) viewHolder;
        vh.mButton.setImageDrawable(action.getIcon());
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder viewHolder) {
        PrimaryActionViewHolder vh = (PrimaryActionViewHolder) viewHolder;
        vh.mButton.setImageDrawable(null);
    }

    @Override
    public void setOnClickListener(Presenter.ViewHolder viewHolder,
                                   View.OnClickListener listener) {
        ((PrimaryActionViewHolder) viewHolder).mButton.setOnClickListener(listener);
    }
}