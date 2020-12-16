package com.example.testdrivendevelopment;

import com.example.testdrivendevelopment.contacts.Contact;

import java.util.List;

public interface FetchContactsUseCase {
    void fetchContacts(String filterTerm);

    void registerListener(Listener listener);

    interface Listener {
        void onContactsFetched(List<Contact> contacts);

        void onContactsFetchFailure();

        void onNetworkError();
    }
}
