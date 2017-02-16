package com.xiaojia.daniujia.ui.frag;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xiaojia.daniujia.Config;
import com.xiaojia.daniujia.InsideMsg;
import com.xiaojia.daniujia.IntentKey;
import com.xiaojia.daniujia.PrefKeyConst;
import com.xiaojia.daniujia.R;
import com.xiaojia.daniujia.dlgs.BaseDialog;
import com.xiaojia.daniujia.dlgs.CommonTipDlg;
import com.xiaojia.daniujia.domain.resp.ConsultEntity;
import com.xiaojia.daniujia.domain.resp.ExpertInfoEntity;
import com.xiaojia.daniujia.domain.resp.ExpertInformationVo;
import com.xiaojia.daniujia.domain.resp.UserCenterRespVo;
import com.xiaojia.daniujia.msg.MsgHelper;
import com.xiaojia.daniujia.ui.act.ActExpertHome;
import com.xiaojia.daniujia.ui.act.ActExpertModifyInfo;
import com.xiaojia.daniujia.ui.act.ActExpertSearch;
import com.xiaojia.daniujia.ui.act.ActLogin;
import com.xiaojia.daniujia.ui.act.ActSpeechSearch;
import com.xiaojia.daniujia.ui.act.ActToBeExpert;
import com.xiaojia.daniujia.ui.act.ActUserBasic;
import com.xiaojia.daniujia.ui.adapters.HomeExpertAdapter;
import com.xiaojia.daniujia.ui.control.LookFroExpertByConditionControl;
import com.xiaojia.daniujia.ui.views.CommonDialog;
import com.xiaojia.daniujia.ui.views.ConditionSelectView;
import com.xiaojia.daniujia.ui.views.ConditionTabView;
import com.xiaojia.daniujia.ui.views.XListView;
import com.xiaojia.daniujia.utils.DialogManager;
import com.xiaojia.daniujia.utils.ListUtils;
import com.xiaojia.daniujia.utils.ScreenUtils;
import com.xiaojia.daniujia.utils.ShareSDKUtil;
import com.xiaojia.daniujia.utils.SysUtil;

import java.util.ArrayList;
import java.util.List;

import cn.sharesdk.wechat.friends.Wechat;

/**
 * Created by Administrator on 2016/12/15.
 */
public class LookFroExpertByConditionFragment extends BaseFragment implements XListView.IXListViewListener,
        ConditionTabView.ConditionItemClick, View.OnClickListener, ConditionSelectView.RightListViewItemClickListener,
        AdapterView.OnItemClickListener, DialogManager.NotifyListener {
    public static final int PAGE_SIZE = 10;
    public static final String SORT_DEFAULT = "default";
    public static final String SORT_VIEW = "viewDesc";
    private LookFroExpertByConditionControl control;
    private ConditionSelectView mSelectView;
    private ConditionTabView mTabView;
    private XListView mListView;
    private View mBg;
    private RelativeLayout mFooterView;
    private TextView mTvBecomeExpert;
    private TextView mTvFooterPhone;
    private TextView mTvInviteFriend;
    private ImageView mShareWeChat;
    private View mViewLine;
    private TextView mTvKeyWord;

    private HomeExpertAdapter mAdapter;
    private List<ConsultEntity> consultantsEntityList = new ArrayList<>();
    private int pageIndex = 1;
    private LocalSearchCondition mCondition;
    private ObjectAnimator mTranslateIn, mTranslateOut;

    private ArrayList<ConditionTabView.TabData> mTabStyles;
    // 哪个top_tab选中
    private int mCurrentPosition = 0;

    // 获取到条件数据后，直接转化成适配器所需数据
    private List<ExpertInfoEntity.Direction> mDirection = new ArrayList<>();
    private List<ExpertInfoEntity.Direction> mIndustry = new ArrayList<>();
    private List<ExpertInfoEntity.Direction> mCity = new ArrayList<>();
    private List<ExpertInfoEntity.Direction.SubDirection> mSort = new ArrayList<>();

    private ItemSelected[] mItemSelected;

    private int mSelectViewSortHeight;
    private int mSelectViewDefaultHeight;
    private boolean isClickDirection = false;

    private boolean isRefresh = true;
    private String mShareText = "";
    private boolean byVoice = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        control = new LookFroExpertByConditionControl();
        setControl(control);

        mCondition = new LocalSearchCondition();
        mItemSelected = new ItemSelected[4];

        mSelectViewDefaultHeight = ScreenUtils.getDisplayMetrics(getActivity()).heightPixels - 160 -
                ScreenUtils.getStatusBarHeight() - ScreenUtils.dip2px(85);
        mSelectViewSortHeight = ScreenUtils.dip2px(120);

        for (int i = 0; i < 4; i++) {
            mItemSelected[i] = new ItemSelected();
        }
        // 默认排序
        mItemSelected[3].setRightSelected(0);

        Intent intent = getActivity().getIntent();
        if (intent != null) {
            if (intent.hasExtra(IntentKey.CONDITION_KEYWORD_FROM_VOICE)) {
                byVoice = intent.getBooleanExtra(IntentKey.CONDITION_KEYWORD_FROM_VOICE, false);
            }
            // 行业
            if (intent.hasExtra(IntentKey.CONDITION_INDUSTRYPARENTID)) {
                mCondition.setIndustryParentId(intent.getStringExtra(IntentKey.CONDITION_INDUSTRYPARENTID));
            }

            // 专长
            if (intent.hasExtra(IntentKey.CONDITION_MAIN_DIRECTION_ID)) {
                isClickDirection = true;
                mCondition.setMainDirectionId(intent.getStringExtra(IntentKey.CONDITION_MAIN_DIRECTION_ID));
            }

            if (intent.hasExtra(IntentKey.CONDITION_KEYWORD)) {
                mCondition.setKeyword(intent.getStringExtra(IntentKey.CONDITION_KEYWORD));
                mShareText = mCondition.getKeyword();
            }

            if (intent.hasExtra(IntentKey.CONDITION_SHARE_TEXT)) {
                mShareText = intent.getStringExtra(IntentKey.CONDITION_SHARE_TEXT);
            }

            if (intent.hasExtra("_need_show_tip") && intent.getBooleanExtra("_need_show_tip", false)) {
                CommonDialog dialog = new CommonDialog(getActivity(), new CommonDialog.ConfirmAction() {
                    @Override
                    public void confirm() {
                        Intent intent = new Intent(getActivity(), ActUserBasic.class);
                        intent.putExtra(IntentKey.INTENT_KEY_IS_EXAMINE, true);
                        startActivity(intent);
                    }

                    @Override
                    public void cancel() {
                    }
                }, "提示", "已收到您提交的成为专家资料，我们会尽快进行审核！", "查看资料", "了解");
                dialog.showdialog();
            }
        }
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.frag_look_for_by_condition;
    }

    @Override
    protected void setUpView(View view) {
        setBackButton(view.findViewById(R.id.look_for_condition_iv_back));
        View searchView = view.findViewById(R.id.search_container);
        searchView.setOnClickListener(this);
        View speechSearchView = view.findViewById(R.id.speech_search);
        speechSearchView.setOnClickListener(this);

        mSelectView = (ConditionSelectView) view.findViewById(R.id.look_for_condition_select_view);
        mTabView = (ConditionTabView) view.findViewById(R.id.tab_condition);
        mListView = (XListView) view.findViewById(R.id.condition_list);
        mBg = view.findViewById(R.id.condition_view_bg);
//        mIvClear = (ImageView) view.findViewById(R.id.look_for_clear);
        mShareWeChat = (ImageView) view.findViewById(R.id.share_wechat);
        mViewLine = view.findViewById(R.id.common_line);
        mTvKeyWord = (TextView) view.findViewById(R.id.condition_tv_search);

        View footerView = LayoutInflater.from(getContext()).inflate(R.layout.condition_footerview, null);
        mFooterView = (RelativeLayout) footerView.findViewById(R.id.footer_view_rl);
        mFooterView.setVisibility(View.GONE);
        mTvBecomeExpert = (TextView) footerView.findViewById(R.id.condition_become_expert);
        mTvInviteFriend = (TextView) footerView.findViewById(R.id.condition_invite_friend);
        mTvFooterPhone = (TextView) mFooterView.findViewById(R.id.condition_footer_tv);
        mTvFooterPhone.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvInviteFriend.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        mTvBecomeExpert.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);

        mAdapter = new HomeExpertAdapter(consultantsEntityList, getActivity());
        mListView.addFooterView(footerView, null, false);
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setXListViewListener(this);

        // 属性动画，为了防止view执行动画后还能挡住底层的ListView
        mTranslateIn = ObjectAnimator.ofFloat(mSelectView, "translationY", -mSelectViewDefaultHeight, 0).setDuration(300);
        mTranslateOut = ObjectAnimator.ofFloat(mSelectView, "translationY", 0, -mSelectViewDefaultHeight).setDuration(300);

        mTabStyles = new ArrayList<>();
        mTabStyles.add(new ConditionTabView.TabData("行业", R.drawable.drop_down, R.drawable.drop_down_blue, R.drawable.put_away_blue));
        mTabStyles.add(new ConditionTabView.TabData("专长", R.drawable.drop_down, R.drawable.drop_down_blue, R.drawable.put_away_blue));
        mTabStyles.add(new ConditionTabView.TabData("城市", R.drawable.drop_down, R.drawable.drop_down_blue, R.drawable.put_away_blue));
        mTabStyles.add(new ConditionTabView.TabData("默认排序", R.drawable.drop_down, R.drawable.drop_down_blue, R.drawable.put_away_blue));

        mTabView.initTab(mTabStyles);
        mTabView.setListener(this);

        ExpertInfoEntity.Direction.SubDirection dirDefault = new ExpertInfoEntity.Direction.SubDirection();
        dirDefault.setName("默认排序");
        dirDefault.setSubDirectionId(1);
        ExpertInfoEntity.Direction.SubDirection dirView = new ExpertInfoEntity.Direction.SubDirection();
        dirView.setName("按浏览数");
        dirView.setSubDirectionId(2);
        mSort.add(dirDefault);
        mSort.add(dirView);

        initEvent();

        control.getExpertInfo(mCondition, byVoice);

    }

    private void initEvent() {
        if (!TextUtils.isEmpty(mCondition.getKeyword())) {
            mTvKeyWord.setText(mCondition.getKeyword());
            mTvKeyWord.setTextColor(Color.parseColor("#333333"));
        }

        mTranslateOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mSelectView.setVisibility(View.GONE);
                super.onAnimationEnd(animation);
            }
        });

        mSelectView.setRightItemClickListener(this);
        mBg.setOnClickListener(this);
        mTvFooterPhone.setOnClickListener(this);
        mShareWeChat.setOnClickListener(this);
        mTvBecomeExpert.setOnClickListener(this);
        mTvInviteFriend.setOnClickListener(this);
        mListView.setOnItemClickListener(this);

    }

    private String mShareUrl;

    public void onReceiveExpertInfo(ExpertInformationVo result) {
        mListView.stopRefresh();
        mListView.stopLoadMore();

        if (result == null) {
            mShareWeChat.setVisibility(View.GONE);
            mListView.setPullLoadEnable(false);
            return;
        }

        ExpertInfoEntity entity = result.getItem();
        if (entity == null) {
            mShareWeChat.setVisibility(View.GONE);
            mListView.setPullLoadEnable(false);
            return;
        }

        mShareUrl = entity.getShareUrl();
        if (isClickDirection) {
            List<ExpertInfoEntity.Direction> directions = entity.getDirections();
            if (directions != null && directions.size() > 0) {
                for (int i = 0; i < directions.size(); i++) {
                    String value = String.valueOf(directions.get(i).getMainDirectionId());
                    if (value != null) {
                        if (mCondition.getMainDirectionId().equals(value)) {
                            mItemSelected[1].setLeftSelected(i);
                            mItemSelected[1].setRightSelected(0);
                            mTabStyles.get(1).setType(ConditionTabView.SELECTED);
                            mTabView.updateTabStyle(mTabStyles);
                            isClickDirection = false;
                            break;
                        }
                    }

                }
            }
        }

        // 刷新条件数据，只有在一次全新的搜索之后才会返回true
        if (entity.isRefreshCondition()) {
            List<ExpertInfoEntity.Direction> direction = entity.getDirections();

            if (direction != null) {
                mDirection.clear();
                mDirection.addAll(direction);
            }

            List<ExpertInfoEntity.Industry> industry = entity.getIndustries();
            if (industry != null) {
                List<ExpertInfoEntity.Direction> toIndustry = new ArrayList<>();
                for (int i = 0; i < industry.size(); i++) {
                    ExpertInfoEntity.Industry in = industry.get(i);
                    if (in != null) {
                        ExpertInfoEntity.Direction dir = new ExpertInfoEntity.Direction();
                        dir.setName(in.getName());
                        dir.setMainDirectionId(in.getIndustryParentId());
                        List<ExpertInfoEntity.Industry.SubIndustry> subIn = in.getSubIndustries();

                        if (subIn != null) {
                            List<ExpertInfoEntity.Direction.SubDirection> toSubIndustry = new ArrayList<>();
                            for (int j = 0; j < subIn.size(); j++) {
                                ExpertInfoEntity.Direction.SubDirection subDirection = new ExpertInfoEntity.Direction.SubDirection();
                                subDirection.setName(subIn.get(j).getName());
                                subDirection.setSubDirectionId(subIn.get(j).getIndustryId());
                                toSubIndustry.add(subDirection);
                            }
                            dir.setSubDirections(toSubIndustry);
                        }

                        toIndustry.add(dir);
                        mIndustry.clear();
                        mIndustry.addAll(toIndustry);
                    }

                }
            }

            List<ExpertInfoEntity.City> city = entity.getCities();
            if (city != null) {
                List<ExpertInfoEntity.Direction> toCity = new ArrayList<>();
                for (int i = 0; i < city.size(); i++) {
                    ExpertInfoEntity.City in = city.get(i);
                    if (in != null) {
                        ExpertInfoEntity.Direction dir = new ExpertInfoEntity.Direction();
                        dir.setName(in.getProvinceName());
                        dir.setMainDirectionId(in.getProvinceId());
                        List<ExpertInfoEntity.City.Cities> subIn = in.getCities();

                        if (subIn != null) {
                            List<ExpertInfoEntity.Direction.SubDirection> toSubIndustry = new ArrayList<>();
                            for (int j = 0; j < subIn.size(); j++) {
                                ExpertInfoEntity.Direction.SubDirection subDirection = new ExpertInfoEntity.Direction.SubDirection();
                                subDirection.setName(subIn.get(j).getCityName());
                                subDirection.setSubDirectionId(subIn.get(j).getCityId());
                                toSubIndustry.add(subDirection);
                            }
                            dir.setSubDirections(toSubIndustry);
                        }

                        toCity.add(dir);
                        mCity.clear();
                        mCity.addAll(toCity);
                    }

                }
            }

        }

        if (entity.isShowConditionBar()) {
            mTabView.setVisibility(View.VISIBLE);
            mViewLine.setVisibility(View.VISIBLE);
        } else {
            mTabView.setVisibility(View.GONE);
            mViewLine.setVisibility(View.GONE);
        }

        List<ExpertInfoEntity.Expert> experts = entity.getItems();
        if (isRefresh)
            consultantsEntityList.clear();
        if (experts != null && experts.size() > 0) {
            for (int i = 0; i < entity.getItems().size(); i++) {
                ConsultEntity consultEntity = new ConsultEntity();
                consultEntity.setName(experts.get(i).getName());
                consultEntity.setCompany(experts.get(i).getCompany());
                consultEntity.setConcretes(experts.get(i).getConcretes());
                consultEntity.setConsultantId(experts.get(i).getConsultantId());
                consultEntity.setFollowerCnt(String.valueOf(experts.get(i).getFollowerCnt()));
                consultEntity.setImgurl(experts.get(i).getImgurl());
                consultEntity.setPosition(experts.get(i).getPosition());
                consultEntity.setVisitCount(String.valueOf(experts.get(i).getVisitCount()));
                consultEntity.setWorkage((int) experts.get(i).getWorkage());

                consultantsEntityList.add(consultEntity);
            }
            if (experts.size() < 10) {
                mListView.setPullLoadEnable(false);
                mFooterView.setVisibility(View.VISIBLE);
            } else {
                if (mFooterView.getVisibility() == View.VISIBLE) {
                    mFooterView.setVisibility(View.GONE);
                }
                mListView.setPullLoadEnable(true);
            }
        } else {
            mShareWeChat.setVisibility(View.GONE);
            mListView.setPullLoadEnable(false);
            if (mFooterView.getVisibility() == View.GONE) {
                mFooterView.setVisibility(View.VISIBLE);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    public void stopRefresh() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSelectView.clearAnimation();
        mTranslateOut = null;
        mTranslateIn = null;
    }

    @Override
    public void onRefresh() {
        pageIndex = 1;
        isRefresh = true;
        control.getExpertInfo(mCondition, false);
    }

    @Override
    public void onLoadMore() {
        pageIndex++;
        isRefresh = false;
        control.getExpertInfo(mCondition, false);
    }

    public int getPageIndex() {
        return pageIndex;
    }

    @Override
    public void OnConditionClickListener(int position) {
        onTabItemClick(position);

        mSelectView.setmLeftCurrentSelect(mItemSelected[position].getLeftSelected());
        mSelectView.setmRightCurrentSelect(mItemSelected[position].getRightSelected());

        if (position == 0) {
            mSelectView.setLeftData(mIndustry);
        } else if (position == 1) {
            mSelectView.setLeftData(mDirection);
        } else if (position == 2) {
            mSelectView.setLeftData(mCity);
        } else {
            mSelectView.setRightData(mSort);
        }

    }

    private CommonTipDlg mTipDlg;

    @Override
    public void onClick(View v) {
        Intent intent;
        if (v == mBg) {
            if (mSelectView.getVisibility() == View.VISIBLE) {
                onTabItemClick(mCurrentPosition);
            }
        } else if (v == mTvFooterPhone) {
            if (mTipDlg == null) {
                mTipDlg = new CommonTipDlg(getActivity(), "", "400-0903-022", "取消", "拨打");
                mTipDlg.setContentPosition(Gravity.CENTER_HORIZONTAL);
                mTipDlg.setTitleVisible(View.GONE);
                mTipDlg.setContentColor("#333333");
                mTipDlg.setOnClickListener(new BaseDialog.DialogClickListener() {
                    @Override
                    public void onCancel() {
                        mTipDlg.dismiss();
                        Intent intent = new Intent(Intent.ACTION_CALL);
                        Uri data = Uri.parse("tel:" + "4000903022");
                        intent.setData(data);
                        startActivity(intent);
                    }

                    @Override
                    public void onOK() {
                        super.onOK();
                        mTipDlg.dismiss();
                    }
                });
            }
            mTipDlg.showDialog();
        } else if (v == mShareWeChat) {
            ShareSDKUtil.share(mShareText + "方面的专家",
                    "「大牛家」有非常丰富的专家库，我为你找到这些专家",
                    "https://dn-daniujia.qbox.me/dnj_new_logo.png", mShareUrl, Wechat.NAME);

        } else if (v.getId() == R.id.search_container) {
            if (mSelectView.getVisibility() == View.VISIBLE) {
                mSelectView.setVisibility(View.GONE);
                mBg.setVisibility(View.GONE);
            }
            intent = new Intent(getActivity(), ActExpertSearch.class);
            if (!TextUtils.isEmpty(mCondition.getKeyword())) {
                intent.putExtra(IntentKey.REQ_CODE_SPEECH_SEARCH_KEYWORD, mTvKeyWord.getText().toString());
                startActivityForResult(intent, IntentKey.REQ_CODE_SPEECH_SEARCH);
                getActivity().overridePendingTransition(R.anim.fade_in_little, R.anim.fade_out_little);
                return;
            }
            startActivityForResult(intent, IntentKey.REQ_CODE_SPEECH_SEARCH);
        } else if (v.getId() == R.id.speech_search) {
            if (mSelectView.getVisibility() == View.VISIBLE) {
                mSelectView.setVisibility(View.GONE);
                mBg.setVisibility(View.GONE);
            }
            intent = new Intent(getActivity(), ActSpeechSearch.class);
            startActivityForResult(intent, IntentKey.REQ_CODE_SPEECH_SEARCH);
        } else if (v == mTvBecomeExpert) {
            if (!SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                intent = new Intent(getActivity(), ActLogin.class);
                startActivityForResult(intent, IntentKey.REQ_CODE_ASK_LOGIN);
            } else {
                if (SysUtil.getIntPref(PrefKeyConst.PREF_APPLY_STEP) >= 15) {
                    startActivity(new Intent(getActivity(), ActExpertModifyInfo.class));
                } else {
                    intent = new Intent(getActivity(), ActToBeExpert.class);
                    intent.putExtra(IntentKey.FROM_WHERE, IntentKey.INTENT_VALUE_TO_BE_EXPERT_FROM_SEARCH);
                    startActivity(intent);
                }

            }
        } else if (v == mTvInviteFriend) {
            String url;
            if (Config.DEBUG) {
                if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                    url = "https://refactorapi.daniujia.com/invite-tip/" + SysUtil.getIntPref(PrefKeyConst.PREF_HASH_ID);
                } else {
                    url = "https://refactorapi.daniujia.com/invite-tip/0";
                }
            } else {
                if (SysUtil.getBooleanPref(PrefKeyConst.PREF_IS_LOGIN)) {
                    url = "https://www.daniujia.com/invite-tip/" + SysUtil.getIntPref(PrefKeyConst.PREF_HASH_ID);
                } else {
                    url = "https://www.daniujia.com/invite-tip/0";
                }
            }
            ShareSDKUtil.share("邀请您做大牛家专家伙伴", "「大牛家」汇聚了大量各行业资深行业专家，我觉得你可以加入",
                    "https://dn-daniujia.qbox.me/dnj_new_logo.png", url, Wechat.NAME);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IntentKey.REQ_CODE_SPEECH_SEARCH) {
            if (data != null && data.hasExtra(IntentKey.INTENT_KEY_SPEECH_SEARCH_RESULT)) {
                byVoice = true;
                String searchContent = data.getStringExtra(IntentKey.INTENT_KEY_SPEECH_SEARCH_RESULT);
                // 需要清空所有搜索条件
                newSearchByKeyWord(searchContent);
                mTvKeyWord.setText(searchContent);
                mTvKeyWord.setTextColor(Color.parseColor("#333333"));
                mShareText = searchContent;
            }
        } else if (requestCode == IntentKey.REQ_CODE_BUY_EXPERT_SERVICE) {
            if (data != null && data.hasExtra(IntentKey.INTENT_KEY_ORDER_ID)) {
                MsgHelper.getInstance().sendMsg(InsideMsg.NOTIFY_JUMP_TO_ME);
                finishActivity();
            }
        } else if (requestCode == IntentKey.REQ_CODE_ASK_LOGIN) {
            if (data != null) {
                control.requestUserInfo();
            }
        }
    }

    private void onTabItemClick(final int position) {
        ViewGroup.LayoutParams lp = mSelectView.getLayoutParams();
        if (position == 3) {
            lp.height = mSelectViewSortHeight;
        } else {
            mSelectView.isGoneLeftList(false);
            mSelectView.setRightShowBlueLine(false);
            lp.height = mSelectViewDefaultHeight;
        }
        mSelectView.setLayoutParams(lp);

        if (mCurrentPosition == position) {
            if (mSelectView.getVisibility() == View.VISIBLE) {
                mBg.setVisibility(View.GONE);
                mShareWeChat.setVisibility(View.VISIBLE);
                if (mTranslateOut != null) {
                    mTranslateOut.start();
                }

                if (position == 0) {
                    if (mCondition.getIndustryId() == 0 && TextUtils.isEmpty(mCondition.getIndustryParentId())) {
                        // 上个item的子条目没有被选中
                        mTabStyles.get(position).setType(ConditionTabView.DEFAULT);
                    } else {
                        mTabStyles.get(position).setType(ConditionTabView.SELECTED);
                    }
                } else if (position == 1) {
                    if (mCondition.getSubDirectionId() == 0 && TextUtils.isEmpty(mCondition.getMainDirectionId())) {
                        mTabStyles.get(position).setType(ConditionTabView.DEFAULT);
                    } else {
                        mTabStyles.get(position).setType(ConditionTabView.SELECTED);
                    }
                } else {
                    mTabStyles.get(position).setType(ConditionTabView.DEFAULT);
                }

            } else {
                mShareWeChat.setVisibility(View.GONE);
                mBg.setVisibility(View.VISIBLE);
                mSelectView.setVisibility(View.VISIBLE);
                if (mTranslateIn != null) {
                    mTranslateIn.start();
                }

                // 处于选择中
                mTabStyles.get(position).setType(ConditionTabView.SELECTING);

            }
        } else {
            if (mSelectView.getVisibility() == View.VISIBLE) {
                mSelectView.setVisibility(View.GONE);
                mShareWeChat.setVisibility(View.GONE);

                mSelectView.setVisibility(View.VISIBLE);
                mBg.setVisibility(View.VISIBLE);
                if (mTranslateIn != null) {
                    mTranslateIn.start();
                }
                if (position == 3) {
                    mSelectView.isGoneLeftList(true);
                    mSelectView.setRightShowBlueLine(true);
                }

            } else {
                mShareWeChat.setVisibility(View.GONE);
                if (position == 3) {
                    mSelectView.isGoneLeftList(true);
                    mSelectView.setRightShowBlueLine(true);
                }
                mBg.setVisibility(View.VISIBLE);
                mSelectView.setVisibility(View.VISIBLE);
                if (mTranslateIn != null) {
                    mTranslateIn.start();
                }

            }

            // 处于选择中
            mTabStyles.get(position).setType(ConditionTabView.SELECTING);
            // 为什么没有第三和第四两个条目，因为需求要求不改变
            if (mCurrentPosition == 0) {
                if (mCondition.getIndustryId() == 0 && TextUtils.isEmpty(mCondition.getIndustryParentId())) {
                    // 上个item的子条目没有被选中
                    mTabStyles.get(mCurrentPosition).setType(ConditionTabView.DEFAULT);
                } else {
                    mTabStyles.get(mCurrentPosition).setType(ConditionTabView.SELECTED);
                }
            } else if (mCurrentPosition == 1) {
                if (mCondition.getSubDirectionId() == 0 && TextUtils.isEmpty(mCondition.getMainDirectionId())) {
                    mTabStyles.get(mCurrentPosition).setType(ConditionTabView.DEFAULT);
                } else {
                    mTabStyles.get(mCurrentPosition).setType(ConditionTabView.SELECTED);
                }
            } else {
                mTabStyles.get(mCurrentPosition).setType(ConditionTabView.DEFAULT);
            }

            mCurrentPosition = position;
        }

        mTabView.updateTabStyle(mTabStyles);
    }

    @Override
    public void onRightItemClickListener(int leftPosition, int rightPosition,
                                         int leftId, String leftName, int rightId, String rightName) {
        mItemSelected[mCurrentPosition].setLeftSelected(leftPosition);
        mItemSelected[mCurrentPosition].setRightSelected(rightPosition);

        switch (mCurrentPosition) {
            case 0:
                mCondition.setIndustryParentId(String.valueOf(leftId));
                mCondition.setIndustryId(rightId);
                if (rightId == 0) {
                    mShareText = leftName;
                } else {
                    mShareText = rightName;
                }
                break;
            case 1:
                mCondition.setMainDirectionId(String.valueOf(leftId));
                mCondition.setSubDirectionId(rightId);
                if (rightId == 0) {
                    mShareText = leftName;
                } else {
                    mShareText = rightName;
                }
                break;
            case 2:
                if (rightId != 0) {
                    mTabView.setTabText(mCurrentPosition, rightName);
                } else {
                    mTabView.setTabText(mCurrentPosition, leftName);
                }
                mCondition.setProvinceId(leftId);
                mCondition.setCityId(rightId);
                break;
            case 3:
                if (rightId == 1) {
                    mCondition.setSort(SORT_DEFAULT);
                } else {
                    mCondition.setSort(SORT_VIEW);
                }
                mTabView.setTabText(mCurrentPosition, rightName);
                break;
        }

        // 更新tab的状态
        onTabItemClick(mCurrentPosition);

        isRefresh = true;
        pageIndex = 1;
        mFooterView.setVisibility(View.GONE);
        control.getExpertInfo(mCondition, byVoice);

    }

    private void newSearchByKeyWord(String keyWord) {
        mCondition = new LocalSearchCondition();
        mCondition.setKeyword(keyWord);
        mDirection.clear();
        mIndustry.clear();
        mCity.clear();

        for (int i = 0; i < 3; i++) {
            mItemSelected[i] = new ItemSelected();
            mTabStyles.get(i).setType(ConditionTabView.DEFAULT);
        }
        mTabView.updateTabStyle(mTabStyles);

        consultantsEntityList.clear();
        mAdapter.notifyDataSetChanged();
        pageIndex = 1;
        mListView.hideFooterView();
        mFooterView.setVisibility(View.GONE);
        control.getExpertInfo(mCondition, byVoice);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (ListUtils.isEmpty(consultantsEntityList)) {
            return;
        }

        int finalPosition = position - mListView.getHeaderViewsCount();
        if (finalPosition >= consultantsEntityList.size())
            return;

        ConsultEntity entity = consultantsEntityList.get(finalPosition);
        if (entity == null) {
            return;
        }

        Intent intent = new Intent(getActivity(), ActExpertHome.class);
        intent.putExtra(IntentKey.INTENT_KEY_EXPERT_ID, entity.getConsultantId());
        startActivityForResult(intent, IntentKey.REQ_CODE_BUY_EXPERT_SERVICE);
    }

    public class ItemSelected {
        private int leftSelected;
        private int rightSelected = -1;

        public int getLeftSelected() {
            return leftSelected;
        }

        public void setLeftSelected(int leftSelected) {
            this.leftSelected = leftSelected;
        }

        public int getRightSelected() {
            return rightSelected;
        }

        public void setRightSelected(int rightSelected) {
            this.rightSelected = rightSelected;
        }
    }

    public class LocalSearchCondition {
        private String keyword;
        private int cityId;
        private int provinceId;
        private String industryParentId = "";
        private int industryId;
        private String mainDirectionId = "";
        private int subDirectionId;
        private String sort = "default";

        @Override
        public String toString() {
            return "LocalSearchCondition{" +
                    "keyword='" + keyword + '\'' +
                    ", cityId=" + cityId +
                    ", provinceId=" + provinceId +
                    ", industryParentId='" + industryParentId + '\'' +
                    ", industryId=" + industryId +
                    ", mainDirectionId='" + mainDirectionId + '\'' +
                    ", subDirectionId=" + subDirectionId +
                    ", sort='" + sort + '\'' +
                    '}';
        }

        public void setKeyword(String keyword) {
            this.keyword = keyword;
        }

        public void setCityId(int cityId) {
            this.cityId = cityId;
        }

        public void setProvinceId(int provinceId) {
            this.provinceId = provinceId;
        }


        public void setIndustryId(int industryId) {
            this.industryId = industryId;
        }


        public void setSubDirectionId(int subDirectionId) {
            this.subDirectionId = subDirectionId;
        }

        public void setSort(String sort) {
            this.sort = sort;
        }

        public String getKeyword() {
            return keyword;
        }

        public int getCityId() {
            return cityId;
        }

        public int getProvinceId() {
            return provinceId;
        }

        public String getIndustryParentId() {
            return industryParentId;
        }

        public void setIndustryParentId(String industryParentId) {
            this.industryParentId = industryParentId;
        }

        public String getMainDirectionId() {
            return mainDirectionId;
        }

        public void setMainDirectionId(String mainDirectionId) {
            this.mainDirectionId = mainDirectionId;
        }

        public int getIndustryId() {
            return industryId;
        }


        public int getSubDirectionId() {
            return subDirectionId;
        }

        public String getSort() {
            return sort;
        }
    }

    public void setUserCenterInfo(UserCenterRespVo info) {
        if (info == null) {
            return;
        }
        int step = info.getUserDesc().getApplyStepValue();
        SysUtil.savePref(PrefKeyConst.PREF_APPLY_STEP, step);
        if (SysUtil.getIntPref(PrefKeyConst.PREF_USER_IDENTITY) == 1 && info.getBeconsultanttip() == 0) {
            DialogManager.showToBeExpertDialog(getActivity(), this);
        }
    }


    @Override
    public void notifyTobeExpert() {
        control.notifyTobeExpert();
    }

    @Override
    public void tobeExpert() {
        Intent intent = new Intent(getActivity(), ActToBeExpert.class);
        intent.putExtra(IntentKey.FROM_WHERE, IntentKey.INTENT_VALUE_TO_BE_EXPERT_FROM_SEARCH);
        startActivity(intent);
    }

    @Override
    public void notifyBecomeExpert() {
        control.notifyBecomeExpert();
    }
}
