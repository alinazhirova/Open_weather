package com.zhirova.alina.presentation.screens.start;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.zhirova.alina.domain.City;
import com.zhirova.alina.presentation.R;
import com.zhirova.alina.presentation.application.CityApplication;
import com.zhirova.alina.presentation.diff_util.CityDiffUtilCallback;
import com.zhirova.alina.presentation.screens.add.AddFragment;
import com.zhirova.alina.presentation.screens.detail.DetailFragment;
import com.zhirova.alina.presentation.screens.start.adapter.CityAdapter;

import java.util.ArrayList;
import java.util.List;


public class StartFragment extends Fragment implements StartContract.View,
        SwipeRefreshLayout.OnRefreshListener,
        CityAdapter.ClickListener {

    private final String TAG = StartFragment.class.getSimpleName();
    private final String BUNDLE_SELECTED = "SELECTED_ITEM";

    private StartContract.Presenter startPresenter;
    private List<City> oldCities = new ArrayList<>();
    private FragmentManager fragmentManager;
    private boolean isDualPane = false;
    public static String selectedCity = null;

    private CityAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView infoText;
    private SwipeRefreshLayout swipeRefreshLayout;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_start, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (savedInstanceState != null) {
            selectedCity = savedInstanceState.getString(BUNDLE_SELECTED);
        }
        startPresenter = new StartPresenter();
        fragmentManager = getActivity().getSupportFragmentManager();
        initUI(view);
        setHasOptionsMenu(true);
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_SELECTED, selectedCity);
    }


    @Override
    public void onStart() {
        super.onStart();
        startPresenter.subscribe(this, getContext());
        CityApplication.firstLoading = false;
    }


    @Override
    public void onStop() {
        super.onStop();
        startPresenter.unsubsribe(this);
    }


    @Override
    public void updateCitiesList(List<City> actualCities) {
        hideLoader();
        List<City> oldList = new ArrayList<>(oldCities);
        List<City> newList = new ArrayList<>(actualCities);
        oldCities = new ArrayList<>(actualCities);

        CityDiffUtilCallback cityDiffUtilCallback = new CityDiffUtilCallback(oldList, newList);
        DiffUtil.DiffResult cityDiffResult = DiffUtil.calculateDiff(cityDiffUtilCallback, true);
        initAdapter();
        adapter.setData(actualCities);
        cityDiffResult.dispatchUpdatesTo(adapter);

        View detailsFrame = getActivity().findViewById(R.id.details);
        isDualPane = detailsFrame != null && detailsFrame.getVisibility() == View.VISIBLE;

        if (isDualPane) {
            if (selectedCity == null) {
                selectedCity = actualCities.get(0).getName();
            }
            onClick(selectedCity);
            adapter.setSelectCity(selectedCity);
            if (CityApplication.firstLoading) {
                selectedCity = null;
            }
        }

        int selectedPosition = 0;
        if (selectedCity != null) {
            selectedPosition = adapter.positionById(selectedCity);
        }
        int finalSelectedPosition = selectedPosition;
        Handler scrollHandler = new Handler(Looper.getMainLooper());
        scrollHandler.postDelayed(() -> {
            recyclerView.smoothScrollToPosition(finalSelectedPosition);
        }, 200);
    }


    @Override
    public void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void showInfoAboutLackOfWeather() {
        infoText.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
    }


    @Override
    public void showInternetError() {
        Snackbar snackbar = Snackbar.make(getView(), getResources().getString(R.string.no_internet),
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void showError() {
        Snackbar snackbar = Snackbar.make(getView(), getResources().getString(R.string.unknown_error),
                Snackbar.LENGTH_LONG);
        snackbar.show();
    }


    @Override
    public void addCityUpdate(Pair<Double, Double> userLocation) {
        startPresenter.refreshCities(userLocation);
    }


    @Override
    public void onRefresh() {
        swipeRefreshLayout.setRefreshing(true);
        swipeRefreshLayout.setColorSchemeResources(R.color.refresh1, R.color.refresh2, R.color.refresh3);
        swipeRefreshLayout.postDelayed(() -> {
            swipeRefreshLayout.setRefreshing(false);
            startPresenter.refreshCities(null);
        }, 1000);
    }


    @Override
    public void onClick(String cityName) {
        selectedCity = cityName;
        int selectedPosition = adapter.positionById(cityName);
        if (selectedPosition > 0) {
            Handler scrollHandler = new Handler(Looper.getMainLooper());
            scrollHandler.postDelayed(() -> {
                recyclerView.smoothScrollToPosition(selectedPosition);
            }, 200);
        }

        if (isDualPane) {
            fragmentManager.popBackStack();
            DetailFragment details = (DetailFragment) fragmentManager.findFragmentById(R.id.details);
            if (details == null) {
                details = DetailFragment.create(cityName);
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.details, details);
                fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                fragmentTransaction.commit();
            }
            else if (!details.getShownCity().equals(cityName)){
                details.updateData(cityName);
            }
        } else {
            DetailFragment curFragment = DetailFragment.create(cityName);
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, curFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_add:
                AddFragment curFragment = new AddFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, curFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                return true;
            case R.id.menu_delete:
                if (selectedCity == null) {
                    Snackbar snackbar = Snackbar.make(getView(),
                            getResources().getString(R.string.no_chosen_city),
                            Snackbar.LENGTH_LONG);
                    snackbar.show();
                } else {
                    showDeletingDialog(selectedCity);
                    selectedCity = null;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void showDeletingDialog(String cityName) {
        String title = getResources().getString(R.string.title_question);
        String message = getResources().getString(R.string.desc_question) + " " + (cityName) + "?";
        String buttonYes = getResources().getString(R.string.yes);
        String buttonNo = getResources().getString(R.string.no);

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);

        builder.setPositiveButton(buttonYes, (dialog, which) -> {
            List<City> oldList = new ArrayList<>(oldCities);
            List<City> actualCities = startPresenter.removeCity(cityName);
            List<City> newList = new ArrayList<>(actualCities);
            oldCities = new ArrayList<>(actualCities);

            CityDiffUtilCallback cityDiffUtilCallback = new CityDiffUtilCallback(oldList, newList);
            DiffUtil.DiffResult cityDiffResult = DiffUtil.calculateDiff(cityDiffUtilCallback, true);

            adapter.setData(actualCities);
            cityDiffResult.dispatchUpdatesTo(adapter);

        });

        builder.setNegativeButton(buttonNo, (dialog, arg1) -> dialog.dismiss());
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private void initUI(View root) {
        recyclerView = root.findViewById(R.id.recycler_view_cities);
        progressBar = root.findViewById(R.id.progress_bar);
        infoText = root.findViewById(R.id.info_text_view);
        swipeRefreshLayout = root.findViewById(R.id.refresh);
        swipeRefreshLayout.setOnRefreshListener(this);

        progressBar.setVisibility(View.GONE);
        infoText.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);
    }


    private void initAdapter() {
        adapter = new CityAdapter(getContext(), selectedCity);
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
    }


}
