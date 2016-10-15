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
package alberapps.java.podcast;

import android.net.Uri;

import alberapps.java.noticias.rss.Noticias;
import alberapps.java.noticias.rss.ParserXML;

public class PodcastCDO {


    public Noticias getPodcastFeed() {

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.scheme("http").authority("www.cinturondeorion.com").appendPath("podcast").appendPath("feed.xml");

            Uri urlNoticias = builder.build();

            Noticias noticias = null;

            ParserXML parser = new ParserXML();

            noticias = parser.parserNoticias(urlNoticias.toString());


            return noticias;

        } catch (Exception e) {
            return null;
        }

    }


}
