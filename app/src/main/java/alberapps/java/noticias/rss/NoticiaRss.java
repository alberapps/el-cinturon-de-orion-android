/**
 *  TiempoBus - Informacion sobre tiempos de paso de autobuses en Alicante
 *  Copyright (C) 2014 Alberto Montiel
 *
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package alberapps.java.noticias.rss;

import java.io.Serializable;

public class NoticiaRss implements Serializable{

    private String title;

    private String description;

    private String comments;

    private String pubDate;

    private String contentEncoded;

    private String commentRss;

    private String slashComments;

    private String link;

    private String urlPrimeraImagen;

    //podcast

    private String guid;

    private String author;

    private String authorShort;

    private String duration;

    public String getAuthorShort() {
        return authorShort;
    }

    public void setAuthorShort(String authorShort) {
        this.authorShort = authorShort;
    }

    public String getCommentRss() {
        return commentRss;
    }

    public void setCommentRss(String commentRss) {
        this.commentRss = commentRss;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getContentEncoded() {
        return contentEncoded;
    }

    public void setContentEncoded(String contentEncoded) {
        this.contentEncoded = contentEncoded;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getSlashComments() {
        return slashComments;
    }

    public void setSlashComments(String slashComments) {
        this.slashComments = slashComments;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrlPrimeraImagen() {
        return urlPrimeraImagen;
    }

    public void setUrlPrimeraImagen(String urlPrimeraImagen) {
        this.urlPrimeraImagen = urlPrimeraImagen;
    }


    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }
}
