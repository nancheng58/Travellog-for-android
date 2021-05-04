package com.code.travellog.core.view.plog;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.code.travellog.AI.AiBoostManager;
import com.code.travellog.R;
import com.code.travellog.config.Constants;
import com.code.travellog.core.data.pojo.BasePojo;
import com.code.travellog.core.data.pojo.plog.PlogPostPojo;
import com.code.travellog.core.data.pojo.plog.PlogStatusPojo;
import com.code.travellog.core.data.pojo.plog.PlogWorkPojo;
import com.code.travellog.core.data.repository.PlogRepository;
import com.code.travellog.core.viewmodel.PlogViewModel;
import com.code.travellog.util.FileUitl;
import com.code.travellog.util.JsonUtils;
import com.code.travellog.util.ToastUtils;
import com.google.android.material.snackbar.Snackbar;
import com.gyf.immersionbar.ImmersionBar;
import com.luck.picture.lib.entity.LocalMedia;
import com.mvvm.base.AbsLifecycleFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import moe.feng.common.stepperview.IStepperAdapter;
import moe.feng.common.stepperview.VerticalStepperItemView;
import moe.feng.common.stepperview.VerticalStepperView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class PlogResultFragment extends AbsLifecycleFragment<PlogViewModel> implements IStepperAdapter {
	public static PlogResultFragment newInstance(){
		return new PlogResultFragment();
	}
	private VerticalStepperView mVerticalStepperView;
	private Timer mTimer;
	private int workid;
	private PlogStatusPojo plogStatusPojo;
	private List<LocalMedia> localMediaList;
	private final static String TAG = PlogResultFragment.class.getSimpleName();
	private List<AiBoostManager.Data> mDetectorResult;
	private AiBoostManager aiBoostManager ;
	private int Turn = 0;

	@Override
	public int getLayoutResId() {
		return R.layout.fragment_vertical_stepper_adapter;
	}

	@Override
	protected int getContentResId() {
		return R.id.vertical_stepper_view;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view,savedInstanceState);
		mVerticalStepperView = view.findViewById(R.id.vertical_stepper_view);
		Toolbar toolbar = getActivity().findViewById(R.id.toolbar);
		ImmersionBar.with(this).statusBarDarkFont(true).init();
		toolbar.setTitle("");

//		getActivity().setSupportActionBar(toolbar);
		mVerticalStepperView.setStepperAdapter(this);
		info[0] = "图片收集";
		info[1] = "图像监督检测";
		info[2] = "图片物体识别";
		info[3] = "图片超分辨率重建";
		info[4] = "等待服务器分配工作序列ID";
		info[5] = "上传图片";
		info[6] = "请求制作";
		info[7] = "排队等待中";
		info[8] = "正在分析命令参数";
		info[9] = "正在分析图片";
		info[10] = "正在生成诗词";
		info[11] = "正在匹配模板";
		info[12] = "正在生成Plog";
		info[13] = "处理完成";

	}

	@Override
	public void initView(Bundle state) {
		getData();
		super.initView(state);
		loadManager.showSuccess();

		getImageObjectDetector(localMediaList);
	}
	public void getData()
	{
		localMediaList = ((MakePlogActivity)activity).getLocalMediaList();
		aiBoostManager = ((MakePlogActivity) Objects.requireNonNull(getActivity())).getAiBoostManager();
	}
	@Override
	@NonNull
	public CharSequence getTitle(int index) {
		return "Step " + (index+1);
	}

	private final static String[] info = new String[15];
	@Override
	@Nullable
	public CharSequence getSummary(int index) {

		/**
			100 排队等待中
			101 正在分析命令参数
			102 正在分析图片
			103 正在生成诗词
			104 正在匹配模板
			105 正在生成影集
			200 处理完成
		 **/
		return Html.fromHtml(info[index]
				+ (mVerticalStepperView.getCurrentStep() > index ? " <b>完成</b>" : ""));
	}

	@Override
	public int size() {
		return 14;
	}

	@Override
	protected void dataObserver() {

		//物体识别完成
		registerSubscriber(AiBoostManager.EVENT_KEY_OBJECTLIST,null,List.class).observe(this,list -> {
			mDetectorResult = list;
			mViewModel.getPlogId();
			changeCurrentStep(2);
			Log.w(TAG,mDetectorResult.toString());
		});
		//获取work_id & 上传工作资料
		registerSubscriber(PlogRepository.EVENT_KEY_PLOGID, PlogWorkPojo.class).observe(this, plogWorkPojo -> {
			if(plogWorkPojo.code!=200) ToastUtils.showToast(plogWorkPojo.msg);
			else {
				workid = plogWorkPojo.data.work_id;
				Log.w("workid",workid + "") ;
				changeCurrentStep(5);
				upLoadData();
			}
		});
		//资料上传完成 & 开始制作
		for(int index = 0 ;index<localMediaList.size(); index++ )
		registerSubscriber(PlogRepository.EVENT_KEY_PLOGPIC,index+"", BasePojo.class).observe(this,basePojo -> {
			if(basePojo.code != 200 ) ToastUtils.showToast(basePojo.msg);
			else{
				Log.w(TAG,Turn+"");
				if(++Turn == localMediaList.size() ){
					mViewModel.PlogStart(workid);
					changeCurrentStep(7);
					Turn = 0;

				}
			}
		});
		//开始制作信号获取成功
		registerSubscriber(PlogRepository.EVENT_KEY_PLOGSTART, BasePojo.class).observe(this, basePojo -> {
			if (basePojo.code != 200) ToastUtils.showToast(basePojo.msg);
			else {
				getResult();
				changeCurrentStep(6);
			}
		});
		// 监听状态
		registerSubscriber(PlogRepository.EVENT_KEY_PLOGRESULT, PlogStatusPojo.class).observe(this, plogStatusPojo -> {
			if (plogStatusPojo == null) ;
			else if (plogStatusPojo.code != 200) {
				ToastUtils.showToast(plogStatusPojo.msg);
			} else if (plogStatusPojo.data.status == -1) {
				ToastUtils.showToast(plogStatusPojo.data.result_msg);
			} else {
				this.plogStatusPojo = plogStatusPojo;
				Log.w("PlogResultInfo", plogStatusPojo.toString());
				switch (plogStatusPojo.data.status){
					case 100: changeCurrentStep(7);break;
					case 101: changeCurrentStep(8);break;
					case 102: changeCurrentStep(9);break;
					case 103: changeCurrentStep(10);break;
					case 104: changeCurrentStep(11);break;
					case 105: changeCurrentStep(12);break;
					case 200: changeCurrentStep(13);mTimer.cancel();break;

				}

			}

		});
	}
	// 每 2 秒进行一次请求
	public void getResult()
	{
		mTimer = new Timer();
		mTimer.schedule(new TimerTask() {
			@Override
			public void run() {
				Log.w("timer",1+"");
				mViewModel.getPlogStatus(workid);
			}
		}, 1000, 2000);
	}
	public void getImageObjectDetector(List<LocalMedia> result)
	{
		new Thread(()->{
			aiBoostManager.setTotal(result.size());
			Log.w(TAG,result.size()+"");
			aiBoostManager.setResultEmpty();
			for (LocalMedia media : result){
				try {
					if (null != media.getPath()) {
						Bitmap bitmap = BitmapFactory.decodeFile(media.getRealPath());
						Log.w(TAG, bitmap.toString());
						aiBoostManager.run(bitmap);
					}
				}catch (Exception e){e.printStackTrace();}
			}
		}).start();
	}
	public void upLoadData()
		{
			new Thread(()->{
				PlogPostPojo plogPostPojo = new PlogPostPojo();
				int index = 0 ;
				plogPostPojo.images = new ArrayList<String>(localMediaList.size());
				plogPostPojo.factors = new ArrayList<PlogPostPojo.Data>(localMediaList.size());
				plogPostPojo.description =((MakePlogActivity) Objects.requireNonNull(getActivity())).getPlogDescription();
				plogPostPojo.title = ((MakePlogActivity)getActivity()).getPlogTitle();
				plogPostPojo.share = ((MakePlogActivity)getActivity()).getShare() ;
				for(LocalMedia localMedia : localMediaList){
					plogPostPojo.images.add(index, index+ FileUitl.getImgType(localMedia.getAndroidQToPath()));

					PlogPostPojo.Data data = new PlogPostPojo.Data();
					data.types = new ArrayList<>();
					data.values = new ArrayList<>();
					plogPostPojo.factors.add(index,data);
					index ++ ;
				}
				index = 0;int  loop = 0 ;
				for (AiBoostManager.Data entry : mDetectorResult){
					plogPostPojo.factors.get(index).types.add(entry.type);
					plogPostPojo.factors.get(index).values.add(entry.value);
					loop ++;
					if (loop == 3) {
						index++;
						loop=0;
					}
				}
				Log.w(TAG, JsonUtils.toJson(plogPostPojo));
				saveJSONDataToFile("info.json",JsonUtils.toJson(plogPostPojo));
				File file =new File(activity.getFilesDir(),"info.json");
				RequestBody requestBody = RequestBody.create(MediaType.parse("*/*"), file);
				MultipartBody multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
						.addFormDataPart("file", file.getName(), requestBody).build();
				mViewModel.postPic(workid,localMediaList.size()+"",multipartBody);
				index = 0;
				Log.w(TAG,"图片数量 "+ localMediaList.size());
				for(LocalMedia localMedia : localMediaList){

					file = new File(localMedia.getAndroidQToPath());
					requestBody = RequestBody.create(MediaType.parse("image/*"), file);
					multipartBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
							.addFormDataPart("file",index+FileUitl.getImgType(localMedia.getAndroidQToPath()), requestBody).build();
					mViewModel.postPic(workid,index+"",multipartBody);
					index ++ ;
				}
			}).start();
		}
	@Override
	protected void onStateRefresh() {

	}


	public void changeCurrentStep(int index){
		if(mVerticalStepperView.getCurrentStep()<index&&mVerticalStepperView.canNext()){
			mVerticalStepperView.setCurrentStep(index);
		}
		if(!mVerticalStepperView.canNext()){
			Snackbar.make(mVerticalStepperView, "Plog长图生成完成!", Snackbar.LENGTH_LONG)
					.setAction("查看",v -> {
						Intent intent = new Intent(activity, PlogDetailsActivity.class);
						intent.putExtra(Constants.PLOG_ID, String.valueOf(plogStatusPojo.data.work_id));
//						intent.putExtra(Constants.PLOG_POJO,plogResultPojo);
						activity.startActivity(intent);
						}).show();

		}

	}

	@Override
	public View onCreateCustomView(final int index, Context context, VerticalStepperItemView parent) {
		View inflateView = LayoutInflater.from(context).inflate(R.layout.item_vertical_stepper, parent, false);
		TextView contentView = inflateView.findViewById(R.id.item_content);
		contentView.setText(info[index]);
		return inflateView;
	}

	@Override
	public void onShow(int index) {

	}

	@Override
	public void onHide(int index) {

	}
	/**
	 * 保存JSON数据到文件
	 */
	private void saveJSONDataToFile(String fileName, String jsonData) {
		try {
			FileOutputStream fos = activity.openFileOutput(fileName,  Context.MODE_PRIVATE);
			fos.write(jsonData.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
