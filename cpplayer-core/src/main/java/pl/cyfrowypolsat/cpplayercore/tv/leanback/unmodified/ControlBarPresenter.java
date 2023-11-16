package pl.cyfrowypolsat.cpplayercore.tv.leanback.unmodified;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.leanback.widget.Action;
import androidx.leanback.widget.ObjectAdapter;
import androidx.leanback.widget.Presenter;

import pl.cyfrowypolsat.cpplayercore.R;

public class ControlBarPresenter extends Presenter {

    static final int MAX_CONTROLS = 7;

    /**
     * The data type expected by this presenter.
     */
    public static class BoundData {
        /**
         * Adapter containing objects of type {@link Action}.
         */
        public ObjectAdapter adapter;

        /**
         * The presenter to be used for the adapter objects.
         */
        public Presenter presenter;
    }

    /**
     * Listener for control selected events.
     */
    public interface OnControlSelectedListener {
        void onControlSelected(Presenter.ViewHolder controlViewHolder, Object item,
                               ControlBarPresenter.BoundData data);
    }

    /**
     * Listener for control clicked events.
     */
    public interface OnControlClickedListener {
        void onControlClicked(Presenter.ViewHolder controlViewHolder, Object item,
                              ControlBarPresenter.BoundData data);
    }

    public class ViewHolder extends Presenter.ViewHolder {
        ObjectAdapter mAdapter;
        ControlBarPresenter.BoundData mData;
        Presenter mPresenter;
        ControlBar mControlBar;
        View mControlsContainer;
        SparseArray<Presenter.ViewHolder> mViewHolders =
                new SparseArray<Presenter.ViewHolder>();
        ObjectAdapter.DataObserver mDataObserver;

        /**
         * Constructor for the ViewHolder.
         */
        ViewHolder(View rootView) {
            super(rootView);
            mControlsContainer = rootView.findViewById(androidx.leanback.R.id.controls_container);
            mControlBar = (ControlBar) rootView.findViewById(R.id.control_bar);
            if (mControlBar == null) {
                throw new IllegalStateException("Couldn't find control_bar");
            }
            mControlBar.setDefaultFocusToMiddle(mDefaultFocusToMiddle);
            mControlBar.setOnChildFocusedListener(new ControlBar.OnChildFocusedListener() {
                @Override
                public void onChildFocusedListener(View child, View focused) {
                    if (mOnControlSelectedListener == null) {
                        return;
                    }
                    for (int position = 0; position < mViewHolders.size(); position++) {
                        if (mViewHolders.get(position).view == child) {
                            mOnControlSelectedListener.onControlSelected(
                                    mViewHolders.get(position),
                                    getDisplayedAdapter().get(position), mData);
                            break;
                        }
                    }
                }
            });
            mDataObserver = new ObjectAdapter.DataObserver() {
                @Override
                public void onChanged() {
                    if (mAdapter == getDisplayedAdapter()) {
                        showControls(mPresenter);
                    }
                }
                @Override
                public void onItemRangeChanged(int positionStart, int itemCount) {
                    if (mAdapter == getDisplayedAdapter()) {
                        for (int i = 0; i < itemCount; i++) {
                            bindControlToAction(positionStart + i, mPresenter);
                        }
                    }
                }
            };
        }

        int getChildMarginFromCenter(Context context, int numControls) {
            // Includes margin between icons plus two times half the icon width.
            return getChildMarginDefault(context) + getControlIconWidth(context);
        }

        void showControls(Presenter presenter) {
            ObjectAdapter adapter = getDisplayedAdapter();
            int adapterSize = adapter == null ? 0 : adapter.size();
            // Shrink the number of attached views
            View focusedView = mControlBar.getFocusedChild();
            if (focusedView != null && adapterSize > 0
                    && mControlBar.indexOfChild(focusedView) >= adapterSize) {
                mControlBar.getChildAt(adapter.size() - 1).requestFocus();
            }
            for (int i = mControlBar.getChildCount() - 1; i >= adapterSize; i--) {
                mControlBar.removeViewAt(i);
            }
            for (int position = 0; position < adapterSize && position < MAX_CONTROLS;
                 position++) {
                bindControlToAction(position, adapter, presenter);
            }
            mControlBar.setChildMarginFromCenter(
                    getChildMarginFromCenter(mControlBar.getContext(), adapterSize));
        }

        void bindControlToAction(int position, Presenter presenter) {
            bindControlToAction(position, getDisplayedAdapter(), presenter);
        }

        private void bindControlToAction(final int position,
                                         ObjectAdapter adapter, Presenter presenter) {
            Presenter.ViewHolder vh = mViewHolders.get(position);
            Object item = adapter.get(position);
            if (vh == null) {
                vh = presenter.onCreateViewHolder(mControlBar);
                mViewHolders.put(position, vh);

                final Presenter.ViewHolder itemViewHolder = vh;
                presenter.setOnClickListener(vh, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Object item = getDisplayedAdapter().get(position);
                        if (mOnControlClickedListener != null) {
                            mOnControlClickedListener.onControlClicked(itemViewHolder, item,
                                    mData);
                        }
                    }
                });
            }
            if (vh.view.getParent() == null) {
                mControlBar.addView(vh.view);
            }
            presenter.onBindViewHolder(vh, item);
        }

        /**
         * Returns the adapter currently bound to the displayed controls.
         * May be overridden in a subclass.
         */
        ObjectAdapter getDisplayedAdapter() {
            return mAdapter;
        }
    }

    ControlBarPresenter.OnControlClickedListener mOnControlClickedListener;
    ControlBarPresenter.OnControlSelectedListener mOnControlSelectedListener;
    private int mLayoutResourceId;
    private static int sChildMarginDefault;
    private static int sControlIconWidth;
    boolean mDefaultFocusToMiddle = true;

    /**
     * Constructor for a ControlBarPresenter.
     *
     * @param layoutResourceId The resource id of the layout for this presenter.
     */
    public ControlBarPresenter(int layoutResourceId) {
        mLayoutResourceId = layoutResourceId;
    }

    /**
     * Returns the layout resource id.
     */
    public int getLayoutResourceId() {
        return mLayoutResourceId;
    }

    /**
     * Sets the listener for control clicked events.
     */
    public void setOnControlClickedListener(ControlBarPresenter.OnControlClickedListener listener) {
        mOnControlClickedListener = listener;
    }

    /**
     * Returns the listener for control clicked events.
     */
    public ControlBarPresenter.OnControlClickedListener getOnItemViewClickedListener() {
        return mOnControlClickedListener;
    }

    /**
     * Sets the listener for control selection.
     */
    public void setOnControlSelectedListener(ControlBarPresenter.OnControlSelectedListener listener) {
        mOnControlSelectedListener = listener;
    }

    /**
     * Returns the listener for control selection.
     */
    public ControlBarPresenter.OnControlSelectedListener getOnItemControlListener() {
        return mOnControlSelectedListener;
    }

    public void setBackgroundColor(ControlBarPresenter.ViewHolder vh, int color) {
        vh.mControlsContainer.setBackgroundColor(color);
    }

    @Override
    public Presenter.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(getLayoutResourceId(), parent, false);
        return new ControlBarPresenter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(Presenter.ViewHolder holder, Object item) {
        ControlBarPresenter.ViewHolder vh = (ControlBarPresenter.ViewHolder) holder;
        ControlBarPresenter.BoundData data = (ControlBarPresenter.BoundData) item;
        if (vh.mAdapter != data.adapter) {
            vh.mAdapter = data.adapter;
            if (vh.mAdapter != null) {
                vh.mAdapter.registerObserver(vh.mDataObserver);
            }
        }
        vh.mPresenter = data.presenter;
        vh.mData = data;
        vh.showControls(vh.mPresenter);
    }

    @Override
    public void onUnbindViewHolder(Presenter.ViewHolder holder) {
        ControlBarPresenter.ViewHolder vh = (ControlBarPresenter.ViewHolder) holder;
        if (vh.mAdapter != null) {
            vh.mAdapter.unregisterObserver(vh.mDataObserver);
            vh.mAdapter = null;
        }
        vh.mData = null;
    }

    int getChildMarginDefault(Context context) {
        if (sChildMarginDefault == 0) {
            sChildMarginDefault = context.getResources().getDimensionPixelSize(
                    R.dimen.lb_playback_controls_child_margin_default);
        }
        return sChildMarginDefault;
    }

    int getControlIconWidth(Context context) {
        if (sControlIconWidth == 0) {
            sControlIconWidth = context.getResources().getDimensionPixelSize(
                    R.dimen.lb_control_icon_width);
        }
        return sControlIconWidth;
    }

    /**
     * @param defaultFocusToMiddle True for middle item, false for 0.
     */
    public void setDefaultFocusToMiddle(boolean defaultFocusToMiddle) {
        mDefaultFocusToMiddle = defaultFocusToMiddle;
    }

}
