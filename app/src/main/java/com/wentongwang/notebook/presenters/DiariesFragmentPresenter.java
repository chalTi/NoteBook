package com.wentongwang.notebook.presenters;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.wentongwang.notebook.R;
import com.wentongwang.notebook.model.Constants;
import com.wentongwang.notebook.model.DiaryItem;
import com.wentongwang.notebook.utils.AccountUtils;
import com.wentongwang.notebook.utils.MyToast;
import com.wentongwang.notebook.view.fragment.interfaces.DiariesView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by Wentong WANG on 2016/6/27.
 */
public class DiariesFragmentPresenter {

    private DiariesView diariesView;

    private boolean update = true;

    public DiariesFragmentPresenter(DiariesView diariesView) {
        this.diariesView = diariesView;
    }

    /**
     * 只有第一次创建这个时候获取信息，以防在fragment暂时放到后台恢复时再次进行多余的请求
     * @param is
     */
    public void canUpdateNotes(boolean is) {
        update = is;
    }

    /**
     * 获取日记信息
     */
    public void getDiaries(){
        diariesView.showPorgressBar();
        BmobQuery<DiaryItem> query = new BmobQuery<DiaryItem>();
        //查询该用户有的diaries
        String user_id = AccountUtils.getUserId(diariesView.getActivity());
        query.addWhereEqualTo(DiaryItem.DIARY_USER_ID, user_id);
        //返回50条数据，如果不加上这条语句，默认返回10条数据
        query.setLimit(50);
        //按更新日期降序排列
        query.order("-updatedAt");
        //执行查询方法
        query.findObjects(diariesView.getActivity(), new FindListener<DiaryItem>() {
            @Override
            public void onSuccess(List<DiaryItem> object) {
                diariesView.hidePorgressBar();
                List<DiaryItem> list = new ArrayList<DiaryItem>();
                if (object.size() > 0) {
                    for (DiaryItem item : object) {
                        //获得信息
                        list.add(item);
                    }
                    diariesView.hideNoDatas();
                    diariesView.updataList(list);
                } else {
                    diariesView.showNoDatas();
                }
                update = false;
            }

            @Override
            public void onError(int code, String msg) {
                update = true;
                diariesView.hidePorgressBar();
                diariesView.showNoDatas();
                Toast.makeText(diariesView.getActivity(), "操作失败: " + msg, Toast.LENGTH_LONG).show();
            }
        });
    }

    /**
     * 删除一条日记
     * @param position
     */
    public void deleteDiaray(final int position) {
        final List<DiaryItem> list = diariesView.getDiariesList();
        String id = list.get(position).getObjectId();
        DiaryItem item = new DiaryItem();
        item.setObjectId(id);
        item.delete(diariesView.getActivity(), new DeleteListener() {
            @Override
            public void onSuccess() {
                MyToast.showLong(diariesView.getActivity(), "删除成功");
                list.remove(position);
                diariesView.updataList(list);
            }

            @Override
            public void onFailure(int code, String msg) {
                MyToast.showLong(diariesView.getActivity(), "删除失败" + msg);
            }
        });
    }

    /**
     * 跳转到修改日记的界面
     * @param position
     */
    public void goToEditDiaryActivity(int position) {
        DiaryItem item = diariesView.getDiariesList().get(position);
        if (item.isLockedInBoolean()){
            checkDiaryPwd(item);
        }else{
            diariesView.startDiaryActivity(item);
        }

    }


    /**
     * 判断是否可以进入加密的日记
     */
    private void checkDiaryPwd(final DiaryItem diaryItem) {
        View layout;
        final EditText et_pwd;

        layout = LayoutInflater.from(diariesView.getActivity()).inflate(R.layout.input_diarypwd_dialog_layout, null);
        et_pwd = (EditText) layout.findViewById(R.id.et_input_diarypwd);

        AlertDialog.Builder inputPwdBuilder = new AlertDialog.Builder(diariesView.getActivity())
                .setTitle("密码确认")
                .setView(layout)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String diaryPwd = AccountUtils.getUserDiaryPwd(diariesView.getActivity());
                        String pwd = et_pwd.getText().toString();
                        if (pwd.equals(diaryPwd)) {
                            diariesView.startDiaryActivity(diaryItem);
                        } else {
                            Toast.makeText(diariesView.getActivity(), "密码不正确，重新输入", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        inputPwdBuilder.create().show();
    }

}
