package com.wentongwang.notebook.view.fragment;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.presenters.NotesFragmentPresenter;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.activity.CreateNoteActivity;
import com.wentongwang.notebook.view.activity.EditNoteActivity;
import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.UpdataEvent;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.view.fragment.interfaces.NotesView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;


/**
 * 便签页面
 * Created by Wentong WANG on 2016/6/3.
 */
public class NotesFragment extends Fragment implements NotesView{

    private ListView listView;
    private MyListViewAdapter adapter;
    private List<NoteItem> listNotes;
    //listview上一个显示的条目
    private int lastItemPosition;
    //添加按钮
    private Button addBtn;
    private int btnY;
    //搜索框
    private EditText etFilter;
    //判断是否显示右下角的按钮
    private boolean isBtnShow = true;
    //无内容时的显示
    private TextView nodata;
    //进度条
    private View progressBar;

    private NotesFragmentPresenter mPresenter = new NotesFragmentPresenter(this);
    /**
     * 用于刷新listview的
     *
     * @param event
     */
    @Subscribe
    public void onEventBackgroundThread(UpdataEvent event) {
        if (event.getType() == UpdataEvent.UPDATE_NOTES) {
            mPresenter.canUpdateNotes(true);
            mPresenter.getNotes();
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onAttach(Context context) {
        //注册EventBus
        EventBus.getDefault().register(this);
        mPresenter.canUpdateNotes(true);
        super.onAttach(context);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.notes_fragment_layout, container, false);
        progressBar = root.findViewById(R.id.progress_bar);
        initData();
        initViews(root);
        initEvents();
        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (listNotes != null && listNotes.size() == 0) {
            mPresenter.getNotes();
        }

    }

    private void initData() {
        listNotes = new ArrayList<>();
    }

    private void initViews(View root) {
        nodata = (TextView) root.findViewById(R.id.tv_no_data);

        listView = (ListView) root.findViewById(R.id.notes_listview);
        adapter = new MyListViewAdapter();
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(true);

        addBtn = (Button) root.findViewById(R.id.add_btn);

        etFilter = (EditText) root.findViewById(R.id.et_filter_string);


    }


    private void initEvents() {
        //获取按钮的位置
        addBtn.post(new Runnable() {

            @Override

            public void run() {
                int[] position = new int[2];
                addBtn.getLocationInWindow(position);
                btnY = position[1];
                System.out.println("getLocationInWindow:" + position[0] + "," + position[1]);
            }

        });

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
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                showDeleteDialog(position);
                return true;
            }
        });
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
                .setMessage("是否要删除这条便签")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mPresenter.deleteNote(position);
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
     * 隐藏动画
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
     * 显示出来的动画
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
    }

    @Override
    public void onDetach() {
        super.onDetach();
        //注销EventBus
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

    /**
     * 获取listnotes
     * @return
     */
    @Override
    public List<NoteItem> getNotesList() {
        return listNotes;
    }

    /**
     * 更新listview
     * @param list
     */
    @Override
    public void updataList(List<NoteItem> list) {
        listNotes = list;
        adapter.notifyDataSetChanged();
    }

    private class MyListViewAdapter extends BaseAdapter implements Filterable {
        private List<NoteItem> mOriginalValues;

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

            NoteItem item;
            item = listNotes.get(position);

            holder.note_date.setText(item.getNote_date());
            holder.note_content.setText(item.getNote_content());

            return convertView;
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {

                @SuppressWarnings("unchecked")
                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {

                    listNotes = (List<NoteItem>) results.values; // 将筛选出的内容赋值
                    notifyDataSetChanged();  // 刷新listview
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults results = new FilterResults();        // 要返回的筛选内容
                    List<NoteItem> filteredArrList = new ArrayList<>(); //保存筛选结果的

                    if (mOriginalValues == null) {
                        mOriginalValues = new ArrayList<NoteItem>(listNotes); // 保存原有的数据
                    }

                    if (constraint == null || constraint.length() == 0) {
                        //如果没有输入，则显示原来的列表
                        results.count = mOriginalValues.size();
                        results.values = mOriginalValues;
                    } else {
                        //如果有输入，获取输入的内容
                        constraint = constraint.toString().toLowerCase();
                        //遍历listview,判断是否有含有该内容的item保存到filteredArrList中
                        int listSize = mOriginalValues.size();
                        for (int i = 0; i < listSize; i++) {
                            String content = mOriginalValues.get(i).getNote_content();
                            String data = mOriginalValues.get(i).getNote_date();
                            if (data.toLowerCase().startsWith(constraint.toString()) || content.toLowerCase().startsWith(constraint.toString())) {
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
            TextView note_date;
            TextView note_content;
        }
    }


}
