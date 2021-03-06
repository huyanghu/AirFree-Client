package me.lancer.airfree.adapter;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.nineoldandroids.animation.AnimatorSet;
import com.nineoldandroids.animation.ObjectAnimator;

import java.util.List;

import me.lancer.distance.R;
import me.lancer.airfree.bean.MobileBean;

public class DocumentAdapter extends BaseAdapter {

    private List<MobileBean> fileList;
    private List<String> searchList;
    private List<String> posList;
    private Handler mHandler;
    protected LayoutInflater mInflater;

    public DocumentAdapter(Context context, List<MobileBean> fileList, List<String> posList, List<String> searchList, Handler mHandler) {
        this.fileList = fileList;
        this.posList = posList;
        this.searchList = searchList;
        this.mHandler = mHandler;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return fileList.size();
    }

    @Override
    public Object getItem(int position) {
        return fileList.get(position);
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;

        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.lv_doc_item, null);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            viewHolder.tvCount = (TextView) convertView.findViewById(R.id.tv_count);
            viewHolder.tvDate = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.mCheckBox = (CheckBox) convertView.findViewById(R.id.cb_music);

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.tvName.setText(fileList.get(position).getFileName());
        viewHolder.tvCount.setVisibility(View.GONE);
        viewHolder.tvDate.setText(fileList.get(position).getFileDate());
        viewHolder.mCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.obj = "" + position;
                mHandler.sendMessage(msg);
            }
        });
        viewHolder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                addAnimation(viewHolder.mCheckBox);
            }
        });
        viewHolder.mCheckBox.setChecked(posList.contains("" + position) ? true : false);
        viewHolder.mCheckBox.bringToFront();

        String fileName = fileList.get(position).getFileName();
        if (searchList.size() > 0) {
            String keyword = searchList.get(0);
            if ((fileName != null && fileName.contains(keyword))) {
                ForegroundColorSpan span = new ForegroundColorSpan(Color.RED);
                SpannableStringBuilder builder1 = new SpannableStringBuilder(fileName);
                int index1 = fileName.indexOf(keyword);
                if (index1 != -1) {
                    builder1.setSpan(span, index1, index1 + keyword.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }
                viewHolder.tvName.setText(builder1);
            } else {
                viewHolder.tvName.setText(fileName);
            }
        }

        return convertView;
    }

    private void addAnimation(View view) {
        float[] vaules = new float[]{0.1f, 0.2f, 0.3f, 0.4f, 0.5f, 0.6f, 0.5f, 0.6f, 0.65f, 0.7f, 0.8f, 0.7f, 0.65f, 0.6f, 0.5f};
        AnimatorSet set = new AnimatorSet();
        set.playTogether(ObjectAnimator.ofFloat(view, "scaleX", vaules), ObjectAnimator.ofFloat(view, "scaleY", vaules));
        set.setDuration(150);
        set.start();
    }

    public static class ViewHolder {
        public TextView tvName;
        public TextView tvCount;
        public TextView tvDate;
        public CheckBox mCheckBox;
    }
}
