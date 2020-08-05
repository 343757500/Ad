package com.lvchuan.ad.view.fragment;

import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;
import com.just.agentweb.AgentWeb;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloadQueueSet;
import com.liulishuo.filedownloader.FileDownloadSampleListener;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.util.FileDownloadUtils;
import com.lvchuan.ad.LocalVImageHolderView;
import com.lvchuan.ad.MainActivity;
import com.lvchuan.ad.R;
import com.lvchuan.ad.base.BaseFragment;
import com.lvchuan.ad.bean.AdEntity;
import com.lvchuan.ad.utils.FileUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdFragment extends BaseFragment implements View.OnClickListener , OnItemClickListener {
    private AgentWeb agentWeb;
    private LinearLayout lin_web;
    private ConvenientBanner adBanner;
    List<AdEntity> bannerList = new ArrayList<>();


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_ad;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        lin_web = findView(R.id.lin_web);
        adBanner = findView(R.id.adBanner);
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(lin_web, new LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()//进度条
                .createAgentWeb()
                .ready()
                .go("https://120.78.175.246/apps/#/?boxCode=LZ02-664342");

        AdEntity adEntity1 = new AdEntity();
        adEntity1.setImgHref("https://dummyimage.com/600x400/00ff00/0011ff.png&text=HelloWorld");
        adEntity1.setId(0);
        AdEntity adEntity2 = new AdEntity();
        adEntity2.setId(1);
        adEntity2.setImgHref("https://stream7.iqilu.com/10339/upload_transcode/202002/17/20200217101826WjyFCbUXQ2.mp4");
        AdEntity adEntity = new AdEntity();
        adEntity.setId(2);
        adEntity.setImgHref("http://mmbiz.qpic.cn/mmbiz/PwIlO51l7wuFyoFwAXfqPNETWCibjNACIt6ydN7vw8LeIwT7IjyG3eeribmK4rhibecvNKiaT2qeJRIWXLuKYPiaqtQ/0");
        bannerList.add(adEntity1);
        bannerList.add(adEntity2);
        bannerList.add(adEntity);


        adBanner.setPages(new CBViewHolderCreator() {
            @Override
            public Holder createHolder(View itemView) {
                LocalVImageHolderView localImageHolderView = new LocalVImageHolderView(itemView);
                localImageHolderView.setCallback((curId, time) -> {
                    handleAdTurn(curId, time);
                });
                return localImageHolderView;
            }

            @Override
            public int getLayoutId() {
                return R.layout.banner_item;
            }
        }, bannerList) .setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                .setPointViewVisible(true).setCanLoop(true).setOnItemClickListener(this);


    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {

        initDownLoad();
    }

    @Override
    public void onClick(View v, int id) {

    }




    private void handleAdTurn(int curId, long time) {
        Log.e("MainActivity",curId+"::"+time);
        if (adBanner != null) {
            adBanner.postDelayed(() -> {
                if (curId < bannerList.size() - 1) {
                    if (adBanner != null) adBanner.setCurrentItem(curId + 1, false);
                } else {
                    if (adBanner != null) adBanner.notifyDataSetChanged();
                }
            }, time);
        }
    }

    @Override
    public void onItemClick(int position) {

    }


    private void initDownLoad() {
        start_multi();
    }


    //netty接收到广告更换的广播
    @Subscriber(tag = "handlecmd")
    public void handlecmd(String message) {
        //JSONObject jsonObject = JSONObject.parseObject(message);

        if (message.equals("0")){
            lin_web.setVisibility(View.GONE);
            adBanner.setVisibility(View.VISIBLE);

        }else if (message.equals("1")){
            adBanner.setVisibility(View.GONE);
            lin_web.setVisibility(View.VISIBLE);
            agentWeb = AgentWeb.with(this)
                    .setAgentWebParent(lin_web, new LinearLayout.LayoutParams(-1, -1))
                    .useDefaultIndicator()//进度条
                    .createAgentWeb()
                    .ready()
                    .go("http://120.78.175.246/apps/#/?boxCode=LZ02-584300");

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_1:
                EventBus.getDefault().post("0","handlecmd");
                break;
            case R.id.bt_2:
                EventBus.getDefault().post("1","handlecmd");
                break;
        }
    }


    public String mSaveFolder = FileDownloadUtils.getDefaultSaveRootPath()+File.separator+"feifei_save";
    // 多任务下载
    private FileDownloadListener downloadListener;

    public FileDownloadListener createLis(){
        return new FileDownloadSampleListener(){
            @Override
            protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if(task.getListener() != downloadListener){
                    return;
                }
                Log.d("feifei","pending taskId:"+task.getId()+",fileName:"+task.getFilename()+",soFarBytes:"+soFarBytes+",totalBytes:"+totalBytes+",percent:"+soFarBytes*1.0/totalBytes);

            }

            @Override
            protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if(task.getListener() != downloadListener){
                    return;
                }
                Log.d("feifei","progress taskId:"+task.getId()+",fileName:"+task.getFilename()+",soFarBytes:"+soFarBytes+",totalBytes:"+totalBytes+",percent:"+soFarBytes*1.0/totalBytes+",speed:"+task.getSpeed());
            }

            @Override
            protected void blockComplete(BaseDownloadTask task) {
                if(task.getListener() != downloadListener){
                    return;
                }
                Log.d("feifei","blockComplete taskId:"+task.getId()+",filePath:"+task.getPath()+",fileName:"+task.getFilename()+",speed:"+task.getSpeed()+",isReuse:"+task.reuse());
            }

            @Override
            protected void completed(BaseDownloadTask task) {
                if(task.getListener() != downloadListener){
                    return;
                }
                Log.d("feifei","completed taskId:"+task.getId()+",isReuse:"+task.reuse());
            }

            @Override
            protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                if(task.getListener() != downloadListener){
                    return;
                }
                Log.d("feifei","paused taskId:"+task.getId()+",soFarBytes:"+soFarBytes+",totalBytes:"+totalBytes+",percent:"+soFarBytes*1.0/totalBytes);
            }

            @Override
            protected void error(BaseDownloadTask task, Throwable e) {
                if(task.getListener() != downloadListener){
                    return;
                }
                Log.d("feifei","error taskId:"+task.getId()+",e:"+e.getLocalizedMessage());
            }

            @Override
            protected void warn(BaseDownloadTask task) {
                if(task.getListener() != downloadListener){
                    return;
                }
                Log.d("feifei","warn taskId:"+task.getId());
            }
        };
    }

    public void start_multi() {
        FileUtil fileUtil = new FileUtil(getActivity());
        try {
            File adShow = fileUtil.createSDDir("AdShow");
            if (fileUtil.isFileExist("AdShow")) {
                downloadListener = createLis();
                //(1) 创建 FileDownloadQueueSet
                final FileDownloadQueueSet queueSet = new FileDownloadQueueSet(downloadListener);

                //(2) 创建Task 队列

                final List<BaseDownloadTask> tasks = new ArrayList<>();
                for (int i = 0; i <bannerList.size() ; i++) {
                    Log.e("AdFragment",bannerList.get(i).getImgHref().substring(bannerList.get(i).getImgHref().lastIndexOf("/"),bannerList.get(i).getImgHref().length()));
                    BaseDownloadTask task = FileDownloader.getImpl().create(bannerList.get(i).getImgHref()).setPath(FileUtil.SDPATH + "AdShow" + bannerList.get(i).getImgHref().substring(bannerList.get(i).getImgHref().lastIndexOf("/"),bannerList.get(i).getImgHref().length()));
                    tasks.add(task);
                }

                //(3) 设置参数
                // 每个任务的进度 无回调
                //queueSet.disableCallbackProgressTimes();
                // do not want each task's download progress's callback,we just consider which task will completed.
                queueSet.setCallbackProgressTimes(100);
                queueSet.setCallbackProgressMinInterval(100);
                //失败 重试次数
                queueSet.setAutoRetryTimes(3);

                //避免掉帧
                FileDownloader.enableAvoidDropFrame();

                //(4)串行下载
                queueSet.downloadSequentially(tasks);

                //(5)任务启动
                queueSet.start();
            }
        }catch (Exception e){

        }
    }

    public void stop_multi(){
        FileDownloader.getImpl().pause(downloadListener);
    }

    public void deleteAllFile(){

        //清除所有的下载任务
        FileDownloader.getImpl().clearAllTaskData();

        //清除所有下载的文件
        int count = 0;
        File file = new File(FileDownloadUtils.getDefaultSaveRootPath());
        do {
            if (!file.exists()) {
                break;
            }

            if (!file.isDirectory()) {
                break;
            }

            File[] files = file.listFiles();

            if (files == null) {
                break;
            }

            for (File file1 : files) {
                count++;
                file1.delete();
            }

        } while (false);

        Toast.makeText(getActivity(),
                String.format("Complete delete %d files", count), Toast.LENGTH_LONG).show();
    }


}
