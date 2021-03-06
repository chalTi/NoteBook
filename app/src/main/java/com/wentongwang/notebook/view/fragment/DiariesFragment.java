package com.wentongwang.notebook.view.fragment;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.presenters.DiariesFragmentPresenter;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.CreateDiaryActivity;
import com.wentongwang.notebook.view.activity.EditDiaryActivity;
import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.view.fragment.interfaces.DiariesView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Wentong WANG on 2016/6/3.
 */
public class DiariesFragment extends Fragment implements DiariesView {
    //显示日记的list
    private ListView listView;
    //日记list的适配器
    private MyListViewAdapter adapter;
    //添加日记按钮
    private Button addBtn;
    //listview上一个显示的条目,用于判断滑动
    private int lastItemPosition;
    //用于控制是否显示添加按钮
    private boolean isBtnShow = true;
    //日记items
    private List<DiaryItem> diaryItems;
    //无内容时候显示
    private TextView nodata;
    //搜索框
    private EditText etFilter;
    //进度条
    private View progressBar;

    private DiariesFragmentPresenter mPresenter = new DiariesFragmentPresenter(this);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        EventBus.getDefault().register(this);
        mPresenter.canUpdateNotes(true);
    }

    @Subscribe
    public void onEventBackgroundThread(UpdataEvent event) {
        if (event.getType() == UpdataEvent.UPDATE_DIARIES) {
            mPresenter.canUpdateNotes(true);
            mPresenter.getDiaries();
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
        progressBar = root.findViewById(R.id.progress_bar);
        initDatas();
        initViews(root);
        initEvents();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (diaryItems != null && diaryItems.size() == 0) {
            mPresenter.getDiaries();
        }
    }

    private void initDatas() {
        diaryItems = new ArrayList<>();
    }

    private void initViews(View root) {
        nodata = (TextView) root.findViewById(R.id.tv_no_data);

        listView = (ListView) root.findViewById(R.id.diaries_listview);
        adapter = new MyListViewAdapter();
        listView.setAdapter(adapter);

        addBtn = (Button) root.findViewById(R.id.add_btn);

        etFilter = (EditText) root.findViewById(R.id.et_filter_string);

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
                mPresenter.goToEditDiaryActivity(position);
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
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

        //监听editText的输入变化,用于筛选
        etFilter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.getFilter().filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 显示是否删除
     */
    private void showDeleteDialog(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle("删除")
                .setIcon(R.drawable.delete)
                .setMessage("是否要删除这条日记")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteDiaray(position);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.create().show();
    }


    /**
     * 隐藏按钮
     *
     * @param view
     */
    private void hideBtnAnim(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 50 + addBtn.getMeasuredHeight());
        animator.setDuration(500);
        animator.start();

        isBtnShow = false;

    }

    /**
     * 显示按钮
     *
     * @param view
     */
    private void showBtnAnim(View view) {

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", 0);
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

    /**
     * 显示进度条
     */
    @Override
    public void showPorgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏进度条
     */
    @Override
    public void hidePorgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showNoDatas() {
        nodata.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideNoDatas() {
        nodata.setVisibility(View.GONE);
    }

    @Override
    public List<DiaryItem> getDiariesList() {
        return diaryItems;
    }

    /**
     * 更新列表
     *
     * @param list
     */
    @Override
    public void updataList(List<DiaryItem> list) {
        diaryItems = list;
        adapter.notifyDataSetChanged();
    }

    /**
     * 跳转到日记详情界面
     *
     * @param diaryItem
     */
    @Override
    public void startDiaryActivity(DiaryItem diaryItem) {
        Intent intent = new Intent();
        intent.setClass(getActivity(), EditDiaryActivity.class);

        Bundle bundle = new Bundle();
        //传递该条目所对应的日记
        bundle.putSerializable("my_diary", diaryItem);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private class MyListViewAdapter extends BaseAdapter implements Filterable {
        private List<DiaryItem> mOriginalValues;

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

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    diaryItems = (List<DiaryItem>) results.values; // 将筛选出的内容赋值
                    notifyDataSetChanged();  // 刷新listview
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // 要返回的筛选内容
                    List<DiaryItem> filteredArrList = new ArrayList<>(); //保存筛选结果的

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<DiaryItem>(diaryItems); // 保存原有的数据
                    }

                    if (constraint == null || constraint.length() == 0) {
                        //如果没有输入，则显示原来的列表
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        //如果有输入，获取输入的内容
                        constraint = constraint.toString().toLowerCase();
                        //遍历listview,判断是否有含有该内容的item保存到filteredArrList中
                        int listSize = mOriginalValues.size(); //这样做优化了在for循环中每一次都要调用.size()
                        for (int i = 0; i < listSize; i++) {
                            String title = mOriginalValues.get(i).getDiary_title();
                            String data = mOriginalValues.get(i).getDiary_date();
                            if (data.toLowerCase().startsWith(constraint.toString()) || title.toLowerCase().startsWith(constraint.toString())) {
                                filteredArrList.add(mOriginalValues.get(i));
                            }
                        }
                        //将筛选完的内容存到result中
                        results.count = filteredArrList.size();
                        results.values = filteredArrList;
                    }
                    return results;
                }
            };
            return filter;
        }

        private class ViewHolder {
            TextView diary_date;
            TextView diary_title;
            ImageView islocked;
        }
    }
}
