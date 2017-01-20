package com.android.huxq17.rascalkiller.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.huxq17.rascalkiller.MainActivity;
import com.android.huxq17.rascalkiller.R;
import com.android.huxq17.rascalkiller.bean.AppInfo;
import com.andview.refreshview.recyclerview.BaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class AppListAdapter extends BaseRecyclerAdapter<AppListAdapter.SimpleAdapterViewHolder> {
    private List<AppInfo> list;
    private boolean isSelectMode = false;
    private MainActivity parent;
    private List<String> selectedApp = new ArrayList<>();
    private OnClickListener clickListener;

    public AppListAdapter(List<AppInfo> list, MainActivity activity) {
        this.list = list;
        this.parent = activity;
    }

    public void onBindViewHolder(final SimpleAdapterViewHolder holder,
                                 int position, boolean isItem) {
        final AppInfo appInfo = list.get(position);
        holder.appName.setText(appInfo.appName);
        holder.appIcon.setImageDrawable(appInfo.appIcon);
        holder.appProcessNum.setText("进程数:" + appInfo.getProcessNum());
        if (longClickListener != null) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    longClickListener.onItemLongClick(v, appInfo.packageName);
                    if (!isSelectMode) {
                        v.setSelected(true);
                        parent.startSupportActionMode();
                        isSelectMode = true;
                        selectApp(appInfo.packageName);
                        return true;
                    }
                    return !isSelectMode ? true : false;
                }
            });
        }
        if (!isSelectMode) {
            holder.itemView.setSelected(false);
            unSelectApp(appInfo.packageName);
        }
        if (isSelectMode) {
            holder.itemView.setSelected(selectedApp.contains(appInfo.packageName));
        }
        if (clickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isSelectMode) {
                        if (v.isSelected()) {
                            unSelectApp(appInfo.packageName);
                        } else {
                            selectApp(appInfo.packageName);
                        }
                        v.setSelected(!v.isSelected());
                    }
                    clickListener.onClick(v, appInfo.packageName);
                }
            });
        }
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View v, String packageName);
    }

    public interface OnClickListener {
        void onClick(View v, String packageName);
    }

    public List<String> getSelectedApp() {
        List<String> selected  = new ArrayList<>();
        selected.addAll(selectedApp);
        return selected;
    }

    public void existActionMode() {
        isSelectMode = false;
        notifyDataSetChanged();
    }

    public void selectApp(String packageName) {
        if (!selectedApp.contains(packageName))
            selectedApp.add(packageName);
    }

    public void unSelectApp(String packageName) {
        selectedApp.remove(packageName);
    }

    public void unSelectAllapp() {
        selectedApp.clear();
    }

    private OnItemLongClickListener longClickListener;

    public void setOnLongClickListener(OnItemLongClickListener listener) {
        longClickListener = listener;
    }

    public void setOnClickListener(OnClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public void setData(ArrayList<AppInfo> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getAdapterItemCount() {
        if (list == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public SimpleAdapterViewHolder getViewHolder(View view) {
        return new SimpleAdapterViewHolder(view, false);
    }

    @Override
    public SimpleAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType, boolean isItem) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_applist, parent, false);

        SimpleAdapterViewHolder vh = new SimpleAdapterViewHolder(v, true);
        return vh;
    }

    public class SimpleAdapterViewHolder extends RecyclerView.ViewHolder {

        public TextView appName;
        public ImageView appIcon;
        public TextView appProcessNum;

        public SimpleAdapterViewHolder(View itemView, boolean isItem) {
            super(itemView);
            if (isItem) {
                appName = (TextView) itemView.findViewById(R.id.tv_appname);
                appIcon = (ImageView) itemView.findViewById(R.id.iv_appicon);
                appProcessNum = (TextView) itemView.findViewById(R.id.tv_process_num);
            }
        }
    }

    public void insert(AppInfo person, int position) {
        insert(list, person, position);
    }

    public void remove(int position) {
        remove(list, position);
    }

    public void clear() {
        clear(list);
    }

    public AppInfo getItem(int position) {
        if (position < list.size())
            return list.get(position);
        else
            return null;
    }

}