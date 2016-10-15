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
package alberapps.android.cinturondeorion.tasks;

import android.os.AsyncTask;

import java.util.List;

import alberapps.java.noticias.NoticiasCDO;
import alberapps.java.noticias.rss.NoticiaRss;

/**
 * Tarea asincrona que se encarga de consultar las noticias rss del tram
 */
public class LoadNoticiasRssAsyncTask extends AsyncTask<Object, Void, List<NoticiaRss>> {


    public interface LoadNoticiasRssAsyncTaskResponder {
        public void noticiasRssLoaded(List<NoticiaRss> noticias);
    }

    private LoadNoticiasRssAsyncTaskResponder responder;


    public LoadNoticiasRssAsyncTask(LoadNoticiasRssAsyncTaskResponder responder) {
        this.responder = responder;
    }


    @Override
    protected List<NoticiaRss> doInBackground(Object... datos) {
        List<NoticiaRss> noticiasList = null;

        Integer filtro = (Integer)datos[0];

        try {

            NoticiasCDO noticiasCDO = new NoticiasCDO();

            noticiasList = noticiasCDO.getNoticias(filtro).getNoticiasList();

        } catch (Exception e) {
            return null;
        }

        return noticiasList;
    }


    @Override
    protected void onPostExecute(List<NoticiaRss> result) {
        if (responder != null) {
            responder.noticiasRssLoaded(result);
        }


    }

}
