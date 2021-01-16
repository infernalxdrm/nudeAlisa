package chan_2.JsonComponents;

public class thread {
    public String comment;
    public int lasthit;
    public String date;
    public String name;
    public String parent;
    public String num;
    public int posts_count;
    public float score;
    public String subject;
    public int timestamp;
    public int views;
    public file[] files;

    public String getComment() {
        return comment;
    }

    public int getLasthit() {
        return lasthit;
    }

    public String getNum() {
        return num;
    }

    public int getPosts_count() {
        return posts_count;
    }

    public float getScore() {
        return score;
    }

    public String getSubject() {
        return subject;
    }

    public int getTimestamp() {
        return timestamp;
    }

    public int getViews() {
        return views;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getParent() {
        return parent;
    }

    public file[] getFiles() {
        return files;
    }

    @Override
    public String toString() {
        return "thread{" +
                "comment='" + comment + '\'' +
                ", lasthit=" + lasthit +
                ", num='" + num + '\'' +
                ", posts_count=" + posts_count +
                ", score=" + score +
                ", subject='" + subject + '\'' +
                ", timestamp=" + timestamp +
                ", views=" + views +
                '}';
    }
}
