package com.xiaojia.daniujia.ui.frag;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.text.method.LinkMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xiaojia.daniujia.Const;
import com.xiaojia.daniujia.ExtraConst;
import com.xiaojia.daniujia.FileStorage;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.ShareDialog;
import com.xiaojia.daniujia.domain.resp.ExpertHomeVo;
import com.xiaojia.daniujia.domain.resp.QRCodeVo;
import com.xiaojia.daniujia.domain.resp.Speciality;
import com.xiaojia.daniujia.ui.act.ActGraphicOrderDetail;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActMain;
import com.xiaojia.daniujia.ui.act.ActMyExpert;
import com.xiaojia.daniujia.ui.act.ActNetPhoneOrderDetail;
import com.xiaojia.daniujia.ui.act.ActOfflineOrderDetail;
import com.xiaojia.daniujia.ui.act.ActUserComment;
import com.xiaojia.daniujia.ui.act.WebActivity;
import com.xiaojia.daniujia.ui.adapters.GoodAtAdapter;
import com.xiaojia.daniujia.ui.control.ExpertHomeControl;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.ui.views.MyListView;
import com.xiaojia.daniujia.ui.views.NotifyingScrollView;
import com.xiaojia.daniujia.ui.views.QRCodeDialog;
import com.xiaojia.daniujia.ui.views.SelectServiceTypeWindow;
import com.xiaojia.daniujia.ui.views.imageview.RoundedImageView;
import com.xiaojia.daniujia.utils.ApplicationUtil;
import com.xiaojia.daniujia.utils.BitmapUtil;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.ExpandableTextView;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.QiniuDownloadUtil;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.SelfTagHandler;
import com.xiaojia.daniujia.utils.ShareSDKUtil;
import com.xiaojia.daniujia.utils.SysUtil;
import com.xiaojia.daniujia.utils.TimeUtils;

import java.io.File;
import java.util.List;

import cn.sharesdk.sina.weibo.SinaWeibo;

/**
 * Created by ZLOVE on 2016/4/26.
 */
public class ExpertHomeFragment extends BaseFragment implements View.OnClickListener, SelfTagHandler.ClickLinkListener, SelectServiceTypeWindow.SelectServiceTypeListener {

    private ExpertHomeControl control;

    private View allView;
    private NotifyingScrollView scrollView;
    private RelativeLayout topContainer;
    private ImageView ivHeadDivider;
    private ImageView ivBack;
    private TextView tvTitle;
    private ImageView ivCollect;
    private ImageView ivShare;

    private ImageView ivBigBg;
    private RoundedImageView ivAvatar;
    private TextView tvName;
    private View expertCareerInfoContainer;
    private TextView tvCorpName;
    private TextView tvCorpPosition;
    private TextView tvCareerYear;

    private View bottomInfoContainer;
    private TextView tvName2;
    private TextView tvCorpName2;
    private TextView tvCorpPosition2;
    private TextView tvCareerYear2;

    private TextView tvExpertCity;
    private TextView tvCollectCount;

    private MyListView goodDirectionListView;
    private GoodAtAdapter goodAtAdapter;
    private ExpandableTextView tvExpertIntroduce;
    private View expertExperienceOutContainer;
    private View emptyExperienceView;
    private LinearLayout expertExperienceContainer;
    private View expertEducationOutContainer;
    private View emptyEducationView;
    private LinearLayout expertEducationContainer;
    private LinearLayout expertCommentOutContainer;
    private LinearLayout expertCommentContainer;
    private LinearLayout expertServiceContainer;

    private View bottomView;
    private Button btnConfirm;

    private View requireOperateView;
    private View requireChatView;
    private View requireCallView;

    private ImageView mIvQRCode;

    private int expertId = 0;
    private boolean isCollected = false;
    private int dis = 0;
    private ExpertHomeVo expertHomeVo = null;
    private ExpertHomeVo.ServiceInfo serviceInfo = null;
    private String expertIntro;
    private String profile;
    private boolean isFromPreview = false;

    private QRCodeVo mQRCode = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new ExpertHomeControl();
        setControl(control);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_expert_home;
    }

    @Override
    protected void setUpView(View view) {
        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.INTENT_KEY_EXPERT_ID)) {
                expertId = intent.getIntExtra(IntentKey.INTENT_KEY_EXPERT_ID, 0);
            }
            if (intent.hasExtra(IntentKey.INTENT_KEY_FROM_EXPERT_PREVIEW)) {
                isFromPreview = intent.getBooleanExtra(IntentKey.INTENT_KEY_FROM_EXPERT_PREVIEW, false);
            }
        }
        allView = view.findViewById(R.id.view);
        scrollView = (NotifyingScrollView) view.findViewById(R.id.id_scroll_view);
        topContainer = (RelativeLayout) view.findViewById(R.id.id_head_container);
        ivHeadDivider = (ImageView) view.findViewById(R.id.head_divider);
        ivBack = (ImageView) view.findViewById(R.id.id_back);
        tvTitle = (TextView) view.findViewById(R.id.id_title);
        ivCollect = (ImageView) view.findViewById(R.id.id_collect);
        ivShare = (ImageView) view.findViewById(R.id.id_share);
        goodDirectionListView = (MyListView) view.findViewById(R.id.id_direction_list);
        expertExperienceOutContainer = view.findViewById(R.id.expert_experience_out_container);
        emptyExperienceView = view.findViewById(R.id.empty_experience_container);
        expertExperienceContainer = (LinearLayout) view.findViewById(R.id.id_expert_industry_experience_container);
        expertEducationOutContainer = view.findViewById(R.id.expert_edu_out_container);
        emptyEducationView = view.findViewById(R.id.empty_education_container);
        expertEducationContainer = (LinearLayout) view.findViewById(R.id.expert_education_container);
        expertCommentOutContainer = (LinearLayout) view.findViewById(R.id.expert_comment_out_container);
        expertCommentContainer = (LinearLayout) view.findViewById(R.id.expert_comment_container);
        expertServiceContainer = (LinearLayout) view.findViewById(R.id.expert_service_container);
        ivBigBg = (ImageView) view.findViewById(R.id.iv_big_image);
        ivAvatar = (RoundedImageView) view.findViewById(R.id.id_avatar);
        tvName = (TextView) view.findViewById(R.id.id_name);
        expertCareerInfoContainer = view.findViewById(R.id.expert_career_info);
        tvCorpName = (TextView) view.findViewById(R.id.id_corp);
        tvCorpPosition = (TextView) view.findViewById(R.id.id_position);
        tvCareerYear = (TextView) view.findViewById(R.id.id_career_year);

        bottomInfoContainer = view.findViewById(R.id.bottom_info_container);
        tvName2 = (TextView) view.findViewById(R.id.id_name_2);
        tvCorpName2 = (TextView) view.findViewById(R.id.id_corp_2);
        tvCorpPosition2 = (TextView) view.findViewById(R.id.id_position_2);
        tvCareerYear2 = (TextView) view.findViewById(R.id.id_career_year_2);

        tvExpertCity = (TextView) view.findViewById(R.id.id_city);
        tvCollectCount = (TextView) view.findViewById(R.id.id_collect_count);
        tvExpertIntroduce = (ExpandableTextView) view.findViewById(R.id.person_introduce);
        bottomView = view.findViewById(R.id.ll_bottom);
        btnConfirm = (Button) view.findViewById(R.id.confirm);

        requireOperateView = view.findViewById(R.id.require_operate_container);
        requireChatView = view.findViewById(R.id.require_go_chat);
        requireCallView = view.findViewById(R.id.require_call_phone);

        mIvQRCode = (ImageView) view.findViewById(R.id.frag_expert_home_iv_qr_code);
        setBackButton(ivBack);
        tvTitle.setVisibility(View.GONE);
        setTopColorChange();

        if (isFromPreview) {
            bottomView.setVisibility(View.GONE);
            ivCollect.setVisibility(View.GONE);
            ivShare.setVisibility(View.GONE);
            btnConfirm.setVisibility(View.GONE);
            mIvQRCode.setVisibility(View.GONE);
        }

        allView.setVisibility(View.VISIBLE);
        ivCollect.setOnClickListener(this);
        ivShare.setOnClickListener(this);
        btnConfirm.setOnClickListener(this);
        mIvQRCode.setOnClickListener(this);

        requireChatView.setOnClickListener(this);
        requireCallView.setOnClickListener(this);

        control.requestExpertInfo(expertId);
    }

    @Override
    public void onClick(View v) {
        if (v == btnConfirm) {
            if (expertHomeVo == null || expertHomeVo.getBasic() == null || expertHomeVo.getServices() == null) {
                return;
            }
            if (expertHomeVo.getServices().getPhoto() == null || !expertHomeVo.getServices().getPhoto().isEnabled()) {
                return;
            }
            SelectServiceTypeWindow window = DialogManager.getSelectServiceWindow(getActivity(), serviceInfo, this);
            window.showAtLocation(scrollView, Gravity.BOTTOM, 0, 0);
        } else if (v == ivCollect) {
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                Intent intent = new Intent(getActivity(), ActLogin.class);
                startActivity(intent);
                return;
            } else {
                control.collectExpertRequest(expertId, isCollected);
            }
        } else if (v == ivShare) {
            ExpertHomeVo.BasicInfo basicInfo = expertHomeVo.getBasic();
            if (basicInfo == null) {
                return;
            }
            String company = TextUtils.isEmpty(basicInfo.getCompanyAbbr()) ? basicInfo.getCompany() : basicInfo.getCompanyAbbr();
            final String title = "[大牛家]" + basicInfo.getName() + "(" + basicInfo.getPosition() + "·" + company + ")";
            final String imgUrl = basicInfo.getImgurl();
            final String linkUrl = String.format(Const.URL_SHARE_CONSULT, expertId);
            if (shareDialog == null){
                shareDialog = new ShareDialog(getActivity());
                shareDialog.setOnShareClickListener(new ShareDialog.ShareClickListener() {
                    @Override
                    public void onShareClick(String platform) {
                        if (platform.equals(SinaWeibo.NAME)) {
                            handleSinaWebShare(title, imgUrl, linkUrl, platform);
                        } else {
                            ShareSDKUtil.share(title, expertIntro, imgUrl, linkUrl, platform);
                        }
                        shareDialog.dismiss();
                    }
                });
            }
            shareDialog.showDialog();

        } else if (v == mIvQRCode) {
            if (mQRCode == null) {
                control.reqeustQRCode(expertId);
            } else {
                showQRDialog(mQRCode);
            }
        } else if (v == requireChatView) {
            Intent intent = new Intent(getActivity(), ActMain.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra(IntentKey.INTENT_KEY_CHAT_TOPIC, expertHomeVo.getDemand().getUsername());
            startActivity(intent);
        } else if (v == requireCallView) {
            control.callPhone(expertHomeVo.getDemand().getDemandId(),  expertHomeVo.getDemand().getExpertId(), expertHomeVo.getDemand().getType());
            CommonDialog callTipDialog = new CommonDialog(getActivity(), null, "温馨提示", getResources().getString(R.string.call_phone_tip), "知道了", true);
            callTipDialog.showdialog();
        }
    }

    private ShareDialog shareDialog = null;

    private void setDirectionData(List<Speciality> data) {
        if (ListUtils.isEmpty(data)) {
            return;
        }
        if (goodAtAdapter == null) {
            goodAtAdapter = new GoodAtAdapter(data, getActivity());
        }
        goodAtAdapter.setIsFromExpertHome(true);
        goodDirectionListView.setAdapter(goodAtAdapter);
    }

    private void setExpertBasicInfo(ExpertHomeVo.BasicInfo basicInfo) {
        if (basicInfo == null) {
            return;
        }
        tvTitle.setText(basicInfo.getName());
        tvExpertCity.setText("城市：" + basicInfo.getCityName());
        if (TextUtils.isEmpty(basicInfo.getAdimgurl())) {
            ivAvatar.setVisibility(View.VISIBLE);
            ivBigBg.setImageResource(R.drawable.expert_home_top_bg);
            Picasso.with(getActivity()).load(basicInfo.getImgurl()).resize(ScreenUtils.dip2px(70), ScreenUtils.dip2px(70)).into(ivAvatar);
            tvName.setText(basicInfo.getName());
            tvCorpName.setText(basicInfo.getCompany());
            tvCorpPosition.setText(basicInfo.getPosition());
            tvCareerYear.setText(String.valueOf(basicInfo.getWorkage()) + "年工作经验");
            expertCareerInfoContainer.setVisibility(View.VISIBLE);
            bottomInfoContainer.setVisibility(View.GONE);
        } else {
            ivAvatar.setVisibility(View.GONE);
            tvName.setVisibility(View.GONE);
            expertCareerInfoContainer.setVisibility(View.GONE);
            Picasso.with(getActivity()).load(basicInfo.getAdimgurl()).placeholder(R.drawable.default_img_bg).error(R.drawable.expert_home_top_bg).into(ivBigBg);
            bottomInfoContainer.setVisibility(View.VISIBLE);
            tvName2.setText(basicInfo.getName());
            tvCorpName2.setText(basicInfo.getCompany());
            tvCorpPosition2.setText(basicInfo.getPosition());
            tvCareerYear2.setText(String.valueOf(basicInfo.getWorkage()) + "年工作经验");
        }
    }

    private int followerCount = 0;
    private void setStatisticInfo(ExpertHomeVo.StatisticInfo statisticInfo) {
        if (statisticInfo == null) {
            return;
        }
        followerCount = statisticInfo.getFollowerCnt();
        tvCollectCount.setText("收藏：" + String.valueOf(followerCount) + "次");
    }

    private void setExpertIntroduce(String profile) {
        if (TextUtils.isEmpty(profile)) {
            return;
        }
        expertIntro = profile;
        profile = profile.replace("\n", "<br/>");
        profile = profile.replace("[Link]", "<link>");
        profile = profile.replace("[/Link]", "</link>");
        profile = profile.replace("[Desc]", "<desc>");
        profile = profile.replace("[/Desc]", "</desc>");
        profile = profile.replace("[Url]", "<url>");
        profile = profile.replace("[/Url]", "</url>");
        this.profile = profile;
        tvExpertIntroduce.setText(Html.fromHtml(profile, null, new SelfTagHandler(this)));
        tvExpertIntroduce.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void addExpertExperienceData(List<ExpertHomeVo.CareerInfo> careerInfo) {
        if (!isAdded()) {
            return;
        }
        if (ListUtils.isEmpty(careerInfo)) {
            if (isFromPreview) {
                emptyExperienceView.setVisibility(View.VISIBLE);
            } else {
                expertExperienceOutContainer.setVisibility(View.GONE);
            }
            return;
        }
        for (ExpertHomeVo.CareerInfo info : careerInfo) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_expert_industry_experience, null);
            TextView tvDuration = (TextView) view.findViewById(R.id.id_career_duration);
            TextView tvCareerTime = (TextView) view.findViewById(R.id.career_time);
            TextView tvPosition = (TextView) view.findViewById(R.id.id_position);
            TextView tvCorp = (TextView) view.findViewById(R.id.id_corp);
            TextView tvDesc = (TextView) view.findViewById(R.id.id_desc);

            long endTime;
            if (info.getEndtime() == 0) {
                endTime = System.currentTimeMillis();
            } else {
                endTime = info.getEndtime() * 1000;
            }

            if (info.getEndtime() == 0) {
                tvDuration.setText(TimeUtils.getTime(info.getBegintime() * 1000, TimeUtils.DATE_FORMAT_CAREER) + "—现在");
            } else {
                tvDuration.setText(TimeUtils.getTime(info.getBegintime() * 1000, TimeUtils.DATE_FORMAT_CAREER) + "—" + TimeUtils.getTime(endTime, TimeUtils.DATE_FORMAT_CAREER));
            }

            tvCareerTime.setText("[" + TimeUtils.getCareerDuration(info.getBegintime(), endTime / 1000) + "]");
            tvPosition.setText(info.getPosition());
            tvCorp.setText(info.getCompany());
            String desc = info.getPositiondesc();
            if (TextUtils.isEmpty(desc)) {
                tvDesc.setVisibility(View.GONE);
            } else {
                tvDesc.setVisibility(View.VISIBLE);
                tvDesc.setText(desc);
            }
            expertExperienceContainer.addView(view);
        }
    }

    private void addExpertEducationData(List<ExpertHomeVo.EducationInfo> educationInfo) {
        if (!isAdded()) {
            return;
        }
        if (ListUtils.isEmpty(educationInfo)) {
            if (isFromPreview) {
                emptyEducationView.setVisibility(View.VISIBLE);
            } else {
                expertEducationOutContainer.setVisibility(View.GONE);
            }
            return;
        }
        for (ExpertHomeVo.EducationInfo info : educationInfo) {
            View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_expert_education, null);
            TextView tvTime = (TextView) view.findViewById(R.id.id_time);
            TextView tvSchool = (TextView) view.findViewById(R.id.id_school);
            TextView tvDegree = (TextView) view.findViewById(R.id.id_degree);

            String begin = TimeUtils.getTime(info.getBegintime() * 1000, TimeUtils.DATE_FORMAT_CAREER);
            String end = TimeUtils.getTime(info.getEndtime() * 1000, TimeUtils.DATE_FORMAT_CAREER);
            tvTime.setText(begin + "—" + end);
            tvSchool.setText(info.getSchool());
            tvDegree.setText(info.getDegree());
            expertEducationContainer.addView(view);
        }
    }

    private void addExpertCommentData(ExpertHomeVo.LastCommentInfo lastComment, int totalCommentNum) {
        if (!isAdded()) {
            return;
        }
        if (isFromPreview || lastComment == null) {
            expertCommentOutContainer.setVisibility(View.GONE);
            return;
        }
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_expert_comment, null);
        View noCommentContainer = view.findViewById(R.id.no_record);
        View commentContainer = view.findViewById(R.id.expert_comment_container);

        if (lastComment == null) {
            noCommentContainer.setVisibility(View.VISIBLE);
            commentContainer.setVisibility(View.GONE);
        } else {
            noCommentContainer.setVisibility(View.GONE);
            commentContainer.setVisibility(View.VISIBLE);

            RoundedImageView ivAvatar = (RoundedImageView) view.findViewById(R.id.id_avatar);
            TextView tvName = (TextView) view.findViewById(R.id.id_name);
            TextView tvTime = (TextView) view.findViewById(R.id.id_time);
            TextView tvContent = (TextView) view.findViewById(R.id.id_content);
            TextView tvAllComment = (TextView) view.findViewById(R.id.all_comment);
            TextView tvServiceWay = (TextView) view.findViewById(R.id.id_service_way);
            if (TextUtils.isEmpty(lastComment.getImgurl())) {
                Picasso.with(getActivity()).load(R.drawable.ic_avatar_default).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).into(ivAvatar);
            } else {
                Picasso.with(getActivity()).load(lastComment.getImgurl()).resize(ScreenUtils.dip2px(40), ScreenUtils.dip2px(40)).into(ivAvatar);
            }
            if (!TextUtils.isEmpty(lastComment.getName())) {
                tvName.setText(lastComment.getName());
            } else {
                tvName.setText(lastComment.getUsername());
            }
            tvTime.setText(TimeUtils.getTime(lastComment.getCommenttime() * 1000, TimeUtils.DATE_FORMAT_DATE));
            tvContent.setText(lastComment.getContent());
            if (lastComment.getServiceType() == 1) {
                tvServiceWay.setText("服务方式:图文约谈");
            } else if (lastComment.getServiceType() == 2) {
                tvServiceWay.setText("服务方式:电话约谈");
            } else if (lastComment.getServiceType() == 3) {
                tvServiceWay.setText("服务方式:线下约谈");
            }
            tvAllComment.setText("查看" + totalCommentNum + "条评价>");
        }
        view.findViewById(R.id.all_comment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActUserComment.class);
                intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, expertId);
                startActivity(intent);
            }
        });
        expertCommentContainer.addView(view);
    }

    private void addExpertServiceData(ExpertHomeVo.ServiceInfo serviceInfo) {
        if (!isAdded()) {
            return;
        }
        if (serviceInfo == null) {
            return;
        }
        this.serviceInfo = serviceInfo;

        boolean txtEnabled;
        boolean phoneEnabled;
        boolean offlineEnabled;

        View view = LayoutInflater.from(getActivity()).inflate(R.layout.view_item_expert_service_way, null);
        TextView tvTxtConsult = (TextView) view.findViewById(R.id.id_txt_consult);
        TextView tvPhoneConsult = (TextView) view.findViewById(R.id.id_phone_consult);
        TextView tvOfflineConsult = (TextView) view.findViewById(R.id.id_offline_consult);

        if (serviceInfo.getPhoto() != null && serviceInfo.getPhoto().isEnabled()) {
            txtEnabled = true;
            tvTxtConsult.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.first_text_color));
        } else {
            txtEnabled = false;
            tvTxtConsult.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
        }

        if (serviceInfo.getPhone() != null && serviceInfo.getPhone().isEnabled()) {
            phoneEnabled = true;
            tvPhoneConsult.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.first_text_color));
        } else {
            phoneEnabled = false;
            tvPhoneConsult.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
        }

        if (serviceInfo.getChat() != null && serviceInfo.getChat().isEnabled()) {
            offlineEnabled = true;
            tvOfflineConsult.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.first_text_color));
        } else {
            offlineEnabled = false;
            tvOfflineConsult.setTextColor(ApplicationUtil.getApplicationContext().getResources().getColor(R.color.third_text_color));
        }

        if (txtEnabled || phoneEnabled || offlineEnabled) {
            btnConfirm.setText("预约服务");
            btnConfirm.setClickable(true);
        } else {
            btnConfirm.setText("暂停服务");
            btnConfirm.setClickable(false);
        }

        tvTxtConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.getTxtConsultTip();
            }
        });

        tvPhoneConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.getPhoneConsultTip();
            }
        });

        tvOfflineConsult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                control.getOfflineConsultTip();
            }
        });

        expertServiceContainer.addView(view);
    }

    private void setTopColorChange() {
        topContainer.getBackground().setAlpha(0);
        scrollView.setOnScrollChangedListener(new NotifyingScrollView.OnScrollChangedListener() {

            @Override
            public void onScrollChanged(ScrollView who, int l, int t, int oldl, int oldt) {
                //define it for scroll height
                int lHeight = 500;//渐变头部颜色
                dis = t;
                if (t <= 50) {
                    topContainer.getBackground().setAlpha(0);
                } else if (t <= 500) {
                    int progress = (int) (new Float(t) / new Float(lHeight) * 255);//计算渐变
                    topContainer.getBackground().setAlpha(progress);
                } else {
                    topContainer.getBackground().setAlpha(255);//全部显示
                }
                if (t < 400) {
                    tvTitle.setVisibility(View.GONE);
                    ivBack.setImageResource(R.drawable.ic_back_white);
                    ivShare.setImageResource(R.drawable.ic_share_white);
                    ivHeadDivider.setVisibility(View.GONE);
                    if (isCollected) {
                        ivCollect.setImageResource(R.drawable.homepage_collection_solid);
                    } else {
                        ivCollect.setImageResource(R.drawable.homepage_collection);
                    }
                } else {
                    tvTitle.setVisibility(View.VISIBLE);
                    ivBack.setImageResource(R.drawable.ic_back_grey);
                    ivShare.setImageResource(R.drawable.ic_share_grey);
                    ivHeadDivider.setVisibility(View.VISIBLE);
                    if (isCollected) {
                        ivCollect.setImageResource(R.drawable.homepage_collection_grey_solid);
                    } else {
                        ivCollect.setImageResource(R.drawable.homepage_collection_grey);
                    }
                }
            }
        });
    }

    public void setExpertData(ExpertHomeVo data) {
        if (data == null) {
            return;
        }
        this.allView.setVisibility(View.GONE);
        this.bottomView.setVisibility(View.VISIBLE);
        this.expertHomeVo = data;
        isCollected = data.isFavorited();
        if (isCollected) {
            ivCollect.setImageResource(R.drawable.homepage_collection_solid);
        }
        setDirectionData(data.getSpecialities());
        setExpertBasicInfo(data.getBasic());
        setStatisticInfo(data.getStatistic());
        setExpertIntroduce(data.getProfile());
        addExpertExperienceData(data.getCareer());
        addExpertEducationData(data.getEducation());
        addExpertCommentData(data.getLastComment(), data.getTotalCommentNum());
        addExpertServiceData(data.getServices());
        if (data.getDemand() != null) {
            if (data.getDemand().isDemand()) {
                btnConfirm.setVisibility(View.GONE);
                requireOperateView.setVisibility(View.VISIBLE);
            }
        }
    }

    private QRCodeDialog dialog = null;

    public void setQRCode(QRCodeVo data) {
        if (data == null)
            return;
        mQRCode = data;
        showQRDialog(mQRCode);
    }

    private void showQRDialog(QRCodeVo data) {
        if (dialog == null) {
            String qrCode = data.getQrCode();
            Bitmap bitmap = BitmapUtil.base64ToBitmap(qrCode);
            dialog = new QRCodeDialog(getActivity());
            dialog.setCanceledOnTouchOutside(true);
            dialog.setName(expertHomeVo.getBasic().getName());
            dialog.setQRCodeBitmap(bitmap);
            String avatarUrl = TextUtils.isEmpty(expertHomeVo.getBasic().getAdimgurl()) ? expertHomeVo.getBasic().getImgurl() : expertHomeVo.getBasic().getAdimgurl();
            Picasso.with(getActivity()).load(avatarUrl).resize(96, 96).error(R.drawable.expert_home_top_bg).into(dialog.getmRivUserIcon());
        }
        dialog.showdialog();
    }

    public void setCollectResp() {
        if (isCollected) {
            showShortToast(R.string.have_deleted_favor);
            isCollected = false;
            followerCount --;
            tvCollectCount.setText("收藏:" + String.valueOf(followerCount) + "次");
            if (dis < 400) {
                ivCollect.setImageResource(R.drawable.homepage_collection);
            } else {
                ivCollect.setImageResource(R.drawable.homepage_collection_grey);
            }
        } else {
            showShortToast(R.string.have_favored);
            isCollected = true;
            followerCount ++;
            tvCollectCount.setText("收藏:" + String.valueOf(followerCount) + "次");
            if (dis < 400) {
                ivCollect.setImageResource(R.drawable.homepage_collection_solid);
            } else {
                ivCollect.setImageResource(R.drawable.homepage_collection_grey_solid);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_BUY_EXPERT_SERVICE) {
            if (data != null) {
                if (data.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                    int orderId = data.getIntExtra(IntentKey.INTENT_KEY_ORDER_ID, 0);
                    Intent intent = new Intent(getActivity(), ActMyExpert.class);
                    intent.putExtra(IntentKey.INTENT_KEY_ORDER_ID, orderId);
                    startActivity(intent);
                    finishActivity(intent);
                }
            }
        }
    }

    @Override
    public void clickLink(String url) {
        Intent intent = new Intent(getActivity(), WebActivity.class);
        intent.putExtra(ExtraConst.EXTRA_WEB_TITLE, "");
        intent.putExtra(ExtraConst.EXTRA_WEB_URL, url);
        startActivity(intent);
    }

    @Override
    public void selectServiceType(int serviceType) {
        if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
            Intent intent = new Intent(getActivity(), ActLogin.class);
            startActivity(intent);
            return;
        }
        if (expertId == SysUtil.getIntPref(PrefKeyConst.PREF_USER_ID)) {
            showShortToast("不能购买自己的服务");
            return;
        }
        if (serviceType == 1) {
            Intent intent = new Intent(getActivity(), ActGraphicOrderDetail.class);
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_AVATAR, expertHomeVo.getBasic().getImgurl());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_NAME, expertHomeVo.getBasic().getName());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_CORP, expertHomeVo.getBasic().getPosition() + " | " + expertHomeVo.getBasic().getCompany());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_PER_PRICE, expertHomeVo.getServices().getPhoto().getPrice());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_PRODUCT_ID, expertHomeVo.getServices().getPhoto().getProductId());
            startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
        } else if (serviceType == 2) {
            Intent intent = new Intent(getActivity(), ActNetPhoneOrderDetail.class);
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_AVATAR, expertHomeVo.getBasic().getImgurl());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_NAME, expertHomeVo.getBasic().getName());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_CORP, expertHomeVo.getBasic().getPosition() + " | " + expertHomeVo.getBasic().getCompany());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_PER_PRICE, expertHomeVo.getServices().getPhone().getPrice());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_PRODUCT_ID, expertHomeVo.getServices().getPhone().getProductId());
            startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
        } else if (serviceType == 3) {
            Intent intent = new Intent(getActivity(), ActOfflineOrderDetail.class);
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_AVATAR, expertHomeVo.getBasic().getImgurl());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_NAME, expertHomeVo.getBasic().getName());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_CORP, expertHomeVo.getBasic().getPosition() + " | " + expertHomeVo.getBasic().getCompany());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_PER_PRICE, expertHomeVo.getServices().getChat().getPrice());
            intent.putExtra(IntentKey.INTENT_KEY_EXPERT_PRODUCT_ID, expertHomeVo.getServices().getChat().getProductId());
            startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
        }
    }

    private void handleSinaWebShare(final String title, String imgUrl, final String linkUrl, final String platform) {
        String[] str = imgUrl.split("/");
        final File file = new File(FileStorage.APP_IMG_DIR, str[str.length - 1]);
        if (file.exists() && file.length() > 0) {
            ShareSDKUtil.share(title, expertIntro, file.getAbsolutePath(), linkUrl, platform);
            return;
        }
        imgUrl = imgUrl + "?imageView2/2/w/240";
        QiniuDownloadUtil.download(file, imgUrl, new QiniuDownloadUtil.DownloadCallback() {
            @Override
            public void downloadComplete(String path) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShareSDKUtil.share(title, expertIntro, file.getAbsolutePath(), linkUrl, platform);
                    }
                });
            }

            @Override
            public void onProgress(int max, int progress) {
            }

            @Override
            public void onFail(Exception e) {
            }
        });
    }
}
