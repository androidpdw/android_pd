package cpz.com.read.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.lidroid.xutils.BitmapUtils;

import java.util.ArrayList;
import java.util.List;

import cpz.com.read.R;
import cpz.com.read.activity.WebActivity;
import cpz.com.read.view.ImageCycleView;

public class HomeFragment extends Fragment
{

    private ImageCycleView mImageCycleView;
    private RelativeLayout rlFirst;
    private RelativeLayout rlSecond;
    private RelativeLayout rlThird;
    private RelativeLayout rlFour;
    private String[] mHomeItemImage = {"http://img1.qunarzz.com/travel/poi/201405/22/5bd8451623620821ddb12cfb.jpg_r_720x400x95_d06a7bf4.jpg"
            , "http://img.51766.com/1001/1204250257379.jpg"
            , "http://dimg02.c-ctrip.com/images/fd/tg/g2/M05/87/D9/Cghzf1WwsVWAFjE2ACqTKYbSfPQ625_C_880_350.jpg"
            ,"http://tw.mjjq.com/pic/20071105/20071105150959972.jpg"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mImageCycleView = (ImageCycleView) view.findViewById(R.id.icv_top);
        mImageCycleView.setIndicationStyle(ImageCycleView.IndicationStyle.IMAGE,
                R.mipmap.dot_blur, R.mipmap.dot_focus, 1f);
        rlFirst = (RelativeLayout) view.findViewById(R.id.ihiv_first);
        rlSecond = (RelativeLayout) view.findViewById(R.id.ihiv_second);
        rlThird = (RelativeLayout) view.findViewById(R.id.ihiv_third);
        rlFour = (RelativeLayout) view.findViewById(R.id.ihiv_four);
        initImageCycleView();
        innitHomItemImage();
        return view;
    }

    private void innitHomItemImage()
    {
        BitmapUtils bitmapUtils = new BitmapUtils(getContext());

        bitmapUtils.display(rlFirst.findViewById(R.id.iv_item_background), mHomeItemImage[0].toString());
        bitmapUtils.display(rlSecond.findViewById(R.id.iv_item_background), mHomeItemImage[1].toString());
        bitmapUtils.display(rlThird.findViewById(R.id.iv_item_background), mHomeItemImage[2].toString());
        bitmapUtils.display(rlFour.findViewById(R.id.iv_item_background), mHomeItemImage[3].toString());
    }

    private void initImageCycleView()
    {
        List<ImageCycleView.ImageInfo> list = new ArrayList<ImageCycleView.ImageInfo>();
        //res图片资源
//        list.add(new ImageCycleView.ImageInfo(R.mipmap.a,"","http://www.baidu.com/"));
//        list.add(new ImageCycleView.ImageInfo(R.mipmap.b,"","2"));
//        list.add(new ImageCycleView.ImageInfo(R.mipmap.c,"","3"));
//        list.add(new ImageCycleView.ImageInfo(R.mipmap.d,"","4"));
//        list.add(new ImageCycleView.ImageInfo(R.mipmap.e,"","5"));

        //使用网络加载图片
        list.add(new ImageCycleView.ImageInfo("https://www.baidu.com/img/bd_logo1.png", "",
                "https://www.baidu.com/"));
        list.add(new ImageCycleView.ImageInfo("http://pic2.nipic.com/20090423/1378382_153331048_2.jpg", "",
                "http://www.163.com/"));
        list.add(new ImageCycleView.ImageInfo("http://www.gdxlz.com/uploads/allimg/160113/1-160113120049319.jpg", "",
                "http://www.sina.com.cn/"));
        mImageCycleView.setOnPageClickListener(new ImageCycleView.OnPageClickListener()
        {
            @Override
            public void onClick(View imageView, ImageCycleView.ImageInfo imageInfo)
            {
                Toast.makeText(getContext(), "你点击了" + imageInfo.value.toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("url", imageInfo.value.toString());
                startActivity(intent);
            }
        });

        mImageCycleView.loadData(list, new ImageCycleView.LoadImageCallBack()
        {
            @Override
            public ImageView loadAndDisplay(ImageCycleView.ImageInfo imageInfo)
            {

//                //本地图片
//                ImageView imageView=new ImageView(getContext());
//                imageView.setImageResource(Integer.parseInt(imageInfo.image.toString()));
//                return imageView;


//				//使用SD卡图片
//				SmartImageView smartImageView=new SmartImageView(MainActivity.this);
//				smartImageView.setImageURI(Uri.fromFile((File)imageInfo.image));
//				return smartImageView;

//				//使用SmartImageView，既可以使用网络图片也可以使用本地资源
//				SmartImageView smartImageView=new SmartImageView(getContext());
//				smartImageView.setImageResource(Integer.parseInt(imageInfo.image.toString()));
//				return smartImageView;

                //使用BitmapUtils,只能使用网络图片
                BitmapUtils bitmapUtils = new BitmapUtils(getContext());
                ImageView imageView = new ImageView(getContext());
                bitmapUtils.display(imageView, imageInfo.image.toString());
                return imageView;


            }
        });
    }


}
