package com.noelia.contactstest.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.noelia.contactstest.R;
import com.noelia.contactstest.helper.ConnectionHelper;
import com.noelia.contactstest.helper.UIHelper;
import com.noelia.contactstest.model.ContactDetail;
import com.noelia.contactstest.model.ContactDetailAddress;

/**
 * Created by nochita.
 */
public class ContactDetailFragment extends Fragment {

    public static final String ARG_CONTACT_ID = "contact_id";
    private ContactDetail contactDetail;
    private String contactId;
    private SimpleDraweeView imageView;
    private TextView nameTextView;
    private TextView birthdateTextView;
    private ViewGroup phoneContainer;
    private ViewGroup addressContainer;

    public static Fragment newInstance(String contactId){
        ContactDetailFragment fragment = new ContactDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_CONTACT_ID, contactId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle args = getArguments();
        contactId =  args.getString(ARG_CONTACT_ID);
    }

    @Override
    public void onResume() {
        super.onResume();
        new GetContactDetailsAsyncTask().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contact_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imageView = (SimpleDraweeView) view.findViewById(R.id.photo_detail_imageview);
        nameTextView = (TextView) view.findViewById(R.id.contact_name);
        birthdateTextView = (TextView) view.findViewById(R.id.contact_birth_date);
        phoneContainer = (ViewGroup) view.findViewById(R.id.contact_phone_container);
        addressContainer = (ViewGroup) view.findViewById(R.id.contact_address_container);
    }

    private void populateData(){
        imageView.setImageURI(contactDetail.getPhoto());
        nameTextView.setText(contactDetail.getFirstName() + " " + contactDetail.getLastName());
        birthdateTextView.setText(getString(R.string.birthdate,
                UIHelper.formatDateToUserFriendly(contactDetail.getBirthdate())));
        UIHelper.populatePhonesIntoContainer(contactDetail.getPhones(), phoneContainer, getActivity());

        for(ContactDetailAddress address : contactDetail.getAddresses()){
            if(!TextUtils.isEmpty(address.getHome())){
                TextView textView = new TextView(getActivity());
                textView.setText(getString(R.string.address_home, address.getHome()));
                addressContainer.addView(textView);
            }
            if(!TextUtils.isEmpty(address.getWork())){
                TextView textView = new TextView(getActivity());
                textView.setText(getString(R.string.address_work, address.getWork()));
                addressContainer.addView(textView);
            }
        }
    }

    private class GetContactDetailsAsyncTask extends AsyncTask<Void, Void, Void>{

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), getString(R.string.progress_dialog_title),
                    getString(R.string.progress_dialog_message), true);
        }

        @Override
        protected Void doInBackground(Void... params) {
            String url = "https://private-d0cc1-iguanafixtest.apiary-mock.com/contacts/" + contactId;

            String response = ConnectionHelper.get(url, getActivity());
            contactDetail = new Gson().fromJson(response, ContactDetail.class);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            populateData();
            progressDialog.dismiss();
        }
    }
}
