package com.noelia.contactstest.ui;

import android.support.v4.app.Fragment;


public class ContactDetailActivity extends BaseActivity {

    public static final String EXTRA_CONTACT_ID = "extra_contact_id";
    private String contactId;

    @Override
    protected Fragment getFragment() {
        contactId =  getIntent().getStringExtra(EXTRA_CONTACT_ID);
        return ContactDetailFragment.newInstance(contactId);
    }
}
