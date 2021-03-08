package com.catherine.videoplay.ui.publish;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.catherine.libnavannotation.FragmentDestination;
import com.catherine.videoplay.R;

@FragmentDestination(pageUrl = "main/tabs/publish")
public class PublishFragment extends Fragment {

    private PublishViewModel mViewModel;

    public static PublishFragment newInstance() {
        return new PublishFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.publish_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(PublishViewModel.class);
        // TODO: Use the ViewModel
    }

}