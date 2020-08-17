package com.lvchuan.ad.view.fragment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.lvchuan.ad.bean.InitBean;
import com.lvchuan.ad.bean.NettyCmdBean;
import com.lvchuan.ad.utils.FileUtil;
import com.lvchuan.ad.utils.NetworkUtil;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdFragment extends BaseFragment implements View.OnClickListener , OnItemClickListener {
    private ConvenientBanner adBanner;
    List<AdEntity> bannerList = new ArrayList<>();
    private boolean isTurning;


    private int curId=0;
    public   Handler handlerAlive=new Handler();
    public   Runnable runnableAlive=new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            //要做的事情
            handlerAlive.postDelayed(this, 20000);

            if (curId < bannerList.size() - 1) {
                if (adBanner != null) adBanner.setCurrentItem(curId + 1, false);
            } else {
                if (adBanner != null) adBanner.notifyDataSetChanged();
            }
        }
    };
    private RelativeLayout rr_ad;


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_ad;
    }

    @Override
    public void initView() {
        EventBus.getDefault().register(this);
        adBanner = findView(R.id.adBanner);
        rr_ad = findView(R.id.rr_ad);
        boolean networkAvailable = NetworkUtil.isNetworkAvailable(getActivity());
        if (!networkAvailable){
            Toast.makeText(getActivity(),"当前网络异常",Toast.LENGTH_LONG).show();
            FileUtil fileUtil=new FileUtil(getActivity());
            List<String> filesAllName = getFilesAllName(fileUtil.SDPATH + "AdShow");
            bannerList.clear();
            for (int i = 0; i < filesAllName.size(); i++) {
                AdEntity adEntity = new AdEntity();
                adEntity.setId(i);
                adEntity.setStatus(1);
                adEntity.setImgHref(filesAllName.get(i));
                bannerList.add(adEntity);
            }
            rr_ad.setVisibility(View.GONE);
            adBanner.setVisibility(View.VISIBLE);
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
            }, bannerList).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setPointViewVisible(true).setCanLoop(true).setOnItemClickListener(this);
        }else {
            Toast.makeText(getActivity(),"当前网络正常",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void initListener() {

    }

    @Override
    public void initData() {
    }

    @Override
    public void onClick(View v, int id) {

    }


    private void handleAdTurn(int curId, long time) {
        Log.e("AdFragment",curId+"::"+time);
        handlerAlive.removeCallbacks(runnableAlive);
        if (adBanner != null) {
            if (isTurning) {
             /*   adBanner.postDelayed(() -> {
                    if (curId < bannerList.size() - 1) {
                        if (adBanner != null) adBanner.setCurrentItem(curId + 1, false);
                    } else {
                        if (adBanner != null) adBanner.notifyDataSetChanged();
                    }
                }, time);*/

            /*    handlerAlive.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (curId < bannerList.size() - 1) {
                            if (adBanner != null) adBanner.setCurrentItem(curId + 1, false);
                        } else {
                            if (adBanner != null) adBanner.notifyDataSetChanged();
                        }
                    }
                }, time);*/

                this.curId=curId;
                handlerAlive.postDelayed(runnableAlive,time);

            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isTurning = true;
    }

    @Override
    public void onPause() {
        super.onPause();
        isTurning=false;
    }

    @Override
    public void onItemClick(int position) {

    }


    private void initDownLoad() {
        start_multi();
    }


    //netty接收到广告更换的广播
    @Subscriber(tag = "nettyCmdBean")
    private void nettyCmdBean(NettyCmdBean nettyCmdBean) {

    }


    //接收activity传过来的数据展示广播
    @Subscriber(tag = "initBean")
    private void initBean(InitBean initBean) {

        rr_ad.setVisibility(View.GONE);
        adBanner.setVisibility(View.VISIBLE);


        bannerList.clear();
        handlerAlive.removeCallbacks(runnableAlive);
        if ("1".equals(initBean.getReturn_info().get(0).getMode())) {
            List<InitBean.ReturnInfoBean.AdvertisementBean> advertisement = initBean.getReturn_info().get(0).getAdvertisement();

            for (int i = 0; i < advertisement.size(); i++) {
                AdEntity adEntity = new AdEntity();
                adEntity.setId(i);
                adEntity.setStatus(0);
                adEntity.setImgHref(advertisement.get(i).getPath());
                bannerList.add(adEntity);
            }

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
            }, bannerList).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setPointViewVisible(true).setCanLoop(true).setOnItemClickListener(this);

            //下载资源到本地，没有网络的情况下显示
            initDownLoad();
        }
    }



    //接收activity传过来的数据展示广播
    @Subscriber(tag = "initNotBean")
    private void initNotBean(String message) {
        rr_ad.setVisibility(View.GONE);
        adBanner.setVisibility(View.VISIBLE);

        FileUtil fileUtil=new FileUtil(getActivity());
        List<String> filesAllName = getFilesAllName(fileUtil.SDPATH + "AdShow");
        bannerList.clear();
        handlerAlive.removeCallbacks(runnableAlive);
        if (filesAllName!=null) {
            for (int i = 0; i < filesAllName.size(); i++) {
                AdEntity adEntity = new AdEntity();
                adEntity.setId(i);
                adEntity.setStatus(1);
                adEntity.setImgHref(filesAllName.get(i));
                bannerList.add(adEntity);
            }
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
            }, bannerList).setPageIndicatorAlign(ConvenientBanner.PageIndicatorAlign.CENTER_HORIZONTAL)
                    .setPointViewVisible(true).setCanLoop(true).setOnItemClickListener(this);
        }
    }



    @Subscriber(tag = "viewChange")
    private void viewChange(String devIds) {
      initView();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
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
                deleteDir(fileUtil.SDCardPath()+"AdShow");
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
            Log.e("AdFragment",e.getMessage());
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



    //删除文件夹和文件夹里面的文件
    public static void deleteDir(final String pPath) {
        File dir = new File(pPath);
        deleteDirWihtFile(dir);
    }

    public static void deleteDirWihtFile(File dir) {
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;
        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete(); // 删除所有文件
            else if (file.isDirectory())
                deleteDirWihtFile(file); // 递规的方式删除文件夹
        }
       // dir.delete();// 删除目录本身
    }



    public static List<String> getFilesAllName(String path) {
        File file=new File(path);
        File[] files=file.listFiles();
        if (files == null){Log.e("error","空目录");return null;}
        List<String> s = new ArrayList<>();
        for(int i =0;i<files.length;i++){
            s.add(files[i].getAbsolutePath());
        }
        return s;
    }

}
