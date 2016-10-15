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
package alberapps.java.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.http.HttpResponseCache;
import android.os.Build;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Acceso a la red. Conexiones para distintas versiones de Android
 */
public class Conectividad {

    public static final String USER_AGENT = "CDORario/1.0 (http://alberapps.blogspot.com; alberapps@gmail.com)";

    public static final int CONNECTION_TIMEOUT_HTTP = 5 * 1000;

    public static final int READ_TIMEOUT_HTTP = 5 * 1000;


    /**
     * Conexion con get y codificacion ISO
     *
     * @param urlGet
     * @param usarCache
     * @return string
     */
    public static String conexionGet(String urlGet, boolean usarCache, String userAgent, boolean utf8) throws Exception {

        // Abrir Conexion
        HttpURLConnection urlConnection = null;

        String datos = null;

        InputStream in = null;

        try {

            // Crear url
            URL url = new URL(urlGet);

            urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setReadTimeout(READ_TIMEOUT_HTTP);
            urlConnection.setConnectTimeout(CONNECTION_TIMEOUT_HTTP);

            urlConnection.setRequestMethod("GET");
            urlConnection.setDoInput(true);

            if (userAgent == null) {
                urlConnection.setRequestProperty("User-Agent", USER_AGENT);
            } else {
                urlConnection.setRequestProperty("User-Agent", userAgent);
            }

            if (!usarCache) {
                urlConnection.addRequestProperty("Cache-Control", "no-cache");
                Log.d("CONEXION", "Sin cache");
            } else {
                Log.d("CONEXION", "Con cache");
            }

            in = new BufferedInputStream(urlConnection.getInputStream());

            if (utf8) {
                datos = Utilidades.obtenerStringDeStreamUTF8(in);
            } else {
                datos = Utilidades.obtenerStringDeStream(in);
            }

        } catch (IOException e) {

            e.printStackTrace();

            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (in != null) {
                in.close();
            }

            throw new Exception("Error al acceder al servicio");

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }

            if (in != null) {
                in.close();
            }
        }

        return datos;

    }

    /**
     * Devuelve inputstream
     *
     * @param urlGet
     * @return stream
     */
    public static InputStream conexionGetIsoStream(String urlGet) throws Exception {

        return Utilidades.stringToStreamIso(conexionGet(urlGet, true, null, false));

    }

    /**
     * @param urlGet
     * @return
     */
    public static String conexionGetIsoString(String urlGet) throws Exception {

        return conexionGet(urlGet, true, null, false);

    }


    public static InputStream conexionGetUtf8Stream(String urlGet) throws Exception {

        return Utilidades.stringToStreamIso(conexionGet(urlGet, true, null, true));

    }

    /**
     * @param urlGet
     * @return
     */
    public static String conexionGetUtf8String(String urlGet, Boolean usarCache) throws Exception {

        return conexionGet(urlGet, usarCache, null, true);

    }

    public static String conexionGetUtf8StringUserAgent(String urlGet, Boolean usarCache, String userAgent) throws Exception {

        return conexionGet(urlGet, usarCache, userAgent, true);

    }

    /**
     * Conexion indicando si hay que usar cache
     *
     * @param urlGet
     * @param usarCache
     * @return stream
     */
    public static InputStream conexionGetIsoStream(String urlGet, Boolean usarCache, String userAgent) throws Exception {

        return Utilidades.stringToStreamIso(conexionGet(urlGet, usarCache, userAgent, false));

    }


    /**
     * Devuelve inputstream sin usar cache en conexion
     *
     * @param urlGet
     * @return stream
     */
    public static InputStream conexionGetIsoStreamNoCache(String urlGet) throws Exception {

        return Utilidades.stringToStreamIso(conexionGet(urlGet, false, null, false));

    }


    /**
     * Activar el uso de cache si la plataforma lo permite
     *
     * @param context
     */
    @SuppressLint("NewApi")
    public static void activarCache(Context context, SharedPreferences preferencias) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            boolean cacheActiva = preferencias.getBoolean("conectividad_cache", true);

            boolean cacheEliminada = preferencias.getBoolean("conectividad_cache_eliminada", false);

            if (cacheActiva) {

                // Activar la cache

                try {
                    File httpCacheDir = new File(context.getCacheDir(), "http");
                    long httpCacheSize = 10 * 1024 * 1024; // 10 MiB
                    HttpResponseCache.install(httpCacheDir, httpCacheSize);
                    Log.i("Conectividad", "Cache activa");

                    Log.d("Conectividad", "Request count: " + HttpResponseCache.getInstalled().getRequestCount());
                    Log.d("Conectividad", "Network count: " + HttpResponseCache.getInstalled().getNetworkCount());
                    Log.d("Conectividad", "Hit count: " + HttpResponseCache.getInstalled().getHitCount());

                    SharedPreferences.Editor editor = preferencias.edit();
                    editor.putBoolean("cache_eliminada", false);
                    editor.commit();

                } catch (IOException e) {
                    Log.i("Conectividad", "HTTP response cache installation failed:" + e);
                }

            } else if (!cacheEliminada) {

                // Si se ha decidido eliminar la cache

                HttpResponseCache cache = HttpResponseCache.getInstalled();

                if (cache != null) {

                    try {
                        cache.delete();

                        SharedPreferences.Editor editor = preferencias.edit();
                        editor.putBoolean("cache_eliminada", true);
                        editor.commit();

                        Log.i("Conectividad", "Cache eliminada");

                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

            }

        }

    }

    /**
     * Asegurar guardado de la cache al salir
     */
    @SuppressLint("NewApi")
    public static void flushCache() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {

            HttpResponseCache cache = HttpResponseCache.getInstalled();
            if (cache != null) {
                cache.flush();

                Log.i("Conectividad", "flush de cache");

            }
        }

    }

}
