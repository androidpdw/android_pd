package com.xiawa.read.activity;

import android.os.Bundle;
import android.widget.TextView;

import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.xiawa.read.R;
import com.xiawa.read.bean.BookRankItem;

public class BookIntroductActivity extends BaseActivity {
	
	private BookRankItem bookDetailItem;
	@ViewInject(R.id.tv_book_name)
	private TextView tv_book_name;
	
	@ViewInject(R.id.tv_book_ISBN)
	private TextView tv_book_ISBN;
	
	@ViewInject(R.id.tv_book_author)
	private TextView tv_book_author;
	
	@ViewInject(R.id.tv_book_publish)
	private TextView tv_book_publish;
	
	@ViewInject(R.id.tv_book_publish_time)
	private TextView tv_book_publish_time;
	
	@ViewInject(R.id.tv_book_count)
	private TextView tv_book_count;
	
	@ViewInject(R.id.tv_book_page)
	private TextView tv_book_page;
	
	@ViewInject(R.id.tv_book_class)
	private TextView tv_book_class;
	
	@ViewInject(R.id.tv_book_text)
	private TextView tv_book_text;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_book_introduct);
		bookDetailItem = (BookRankItem) getIntent().getSerializableExtra("bookItem");
		ViewUtils.inject(this);
		setHeaderTitle("图书详情");
		tv_book_author.setText(bookDetailItem.author);
		tv_book_name.setText(bookDetailItem.bookname);
		tv_book_ISBN.setText(bookDetailItem.isbn);
		tv_book_publish.setText(bookDetailItem.publisher);
		tv_book_text.setText(bookDetailItem.text);
	}
}
