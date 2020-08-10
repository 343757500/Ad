package com.lvchuan.ad;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.bigkoo.convenientbanner.holder.Holder;
import com.bumptech.glide.Glide;
import com.lvchuan.ad.bean.AdEntity;
import com.lvchuan.ad.view.MyStandardGSYVideoPlayer;
import com.pili.pldroid.player.PLOnCompletionListener;
import com.pili.pldroid.player.PLOnErrorListener;
import com.shuyu.gsyvideoplayer.listener.VideoAllCallBack;
import java.io.File;


public class LocalVImageHolderView extends Holder<AdEntity> implements VideoAllCallBack {

    private ImageView iv_ad_img;
    private FrameLayout fl_video;
    private ImageView coverView;
    private LinearLayout loadingView;
    private Callback callback;
    private int curId;
    String imageHref;
    private MyStandardGSYVideoPlayer svplayer;

    @Override
    public void onStartPrepared(String url, Object... objects) {

    }

    @Override
    public void onPrepared(String url, Object... objects) {

    }

    @Override
    public void onClickStartIcon(String url, Object... objects) {

    }

    @Override
    public void onClickStartError(String url, Object... objects) {

    }

    @Override
    public void onClickStop(String url, Object... objects) {

    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {

    }

    @Override
    public void onClickResume(String url, Object... objects) {

    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {

    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {

    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {

    }

    @Override
    public void onAutoComplete(String url, Object... objects) {
        Log.e("payer", "failed to seek !");
        if(callback!=null)callback.onCanTurn(curId, 500);
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {

    }

    @Override
    public void onQuitFullscreen(String url, Object... objects) {

    }

    @Override
    public void onQuitSmallWidget(String url, Object... objects) {

    }

    @Override
    public void onEnterSmallWidget(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {

    }

    @Override
    public void onTouchScreenSeekLight(String url, Object... objects) {

    }

    @Override
    public void onPlayError(String url, Object... objects) {

    }

    @Override
    public void onClickStartThumb(String url, Object... objects) {

    }

    @Override
    public void onClickBlank(String url, Object... objects) {

    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {

    }

    public interface Callback {
        void onCanTurn(int position, long time);
    }

    public void setCallback(Callback mCallback) {
        this.callback = mCallback;
    }

    public LocalVImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        iv_ad_img = itemView.findViewById(R.id.iv_ad_img);
        svplayer = itemView.findViewById(R.id.svplayer);
        fl_video = itemView.findViewById(R.id.fl_video);
        coverView = itemView.findViewById(R.id.coverView);
        loadingView = itemView.findViewById(R.id.loadingView);

    }

    @Override
    public void updateUI(AdEntity data) {
        if(data==null){
            return;
        }
        imageHref = data.getImgHref();
        if(imageHref!=null&&!imageHref.isEmpty()){
            curId = data.getId();
            Log.i("ad_url", imageHref + "  |" +curId);
            //pl_ad_video.stopPlayback();
            if(imageHref.endsWith("gif")){
                fl_video.setVisibility(View.GONE);
                iv_ad_img.setVisibility(View.VISIBLE);
                if(data.getStatus()==0){

                    Glide.with(ClientApplication.getInstance()
                            .getApplicationContext())
                            .asGif()
                            .load(data.getImgHref())
                            .into(iv_ad_img);
                }else{
                    Glide.with(ClientApplication.getInstance()
                            .getApplicationContext())
                            .asGif()
                            .load(data.getLocalCache())
                            .into(iv_ad_img);
                }
                if(callback!=null)callback.onCanTurn(curId, 5000);
                return;
            }
            if(imageHref.endsWith("mp4")){
                /*if(data.getStatus()==0){
                    if(callback!=null)callback.onCanTurn(curId, 1000);
                    return;
                }*/
                fl_video.setVisibility(View.VISIBLE);
               // pl_ad_video.setCoverView(coverView);
                iv_ad_img.setVisibility(View.GONE);
               // initAvOptions();
                Glide.with(ClientApplication.getInstance()
                        .getApplicationContext())
                        .load("http://mmbiz.qpic.cn/mmbiz/PwIlO51l7wuFyoFwAXfqPNETWCibjNACIt6ydN7vw8LeIwT7IjyG3eeribmK4rhibecvNKiaT2qeJRIWXLuKYPiaqtQ/0")
                        .into(coverView);
                ((ViewGroup)coverView.getParent()).removeView(coverView);

                svplayer.setUp(imageHref,true,"视频标题");
                svplayer.setVideoAllCallBack(this);
                svplayer.setThumbImageView(coverView);
                svplayer.startPlayLogic();
                //if(callback!=null)callback.onCanTurn(curId, 5000);
                return;
            }
            fl_video.setVisibility(View.GONE);
            iv_ad_img.setVisibility(View.VISIBLE);
            if(data.getStatus()==0){
                Glide.with(ClientApplication.getInstance()
                        .getApplicationContext())
                        .load(data.getImgHref())
                        .into(iv_ad_img);
            }else{
                Glide.with(ClientApplication.getInstance()
                        .getApplicationContext())
                        .load(data.getLocalCache())
                        .into(iv_ad_img);
            }
            if(callback!=null)callback.onCanTurn(curId, 5000);
        }
    }

    /**
     * 初始化播放配置
     */
   /* private void initAvOptions() {
        AVOptions options = new AVOptions();
        //超时时间
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        //解码方式
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);
        //设置播放方式 1/直播 0/点播
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
        //设置缓存文件夹
        options.setString(AVOptions.KEY_CACHE_DIR, Constants.VIDEO_CACHE_DIR);
        //播放位置
        options.setInteger(AVOptions.KEY_START_POSITION, 0);
        pl_ad_video.setAVOptions(options);
        pl_ad_video.setOnCompletionListener(mOnCompletionListener);
        pl_ad_video.setOnErrorListener(mOnErrorListener);
        pl_ad_video.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_ORIGIN);
//        videoView.setDisplayAspectRatio(PLVideoView.ASPECT_RATIO_PAVED_PARENT);
        pl_ad_video.setBufferingIndicator(loadingView);
    }*/

    private PLOnCompletionListener mOnCompletionListener = new PLOnCompletionListener() {
        @Override
        public void onCompletion() {
           // pl_ad_video.stopPlayback();
            if(callback!=null)callback.onCanTurn(curId, 1000);
        }
    };

    private PLOnErrorListener mOnErrorListener = new PLOnErrorListener() {
        @Override
        public boolean onError(int errorCode) {
            switch (errorCode) {
                case PLOnErrorListener.ERROR_CODE_IO_ERROR:
                    Log.e("payer", "IO Error!");
                    return false;
                case PLOnErrorListener.ERROR_CODE_OPEN_FAILED:
                    Log.e("payer", "failed to open player !");
                    String fileName = imageHref.replace("https://", "")
                            .replace("/", "-");
                    String videoCachePath = Constants.VIDEO_CACHE_DIR + File.separator + fileName + ".mp4";
                    File file = new File(videoCachePath);
                    if (file.exists()) {
                        file.delete();
                    }
                    if(callback!=null)callback.onCanTurn(curId, 1000);
                    break;
                case PLOnErrorListener.ERROR_CODE_SEEK_FAILED:
                    Log.e("payer", "failed to seek !");
                    if(callback!=null)callback.onCanTurn(curId, 1000);
                    return true;
                case PLOnErrorListener.ERROR_CODE_CACHE_FAILED:
                    Log.e("payer", "failed to cache url !");
                    if(callback!=null)callback.onCanTurn(curId, 1000);
                    break;
                default:
                    Log.e("payer", "unknown error !");
                    if(callback!=null)callback.onCanTurn(curId, 1000);
                    break;
            }
            return true;
        }
    };
}
