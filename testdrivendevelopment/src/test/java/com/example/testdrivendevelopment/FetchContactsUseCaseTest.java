package com.example.testdrivendevelopment;

import com.example.testdrivendevelopment.contacts.Contact;
import com.example.testdrivendevelopment.networking.ContactSchema;
import com.example.testdrivendevelopment.networking.GetContactsHttpEndpoint;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.stubbing.Answer;

import java.util.ArrayList;
import java.util.List;

import static com.example.testdrivendevelopment.networking.GetContactsHttpEndpoint.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class FetchContactsUseCaseTest {

    public static final String FILTER_TERM = "Filter term";
    public static final String ID = "12345";
    public static final String FULL_NAME = "Full Name";
    public static final String IMAGEURL = "www.imageurl.com";
    public static final String PHONE_NUMBER = "123456789";
    public static final double AGE = 23.3;

    private FetchContactsUseCase fetchContactsUseCase;

    @Mock
    private GetContactsHttpEndpoint getContactsHttpEndpointMock;
    @Mock
    private FetchContactsUseCase.Listener listenerMock1;
    @Mock
    private FetchContactsUseCase.Listener listenerMock2;

    @Captor
    ArgumentCaptor<List<Contact>> contactListAc;

    @Before
    public void setUp() throws Exception {
        fetchContactsUseCase = new FetchContactUseCaseImpl(getContactsHttpEndpointMock);
        respondSuccessfully();
    }


    @Test
    public void givenEndpointSucceeds_fetchContacts_passesCorrectFilterTerm() {
        // Arrange
        ArgumentCaptor<String> argumentCaptor = ArgumentCaptor.forClass(String.class);
        // Act
        fetchContactsUseCase.fetchContacts(FILTER_TERM);
        // Assert
        verify(getContactsHttpEndpointMock, times(1))
                .getContacts(argumentCaptor.capture(), any(Callback.class));
        assertEquals(FILTER_TERM, argumentCaptor.getValue());
    }

    @Test
    public void givenEndpointSucceeds_fetchContacts_notifiesListenersWithContactData() {
        // Arrange
        fetchContactsUseCase.registerListener(listenerMock1);
        fetchContactsUseCase.registerListener(listenerMock2);
        // Act
        fetchContactsUseCase.fetchContacts(FILTER_TERM);
        // Assert
        verify(listenerMock1).onContactsFetched(contactListAc.capture());
        verify(listenerMock2).onContactsFetched(contactListAc.capture());
        List<Contact> contactList1 = contactListAc.getAllValues().get(0);
        List<Contact> contactList2 = contactListAc.getAllValues().get(1);
        assertEquals(contactList1, dummyContactList());
        assertEquals(contactList2, dummyContactList());
    }

    @Test
    public void givenEndpointFailsWithGeneralError_fetchContacts_shouldNotifyListenersWithAFailure() {
        // Arrange
        failWith(FailReason.GENERAL_ERROR);
        fetchContactsUseCase.registerListener(listenerMock1);
        fetchContactsUseCase.registerListener(listenerMock2);
        // Act
        fetchContactsUseCase.fetchContacts(FILTER_TERM);
        // Assert
        verify(listenerMock1).onContactsFetchFailure();
        verify(listenerMock2).onContactsFetchFailure();
    }

    // If request fails with a network error, then the use case should notify listeners of a network
    // error


    @Test
    public void givenEndpointFailsWithNetworkError_fetchContacts_shouldNotifyListenersOfANetworkError() {
        // Arrange
        failWith(FailReason.NETWORK_ERROR);
        fetchContactsUseCase.registerListener(listenerMock1);
        fetchContactsUseCase.registerListener(listenerMock2);
        // Act
        fetchContactsUseCase.fetchContacts(FILTER_TERM);
        // Assert
        verify(listenerMock1).onNetworkError();
    }

    private void respondSuccessfully() {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Callback callback = invocation.getArgument(1);
                callback.onGetContactsSucceeded(dummyContactSchemaList());
                return null;
            }
        }).when(getContactsHttpEndpointMock)
                .getContacts(any(String.class), any(Callback.class));
    }

    private void failWith(final FailReason failReason) {
        doAnswer(new Answer() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                Callback callback = invocation.getArgument(1);
                callback.onGetContactsFailed(failReason);
                return null;
            }
        }).when(getContactsHttpEndpointMock)
                .getContacts(any(String.class), any(Callback.class));
    }

    private List<ContactSchema> dummyContactSchemaList() {
        List<ContactSchema> contactSchemas = new ArrayList<>();
        contactSchemas.add(new ContactSchema(ID, FULL_NAME, PHONE_NUMBER, IMAGEURL, AGE));
        return contactSchemas;
    }

    private List<Contact> dummyContactList() {
        List<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact(ID, FULL_NAME, IMAGEURL));
        return contacts;
    }
}