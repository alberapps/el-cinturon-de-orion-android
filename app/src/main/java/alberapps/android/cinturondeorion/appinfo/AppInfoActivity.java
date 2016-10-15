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
package alberapps.android.cinturondeorion.appinfo;

import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import alberapps.android.cinturondeorion.R;
import alberapps.java.util.Utilidades;


/**
 * Informacion de la app
 */
public class AppInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.infoapp);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);

            //actionBar.setElevation(0);

        }


        ImageView botonGpl = (ImageView) findViewById(R.id.boton_gpl);
        assert botonGpl != null;
        botonGpl.setOnClickListener(new ImageView.OnClickListener() {
            public void onClick(View arg0) {

                Utilidades.openWebPage(AppInfoActivity.this, "http://www.gnu.org/licenses/gpl-3.0-standalone.html");

            }
        });


        ImageView botonCDO = (ImageView) findViewById(R.id.imageLogoCDO);
        assert botonCDO != null;
        botonCDO.setOnClickListener(new ImageButton.OnClickListener() {
            public void onClick(View arg0) {

                Utilidades.openWebPage(AppInfoActivity.this, "http://www.cinturondeorion.com");

            }
        });


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

    /*
    @Override
    protected void onStart() {

        super.onStart();
        GoogleAnalytics.getInstance(this).reportActivityStart(this);

    }

    @Override
    protected void onStop() {

        GoogleAnalytics.getInstance(this).reportActivityStop(this);
        super.onStop();

    }
    */

}
