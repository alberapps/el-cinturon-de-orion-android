/**
 * El Cinturon de Orion - Inoformacion y podcast del programa de radio El Cinturon de Orion de Radio San Vicente
 * Copyright © 2016 Alberto Montiel
 * <p/>
 * <p/>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p/>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p/>
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package alberapps.android.cinturondeorion.noticias.detalle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import alberapps.android.cinturondeorion.R;
import alberapps.java.noticias.NoticiasCDO;
import alberapps.java.noticias.rss.NoticiaRss;
import alberapps.java.util.Utilidades;

public class DetalleNoticiaActivity extends AppCompatActivity {

    public static String EXTRA_NOTICIA;

    private NoticiaRss noticia;

    private ShareActionProvider mShareProvider = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_noticia);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                //      .setAction("Action", null).show();

                shareNoticia(noticia);


            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noticia = (NoticiaRss) getIntent().getSerializableExtra(EXTRA_NOTICIA);

        TextView detalleNoticia = (TextView) findViewById(R.id.texto_detalle_noticia);


        UrlImageParser ip = new UrlImageParser(detalleNoticia, this);


        String contenido = "<h1>" + noticia.getTitle() + "</h1>" + noticia.getContentEncoded();


        detalleNoticia.setText(Html.fromHtml(contenido, ip, null));
        detalleNoticia.setMovementMethod(LinkMovementMethod.getInstance());


        setTitle(getString(R.string.detalle_cdo) + " " + Utilidades.getDayMonthString(Utilidades.getFechaDateRss(noticia.getPubDate())));

        /*AppCompatImageView view = (AppCompatImageView)findViewById(R.id.cabecera_imagen);

        UrlImageParser im = new UrlImageParser(this);

        Drawable img = im.getDrawable(noticia.getUrlPrimeraImagen());

        view.setImageDrawable(img);
        */


    }

    private void shareNoticia(NoticiaRss noticia) {
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_noticia) + " " + noticia.getTitle() + " " + noticia.getGuid());

        startActivity(myShareIntent);
    }

    public class URLDrawable extends BitmapDrawable {

        protected Drawable drawable;

        @Override
        public void draw(Canvas canvas) {

            if (drawable != null) {
                drawable.draw(canvas);
            }

        }
    }

    public class UrlImageParser implements Html.ImageGetter {

        Context context;
        View container;

        public UrlImageParser(View v, Context c) {
            this.context = c;
            this.container = v;
        }

        public UrlImageParser(Context c) {
            this.context = c;

        }

        @Override
        public Drawable getDrawable(String source) {

            URLDrawable urlDrawable = new URLDrawable();

            ImageGetterAsyncTask at = new ImageGetterAsyncTask(urlDrawable);

            at.execute(source);

            return urlDrawable;
        }

        // Using an AsyncTask to load the slow images in a
        // background thread
        public class ImageGetterAsyncTask extends AsyncTask<String, Void, Drawable> {

            URLDrawable urlDrawable;

            public ImageGetterAsyncTask(URLDrawable drawable) {
                this.urlDrawable = drawable;
            }

            @Override
            protected Drawable doInBackground(String... params) {
                String source = params[0];

                Drawable imagenRecuperada = null;

                try {
                    imagenRecuperada = NoticiasCDO.recuperaImagenDrawable(source, DetalleNoticiaActivity.this);

                    Log.d("Detalle noticia", "Imagen recuperada: " + source);


                } catch (Exception e) {
                    return null;
                }

                return imagenRecuperada;
            }

            @Override
            protected void onPostExecute(Drawable result) {

                if (result != null) {

                    urlDrawable.setBounds(0, 0, 0 + result.getIntrinsicWidth(), 0 + result.getIntrinsicHeight());

                    urlDrawable.setLevel(1);

                    urlDrawable.drawable = result;

                    if (container != null) {

                        CharSequence t = ((TextView) container).getText();

                        ((TextView) container).setText(t);

                    }
                }

            }
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_detalle_noticia, menu);

        return super.onCreateOptionsMenu(menu);
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_detalle_noticia, menu);

        MenuItem item = menu.findItem(R.id.menu_item_share);

        mShareProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        sharePrincipal();

        return true;
    }

    private void setShareIntent(Intent shareIntent){
        if (mShareProvider != null){
            mShareProvider.setShareIntent(shareIntent);
        }
    }

    private void sharePrincipal(){
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_principal));

        setShareIntent(myShareIntent);
    }
*/
}
