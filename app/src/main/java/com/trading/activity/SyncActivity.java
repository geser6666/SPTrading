package com.trading.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.trading.R;
import com.trading.dao.ClientCardsDao;
import com.trading.dao.OrdersDao;
import com.trading.dao.ParamsDao;
import com.trading.dao.PartnersDao;
import com.trading.dao.TovarsDao;
import com.trading.db.DB;
import com.trading.transfer.DBInteraction;
import com.trading.utils.CC_card;
import com.trading.utils.ClientCard;
import com.trading.utils.Grsklad;
import com.trading.utils.Order;
import com.trading.utils.Partner;
import com.trading.utils.Tovar;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import static android.support.v4.app.ActivityCompat.startActivity;

public class SyncActivity extends ListActivity {
	private final static int SYNCMENU_ALL = 0;
	private final static int SYNCMENU_DOCS = 2;
	private final static int SYNCMENU_PARTNER = 3;
	private final static int SYNCMENU_TOVAR = 4;
	private final static int SYNCMENU_CC_TEMPLATES = 5;
	private final static int SYNCMENU_CC_CARD = 6;

	private static final int DIALOG_LOAD_KEY = 1;

	protected ProgressDialog progressdialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			// setContentView(R.layout.main_menu);
			setListAdapter(ArrayAdapter.createFromResource(this,
					R.array.itemSync, R.layout.menu_list_item));
		} catch (Exception e) {
			Toast t = new Toast(getApplicationContext());
			t.setText(e.getMessage());
		}

	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub

		switch (position) {
			case SYNCMENU_ALL:
				String[] ddl = this.getResources().getStringArray(
						this.getResources().getIdentifier("ver_4","array",this.getPackageName())
				);
				for (String ddl_txt: ddl)
				{
					try {
						DB.getDB(this).execSQL(ddl_txt);
					} catch (Exception e) {
						String xx;
						xx=e.getMessage();
						// TODO: handle exception
					}

				}

				break;

			case SYNCMENU_DOCS:
			try {

				SyncDocs();

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(SyncActivity.this, "неудача" + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

			break;

		case SYNCMENU_PARTNER:
			try {
				
				 SyncPartner();

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(SyncActivity.this, "неудача" + e.getMessage(),
						Toast.LENGTH_SHORT).show();
			}

			break;
		case SYNCMENU_TOVAR:
			try {
			SyncTovar();

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}
			break;

		case SYNCMENU_CC_TEMPLATES:
			try {
			SyncCCTemplates();

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;
		case SYNCMENU_CC_CARD:
			try {
				
			SyncCC();

			} catch (Exception e) {
				// TODO: handle exception
				Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
			}

			break;
		default:

			Toast.makeText(this, "В разработке.", Toast.LENGTH_SHORT).show();
			break;
		}
	}

	public void SyncPartner() {
		showDialog(DIALOG_LOAD_KEY); // Показываем диалог
		// Запускаем в отдельном потоке загрузку ваших данных
		new Thread(new Runnable() {

			@Override
			public void run() {
				ArrayList<Partner> ar = null;
				try {
					showProgressMsg("Начинаем загрузку партнеров...");

					DBInteraction dbitem = new DBInteraction(
							ParamsDao.getAgentId(SyncActivity.this)
							, 1,SyncActivity.this);
					String s;

					ar = dbitem.GetPartners();

					PartnersDao pd = new PartnersDao(SyncActivity.this);
					pd.clearPartnersWeekDay();
					showProgressMsg("Добавление партнеров в базу...");

					int err=
					pd.setInsertUpdatePartners(ar);
					showMsg("Обработано:" + String.valueOf(ar.size())
							+ " партнеров. Пропущено: "+String.valueOf(err));
					handler.sendEmptyMessage(0); // посылаем уведомление об
													// окончании загрузки
				} catch (IOException e) {
					// Toast.makeText(SyncActivity.this, e.getMessage(),
					// Toast.LENGTH_SHORT).show();
					showMsg(e.getMessage());
				} finally {

				}

			}
		}).start();

	}

	public void SyncTovar() {
		showDialog(DIALOG_LOAD_KEY); // Показываем диалог
		// Запускаем в отдельном потоке загрузку ваших данных
		new Thread(new Runnable() {

			@Override
			public void run() {
				DBInteraction dbitem = new DBInteraction(
						ParamsDao.getAgentId(SyncActivity.this), 1, SyncActivity.this);
				String s;
				ArrayList<Grsklad> ar = null;
				ArrayList<Tovar> art = null;
				try {
					showProgressMsg("Получение групп товаров...");
					ar = dbitem.GetGroups();
					showProgressMsg("Добавление групп товаров...");
					TovarsDao td = new TovarsDao(SyncActivity.this);
					td.setInsertUpdateGroups(ar);
					showProgressMsg("Получение товаров...");
					art = dbitem.GetTovari();
					showProgressMsg("Добавление товаров...");
					td = new TovarsDao(SyncActivity.this);
					int err=
					td.setInsertUpdateTovars(art);

					showMsg("Обработано:" + String.valueOf(ar.size())
							+ "групп и " + String.valueOf(art.size())
							+ "товаров. Пропущено "+String.valueOf(err)+" товаров.");
					handler.sendEmptyMessage(0); // посылаем уведомление об

				} catch (Exception e) {
					// Toast.makeText(this, e.getMessage(),
					// Toast.LENGTH_SHORT).show();
					showMsg(e.getMessage());

				} finally {
					// Toast.makeText(this,
					// "Обработано:"+String.valueOf(ar.size())+"групп и "+
					// String.valueOf(art.size())+"товаров.",
					// Toast.LENGTH_SHORT).show();
				}
			}
		}).start();

	}

	// Это ваш обработчик удаления диалога и к примеру запуск новой Activity

	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			dismissDialog(DIALOG_LOAD_KEY); // удаляем диалог

		}

	};

	@Override
	// Здесь вы создаете все диалоги
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DIALOG_LOAD_KEY: {

			progressdialog = new ProgressDialog(this);
			// progressdialog.setMessage("Загрузка, подождите пожалуйста...");
			progressdialog.setCancelable(true);
			return progressdialog;
		}
		}
		return super.onCreateDialog(id);
	}

	public void showMsg(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(SyncActivity.this, msg, Toast.LENGTH_LONG)
						.show();
			}
		});

	}

	public void showProgressMsg(final String msg) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				progressdialog.setMessage(msg);
			}
		});

	}

	public void SyncDocs() {
		
		
		
		showDialog(DIALOG_LOAD_KEY); // Показываем диалог
		// Запускаем в отдельном потоке загрузку ваших данных
		new Thread(new Runnable() {

			@Override
			public void run() {
				DBInteraction dbitem = new DBInteraction(
						ParamsDao.getAgentId(SyncActivity.this)
						
						, 1, SyncActivity.this);
				String s;
				ArrayList<Order> ar = null;

				try {
					showProgressMsg("Начинаем загрузку документов...");
					OrdersDao od = new OrdersDao(SyncActivity.this);
					ar = dbitem.GetLastOrders();
					od.DelOrders();
					for (int i = 0; i < ar.size(); i++) {
						
							showProgressMsg("Добавление документа "+ String.valueOf(i + 1));
						try {
							od.SaveOrder(ar.get(i));	
						} catch (Exception e) {
							// TODO: handle exception
						}

						
					}

					showMsg("Обработано:" + String.valueOf(ar.size())
							+ " документов.");
					handler.sendEmptyMessage(0); // посылаем уведомление об
													// окончании загрузки
				} catch (Exception e) {
					// Toast.makeText(this, e.getMessage(),
					// Toast.LENGTH_SHORT).show();
					showMsg(e.getMessage()+"SyncDoc");

				} finally {
					// Toast.makeText(this,
					// "Обработано:"+String.valueOf(ar.size())+"групп и "+
					// String.valueOf(art.size())+"товаров.",
					// Toast.LENGTH_SHORT).show();
				}
	}
		}).start();
	}
public void SyncCCTemplates() {
		
		
		
		showDialog(DIALOG_LOAD_KEY); // Показываем диалог
		// Запускаем в отдельном потоке загрузку ваших данных
		new Thread(new Runnable() {

			@Override
			public void run() {
				DBInteraction dbitem = new DBInteraction(
						ParamsDao.getAgentId(SyncActivity.this), 1, SyncActivity.this);
				String s;
				ArrayList<ClientCard> ar = null;

				try {
					showProgressMsg("Начинаем загрузку шаблонов...");
					ClientCardsDao ccd = new ClientCardsDao(SyncActivity.this);
					ar = dbitem.GetCardTemplates();
					for (int i = 0; i < ar.size(); i++) {
						
							showProgressMsg("Добавление карточки "+ String.valueOf(i + 1));
						try {
							ccd.SaveCC_cardTemplate(ar.get(i));	
						} catch (Exception e) {
							showMsg(e.getMessage()+"SyncDoc");
							handler.sendEmptyMessage(0); // посылаем уведомление об
							// окончании загрузки
						}

						
					}

					showMsg("Обработано:" + String.valueOf(ar.size())
							+ " документов.");
					handler.sendEmptyMessage(0); // посылаем уведомление об
													// окончании загрузки
				} catch (Exception e) {
					// Toast.makeText(this, e.getMessage(),
					// Toast.LENGTH_SHORT).show();
					showMsg(e.getMessage()+"SyncDoc");
					handler.sendEmptyMessage(0); // посылаем уведомление об
					// окончании загрузки

				} finally {
					// Toast.makeText(this,
					// "Обработано:"+String.valueOf(ar.size())+"групп и "+
					// String.valueOf(art.size())+"товаров.",
					// Toast.LENGTH_SHORT).show();
				}
	}
		}).start();
	}
public void SyncCC() {
	
	
	
	showDialog(DIALOG_LOAD_KEY); // Показываем диалог
	// Запускаем в отдельном потоке загрузку ваших данных
	new Thread(new Runnable() {

		@Override
		public void run() {
			DBInteraction dbitem = new DBInteraction(
					ParamsDao.getAgentId(SyncActivity.this)
					
					, 
					1, SyncActivity.this);
			String s;
			ArrayList<CC_card> ar = null;

			try {
				showProgressMsg("Начинаем загрузку карт...");
				ClientCardsDao ccd = new ClientCardsDao(SyncActivity.this);
				ccd.delAllCards();
				ar = dbitem.GetCC_Card();
				for (int i = 0; i < ar.size(); i++) {
					
						showProgressMsg("Добавление карточки "+ String.valueOf(i + 1));
					try {
						ccd.SaveCC_card(ar.get(i));	
					} catch (Exception e) {
						showMsg(e.getMessage()+"SyncDoc");
						handler.sendEmptyMessage(0); // посылаем уведомление об
						// окончании загрузки
					}

					
				}

				showMsg("Обработано:" + String.valueOf(ar.size())
						+ " документов.");
				handler.sendEmptyMessage(0); // посылаем уведомление об
												// окончании загрузки
			} catch (Exception e) {
				// Toast.makeText(this, e.getMessage(),
				// Toast.LENGTH_SHORT).show();
				showMsg(e.getMessage()+"SyncDoc");
				handler.sendEmptyMessage(0); // посылаем уведомление об
				// окончании загрузки

			} finally {
				// Toast.makeText(this,
				// "Обработано:"+String.valueOf(ar.size())+"групп и "+
				// String.valueOf(art.size())+"товаров.",
				// Toast.LENGTH_SHORT).show();
			}
}
	}).start();
}	
	
}
