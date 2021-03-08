package com.catherine.videoplay.ui.sofa;

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

@FragmentDestination(pageUrl = "main/tabs/sofa")
public class SofaFragment extends Fragment {

    private SofaViewModel mViewModel;

    public static SofaFragment newInstance() {
        return new SofaFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.sofa_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(SofaViewModel.class);
        // TODO: Use the ViewModel
    }

}