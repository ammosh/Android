package com.main.pokyfun;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.StringTokenizer;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

class Contact{
	String name = "";
	ArrayList<String> numbers = new ArrayList<String>();
}
public class Contacts extends Fragment {
	ArrayList<String> list = new ArrayList<String>();
	ArrayList<Contact> listContacts = new ArrayList<Contact>();
	ListView listV;
	ArrayAdapter<String> adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.contacts, container, false);
		adapter = new ArrayAdapter<String>(getActivity(),
				android.R.layout.simple_list_item_1, list);
		listV = (ListView) rootView.findViewById(R.id.listOfContacts);
		fetchContacts();
		listV.setAdapter(adapter);
		listV.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (position < list.size()){
					Contact cnt = listContacts.get(position);
					Intent intent = new Intent(getActivity(), SendPoke.class);
					Bundle b = new Bundle();
					b.putString("name", cnt.name);
					String nums [] = new String[cnt.numbers.size()];
					for (int i = 0; i < cnt.numbers.size(); i++) {
						nums[i] = cnt.numbers.get(i);
 					}
					b.putStringArray("numbers", nums);
					intent.putExtras(b);
					startActivity(intent);
				}
			}
		});
		return rootView;
	}

	public void fetchContacts() {

		String phoneNumber = null;
		String email = null;

		Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
		String _ID = ContactsContract.Contacts._ID;
		String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
		String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

		Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
		String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
		String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

		Uri EmailCONTENT_URI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
		String EmailCONTACT_ID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
		String DATA = ContactsContract.CommonDataKinds.Email.DATA;

		ContentResolver contentResolver = getActivity().getContentResolver();

		Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
				null);

		// Loop for every contact in the phone
		if (cursor.getCount() > 0) {

			while (cursor.moveToNext()) {
				StringBuffer output = new StringBuffer();
				String contact_id = cursor
						.getString(cursor.getColumnIndex(_ID));
				String name = cursor.getString(cursor
						.getColumnIndex(DISPLAY_NAME));

				int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
						.getColumnIndex(HAS_PHONE_NUMBER)));
				Contact newC = new Contact();
				if (hasPhoneNumber > 0) {
					newC.name = name;
					output.append("Name: " + name);

					// Query and loop for every phone number of the contact
					Cursor phoneCursor = contentResolver.query(
							PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
							new String[] { contact_id }, null);

					while (phoneCursor.moveToNext()) {
						phoneNumber = phoneCursor.getString(phoneCursor
								.getColumnIndex(NUMBER));
						output.append("\n" + phoneNumber);
						newC.numbers.add(phoneNumber);
					}

					phoneCursor.close();
				}
				if (output.length() > 0) {
					listContacts.add(newC);
				}
			}
			if (listContacts.size() > 0){
				Collections.sort(listContacts, new Comparator<Contact>(){
					   public int compare(Contact b1, Contact b2) {
						   return b1.name.compareTo(b2.name);
					   }
					});
				for (int i = 0; i < listContacts.size(); i++) {
					list.add(listContacts.get(i).name);
				}
				adapter.notifyDataSetChanged();
			}
		}
	}

	public void showToast(int position) {
		if (list != null && position < list.size())
			Toast.makeText(getActivity().getApplicationContext(),
					list.get(position), Toast.LENGTH_LONG).show();
		else
			Toast.makeText(getActivity().getApplicationContext(),
					"Error: retreiving contact", Toast.LENGTH_LONG).show();
	}
}
