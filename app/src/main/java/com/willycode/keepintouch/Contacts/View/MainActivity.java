package com.willycode.keepintouch.Contacts.View;


import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.appinvite.AppInvite;
import com.google.android.gms.appinvite.AppInviteInvitation;
import com.google.android.gms.appinvite.AppInviteInvitationResult;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.willycode.keepintouch.BuildConfig;
import com.willycode.keepintouch.Contacts.KitApplication;
import com.willycode.keepintouch.Contacts.Model.Contact;
import com.willycode.keepintouch.Contacts.Model.ContactContract;
import com.willycode.keepintouch.Contacts.Model.ContactDbHelper;
import com.willycode.keepintouch.Contacts.Presenter.ContactPresenter;
import com.willycode.keepintouch.Contacts.Presenter.ContactPresenterImpl;
import com.willycode.keepintouch.Contacts.Utils.SwipeDismissListViewTouchListener;
import com.willycode.keepintouch.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ContactListView, AdapterView.OnItemClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {


    private ListView listView;
    private ContactPresenter presenter;
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_INVITE = 0;
    private static final int RESULT_PICK_CONTACT = 1;
    private static final int RESOLVE_CONNECTION_REQUEST_CODE = 2;
    private static final int RC_SIGN_IN = 3;
    private GoogleApiClient mGoogleApiClient;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String Frequence = "frequence";
    public static final String AlarmSet = "alarm_set";
    private ArrayAdapter<String> mAdapter;
    private SwipeRefreshLayout mySwipeRefreshLayout;
    private List<Contact> myContacts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(BuildConfig.DEBUG) {
            Intent i = new Intent("com.willycode.keepintouch.CALL_ACTION");
            i.putExtra(ContactDbHelper.ContactEntry.COLUMN_NAME, "Your Mom");
            sendBroadcast(i);
        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        listView = (ListView) findViewById(R.id.list);
        listView.setOnItemClickListener(this);
        mySwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.layout);
        mySwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                nothing();
            }
        });
        presenter = new ContactPresenterImpl(this,this);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(contactPickerIntent, RESULT_PICK_CONTACT);
            }
        });

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();


        // Create an auto-managed GoogleApiClient with acccess to App Invites.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(AppInvite.API)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        // Check for App Invite invitations and launch deep-link activity if possible.
        // Requires that an Activity is registered in AndroidManifest.xml to handle
        // deep-link URLs.
        boolean autoLaunchDeepLink = true;
        AppInvite.AppInviteApi.getInvitation(mGoogleApiClient, this, autoLaunchDeepLink)
                .setResultCallback(
                        new ResultCallback<AppInviteInvitationResult>() {
                            @Override
                            public void onResult(AppInviteInvitationResult result) {
                                Log.d(TAG, "getInvitation:onResult:" + result.getStatus());
                                if(result.getStatus().isSuccess())
                                {
                                    Tracker t = ((KitApplication) getApplication()).tracker();
                                    // Build and send an Event.
                                    t.send(new HitBuilders.EventBuilder()
                                            .setCategory(getString(R.string.category_id))
                                            .setAction(getString(R.string.accepted))
                                                    //.setLabel(getString(labelId))
                                            .build());
                                }
                                // Because autoLaunchDeepLink = true we don't have to do anything
                                // here, but we could set that to false and manually choose
                                // an Activity to launch to handle the deep link here.
                            }
                        });
        SharedPreferences prefs = getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        String frequence = prefs.getString(Frequence, null);
        if (frequence == null) {
            DialogFragment newFragment = new FrequenceFragment();
            newFragment.show(getSupportFragmentManager(), "frequences");
        }
    }

    private void nothing() {
        mySwipeRefreshLayout.setRefreshing(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_invite) {
            int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
            // Showing status
            if(status==ConnectionResult.SUCCESS) {
                if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                    Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                    startActivityForResult(signInIntent, RC_SIGN_IN);
                }
            }
            else{
                int requestCode = 10;
                Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
                dialog.show();
            }
            return true;
        }
        if (id == R.id.action_set_frequence) {
            DialogFragment newFragment = new FrequenceFragment();
            newFragment.show(getSupportFragmentManager(), "frequences");
            return true;
        }


        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        ContactContract cc = new ContactContract(MainActivity.this);
        List<Contact> l = cc.getAllContacts();
        Contact c = l.get(position);
        String num = c.getPhoneNumber();
        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
        phoneIntent.setData(Uri.parse("tel:" + num));
        try{
            startActivity(phoneIntent);
        }
        catch (Exception ex){
            showMessage("Phone call failed !");
        }
    }

    @Override
    public void setContacts(final List<Contact> contacts) {
        myContacts = contacts;
        List<String> items = new ArrayList<>();
        for(Contact c :contacts){
            items.add(c.getName());
        }
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(mAdapter);
        // Create a ListView-specific touch listener. ListViews are given special treatment because
        // by default they handle touches for their list items... i.e. they're in charge of drawing
        // the pressed state (the list selector), handling list item clicks, etc.
        SwipeDismissListViewTouchListener touchListener =
                new SwipeDismissListViewTouchListener(
                        listView,
                        new SwipeDismissListViewTouchListener.DismissCallbacks() {
                            @Override
                            public boolean canDismiss(int position) {
                                return true;
                            }

                            @Override
                            public void onDismiss(ListView listView, int[] reverseSortedPositions) {
                                for (int position : reverseSortedPositions) {
                                    deleteItem(contacts.get(position));
                                }
                                mAdapter.notifyDataSetChanged();
                            }
                        });
        listView.setOnTouchListener(touchListener);
        // Setting this scroll listener is required to ensure that during ListView scrolling,
        // we don't look for swipes.
        listView.setOnScrollListener(touchListener.makeScrollListener());
    }

    private void deleteItem(final Contact contact) {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        ContactContract cc = new ContactContract(getApplicationContext());
                        cc.deleteContact(contact);
                        presenter.onResume();
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.delete_item)+" "+contact.getName()+" ?").setPositiveButton(getString(android.R.string.ok), dialogClickListener)
                .setNegativeButton(getString(android.R.string.no), dialogClickListener).show();

    }

    @Override public void showProgress() {
        mySwipeRefreshLayout.setRefreshing(true);
        listView.setVisibility(View.INVISIBLE);
    }

    @Override public void hideProgress() {
        mySwipeRefreshLayout.setRefreshing(false);
        listView.setVisibility(View.VISIBLE);
    }

    // [START on_activity_result]
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult: requestCode=" + requestCode + ", resultCode=" + resultCode);
            if (resultCode == RESULT_OK) {
                switch (requestCode) {
                    // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
                    case RC_SIGN_IN:
                        GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
                        if (result.isSuccess()) {
                            Account[] accounts = AccountManager.get(this).getAccounts();
                            Intent intent = new AppInviteInvitation.IntentBuilder(getString(R.string.invitation_title))
                                    .setMessage(getString(R.string.invitation_message))
                                    .setDeepLink(Uri.parse(getString(R.string.invitation_deep_link)))
                                    .setCustomImage(Uri.parse(getString(R.string.invitation_custom_image)))
                                    .setCallToActionText(getString(R.string.invitation_cta))
                                    .setGoogleAnalyticsTrackingId("UA-70191148-1")
                                    .setAccount(accounts[0])
                                    .build();
                            startActivityForResult(intent, REQUEST_INVITE);
                        } else {
                            // Sending failed or it was canceled, show failure message to the user
                            showMessage(getString(R.string.google_play_services_error));
                        }

                    case RESOLVE_CONNECTION_REQUEST_CODE:
                        if (!mGoogleApiClient.isConnecting() &&
                                !mGoogleApiClient.isConnected()) {
                            mGoogleApiClient.connect();
                        }
                        break;
                    case RESULT_PICK_CONTACT:
                        contactPicked(data);
                        break;
                    case REQUEST_INVITE:
                        // Check how many invitations were sent and log a message
                        // The ids array contains the unique invitation ids for each invitation sent
                        // (one for each contact select by the user). You can use these for analytics
                        // as the ID will be consistent on the sending and receiving devices.
                        String[] ids = AppInviteInvitation.getInvitationIds(resultCode, data);
                        // Get tracker.
                        Tracker t = ((KitApplication) getApplication()).tracker();
                        // Build and send an Event.
                        t.send(new HitBuilders.EventBuilder()
                                .setCategory(getString(R.string.category_id))
                                .setAction(getString(R.string.sent))
                                //.setLabel(getString(labelId))
                                .build());
                        Log.d(TAG, getString(R.string.sent_invitations_fmt, ids.length));
                        break;
                }

            } else {
                if(requestCode == REQUEST_INVITE) {
                    // Sending failed or it was canceled, show failure message to the user
                    showMessage(getString(R.string.send_failed));
                }
            }
    }
    // [END on_activity_result]

    public void showMessage(String msg) {
        ViewGroup container = (ViewGroup) findViewById(R.id.layout);
        Snackbar.make(container, msg, Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Query the Uri and read contact details. Handle the picked contact data.
     * @param data
     */
    private void contactPicked(Intent data) {
        Cursor cursor = null;
        try {
            String phoneNo = null ;
            String name = null;
            // getData() method will have the Content Uri of the selected contact
            Uri uri = data.getData();
            //Query the content uri
            cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            // column index of the phone number
            int  phoneIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER);
            // column index of the contact name
            int  nameIndex =cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME);
            phoneNo = cursor.getString(phoneIndex);
            name = cursor.getString(nameIndex);
            Contact c = new Contact(name,phoneNo);
            SharedPreferences prefs = this.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
            int p = Integer.valueOf(prefs.getString(Frequence, null));
            String period = null;
            switch (p){
                case 0:
                    period = Contact.DAILY;
                    break;
                case 1:
                    //ogni 7 giorno
                    period = Contact.WEEKLY;
                    break;
                case 2:
                    //ogni 4 settimane
                    period = Contact.MONTHLY;
                    break;
                case 3:
                    //ogni 365 giorni
                    period = Contact.YEARLY;
                    break;
            }
            c.setPeriod(period);
            c.setLastCallTime(new Date());
            ContactContract cc = new ContactContract(this);
            cc.addContact(c);
            presenter.onResume();
        } catch (Exception e) {
            showMessage(e.getMessage());
        }
    }

    @Override
    public void onConnected(Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (connectionResult.hasResolution()) {
            try {
                connectionResult.startResolutionForResult(this, RESOLVE_CONNECTION_REQUEST_CODE);
            } catch (IntentSender.SendIntentException e) {
                // Unable to resolve, message user appropriately
                showMessage(getString(R.string.google_play_services_error));
            }
        } else {
            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
        }
    }
}
