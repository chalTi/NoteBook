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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.activity.CreateDiaryActivity;
import com.wentongwang.notebook.activity.EditDiaryActivity;
import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.SPUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Wentong WANG on 2016/6/3.
 */
public class DiariesFragment extends Fragment {

    private ListView listView;
    private MyListViewAdapter adapter;
    private Button addBtn;

    //listview上一个显示的条目
    private int lastItemPosition;

    private boolean isBtnShow = true;

    private List<DiaryItem> diaryItems;

    private TextView nodata;
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
    }

    @Subscribe
    public void onEventBackgroundThread(UpdataEvent event) {
        if (event.getType() == UpdataEvent.UPDATE_DIARIES) {
            getDiaries();
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.diaries_fragment_layout, container, false);
        initDatas();
        initViews(root);
        initEvents();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDiaries();
    }

    private void initDatas() {

        diaryItems = new ArrayList<>();
    }
    /**
     * 获取日记信息
     */
    private void getDiaries(){

        Bmob.initialize(getActivity(), Constants.APPLICATION_ID);

        BmobQuery<DiaryItem> query = new BmobQuery<DiaryItem>();
        //查询该用户有的diaries
        String user_id = (String) SPUtils.get(getActivity(), "user_id", "");
        query.addWhereEqualTo(DiaryItem.DIARY_USER_ID, user_id);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //按更新日期降序排列
        query.order("-updatedAt");
        //执行查询方法
        query.findObjects(getActivity(), new FindListener<DiaryItem>() {
            @Override
            public void onSuccess(List<DiaryItem> object) {
                diaryItems.clear();
                if (object.size() > 0) {
                    for (DiaryItem item : object) {
                        //获得信息
                        diaryItems.add(item);
                    }
                    nodata.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                } else {
                    nodata.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onError(int code, String msg) {
                nodata.setVisibility(View.VISIBLE);
                Toast.makeText(getActivity(), "操作失败: " + msg, Toast.LENGTH_LONG).show();
            }
        });


    }
    private void initViews(View root) {
        nodata = (TextView) root.findViewById(R.id.tv_no_data);

        listView = (ListView) root.findViewById(R.id.diaries_listview);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), EditDiaryActivity.class);

                Bundle bundle = new Bundle();
                bundle.putSerializable("my_diary", diaryItems.get(position));
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(getActivity(), CreateDiaryActivity.class);
                startActivity(it);
            }
        });
    }


    private void hideBtnAnim(View view) {
        //获取按键初始化后在界面中的Y坐标
        int btnY = addBtn.getTop();

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "y", btnY + 50 + addBtn.getMeasuredHeight());
        animator.setDuration(500);
        animator.start();

        isBtnShow = false;

    }

    private void showBtnAnim(View view){
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
        EventBus.getDefault().unregister(this);
    }

    private class MyListViewAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return diaryItems.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(getActivity()).inflate(R.layout.diaries_listview_item, null);
                holder = new ViewHolder();

                holder.diary_title = (TextView) convertView.findViewById(R.id.diary_title);
                holder.diary_date = (TextView) convertView.findViewById(R.id.diary_date);
                holder.islocked = (ImageView) convertView.findViewById(R.id.iv_locked_icon);

                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            DiaryItem diaryItem = diaryItems.get(position);

            holder.diary_title.setText(diaryItem.getDiary_title());
            holder.diary_date.setText(diaryItem.getDiary_date());
            if (diaryItem.isLockedInBoolean()) {
                holder.islocked.setVisibility(View.VISIBLE);
            } else {
                holder.islocked.setVisibility(View.GONE);
            }

            return convertView;
        }

        private class ViewHolder {
            TextView diary_date;
            TextView diary_title;
            ImageView islocked;
        }
    }
}
