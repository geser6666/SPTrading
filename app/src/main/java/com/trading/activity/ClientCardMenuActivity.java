package com.trading.activity;

import java.util.Calendar;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.trading.R;
import com.trading.dao.ClientCardsDao;
import com.trading.db.DB;

public class ClientCardMenuActivity extends ListActivity {
	/** Called when the activity is first created. */
	private final static int REFERENCE_NEWCARD = 0;
	private final static int REFERENCE_CARDLIST = 1;
	private final static int REFERENCE_DAY_CARDLIST = 2;
	private int SELECTED_menu;// 0- новая, 1 - список

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			// setContentView(R.layout.main_menu);
			setListAdapter(ArrayAdapter.createFromResource(this,
					R.array.itemCCReference, R.layout.menu_list_item));
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}

		try {

		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}

		
		SQLiteDatabase db;
		try {
			db = DB.getDB(this);
			 db.execSQL("alter table cc_card add column prim varchar(100)");
				db.close();
		} catch (Exception e) {
			// TODO: handle exception
			//Toast.makeText(this, e.getMessage(), 10000).show();
		} finally {

		}
//*/
		
		
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub

		switch (position) {
		case REFERENCE_NEWCARD:
			try {

				Intent intent = new Intent();
				intent.putExtra("notemptyfieldname", "idhenkel");
				intent.putExtra("week_day", Calendar.getInstance().getTime().getDay());
				
				intent.setClass(this, PartnersListActivity.class);

				startActivityForResult(intent, 0);
			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), 10000).show();
			}

			break;

		case REFERENCE_CARDLIST:
			try {
				Intent intent = new Intent();
				intent.putExtra("notemptyfieldname", "idhenkel");
				intent.putExtra("week_day", Calendar.getInstance().getTime().getDay());

				intent.setClass(this, PartnersListActivity.class);
				startActivityForResult(intent, 1);
				// startActivity(new Intent(this,TovarsListActivity.class));
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), 10000).show();
			}

			break;
		case REFERENCE_DAY_CARDLIST:
			try {
				Intent intent = new Intent();
				intent.setClass(this, ClientCardDayListActivity.class);
				startActivity(intent);
				// startActivity(new Intent(this,TovarsListActivity.class));
			} catch (Exception e) {
				Toast.makeText(this, e.getMessage(), 10000).show();
			}

			break;

		default:

			Toast.makeText(this, "В разработке.", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		Intent intent;
		final int partnerid;
		final String partnername;
		final String cat;
		if (resultCode != 0) {
			switch (requestCode) {
			case 0:
				partnerid=data.getExtras().getInt("_id");
				partnername=data.getExtras().getString("name");
				cat=data.getExtras().getString("cat");
				AlertDialog.Builder builder=new AlertDialog.Builder(this);
				builder.setMessage("Создать новую карту клиента на основе предыдущей?");
				builder.setPositiveButton("Да",
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub
								ClientCardsDao ccd = new ClientCardsDao(
										ClientCardMenuActivity.this);
								int uu = ccd.createNewClientCard(partnerid);

								Intent intent = new Intent();
								intent.putExtra("_ID", uu);
								if (uu == 0) {
									intent.putExtra("partnerid", partnerid);
									intent.putExtra("partnername", partnername);
									intent.putExtra("partnercat", cat);
								}
								intent.setClass(ClientCardMenuActivity.this,
										ClientCardActivity.class);
								// setResult(RESULT_OK, intent);
								// ClientCardListActivity.this.finish();
								startActivity(intent);

							}

						});
				builder.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Intent intent = new Intent();
						intent.putExtra("_ID", 0);
						intent.putExtra("partnerid", partnerid);
						intent.putExtra("partnername", partnername);
						intent.putExtra("partnercat", cat);
						intent.setClass(ClientCardMenuActivity.this, ClientCardActivity.class);
						startActivity(intent);		
					}
				});
				builder.setCancelable(false);
				builder.show();
				// Toast.makeText(this, "Выбрана новая",
				// Toast.LENGTH_SHORT).show();
				

				break;
			case 1:
				// Toast.makeText(this, "Выбран список",
				// Toast.LENGTH_SHORT).show();
				intent = new Intent();
				intent.putExtra("partnerid", data.getExtras().getInt("_id"));
				intent.putExtra("partnername", data.getExtras().getString(
						"name"));
				intent.setClass(this, ClientCardListActivity.class);
				startActivity(intent);

				break;

			default:
				break;
			}
		}

	}

}