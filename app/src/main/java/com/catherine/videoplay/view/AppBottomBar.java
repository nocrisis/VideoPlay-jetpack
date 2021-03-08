package com.catherine.videoplay.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.catherine.videoplay.R;
import com.catherine.videoplay.model.BottomBar;
import com.catherine.videoplay.model.Destination;
import com.catherine.videoplay.util.AppConfig;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.LabelVisibilityMode;

import java.util.List;

public class AppBottomBar extends BottomNavigationView {
    private BottomBar bottomBar;
    private static int[] sIcons = new int[]{R.drawable.icon_tab_home, R.drawable.icon_tab_sofa, R.drawable.icon_tab_publish, R.drawable.icon_tab_find, R.drawable.icon_tab_mine};

    public AppBottomBar(@NonNull Context context) {
        this(context, null);
    }

    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public AppBottomBar(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        bottomBar = AppConfig.getBottomBar();
        List<BottomBar.Tabs> tabs = bottomBar.getTabs();
        int[][] states = new int[2][];
        states[0] = new int[]{android.R.attr.state_selected};
        states[1] = new int[]{};
        int[] colors = new int[]{Color.parseColor(bottomBar.getActiveColor()), Color.parseColor(bottomBar.getInActiveColor())};
        ColorStateList stateList = new ColorStateList(states, colors);
        setItemIconTintList(stateList);
        setItemTextColor(stateList);
        //LABEL_VISIBILITY_LABELED:设置按钮的文本为一直显示模式
        //LABEL_VISIBILITY_AUTO:当按钮个数小于三个时一直显示，或者当按钮个数大于3个且小于5个时，被选中的那个按钮文本才会显示
        //LABEL_VISIBILITY_SELECTED：只有被选中的那个按钮的文本才会显示
        //LABEL_VISIBILITY_UNLABELED:所有的按钮文本都不显示
        setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
//        setSelectedItemId(bottomBar.getSelectTab());
        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.Tabs tab = tabs.get(i);
            if (!tab.isEnable()) {
                return;
            }
            int id = getItemId(tab.getPageUrl());
            if (id < 0) {
                return;
            }
            MenuItem item = getMenu().add(0, id, tab.getIndex(), tab.getTitle());
            item.setIcon(sIcons[tab.getIndex()]);
        }
        //此处给按钮icon设置大小
        for (int i = 0; i < tabs.size(); i++) {
            BottomBar.Tabs tab = tabs.get(i);
            int iconSize = dp2px(tab.getSize());
            BottomNavigationMenuView menuView = (BottomNavigationMenuView) getChildAt(0);
            BottomNavigationItemView itemView = (BottomNavigationItemView) menuView.getChildAt(tab.getIndex());
            itemView.setIconSize(iconSize);
            if (TextUtils.isEmpty(tab.getTitle())) {
                itemView.setIconTintList(ColorStateList.valueOf(Color.parseColor(tab.getTintColor())));
                itemView.setShifting(false);//取消点击时上下浮动的效果
            }
        }
        /**
         * 如果想要禁止掉所有按钮的点击浮动效果。
         * 那么还需要给选中和未选中的按钮配置一样大小的字号。
         *
         *  在MainActivity布局的AppBottomBar标签增加如下配置，
         *  @style/active，@style/inActive 在style.xml中
         *  app:itemTextAppearanceActive="@style/active"
         *  app:itemTextAppearanceInactive="@style/inActive"
         */
        //底部导航栏默认选中项
        if (bottomBar.getSelectTab() != 0) {
            BottomBar.Tabs selectTab = bottomBar.getTabs().get(bottomBar.getSelectTab());
            if (selectTab.isEnable()) {
                int itemId = getItemId(selectTab.getPageUrl());
                //这里需要延迟一下 再定位到默认选中的tab
                //因为 咱们需要等待内容区域,也就NavGraphBuilder解析数据并初始化完成，
                //否则会出现 底部按钮切换过去了，但内容区域还没切换过去
                post(() -> setSelectedItemId(itemId));
            }
        }
    }

    private int dp2px(int size) {
        float value = getContext().getResources().getDisplayMetrics().density * size + 0.5f;
        return (int) value;
    }


    private int getItemId(String pageUrl) {
        Destination destination = AppConfig.getDestConfig().get(pageUrl);
        if (destination == null) {
            return -1;
        }
        return destination.getId();
    }
}
