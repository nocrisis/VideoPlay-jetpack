package com.catherine.videoplay.util;

import android.content.ComponentName;

import androidx.fragment.app.FragmentActivity;
import androidx.navigation.ActivityNavigator;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavGraphNavigator;
import androidx.navigation.NavigatorProvider;
import androidx.navigation.fragment.FragmentNavigator;

import com.catherine.libcommon.AppGlobals;
import com.catherine.videoplay.FixFragmentNavigator;
import com.catherine.videoplay.model.Destination;

import java.util.HashMap;

public class NavGraphBuilder {
    public static void build(NavController controller, FragmentActivity activity, int containerId) {

        NavigatorProvider provider = controller.getNavigatorProvider();
//        FragmentNavigator fragmentNavigator = provider.getNavigator(FragmentNavigator.class);
        NavGraph navGraph = new NavGraph(new NavGraphNavigator(provider));
        FixFragmentNavigator fragmentNavigator = new FixFragmentNavigator(activity, activity.getSupportFragmentManager(), containerId);
        provider.addNavigator(fragmentNavigator);

        ActivityNavigator activityNavigator = provider.getNavigator(ActivityNavigator.class);

        HashMap<String, Destination> destConfig = AppConfig.getDestConfig();
        for (Destination value : destConfig.values()) {
            if (value.isFragment()) {
                FragmentNavigator.Destination destination = fragmentNavigator.createDestination();
                destination.setClassName(value.getClassName());
                destination.setId(value.getId());
                destination.addDeepLink(value.getPageUrl());
                navGraph.addDestination(destination);

            } else {
                ActivityNavigator.Destination destination = activityNavigator.createDestination();
                destination.setId(value.getId());
                destination.setComponentName(new ComponentName(AppGlobals.getApplication().getPackageName(), value.getClassName()));
                destination.addDeepLink(value.getPageUrl());
                navGraph.addDestination(destination);

            }
            if (value.isAsStarter()) {
                navGraph.setStartDestination(value.getId());

            }
        }
        controller.setGraph(navGraph);

    }
}
