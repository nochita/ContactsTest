package com.noelia.contactstest.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.noelia.contactstest.R;
import com.noelia.contactstest.helper.UIHelper;
import com.noelia.contactstest.model.Contact;
import com.noelia.contactstest.model.ContactPhone;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by nochita on 07/03/2017.
 */
public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private Context context;
    private List<Contact> contactList;

    public ContactAdapter(Context context, List<Contact> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.contact_item, parent, false);

        return new ContactViewHolder(itemView, new ContactAdapter.IMyViewHolderClicks() {

            @Override
            public void onClick(View caller) {
                Contact contact = (Contact) caller.getTag();

                Intent intent = new Intent(context, ContactDetailActivity.class);
                intent.putExtra(ContactDetailActivity.EXTRA_CONTACT_ID, contact.getId());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public void onBindViewHolder(ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);

        ImageLoader.getInstance().displayImage(contact.getThumb(), holder.imageView);
        holder.firstNameTextView.setText(contact.getFirstName());
        holder.lastNameTextView.setText(contact.getLastName());
        holder.birthdateTextView.setText(context.getString(R.string.birthdate,
                UIHelper.formatDateToUserFriendly(contact.getBirthdate())));
        UIHelper.populatePhonesIntoContainer(contact.getPhones(), holder.phoneContainer, context);

        holder.cardContainer.setTag(contact);
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void swapElements(List<Contact> contactList){
        this.contactList = contactList;
        notifyDataSetChanged();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewGroup cardContainer;
        public ImageView imageView;
        public TextView firstNameTextView;
        public TextView lastNameTextView;
        public TextView birthdateTextView;
        public ViewGroup phoneContainer;
        public IMyViewHolderClicks listener;

        public ContactViewHolder(View itemView, IMyViewHolderClicks listener) {
            super(itemView);
            cardContainer = (ViewGroup) itemView.findViewById(R.id.card_container);
            imageView = (ImageView) itemView.findViewById(R.id.contact_image);
            firstNameTextView = (TextView) itemView.findViewById(R.id.contact_first_name);
            lastNameTextView = (TextView) itemView.findViewById(R.id.contact_last_name);
            birthdateTextView = (TextView) itemView.findViewById(R.id.contact_birth_date);
            phoneContainer = (ViewGroup) itemView.findViewById(R.id.contact_phone_container);

            this.listener = listener;
            cardContainer.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            this.listener.onClick(view);
        }
    }

    public interface IMyViewHolderClicks {
        public void onClick(View caller);
    }
}
