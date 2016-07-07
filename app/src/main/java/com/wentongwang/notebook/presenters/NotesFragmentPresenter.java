package com.wentongwang.notebook.presenters;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.NoteItem;
import com.wentongwang.notebook.model.Response;
import com.wentongwang.notebook.model.business.NoteBiz;
import com.wentongwang.notebook.model.business.OnResponseListener;
import com.wentongwang.notebook.model.business.UserBiz;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.fragment.interfaces.NotesView;

import java.util.ArrayList;
import java.util.Collection;
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
    private NoteBiz noteBiz;
    private UserBiz userBiz;

    public NotesFragmentPresenter(NotesView notesView) {
        this.notesView = notesView;
        noteBiz = new NoteBiz();
        userBiz = new UserBiz();
    }

    /**
     * 获取用户所有的便签
     */
    public void getNotes() {
        if (update) {
            notesView.showPorgressBar();

            //查询该用户有的notes
            String user_id = AccountUtils.getUserId(notesView.getActivity());

            userBiz.getUserNotes(notesView.getActivity(), user_id, new OnResponseListener() {

                @Override
                public void onResponse(Response response) {
                    notesView.hidePorgressBar();
                    if (response.isSucces()) {
                        List<NoteItem> list = new ArrayList<NoteItem>();
                        list.addAll(response.getNoteItemList());
                        if (list.size() > 0) {
                            notesView.hideNoDatas();
                            notesView.updataList(list);
                        } else {
                            notesView.showNoDatas();
                        }
                        update = false;
                    } else {
                        notesView.showNoDatas();
                        update = true;
                        MyToast.showLong(notesView.getActivity(), "操作失败: " + response.getMsg());
                    }
                }
            });
        }
    }

    /**
     * 只有第一次创建这个时候获取信息，以防在fragment暂时放到后台恢复时再次进行多余的请求
     *
     * @param is
     */
    public void canUpdateNotes(boolean is) {
        update = is;
    }

    /**
     * 删除一条note
     *
     * @param position
     */
    public void deleteNote(final int position) {
        final List<NoteItem> list = notesView.getNotesList();
        String id = list.get(position).getObjectId();

        if (TextUtils.isEmpty(id)) {
            MyToast.showLong(notesView.getActivity(), "客户端数据有误，删除失败，请重新登录");
            return;
        }

        notesView.showPorgressBar();
        noteBiz.deleteNote(notesView.getActivity(), id, new OnResponseListener() {
            @Override
            public void onResponse(Response response) {
                notesView.hidePorgressBar();
                if (response.isSucces()) {
                    MyToast.showShort(notesView.getActivity(), "删除成功");
                    list.remove(position);
                    if (list.size() == 0) {
                        notesView.showNoDatas();
                    } else {
                        notesView.hideNoDatas();
                    }
                    notesView.updataList(list);
                } else {
                    MyToast.showLong(notesView.getActivity(), "删除失败" + response.getMsg());
                }
            }
        });
    }
}
