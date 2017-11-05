package com.github.andriykuba.mtgwallpapers;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.widget.TextView;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        Toolbar.initChild(this);

        TextView aboutTextView = (TextView) findViewById(R.id.info_text);

        Spanned aboutText = Html.fromHtml(getString(R.string.info_text));
        aboutTextView.setText(aboutText);
        aboutTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }
}
