package mobi.cangol.mobile.uiframe.demo.fragment;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import mobi.cangol.mobile.base.BaseDialogFragment;
import mobi.cangol.mobile.uiframe.demo.R;

/**
 * Created by Cangol
 */
public class CommonDialogFragment extends BaseDialogFragment {
    private TextView titleTv;
    private View titleLine;
    private TextView messageTv;
    private Button leftBtn;
    private Button rightBtn;
    private Button centerBtn;
    private ListView listView;
    private LinearLayout contentView;

    Builder builder;

    public CommonDialogFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.common_dialog, container, false);
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initViews(savedInstanceState);
    }

    @Override
    protected void findViews(View view) {
        titleTv = (TextView) view.findViewById(R.id.dialog_common_title);
        titleLine = (View) view.findViewById(R.id.dialog_common_title_line);
        messageTv = (TextView) view.findViewById(R.id.dialog_common_message);
        leftBtn = (Button) view.findViewById(R.id.dialog_common_left);
        rightBtn = (Button) view.findViewById(R.id.dialog_common_right);
        centerBtn = (Button) view.findViewById(R.id.dialog_common_center);
        listView = (ListView) view.findViewById(R.id.dialog_common_list);
        contentView = (LinearLayout) view.findViewById(R.id.dialog_common_content);
    }

    @Override
    protected void initViews(Bundle savedInstanceState) {
        if (builder.title != null)
            setTitle(builder.title);
        if (builder.message != null)
            setMessage(builder.message);
        if (builder.leftText != null)
            setLeftButtonInfo(builder.leftText, builder.leftButtonClickListener);
        if (builder.rightText != null)
            setRightButtonInfo(builder.rightText, builder.rightButtonClickListener);
        if (builder.centerText != null)
            setCenterButtonInfo(builder.centerText, builder.centerButtonClickListener);
        if (builder.adapter != null)
            setListViewInfo(builder.adapter, builder.onDialogItemClickListener);
        if (builder.contentViewId > 0)
            setContentView(builder.contentViewId);
    }

    @Override
    protected void initData(Bundle savedInstanceState) {

    }

    private void setTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            titleTv.setText(title);
            titleTv.setVisibility(View.VISIBLE);
            titleLine.setVisibility(View.VISIBLE);
        } else {
            titleTv.setVisibility(View.GONE);
            titleLine.setVisibility(View.GONE);
        }
    }

    private void setMessage(CharSequence message) {
        if (!TextUtils.isEmpty(message)) {
            messageTv.setText(message);
            messageTv.setVisibility(View.VISIBLE);
        } else messageTv.setVisibility(View.GONE);
    }

    private void setLeftButtonInfo(String text, final OnButtonClickListener onButtonClickListener) {
        centerBtn.setVisibility(View.GONE);
        leftBtn.setVisibility(View.VISIBLE);
        this.findViewById(R.id.dialog_common_line).setVisibility(View.VISIBLE);
        this.findViewById(R.id.dialog_common_btn_area).setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) leftBtn.setText(text);
        leftBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null)
                    onButtonClickListener.onClick(v);
                dismiss();
            }

        });
    }

    private void setRightButtonInfo(String text, final OnButtonClickListener onButtonClickListener) {
        centerBtn.setVisibility(View.GONE);
        rightBtn.setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) rightBtn.setText(text);
        this.findViewById(R.id.dialog_common_left_line).setVisibility(View.VISIBLE);
        this.findViewById(R.id.dialog_common_line).setVisibility(View.VISIBLE);
        this.findViewById(R.id.dialog_common_btn_area).setVisibility(View.VISIBLE);
        rightBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null)
                    onButtonClickListener.onClick(v);
                dismiss();
            }

        });
    }

    private void setCenterButtonInfo(String text, final OnButtonClickListener onButtonClickListener) {
        leftBtn.setVisibility(View.GONE);
        rightBtn.setVisibility(View.GONE);
        centerBtn.setVisibility(View.VISIBLE);
        this.findViewById(R.id.dialog_common_left_line).setVisibility(View.GONE);
        this.findViewById(R.id.dialog_common_right_line).setVisibility(View.GONE);
        this.findViewById(R.id.dialog_common_line).setVisibility(View.VISIBLE);
        this.findViewById(R.id.dialog_common_btn_area).setVisibility(View.VISIBLE);
        if (!TextUtils.isEmpty(text)) centerBtn.setText(text);
        centerBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (onButtonClickListener != null)
                    onButtonClickListener.onClick(v);
                dismiss();
            }

        });
    }

    private void setListViewInfo(BaseAdapter adapter, final OnDialogItemClickListener onDialogItemClickListener) {
        listView.setVisibility(View.VISIBLE);
        this.findViewById(R.id.dialog_common_line).setVisibility(View.GONE);
        this.findViewById(R.id.dialog_common_btn_area).setVisibility(View.GONE);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onDialogItemClickListener != null)
                    onDialogItemClickListener.onItemClick(parent, view, position, id);
                dismiss();
            }

        });
    }

    private void setContentView(int resId) {
        View view = LayoutInflater.from(getActivity()).inflate(resId, contentView, false);
        contentView.addView(view);
    }


    public interface OnButtonClickListener {
        void onClick(View view);
    }

    public interface OnDialogItemClickListener {
        void onItemClick(AdapterView<?> parent, View view, int position, long id);
    }


    public static class Builder {
        String title;
        String message;
        String leftText;
        String rightText;
        String centerText;
        OnButtonClickListener leftButtonClickListener;
        OnButtonClickListener rightButtonClickListener;
        OnButtonClickListener centerButtonClickListener;
        BaseAdapter adapter;
        OnDialogItemClickListener onDialogItemClickListener;
        int contentViewId = 0;
        Context context;

        public CommonDialogFragment create() {
            CommonDialogFragment dialogFragment = new CommonDialogFragment();
            dialogFragment.builder = self();
            return dialogFragment;
        }

        public Builder(Context context) {
            this.context = context;
        }

        public Builder self() {
            return this;
        }

        public Builder setTitle(int resId) {
            this.title = context.getString(resId);
            return self();
        }

        public Builder setTitle(String title) {
            this.title = title;
            return self();
        }

        public Builder setMessage(int resId) {
            this.message = context.getString(resId);
            return self();
        }

        public Builder setMessage(String message) {
            this.message = message;
            return self();
        }

        public Builder setRightButtonInfo(String text, final OnButtonClickListener onButtonClickListener) {
            this.rightText = text;
            this.rightButtonClickListener = onButtonClickListener;
            return self();
        }

        public Builder setLeftButtonInfo(String text, final OnButtonClickListener onButtonClickListener) {
            this.leftText = text;
            this.leftButtonClickListener = onButtonClickListener;
            return self();

        }

        public Builder setCenterButtonInfo(String text, final OnButtonClickListener onButtonClickListener) {
            this.centerText = text;
            this.centerButtonClickListener = onButtonClickListener;
            return self();
        }

        public Builder setContentView(int resId) {
            this.contentViewId = resId;
            return self();
        }

        public Builder setListViewInfo(BaseAdapter adapter, final OnDialogItemClickListener onDialogItemClickListener) {
            this.adapter = adapter;
            this.onDialogItemClickListener = onDialogItemClickListener;
            return self();
        }
    }
}
