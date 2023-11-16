package pl.cyfrowypolsat.cpplayercore.tv.leanback.modified;

import androidx.leanback.widget.Presenter;
import androidx.leanback.widget.PresenterSelector;


public class TvControlButtonPresenterSelector extends PresenterSelector {

    private final Presenter mPrimaryPresenter = new TvPrimaryControlButtonPresenter();
    private final Presenter mSecondaryPresenter = new TvSecondaryControlButtonPresenter();
    private final Presenter[] mPresenters = new Presenter[]{mPrimaryPresenter, mSecondaryPresenter};


    public Presenter getPrimaryPresenter() {
        return mPrimaryPresenter;
    }

    public Presenter getSecondaryPresenter() {
        return mSecondaryPresenter;
    }

    @Override
    public Presenter getPresenter(Object item) {
        return mPrimaryPresenter;
    }

    @Override
    public Presenter[] getPresenters() {
        return mPresenters;
    }

}
