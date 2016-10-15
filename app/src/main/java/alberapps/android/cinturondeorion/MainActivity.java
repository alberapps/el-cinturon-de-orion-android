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
package alberapps.android.cinturondeorion;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import alberapps.android.cinturondeorion.appinfo.AppInfoActivity;
import alberapps.android.cinturondeorion.noticias.NoticiasFragment;
import alberapps.android.cinturondeorion.noticias.detalle.DetalleNoticiaActivity;
import alberapps.android.cinturondeorion.preferencias.SettingsActivity;
import alberapps.android.cinturondeorion.uamp.ui.MusicPlayerActivity;
import alberapps.java.noticias.rss.NoticiaRss;
import alberapps.java.util.Conectividad;
import alberapps.java.util.Utilidades;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, NoticiasFragment.OnListFragmentInteractionListener {

    private SharedPreferences preferencias = null;

    private ShareActionProvider mShareProvider = null;

    /**
     * The {@link Tracker} used to record screen views.
     */
    private Tracker mTracker;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        //PreferenceManager.setDefaultValues(this, R.xml.pref_general, false);
        preferencias = PreferenceManager.getDefaultSharedPreferences(this);

        Conectividad.activarCache(this, preferencias);


        if (preferencias.getBoolean("analytics_on", false)) {

            // Activar
            GoogleAnalytics.getInstance(getApplicationContext()).setAppOptOut(false);

            // [START shared_tracker]
            // Obtain the shared Tracker instance.
            CinturonDeOrionApplication application = (CinturonDeOrionApplication) getApplication();
            mTracker = application.getDefaultTracker();
            // [END shared_tracker]


            sendScreen("Noticias - Principal");

            Log.d("PRINCIPAL", "Analytics activo");

        } else {
            GoogleAnalytics.getInstance(getApplicationContext()).setAppOptOut(true);

            Log.d("PRINCIPAL", "Analytics inactivo");

        }



    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mTracker == null && preferencias.getBoolean("analytics_on", false)) {
            // [START shared_tracker]
            // Obtain the shared Tracker instance.
            CinturonDeOrionApplication application = (CinturonDeOrionApplication) getApplication();
            mTracker = application.getDefaultTracker();
            // [END shared_tracker]
        }

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        //MenuItem item = menu.findItem(R.id.menu_item_share);

        //mShareProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(item);

        //sharePrincipal();

        return true;
    }

    private void setShareIntent(Intent shareIntent) {
        if (mShareProvider != null) {
            mShareProvider.setShareIntent(shareIntent);
        }
    }

    private void sharePrincipal() {
        Intent myShareIntent = new Intent(Intent.ACTION_SEND);
        myShareIntent.setType("text/plain");
        myShareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share_principal));

        //setShareIntent(myShareIntent);

        startActivity(myShareIntent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_radio) {

            sendScreen("Podcast");

            Intent podcast = new Intent(this, MusicPlayerActivity.class);
            startActivity(podcast);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_noticias_portada) {

            mostrarNoticias(0);

            sendScreen("Noticias - Principal");


        } else if (id == R.id.nav_noticias_programas) {

            mostrarNoticias(1);

            sendScreen("Noticias - Programas");


        } else if (id == R.id.nav_podcast) {

            sendScreen("Podcast");

            item.setChecked(false);

            sendScreen("Podcasts - Principal");

            Intent podcast = new Intent(this, MusicPlayerActivity.class);
            startActivity(podcast);

        } /*else if (id == R.id.nav_el_programa) {

            Intent intent = new Intent(this, InfoActivity.class);
            startActivity(intent);

        }*/ else if (id == R.id.nav_manage) {

            sendScreen("Preferencias - Principal");

            item.setChecked(false);

            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);

        } else if (id == R.id.nav_share) {

            sendScreen("Compartir");

            item.setChecked(false);

            sharePrincipal();

        } /*else if (id == R.id.nav_send) {

        }*/ else if (id == R.id.nav_blog) {

            sendScreen("Link - Blog");

            item.setChecked(false);

            Utilidades.openWebPage(this, "http://www.cinturondeorion.com/");

        } else if (id == R.id.nav_twitter) {

            sendScreen("Link - Twitter");

            item.setChecked(false);

            Utilidades.openWebPage(this, "https://twitter.com/cinturondorion");

        } else if (id == R.id.nav_facebook) {

            sendScreen("Link - Facebook");

            item.setChecked(false);

            Utilidades.openWebPage(this, "https://www.facebook.com/El-Cintur%C3%B3n-de-Ori%C3%B3n-361530634013075/");

        } else if (id == R.id.nav_appinfo) {

            sendScreen("Appinfo");

            item.setChecked(false);

            Intent intent = new Intent(this, AppInfoActivity.class);
            startActivity(intent);

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void mostrarNoticias(int filtro) {

        NoticiasFragment fragment = (NoticiasFragment) getSupportFragmentManager().findFragmentById(R.id.noticias_fragment);

        fragment.consultarNoticias(filtro);


    }

    @Override
    public void onListFragmentInteraction(NoticiaRss item) {

        //Noticia seleccionada

        Intent intent = new Intent(this, DetalleNoticiaActivity.class);
        intent.putExtra(DetalleNoticiaActivity.EXTRA_NOTICIA, item);
        startActivity(intent);


    }

    @Override
    protected void onStop() {

        Conectividad.flushCache();

        super.onStop();
    }


    private void sendScreen(String name) {

        if(mTracker != null) {
            // [START screen_view_hit]
            Log.i(TAG, "Setting screen name: " + name);
            mTracker.setScreenName("CDO~" + name);
            mTracker.send(new HitBuilders.ScreenViewBuilder().build());
            // [END screen_view_hit]
        }
    }

}
