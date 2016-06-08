package com.wentongwang.notebook.fragment;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.activity.CreateNoteActivity;
import com.wentongwang.notebook.activity.EditNoteActivity;
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.DatabaseUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;


/**
 * 便签页面
 * Created by Wentong WANG on 2016/6/3.
 */
public class NotesFragment extends Fragment {

    private ListView listView;
    private MyListViewAdapter adapter;
    private List<NoteItem> listNotes;
    //listview上一个显示的条目
    private int lastItemPosition;
    private Button addBtn;

    private TextView nodata;
    private boolean isBtnShow = true;

    //数据库操作工具
    private DatabaseUtils databaseUtils;

    /**
     * 用于刷新listview的
     * @param event
     */
    @Subscribe
    public void onEventBackgroundThread(UpdataEvent event) {
        if (event.getType() == UpdataEvent.UPDATE_NOTES) {
            getNotes();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        //注册EventBus
        EventBus.getDefault().register(this);
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notes_fragment_layout, container, false);
        initData();
        initViews(root);
        initEvents();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (databaseUtils != null) {
            getNotes();
        }
    }

    private void initData() {

        databaseUtils = new DatabaseUtils(getActivity());
        listNotes = new ArrayList<>();
//        if (databaseUtils != null) {
//            listNotes = new ArrayList<>();
//            getNotes();
//        }
    }

    /**
     * 从数据库中提取notes
     */
    private void getNotes(){
        listNotes = databaseUtils.getNotes();
        if (listNotes == null || listNotes.size() == 0) {
            nodata.setVisibility(View.VISIBLE);
        } else {
            nodata.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    private void initViews(View root) {
        nodata = (TextView) root.findViewById(R.id.tv_no_data);

        listView = (ListView) root.findViewById(R.id.notes_listview);
        adapter = new MyListViewAdapter();
        listView.setAdapter(adapter);

        addBtn = (Button) root.findViewById(R.id.add_btn);
    }


    private void initEvents() {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (lastItemPosition != firstVisibleItem) {
                    //写这个为了实现，下滑隐藏右下角的按钮
                    if (isBtnShow && firstVisibleItem > lastItemPosition) {
                        //listview向下滑动
                        hideBtnAnim(addBtn);
                    } else if (!isBtnShow && firstVisibleItem < lastItemPosition) {
                        showBtnAnim(addBtn);
                    }
                    lastItemPosition = firstVisibleItem;
                }
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(getActivity(), CreateNoteActivity.class);
                startActivity(it);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), EditNoteActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("my_note", listNotes.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    /**
     * 隐藏动画
     * @param view
     */
    private void hideBtnAnim(View view) {
        //获取按键初始化后在界面中的Y坐标
        int btnY = addBtn.getTop();

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", btnY + 50 + addBtn.getMeasuredHeight());
        animator.setDuration(500);
        animator.start();

        isBtnShow = false;

    }

    /**
     * 显示出来的动画
     * @param view
     */
    private void showBtnAnim(View view) {
        int btnY = addBtn.getTop();

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", btnY);
        animator.setDuration(1000);
        animator.setInterpolator(new BounceInterpolator());
        animator.start();

        isBtnShow = true;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //注销EventBus
        EventBus.getDefault().unregister(this);
    }

    private class MyListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return listNotes.size();
        }

        @Override
        public Object getItem(int position) {
            return listNotes.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.notes_listview_item, null);
                holder = new ViewHolder();

                holder.note_content = (TextView) convertView.findViewById(R.id.note_content);
                holder.note_date = (TextView) convertView.findViewById(R.id.note_date);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            NoteItem item = new NoteItem();
            item = listNotes.get(position);

            holder.note_date.setText(item.getNote_date());
            holder.note_content.setText(item.getNote_content());

            return convertView;
        }

        private class ViewHolder {
            TextView note_date;
            TextView note_content;
        }
    }
}
