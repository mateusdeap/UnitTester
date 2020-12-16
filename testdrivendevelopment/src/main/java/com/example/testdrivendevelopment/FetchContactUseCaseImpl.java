package com.example.testdrivendevelopment;

import com.example.testdrivendevelopment.contacts.Contact;
import com.example.testdrivendevelopment.networking.ContactSchema;
import com.example.testdrivendevelopment.networking.GetContactsHttpEndpoint;

import java.util.ArrayList;
import java.util.List;

import static com.example.testdrivendevelopment.networking.GetContactsHttpEndpoint.*;

public class FetchContactUseCaseImpl implements FetchContactsUseCase {

    private final GetContactsHttpEndpoint httpEndpoint;
    private List<Listener> listeners;

    public FetchContactUseCaseImpl(GetContactsHttpEndpoint httpEndpoint) {
        this.httpEndpoint = httpEndpoint;
        this.listeners = new ArrayList<>();
    }

    @Override
    public void fetchContacts(String filterTerm) {
        httpEndpoint.getContacts(filterTerm, new Callback() {
            @Override
            public void onGetContactsSucceeded(List<ContactSchema> cartItems) {
                for (Listener listener :
                        listeners) {
                    listener.onContactsFetched(contactsFromContactSchemaList(cartItems));
                }
            }

            @Override
            public void onGetContactsFailed(FailReason failReason) {
                switch (failReason) {
                    case GENERAL_ERROR:
                        for (Listener listener :
                                listeners) {
                            listener.onContactsFetchFailure();
                        }
                        break;
                    case NETWORK_ERROR:
                        for (Listener listener :
                                listeners) {
                            listener.onNetworkError();
                        }
                        break;
                }
            }
        });
    }

    private List<Contact> contactsFromContactSchemaList(List<ContactSchema> contactSchemas) {
        List<Contact> contacts = new ArrayList<>();
        for (ContactSchema schema :
                contactSchemas) {
            contacts.add(new Contact(
                    schema.getId(),
                    schema.getFullName(),
                    schema.getImageUrl()
            ));
        }
        return contacts;
    }

    @Override
    public void registerListener(Listener listener) {
        this.listeners.add(listener);
    }
}
