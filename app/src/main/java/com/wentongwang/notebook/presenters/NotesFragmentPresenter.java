package com.wentongwang.notebook.presenters;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.fragment.interfaces.NotesView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Wentong WANG on 2016/6/27.
 */
public class NotesFragmentPresenter {

    private NotesView notesView;
    private boolean update = true;

    public NotesFragmentPresenter(NotesView notesView) {
        this.notesView = notesView;
    }

    public void getNotes() {
        if (update) {
            notesView.showPorgressBar();
            Bmob.initialize(notesView.getActivity(), Constants.APPLICATION_ID);

            BmobQuery<NoteItem> query = new BmobQuery<NoteItem>();
            //查询该用户有的notes
            String user_id = AccountUtils.getUserId(notesView.getActivity());
            query.addWhereEqualTo(NoteItem.NOTE_USER_ID, user_id);
            //返回50条数据，如果不加上这条语句，默认返回10条数据
            query.setLimit(50);
            //按更新日期降序排列
            query.order("-updatedAt");
            //执行查询方法
            query.findObjects(notesView.getActivity(), new FindListener<NoteItem>() {
                @Override
                public void onSuccess(List<NoteItem> object) {
                    notesView.hidePorgressBar();
                    List<NoteItem> list = new ArrayList<NoteItem>();
                    if (object.size() > 0) {
                        for (NoteItem noteItem : object) {
                            //获得信息
                            list.add(noteItem);
                        }
                        notesView.hideNoDatas();
                        notesView.updataList(list);
                    } else {
                        notesView.showNoDatas();
                    }
                    update = false;
                }

                @Override
                public void onError(int code, String msg) {
                    notesView.showNoDatas();
                    notesView.hidePorgressBar();
                    update = true;
                    Toast.makeText(notesView.getActivity(), "操作失败: " + msg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * 只有第一次创建这个时候获取信息，以防在fragment暂时放到后台恢复时再次进行多余的请求
     * @param is
     */
    public void canUpdateNotes(boolean is) {
        update = is;
    }

    /**
     * 删除一条note
     * @param position
     */
    public void deleteNote(final int position) {
        final List<NoteItem> list = notesView.getNotesList();
        String id = list.get(position).getObjectId();
        NoteItem item = new NoteItem();
        item.setObjectId(id);
        item.delete(notesView.getActivity(), new DeleteListener() {
            @Override
            public void onSuccess() {
                MyToast.showLong(notesView.getActivity(), "删除成功");
                list.remove(position);
                notesView.updataList(list);
            }

            @Override
            public void onFailure(int code, String msg) {
                MyToast.showLong(notesView.getActivity(), "删除失败" + msg);
            }
        });
    }
}
