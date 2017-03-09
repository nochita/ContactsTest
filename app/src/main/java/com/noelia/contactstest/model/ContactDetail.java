package com.noelia.contactstest.model;

import java.util.List;
import java.util.Map;

/**
 * Created by nochita on 07/03/2017.
 */
public class ContactDetail extends Contact {

    private List<ContactDetailAddress> addresses;

    public List<ContactDetailAddress> getAddresses() {
        return addresses;
    }

    public void setAddresses(List<ContactDetailAddress> addresses) {
        this.addresses = addresses;
    }
}
