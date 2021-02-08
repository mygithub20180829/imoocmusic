package models;

import io.realm.RealmObject;

public class MusicModel extends RealmObject {
    /**
     * "musicId": "101",
     *           "name": "Nostalgic Piano",
     *           "poster": "http://res.lgdsunday.club/poster-1.png",
     *           "path": "http://res.lgdsunday.club/Nostalgic%20Piano.mp3",
     *           "author": "Rafael Krux"
     */
    private String musicId;
    private String name;
    private String poster;
    private String path;
    private String author;

    public void setMusicId(String musicId) {
        this.musicId = musicId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getMusicId() {
        return musicId;
    }

    public String getName() {
        return name;
    }

    public String getPoster() {
        return poster;
    }

    public String getPath() {
        return path;
    }

    public String getAuthor() {
        return author;
    }


}
