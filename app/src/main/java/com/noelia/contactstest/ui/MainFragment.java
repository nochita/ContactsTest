package com.noelia.contactstest.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.noelia.contactstest.R;
import com.noelia.contactstest.helper.ConnectionHelper;
import com.noelia.contactstest.model.Contact;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nochita on 07/03/2017.
 */
public class MainFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private List<Contact> contactList;
    private RecyclerView recyclerView;
    private ContactAdapter adapter;
    private SwipeRefreshLayout swipeLayout;
    private boolean isRefreshing;

    public static Fragment newInstance(){
        return new MainFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getActivity()).build();
        ImageLoader.getInstance().init(config);
    }

    @Override
    public void onResume() {
        super.onResume();
        isRefreshing = false;
        getContacts();
    }

    private void getContacts(){
        new GetContactsAsyncTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_main, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swiperefresh);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeColors(getResources().getColor(android.R.color.holo_blue_bright));

        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

    }

    private void populateContacts(){
        if(adapter == null){
            adapter = new ContactAdapter(getActivity(), contactList);
            recyclerView.setAdapter(adapter);
        } else {
            adapter.swapElements(contactList);
        }
    }

    @Override
    public void onRefresh() {
        isRefreshing = true;
        getContacts();
        swipeLayout.setRefreshing(false);
    }

    private class GetContactsAsyncTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(!isRefreshing){
                progressDialog = ProgressDialog.show(getActivity(), getString(R.string.progress_dialog_title),
                        getString(R.string.progress_dialog_message), true);
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {

            String url = "https://private-d0cc1-iguanafixtest.apiary-mock.com/contacts";

            Type listType = new TypeToken<ArrayList<Contact>>(){}.getType();
            String response = ConnectionHelper.get(url, getActivity());
            contactList = new Gson().fromJson(response, listType);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (contactList != null && !contactList.isEmpty()){
                populateContacts();
            } else {
                new AlertDialog.Builder(getActivity())
                        .setTitle(R.string.error)
                        .setMessage(R.string.error_message)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                               getActivity().finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }

            if(!isRefreshing){
                progressDialog.dismiss();
            }
        }
    }
}
